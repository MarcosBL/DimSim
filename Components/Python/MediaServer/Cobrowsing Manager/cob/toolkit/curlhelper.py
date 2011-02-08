import os
import re
import pycurl
import string
import urllib
import urlparse
import socket
import StringIO
import threading

import logging
import toolkit.logManager as logManager

###################################################################
##  PYCURL RELATED CONSTANTS.                                    ##
##  DON'T MODIFY THESE VALUES UNLESS YOU KNOW WHAT YOU ARE DOING ##
###################################################################

def curlUserAgent():
    return 'Mozilla/4.0 (compatible; MSIE 6.0)'

def curlHeaders():
    return ['Cache-control: max-age=0', 'Pragma: no-cache', 'Connection: Keep-Alive']

def curlConnectTimeout():
    return 40

def curlTimeout():
    return 300

class CurlWrapper(object):
    httpHandle = None
    httpHeaders = None
    httpLock = None

    def __init__(self):
        self.httpHeaders = None
        self.httpLock = threading.BoundedSemaphore(1)
        #self.setupHandle()

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
        self.httpHandle.setopt(pycurl.HTTPHEADER, curlHeaders())
        self.httpHandle.setopt(pycurl.USERAGENT, curlUserAgent())
        self.httpHandle.setopt(pycurl.FOLLOWLOCATION, 1)
        self.httpHandle.setopt(pycurl.CONNECTTIMEOUT, curlConnectTimeout())
        self.httpHandle.setopt(pycurl.TIMEOUT, curlTimeout())
        #self.httpHandle.setopt(pycurl.COOKIEJAR, 'cookiejar.txt')
        #self.httpHandle.setopt(pycurl.COOKIEFILE, 'cookiejar.txt')
        #self.httpHandle.setopt(pycurl.COOKIELIST, 'ALL')
        self.httpHandle.setopt(pycurl.HEADERFUNCTION, self.headersCallback)


    def headersCallback(self, buf):
        # Callback function invoked when header data is ready
        if self.httpHeaders:
            self.httpHeaders.write(buf)

        return

    def downloadToFileFromHTTPFormSubmit(self, url, method, paramDict, outputFile):
        self.httpLock.acquire()
        self.setupHandle()
        url = urllib.unquote(url)
        self.httpHeaders = StringIO.StringIO()

        oFile = None
        try:
            oFile = open(outputFile, 'wb')
        except:
            #unable to create a file for storing content
            self.httpLock.release()
            return '-2'

        if method == 'get':
            self.httpHandle.setopt(pycurl.URL, url + '?' + urllib.urlencode(paramDict))
        else:
            self.httpHandle.setopt(pycurl.URL, url)
            self.httpHandle.setopt(pycurl.POST, 1)
            self.httpHandle.setopt(pycurl.POSTFIELDS, urllib.urlencode(paramDict))

        self.httpHandle.setopt(pycurl.WRITEDATA, oFile)

        try:
            self.httpHandle.perform()
        except pycurl.error, e:
            #exception in peforming HTTP action
            logManager.log('Exception in retrieving url ' + url, logging.WARN)
            logManager.log(str(e[0]) + ' :: ' + str(e[1]), logging.DEBUG)
            oFile.close()
            self.setupHandle()
            self.httpLock.release()
            return '-1'

        oFile.flush()
        oFile.close()

        self.httpHeaders.flush()
        headerData = self.httpHeaders.getvalue()

        self.httpHeaders.close()
        del self.httpHeaders
        self.httpHeaders = None

        if string.find(headerData, '200 OK') > 0 and string.find(headerData, 'text/html') > 0:
            matchObj = re.finditer(('Location\s{0,}:\s{0,}\S+'), headerData)
            flURL = url
            for item in matchObj:
                # simply iterate till the end and use the
                # final location and the original URL
                flURL = item.group()
                flURL = string.replace(flURL, ' ', '')
                flURL = string.lstrip(flURL, 'Location:')
            self.httpLock.release()
            if string.find(flURL, 'http://') != 0 and string.find(flURL, 'https://') != 0:
                # this is a relative link.
                # create absolute using the current url
                # and call the function again.
                baseURL = string.rstrip(urlparse.urljoin(url, 'dimdimsl'), 'dimdimsl')
                flURL = urlparse.urljoin(baseURL, flURL)
                flURL = self.downloadToFileFromHTTPURL(flURL, outputFile)
                
            return flURL

        logManager.log('unable to fetch url ' + url, logging.WARN)
        logManager.log(headerData, logging.DEBUG)

        try:
            os.remove(outputFile)
        except:
            pass
        self.setupHandle()
        self.httpLock.release()
        return '0'

    def downloadToFileFromHTTPURL(self, url, outputFile):

        self.httpLock.acquire()
        self.setupHandle()
        url = urllib.unquote(url)
        
        # first check if this is a valid URL
        socketDataList = None
        try:
            urlData = urlparse.urlparse(url)
        except:
            logManager.log('failed to lookup hostname of ' + url, logging.WARN)
            self.httpLock.release()
            return '-1'
        
        self.httpHeaders = StringIO.StringIO()

        oFile = None
        try:
            oFile = open(outputFile, 'wb')
        except:
            #unable to create a file for storing content
            self.httpLock.release()
            return '-2'

        self.httpHandle.setopt(pycurl.URL, url)
        self.httpHandle.setopt(pycurl.WRITEDATA, oFile)

        try:
            self.httpHandle.perform()
        except pycurl.error, e:
            #exception in peforming HTTP action
            logManager.log('Exception in retrieving url ' + url, logging.WARN)
            logManager.log(str(e[0]) + ' :: ' + str(e[1]), logging.DEBUG)
            oFile.close()
            self.setupHandle()
            self.httpLock.release()
            return '-1'

        oFile.flush()
        oFile.close()

        self.httpHeaders.flush()
        headerData =  self.httpHeaders.getvalue()

        self.httpHeaders.close()
        del self.httpHeaders
        self.httpHeaders = None

        if string.find(headerData, '200 OK') > 0 and string.find(headerData, 'text/html') > 0:
            matchObj = re.finditer(('Location\s{0,}:\s{0,}\S+'), headerData)
            flURL = url
            for item in matchObj:
                # simply iterate till the end and use the
                # final location and the original URL
                flURL = item.group()
                flURL = string.replace(flURL, ' ', '')
                flURL = string.lstrip(flURL, 'Location:')
            self.httpLock.release()
            if string.find(flURL, 'http://') != 0 and string.find(flURL, 'https://') != 0:
                # this is a relative link.
                # create absolute using the current url
                # and call the function again.
                baseURL = string.rstrip(urlparse.urljoin(url, 'dimdimsl'), 'dimdimsl')
                flURL = urlparse.urljoin(baseURL, flURL)
                oFile.close()
                os.remove(outputFile)
                flURL = self.downloadToFileFromHTTPURL(flURL, outputFile)
                
            return flURL

        logManager.log('unable to fetch url ' + url, logging.WARN)
        logManager.log(headerData, logging.DEBUG)

        try:
            os.remove(outputFile)
        except:
            pass

        self.httpLock.release()
        return '0'
