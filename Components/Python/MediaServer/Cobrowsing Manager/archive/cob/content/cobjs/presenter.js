var dimdimsl_oldEventJSON   = "";
var dimdimsl_attributes     =
{
    serverAddress   :   "",
    
    dimdimID        :   "",
    roomID          :   "",
    sessionID       :   "",
    
    resourceID      :   "",
    pageNum         :   "",

    role            :   "",
    resourceType    :   ""
};

var dimdimsl_scrollEvent =
{
    horPos : 0,
    verPos : 0
};

var dimdimsl_mouseEvent =
{
    horPos : 0,
    verPos : 0
};

var dimdimsl_events =
{
    scroll  : dimdimsl_scrollEvent,
    mouse   : dimdimsl_mouseEvent,
    lock    : null
};
    
var dimdimsl_contentAdvisory =
{   
    // useful routines
    init:function()
    {
        var docLocation = document.location.href.toLowerCase();
        var documentAttributes = docLocation.split("/");
        
        // We are ignoring roomID and sessionID for now.

        dimdimsl_attributes.serverAddress    = documentAttributes[0] + "//" + documentAttributes[2];
        dimdimsl_attributes.dimdimID         = documentAttributes[4];
        dimdimsl_attributes.resourceType     = documentAttributes[5];
        dimdimsl_attributes.resourceID       = documentAttributes[6];
        dimdimsl_attributes.pageNum          = documentAttributes[7];
        dimdimsl_attributes.role             = documentAttributes[8];
    },
    getScrollPos:function()
    {
        var scrOfX = 0;
        var scrOfY = 0;
        if( typeof(window.pageYOffset) == 'number' ){scrOfX = window.pageXOffset;scrOfY = window.pageYOffset;}
        else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ){scrOfX = document.body.scrollLeft;scrOfY = document.body.scrollTop;}
        else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ){scrOfX = document.documentElement.scrollLeft;scrOfY = document.documentElement.scrollTop;}
        return [scrOfX, scrOfY];
    },
    getEventTarget:function(e)
    {
        return e.target;
    },
    setupProxy:function(cmdType, additionalData)
    {
        var data =
        {
            type    : "",
            buffer  : ""
        };
        data.type = cmdType;
        if (cmdType == "ready")
        {
            data.buffer = dimdimsl_attributes;
        }
        else if(cmdType == "anch_area" || cmdType == "form")
        {
            data.buffer = additionalData;
        }
        else{}
        var commandURL = dimdimsl_attributes.serverAddress + "/content/dimdimsl_proxy.html";
        commandURL += "#data=" + escape(JSON.stringify(data));
        window.open(commandURL, "dimdimsl_proxyIFrame");
    },
    sendEvents:function()
    {
        var currentEventJSON = JSON.stringify(dimdimsl_events);
        if (currentEventJSON != dimdimsl_oldEventJSON)
        {
            dimdimsl_oldEventJSON = currentEventJSON;
            var queryURL = dimdimsl_attributes.serverAddress + '/cobrowsing/sendEvents';
            var queryParams =
            {
                'dimdimID'  : dimdimsl_attributes.dimdimID,
                'roomID'    : dimdimsl_attributes.roomID,
                'sessionID' : dimdimsl_attributes.sessionID,
                'events'    : JSON.stringify(dimdimsl_events),
                'cflag'     : Math.random()
            };
            
            jQuery.post(queryURL, queryParams, function(data, textStatus){});
        }
        setTimeout(dimdimsl_contentAdvisory.sendEvents, 1000);
    },
    
    urlEvent:function(anchorElement, e, targetType)
    {
        var anchorHREF = anchorElement.toString();
        if (anchorElement.tagName.toLowerCase() != targetType)
        {
            while(anchorElement.parentNode != window && typeof(anchorElement.parentNode.tagName)!="undefined")
            {
                if(anchorElement.parentNode.tagName.toLowerCase()=="body")
                {
                    break;
                }
                if(anchorElement.parentNode.tagName.toLowerCase()==targetType)
                {
                    anchorElement = anchorElement.parentNode;
                    anchorHREF = anchorElement.toString();
                    break;
                }
                else
                {
                    anchorElement=anchorElement.parentNode;
                }
            }
        }
        
        // if the first character is '#',
        if (anchorHREF.charAt(0) == '#')
        {
            // we let it go unhindered.
            // scroll event listener will take care of this.
            return true;
        }
        // stop default action
        e.preventDefault();
        e.stopPropagation();
        
        if (anchorElement.tagName.toLowerCase() != targetType)
        {
            return false;
        }
        
        if(anchorHREF.toLowerCase().match("^javascript:"))
        {
            // there are just too many things that can happen here.
            // not supported yet.
            return false;
        }
        dimdimsl_contentAdvisory.setupProxy("anch_area", anchorHREF);
        return false;
    },
    formEvent:function(formElement, e)
    {
        e.preventDefault();
        e.stopPropagation();
        formAttributes = jQuery(formElement).serializeArray();
        var submitAttributes = {};
        for (var i = 0; i < formAttributes.length; i++)
        {
            submitAttributes[formAttributes[i].name] = formAttributes[i].value;
        }
        
        // inject dimdim's parameters to the form attribute array
        submitAttributes.dimdimsl_method = "get";
        if (formElement.method.toLowerCase() == "post")
        {
            submitAttributes.dimdimsl_method = "post";
        }
        submitAttributes.dimdimsl_action = formElement.action;
        dimdimsl_contentAdvisory.setupProxy("form", JSON.stringify(submitAttributes));
    }
};
jQuery(document).ready(function()
{
    dimdimsl_contentAdvisory.init();
    jQuery(document).bind("contextmenu",function(e)
    {
        return false;
    });
    
    var proxyURL = dimdimsl_attributes.serverAddress + "/content/dimdimsl_proxy.html";
    jQuery(document.body).append('<iframe id="dimdimsl_proxyIFrame" name="dimdimsl_proxyIFrame" style="visibility:hidden"></iframe>');
    window.open(proxyURL, "dimdimsl_proxyIFrame");
    
    jQuery(window).mousemove(function(e)
    {
        dimdimsl_mouseEvent.horPos = e.pageX;
        dimdimsl_mouseEvent.verPos = e.pageY;
    });
    
    jQuery(window).scroll(function(e)
    {
        var scrollParams = dimdimsl_contentAdvisory.getScrollPos();
        dimdimsl_scrollEvent.horPos = scrollParams[0];
        dimdimsl_scrollEvent.verPos = scrollParams[1];
    });
    
    jQuery.each(document.getElementsByTagName("a"), function()
    {
        jQuery(this).bind("click", function(e)
        {
            dimdimsl_contentAdvisory.urlEvent(dimdimsl_contentAdvisory.getEventTarget(e), e, "a");
        });
    });
    
    jQuery.each(document.getElementsByTagName("area"), function()
    {
        jQuery(this).bind("click", function(e)
        {
            dimdimsl_contentAdvisory.urlEvent(dimdimsl_contentAdvisory.getEventTarget(e), e, "a");
        });
    });
    
    jQuery.each(document.getElementsByTagName("form"), function()
    {
        jQuery(this).bind("submit", function(e)
        {
            dimdimsl_contentAdvisory.formEvent(dimdimsl_contentAdvisory.getEventTarget(e), e);
        });
    });
    
    dimdimsl_contentAdvisory.setupProxy("ready");
    dimdimsl_contentAdvisory.sendEvents();
});