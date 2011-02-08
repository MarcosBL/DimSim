import os
import string
import shutil
import cherrypy
from threading import BoundedSemaphore
from toolkit import osconfig

class scopedLock(object):

    # A simple scoped lock class.
    # Acquires a bounded semaphore on creation and releases it when it goes out of scope

    def __init__(self, semaphore):
        self.lock = semaphore
        self.lock.acquire()
    def __del__(self):
        self.lock.release()

class Collator(object):
    statsMap = None
    statsLock = None
    pptFailureCount = None
    pdfFailureCount = None
    retrieveFailureCount = None
    
    def __init__(self):
        try:
            shutil.rmtree(osconfig.statisticsPath())
        except:
            pass
        os.makedirs(osconfig.statisticsPath())
        
        self.statsMap = {}
        self.statsLock = BoundedSemaphore(1)
        
        self.pptFailureCount = 0
        self.pdfFailureCount = 0
        self.retrieveFailureCount = 0
        
        
    def register(self, docID):
        lock = scopedLock(self.statsLock)
        self.statsMap[docID] = 'waiting'
        
    def unregister(self, docID):
        
        if not self.statsMap.get(docID):
            return
        
        # remove from statsMap
        try:
            self.statsMap.pop(docID)
        except:
            pass

        filePath = os.path.join(osconfig.statisticsPath(),docID)
        fp = open(filePath, 'w')
        fp.close()
        
    def resetRetrieveFailures(self):
        lock = scopedLock(self.statsLock)
        
        self.retrieveFailureCount = 0
        
    def registerRetrieveFailure(self):
        lock = scopedLock(self.statsLock)
        
        self.retrieveFailureCount += 1
        
    def resetPDFFailures(self):
        lock = scopedLock(self.statsLock)
        self.pdfFailureCount = 0
        
    def resetUploadFailures(self):
        lock = scopedLock(self.statsLock)
        
        self.pdfFailureCount = 0
        self.pptFailureCount = 0

    def registerPDFFailure(self, docID, meetingID):
        lock = scopedLock(self.statsLock)

        # getDocumentStatus will always have an error registered in the process's
        # errorCode. This needs to be detected and registered.

        # The intention is to detect 3 consecutive failures and put it up as critical
        # Even if one process succeeds, counter is set to 0

        # This needs to be tracked separately for both pdf2swf conversions and ppt2pdf
        # conversions

        # Whether office performance is registered of swftools performance is registered
        # depends on the docType

        cherrypy.log(':::INTERNAL PDF UPLOAD FAILURE detected for document ' + docID + 'in meeting ' + meetingID)
        self.pdfFailureCount += 1
        
    def registerPPTFailure(self, docID, meetingID):
        lock = scopedLock(self.statsLock)
        
        # getDocumentStatus will always have an error registered in the process's
        # errorCode. This needs to be detected and registered.
        
        # The intention is to detect 3 consecutive failures and put it up as critical
        # Even if one process succeeds, counter is set to 0
        
        # This needs to be tracked separately for both pdf2swf conversions and ppt2pdf
        # conversions
        
        # Whether office performance is registered of swftools performance is registered
        # depends on the docType
        
        cherrypy.log(':::INTERNAL PPT UPLOAD FAILURE detected for document ' + docID + 'in meeting ' + meetingID)
        self.pptFailureCount += 1
    
    def setConversionState(self, docID, state):
        
        lock = scopedLock(self.statsLock)
        
        if not self.statsMap.get(docID):
            return
        
        cherrypy.log(':::CONVERSION STATE getting set to ' + state + ' for docID = ' + docID)
        
        # States accepted -
        # 1. Waiting, 2. Office conversion 3. PDF2SWF conversion 4. Done
        # Names used - 1. waiting, 2. office 3. pdf2swf 4. done
        
        # we won't be having too many active presentations at a time.
        # May be a maximum of 500 or something.
        
        if state == 'done':
            self.unregister(docID)
        else:
            if self.statsMap.get(docID) != 'done':
                self.statsMap[docID] = state
            
        return
            
    def queryHealthState(self, param):
        
        lock = scopedLock(self.statsLock)

        param = string.lower(param)
        
        waitingCount = 0
        officeCount = 0  # Maximum 1 allowed.
        pdf2swfCount = 0
        completedCount = 0

        health = 'NORMAL'

        for value in self.statsMap.itervalues():
            if value == 'waiting':
                waitingCount += 1
            elif value == 'office':
                officeCount += 1
            else:
                pdf2swfCount += 1

        for root, dirs, files in os.walk(osconfig.statisticsPath()):
            completedCount += len(files)

        if officeCount == 0 and pdf2swfCount == 0 and waitingCount > 0:
            health = 'CRITICAL'

        if officeCount > 1:
            health = 'WARNING'
            
        if officeCount == 0 and waitingCount == 0 and pdf2swfCount >= (2* osconfig.maxPDFAccess()):
            health = 'WARNING'
            
        if self.pdfFailureCount >= 3:
            health = 'CRITICAL'
            
        if self.pptFailureCount >= 3:
            health = 'CRITICAL'
            
        if self.retrieveFailureCount >= 5:
            health = 'CRITICAL'
            
        if param == 'waiting':
            return str(waitingCount)
        
        if param == 'office':
            return str(officeCount)
        
        if param == 'pdf':
            return str(pdf2swfCount)
        
        if param == 'completed':
            return str(completedCount)
        
        if param == 'pptfailures':
            return str(self.pptFailureCount)
        
        if param == 'pdffailures':
            return str(self.pdfFailureCount)
        
        if param == 'retrievefailures':
            return str(self.retrieveFailureCount)
        
        return health
    
    def queryHealth(self):
        
        lock = scopedLock(self.statsLock)
        
        waitingCount = 0
        officeCount = 0  # Maximum 1 allowed.
        pdf2swfCount = 0
        completedCount = 0
        
        health = 'NORMAL >> '
        
        for value in self.statsMap.itervalues():
            if value == 'waiting':
                waitingCount += 1
            elif value == 'office':
                officeCount += 1
            else:
                pdf2swfCount += 1
                
        for root, dirs, files in os.walk(osconfig.statisticsPath()):
            completedCount += len(files)
            
        if officeCount == 0 and pdf2swfCount == 0 and waitingCount > 0:
            health = 'CRITICAL >> '
            
        if officeCount > 1:
            health = 'WARNING >> '
            
        if self.pdfFailureCount >= 3:
            health = 'CRITICAL >> '

        if self.pptFailureCount >= 3:
            health = 'CRITICAL >> '
            
        if self.retrieveFailureCount >= 5:
            health = 'CRITICAL >> '
            
        return health + ' Waiting = ' + str(waitingCount) + ', Office = ' + str(officeCount) + \
    ', Pdf2Swf = ' + str(pdf2swfCount) + ', Completed = ' + str(completedCount) + \
    ', Consecutive_PDF_Upload_Failures = ' + str(self.pdfFailureCount) + ', Consecutive_PPT_Upload_Failures = ' + str(self.pptFailureCount) + \
    ', Consecutive_Retrieve_Failures = ' + str(self.retrieveFailureCount)