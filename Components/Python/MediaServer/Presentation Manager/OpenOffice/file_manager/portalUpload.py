import os
import sys
import fileMgr
import zipfile
from toolkit import osconfig
from toolkit import unzip

archiveLoc = osconfig.filemgrArchive()


def uploadLogo(logoFile, meetingID, logoType):
    
    # Upload Logo accepts a logo file (image file) of different types - .png, .jpeg etc.
    try:
        fileMgr.deleteImageFiles(meetingID)
        dirPath = os.path.join(archiveLoc, meetingID)
        if not os.path.exists(dirPath):
            os.makedirs(dirPath)
        fileStoreLocation = os.path.join(dirPath, 'dimdim_logo')
        fileStoreLocation += '.' + logoType

        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(fileStoreLocation, 'wb')
        else:
            storedFile = open(fileStoreLocation, 'w')
        while True:
            data = logoFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)

        storedFile.close()

        return 'The file ' + os.path.basename(fileStoreLocation) + ' has been uploaded'
    except:
        return 'There was an error uploading the file, please try again!'
    
def sanitizePath(location):
    locList = []
    data = os.path.splitdrive(location)
    drive = data[0]
    data = os.path.split(data[1])
    locList.append(data[1])
    try:
        while True:
            data = os.path.split(data[0])
            if data[0] == '\\' and data[1] == '':
                break
            locList.append(data[1])
    except:
        pass
    locList.append(drive)
    san = ''
    san = locList[0]
    for i in range(1, len(locList)):
        san = locList[i] + '\\\\' + san
    
    return san

def uploadZip(zipFile, meetingID, zipName):
    try:
        dirPath = os.path.join(archiveLoc, meetingID)
#        print dirPath
        if not os.path.exists(dirPath):
            os.makedirs(dirPath)
        zipStoreLocation = os.path.join(dirPath, zipName)
        # comment the following line if zipName has .zip extension included
        # zipStoreLocation += '.zip'
        storedFile = None
        if sys.platform.startswith('win'):
            storedFile = open(zipStoreLocation, 'wb')
        else:
            storedFile = open(zipStoreLocation, 'w')
 #       print storedFile
        while True:
            data = zipFile.file.read(8192)
            if not data:
                break
            storedFile.write(data)
            
        storedFile.close()
            
        unzipper = unzip.unzip()
        unzipper.extract(zipStoreLocation, dirPath)
        
        os.remove(zipStoreLocation)

    except:
        return 'There was an error uploading the zip file, please try again!'
