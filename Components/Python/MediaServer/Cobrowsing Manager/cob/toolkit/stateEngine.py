import os
import shutil
import demjson
import threading
from toolkit import osconfig
from distutils import dir_util

import logging
import toolkit.logManager as logManager

class sl_stateMachine(object):
    
    jsonObj         = None
    stateLock       = None
    stateMachine    = None
    
    def __init__(self):
        
        #   stateMachine    = {dimdimID: {bmAction: bookmarkAction, currentResource: resourceID, locked : False, resources : {RESOURCEDICT}}, ...}
        #   RESOURCEDICT    = {'regular' : {resourceID : {lastPage: pageID, currentPage: pageID, pages:{PAGEDICT}}}, 'preloaded' : \
        #                      {resourceID: {lastPage: pageID, currentPage: pageID, pages:{PAGEDICT}}}}
        #   PAGEDICT        = {pageID : {INFODICT}, ...}
        #   INFODICT        = {url : associatedURL, events : {EVENTDICT}}
        #   EVENTDICT       = {eventType : {EIDICT}}      -  only one entry per eventType
        #   EIDICT          = may vary depending on event
        #
        #   Note: The stateMachine does not care about the content itself. All it tries to do, is maintain sanity
        #   Note 2 : Due to the nature of the synchrolive application, every 'set' / 'update' is to the currently shared
        #           resource in a given meeting. 
        #   
        
        self.stateMachine   = {}
        self.jsonObj        = demjson.JSON()
        self.stateLock      = threading.BoundedSemaphore(1)
        self.importStateMachine()
        return
    
    def getMeeting(self, dimdimID):
        return self.stateMachine.get(dimdimID)
    
    def getResourceByID(self, dimdimID, resourceID):
        meeting = self.getMeeting(dimdimID)
        resourceList = meeting.get('resources')
        regularResources = resourceList.get('regular')
        preloadedResources = resourceList.get('preloaded')
        
        if regularResources.has_key(resourceID):
            return regularResources.get(resourceID)
        return preloadedResources.get(resourceID)
    
    def getCurrentResource(self, dimdimID):
        meeting = self.getMeeting(dimdimID)
        if not meeting:
            return None
        
        currentResource = meeting.get('currentResource')
        if not currentResource:
            return None
        
        # if currentResource is valid, there *must* be a resourceInfo associated
        # stateMachine should take care of this
        
        return self.getResourceByID(dimdimID, currentResource)
    
    def getResourceType(self, dimdimID, resourceID):
        meeting = self.getMeeting(dimdimID)
        resourceList = meeting.get('resources')
        regularList = resourceList.get('regular')
        preloadedList = resourceList.get('preloaded')
        if regularList.has_key(resourceID):
            return 'regular'
        return 'preloaded'
    
    def getCurrentResourceType(self, dimdimID):
        meeting = self.getMeeting(dimdimID)
        if not meeting:
            return None
        currentResourceID = meeting.get('currentResource')
        return self.getResourceType(dimdimID, currentResourceID)
    
    def getPageByNum(self, dimdimID, pageNum):
        resource = self.getCurrentResource(dimdimID)
        if not resource:
            return None
        
        pageList = resource.get('pages')
        if not pageList:
            return None
        
        return pageList.get(pageNum)
    
    def getCurrentPage(self, dimdimID):
        resource = self.getCurrentResource(dimdimID)
        if not resource:
            return None
        
        currentPage = resource.get('currentPage')
        if currentPage < 0:
            return None
        
        # if currentPage is valid, there *must* be a pageInfo associated
        # stateMachine should take care of this
        
        pageList = resource.get('pages')
        return pageList.get(currentPage)
    
    def getEventsOfPage(self, dimdimID, pageNum):
        page = self.getPageByNum(dimdimID, pageNum)
        if not page:
            return None
        
        # page is always associated with 'events', even if it is just a {}
        return page.get('events')
    
    def getCurrentEvents(self, dimdimID):  
        page = self.getCurrentPage(dimdimID)
        if not page:
            return None
        
        # page is always associated with 'events', even if it is just a {}
        return page.get('events')
    
    def registerMeeting(self, dimdimID, bookmarkAction):
        # FORMAT: {dimdimID: {bmAction: bookmarkAction, currentResource: resourceID, locked : False, resources : {RESOURCEDICT}}}
        ## bmAction is required for bookmarking functionality. stateMachine doesn't care what the action is.
        ## The caller should make sure it is valid and handle it appropriately.
        ## Default Values : currentResource : None, locked : False, resources : {'regular':{}, 'preloaded':{}}
        
        # NOTE: registerMeeting is for external usage only. importStateEngine shouldn't use registerMeeting
        # If there is a meeting that is already registered, registerMeeting ignores any further registrations
        
        if self.getMeeting(dimdimID):
            return
        
        meetingPath = os.path.join(os.path.join(osconfig.cobArchive(), dimdimID))
        if os.path.isdir(meetingPath):
            try:
                shutil.rmtree(meetingPath)
            except:
                pass            
        os.makedirs(meetingPath)
        
        self.stateLock.acquire()        
        meeting = {dimdimID : {'bmAction' : bookmarkAction, 'currentResource' : None, 'locked' : False, 'resources' : {'regular':{}, 'preloaded':{}}}}
        self.stateMachine.update(meeting)        
        self.stateLock.release()
        
        self.exportStateMachine(dimdimID)
        logManager.log('meeting ' + dimdimID + ' registered', logging.INFO)
        return
    
    def getLockState(self, dimdimID):
        meeting = self.getMeeting(dimdimID)
        return {'locked' : meeting.get('locked')}
    
    def updateLockState(self, dimdimID, lockState):
        # lockState is expected to be a dict object
        # lock is a meeting level property
        
        meeting = self.getMeeting(dimdimID)
        if meeting:
            self.stateLock.acquire()
            meeting.update(lockState)
            self.stateLock.release()
            self.exportStateMachine(dimdimID)
        
        return
    
    def registerResource(self, dimdimID, resourceID, resourceType):
        # FORMAT: resourceList is of the form {'regular':{RESOURCE1, RESOURCE2, ...}, 'preloaded':{RESOURCE3, RESOURCE4, ....}}
        # RESOURCE is of the form {resourceID : {lastPage: pageID, currentPage: pageID, pages:{PAGEDICT}}}
        ## Default values for RESOURCE: lastPage = -1, currentPage = -1 and {} as pages
        ## resourceType has to be 'regular' or 'preloaded'.
        
        # If a resource is already registered, we return immediately.
        # registerResource *must* not be called before registerMeeting. State Machine is not going to expect orphan resources
        # State Machine also doesn't expect any resourceType other than 'regular' or 'preloaded'.
        
        if self.getResourceByID(dimdimID, resourceID):
            return
        
        meeting = self.getMeeting(dimdimID)
        resourceList = meeting.get('resources')
        typeList = resourceList.get(resourceType)
        resource = {resourceID : {'lastPage' : -1, 'currentPage' : -1, 'pages' : {}}}
        
        self.stateLock.acquire()
        typeList.update(resource)
        meeting.update({'currentResource' : resourceID})        
        self.stateLock.release();
        
        self.exportStateMachine(dimdimID)
        logManager.log('resource ' + resourceID + ' registered under meeting ' + dimdimID, logging.INFO)
        return
    
    def resetCurrentResource(self, dimdimID):
        # simply sets currentPage = 0
        
        resource = self.getCurrentResource(dimdimID)
        self.stateLock.acquire()
        resource.update({'currentPage' : 0})
        self.stateLock.release()
        return
        
    
    def registerPage(self, dimdimID, associatedURL, events = {}):
        # associatedURL should be a valid http url
        # events is expected to be a dict type and can have multiple event types.
        # this routine creates a new page and updates the currentPage for the currentResource in dimdimID
        
        resource = self.getCurrentResource(dimdimID)
        pageList = resource.get('pages')
        currentPage = resource.get('currentPage') + 1
        
        page = {'url' : associatedURL, 'events' : events}
        
        self.stateLock.acquire()
        pageList.update({currentPage : page})
        resource.update({'lastPage' : currentPage, 'currentPage' : currentPage})        
        self.stateLock.release()
        
        self.exportStateMachine(dimdimID)
        logManager.log('url ' + associatedURL + ' has been added to current resource of meeting ' + dimdimID, logging.INFO)
        return currentPage
        
    def updateCurrentEvents(self, dimdimID, events):
        # events is expected to be a dict type and can have multiple event types.
        # this routine only updates an existing entry in the stateMachine.
        
        page = self.getCurrentPage(dimdimID)
        
        self.stateLock.acquire()
        if page:
            page.update({'events' : events})
        self.stateLock.release()
            
        self.exportStateMachine(dimdimID)
        return
    
    def navigateToPage(self, dimdimID, action):
        # no convoluted logic taking care of non-existent pages / resources
        # action can either be 'back' or 'forward'
        
        resource = self.getCurrentResource(dimdimID)
        currentPage = resource.get('currentPage')
        lastPage = resource.get('lastPage')
        
        if action == 'back':
            if currentPage > 0:
                currentPage = currentPage - 1
        else:
            if currentPage < lastPage:
                currentPage = currentPage + 1
                
        self.stateLock.acquire()                
        resource.update({'currentPage': currentPage})        
        self.stateLock.release()
        
        self.exportStateMachine(dimdimID)
        return currentPage
    
    def navigateToResource(self, dimdimID, resourceID):
        
        resource = self.getResourceByID(dimdimID, resourceID)
        if not resource:
            return False
        
        meeting = self.getMeeting(dimdimID)
        
        self.stateLock.acquire()
        meeting.update({'currentResource' : resourceID})
        self.stateLock.release()
        
        self.exportStateMachine(dimdimID)
        return
    
    def unregisterMeeting(self, dimdimID):
        
        self.stateLock.acquire()
        self.stateMachine.pop(dimdimID)
        self.stateLock.release()
    
    def unregisterResource(self, dimdimID, resourceID):
        
        meeting  = self.getMeeting(dimdimID)
        resource = self.getResourceByID(dimdimID, resourceID)
        if not resource:
            return
        
        resourceList = meeting.get('resources')
        regularList = resourceList.get('regular')
        preloadedList = resourceList.get('preloaded')
        
        self.stateLock.acquire()        
        if meeting.get('currentResource') == resourceID:
            meeting.update({'currentResource' : None})        
        if regularList.has_key(resourceID):
            regularList.pop(resourceID)
            resourcePath = os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'regular'), resourceID)
        else:
            preloadedList.pop(resourceID)
            resourcePath = os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'preloaded'), resourceID)        
        try:
            shutil.rmtree(resourcePath)
        except:
            pass        
        self.stateLock.release()
        
        self.exportStateMachine(dimdimID)
        return
        
    
    def handleMeetingClose(self, dimdimID):
        # when a meeting closes, all 'regular' resources
        # should be cleaned up both from the disk and the stateMachine
        
        logManager.log('meeting ' + dimdimID + ' is getting closed', logging.INFO)
        
        meeting = self.getMeeting(dimdimID)
        if not meeting:
            return
        
        resourceList = meeting.get('resources')
        preloadedList = resourceList.get('preloaded')
        if len(preloadedList) == 0:
            # get rid of the entire meeting record
            self.unregisterMeeting(dimdimID)
            try:
                # get rid of meta data
                os.remove(os.path.join(os.path.join(osconfig.cobArchive(), 'META-INF'), dimdimID))
            except:
                pass
            try:
                # get rid of the files
                shutil.rmtree(os.path.join(osconfig.cobArchive(), dimdimID))
            except:
                pass
            return
        
        self.stateLock.acquire()
        resourceList.update({'regular':{}})
        self.stateLock.release()
        
        try:
            # delete the 'regular' folder
            shutil.rmtree(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'regular'))
        except:
            pass

        self.exportStateMachine(dimdimID)
        return
    
    def bookmarkCurrentPage(self, dimdimID, newResourceID):
        
        meeting = self.getMeeting(dimdimID)
        resourceList = meeting.get('resources')
        
        currentResourceID = meeting.get('currentResource')
        currentResourceCopy = self.getCurrentResource(dimdimID)
        currentResourceType = self.getResourceType(dimdimID, currentResourceID)
        
        currentPageNum = currentResourceCopy.get('currentPage')
        currentPageCopy = self.getCurrentPage(dimdimID)
        
        newResource = {'currentPage':0, 'lastPage':0, 'pages': {0:currentPageCopy}}
        
        sourcePath = os.path.join(os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), \
                                               currentResourceType), currentResourceID), str(currentPageNum))
        destPath = os.path.join(os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), \
                                               currentResourceType), newResourceID), '0')
        try:
            shutil.rmtree(destPath)
        except:
            pass
        
        dir_util.copy_tree(sourcePath, destPath)
        typeList = resourceList.get(currentResourceType)
        
        self.stateLock.acquire()
        typeList.update({newResourceID : newResource})
        self.exportStateMachine(dimdimID)
        self.stateLock.release()
        
        return
    
    def trimResourceToCurrentPage(self, dimdimID):
        # this needs to be used if the user goes back to a previous page
        # and then decides moves to a different page, it would all later pages invalid
        # so before letting him move on, we cleanup all pages > currentPage
        
        # this is only ever required on the current shared resource.
        
        meeting = self.getMeeting(dimdimID)
        resource = self.getCurrentResource(dimdimID)
        resourceID = meeting.get('currentResource')
        lastPage = resource.get('lastPage')
        currentPage = resource.get('currentPage')
        
        pageList = resource.get('pages')
        
        for pageNum in range(currentPage + 1, lastPage + 1):
            pageList.pop(pageNum)
            resourcePath = os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), self.getResourceType(dimdimID, resourceID)), resourceID)
            pagePath = os.path.join(resourcePath, str(pageNum))
            try:
                shutil.rmtree(pagePath)
            except:
                pass
        
        resource.update({'lastPage' : currentPage})
        self.exportStateMachine(dimdimID)
        return
    
    def exportStateMachine(self, dimdimID):
        
        metaDir = os.path.join(osconfig.cobArchive(), 'META-INF')
        if not os.path.isdir(metaDir):
            os.makedirs(metaDir)
        metaFileLocation = os.path.join(metaDir, dimdimID)
        meeting = self.getMeeting(dimdimID)
        metaFileHandle = open(metaFileLocation, 'wb')
        metaFileHandle.write(self.jsonObj.encode(meeting))
        metaFileHandle.close()
        return    
    
    def importStateMachine(self):
        
        metaDir = os.path.join(osconfig.cobArchive(), 'META-INF')
        if not os.path.isdir(metaDir):
            return
        dimdimIDList = os.listdir(metaDir)
        for dimdimID in dimdimIDList:
            try:
                logManager.log('Detected meeting ' + dimdimID, logging.INFO)
                metaFileLocation = os.path.join(metaDir, dimdimID)
                metaFileHandle = open(metaFileLocation, 'rb')
                meeting = self.jsonObj.decode(metaFileHandle.read())
                metaFileHandle.close()
                self.stateMachine.update({dimdimID : meeting})
            except:
                logManager.log('Exception in importing state information of meeting ' + dimdimID, logging.WARN)    
                pass
        return