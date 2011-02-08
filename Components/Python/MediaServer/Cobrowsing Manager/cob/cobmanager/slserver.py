import os
import re
import urllib
import string
import shutil
import random
from distutils import dir_util

import demjson
import urlparse

from toolkit import osconfig
from toolkit import contentMgr
from toolkit.jsonhelper import jsonObject
from toolkit.curlhelper import CurlWrapper
from toolkit.stateEngine import sl_stateMachine

import logging
import toolkit.logManager as logManager

class COBServer(object):
    
    jsonObj = None
    curlHandle = None
    stateEngine = None
    
    attendeeCacheBuffer = None
    presenterCacheBuffer = None
    
    def __init__(self):
    
        self.jsonObj = demjson.JSON()
        self.curlHandle = CurlWrapper()
        self.stateEngine = sl_stateMachine()
        
        # fill up cache buffer
        presenterCacheHandle = open(os.path.join(osconfig.cobArchive(), 'presenter.template'), 'r')
        self.presenterCacheBuffer = presenterCacheHandle.read()
        presenterCacheHandle.close()
        
        attendeeCacheHandle = open(os.path.join(osconfig.cobArchive(), 'attendee.template'), 'r')
        self.attendeeCacheBuffer = attendeeCacheHandle.read()
        attendeeCacheHandle.close()
        
        self.presenterCacheBuffer = string.replace(self.presenterCacheBuffer, 'DIMDIMSL_SCRIPTSERVER_URL', osconfig.scriptServerURL())
        self.attendeeCacheBuffer = string.replace(self.attendeeCacheBuffer, 'DIMDIMSL_SCRIPTSERVER_URL', osconfig.scriptServerURL())
        
        return
    
    def generateResourceID(self):
        random.seed()
        rString1 = str(random.random())
        rString1 = rString1[2:9]
        rString2 = str(random.random())
        rString2 = rString2[2:random.randint(1,8)]   
        return 'r' + rString1 + rString2
    
    def baseURL(self, originalURL):
        return string.rstrip(urlparse.urljoin(originalURL, 'dimdimsl'), 'dimdimsl')
    
    def closeMeeting(self, dimdimID):
        self.stateEngine.handleMeetingClose(dimdimID)
        
        jsonSuccess = jsonObject()
        jsonSuccess.clearResponse()
        jsonSuccess.add('result', 'true')
        jsonSuccess.add('method', 'closeMeeting')
        jsonSuccess.add('dimdimID', dimdimID)
        jsonSuccess.add('error', '7200')
        return jsonSuccess.jsonResponse()
    
    def listURLResources(self, dimdimID, bookmarkAction):
        jsonError = jsonObject()
        jsonError.clearResponse()
        jsonError.add('result', 'false')
        jsonError.add('method', 'listURLResources')
        jsonError.add('dimdimID', dimdimID)
        jsonError.add('error', '7404')
        
        meeting = self.stateEngine.getMeeting(dimdimID)
        if not meeting:
            # register the meeting first
            self.stateEngine.registerMeeting(dimdimID, bookmarkAction)
            return jsonError.jsonResponse()
        
        # update the bookmark action
        meeting.update({'bmAction' : bookmarkAction})
        
        jsonSuccess = jsonObject()
        jsonSuccess.clearResponse()
        jsonSuccess.add('result', 'true')
        jsonSuccess.add('method', 'listURLResources')
        entryJSONValue = '['
        count = 0

        resourceList = meeting.get('resources')
        regularList = resourceList.get('regular')
        preloadedList = resourceList.get('preloaded')
        noOfResources = len(regularList) + len(preloadedList)
        for (resourceID, resource) in regularList.iteritems():
            page = self.stateEngine.getCurrentPage(dimdimID)
            
            entryJSON = jsonObject()
            entryJSON.clearResponse()
            entryJSON.add('resourceID', resourceID)
            entryJSON.add('resourceType', 'regular')
            entryJSON.add('resourceURL', page.get('url'))
            entryJSONValue += entryJSON.jsonResponse()
            del entryJSON
            entryJSON = None
            
            if count < noOfResources - 1:
                entryJSONValue += ','
            count = count + 1
            
        for (resourceID, resource) in preloadedList.iteritems():
            page = self.stateEngine.getCurrentPage(dimdimID)
            
            entryJSON = jsonObject()
            entryJSON.clearResponse()
            entryJSON.add('resourceID', resourceID)
            entryJSON.add('resourceType', 'preloaded')
            entryJSON.add('resourceURL', page.get('url'))
            entryJSONValue += entryJSON.jsonResponse()
            del entryJSON
            entryJSON = None
            
            if count < noOfResources - 1:
                entryJSONValue += ','
            count = count + 1
            
        entryJSONValue += ']'

        jsonSuccess.add('resources',entryJSONValue)
        jsonSuccess.add('dimdimID', dimdimID)
        jsonSuccess.add('error', '7200')
        

        return jsonSuccess.jsonResponse()
    
    def sendEvents(self, dimdimID, roomID, sessionID, events):
        if not self.stateEngine.getMeeting(dimdimID):
            response = self.jsonObj.encode({'result' : False})
            return response.encode()
        eventObj = self.jsonObj.decode(events)
        lockState = eventObj.pop('lock')
        if lockState and (lockState.get('locked') != None):
            self.stateEngine.updateLockState(dimdimID, lockState)
        self.stateEngine.updateCurrentEvents(dimdimID, eventObj)
        response = self.jsonObj.encode({'result' : True})
        return response.encode()
    
    def bookmarkCurrentPage(self, dimdimID, roomID, sessionID):
        # bookmark can only be done on current resource, on the current page
        
        newResourceID = self.generateResourceID()
        meeting = self.stateEngine.getMeeting(dimdimID)
        if not meeting:
            response = self.jsonObj.encode({'result':False})
            return response.encode()
        page = self.stateEngine.getCurrentPage(dimdimID)
        self.stateEngine.bookmarkCurrentPage(dimdimID, newResourceID)
        response = self.jsonObj.encode({'result':True, 'resourceID':newResourceID, \
                                        'url' : urllib.quote(page.get('url')), 'bmAction' : meeting.get('bmAction')})
        return response.encode()
    
    def unregisterResource(self, dimdimID, roomID, sessionID, resourceID):
        # ideally, this should never be used.
        # we might have to use it if console cannot add a bookmarked
        # resource to the 'show items' list
        self.stateEngine.unregisterResource(dimdimID, resourceID)
        
        response = self.jsonObj.encode({'result':True})
        return response.encode()
    
    def syncToAttendeeContext(self, dimdimID, roomID, sessionID, returnType = 'json'):
        
        # Just give the current resource data and be done with it
        meeting = self.stateEngine.getMeeting(dimdimID)
        if not meeting:
            response = self.jsonObj.encode({'result':False})
            if returnType == 'json':
                return response.encode()
            return 'dimdimsl_contentAdvisory.handleSyncResponse('+response.encode()+')'
        currentResource = meeting.get('currentResource')
        resource = self.stateEngine.getCurrentResource(dimdimID)
        resourceType = self.stateEngine.getResourceType(dimdimID, currentResource)
        currentPageNum = resource.get('currentPage')
        lastPageNum = resource.get('lastPage')
        page = self.stateEngine.getCurrentPage(dimdimID)
        events = page.get('events')
        
        # add the lock event
        lockState = self.stateEngine.getLockState(dimdimID)
        if lockState and (lockState.get('locked') != None):
            events['lock'] = lockState
        
        responseMap = {}
        responseMap['result'] = True
        responseMap['resourceType'] = resourceType
        responseMap['resourceID'] = currentResource
        responseMap['pageNum'] = currentPageNum
        responseMap['lastPage'] = lastPageNum
        responseMap['page'] = page
        
        response = self.jsonObj.encode(responseMap)
        if returnType == 'json':
            return response.encode()
        
        return 'dimdimsl_contentAdvisory.handleSyncResponse('+response.encode()+')'
    
    def syncToPresenterContext(self, dimdimID, roomID, sessionID, pageNum = -1):
        
        # Presenter is a bit tricky.
        # pageNum parameter basically suggests that
        # it is the last known good configuration for the presenter.
        # so trim any resources till pageNum and then
        # sync to pageNum
        
        meeting = self.stateEngine.getMeeting(dimdimID)
        if not meeting:
            response = self.jsonObj.encode({'result':False})
            return response.encode()
        resource = self.stateEngine.getCurrentResource(dimdimID)
        if (pageNum > -1):
            if resource.get('lastPage') >= pageNum:
                resource.update({'currentPage' : pageNum})
                self.stateEngine.trimResourceToCurrentPage(dimdimID)
        
        # prepare presenter cache with current state
        resourceID = meeting.get('currentResource')
        resourceType = self.stateEngine.getCurrentResourceType(dimdimID)
        currentPageNum = resource.get('currentPage')
        
        # prepare cache
        presenterLocation = resourceType + '/' + resourceID + '/' + str(currentPageNum) + '/presenter/content.html'
        presenterCache = string.replace(self.presenterCacheBuffer, 'DIMDIMSL_COB_CONTENT_SRC', presenterLocation)
        
        pCacheLocation = os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'p_cache.html')
        pCacheHandle = open(pCacheLocation, 'wb')
        pCacheHandle.write(presenterCache)
        pCacheHandle.close()
        
        response = self.jsonObj.encode({'result' : True, 'pageNum' : currentPageNum, 'location' : '/content/' + str(dimdimID) + '/p_cache.html'})
        return response.encode()
    
    def syncToResource(self, dimdimID, roomID, sessionID, resourceID, returnType):
        
        # navigate to the given resource first
        self.stateEngine.navigateToResource(dimdimID, resourceID)
        self.stateEngine.resetCurrentResource(dimdimID)
        self.stateEngine.trimResourceToCurrentPage(dimdimID)
        
        # prepare attendee and presenter cache with current state
        resource = self.stateEngine.getCurrentResource(dimdimID)
        resourceType = self.stateEngine.getCurrentResourceType(dimdimID)
        
        currentPageNum = resource.get('currentPage')
        
        # prepare cache
        presenterLocation = resourceType + '/' + resourceID + '/' + str(currentPageNum) + '/presenter/content.html'
        presenterCache = string.replace(self.presenterCacheBuffer, 'DIMDIMSL_COB_CONTENT_SRC', presenterLocation)
        
        attendeeLocation = resourceType + '/' + resourceID + '/' + str(currentPageNum) + '/attendee/content.html'
        attendeeCache = string.replace(self.attendeeCacheBuffer, 'DIMDIMSL_COB_CONTENT_SRC', attendeeLocation)
        
        pCacheLocation = os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'p_cache.html')
        pCacheHandle = open(pCacheLocation, 'wb')
        pCacheHandle.write(presenterCache)
        pCacheHandle.close()
        
        aCacheLocation = os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'a_cache.html')
        aCacheHandle = open(aCacheLocation, 'wb')
        aCacheHandle.write(attendeeCache)
        aCacheHandle.close()
        
        if returnType == 'method':
            return 'cobCallback(\''+str(dimdimID) + '\')'
        
        jsonSuccess = jsonObject()
        jsonSuccess.clearResponse()
        jsonSuccess.add('result', 'true')
        jsonSuccess.add('method', 'syncToURLResource')
        jsonSuccess.add('error', '7200')
        jsonSuccess.add('location', '/content/' + str(dimdimID))
        
        logManager.log('Meeting ' + dimdimID + ' synced to resource ' + resourceID, logging.INFO)
        
        return jsonSuccess.jsonResponse()
    
    def formSubmit(self, paramDict):
        
        dimdimID = paramDict.pop('dimdimsl_dimdimID')
        roomID = paramDict.pop('dimdimsl_roomID')
        sessionID = paramDict.pop('dimdimsl_sessionID')
        method = paramDict.pop('dimdimsl_method')
        action = paramDict.pop('dimdimsl_action')
        
        
        originalAction = urllib.unquote(action)
        page = self.stateEngine.getCurrentPage(dimdimID)
        currentBaseURL = self.baseURL(page.get('url'))
        baseURL = self.baseURL(originalAction)
        
        if string.find(originalAction, osconfig.serverURL()) >= 0:
            # some pages don't have base injected (e.g. youtube)
            # these pages might give out relative links which
            # assume osconfig.serverURL() as the base URL.
            originalAction = string.lstrip(originalAction, osconfig.serverURL())
            
        # it is possible that originalAction isn't an absolute link
        if string.find(originalAction, 'http://') != 0:
            # the only way originalAction isn't an absolute link is if
            # it belongs to the existing server. so we can
            # safely set baseURL to existing currentBaseURL
            baseURL = currentBaseURL
            originalAction = urlparse.urljoin(baseURL, originalAction)
            action = urllib.quote(originalAction)
    
        meeting = self.stateEngine.getMeeting(dimdimID)
        resourceID = meeting.get('currentResource')
        resource = self.stateEngine.getCurrentResource(dimdimID)
        lastPageNum = resource.get('lastPage')
        currentPageNum = resource.get('currentPage')
        if (currentPageNum != lastPageNum):
            self.stateEngine.trimResourceToCurrentPage(dimdimID)
            
        resourceType = self.stateEngine.getResourceType(dimdimID, resourceID)
        currentPageNum = currentPageNum + 1
        
        # just create the page and return its location
        
        resourcePath = os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), resourceType), resourceID)
        pagePath = os.path.join(resourcePath, str(currentPageNum))
        presenterPagePath = os.path.join(pagePath, 'presenter')
        attendeePagePath = os.path.join(pagePath, 'attendee')
        
        if not os.path.isdir(presenterPagePath):
            os.makedirs(presenterPagePath)
        if not os.path.isdir(attendeePagePath):
            os.makedirs(attendeePagePath)
        
        tempStore = os.path.join(resourcePath, 'temp.html')
        try:
            os.remove(tempStore)
        except:
            pass
        
        retval = self.curlHandle.downloadToFileFromHTTPFormSubmit(action, method, paramDict, tempStore)
        if len(retval) < 3:
            try:
                os.remove(tempStore)
            except:
                pass
            try:
                shutil.rmtree(pagePath)
            except:
                pass

            response = self.jsonObj.encode({'result' : False, 'error' : 7500})
            return response.encode()
        
        if originalAction != retval:
            # must be a 302. this is our new URL
            originalAction = retval
            baseURL = self.baseURL(originalAction)
            encodedURL = urllib.quote(originalAction)
        
        htmlHandle = open(tempStore, 'r')
        htmlContent = htmlHandle.read()
        htmlHandle.close()
        
        # inject baseURL and jquery.noConflict
        # TODO - WHAT IF BASE URL IS ALREADY THERE ??
        htmlContent = contentMgr.sanitizeHTML(htmlContent, baseURL)
        
        self.prepareHtml(originalAction, htmlContent, presenterPagePath, attendeePagePath)
     
        try:
            os.remove(tempStore)
        except:
            pass
        
        # add this page to the stateMachine
        self.stateEngine.registerPage(dimdimID, originalAction)
        resource = self.stateEngine.getCurrentResource(dimdimID)
        lastPage = resource.get('lastPage')
        response = self.jsonObj.encode({'result' : True, 'lastPage' : str(lastPage), 'currentPage' : str(currentPageNum), 'error' : 7200})
        return response.encode()
    
    def navigateToURL(self, dimdimID, roomID, sessionID, encodedURL):
        
        # navigateToURL is called only from within a page.
        # so this is always for the 'current' resourceID
            
        originalURL = urllib.unquote(encodedURL)
        page = self.stateEngine.getCurrentPage(dimdimID)
        currentBaseURL = self.baseURL(page.get('url'))
        baseURL = self.baseURL(originalURL)
        
        if string.find(originalURL, osconfig.serverURL()) >= 0:
            # some pages don't have base injected (e.g. youtube)
            # these pages might give out relative links which
            # assume osconfig.serverURL() as the base URL.
            originalURL = string.lstrip(originalURL, osconfig.serverURL())
        
        # it is possible that originalURL isn't an absolute link
        if string.find(originalURL, 'http://') != 0:
            # the only way originalURL isn't an absolute link is if
            # it belongs to the existing server. so we can
            # safely set baseURL to existing currentBaseURL
            baseURL = currentBaseURL
            originalURL = urlparse.urljoin(baseURL, originalURL)
            encodedURL = urllib.quote(originalURL)
        
        meeting = self.stateEngine.getMeeting(dimdimID)
        resourceID = meeting.get('currentResource')
        resource = self.stateEngine.getCurrentResource(dimdimID)
        lastPageNum = resource.get('lastPage')
        currentPageNum = resource.get('currentPage')
        if (currentPageNum != lastPageNum):
            self.stateEngine.trimResourceToCurrentPage(dimdimID)
            
        resourceType = self.stateEngine.getResourceType(dimdimID, resourceID)
        currentPageNum = currentPageNum + 1
        
        # just create the page and return its location
        
        resourcePath = os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), resourceType), resourceID)
        pagePath = os.path.join(resourcePath, str(currentPageNum))
        presenterPagePath = os.path.join(pagePath, 'presenter')
        attendeePagePath = os.path.join(pagePath, 'attendee')
        
        if not os.path.isdir(presenterPagePath):
            os.makedirs(presenterPagePath)
        if not os.path.isdir(attendeePagePath):
            os.makedirs(attendeePagePath)
        
        tempStore = os.path.join(resourcePath, 'temp.html')
        try:
            os.remove(tempStore)
        except:
            pass
        
        retval = self.curlHandle.downloadToFileFromHTTPURL(encodedURL, tempStore)
        if len(retval) < 3:
            try:
                os.remove(tempStore)
            except:
                pass
            try:
                shutil.rmtree(pagePath)
            except:
                pass
                
            response = self.jsonObj.encode({'result' : False, 'error' : 7500})
            return response.encode()
        
        if originalURL != retval:
            # must be a 302. this is our new URL
            originalURL = retval
            baseURL = self.baseURL(originalURL)
            encodedURL = urllib.quote(originalURL)
            
        htmlHandle = open(tempStore, 'r')
        htmlContent = htmlHandle.read()
        htmlHandle.close()
        
        htmlContent = contentMgr.sanitizeHTML(htmlContent, baseURL)
        self.prepareHtml(originalURL, htmlContent, presenterPagePath, attendeePagePath)
        
        try:
            os.remove(tempStore)
        except:
            pass
        
        # add this page to the stateMachine
        self.stateEngine.registerPage(dimdimID, originalURL)
        resource = self.stateEngine.getCurrentResource(dimdimID)
        lastPage = resource.get('lastPage')
        response = self.jsonObj.encode({'result' : True, 'lastPage' : str(lastPage), 'currentPage' : str(currentPageNum), 'error' : 7200})
        return response.encode()
    
    def navigateBack(self, dimdimID, roomID, sessionID):
        currentPageNum = self.stateEngine.navigateToPage(dimdimID, 'back')
        resource = self.stateEngine.getCurrentResource(dimdimID)
        lastPage = resource.get('lastPage')
        response = self.jsonObj.encode({'result' : True, 'lastPage' : str(lastPage), 'currentPage' : str(currentPageNum), 'error' : 7200})
        return response.encode()
    
    def navigateForward(self, dimdimID, roomID, sessionID):
        currentPageNum = self.stateEngine.navigateToPage(dimdimID, 'forward')
        resource = self.stateEngine.getCurrentResource(dimdimID)
        lastPage = resource.get('lastPage')
        response = self.jsonObj.encode({'result' : True, 'lastPage' : str(lastPage), 'currentPage' : str(currentPageNum), 'error' : 7200})
        return response.encode()
    
    def getResourceInfo(self, dimdimID, roomID, sessionID):
        resource = self.stateEngine.getCurrentResource(dimdimID)
        lockState = self.stateEngine.getLockState(dimdimID)
        if resource:
            currentPage = resource.get('currentPage')
            page = self.stateEngine.getCurrentPage(dimdimID)
            currentURL = page.get('url')
            toBeReplaced = 'http://'
            if string.find(currentURL, 'https://') == 0:
                toBeReplaced = 'https://'
            currentURL = string.replace(currentURL, toBeReplaced, '')
            lastPage = resource.get('lastPage')
            resourceInfo = {'result' : True, 'currentPage' : currentPage, 'lastPage' : lastPage, 'url': currentURL}
            if lockState and (lockState.get('locked') != None):
                resourceInfo['lock'] = lockState
            response = self.jsonObj.encode(resourceInfo)
        else:
            response = self.jsonObj.encode({'result':False})
        return response.encode();
    
    def createURLResource(self, dimdimID, roomID, sessionID, encodedURL, resourceID = '_default', resourceType = 'regular'):
        
        # createURLResource is only called for a fresh resource
        
        jsonError = jsonObject()
        jsonError.clearResponse()
        jsonError.add('result', 'false')
        jsonError.add('method', 'cacheURL')
        jsonError.add('error', '7500')
        
        meeting = self.stateEngine.getMeeting(dimdimID)
        if not meeting:
            # register the meeting first
            # ideally, this call should never come..
            # meeting should be registered in listURLResources
            logManager.log('Detected that meeting ' + dimdimID + ' was never registered. This could mean that listURLResources was not called', logging.WARN)
            self.stateEngine.registerMeeting(dimdimID, '')
            self.stateEngine.registerMeeting(dimdimID, 'http://webmeeting.dimdim.com:80/dimdim/BookmarkCobResource.action')
            #return jsonError.jsonResponse()
        
        if resourceType != 'regular':
            resourceType = 'preloaded'
            
        if resourceID == '_default':
            resourceType = 'regular'    # can't have preloaded resources with _default
            
        originalURL = urllib.unquote(encodedURL)
        baseURL = self.baseURL(originalURL)
        
        pageNum = '0'       # duh! this is a new resource
        
        resourcePath = os.path.join(os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), resourceType), resourceID)
        presenterPagePath = os.path.join(os.path.join(resourcePath, pageNum), 'presenter')
        attendeePagePath = os.path.join(os.path.join(resourcePath, pageNum), 'attendee')
        
        if not os.path.isdir(presenterPagePath):
            os.makedirs(presenterPagePath)
        if not os.path.isdir(attendeePagePath):
            os.makedirs(attendeePagePath)
        
        tempStore = os.path.join(resourcePath, 'temp.html')
        try:
            os.remove(tempStore)
        except:
            pass
        
        retval = self.curlHandle.downloadToFileFromHTTPURL(encodedURL, tempStore)
        
        if len(retval) < 3:
            try:
                shutil.rmtree(presenterPagePath)
                shutil.rmtree(attendeePagePath)
                os.remove(tempStore)
            except:
                pass
            return jsonError.jsonResponse()
        
        if originalURL != retval:
            # must be a 302. this is our new URL
            originalURL = retval
            baseURL = self.baseURL(originalURL)
            encodedURL = urllib.quote(originalURL)
            
        htmlHandle = open(tempStore, 'r')
        htmlContent = htmlHandle.read()
        htmlHandle.close()
        
        # inject baseURL
        htmlContent = contentMgr.sanitizeHTML(htmlContent, baseURL)
        self.prepareHtml(originalURL, htmlContent, presenterPagePath, attendeePagePath)
        
        # prepare cache
        presenterLocation = resourceType + '/' + resourceID + '/' + pageNum + '/presenter/content.html'
        presenterCache = string.replace(self.presenterCacheBuffer, 'DIMDIMSL_COB_CONTENT_SRC', presenterLocation)
        
        attendeeLocation = resourceType + '/' + resourceID + '/' + pageNum + '/attendee/content.html'
        attendeeCache = string.replace(self.attendeeCacheBuffer, 'DIMDIMSL_COB_CONTENT_SRC', attendeeLocation)
        
        pCacheLocation = os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'p_cache.html')
        pCacheHandle = open(pCacheLocation, 'wb')
        pCacheHandle.write(presenterCache)
        pCacheHandle.close()
        
        aCacheLocation = os.path.join(os.path.join(osconfig.cobArchive(), dimdimID), 'a_cache.html')
        aCacheHandle = open(aCacheLocation, 'wb')
        aCacheHandle.write(attendeeCache)
        aCacheHandle.close()
        
        # clean up
        
        try:
            os.remove(tempStore)
        except:
            pass
        
        # register everything
        
        
        self.stateEngine.registerResource(dimdimID, resourceID, resourceType)
        self.stateEngine.registerPage(dimdimID, originalURL)
        
        jsonSuccess = jsonObject()
        jsonSuccess.clearResponse()
        jsonSuccess.add('result', 'true')
        jsonSuccess.add('method', 'cacheURL')
        jsonSuccess.add('error', '7200')
        jsonSuccess.add('location', '/content/' + str(dimdimID))
        
        return jsonSuccess.jsonResponse()
    
    def prepareHtml(self, currentURL, htmlContent, presenterPagePath, attendeePagePath):
        
        # disable frame busters
        matchObj = re.search(('\S{0,}(window.top|top).(location|location.href)\s{0,}=\s{0,}\S{0,};'), htmlContent)
        while matchObj:
            start, end = matchObj.span()
            htmlContent = htmlContent[0:start] + 'var disabled = "disabled";' + htmlContent[end : len(htmlContent)+1]
            matchObj = re.search(('\S{0,}(window.top|top).(location|location.href)\s{0,}=\s{0,}\S{0,};'), htmlContent)
            
        # disable meta tags
        matchObj = re.finditer(('<meta.*?>'), htmlContent, re.IGNORECASE)
        for item in matchObj:
            match = item.group()
            internalMatch = re.search('<meta.+http-equiv\s{0,}=\s{0,}"refresh".+?>', match, re.IGNORECASE)
            if internalMatch:
                htmlContent = string.replace(htmlContent, internalMatch.group(), '<!--'+internalMatch.group()+'-->')
                break
            
        # disable parent object modifications
        matchObj = re.finditer('\S{0,}\s{0,}(window.parent|parent)\s{0,}=\s{0,}\S+;', htmlContent, re.IGNORECASE)
        for item in matchObj:
            match = item.group()
            token = 'parent'
            if string.find(match, 'window.parent') > 0:
                token = 'window.parent'
            match = string.replace(match, token, 'self')
            htmlContent = string.replace(htmlContent, item.group(), match)
            
        # disable target tags
        matchObj = re.search('<(a|area|form).*target\s{0,}=\s{0,}"\S+".*>', htmlContent, re.IGNORECASE)
        while matchObj:
            tagMatchString = matchObj.group()
            targetMatch = re.search('target\s{0,}=\s{0,}"\S+"', tagMatchString, re.IGNORECASE)
            htmlContent = string.replace(htmlContent, targetMatch.group(), '')
            matchObj = re.search('<a.*target\s{0,}=\s{0,}"\S+".*>', htmlContent, re.IGNORECASE)
        
        presenterContent    = htmlContent
        attendeeContent     = htmlContent
        
        presenterContent    = contentMgr.insertPreamble(presenterContent, 'presenter')
        attendeeContent     = contentMgr.insertPreamble(attendeeContent, 'attendee')
        
        # take care of urls which are not part of anchor / area tags
        # for lack of better skills at regular expressions, attempting urls enclosed
        # in single and double quotes separately.
            
        matchObj = re.search(('(document|window).(location|location.href)\s{0,}=\s{0,}\'\S+\''), presenterContent)
        while matchObj:     # Matching single quotes
            start, end = matchObj.span()
            match = string.replace(matchObj.group(), ' ', '')
            pos = string.find(match, '=')
            rhs = match[pos + 1 : len(match) - 1]
            replace = 'var dimdimsl_navigateURL = '+rhs+';dimdimsl_contentAdvisory.setupProxy("anch_area", dimdimsl_navigateURL);'
            presenterContent = presenterContent[0:start] + replace + presenterContent[end:len(presenterContent)+1]
            matchObj = re.search(('(document|window).(location|location.href)\s{0,}=\s{0,}\'\S+\''), presenterContent)
            
        matchObj = re.search(('(document|window).(location|location.href)\s{0,}=\s{0,}"\S+"'), presenterContent)
        while matchObj:     # Matching double quotes
            start, end = matchObj.span()
            match = string.replace(matchObj.group(), ' ', '')
            pos = string.find(match, '=')
            rhs = match[pos + 1 : len(match) - 1]
            replace = 'var dimdimsl_navigateURL = '+rhs+';dimdimsl_contentAdvisory.setupProxy("anch_area", dimdimsl_navigateURL);'
            presenterContent = presenterContent[0:start] + replace + presenterContent[end:len(presenterContent)+1]
            matchObj = re.search(('(document|window).(location|location.href)\s{0,}=\s{0,}"\S+"'), presenterContent)

        presenterHandle = open(os.path.join(presenterPagePath, 'content.html'), 'wb')
        presenterHandle.write(presenterContent)
        presenterHandle.close()
        
        attendeeHandle = open(os.path.join(attendeePagePath, 'content.html'), 'wb')
        attendeeHandle.write(attendeeContent)
        attendeeHandle.close()