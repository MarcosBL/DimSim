import os
import sys
import string
import shutil

from toolkit import osconfig
from toolkit.jsonhelper import jsonObject
from toolkit import filehelper

filemgrArchive = osconfig.filemgrArchive()

def privateFilesExist(meetingID):
    # JSON Response BEGIN (error message)
    jsonError = jsonObject()
    jsonError.clearResponse()
    jsonError.add('result','false')
    jsonError.add('method','privateFilesExist')
    jsonError.add('meetingID', meetingID)
    #JSON Response END
    
    # JSON Response BEGIN (success message)
    jsonSuccess = jsonObject()
    jsonSuccess.clearResponse()
    jsonSuccess.add('result','true')
    jsonSuccess.add('method','privateFilesExist')
    jsonSuccess.add('meetingID', meetingID)
    #JSON Response END
    
    if meetingID == '':
        return jsonError.jsonResponse()
    
    folderName = os.path.join(filemgrArchive, meetingID);
    
    if not os.path.exists(folderName):
        return jsonError.jsonResponse()
    
    filelist = os.listdir(folderName)
    
    if len(filelist) == 0:
        return jsonError.jsonResponse()
    
    fileListBuffer = '['
    for i in range (0, len(filelist)):
        if os.path.isdir(os.path.join(folderName, filelist[i])):
            continue
        fileJSON = jsonObject()
        fileJSON.clearResponse()
        fileJSON.add('fileName', filelist[i])
        fileListBuffer += fileJSON.jsonResponse()
        if i < (len(filelist) - 1):
            fileListBuffer += ','
            
    fileListBuffer += ']'
    jsonSuccess.add('files', fileListBuffer)
    accessPath = os.path.basename(filemgrArchive) + '/' + meetingID
    jsonSuccess.add('path',accessPath)
    return jsonSuccess.jsonResponse()

    jsonSuccess.add('files', filelist)
    accessPath = os.path.basename(filemgrArchive) + '/' + meetingID
    jsonSuccess.add('path',accessPath)
    return jsonSuccess.jsonResponse()

def deletePrivateFiles(meetingID):
    # JSON Response BEGIN (error message)
    jsonError = jsonObject()
    jsonError.clearResponse()
    jsonError.add('result','false')
    jsonError.add('method','deletePrivateFiles')
    #JSON Response END
    
    # JSON Response BEGIN (success message)
    jsonSuccess = jsonObject()
    jsonSuccess.clearResponse()
    jsonSuccess.add('result','true')
    jsonSuccess.add('method','deletePrivateFiles')
    #JSON Response END
    
    if meetingID == '':
        return jsonError.jsonResponse()
    
    pathEntries = filehelper.searchPathByKeyword(filemgrArchive, meetingID)
    
    if len(pathEntries) == 0:
        return jsonError.jsonResponse()
    
    if sys.platform.startswith('win'):
        accessPath = filemgrArchive + '\\' + meetingID
    else:
        accessPath = filemgrArchive + '/' + meetingID

    try:
        shutil.rmtree(accessPath)
    except:
        return jsonError.jsonResponse()
            
    return jsonSuccess.jsonResponse()

def deleteImageFiles(meetingID):
    # JSON Response BEGIN (error message)
    jsonError = jsonObject()
    jsonError.clearResponse()
    jsonError.add('result','false')
    jsonError.add('method','deleteImageFiles')
    #JSON Response END
    
    # JSON Response BEGIN (success message)
    jsonSuccess = jsonObject()
    jsonSuccess.clearResponse()
    jsonSuccess.add('result','true')
    jsonSuccess.add('method','deleteImageFiles')
    #JSON Response END
    
    if meetingID == '':
        return jsonError.jsonResponse()
    
    pathEntries = filehelper.searchPathByKeyword(filemgrArchive, meetingID)
    
    if len(pathEntries) == 0:
        return jsonError.jsonResponse()
    
    if sys.platform.startswith('win'):
        accessPath = filemgrArchive + '\\' + meetingID
    else:
        accessPath = filemgrArchive + '/' + meetingID

    try:
        for root, dirs, files in os.walk(accessPath):
            for name in files:
                if string.find(name, 'dimdim_logo.') >= 0:
                    if sys.platform.startswith('win'):
                        os.remove(accessPath + '\\' + name)
                    else:
                        os.remove(accessPath + '/' + name)
    except:
        return jsonError.jsonResponse()
           
    return jsonSuccess.jsonResponse()
