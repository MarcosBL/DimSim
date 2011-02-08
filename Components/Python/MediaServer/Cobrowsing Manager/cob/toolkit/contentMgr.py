import os
import re

import string
from toolkit import osconfig
from toolkit.istr import iStr

def insertIntoBuffer(data, content, location):
    # To insert one buffer into another at a given location
    dataLen = len(data)
    return data[0:location] + content + data[location:dataLen]

def sanitizeHTML(htmlContent, url):

    # inserts the url as a <base> tag in htmlContent
    # as long as the url doesn't come under the restricted site list defined under osconfig.baseRestrictedSites()

    restricted = False
    try:
        for site in osconfig.baseRestrictedSites():
            site = string.strip(site, ' ')
            if len(site) > 0 and string.find(url, site) >= 0:
                restricted = True
                break
    except:
        pass
    
    # now in case of any JQuery conflicts -

    htmlString = iStr(htmlContent)
    endOfBodyTagLoc = string.rfind(htmlString, '</body>')
    del htmlString
    htmlString = None
    htmlContent = insertIntoBuffer(htmlContent, '<script language="javascript" type="text/javascript"> try{if (jQuery) { jQuery.noConflict(); }}catch(e){} </script>', endOfBodyTagLoc)

    if string.find(htmlContent, '<base') > 0:
        # base is already there.. let us not introduce a new one
        restricted = True

    if restricted:
        return htmlContent

    htmlString = iStr(htmlContent)
    headTagPattern = re.compile('<head.*?>', re.IGNORECASE)
    headTagMatchObj = re.search(headTagPattern, htmlString)
    headTagMatch = 0
    if headTagMatchObj:
        headTagMatch = headTagMatchObj.span()
        headTagLoc = headTagMatch[1]
        htmlContent =  insertIntoBuffer(htmlContent, '<base href="' + url + '"> </base>', headTagLoc)
        
    del htmlString
    
    return htmlContent

def insertPreamble(htmlContent, role):
    matchObj = re.search('<body.*?>', htmlContent, re.IGNORECASE)
    if not matchObj:
        return htmlContent
    immediatelyAfterBodyStartTag = matchObj.span()[1]
    preamble = osconfig.scriptServerURL()
    if role == 'presenter':
        preamble += 'dimdimsl_presenterPreamble.js'
    else:
        preamble += 'dimdimsl_attendeePreamble.js'
    return insertIntoBuffer(htmlContent, '<script type="text/javascript" src="'+preamble+'"></script>', immediatelyAfterBodyStartTag)