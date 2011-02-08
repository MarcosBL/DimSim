import os
import pycurl
import string
import urllib
import StringIO
import threading

import osconfig

class CurlWrapper(object):
    httpHandle = None
    httpHeaders = None
    httpLock = None

    def __init__(self):
        self.httpHeaders = None
        self.httpLock = threading.BoundedSemaphore(1)
        self.setupHandle()

    def __del__(self):
        self.httpHandle.close()
        del self.httpHandle

    def setupHandle(self):
        try:
            if self.httpHandle:
                self.httpHandle.close()
        except:
            pass

        try:
            del self.httpHandle
        except:
            pass

        self.httpHandle = None
        self.httpHandle = pycurl.Curl()
        self.httpHandle.setopt(pycurl.HTTPHEADER, osconfig.curlHeaders())
        self.httpHandle.setopt(pycurl.USERAGENT, osconfig.curlUserAgent())
        self.httpHandle.setopt(pycurl.FOLLOWLOCATION, 1)
        self.httpHandle.setopt(pycurl.CONNECTTIMEOUT, osconfig.curlConnectTimeout())
        self.httpHandle.setopt(pycurl.TIMEOUT, osconfig.curlTimeout())
        self.httpHandle.setopt(pycurl.COOKIEJAR, 'cookiejar.txt')
        self.httpHandle.setopt(pycurl.COOKIEFILE, 'cookiejar.txt')
        self.httpHandle.setopt(pycurl.HEADERFUNCTION, self.headersCallback)


    def headersCallback(self, buf):
        # Callback function invoked when header data is ready
        if self.httpHeaders:
            self.httpHeaders.write(buf)

        return

    def downloadToFileFromHTTPFormSubmit(self, url, method, paramDict, outputFile):
        self.httpLock.acquire()
        self.setupHandle()
        self.httpHeaders = StringIO.StringIO()

        oFile = None
        try:
            oFile = open(outputFile, 'wb')
        except:
            #unable to create a file for storing content
            self.httpLock.release()
            return -2

        if method == 'get':
            url += '?' + urllib.urlencode(paramDict)

            strippedURL = string.lstrip(url, 'http://')
            url = 'http://' + urllib.quote(strippedURL)

            self.httpHandle.setopt(pycurl.URL, url)
        else:

            strippedURL = string.lstrip(url, 'http://')
            url = 'http://' + urllib.quote(strippedURL)

            self.httpHandle.setopt(pycurl.URL, url)
            self.httpHandle.setopt(pycurl.POST, 1)
            self.httpHandle.setopt(pycurl.POSTFIELDS, urllib.urlencode(paramDict))

        self.httpHandle.setopt(pycurl.WRITEDATA, oFile)

        try:
            self.httpHandle.perform()
        except:
            #exception in performing HTTP action
            self.httpLock.release()
            return -1

        self.httpHeaders.flush()
        headerData = self.httpHeaders.getvalue()

        self.httpHeaders.close()
        del self.httpHeaders
        self.httpHeaders = None

        oFile.flush()
        oFile.close()

        if string.find(headerData, '200 OK') > 0:
            self.httpLock.release()
            return 1

        os.remove(outputFile)
        self.setupHandle()
        self.httpLock.release()
        return 0

    def downloadToFileFromHTTPURL(self, url, outputFile):

        self.httpLock.acquire()
        self.httpHeaders = StringIO.StringIO()

        oFile = None
        try:
            oFile = open(outputFile, 'wb')
        except:
            #unable to create a file for storing content
            self.httpLock.release()
            return -2

        strippedURL = string.lstrip(url, 'http://')
        url = 'http://' + urllib.quote(strippedURL)

        self.httpHandle.setopt(pycurl.URL, url)
        self.httpHandle.setopt(pycurl.WRITEDATA, oFile)

        try:
            self.httpHandle.perform()
        except:
            #exception in peforming HTTP action
            self.httpLock.release()
            return -1

        self.httpHeaders.flush()
        headerData =  self.httpHeaders.getvalue()

        self.httpHeaders.close()
        del self.httpHeaders
        self.httpHeaders = None

        oFile.flush()
        oFile.close()

        if string.find(headerData, '200 OK') > 0:
            self.httpLock.release()
            return 1

        print headerData

        os.remove(outputFile)
        self.httpLock.release()
        return 0
