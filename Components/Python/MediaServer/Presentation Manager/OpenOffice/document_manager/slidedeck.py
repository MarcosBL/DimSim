import os
import sys
import shutil
import string
from demjson import JSON
import threading
import xml.dom.minidom
from distutils import dir_util
import logging
import cherrypy
import time

from toolkit import idgen
from toolkit import osconfig
from toolkit import filehelper
from stats_manager.mule import Collator
from toolkit.jsonhelper import jsonObject
from toolkit.curlhelper import CurlWrapper
from toolkit.filehelper import serialObject

from cherrypy.lib import static
from engine import exportEngine

oopath = osconfig.ooPath()

if sys.platform.startswith('win'):
    import win32process
    import win32file
    import win32com.client, pythoncom
else:
    sys.path.append(oopath)

import uno
import unohelper
import popen2

maxPDFAccess = osconfig.maxPDFAccess()
mediaDirectory = osconfig.mediaDirectory()

class CSlidedeck(object):
    processMap = None
    officeLock = None
    pdfLock = None
    collator = None
    curlHandle = None
    demHelper = None
    resolveUrl = None
    isOOHealthy = None

    def __init__(self):
        self.processMap = {}
        self.officeLock = threading.BoundedSemaphore(1)
        self.pdfLock = threading.BoundedSemaphore(maxPDFAccess)
        self.collator = Collator()
        self.curlHandle = CurlWrapper()
        self.demHelper = JSON()
        self.resolveUrl = "uno:socket,host=localhost,port=8100;urp;StarOffice.ComponentContext"
        self.isOOHealthy = "False"

    def findDocuments(self, path, meetingID, docID):
        localMetaPath = os.path.join(os.path.join(os.path.join(path, meetingID), docID), 'documentData.txt')
        preloadedMetaPath = os.path.join(os.path.join(os.path.join(os.path.join(path, meetingID), 'Preloaded'), docID), 'documentData.txt')
        globalMetaPath = os.path.join(os.path.join(os.path.join(path, 'global-meeting'), docID), 'documentData.txt')

        entries = []
        if os.path.exists(localMetaPath):
            entries.append(localMetaPath)

        if os.path.exists(preloadedMetaPath):
            entries.append(preloadedMetaPath)

        if os.path.exists(globalMetaPath):
            if meetingID != 'global-meeting':   # because we have already taken care of this
                entries.append(globalMetaPath)

        return entries

    def findMeetingDocuments(self, path, meetingID):

        # primarily used to find preloaded and global documents

        entries = []

        globalDir = os.path.join(path, 'global-meeting')
        preloadedDir = os.path.join(os.path.join(path, meetingID), 'Preloaded')

        globalFolderList = []
        preloadedFolderList = []

        if os.path.exists(globalDir):
            globalFolderList = os.listdir(globalDir)

        if os.path.exists(preloadedDir):
            preloadedFolderList = os.listdir(preloadedDir)

        if len(globalFolderList) > 0:
            for i in range(0, len(globalFolderList)):
                metaFileName = os.path.join(os.path.join(os.path.join(path, 'global-meeting'), globalFolderList[i]), 'documentData.txt')
                if os.path.isfile(metaFileName):
                    entries.append(metaFileName)

        if len(preloadedFolderList) > 0:
            for i in range(0, len(preloadedFolderList)):
                metaFileName = os.path.join(os.path.join(os.path.join(os.path.join(path, meetingID), 'Preloaded'), preloadedFolderList[i]), 'documentData.txt')
                if os.path.isfile(metaFileName):
                    entries.append(metaFileName)

        return entries

    def generatePPTIDxx(self):
        doc = xml.dom.minidom.Document()
        textNode = doc.createElement("jb")
        textNode.appendChild(doc.createTextNode(idgen.gen()))
        doc.appendChild(textNode)
        return doc.toxml()

    def generateDocID(self):
        return idgen.gen()

    def copyAllPreloaded(self, sourceMeetingRoom, destinationMeetingRoom, strictJSON = False):
        slidedeckArchive = osconfig.slidedeckArchive()

        sourcePath = os.path.join(os.path.join(slidedeckArchive, sourceMeetingRoom), 'Preloaded')

        if not os.path.isdir(sourcePath):
            if not strictJSON:
                jsonError = jsonObject()
                jsonError.clearResponse()
                jsonError.add('result', 'false')
                jsonError.add('method', 'copyAllPreloaded')
                jsonError.add('error', '7404')
                return jsonError.jsonResponse()
            else:
                jsonError = {'result':'false', 'method':'copyAllPreloaded', 'error':7404}
                response = self.demHelper.encode(jsonError)
                return response.encode()

        for entry in os.listdir(sourcePath):
            sourcePath = os.path.join(os.path.join(os.path.join(slidedeckArchive, sourceMeetingRoom), 'Preloaded'), entry)
            destPath = os.path.join(os.path.join(os.path.join(slidedeckArchive, destinationMeetingRoom), 'Preloaded'), entry)

            try:
                # in case the destination directory doesn't exist
                os.makedirs(destPath)
            except:
                pass
            
            try:
                fileList = dir_util.copy_tree(sourcePath, destPath)
            except:
                if not strictJSON:
                    jsonError = jsonObject()
                    jsonError.clearResponse()
                    jsonError.add('result', 'false')
                    jsonError.add('method', 'copyAllPreloaded')
                    jsonError.add('error', '7500')
                    return jsonError.jsonResponse()
                else:
                    jsonError = {'result':'false', 'method':'copyAllPreloaded', 'error':7500}
                    response = self.demHelper.encode(jsonError)
                    return response.encode()

        if not strictJSON:
            jsonError = jsonObject()
            jsonError.clearResponse()
            jsonError.add('result', 'true')
            jsonError.add('method', 'copyAllPreloaded')
            jsonError.add('error', '7200')
            return jsonError.jsonResponse()
        else:
            jsonError = {'result':'true', 'method':'copyAllPreloaded', 'error':7200}
            response = self.demHelper.encode(jsonError)
            return response.encode()

    def deletePreloadedDocumentFromRooms(self, inputFile, strictJSON = False):

        fileStoreLocation = os.path.join(mediaDirectory, self.generateDocID())
        fileStoreLocation += '.cfg'
        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(fileStoreLocation, 'wb')
        else:
            storedFile = open(fileStoreLocation, 'w')
        while True:
            data = inputFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)

        storedFile.close()

        response = self.deletePreloadedDocumentFromRoomsAtLocation(fileStoreLocation, strictJSON)
        os.remove(fileStoreLocation)
        return response

    def deletePreloadedDocumentFromRoomsAtLocation(self, inputFileLocation, strictJSON = False):
        # file should have meetingIDs and docIDs as follows -
        # meetingID:docID,meetingID:docID,....

        inFileContent = None
        try:
            inFile = open(inputFileLocation, 'r')
            inFileContent = inFile.read()
            inFile.close()
        except:
            return 'failure to open fail at ' + inputFileLocation
        itemList = string.split(inFileContent, ',')

        for item in itemList:
            itemContent = string.split(item, ':')
            meetingID = string.strip(itemContent[0], ' ')
            docID = string.strip(itemContent[1], ' ')
            self.deleteDocument(docID, meetingID, strictJSON)

    def copyPreloadedDocumentToRooms(self, sourceMeetingRoom, docID, inputFile, strictJSON = False):

        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.cfg'
        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(fileStoreLocation, 'wb')
        else:
            storedFile = open(fileStoreLocation, 'w')
        while True:
            data = inputFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)

        storedFile.close()

        response = self.copyPreloadedDocumentToRoomsAtLocation(sourceMeetingRoom, docID, fileStoreLocation, strictJSON)
        os.remove(fileStoreLocation)
        return response

    def copyPreloadedDocumentToRoomsAtLocation(self, sourceMeetingRoom, docID, inputFileLocation, strictJSON=False):

        # file should have meetingIDs separated by commas

        inFileContent = None
        try:
            inFile = open(inputFileLocation, 'r')
            inFileContent = inFile.read()
            inFile.close()
        except:
            return 'failure to open file at ' + inputFileLocation

        meetingIDList = string.split(inFileContent, ',')
        failedMeetingIDList = []
        for meetingID in meetingIDList:
            meetingID = string.strip(meetingID, ' ')
            response = self.copyPreloadedDocument(sourceMeetingRoom, docID, meetingID, strictJSON)
            if string.find(response, '7200') < 0:
                failedMeetingIDList.append(meetingID)

        if len(failedMeetingIDList) > 0:
            retString = 'failed to copy document to following meetingIDs : '
            for meetingID in failedMeetingIDList:
                retString += meetingID + ', '
            return retString
        else:
            return 'document copied to all meetings successfully'

    def copyPreloadedDocument(self, sourceMeetingRoom, docID, destinationMeetingRoom, strictJSON=False):
        slidedeckArchive = osconfig.slidedeckArchive()

        sourcePath = os.path.join(os.path.join(os.path.join(slidedeckArchive, sourceMeetingRoom), 'Preloaded'), docID)
        destPath = os.path.join(os.path.join(os.path.join(slidedeckArchive, destinationMeetingRoom), 'Preloaded'), docID)
        if not os.path.isdir(sourcePath):
            if not strictJSON:
                jsonError = jsonObject()
                jsonError.clearResponse()
                jsonError.add('result', 'false')
                jsonError.add('method', 'copyPreloadedDocument')
                jsonError.add('error', '7404')
                return jsonError.jsonResponse()
            else:
                jsonError = {'result':'false', 'method':'copyPreloadedDocument', 'error':7404}
                response = self.demHelper.encode(jsonError)
                return response.encode()

        try:
            fileList = dir_util.copy_tree(sourcePath, destPath)
        except:
            if not strictJSON:
                jsonError = jsonObject()
                jsonError.clearResponse()
                jsonError.add('result', 'false')
                jsonError.add('method', 'copyPreloadedDocument')
                jsonError.add('error', '7500')
                return jsonError.jsonResponse()
            else:
                jsonError = {'result':'false', 'method':'copyPreloadedDocument', 'error':7500}
                response = self.demHelper.encode(jsonError)
                return response.encode()

        if not strictJSON:
            jsonError = jsonObject()
            jsonError.clearResponse()
            jsonError.add('result', 'true')
            jsonError.add('method', 'copyPreloadedDocument')
            jsonError.add('error', '7200')
            return jsonError.jsonResponse()
        else:
            jsonError = {'result':'true', 'method':'copyPreloadedDocument', 'error':7200}
            response = self.demHelper.encode(jsonError)
            return response.encode()


    def getDocumentData(self, meetingID, docID, strictJSON=False):
        slidedeckArchive = osconfig.slidedeckArchive()
        [installationID, dimdimID] = osconfig.splitIdentity(meetingID)
        meetingID = installationID + '____' + dimdimID
        jsonSuccess = jsonObject()
        jsonSuccess.clearResponse()
        jsonSuccess.add('result', 'true')
        jsonSuccess.add('method', 'getDocumentData')
        jsonSuccess.add('meetingID', meetingID)
        entries = self.findDocuments(slidedeckArchive, meetingID, docID)
        
        strictSuccess = {'result' : 'true', 'method' : 'getDocumentData', 'meetingID' : meetingID}

        if len(entries) == 0:
            if not strictJSON:
                jsonError = jsonObject()
                jsonError.clearResponse()
                jsonError.add('result', 'false')
                jsonError.add('method', 'getDocumentData')
                jsonError.add('meetingID', meetingID)
                jsonError.add('docID', docID)
                jsonError.add('error', '7404')
                return jsonError.jsonResponse()
            else:
                strictError = {'result':'false', 'method':'getDocumentData', 'meetingID':meetingID, 'docID':docID, 'error':7404}
                response = self.demHelper.encode(strictError)
                return response.encode()

        if not strictJSON:
            for i in range(0, len(entries)):
                fp = serialObject()
                fp.clearBuffer()
                fp.importData(entries[i])
                #jsonSuccess.add('docName', fp.get('docName'))
                jsonSuccess.add('docID', docID)
                jsonSuccess.add('docName', fp.get('docName'))
                jsonSuccess.add('noOfPages', string.atoi(fp.get('noOfPages')))
                jsonSuccess.add('width', fp.get('width'))
                jsonSuccess.add('height', fp.get('height'))
            jsonSuccess.add('error', '7200')
            return jsonSuccess.jsonResponse()
        else:
            for i in range(0, len(entries)):
                fp = serialObject()
                fp.clearBuffer()
                fp.importData(entries[i])
                strictSuccess.update({'docID':docID, 'docName':fp.get('docName'), 'noOfPages' : string.atoi(fp.get('noOfPages')), \
                                      'width': string.atoi(fp.get('width')), 'height': string.atoi(fp.get('height'))})
                
            response = self.demHelper.encode(strictSuccess)
            return response.encode()

    def listDocuments(self, meetingID, strictJSON = False):
        slidedeckArchive = osconfig.slidedeckArchive()
        [installationID, dimdimID] = osconfig.splitIdentity(meetingID)
        meetingID = installationID + '____' + dimdimID
        jsondata = jsonObject()
        jsondata.clearResponse()
        entries = self.findMeetingDocuments(slidedeckArchive, meetingID)

        if len(entries) == 0:
            if not strictJSON:
                jsondata.add('result', 'false')
                jsondata.add('method', 'listDocuments')
                jsondata.add('error', '7404')
                jsondata.add('meetingID', meetingID)
                return jsondata.jsonResponse()
            else:
                jsondata = {'result':'false', 'method':'listDocuments', 'error':7404, 'meetingID':meetingID}
                response = self.demHelper.encode(jsondata)
                return response.encode()
            
        if not strictJSON:
            jsondata.add('result', 'true')
            jsondata.add('method', 'listDocuments')
            jsondata.add('meetingID', meetingID)
            jsondata.add('error', '7200')
    
            entryJSONValue = '['
            for i in range(0, len(entries)):
                entryJSON = jsonObject()
                entryJSON.clearResponse()
                fp = serialObject()
                fp.clearBuffer()
                fp.importData(entries[i])
                entryJSON.add('docName',fp.get('docName'))
                entryJSON.add('docID',fp.get('docID'))
                entryJSON.add('width', fp.get('width'))
                entryJSON.add('height', fp.get('height'))
                entryJSON.add('noOfPages',string.atoi(fp.get('noOfPages')))
                if entries[i].find('global-meeting') >= 0:
                    entryJSON.add('Type', "global")
                else:
                    entryJSON.add('Type', "preloaded")
                entryJSONValue += entryJSON.jsonResponse()
                if i < len(entries) -1:
                    entryJSONValue += ','
    
            entryJSONValue += ']'
            jsondata.add('docs',entryJSONValue)
            return jsondata.jsonResponse()
        else:
            jsondata = {'result':'true', 'method':'listDocuments', 'meetingID':meetingID, 'error':7200}
            entryList = []
            for i in range(0, len(entries)):
                fp = serialObject()
                fp.clearBuffer()
                fp.importData(entries[i])
                entryMap = {'docName' : fp.get('docName'), 'docID' : fp.get('docID'), 'width' : string.atoi(fp.get('width')), \
                                        'height': string.atoi(fp.get('height')), 'noOfPages' : string.atoi(fp.get('noOfPages')) }
                if entries[i].find('global-meeting') >= 0:
                    entryMap.update({'Type':'global'})
                else:
                    entryMap.update({'Type' : 'preloaded'})
                entryList.append(entryMap)
            jsondata.update({'docs':entryList})
            response = self.demHelper.encode(jsondata)
            return response.encode()
            

    def startDocumentConversion(self, docID, docName, meetingID, roomID, sessionID, docType, uploadType):

        # 'uploadType accepts "global" or "preloaded". If neither is specified, it is assumed as a regular presentation.
        # if the type is global, "global-meeting" is used as a meeting key. Any meeting ID specified is discarded

        errorjson = jsonObject()
        errorjson.clearResponse()
        errorjson.add('result', 'false')
        errorjson.add('method', 'startDocumentConversion')
        errorjson.add('meetingID', meetingID)
        errorjson.add('docID', docID)

        type = string.lower(uploadType)

        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.' + str(docType)

        if not os.path.isfile(fileStoreLocation):
            errorjson.add('error', '7404')       # file not found. not uploaded properly
            return errorjson.jsonResponse()

        xPorter = None
        try:
            xPorter = exportEngine(meetingID, roomID, sessionID, fileStoreLocation, docID, uploadType, docName, docType, self.officeLock, self.pdfLock, self.collator)
            self.processMap[docID] = xPorter
            xPorter.start()

            # job created. register in collator
            self.collator.register(docID)

        except:
            errorjson.add('error', '7503')      # service not available. unable to convert
            return errorjson.jsonResponse()

        # JSON Response BEGIN
        jsondata = jsonObject()
        jsondata.clearResponse()
        jsondata.add('result','true')
        jsondata.add('method','startDocumentConversion')
        #jsondata.add('docName',docName)
        jsondata.add('docID',docID)
        jsondata.add('docType', docType)
        jsondata.add('meetingID', meetingID)
        #JSON Response END

        doc = xml.dom.minidom.Document()
        textNode = doc.createElement("jb")
        textNode.appendChild(doc.createTextNode(jsondata.jsonResponse()))
        doc.appendChild(textNode)

        return jsondata.jsonResponse()

    def cancelDocumentConversion(self, docID, meetingID):
        jsondata = jsonObject()
        jsondata.clearResponse()
        [installationID, dimdimID] = osconfig.splitIdentity(meetingID)
        meetingID = installationID + '____' + dimdimID
        if self.processMap.has_key(docID):
            self.collator.setConversionState(docID, 'done')
            try:
                xPorter = self.processMap.get(docID)
                xPorter.cancelConversion(False)
                self.processMap.pop(docID)
                try:
                    del xPorter
                except:
                    pass
                jsondata.add('result','true')
                jsondata.add('method','cancelDocumentConversion')
                jsondata.add('docID',docID)
                jsondata.add('error', '7200')
            except:
                jsondata.add('result','false')
                jsondata.add('method', 'cancelDocumentConversion')
                jsondata.add('docID',docID)
                jsondata.add('error','7500')
        else:
            jsondata.add('result','false')
            jsondata.add('method', 'cancelDocumentConversion')
            jsondata.add('docID',docID)
            jsondata.add('error','7404')

        self.deleteDocument(docID, meetingID)
        return jsondata.jsonResponse()

    def getDocumentStatus(self, docID, strictJSON = False):
        
        strictData = {}
        jsondata = jsonObject()
        jsondata.clearResponse()
        if self.processMap.has_key(docID):
            try:
                xPorter = self.processMap.get(docID)
                bPop = False
                if xPorter.errCode != 7200:

                    try:
                        if xPorter.docType == 'ppt':
                            self.collator.registerPPTFailure(docID, xPorter.meetingID)
                        elif xPorter.docType == 'pdf':
                            self.collator.registerPDFFailure(docID, xPorter.meetingID)
                    except:
                        pass
                    
                    if not strictJSON:
                        jsondata.add('result', 'false')
                        jsondata.add('method', 'getDocumentStatus')
                        jsondata.add('docID', docID)
                        jsondata.add('error', xPorter.errCode)
                    else:
                        strictData.update({'result' : 'false', 'method' : 'getDocumentStatus', 'docID' : docID, 'error' : xPorter.errCode})
                        
                    bPop = True
                    meetingID = xPorter.meetingID
                    self.collator.setConversionState(docID, 'done')
                    try:
                        xPorter.cancelConversion()
                    except:
                        pass

                    try:
                         self.deleteDocument(docID, meetingID, strictJSON)
                    except:
                        pass

                    try:
                        del xPorter
                    except:
                        pass

                    if not strictJSON:
                        return jsondata.jsonResponse()
                    else:
                        response = self.demHelper.encode(strictData)
                        return response.encode()
                    
                    
                if not strictJSON:
                    jsondata.add('result','true')
                    jsondata.add('method','getDocumentStatus')
                    jsondata.add('docID',docID)
                    jsondata.add('width',xPorter.width)
                    jsondata.add('height',xPorter.height)
                    jsondata.add('pagesConverted',xPorter.pagesConverted)
                    jsondata.add('totalPages',xPorter.totalPageCount)
                    jsondata.add('conversionComplete',xPorter.conversionComplete)
                    jsondata.add('error', '7200')
                else:
                    strictData.update({'result':'true', 'method':'getDocumentStatus', 'docID':docID, 'width':xPorter.width, 'height':xPorter.height, \
                                       'pagesConverted' : xPorter.pagesConverted, 'totalPages': xPorter.totalPageCount, \
                                       'conversionComplete':xPorter.conversionComplete, 'error':7200})


                if xPorter.conversionComplete == True:
                    bPop = True
                    try:
                        if xPorter.docType == 'pdf':
                            self.collator.resetPDFFailures()
                        else:
                            self.collator.resetUploadFailures()
                    except:
                        pass

                    try:
                        del xPorter
                    except:
                        pass

                if bPop:
                    self.processMap.pop(docID)

            except:
                jsondata.add('result', 'false')
                jsondata.add('method', 'getDocumentStatus')
                jsondata.add('docID', docID)
                jsondata.add('error', '7500')
                strictData.update({'result':'false', 'method':'getDocumentStatus', 'docID':docID, 'error':7500})
        else:
            if not strictJSON:
                jsondata.add('result','false')
                jsondata.add('method','getDocumentStatus')
                jsondata.add('docID',docID)
                jsondata.add('error','7404')
            else:
                strictData.update({'result':'false', 'method':'getDocumentStatus', 'docID':docID, 'error':7404})

        if not strictJSON:
            return jsondata.jsonResponse()
        else:
            response = self.demHelper.encode(strictData)
            return response.encode()

    def uploadPPTXSlide(self, docFile, meetingID, docID, docName, pageNo, noOfPages):
        # 'Type' will be 'regular', becuase PPTX slides are uploaded by the presenter
        # There is no conversion involved in this operation. We just need to commit this file to archive

        iPageNo = string.atoi(pageNo)
        iNoOfPages = string.atoi(noOfPages)

        jsonComplete = jsonObject()
        jsonComplete.clearResponse()
        jsonComplete.add('result', 'true')
        jsonComplete.add('method', 'uploadPPTXSlide')
        jsonComplete.add('docID', docID)

        slidedeckArchive = osconfig.slidedeckArchive()
        specArchive = os.path.join(slidedeckArchive, meetingID)

        if docID == 'GENERATE' or docID == '':
            docID = idgen.gen()

        specArchive = os.path.join(specArchive, docID)

        if not os.path.isdir(specArchive):
            os.makedirs(specArchive)

        storedFileName = os.path.join(specArchive, pageNo)
        storedFileName += ".jpg"

        if sys.platform.startswith('win'):
            storedFile = open(storedFileName, 'wb')
        else:
            storedFile = open(storedFileName, 'w')
        while True:
            data = docFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)

        storedFile.close()

        if (iPageNo == iNoOfPages - 1):
            # store some details for probable future use
            storeObject = filehelper.serialObject()
            storeObject.clearBuffer()
            storeObject.add('docName', docName)
            storeObject.add('docID',docID)
            storeObject.add('noOfPages',noOfPages)
            dataStore = os.path.join(specArchive, 'documentData.txt');
            storeObject.exportData(dataStore)

            jsonComplete.add('complete', 'true')
            return jsonComplete.jsonResponse()

        jsonComplete.add('complete', 'false')
        return jsonComplete.jsonResponse()

    def startStress(self, docFile, docType, count, index):

        if (count <= 0):
            return

        iter = 0
        docID = 's' + str(index) + '_' + str(iter)

        # store docFile for the first iteration

        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.' + docType
        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(fileStoreLocation, 'wb')
        else:
            storedFile = open(fileStoreLocation, 'w')
        while True:
            data = docFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)

        storedFile.close()

        # now make copies for all remaining iterations

        for i in range(1, string.atoi(count)):
            if sys.platform.startswith('win'):
                shellCmd = 'copy ' + fileStoreLocation + ' ' + os.path.join(mediaDirectory, 's' + str(index) + '_' + str(i)) + '.' + docType
            else:
                shellCmd = 'cp ' + fileStoreLocation + ' ' + os.path.join(mediaDirectory, 's' + str(index) + '_' + str(i)) + '.' + docType
            print shellCmd
            os.system(shellCmd)

        # start conversions for each of the iterations

        for i in range(0, string.atoi(count)):
            print self.startDocumentConversion('s' + str(index) + '_' + str(i), 'stress' + str(index) + '_' + str(i),'ROOMID','SESSIONID', 'stress', docType, '')

    def uploadPreloadedDocumentWithPath(self, filePath, fileName, meetingID, docID, docType, strictJSON = False):

        if docID == 'GENERATE' or docID == '':
            docID = idgen.gen()

        docPath = os.path.join(osconfig.preloadedDocumentRoot(),filePath)
        if not os.path.isfile(docPath):
            return 'invalid file location'

        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.' + docType

        # copy the file from location to media directory

        if sys.platform.startswith('win'):
            os.system('copy ' + docPath + ' ' + fileStoreLocation)
        else:
            os.system('cp ' + docPath + ' ' + fileStoreLocation)

        if len(fileName) == 0:
            fileName = os.path.basename(docPath)
        xPorter = exportEngine(meetingID, '', '', fileStoreLocation, docID, 'preloaded', fileName, docType, self.officeLock, self.pdfLock, self.collator)

        self.processMap[docID] = xPorter
        xPorter.start()
        
        if not strictJSON:
            # JSON Response BEGIN
            jsondata = jsonObject()
            jsondata.clearResponse()
            jsondata.add('result','true')
            jsondata.add('method','uploadPreloadedDocumentWithPath')
            jsondata.add('docName',fileName)
            jsondata.add('docType',docType)
            jsondata.add('docID',docID)
            #JSON Response END
    
            return jsondata.jsonResponse()
        else:
            jsondata = {'result': 'true', 'method' : 'uploadPreloadedDocumentWithPath', 'docName': fileName, 'docType' : docType, 'docID' : docID}
            response = self.demHelper.encode(jsondata)
            return response.encode()

    def uploadPreloadedDocumentWithURL(self, fileURL, fileName, meetingID, docID, docType, strictJSON = False):

         # fileName can be empty. If that is the case, we take the name from the url using os.path.basename

        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.' + docType

        # download the file to media directory

        retval = self.curlHandle.downloadToFileFromHTTPURL(fileURL, fileStoreLocation)
        if retval == 0:
            return 'unable to download file'
        if retval == -1:
            return 'internal exception in downloading file'
        if retval == -2:
            return 'unable to create local copy due to access privileges'

        if len(fileName) == 0:
            fileName = os.path.basename(fileURL)
        xPorter = exportEngine(meetingID, '', '', fileStoreLocation, docID, 'preloaded', fileName, docType, self.officeLock, self.pdfLock, self.collator)

        self.processMap[docID] = xPorter
        xPorter.start()
        
        if not strictJSON:
            # JSON Response BEGIN
            jsondata = jsonObject()
            jsondata.clearResponse()
            jsondata.add('result','true')
            jsondata.add('method','uploadPreloadedDocumentWithURL')
            jsondata.add('docName',fileName)
            jsondata.add('docType',docType)
            jsondata.add('docID',docID)
            #JSON Response END
            
            return jsondata.jsonResponse()
        
        else:
            strictData = {'result' : 'true', 'method' : 'uploadPreloadedDocumentWithURL', 'docName' : fileName, 'docType' : docType, 'docID': docID}
            response = self.demHelper.encode(strictData)
            return response.encode()

    def uploadPreloadedDocument(self, docFile, fileName, meetingID, docID, docType, strictJSON = False):

         # fileName can be empty. If that is the case, we take the name from the file object

        if docID == 'GENERATE' or docID == '':
            docID = idgen.gen()

        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.' + docType
        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(fileStoreLocation, 'wb')
        else:
            storedFile = open(fileStoreLocation, 'w')
        while True:
            data = docFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)

        storedFile.close()

        if len(fileName) == 0:
            fileName = os.path.basename(docFile.filename)

        xPorter = exportEngine(meetingID, '', '', fileStoreLocation, docID, 'preloaded', fileName, docType, self.officeLock, self.pdfLock, self.collator)

        self.processMap[docID] = xPorter
        xPorter.start()

        if not strictJSON:
            # JSON Response BEGIN
            jsondata = jsonObject()
            jsondata.clearResponse()
            jsondata.add('result','true')
            jsondata.add('method','uploadPreloadedDocument')
            jsondata.add('docName',fileName)
            jsondata.add('docType',docType)
            jsondata.add('docID',docID)
            #JSON Response END
    
            return jsondata.jsonResponse()
        else:
            strictData = {'result' : 'true', 'method' : 'uploadPreloadedDocument', 'docName' : fileName, 'docType' : docType, 'docID': docID}
            response = self.demHelper.encode(strictData)
            return response.encode()


    def uploadDocument(self, docFile, meetingID, roomID, sessionID, docID, docType, uploadType):

        # 'uploadType' accepts "global" or "preloaded". If neither is specified, it is assumed as a regular presentation.
        # if the type is global, "global-meeting" is used as a meeting key. Any meeting ID specified is discarded
        oosetupsemaphore = threading.BoundedSemaphore(1)
        if not docType == 'pdf':
            #print 'in upload document function'
            self.isOOHealthy = self.checkOO()
            #print self.isOOHealthy+' ooHealthy'
            if self.isOOHealthy=="False":
                oosetupsemaphore.acquire()
                self.startOO()
                oosetupsemaphore.release()
        uploadType = string.lower(uploadType)

        if docID == 'GENERATE' or docID == '':
            docID = idgen.gen()

        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.' + docType
        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(fileStoreLocation, 'wb')
        else:
            storedFile = open(fileStoreLocation, 'w')
        while True:
            data = docFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)

        storedFile.close()
        xPorter = exportEngine(meetingID, roomID, sessionID, fileStoreLocation, docID, uploadType, os.path.basename(docFile.filename), docType, self.officeLock, self.pdfLock, self.collator)

        self.processMap[docID] = xPorter
        xPorter.start()

        # JSON Response BEGIN
        jsondata = jsonObject()
        jsondata.clearResponse()
        jsondata.add('result','true')
        jsondata.add('method','uploadDocument')
        jsondata.add('docName',os.path.basename(docFile.filename))
        jsondata.add('docType',docType)
        jsondata.add('docID',docID)
        #JSON Response END

        return jsondata.jsonResponse()

    def retrieveDocument(self, docID, meetingID, pageNo):
        [installationID, dimdimID] = osconfig.splitIdentity(meetingID)
        meetingID = installationID + '____' + dimdimID
        # JSON Response BEGIN (error message)
        jsondata = jsonObject()
        jsondata.clearResponse()
        jsondata.add('result','false')
        jsondata.add('method','retrieveDocument')
        jsondata.add('docID',docID)
        jsondata.add('meetingID',meetingID)
        jsondata.add('error', '7404')
        #JSON Response END

        if (docID == ''):
            return jsondata.jsonResponse()

        if sys.platform.startswith('win'):
            archiveDirectory = string.rstrip(osconfig.slidedeckArchive(), '\\')
        else:
            archiveDirectory = osconfig.slidedeckArchive()

        localDocDir = os.path.join(os.path.join(archiveDirectory, meetingID), docID)
        preloadedDocDir = os.path.join(os.path.join(os.path.join(archiveDirectory, meetingID), 'Preloaded'), docID)
        globalDocDir = os.path.join(os.path.join(archiveDirectory, 'global-meeting'), docID)

        docPath = None
        if os.path.exists(localDocDir):
            docPath = localDocDir
        if os.path.exists(preloadedDocDir):
            docPath = preloadedDocDir
        if os.path.exists(globalDocDir):
            docPath = globalDocDir

        if not docPath:
            self.collator.registerRetrieveFailure()
            return jsondata.jsonResponse()

        fileList = os.listdir(docPath)
        if len(fileList) <= 1:  # we need *atleast* 2 files. i.e. atleast 1 swf and 1 meta data file
            self.collator.registerRetrieveFailure()
            return jsondata.jsonResponse()

        swfpath = docPath
        jpgpath = docPath

        if sys.platform.startswith('win'):
           jpgpath += '\\' + pageNo + '.jpg'
           swfpath += '\\' + pageNo + '.swf'
        else:
           jpgpath += '/' + pageNo + '.jpg'
           swfpath += '/' + pageNo + '.swf'

        path = ''

        if os.path.isfile(swfpath):
            path = swfpath
        elif os.path.isfile(jpgpath):
            path = jpgpath
        else:
            self.collator.registerRetrieveFailure()
            return jsondata.jsonResponse()
        self.collator.resetRetrieveFailures()
        return static.serve_file(path,"application/octet-stream","inline",os.path.basename(path))

    def deletePreloadedDocuments(self, meetingID, strictJSON = False):

        if sys.platform.startswith('win'):
            archiveDirectory = string.rstrip(osconfig.slidedeckArchive(), '\\')
        else:
            archiveDirectory = osconfig.slidedeckArchive()

        preloadedDocDir = os.path.join(os.path.join(archiveDirectory, meetingID), 'Preloaded')

        try:
            shutil.rmtree(preloadedDocDir)
        except:
            pass

        if not strictJSON:
            # JSON Response BEGIN
            jsondata = jsonObject()
            jsondata.clearResponse()
            jsondata.add('result','true')
            jsondata.add('method','deletePreloadedDocuments')
            jsondata.add('meetingID', meetingID)
            jsondata.add('error', '7200')
            #JSON Response END
    
            return jsondata.jsonResponse()
        else:
            jsondata = {'result' : 'true', 'method' : 'deletePreloadedDocuments', 'meetingID': meetingID, 'error' : 7200}
            response = self.demHelper.encode(jsondata)
            return response.encode()

    def deleteDocument(self, docID, meetingID, strictJSON = False):

        # JSON Response BEGIN (error message)
        
        strictdata = {'result' : 'false', 'method' : 'deleteDocument', 'docID' : docID, 'meetingID' : meetingID, 'error' : 7404}
        
        jsondata = jsonObject()
        jsondata.clearResponse()
        jsondata.add('result','false')
        jsondata.add('method','deleteDocument')
        jsondata.add('docID', docID)
        jsondata.add('meetingID', meetingID)
        jsondata.add('error', '7404')
        #JSON Response END

        if (docID == ''):
            if not strictJSON:
                return jsondata.jsonResponse()
            else:
                response = self.demHelper.encode(strictdata)
                return response.encode()
            

        if sys.platform.startswith('win'):
            archiveDirectory = string.rstrip(osconfig.slidedeckArchive(), '\\')
        else:
            archiveDirectory = osconfig.slidedeckArchive()

        # check if the directory exists, in global, preloaded and local

        localDocDir = os.path.join(os.path.join(archiveDirectory, meetingID), docID)
        preloadedDocDir = os.path.join(os.path.join(os.path.join(archiveDirectory, meetingID), 'Preloaded'), docID)
        globalDocDir = os.path.join(os.path.join(archiveDirectory, 'global-meeting'), docID)

        docPath = None
        if os.path.isdir(localDocDir):
            docPath = localDocDir
        if os.path.isdir(preloadedDocDir):
            docPath = preloadedDocDir
        if os.path.isdir(globalDocDir):
            if meetingID != 'global-meeting':   # we have already taken care of this
                docPath = globalDocDir

        if not docPath:
            return jsondata.jsonResponse()

        try:
            shutil.rmtree(docPath)
        except:
            pass

        if not strictJSON:
            # JSON Response BEGIN
            jsondata = jsonObject()
            jsondata.clearResponse()
            jsondata.add('result','true')
            jsondata.add('method','deleteDocument')
            jsondata.add('docID', docID)
            jsondata.add('meetingID', meetingID)
            jsondata.add('error', '7200')
            #JSON Response END
    
            return jsondata.jsonResponse()
        else:
            
            strictdata = {'result' : 'true', 'method' : 'deleteDocument', 'docID' : docID, 'meetingID' : meetingID, 'error' : 7200}
            response = self.demHelper.encode(strictdata)
            return response.encode()

    def closeMeeting(self, meetingID):

        # JSON Response BEGIN (error message)
        jsonError = jsonObject()
        jsonError.clearResponse()
        jsonError.add('result','false')
        jsonError.add('method','closeMeeting')
        jsonError.add('meetingID',meetingID)
        jsonError.add('error','7404')
        #JSON Response END

        # Do meetingID-related input verification
        if (meetingID == ''):
            return jsonError.jsonResponse()

        if sys.platform.startswith('win'):
            archiveDirectory = string.rstrip(osconfig.slidedeckArchive(), '\\')
        else:
            archiveDirectory = string.rstrip(osconfig.slidedeckArchive(), '/')

        localMeetingDir = os.path.join(archiveDirectory, meetingID)

        if not os.path.isdir(localMeetingDir):
            return jsonError.jsonResponse()

        docList = os.listdir(localMeetingDir)

        if len(docList) == 0:
            return jsonError.jsonResponse()

        for i in range (0, len(docList)):
            if docList[i] == 'Preloaded': # we don't delete preloaded directories
                continue
            docPath = os.path.join(localMeetingDir, docList[i])
            try:
                shutil.rmtree(docPath)
            except:
                pass

        docList = os.listdir(localMeetingDir)
        if len(docList) == 0:
            try:
                os.rmdir(localMeetingDir)
            except:
                pass

        # JSON Response BEGIN
        jsonSuccess = jsonObject()
        jsonSuccess.clearResponse()
        jsonSuccess.add('result','true')
        jsonSuccess.add('method','closeMeeting')
        jsonSuccess.add('meetingID',meetingID)
        jsonSuccess.add('error', '7200')
        #JSON Response END

        return jsonSuccess.jsonResponse()

    def uploadOnlyDocument(self, meetingID, docID, docType, docFile):
        
        # 'uploadType' accepts "global" or "preloaded". If neither is specified, it is assumed as a regular presentation.
        # if the type is global, "global-meeting" is used as a meeting key. Any meeting ID specified is discarded
        oosetupsemaphore = threading.BoundedSemaphore(1)
        if not docType == 'pdf':
            #print 'in upload document function'
            self.isOOHealthy = self.checkOO()
            #print self.isOOHealthy+' ooHealthy'
            if self.isOOHealthy=="False":
                oosetupsemaphore.acquire()
                self.startOO()
                oosetupsemaphore.release()
                
        if docID == 'GENERATE' or docID == '':
            docID = idgen.gen()
            
        fileStoreLocation = os.path.join(mediaDirectory, docID)
        fileStoreLocation += '.' + docType
        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(fileStoreLocation, 'wb')
        else:
            storedFile = open(fileStoreLocation, 'w')
        while True:
            data = docFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)
            
        storedFile.close()
        # JSON Response BEGIN
        jsondata = jsonObject()
        jsondata.clearResponse()
        jsondata.add('result','true')
        jsondata.add('method','uploadOnlyDocument')
        #jsondata.add('docName',os.path.basename(docFile.filename))
        jsondata.add('docType',docType)
        jsondata.add('docID',docID)
        #JSON Response END

        return jsondata.jsonResponse()

    def startOO(self):
       cherrypy.log('waiting to start openoffice---')
       status = self.getOpenOfficeContext()
       if status == "Success":
           cherrypy.log('openoffice started successfully---')
       else:
           cherrypy.log('openoffice couldnot get start even after waiting for it---')


        #print cmd

    def checkOO(self):
        local = uno.getComponentContext()
        resolver = local.ServiceManager.createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", local)
        try:
            context = resolver.resolve(self.resolveUrl)
            cherrypy.log('OpenOffice was already running---')
            self.isOOHealthy="True"
            return "True"
        except:
            cherrypy.log('killing All openoffice process---')
            if sys.platform.startswith('win'):
                cmd = 'tskill soffice'
            else:
                cmd = 'killall -9 soffice.bin'
            try:
                os.system(cmd)
            except:
               cherrypy.log('OpenOffice could not get killed---')
            self.isOOHealthy="False"
            return "False"

    def getOpenOfficeContext(self):
        cherrypy.log('starting oo process')
        if sys.platform.startswith('win'):
            cmd = 'c:\\www\\mods\\RunHiddenConsole.exe StartOO.bat'
            os.system(cmd)
        else:
            args = ['/usr/local/dimdim/Mediaserver/mods/StartOO.sh']
            popen2.Popen3(args)
            #cmd = 'nohup sh StartOO.sh 2>&1 &'
        #os.system(cmd)
        bSuccess = False
        nLoop = 5
        while True:
            try:
                context = resolver.resolve(self.resolveUrl)
                bSuccess = True
                return "Success"
            except:
                nLoop -= 1
                if nLoop <= 0:
                    cherrypy.log('Error in resolving url resolver---')
                    return "Failure"
                time.sleep(2)  # Sleep 1 second.

        if bSuccess == False:
            return "Failure"
