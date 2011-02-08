import os
import sys

sys.path.append(os.getcwd())

from document_manager.slidedeck import CSlidedeck
from file_manager import fileMgr
from file_manager import portalUpload

class LandingPages(object):
    def index(self):
        return 'Landing Page'
    
class CInterface(object):
    sdeck = None
    def __init__(self):
        self.sdeck = CSlidedeck()

# BEGIN : Slidedeck Interfaces

    def generateDocID(self, meetingID, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return "<jb>"+self.sdeck.generateDocID()+"</jb>"

    def getDocumentData(self, meetingID, docID):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.getDocumentData(meetingID, docID)

    def listDocuments(self, meetingID):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.listDocuments(meetingID)
    
    def stress(self):
        return """
        <html><body>
            <form action="startStress" method="post" enctype="multipart/form-data">
                    file  <input type="file" name="docFile"/>
                    <br/>
                    docType  <input type="text" name="docType"/>
                    <br/>
                    count  <input type="text" name="count"/>
                    <br/>
                    index  <input type="text" name="index"/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""

    def startStress(self, docFile, docType, count, index):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.startStress(docFile, docType, count, index)
    
    stress.exposed = True
    startStress.exposed = True

    def upload(self):
        return """
        <html><body>
            <form action="uploadDocument" method="post" enctype="multipart/form-data">
                    file  <input type="file" name="docFile"/>
                    <br/>
                    meetingID  <input type="text" name="meetingID"/>
                    <br/>
                    docID  <input type="text" name="docID"/>
                    <br/>
                    roomID  <input type="text" name="roomID"/>
                    <br/>
                    sessionID  <input type="text" name="sessionID"/>
                    <br/>
                    docType  <input type="text" name="docType"/>
                    <br/>
                    uploadType  <input type="text" name="uploadType"/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    upload.exposed = True

    def uploadDocument(self, docFile, meetingID, roomID, sessionID, docID, docType, uploadType):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.uploadDocument(docFile, meetingID, roomID, sessionID, docID, docType, uploadType)

    def uploadPPTXSlide(self, slideFile, meetingID, pptID, pptName, slideNo, noOfSlides):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.uploadPPTXSlide(slideFile, meetingID, pptID, pptName, slideNo, noOfSlides)

    def startDocumentConversion(self, docID, docName, meetingID, roomID, sessionID, docType, uploadType, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return "<jb>"+self.sdeck.startDocumentConversion(docID, docName, meetingID, roomID, sessionID, docType, uploadType)+"</jb>"

    def getDocumentStatus(self, docID, meetingID, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        status = "<jb>"+self.sdeck.getDocumentStatus(docID)+"</jb>"
        return status

    def cancelDocumentConversion(self, docID, meetingID, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.cancelDocumentConversion(docID, meetingID)

    # Needs flash player change for this. To be converted to retrieveDocument
    def retrievePresentation(self, meetingID, presentationFile, slideNo, cflag='ignore'):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.retrieveDocument(presentationFile, meetingID, slideNo)

    def deleteDocument(self, docID, meetingID):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.deleteDocument(docID, meetingID)

    def closeMeeting(self, meetingID):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.closeMeeting(meetingID)

    generateDocID.exposed = True
    getDocumentData.exposed = True
    listDocuments.exposed = True
    uploadDocument.exposed = True
    uploadPPTXSlide.exposed = True
    startDocumentConversion.exposed = True
    getDocumentStatus.exposed = True
    cancelDocumentConversion.exposed = True
    retrievePresentation.exposed = True
    deleteDocument.exposed = True
    closeMeeting.exposed = True

# END : Slidedeck Interfaces

#BEGIN : Statistics related interfaces
    
    def queryHealth(self):
        return self.sdeck.collator.queryHealth()
    
    def queryHealthState(self, param):
        return self.sdeck.collator.queryHealthState(param)
    
    queryHealth.exposed = True
    queryHealthState.exposed = True

#END : Statistics related interfaces

#BEGIN : Annotation related interface

    def generateDocID2(self, meetingID, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return "dmsReturn('DMS','PPT_ID','ok','"+self.sdeck.generateDocID()+"')"

    def startDocumentConversion2(self, docID, docName, meetingID, roomID, sessionID, docType, uploadType, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return "dmsReturn( 'DMS','CONVERSION_START','ok','"+self.sdeck.startDocumentConversion(docID, docName, meetingID, roomID, sessionID, docType, uploadType)+"')"

    def getDocumentStatus2(self, docID, meetingID, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return "dmsReturn( 'DMS','PROGRESS_CHECK','ok','"+self.sdeck.getDocumentStatus(docID)+"')"

    def cancelDocumentConversion2(self, docID, meetingID, docType, cflag):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return "dmsReturn( 'DMS','PROGRESS_CHECK','ok','"+self.sdeck.cancelDocumentConversion(docID, meetingID)+"')"

    def uploadOnlyDocument(self, meetingID, docID, docType, docFile):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.uploadOnlyDocument(meetingID, docID, docType, docFile)

    generateDocID2.exposed = True
    startDocumentConversion2.exposed = True
    getDocumentStatus2.exposed = True
    cancelDocumentConversion2.exposed = True
    uploadOnlyDocument.exposed = True
#END : Annotation related interface

import cherrypy
from flup.server.scgi import WSGIServer
root = LandingPages()
root.pmgr = CInterface()
    
# Start the CherryPy server.

if sys.platform.startswith('win'):
    cherrypy.config.update(os.path.join(os.path.dirname(__file__), 'wdms.conf'))
    app = cherrypy.tree.mount(root, config=os.path.join(os.path.dirname(__file__), 'wdms.conf'))
else:
    cherrypy.config.update(os.path.join(os.path.dirname(__file__), '/usr/local/dimdim/Mediaserver/mods/ldms.conf'))
    app = cherrypy.tree.mount(root, config=os.path.join(os.path.dirname(__file__), '/usr/local/dimdim/Mediaserver/mods/ldms.conf'))

# For running standalong cherrypy -




cherrypy.engine.autoreload_on = False
cherrypy.engine.start(blocking=False)

try:
    WSGIServer(app, bindAddress=('localhost',40002)).run()
finally:
    cherrypy.engine.stop()

