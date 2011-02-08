#!/usr/bin/python2
import os
import sys
import string
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

    def getDocumentData(self, meetingID, docID, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.getDocumentData(meetingID, docID, strictJSON)

    def listDocuments(self, meetingID, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.listDocuments(meetingID, strictJSON)

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

    def uploadPreloaded(self):
        return """
        <html><body>
            <form action="uploadPreloadedDocument" method="post" enctype="multipart/form-data">
                    File  <input type="file" name="docFile"/>
                    <br/>
                    <br/>
                    File Name (can be empty)  <input type="text" name="fileName"/>
                    <br/>
                    <br/>
                    Meeting Room Name (cannot be empty)  <input type="text" name="meetingID"/>
                    <br/>
                    <br/>
                    Installation ID (cannot be empty)  <input type="text" name="installationID"/>
                    <br/>
                    <br/>
                    Document ID (can be empty) <input type="text" name="docID"/>
                    <br/>
                    <br/>
                    Document Type   <select name="docType">
                                        <option value="ppt" selected>ppt</option>
                                        <option value="pdf">pdf</option>
                                    </select>
                    <br/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    uploadPreloaded.exposed = True

    def uploadPreloadedWithPath(self):
        return """
        <html><body>
            <form action="uploadPreloadedDocumentWithPath" method="post" enctype="multipart/form-data">
                    File Location (cannot be empty) <input type="text" name="filePath"/>
                    <br/>
                    <br/>
                    File Name (can be empty) <input type="text" name="fileName"/>
                    <br/>
                    <br/>
                    Meeting Room Name (cannot be empty)  <input type="text" name="meetingID"/>
                    <br/>
                    <br/>
                    Installation ID (cannot be empty)  <input type="text" name="installationID"/>
                    <br/>
                    <br/>
                    Document ID  (can be empty) <input type="text" name="docID"/>
                    <br/>
                    <br/>
                    Document Type   <select name="docType">
                                        <option value="ppt" selected>ppt</option>
                                        <option value="pdf">pdf</option>
                                    </select>
                    <br/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    uploadPreloadedWithPath.exposed = True

    def uploadPreloadedDocumentWithPath(self, filePath, fileName, meetingID, docID, docType,installationID = '_default' , strictJSON = 'false'):
        meetingID = installationID + '____' + meetingID
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        if len(docID) == 0:
            docID = self.sdeck.generateDocID()

        return self.sdeck.uploadPreloadedDocumentWithPath(filePath, fileName, meetingID, docID, docType, strictJSON)

    def deletePreloadedFromRooms(self):
        return """
        <html><body>
            <form action="deletePreloadedDocumentFromRooms" method="post" enctype="multipart/form-data">
                    File  <input type="file" name="inputFile"/>
                    <br/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    deletePreloadedFromRooms.exposed = True

    def copyPreloadedToRooms(self):
        return """
        <html><body>
            <form action="copyPreloadedDocumentToRooms" method="post" enctype="multipart/form-data">
                    File  <input type="file" name="inputFile"/>
                    <br/>
                    <br/>
                    Meeting Room Name <input type="text" name="sourceMeetingRoom"/>
                    <br/>
                    <br/>
                    Document ID <input type="text" name="docID"/>
                    <br/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    copyPreloadedToRooms.exposed = True

    def uploadPreloadedWithURL(self):
        return """
        <html><body>
            <form action="uploadPreloadedDocumentWithURL" method="post" enctype="multipart/form-data">
                    File URL (cannot be empty and has to be escaped) <input type="text" name="fileURL"/>
                    <br/>
                    e.g. <i>http://ip:port/cmd?param1=value1&param2=value2</i> becomes <i>http%3A//ip%3Aport/cmd%3Fparam1%3Dvalue1%26param2%3Dvalue2</i>
                    <br/>
                    Failure to encode URL as shown above will give inconsistent results
                    <br/>
                    <br/>
                    File Name (can be empty) <input type="text" name="fileName"/>
                    <br/>
                    <br/>
                    Meeting Room Name (cannot be empty)  <input type="text" name="meetingID"/>
                    <br/>
                    <br/>
                    Installation ID (cannot be empty)  <input type="text" name="installationID"/>
                    <br/>
                    <br/>
                    Document ID  (can be empty) <input type="text" name="docID"/>
                    <br/>
                    <br/>
                    Document Type   <select name="docType">
                                        <option value="ppt" selected>ppt</option>
                                        <option value="pdf">pdf</option>
                                    </select>
                    <br/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    uploadPreloadedWithURL.exposed = True

    def uploadPreloadedDocumentWithURL(self, fileURL, fileName, meetingID, docID, docType, installationID = '_default', strictJSON = 'false'):
        meetingID = installationID + '____' + meetingID
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        if len(docID) == 0:
            docID = self.sdeck.generateDocID()

        return self.sdeck.uploadPreloadedDocumentWithURL(fileURL, fileName, meetingID, docID, docType, strictJSON)

    def uploadPreloadedDocument(self, docFile, fileName, meetingID, docID, docType, installationID = '_default', strictJSON = 'false'):
        meetingID = installationID + '____' + meetingID
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        if len(docID) == 0:
            docID = self.sdeck.generateDocID()

        return self.sdeck.uploadPreloadedDocument(docFile, fileName, meetingID, docID, docType, strictJSON)

    def copyAllPreloaded(self, sourceMeetingRoom, destinationMeetingRoom, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        return self.sdeck.copyAllPreloaded(sourceMeetingRoom, destinationMeetingRoom, strictJSON)

    def deletePreloadedDocumentFromRooms(self, inputFile, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        return self.sdeck.deletePreloadedDocumentFromRooms(inputFile, strictJSON)

    def deletePreloadedDocumentFromRoomsAtLocation(self, inputFileLocation, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        return self.sdeck.deletePreloadedDocumentFromRoomsAtLocation(inputFileLocation, strictJSON)

    def copyPreloadedDocumentToRooms(self, sourceMeetingRoom, docID, inputFile, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        return self.sdeck.copyPreloadedDocumentToRooms(sourceMeetingRoom, docID, inputFile, strictJSON)

    def copyPreloadedDocumentToRoomsAtLocation(self, sourceMeetingRoom, docID, inputFileLocation, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        return self.sdeck.copyPreloadedDocumentToRoomsAtLocation(sourceMeetingRoom, docID, inputFileLocation, strictJSON)

    def copyPreloadedDocument(self, sourceMeetingRoom, docID, destinationMeetingRoom, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()

        return self.sdeck.copyPreloadedDocument(sourceMeetingRoom, docID, destinationMeetingRoom, strictJSON)

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

    def getDocumentStatus(self, docID, meetingID, strictJSON = 'false', cflag = 'ignore'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        status = "<jb>"+self.sdeck.getDocumentStatus(docID, strictJSON)+"</jb>"
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

    def deleteDoc(self):
        return """
        <html><body>
            <form action="deleteDocument" method="post" enctype="multipart/form-data">
                    Document ID  (cannot be empty) <input type="text" name="docID"/>
                    <br/>
                    <br/>
                    Meeting ID  (cannot be empty) <input type="text" name="meetingID"/>
                    <br/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    deleteDoc.exposed = True

    def deleteDocument(self, docID, meetingID, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
        
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.deleteDocument(docID, meetingID)


    def deletePreloadedDocs(self):
        return """
        <html><body>
            <form action="deletePreloadedDocuments" method="post" enctype="multipart/form-data">
                    Meeting ID  (cannot be empty) <input type="text" name="meetingID"/>
                    <br/>
                    <br/>
                    <input type="submit" />
                </form>
        </body></html>
        	"""
    deletePreloadedDocs.exposed = True

    def deletePreloadedDocuments(self, meetingID, strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.deletePreloadedDocuments(meetingID, strictJSON)

    def closeMeeting(self, meetingID):
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return self.sdeck.closeMeeting(meetingID)

    generateDocID.exposed = True
    getDocumentData.exposed = True
    listDocuments.exposed = True
    uploadPreloadedDocument.exposed = True
    uploadPreloadedDocumentWithPath.exposed = True
    uploadPreloadedDocumentWithURL.exposed = True
    copyAllPreloaded.exposed = True
    deletePreloadedDocumentFromRooms.exposed = True
    deletePreloadedDocumentFromRoomsAtLocation.exposed = True
    copyPreloadedDocumentToRooms.exposed = True
    copyPreloadedDocumentToRoomsAtLocation.exposed = True
    copyPreloadedDocument.exposed = True
    uploadDocument.exposed = True
    uploadPPTXSlide.exposed = True
    startDocumentConversion.exposed = True
    getDocumentStatus.exposed = True
    cancelDocumentConversion.exposed = True
    retrievePresentation.exposed = True
    deleteDocument.exposed = True
    deletePreloadedDocuments.exposed = True
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

    def getDocumentStatus2(self, docID, meetingID, cflag = 'ignore', strictJSON = 'false'):
        if (string.lower(strictJSON) == 'false'):
            strictJSON = False
        else:
            strictJSON = True
            
        if not self.sdeck:
            self.sdeck = CSlidedeck()
        return "dmsReturn( 'DMS','PROGRESS_CHECK','ok','"+self.sdeck.getDocumentStatus(docID, strictJSON)+"')"

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
from flup.server.fcgi import WSGIServer
root = LandingPages()
root.pmgr = CInterface()

# Start the CherryPy server.

app = cherrypy.tree.mount(root)
# For running standalong cherrypy -

cherrypy.config.update({'server.thread_pool':275})
cherrypy.config.update({'server.socket_host':'0.0.0.0'})
cherrypy.config.update({'environment' : 'production'})
cherrypy.config.update({'log.access_file':'/usr/local/dimdim/Mediaserver/www/logs/cherrypy_access.log'})
cherrypy.config.update({'log.error_file':'/usr/local/dimdim/Mediaserver/www/logs/cherrypy_error.log'})

cherrypy.engine.autoreload_on = False


try:
    WSGIServer(app).run()
finally:
    cherrypy.engine.stop()

