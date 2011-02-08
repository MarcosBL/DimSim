# The conversion engine any document and converts it to a bunch of SWFs.
# As of 041508, we support only PPT/PPTX/PDF conversions in the production. However,
# the engine is ready to accept any document.

import os
import sys
import popen2
import string
import pycurl
import shutil
import cherrypy

from threading import Timer
from threading import BoundedSemaphore

#import win32com.client, pythoncom

from toolkit import osconfig
from toolkit import filehelper
from toolkit.dThread import dThread

selfDestructTime = 360.0 # in seconds
mediaArchive = osconfig.mediaDirectory()
oopath = osconfig.ooPath()
slidedeckArchive = osconfig.slidedeckArchive()
mailboxURL = osconfig.mailboxURL()
if sys.platform.startswith('win'):
    import win32process
    import win32file
    import win32com.client, pythoncom
else:
    sys.path.append(oopath)

import uno
import unohelper
from com.sun.star.beans import PropertyValue

class scopedLock(object):

    # A simple scoped lock class.
    # Acquires a bounded semaphore on creation and releases it when it goes out of scope

    def __init__(self, semaphore, method, docID):
        self.lock = semaphore
        self.lock.acquire()
        self.method = method
        self.docID = docID
        #print 'acquired by ' + self.method + ' docID = ' + self.docID
    def __del__(self):
        self.lock.release()
        #print 'released by ' + self.method + ' docID = ' + self.docID

