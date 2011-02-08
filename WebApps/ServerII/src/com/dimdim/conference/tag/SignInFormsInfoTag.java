/*
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
 * Part of the DimDim V 1.0 Codebase (http://www.dimdim.com)	          *
 *                                                                        *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.                 *
 *                                                                        *
 *                                                                        *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license                 *
 *                                                                        *
 **************************************************************************
 */
 
package com.dimdim.conference.tag;

//import java.text.DateFormat;
//import java.util.Locale;
import javax.servlet.jsp.tagext.TagSupport;
//import	javax.servlet.http.HttpServletRequest;
//import	javax.servlet.ServletContext;

import com.dimdim.conference.ConferenceConsoleConstants;
//import com.dimdim.conference.application.UserSession;
//import com.dimdim.conference.application.UserManager;
//import com.dimdim.conference.model.IConference;
//import com.dimdim.conference.model.IConferenceParticipant;
//import com.dimdim.conference.model.ConferenceInfo;

/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 * 
 * This tag adds the information about the current user to the page, so
 * that the console code knows its own id. This tag adds following lines to
 * the page.
 * ----------------------
 * <script langauage='javascript'>
 * 		window.userid = '<user id>'
 * 		window.userroll = 'PRESENTER/ACTIVE_PRESENTER/ATTENDEE'
 * </script>
 * ----------------------
 */
public class SignInFormsInfoTag	extends	TagSupport
{
	public	int	doEndTag()
	{
		try
		{
			StringBuffer buf = new StringBuffer();
//			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
//			String browserType = (String)pageContext.getSession().
//					getAttribute(ConferenceConsoleConstants.BROWSER_TYPE);
			
			buf.append("<script language='javascript'>");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			
//			buf.append("	window.presenter_download_bandwidth_required='"+
//					ConferenceConsoleConstants.getPresenterDownloadBandwidthRequired()+"';");
//			buf.append(ConferenceConsoleConstants.lineSeparator);
//			buf.append("	window.attendee_download_bandwidth_required='"+
//					ConferenceConsoleConstants.getAttendeeDownloadBandwidthRequired()+"';");
//			buf.append(ConferenceConsoleConstants.lineSeparator);
			
//			buf.append("	window.presenter_upload_bandwidth_required='"+
//					ConferenceConsoleConstants.getPresenterUploadBandwidthRequired()+"';");
//			buf.append(ConferenceConsoleConstants.lineSeparator);
//			buf.append("	window.attendee_upload_bandwidth_required='"+
//					ConferenceConsoleConstants.getAttendeeUploadBandwidthRequired()+"';");
//			buf.append(ConferenceConsoleConstants.lineSeparator);
			
			buf.append("	window.server_max_attendee_audios='"+ConferenceConsoleConstants.getMaxAttendeeAudios()+"';");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			buf.append("	window.trackback_url='"+ConferenceConsoleConstants.getTrackbackURL()+"';");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			
			buf.append("</script>");
			buf.append(ConferenceConsoleConstants.lineSeparator);
			
			pageContext.getOut().println(buf.toString());
		}
		catch(Exception e)
		{
		}
		return	EVAL_PAGE;
	}
}
