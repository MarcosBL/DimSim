var dimdimsl_attributes =
{
    serverAddress   :   "",
    
    dimdimID       :   "",
    roomID          :   "",
    sessionID       :   "",
    
    resourceID      :   "",
    pageNum         :   "",
    
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
    
var dimdimsl_lockEvent =
{
    locked : false
};

var dimdimsl_events =
{
    scroll  : dimdimsl_scrollEvent,
    mouse   : dimdimsl_mouseEvent,
    lock    : dimdimsl_lockEvent
};

var dimdimsl_page =
{
    url     : "",
    events  : dimdimsl_events
};

var dimdimsl_contentAdvisory =
{
    // useful routines
    init:function()
    {
        var docLocation = document.location.href.toLowerCase();
        var documentAttributes = docLocation.split("/");
        
        // We are ignoring roomID and sessionID for now.
        dimdimsl_attributes.serverAddress = documentAttributes[0] + "//" + documentAttributes[2];
        dimdimsl_attributes.dimdimID = documentAttributes[4];
        dimdimsl_attributes.resourceType = documentAttributes[5];
        dimdimsl_attributes.resourceID = documentAttributes[6];
        dimdimsl_attributes.pageNum = documentAttributes[7];
        dimdimsl_attributes.role = documentAttributes[8];
    },
    moveCursor:function(horPos, verPos)
    {
        var verString = verPos + "px";
        var horString = horPos + "px";
        jQuery("#dimdimsl_cursorDiv").animate({top:verString, left:horPos}, 50);
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
    scroll:function(horPos, verPos)
    {
        try{jQuery('html,body').animate({scrollTop: verPos, scrollLeft: horPos}, 300);}catch(e){alert(e);}
        var scrollParams = dimdimsl_contentAdvisory.getScrollPos();
        curHorPos = scrollParams[0];
        curVerPos = scrollParams[1];
        if (verPos != curVerPos || horPos != curHorPos)
        {
            var scrollTimer = setTimeout(function(){scrollTo(horPos, verPos);}, 100);
            clearTimeout(scrollTimer);
        }
    },
    setupProxy:function(cmdType, additionalData)
    {
        var data =
        {
            type    : "",
            buffer  : ""
        };
        data.type = cmdType;
        if (cmdType != "ready")
        {
            data.buffer = additionalData;
        }
        else
        {
            data.buffer = dimdimsl_attributes;
        }
        
        var commandURL = dimdimsl_attributes.serverAddress + "/content/dimdimsl_proxy.html";
        commandURL += "#data=" + escape(JSON.stringify(data));
        window.open(commandURL, "dimdimsl_proxyIFrame");
    },
    handleSyncResponse:function(data)
    {
        // Scroll and Mouse are handled internally. Everything else is done externally.
        
        if (data && data.result)
        {
            if (data.pageNum == dimdimsl_attributes.pageNum)
            {
                var currentURL = dimdimsl_page.url;
                
                dimdimsl_page = data.page;
                dimdimsl_events = dimdimsl_page.events;
                
                if (dimdimsl_events.scroll)
                {
                    dimdimsl_contentAdvisory.scroll(dimdimsl_events.scroll.horPos, dimdimsl_events.scroll.verPos);
                }
                if (dimdimsl_events.mouse)
                {
                    dimdimsl_contentAdvisory.moveCursor(dimdimsl_events.mouse.horPos, dimdimsl_events.mouse.verPos);
                }
                if (dimdimsl_events.lock)
                {
                    dimdimsl_contentAdvisory.setupProxy("lock", JSON.stringify(dimdimsl_page.events.lock));
                }
                if (currentURL != dimdimsl_page.url)
                {
                    dimdimsl_contentAdvisory.setupProxy("url", dimdimsl_page.url);
                }
            }
            else
            {
                var resourceLocation = data.resourceType + '/' + data.resourceID;
                var contentLocation = resourceLocation + '/' + data.pageNum + '/attendee/content.html?cflag='+Math.random();
                dimdimsl_contentAdvisory.setupProxy("navigate", escape(contentLocation));
            }
        }
    },
    syncToAttendeeContext:function()
    {
        var queryURL = dimdimsl_attributes.serverAddress + '/cobrowsing/syncToAttendeeContext';
        var queryParams =
        {
            'dimdimID'  : dimdimsl_attributes.dimdimID,
            'roomID'    : dimdimsl_attributes.roomID,
            'sessionID' : dimdimsl_attributes.sessionID,
            'returnType': 'json',
            'cflag'     : Math.random()
        };
        
        jQuery.getJSON(queryURL, queryParams, function(data, textStatus)
        {
            dimdimsl_contentAdvisory.handleSyncResponse(data);
            setTimeout(dimdimsl_contentAdvisory.syncToAttendeeContext, 2000);
        });
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
    
    jQuery(document.body).append('<div id="dimdimsl_cursorDiv" class="dimdimsl_cursorDiv"></div>');
    jQuery("#dimdimsl_cursorDiv").css("position", "absolute");
    jQuery("#dimdimsl_cursorDiv").css("background", "url(" + dimdimsl_attributes.serverAddress + "/content/cobjs/cursor.gif) no-repeat");
    jQuery("#dimdimsl_cursorDiv").css("width", "17px");
    jQuery("#dimdimsl_cursorDiv").css("height", "22px");
    jQuery("#dimdimsl_cursorDiv").css("top", "400px");
    jQuery("#dimdimsl_cursorDiv").css("left", "500px");
    jQuery("#dimdimsl_cursorDiv").css("z-index", "2");
    
    dimdimsl_contentAdvisory.setupProxy("ready");
    dimdimsl_contentAdvisory.syncToAttendeeContext();
});