class exportEngine(dThread):

    # conversion related members

    docID = None

    width = None
    height = None

    docType = None
    docName = None
    location = None
    meetingID = None
    roomID = None
    sessionID = None
    installationID = None

    errCode = None
    uploadType = None

    totalPageCount = None
    pagesConverted = None

    conversionComplete = None

    # management members

    pdfLock = None              # This will control number of simultaneous pdf to swf conversions
    stateLock = None            # Internal lock to synchronize state management
    officeLock = None           # This will control number of simultaneous ppt to pdf conversions

    collator = None             # This will help collate statistical information about the job

    pdfLocked = None            # This will help syncing pdfLock, especially during cancel conversion or when self destruct is triggered.
    officeLocked = None         # This will help syncing officeLock, especially during cancel conversion or when self destruct is triggered.

    destructTimer = None        # After timeout, this will trigger a self destruct

    conversionState = None      # Keeps track of stage of conversion. Mainly useful during cancel conversion and self destruct

    # Valid conversion states - CREATED, PPT2PS, PS2PDF, PDF2SWF, POSTPROC, CANCELLED, COMPLETED

    def __init__(self, meetingID, roomID, sessionID, location, docID, uploadType, docName, docType, officeLock, pdfLock, collator):

        dThread.__init__(self)
        [installationID, dimdimID] = osconfig.splitIdentity(meetingID)
        self.meetingID = installationID + '____' + dimdimID

        self.docID              = docID

        self.width              = -1
        self.height             = -1

        self.docType            = docType
        self.docName            = docName
        self.location           = location
        #self.meetingID          = meetingID
        self.sessionID          = sessionID

        self.roomID             = roomID
        self.errCode            = 7200
        self.uploadType         = uploadType

        self.totalPageCount     = 0
        self.pagesConverted     = 0

        self.conversionComplete = False

        self.pdfLock            = pdfLock
        self.stateLock          = BoundedSemaphore(1)
        self.officeLock         = officeLock

        self.collator           = collator

        self.pdfLocked          = False
        self.officeLocked       = False

        self.destructTimer      = None

        self.conversionState    = 'CREATED'

        return

    def harakiri(self):
        self.collator.setConversionState(self.docID, 'done')
        self.setErrorCode(7408)
        self.cancelConversion()

    def cancelConversion(self, bSelfDestruct=True):

        # set conversion state to 'CANCELLED'
        self.setConversionState('CANCELLED')

        # set job status to collator
        self.collator.setConversionState(self.docID, 'done')

        # cancel timer if necessary.
        # if this is a self-destruct call, no need to cancel timer

        try:

            del self.destructTimer
            self.destructTimer = None

        except:
            #print 'exception: cancelconversion1'
            pass

        # reset office stuff if necessary

        if self.officeLocked:
            try:
                # kill office and ghostscript
                #cherrypy.log('killing office for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
                #self.killOffice()

                # release office semaphore
                cherrypy.log('releasing office for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
                self.officeRelease()

            except:
                #print 'exception: cancelconversion2'
                pass

        # reset pdf stuff if necessary

        if self.pdfLocked:
            try:
                # release pdf semaphore
                cherrypy.log('releasing pdflock for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
                self.pdfRelease()
            except:
                #print 'exception: cancelconversion3'
                pass

        # remove any temporary files
        try:
            os.remove(self.location)
        except:
            #print 'exception: cancelconversion4'
            pass

        if not bSelfDestruct:
            cherrypy.log(':::CANCEL::: cleanup done for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        else:
            cherrypy.log(':::HARAKIRI::: cleanup done for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        try:
            if sys.platform.startswith('win'):
                shellCmd = ''
            else:
                shellCmd = 'kill -9 ' + '`ps -ef | grep '+ self.docID +'| grep -v grep | sort | awk '+ "'{print $2}'`"
            #print 'value is '+shellCmd
            #f=open('/tmp/workfile', 'w')
            #f.write(shellCmd)
            #f.close()
            os.system(shellCmd)
        except:
            pass

        exit()

    def run(self):

        cherrypy.log('\n\n\n')
        cherrypy.log('DMS build 041708 - Job created... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)

        # start the self destruct timer
        self.destructTimer = Timer(selfDestructTime, self.harakiri)
        self.destructTimer.start()

        # initiate conversion
        self.convertDocument()

        self.setConversionState('COMPLETED')

        # job completed successfully.
        self.collator.setConversionState(self.docID, 'done')
        cherrypy.log('Job completed... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)

        # conversion complete. cancel timer
        try:
            self.destructTimer.cancel()
        except:
            #print 'exception: run after job'
            pass

        return

    def convertDocument(self):
        storePath = ''
        #print 'in convert document'
        try:
            storePath = self.getPDFLocation()
            #print storePath
        except:
            cherrypy.log('Conversion could have been cancelled... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            if self.getErrorCode() == 7200:
                self.setErrorCode(7406)
            return

        if storePath == '':
            tempError = self.getErrorCode()
            self.setErrorCode(tempError)
            return

        if self.getConversionState() == 'CANCELLED':
            return

        retval = self.pdfAcquire()
        if not retval:
            if self.getErrorCode() == 7200:
                self.setErrorCode(7406)
            return

        cherrypy.log('PDF available. Proceeding to SWF creation... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        if sys.platform.startswith('win'):
            infile = storePath + '\\conv.pdf'
        else:
            infile = storePath + 'conv.pdf'
#        print infile
        if self.getConversionState() == 'CANCELLED':
            return

        cherrypy.log('Retrieving page count... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        self.getPageCount(storePath)

        if self.totalPageCount == 0:
            cherrypy.log('Invalid page count... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            if (self.getErrorCode() != 7901 and self.getErrorCode() != 7902):
                self.setErrorCode(7406)
            self.pdfRelease()

            return

        if self.getConversionState() == 'CANCELLED':
            return

        cherrypy.log('Retrieving page dimensions... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        self.getPageDimensions(storePath)
        self.pagesConverted = 1

        self.setConversionState('PDF2SWF')

        for i in range(2, self.totalPageCount + 1):

            if self.getConversionState() == 'CANCELLED':
                return

            if self.getErrorCode() != 7200:
                cherrypy.log('Must have detected self-kill... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
                self.pdfRelease()
                return
            if sys.platform.startswith('win'):
                outfile = storePath + '\\' + str(i - 1) + '.swf'
                shellCmd = 'c:\\swftools\\pdf2swf.exe -qq -p ' + str(i) + ' ' + infile + ' -o ' + outfile
            else:
                outfile = storePath + '/' + str(i - 1) + '.swf'
                shellCmd = 'pdf2swf -qq -p ' + str(i) + ' ' + infile + ' -o ' + outfile

            try:
                os.system(shellCmd)
                print shellCmd
                self.pagesConverted += 1
            except:
                #print 'exception: convertDocument1'
                pass

            if not os.path.isfile(outfile):
                if sys.platform.startswith('win'):
                    shellCmd = 'copy ' + mediaArchive + '\\placeholder.swf' ' ' + outfile
                else:
                    shellCmd = 'cp ' + mediaArchive + '/placeholder.swf' ' ' + outfile
                os.system(shellCmd)

        if self.getConversionState() == 'CANCELLED':
            return

        self.pdfRelease()

        self.setConversionState('POSTPROC')

        cherrypy.log('Burning document information to disk... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        storeObject = filehelper.serialObject()
        storeObject.clearBuffer()
        storeObject.add('docName',self.docName)
        storeObject.add('docID',self.docID)
        storeObject.add('noOfPages',self.totalPageCount)
        storeObject.add('width', self.width)
        storeObject.add('height', self.height)
        storeObject.exportData(storePath+'documentData.txt')

        self.conversionComplete = True

        # upload the pdf to mail box
        curlHandle = pycurl.Curl()

        formData = []
        formData.append(('dimdimId', self.meetingID))
        formData.append(('roomId', self.roomID))
        formData.append(('sessionId', self.sessionID))
        formData.append(('fileName', self.docID + '.pdf'))
        formData.append(('fileType', 'PPT'))
        formData.append(('myFile', (pycurl.FORM_FILE, infile)))

        curlHandle.setopt(curlHandle.URL, mailboxURL+'uploadSessionFile')
        curlHandle.setopt(curlHandle.HTTPPOST, formData)
        curlHandle.setopt(pycurl.HTTPHEADER, ["Expect: "])

        try:
            curlResponse = curlHandle.perform()
        except:
            cherrypy.log('Caught exception trying to upload pdf to mailbox... ' + self.meetingID + '/' + self.roomID + '/' + self.sessionID)


        try:
            os.remove(self.location)
        except:
            #print 'exception: convertDocument1'
            pass

        return

    def getPDFFromOfficeDocument(self):
        #print 'in getPDFFROMofficeDocument before calling generic storePath'
        if self.getConversionState() == 'CANCELLED':
            return ''
        storePath = self.getGenericStorePath()
        #print storePath+'in getPDFFROMofficeDocument'
        infile = self.location
        #print infile
        local = uno.getComponentContext()
        resolver = local.ServiceManager.createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", local)
        resolveUrl = "uno:socket,host=localhost,port=8100;urp;StarOffice.ComponentContext"
        context = resolver.resolve(resolveUrl)
        try:
            desktopService = context.ServiceManager.createInstance("com.sun.star.frame.Desktop")
          #  print 'desktop service done'
        except:
            cherrypy.log('Error in getting desktop service... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            self.setErrorCode(7909)
            return ''

        if self.getConversionState() == 'CANCELLED':
            return ''

        if sys.platform.startswith('win'):
            outfile = storePath + 'conv.pdf'
        else:
            outfile = storePath + '/conv.pdf'

        outtempFile = storePath + '\\conv.ps'

        args = (PropertyValue("Hidden", 0, True, 0),)
        url = unohelper.systemPathToFileUrl(self.location)
        try:
            sourceFile = desktopService.loadComponentFromURL(url,"_blank", 0, args)
            #print sourceFile
            #iPageCount = sourceFile.getDrawPages().getCount()
            #self.totalPageCount = iPageCount
            #print self.totalPageCount
        except:
            cherrypy.log('Error has occured in loadComponentFromURL... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            self.setErrorCode(7406)
            return ''

        if self.getConversionState() == 'CANCELLED':
            return ''

        filterName = PropertyValue()
        filterName.Name = 'FilterName'
        filterName.Value = 'impress_pdf_Export'
        storePath = self.getGenericStorePath()
        pdfPath = unohelper.systemPathToFileUrl(storePath)+ 'conv.pdf'
        #print pdfPath + ' in systemPathToFileUrl'
        urlData = PropertyValue()
        urlData.Name = 'URL'
        urlData.Value = pdfPath
        #print pdfPath
        try:
            sourceFile.storeToURL(unohelper.absolutize(unohelper.systemPathToFileUrl(self.getGenericStorePath()), unohelper.systemPathToFileUrl('conv.pdf')), (filterName,))
	    #sourceFile.storeToURL(pdfPath, (filterName,))
        except:
            cherrypy.log('Error in exporting document to PDF... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            self.setErrorCode(7407)
            storePath = ''

        if self.getConversionState() == 'CANCELLED':
            return ''

        try:
            sourceFile.dispose()
        except:
            cherrypy.log('Error in disposing document... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            pass

        return storePath


    def getPowerpointApp(self):
        pptApp = None

        try:
            # initialize COM
            pythoncom.CoInitializeEx(pythoncom.COINIT_APARTMENTTHREADED)
            pptApp = win32com.client.DispatchEx('Powerpoint.Application')

        except:
            #print 'exception: getPowerpointApp1'
            # Something seriously wrong, I guess
            return None

        return pptApp

    def resetPowerpointApp(self, pptApp):
        try:
            pptApp.Quit()
            pythoncom.CoUninitialize()
        except:
            #print 'exception: resetpowerpointapp1'
            pass

    def pdfAcquire(self):
        if self.getConversionState() == 'CANCELLED':
            return False

        try:
            if not self.pdfLocked:
                self.pdfLock.acquire()
                self.pdfLocked = True
                self.collator.setConversionState(self.docID, 'pdf2swf')
                cherrypy.log('Acquired pdf semaphore... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        except:
            cherrypy.log('pdfAcquire caught an exception for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            return False

        return True

    def pdfRelease(self):

        try:
            if self.pdfLocked:
                self.pdfLock.release()
                self.pdfLocked = False
                cherrypy.log('Released pdf semaphore for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        except:
            cherrypy.log('pdfRelease caught an exception for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            pass

        try:
            # semaphore released. wait for next step
            self.collator.setConversionState(self.docID, 'waiting')
        except:
            #print 'exception: pdfrelease1'
            pass

        return

    def officeAcquire(self):
        if self.getConversionState() == 'CANCELLED':
            # no need to acquire anything.. leave it
            return False

        try:
            if not self.officeLocked:
                self.officeLock.acquire()
                self.officeLocked = True
                self.collator.setConversionState(self.docID, 'office')
                cherrypy.log('Acquired office semaphore for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)

        except:
            cherrypy.log('officeAcquire caught an exception for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            return False

        return True

    def officeRelease(self):

        try:
            if self.officeLocked:
                self.officeLock.release()
                self.officeLocked = False
                cherrypy.log('Released office semaphore for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        except:
            cherrypy.log('officeRelease caught an exception for meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            pass

        try:
            # semaphore released. wait for next step
            self.collator.setConversionState(self.docID, 'waiting')
        except:
            #print 'exception: officerelease'
            pass

        return

    def validateContent(self):

        fileSize = None
        cherrypy.log('Checking if 0 byte... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        cherrypy.log(self.location)
        try:
            fileSize = os.stat(self.location)[6]
        except:
            cherrypy.log('File not found. 0 byte check failed... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            self.setErrorCode(7406)
            self.collator.setConversionState(self.docID, 'done')
            return
        # check for 0 byte files

        if fileSize == 0:
            cherrypy.log('0 byte check failed... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            self.setErrorCode(7406)
            self.collator.setConversionState(self.docID, 'done')
            return

        cherrypy.log('0 byte check passed... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)

        # check for > 20 MB files

        cherrypy.log('Max filesize check... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)

        if fileSize > 20 * 1024 * 1024:
            cherrypy.log('File size > 10MB. Check failed... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            self.setErrorCode(7900)
            return

        cherrypy.log('Max filesize check passed... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)

        return

    def getPDFLocation(self):

        cherrypy.log('Validating Content... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)

        self.validateContent()
        if self.getErrorCode() != 7200:
            cherrypy.log('Content validation failed... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
            os.remove(self.location)
            return ''

        #print 'in get PDFLocation '

        storePath = self.getGenericStorePath()

        if self.docType == 'pdf':
            if sys.platform.startswith('win'):
                shellCmd = 'copy ' + self.location + ' ' + storePath + '\\conv.pdf'
            else:
                shellCmd = 'cp ' + self.location + ' ' + storePath + '/conv.pdf'
            os.system(shellCmd)

        else:
            retval = self.officeAcquire()
            #print retval
            if not retval:
                #print 'failed in retval'
                return ''
            #print 'calling getPDFFromOfficeDocument'
            storePath = self.getPDFFromOfficeDocument()
           # print storePath
            self.officeRelease()

        return storePath

    def killOffice(self):

        if self.getConversionState() == 'PPT2PS':
            try:
                pythoncom.CoUninitialize()
            except:
                #print 'exception: killoffice1'
                pass

        cherrypy.log('Cleaning up any Powerpoint processes... meeting ID = ' + self.meetingID + ' and docID = ' + self.docID)
        shellCmd = 'taskkill /F /IM powerpnt.exe'
        try:
            stdout = os.popen(shellCmd)
            shellResponse = stdout.read()
            stdout.close()
        except:
            #print 'exception: killoffice2'
            pass

        return

    def getGenericStorePath(self):
        # Directory Structure
        #print 'in getGenericStorePath'
        #print slidedeckArchive
        if sys.platform.startswith('win'):
            #print 'in if loop checking win or lin'
            storePath = slidedeckArchive
            #print storePath
        else:
            storePath = slidedeckArchive + '/'

        # Check for a valid meeting-key. We don't allow .. or ../ or other variants.
        # Basically nothing which takes store location out of the correct directory structure.

        if self.uploadType != 'global' and self.meetingID == "":
            sourceFile.dispose()
            #print 'source file disposing'
            return ''
        #print ''
        if self.uploadType != 'global':
            storePath += self.meetingID
            #print storePath + ' if not global'
        if not os.path.isdir(storePath):
            os.makedirs(storePath)
            cherrypy.log('First path '+ storePath)
        else:
            if len(os.path.normpath(storePath)) < len(os.path.normpath(slidedeckArchive)):
                sourceFile.dispose()
                return ''

        # Valid meeting-key

        if self.uploadType == 'global':
            if (sys.platform.startswith('win')):
                storePath += 'global-meeting\\'
            else:
                storePath += 'global-meeting/' #global-meeting is the meetingid placeholder for global ppts
        elif self.uploadType == 'preloaded':
            if (sys.platform.startswith('win')):
                storePath += '\\Preloaded\\'
            else:
                storePath += '/Preloaded/'
        else:
            if (sys.platform.startswith('win')):
                storePath += '\\'
            else:
                storePath += '/'

        if not os.path.isdir(storePath):
            os.makedirs(storePath)
            cherrypy.log('Second path '+ storePath)

        if (sys.platform.startswith('win')):
            storePath = storePath + self.docID + '\\'
        else:
            storePath = storePath + self.docID + '/'

        # delete any previous data
        try:
            shutil.rmtree(storePath)
        except:
            pass
        os.makedirs(storePath)
        cherrypy.log('Last path '+ storePath)

        return storePath

    def getPageDimensions(self, storePath):
        if sys.platform.startswith('win'):
            widthShellCmd = 'c:\\swftools\\swfdump.exe --width ' + storePath + '0.swf'
            heightShellCmd = 'c:\\swftools\\swfdump.exe --height ' + storePath + '0.swf'
        else:
            widthShellCmd = 'swfdump --width ' + storePath + '0.swf'
            heightShellCmd = 'swfdump --height ' + storePath + '0.swf'
        widthBuf = None
        heightBuf = None

        try:
            stdout = os.popen(widthShellCmd)
            widthBuf = stdout.read()
            stdout.close()

            stdout = os.popen(heightShellCmd)
            heightBuf = stdout.read()
            stdout.close()

        except:
            # Set default width and height
            self.width = '842'
            self.height = '595'
            #print 'exception: getpagedims1'
            return

        widthBuf = widthBuf.lstrip('-X')
        widthBuf = widthBuf.lstrip(' ')
        widthBuf = widthBuf.rstrip('\n')

        heightBuf = heightBuf.lstrip('-Y')
        heightBuf = heightBuf.lstrip(' ')
        heightBuf = heightBuf.rstrip('\n')

        self.width = widthBuf
        self.height = heightBuf
        return

    def getPageCount(self, storePath):
        if sys.platform.startswith('win'):
            shellCmd = 'c:\\swftools\\pdf2swf.exe -p 1 ' + storePath + 'conv.pdf' ' -o ' + storePath + '0.swf'
        else:
            shellCmd = 'pdf2swf -p 1 ' + storePath + 'conv.pdf' ' -o ' + storePath + '0.swf'
        buf = None
        delveError = False

        try:
            stdout = os.popen(shellCmd)
            buf = stdout.read()
            stdout.close()
        except:
            #print 'exception: getpagecount1'
            delveError = True
            return
        lines = buf.split('\n')

        # check for any errors
        for l in lines:
            if (l.find('ERROR') >= 0 and l.find('ERROR') <= len(l)):
                # some error found
                delveError = True
                break

        if delveError:
            for l in lines:
                if (l.find('document as broken') >= 0 and l.find('document as broken') <= len(l)):
                    self.setErrorCode(7901)
                    return
                if (l.find('PDF disallows copying') >= 0 and l.find('PDF disallows copying') <= len(l)):
                    self.setErrorCode(7902)
                    return
            #self.setErrorCode(7903)
            return

        for l in lines:
            if l.find('Pages:') >= 0:
                l = l.lstrip('Pages:')
                l = l.lstrip(' ')

                try:
                    self.totalPageCount = string.atoi(l)
                except:
                    #print 'exception: getpagecount2'
                    self.totalPageCount = 0

        if not os.path.isfile(storePath + '0.swf'):
            if sys.platform.startswith('win'):
                shellCmd = 'copy ' + mediaArchive + '\\placeholder.swf' ' ' + storePath + '0.swf'
            else:
                shellCmd = 'cp ' + mediaArchive + '/placeholder.swf' ' ' + storePath + '0.swf'
            os.system(shellCmd)

        return

    def setErrorCode(self, code):
        lock = scopedLock(self.stateLock, 'setErrorCode', self.docID)
        self.errCode = code

    def getErrorCode(self):
        lock = scopedLock(self.stateLock, 'getErrorCode', self.docID)
        return self.errCode

    def setConversionState(self, state):
        lock = scopedLock(self.stateLock, 'setConversionState', self.docID)
        self.conversionState = state

    def getConversionState(self):
        lock = scopedLock(self.stateLock, 'getConversionState', self.docID)
        return self.conversionState
