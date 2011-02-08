import os
import sys
import logging
import cherrypy
from toolkit import osconfig
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
from com.sun.star.beans import PropertyValue

cherrypy.log('Cleaning up any open office processes... ')
shellCmd = None
if sys.platform.startswith('win'):
    shellCmd = 'tskill soffice'
else:
    shellCmd = 'killall -9 soffice.bin'
try:
    os.system(shellCmd)
except:
    pass

app = 'soffice'
display = '-nodisplay'
logo = '-nologo'

headview = '-headless'
view = '-invisible'


if sys.platform.startswith('win'):
    systemString = 'soffice -headless -invisible "-accept=socket,host=localhost,port=8100;urp;"'
else:
    acceptString = '-accept=socket,host=localhost,port=8100;urp;StarOffice.ServiceManager'


try:
    if sys.platform.startswith('win'):
        hProcess, hThread, processID, threadID = win32process.CreateProcess(None, systemString, None, None, 0, win32process.CREATE_NEW_PROCESS_GROUP, None, None, win32process.STARTUPINFO())
    else:
        wizard = '-nofirststartwizard'
        args = ['soffice.bin', wizard, headview, acceptString]
        hProcess = popen2.Popen3(args)
        processID = hProcess.pid
#              cmd = 'netstat -anp | grep 8100'
#             os.system(cmd)
except:
    cherrypy.log('Error in starting Open Office process...')