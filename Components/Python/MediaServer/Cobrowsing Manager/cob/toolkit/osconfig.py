import os
import sys
import string
import cherrypy
from cherrypy.process import plugins
# As of October 30, 2008 - CherryPy 3.1 stable has a bug where flup complains about fastcgi in Linux, if we use cherryd functionality.
# Using svn version of cherrypy/process/servers.py as a patch, till a stable release is available
import cp_servers as servers

import logManager

hostString='192.168.1.29:81'
separator='____'
def scriptServerURL():
    # This URL should be accessible from outside by all attendees and presenters
    # DO NOT USE INTERNAL ADDRESSES !!
    # Note: There should be a '/' at the end
    return 'http://'+hostString+'/content/cobjs/'

def serverURL():
    # This URL should be accessible from outside by all attendees and presenters
    # DO NOT USE INTERNAL ADDRESSES !!
    # Note: There should be a '/' at the end
    return 'http://'+hostString+'/'

def scriptServerDisk():
    # This location should allow read access to cobserver
    if sys.platform.startswith('win'):
        return 'C:\\www\\archive\\cob\\content\\cobjs'
    return '/usr/local/dimdim/CobrowsingManager/archive/cob/content/cobjs'

def cobArchive():
    if sys.platform.startswith('win'):
        return 'C:\\www\\archive\\cob\\content'
    return '/usr/local/dimdim/CobrowsingManager/archive/cob/content'

""" DO NOT MODIFY CODE BELOW """

def baseRestrictedSites():
    return ['youtube.com']

def getDimdimID(dimdimID):
    dimdimID = string.replace(dimdimID, ' ', '')
    installationID = '_default'
    if string.find(dimdimID, separator) >= 0:
        installationID = dimdimID[0:string.find(dimdimID, separator)]
        dimdimID = dimdimID[len(installationID) + len(separator) : len(dimdimID)]
        if len(installationID) == 0:
            installationID = '_default'
            
    return installationID + separator + dimdimID

def splitDimdimID(dimdimID):
    return string.split(dimdimID, separator)

# CherryPy Configuration

def error_page_404(status, message, traceback, version):
    errorPageFile = None
    if string.find(message, 'presenter') > 0:
        if sys.platform.startswith('win'):
            errorPageFile = open(os.path.join(os.path.join(os.path.join(os.getcwd(), 'toolkit'), 'pages'), 'p404.html'), 'r')
        else:
            errorPageFile = open('/usr/local/dimdim/CobrowsingManager/cob/toolkit/pages/p404.html', 'r')
    else:
        if sys.platform.startswith('win'):
            errorPageFile = open(os.path.join(os.path.join(os.path.join(os.getcwd(), 'toolkit'), 'pages'), 'a404.html'), 'r')
        else:
            errorPageFile = open('/usr/local/dimdim/CobrowsingManager/cob/toolkit/pages/a404.html', 'r')

    errorContent = errorPageFile.read()
    errorPageFile.close()
    return errorContent

def error_page_500(status, message, traceback, version):
    errorPageFile = None
    if string.find(message, 'pollForEvent') > 0:
        if sys.platform.startswith('win'):
            errorPageFile = open(os.path.join(os.path.join(os.path.join(os.getcwd(), 'toolkit'), 'pages'), 'p500.html'), 'r')
        else:
            errorPageFile = open('/usr/local/dimdim/CobrowsingManager/cob/toolkit/pages/p500.html', 'r')
    else:
        # leap of faith.. this can be attendee also
        if sys.platform.startswith('win'):
            errorPageFile = open(os.path.join(os.path.join(os.path.join(os.getcwd(), 'toolkit'), 'pages'), 'a500.html'), 'r')
        else:
            errorPageFile = open('/usr/local/dimdim/CobrowsingManager/cob/toolkit/pages/a500.html', 'r')

    errorContent = errorPageFile.read()
    errorPageFile.close()
    return errorContent

def setupCherrypyConfig(root):
    
    cherrypy.tree.mount(root)
    cherrypy.config.update({'global':{'server.socket_host':'0.0.0.0', 'server.socket_port' : 81, 'server.thread_pool' : 75}})
    
    cherrypy.config.update({'error_page.404': error_page_404})
    cherrypy.config.update({'error_page.500': error_page_500})
    
    cherrypy.config.update({'environment' : 'staging'}) # use 'production' for production and 'staging' for development / staging machines
    
    cherrypy.server.socket_timeout = 120 # default is 10.. probably too less in case of COB
    
    if sys.platform.startswith('win'):
        cherrypy.config.update({'global':{'log.access_file' : 'C:\\www\\logs\\cherrypy_access.log', 'log.error_file' : 'C:\\www\\logs\\cherrypy_error.log'}})
        cherrypy.config.update({'tools.staticdir.on' : True, 'tools.staticdir.root' : 'C:\\www\\archive', 'tools.staticdir.dir' : 'cob'})
        logManager.setupLogging('C:\\www\\logs\\synchrolive.log', 'debug')   # use 'info' for production and 'debug' for development / staging machines
    else:
        cherrypy.config.update({'global':{'log.access_file' : '/usr/local/dimdim/CobrowsingManager/logs/cherrypy_access.log', \
                                  'log.error_file' : '/usr/local/dimdim/CobrowsingManager/logs/cherrypy_error.log'}})
        cherrypy.config.update({'tools.staticdir.on' : True, 'tools.staticdir.root' : '/usr/local/dimdim/CobrowsingManager/archive', 'tools.staticdir.dir' : 'cob'})
        logManager.setupLogging('/usr/local/dimdim/CobrowsingManager/logs/synchrolive.log', 'info')   # use 'info' for production and 'debug' for development / staging machines
        
    # setup logging
    return

def run(bFastCGI = True):
    
    if not sys.platform.startswith('win'):
        cherrypy.config.update({'log.screen' : False})
        # daemonize, if the platform is not 'windows'. No support for windows - daemonizing requires fork
        plugins.Daemonizer(cherrypy.engine).subscribe()
        plugins.PIDFile(cherrypy.engine, '/var/run/cob.pid').subscribe()
        
    if hasattr(cherrypy.engine, "signal_handler"):
        cherrypy.engine.signal_handler.subscribe()
    if hasattr(cherrypy.engine, "console_control_handler"):
        cherrypy.engine.console_control_handler.subscribe()
    
    if bFastCGI:
        # run as a fast cgi process
        
        cherrypy.config.update({'autoreload.on': False})
        cherrypy.server.unsubscribe()
        
        cherrypy.config.update({'global':{'server.socket_host':'127.0.0.1', 'server.socket_port' : 40005}})
        bindPort        = cherrypy.config.get('server.socket_port')
        bindHost        = cherrypy.config.get('server.socket_host')
        bindAddress     = (bindHost, bindPort)
        flupServer      = servers.FlupFCGIServer(application=cherrypy.tree, bindAddress=bindAddress)
        serverAdapter   = servers.ServerAdapter(cherrypy.engine, httpserver=flupServer, bind_addr=bindAddress)
        serverAdapter.subscribe()
        
    # Always start the engine; this will start all other services
    try:
        cherrypy.engine.start()
    except:
        # Assume the error has been logged already via bus.log.
        sys.exit(1)
    else:
        cherrypy.engine.block()