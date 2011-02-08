package com.dimdim.email.application;

import java.io.File;
import java.util.Locale;

import com.dimdim.email.model.ConferenceInfo;
import com.dimdim.email.model.PasswordRecovery;
import com.dimdim.locale.ILocaleManager;
import com.dimdim.locale.LocaleManager;
import com.dimdim.locale.LocaleResourceFile;

public class TestClass {

	public static void main(String[] args){
		ConferenceInfo confInfo = new ConferenceInfo();
		confInfo.setKey("testKey");
		
		String presenters = "dilip@dimdim.com";
		String attendes = "vedick@gmail.com;dilip@dimdim.com";
			
		EmailConstants.initConstants("E:/DilipWorkArea/DimDimCodeBase/v2.0/Experimental Code/PortalServer/tmp");
		//InvitationEmailsHelper mailHelper = new InvitationEmailsHelper(confInfo, presenters, attendes, "joint this meeting");
		//mailHelper.sendInvitationEmails(null, null, Locale.ENGLISH);
		
			try
			{
				LocaleManager.initManager("E:/DilipWorkArea/DimDimCodeBase/v2.0/Experimental Code/PortalServer/tmp/data");
				ILocaleManager localMngr = LocaleManager.getManager();
				PasswordRecovery  pwdEmail = new PasswordRecovery("dilip@dimdim.com", "testNewPwd", "dilip", Locale.US, localMngr, null);
				//jie.setEmailText("test Message");
				EmailDispatchManager2 emailDispatcher = EmailDispatchManager2.getManager();
				emailDispatcher.setLocaleManager(localMngr);
				emailDispatcher.dispatch(pwdEmail, Locale.US, LocaleResourceFile.FREE);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}
}
