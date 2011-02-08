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

package com.dimdim.conference.ui.envcheck.client.command;
/*
import com.dimdim.conference.ui.envcheck.client.EnvGlobals;
import com.dimdim.conference.ui.envcheck.client.layout.CheckPanel;
import com.google.gwt.user.client.Window;
*/
/**
 * @author Jayant Pandit
 * @email Jayant.Pandit@communiva.com
 *
 */

public class BandwidthCheck	//extends	Check
{
	/*
	public	static	final	int	CheckIndex	=	4;

	protected	double	downloadBandwidth = 0;
	protected	double	uploadBandwidth = 0;

	protected	CheckPanel	panel;
	protected	UserCommand	command;

	protected	boolean	measurementInProgress = false;
	protected	int		dataSizeKB = 40;
	protected	int		returnCode;

	protected	DownloadBandwidthCalculator	downloadBandwidthCalculator;
	protected	UploadBandwidthCalculator	uploadBandwidthCalculator;

	public	BandwidthCheck(int dataSizeKB)
	{
		this.dataSizeKB = dataSizeKB;
	}
	public double getDownloadBandwidth()
	{
		return downloadBandwidth;
	}
	public void setDownloadBandwidth(double downloadBandwidth)
	{
		this.downloadBandwidth = downloadBandwidth;
		//Window.alert("Download bandwidth:"+this.downloadBandwidth);
		this.uploadBandwidthCalculator.calculateUploadBandwidth(dataSizeKB);
	}
	public double getUploadBandwidth()
	{
		return uploadBandwidth;
	}
	public void setUploadBandwidth(double uploadBandwidth)
	{
		this.uploadBandwidth = uploadBandwidth;
//		Window.alert("Upload bandwidth:"+this.uploadBandwidth);
		this.completeBandwidthCheck();
	}

	/**
	 * This method should be executed only if the check is applicable
	 * The check process itself considers the command and will return
	 * success
	 *--/
	public	int		runCheck(CheckPanel panel, UserCommand command)
	{
		//	Set the panel text to checking and helptext frame to neutral
		//	check explaination.


		return	Check.CHECK_SUCCESS;
		/*
		if (command.isCheckApplicable(BandwidthCheck.CheckIndex))
		{
			//	Kick off the download and upload measurements.
			if (!this.measurementInProgress)
			{
				this.panel = panel;
				this.command = command;

				this.downloadBandwidthCalculator = new DownloadBandwidthCalculator(this);
				this.uploadBandwidthCalculator = new UploadBandwidthCalculator(this);
				this.measurementInProgress = true;
//				this.panel.setCheckInProgress("");
				this.downloadBandwidthCalculator.calculateDownloadBandwidth(dataSizeKB);

				this.returnCode = Check.CHECK_REPEAT;
			}
			return	this.returnCode;
		}
		else
		{
			panel.setCheckSucceeded(getSuccessMessage(BandwidthCheck.CheckIndex,
					command.getCommandId(), (int)this.downloadBandwidth, (int)this.uploadBandwidth));
			return	Check.CHECK_SUCCESS;
		}
		*--/
	}
	private	void	completeBandwidthCheck()
	{
		String commandId = command.getCommandId();
		int	downloadBandwidthRequired = this.getAttendeeDownloadBandwidthRequired();
		int	uploadBandwidthRequired = this.getAttendeeUploadBandwidthRequired();
//		Window.alert("Download - required:"+downloadBandwidthRequired+" available:"+downloadBandwidth+
//				", Upload - required:"+uploadBandwidthRequired+" available:"+uploadBandwidth);
		if (commandId.equals(EnvGlobals.ACTION_HOST_MEETING))
		{
			downloadBandwidthRequired = this.getPresenterDownloadBandwidthRequired();
			uploadBandwidthRequired = this.getPresenterUploadBandwidthRequired();
		}
		String requiredBWs = "";//"("+downloadBandwidthRequired+"/"+uploadBandwidthRequired+")";
		if (downloadBandwidth < downloadBandwidthRequired ||
				uploadBandwidth < uploadBandwidthRequired)
		{
			String s = EnvGlobals.getCheckFailedText(BandwidthCheck.CheckIndex,
					command.getCommandId(),requiredBWs,null,null,(int)downloadBandwidth,(int)uploadBandwidth);
			panel.setCheckFailed(s);
			this.returnCode = Check.CHECK_FAILURE;
		}
		else
		{
			String s = EnvGlobals.getCheckSucceededText(BandwidthCheck.CheckIndex,
					command.getCommandId(),requiredBWs,null,(int)downloadBandwidth,(int)uploadBandwidth);
			panel.setCheckSucceeded(s);
//			Window.alert(s);
			this.returnCode = Check.CHECK_SUCCESS;
		}
	}
	private	int	getPresenterDownloadBandwidthRequired()
	{
		return	(new Integer(getPresenterDownloadBandwidthLimit())).intValue();
	}
	private	int	getAttendeeDownloadBandwidthRequired()
	{
		return	(new Integer(getAttendeeDownloadBandwidthLimit())).intValue();
	}
	private	int	getPresenterUploadBandwidthRequired()
	{
		return	(new Integer(getPresenterUploadBandwidthLimit())).intValue();
	}
	private	int	getAttendeeUploadBandwidthRequired()
	{
		return	(new Integer(getAttendeeUploadBandwidthLimit())).intValue();
	}

	private native	String	getPresenterDownloadBandwidthLimit() /*-{
		return	($wnd.presenter_download_bandwidth_required);
	}-*--/;
	private	native	String	getAttendeeDownloadBandwidthLimit() /*-{
		return	($wnd.attendee_download_bandwidth_required);
	}-*--/;
	private native	String	getPresenterUploadBandwidthLimit() /*-{
		return	($wnd.presenter_upload_bandwidth_required);
	}-*--/;
	private	native	String	getAttendeeUploadBandwidthLimit() /*-{
		return	($wnd.attendee_upload_bandwidth_required);
	}-*--/;
	public	native	String	getConfKey() /*-{
		return	($wnd.conf_key);
	}-*--/;
	*/
}
