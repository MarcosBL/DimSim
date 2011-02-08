/**
**************************************************************************
*                                                                        *
*               DDDDD   iii             DDDDD   iii                      *
*               DD  DD      mm mm mmmm  DD  DD      mm mm mmmm           *
*               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
*               DD   DD iii mmm  mm  mm DD   DD iii mmm  mm  mm          *
*               DDDDDD  iii mmm  mm  mm DDDDDD  iii mmm  mm  mm          *
*                                                                        *
**************************************************************************
**************************************************************************
*                                                                        *
* Part of the DimDim V 1.0 Codebase (http://www.dimdim.com)	             *
*                                                                        *
* Copyright (c) 2006 Communiva Inc. All Rights Reserved.                 *
*                                                                        *
*                                                                        *
* This code is licensed under the DimDim License                         *
* For details please visit http://www.dimdim.com/license/Dimdim-MPL.txt  *
* 									 	                                 *	
* DimDim License is applicable to the following functions:		         *
*									                                     *
*   function scheduleConferenceI                                         *
*   function startScheduledConferenceI                                   *
*   function joinScheduledConferenceI                                    *
*   function startNewConferenceI                                         *
*   function joinConferenceI                                             *
*							                                             *  
**************************************************************************
*/

/**
 *	This file contains the sample of how the dimdim api can be used
 *	interactively. These functions are used by the interactive forms on
 *	the dimdim web conference server.
 */

/**
 *	Dimdim Interactive Success Handler - This handler checks the result.
 *	If the result is success, the handler puts the window on the url in
 *	the response object. If not the returned error message is presented
 *	to the user in a popup box.
 */

var interactiveSuccessHandler = function(o)
{
	if (o.responseText !== undefined)
	{
		var resp = eval('('+o.responseText+')');

		if (resp.result == "success")
		{
			if (resp.url != "")
			{
				window.location = resp.url;
			}
			else
			{
				alert(resp.result);
			}
		}
		else if (resp.message != "")
		{
			serverError(resp);
		}
	}
	else
	{
		alert("No response from server");
	}
}

/**
 *	Dimdim interactive Failure Handler: this function simply presents the
 *	returned error to the user in a popup box.
 */

var interactiveFailureHandler = function(o)
{
	alert("Failure: "+o.statusText);
}


  /**
    * Function to display the alert message
    * @public
    * @param string text: Message to be displayed
    * @return void
   */

function showMessage(text)
{
	alert(text);
}

/**
 *	The interactive counterparts of the api calls.
 */
 
function scheduleConferenceI(baseUrl,organizerEmail,organizerDisplayName,
	confName,confKey,presenters,attendees,timeStr,timeZone,sendEmail)
{
	schedulerConference(baseUrl,organizerEmail,organizerDisplayName,
		confName,confKey,presenters,attendees,timeStr,timeZone,sendEmail,
		interactiveSuccessHandler,interactiveFailureHandler);
}

function startScheduledConferenceI(baseUrl,email,displayName,confKey)
{
	startScheduledConference(baseUrl,email,displayName,confKey,
		interactiveSuccessHandler,interactiveFailureHandler);
}

function joinScheduledConferenceI(baseUrl,email,displayName,confKey)
{
	joinScheduledConference(baseUrl,email,displayName,confKey,
		interactiveSuccessHandler,interactiveFailureHandler);
}

function startNewConferenceI(baseUrl,email,displayName,confName,confKey)
{
	startNewConference(baseUrl,email,displayName,confName,confKey,
		interactiveSuccessHandler,interactiveFailureHandler);
}

function joinConferenceI(baseUrl,email,displayName,confKey,presenter)
{
	joinConference(baseUrl,email,displayName,confKey,
		interactiveSuccessHandler,interactiveFailureHandler,presenter);
}

