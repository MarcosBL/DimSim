/**
 *	New JSInterface based simpler call back and forth mechanism.
 *	This file is included by both the console and the popout
 *	window. The calls from the js methods thats are called from
 *	the other window simply pass on all the arguments to the
 *	JSInterface. The jsinterface in turn passes on the arguments
 *	the appropriate listener. In this case there are two - one
 *	the events and the initial data transfer from console to
 *	popout.
 *
 *	In the popout listener keys are 'EVENT' and 'DATA'
 */

var workspace_popout_window  = null;

function popoutWorkspace(url)
{
	if (workspace_popout_window == null)
	{
		workspace_popout_window = window.open(url,"_blank",
"directories=no,menubar=no,resizable=yes,scrollbars=auto,status=no,titlebar=no,top=0,location=no");
	}
}

popoutClosed = function()
{
	workspace_popout_window = null;
}

sendDataObjectToPopout = function(data)
{
	if (workspace_popout_window)
	{
		workspace_popout_window.receiveDataInPopoutFromConsole(data);
	}
}

function	receiveDataInPopoutFromConsole(data)
{
	JSInterface.callGWT('POPOUT_DATA',data);
}

sendEventToPopout = function(windowId,eventText)
{
	if (workspace_popout_window)
	{
		workspace_popout_window.receiveEventFromConsole(eventText);
	}
}

function	receiveEventFromConsole(eventText)
{
	JSInterface.callGWT('POPOUT_EVENT',eventText);
}

/**
 *	These two functions transfer all messages from popout to the
 *	console.
 */

sendMessageFromPopoutToConsole = function(msg)
{
	window.opener.receiveMessageInConsoleFromPopout(msg);
}

function  receiveMessageInConsoleFromPopout(msg)
{
	JSInterface.callGWT('POPOUT_PARENT',msg);
}



/***********************************************************
 *	Popin support.
 */

var close_callback_required = true;

handleConsoleClosedForPopouts = function()
{
	if (workspace_popout_window != null)
	{
		workspace_popout_window.reportCloseCallbackNotRequiredInPopout();
		closeWorkspacePopout();
	}
}

reportCloseCallbackNotRequiredToPopout = function()
{
	if (workspace_popout_window != null)
	{
		workspace_popout_window.reportCloseCallbackNotRequiredInPopout();
	}
}

function reportCloseCallbackNotRequiredInPopout()
{
	close_callback_required = false;
}

closeWorkspacePopout = function()
{
	if (workspace_popout_window != null)
	{
		workspace_popout_window.close();
		workspace_popout_window = null;
	}
}









/***************** OBSOLETE **************************/

var debug_popout_window = null;

function Queue()
{
  var queue=new Array();
  var queueSpace=0;
  this.enqueue=function(element)
  {
    queue.push(element);
  }
  this.dequeue=function()
  {
    if (queue.length)
    {
      var element=queue[queueSpace];
      if (++queueSpace*2 >= queue.length)
      {
        for (var i=queueSpace;i<queue.length;i++) queue[i-queueSpace]=queue[i];
        queue.length-=queueSpace;
        queueSpace=0;
      }
      return element;
    }
    else
    {
      return undefined;
    }
  }
}

/**
 *	This queue is used by the console to send new events to the popout
 *	window.
 */

var events_queue = new Queue();

/**
 *	This queue is used by the console to send initial data to the popout
 *	window. The initial data include current state of some of the feature
 *	models, panels and some history as applicable. e.g. the resource player
 *	needs to know only what the current movie that it should play. The
 *	user and resource rosters need to be made available to the popout
 *	window so that the relevant data is available to the panels as required
 *	for new events.
 *
 *	Each data transfer block employs two objects. First is a simple string
 *	code, that identifies the exact object or type and the second is the
 *	data string itself that can be effectively read only by the object
 *	identified by the first code.
 */

var data_queue = new Queue();


reportNumberOfDataObjects = function(num)
{
	workspace_popout_window.reportNumberOfDataObjectsInPopoutWindow(num);
}

sendDataObject = function(code,data)
{
	workspace_popout_window.sendDataObjectInPopoutWindow(code,data);
}

reportNumberOfDataObjectsInPopoutWindow = function(num)
{
	data_queue.enqueue(num);
}

function	sendDataObjectInPopoutWindow(code,data)
{
	data_queue.enqueue(code);
	data_queue.enqueue(data);
}

enqueueEvent = function(windowId,event)
{
//alert(event);
	workspace_popout_window.enqueueEventInPopoutWindow(event);
}

enqueueEventText = function(windowId,eventText)
{
//alert(event);
	workspace_popout_window.enqueueEventInPopoutWindow(eventText);
}

function enqueueEventInPopoutWindow(event)
{
//alert(event);
	events_queue.enqueue(event);
}

dequeueNextEvent = function()
{
	try
	{
		var event = events_queue.dequeue();
		if (event == undefined)
		{
			return	null;
		}
		return	event;
	}
	catch(e)
	{
		return	null;
	}
}

dequeueNextEventText = function()
{
	try
	{
		var event = events_queue.dequeue();
		if (event == undefined)
		{
			return	null;
		}
		return	event;
	}
	catch(e)
	{
		return	null;
	}
}

dequeueNextDataText = function()
{
	try
	{
		var data = data_queue.dequeue();
		if (data == undefined)
		{
			return	null;
		}
		return	data;
	}
	catch(e)
	{
		return	null;
	}
}

/**
 *	Objects and methods for callback support.
 */

var callback_queue = new Queue();

enqueueCallbackText = function(callbackDataText)
{
//alert(callbackDataText);
	window.opener.enqueueCallbackTextInConsole(callbackDataText);
}

function enqueueCallbackTextInConsole(event)
{
//alert(event);
	callback_queue.enqueue(event);
}

dequeueNextCallbackDataText = function()
{
	try
	{
		var data = callback_queue.dequeue();
		if (data == undefined)
		{
			return	null;
		}
		return	data;
	}
	catch(e)
	{
		return	null;
	}
}



