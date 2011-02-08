import os
import sys
import string
MailBoxIP='localhost'
def getIdentitySeparatorSequence():
    return "____"

def splitIdentity(identity):
    # identity is of the type installationID<separator>dimdimID
    # for now, taking ### as the separator.
    # this separator can be changed anytime
    # by just modifying the code below.
    # remove initial or trailing spaces, split and return [installationID, dimdimID]

    sep = getIdentitySeparatorSequence();
    if string.find(identity, sep) < 0:
        identity = sep + identity # in case there is no separator, make sure our logic doesn't fail

    [installationID, dimdimID] = string.split(string.strip(identity, ' '), sep)
    if len(installationID) == 0:
        installationID = '_default'
    if len(dimdimID) == 0:
        dimdimID = '_default'
    #meetingID = [installationID, dimdimID]
    return [installationID, dimdimID]

def preloadedDocumentRoot():
    # Examples : \\\\192.168.1.78\\Experiments\\ (Network Path)
    #           D:\\presentations\\             (Windows)
    #           /usr/local/presentations        (Linux)
    #           /media/sambamount/presentations (Linux, with a samba share mounted under media)
    # Each backslash needs to be escaped. So for every '\' you need to use in your preloadedDocumentRoot, make it '\\'
    # e.g. \\192.168.1.30\pdfs will not work. This needs to be used as - \\\\192.168.1.30\\pdfs
    #
    # Forward slash, i.e. '/', need not be escaped. Take a look at examples above for valid preloadedDocumentRoot values.

    return ''

def mailboxURL():
	return 'http://'+MailBoxIP+'/mbox/'# specify complete ipaddress neither localhost nor 127.0.0.1

def mediaDirectory():
    if sys.platform.startswith('win'):
        return 'C:\\www\\media'
    return os.path.dirname('/usr/local/dimdim/Mediaserver/www/media/')

def slidedeckArchive():
    if sys.platform.startswith('win'):
        return 'C:\\www\\archive\\slidedeck\\'
    return os.path.dirname('/usr/local/dimdim/Mediaserver/www/archive/slidedeck/')

def resourceArchive():
    if sys.platform.startswith('win'):
        return 'C:\\www\\archive\\resourcemanager\\'
    return os.path.dirname('/usr/local/dimdim/Mediaserver/www/archive/resourceManager/')

def filemgrArchive():
    if sys.platform.startswith('win'):
        return 'C:\\www\\archive\\static_files'
    return os.path.dirname('/usr/local/dimdim/Mediaserver/www/archive/static_files/')

def lighttpdPath():
    if sys.platform.startswith('win'):
        return 'C:\\lighttpd\\sbin\\lighttpd.exe -f C:\\lighttpd\\etc\\lighttpd.conf'
    else:
        return ''

def statisticsPath():
    if sys.platform.startswith('win'):
        return 'C:\\www\\archive\\statistics\\'
    return os.path.dirname('/usr/local/dimdim/Mediaserver/www/archive/statistics/')

def ooPath():
    if sys.platform.startswith('win'):
        return ''
    return os.path.dirname('/opt/openoffice.org2.4/program/')

def maxPDFAccess():
    return 3

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
