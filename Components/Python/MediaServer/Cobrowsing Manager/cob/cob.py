import os
import sys
import string
from toolkit import osconfig
sys.path.append(os.getcwd())

from cobmanager.slserver import COBServer

class LandingPages(object):
    def index(self):
        return 'There is nothing to see here...'
    index.exposed = True

class COBInterface(object):

    server = None

    def __init__(self):
        self.server = COBServer()

    def index(self):
        return "Welcome to Dimdim Synchrolive!!"

    def formSubmit(self, *args, **kwargs):
        return self.server.formSubmit(cherrypy.request.params)

    def listURLResources(self, dimdimID, roomID, bookmarkAction, cflag = 1):
        return self.server.listURLResources(osconfig.getDimdimID(dimdimID), bookmarkAction)
    
    def syncToAttendeeContext(self, dimdimID, roomID, sessionID, returnType = 'json', cflag = 1):
        return self.server.syncToAttendeeContext(osconfig.getDimdimID(dimdimID), roomID, sessionID, returnType)
    
    def syncToPresenterContext(self, dimdimID, roomID, sessionID, pageNum = -1, cflag = 1):
        return self.server.syncToPresenterContext(osconfig.getDimdimID(dimdimID), roomID, sessionID, pageNum)
        
    def syncToResource(self, dimdimID, roomID, sessionID, resourceID, returnType = 'method', cflag = 1):
        return self.server.syncToResource(osconfig.getDimdimID(dimdimID), roomID, sessionID, resourceID, returnType)
    
    def deleteURLResource(self, dimdimID, roomID, sessionID, resourceID, cflag):
        return self.server.deleteURLResource(osconfig.getDimdimID(dimdimID), roomID, sessionID, resourceID)
    
    def closeMeeting(self, dimdimID, roomID):
        return self.server.closeMeeting(dimdimID)

    def createURLResource(self, dimdimID, roomID, sessionID, encodedURL, resourceID = '_default', resourceType = 'regular', cflag = None):
        return self.server.createURLResource(osconfig.getDimdimID(dimdimID), roomID, sessionID, encodedURL, resourceID, resourceType)
    
    def navigateToURL(self, dimdimID, roomID, sessionID, encodedURL, cflag = 1):
        return self.server.navigateToURL(osconfig.getDimdimID(dimdimID), roomID, sessionID, encodedURL)

    def sendEvents(self, dimdimID, roomID, sessionID, events, cflag = 1):
        return self.server.sendEvents(osconfig.getDimdimID(dimdimID), roomID, sessionID, events)

    def navigateBack(self, dimdimID, roomID, sessionID, cflag = 1):
        return self.server.navigateBack(osconfig.getDimdimID(dimdimID), roomID, sessionID)

    def navigateForward(self, dimdimID, roomID, sessionID, cflag = 1):
        return self.server.navigateForward(osconfig.getDimdimID(dimdimID), roomID, sessionID)
    
    def getResourceInfo(self, dimdimID, roomID, sessionID, cflag = 1):
        return self.server.getResourceInfo(osconfig.getDimdimID(dimdimID), roomID, sessionID)
    
    def bookmarkCurrentPage(self, dimdimID, roomID, sessionID, cflag = 1):
        return self.server.bookmarkCurrentPage(dimdimID, roomID, sessionID)
    
    def unregisterResource(self, dimdimID, roomID, sessionID, resourceID, cflag = 1):
        return self.server.unregisterResource(dimdimID, roomID, sessionID, resourceID)

    index.exposed = True
    sendEvents.exposed = True
    formSubmit.exposed = True
    navigateBack.exposed = True
    closeMeeting.exposed = True
    navigateToURL.exposed = True
    syncToResource.exposed = True
    navigateForward.exposed = True
    getResourceInfo.exposed = True
    listURLResources.exposed = True
    createURLResource.exposed = True
    deleteURLResource.exposed = True
    unregisterResource.exposed = True
    bookmarkCurrentPage.exposed = True
    syncToAttendeeContext.exposed = True
    syncToPresenterContext.exposed = True

import cherrypy

root = LandingPages()
root.cobrowsing = COBInterface()
if __name__ == '__main__':
    osconfig.setupCherrypyConfig(root)
    osconfig.run()