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
 *								                                          *
 * Copyright (c) 2006 Communiva Inc. All Rights Reserved.	              *
 *								                                          *
 *								                                          *
 * This code is licensed under the DimDim License                         *
 * For details please visit http://www.dimdim.com/license		          *
 *									                                      *
 **************************************************************************
 */
/*
 **************************************************************************
 *	File Name  : ConferenceConfig.java
 *  Created On : Apr 11, 2006
 *  Created By : Saurav Mohapatra
 **************************************************************************
*/

package com.dimdim.conference.config;

import java.util.ArrayList;

/**
 * This the conference config that can be passed to a conference manager 
 * to create a conference
 * 
 * @author Saurav Mohapatra
 * @email  Saurav.Mohapatra@communiva.com
 */
public class ConferenceConfig
{
	private String    conferenceKey = null;
	private String    conferenceCode = null;
	private String    conferenceName = null;
	private	String	  conferenceOwnerId = null;
	private String    conferenceType = null;
	private String    conferenceDescription = null;
	private String    conferenceAttendancePolicy = null;
	
	private boolean   conferenceHasLobby = false;
	
	private ArrayList featureConfigs = null;
	private ArrayList participantConfigs = null;
	private ArrayList externalResourceConfigs = null;
	
	private	ScheduleConfig		scheduleConfig;
	private	PermissionsConfig	permissionsConfig;
	
	private	int		maxAudioBroadcasters	=	3;
	private	int		maxVideoBroadcasters	=	1;
	
	public ConferenceConfig()
	{
		super();
		
		this.permissionsConfig = new PermissionsConfig();
	}

	/**
	 * @return Returns the conferenceAttendancePolicy.
	 */
	public String getConferenceAttendancePolicy()
	{
		return conferenceAttendancePolicy;
	}

	/**
	 * @param conferenceAttendancePolicy The conferenceAttendancePolicy to set.
	 */
	public void setConferenceAttendancePolicy(String conferenceAttendancePolicy)
	{
		this.conferenceAttendancePolicy = conferenceAttendancePolicy;
	}

	/**
	 * @return Returns the conferenceDescription.
	 */
	public String getConferenceDescription()
	{
		return conferenceDescription;
	}

	/**
	 * @param conferenceDescription The conferenceDescription to set.
	 */
	public void setConferenceDescription(String conferenceDescription)
	{
		this.conferenceDescription = conferenceDescription;
	}

	/**
	 * @return Returns the conferenceHasLobby.
	 */
	public boolean getConferenceHasLobby()
	{
		return conferenceHasLobby;
	}

	/**
	 * @param conferenceHasLobby The conferenceHasLobby to set.
	 */
	public void setConferenceHasLobby(boolean conferenceHasLobby)
	{
		this.conferenceHasLobby = conferenceHasLobby;
	}

	/**
	 * @return Returns the conferenceKey.
	 */
	public String getConferenceKey()
	{
		return conferenceKey;
	}

	/**
	 * @param conferenceKey The conferenceKey to set.
	 */
	public void setConferenceKey(String conferenceKey)
	{
		this.conferenceKey = conferenceKey;
	}

	/**
	 * @return Returns the conferenceName.
	 */
	public String getConferenceName()
	{
		return conferenceName;
	}

	/**
	 * @param conferenceName The conferenceName to set.
	 */
	public void setConferenceName(String conferenceName)
	{
		this.conferenceName = conferenceName;
	}

	/**
	 * @return Returns the conferenceScheduleConfig.
	 */
	public ScheduleConfig getConferenceScheduleConfig()
	{
		return scheduleConfig;
	}

	/**
	 * @param conferenceScheduleConfig The conferenceScheduleConfig to set.
	 */
	public void setScheduleConfig(ScheduleConfig scheduleConfig)
	{
		this.scheduleConfig = scheduleConfig;
	}

	/**
	 * @return Returns the conferenceType.
	 */
	public String getConferenceType()
	{
		return conferenceType;
	}

	/**
	 * @param conferenceType The conferenceType to set.
	 */
	public void setConferenceType(String conferenceType)
	{
		this.conferenceType = conferenceType;
	}

	/**
	 * @return Returns the externalResourceConfigs.
	 */
	public ArrayList getExternalResourceConfigs()
	{
		return externalResourceConfigs;
	}

	/**
	 * @param externalResourceConfigs The externalResourceConfigs to set.
	 */
	public void setExternalResourceConfigs(ArrayList externalResourceConfigs)
	{
		this.externalResourceConfigs = externalResourceConfigs;
	}

	/**
	 * @return Returns the featureConfigs.
	 */
	public ArrayList getFeatureConfigs()
	{
		return featureConfigs;
	}

	/**
	 * @param featureConfigs The featureConfigs to set.
	 */
	public void setFeatureConfigs(ArrayList featureConfigs)
	{
		this.featureConfigs = featureConfigs;
	}

	/**
	 * @return Returns the participantConfigs.
	 */
	public ArrayList getParticipantConfigs()
	{
		return participantConfigs;
	}

	/**
	 * @param participantConfigs The participantConfigs to set.
	 */
	public void setParticipantConfigs(ArrayList participantConfigs)
	{
		this.participantConfigs = participantConfigs;
	}
	
	
	/**
	 * get a feature config by name
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param featureName
	 * @return
	 *
	 */
	public FeatureConfig getFeatureConfig(String featureName)
	{
		FeatureConfig fc = null;
		if(featureName != null && featureConfigs != null)
		{
			for(int i = 0; i < featureConfigs.size(); i++)
			{
				FeatureConfig featureConfig = (FeatureConfig)featureConfigs.get(i);
				if(featureConfig.getName().equals(featureName))
				{
					fc = featureConfig;
					break;
				}
			}
		}
		return fc;
	}
	/**
	 * add a feature config
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param featureConfig
	 *
	 */
	public void addFeatureConfig(FeatureConfig featureConfig)
	{
		if (featureConfig == null)
		{
			featureConfigs = new ArrayList();
		}
		if(!hasFeatureConfig(featureConfig.getName()))
		{
			featureConfigs.add(featureConfig);
		}
	}
	/**
	 * check if a feature config is present
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param featureName
	 * @return
	 *
	 */
	public boolean hasFeatureConfig(String featureName)
	{
		return getFeatureConfig(featureName) != null;
	}
	/**
	 * clear all the feature configs
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 *
	 */
	public void clearFeatureConfigs()
	{
		if (featureConfigs != null)
		{
			featureConfigs.clear();
		}
	}
	
	
	/**
	 * get a externalResource config by name
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param externalResourceName
	 * @return
	 *
	 */
	public ExternalResourceConfig getExternalResourceConfig(String externalResourceName)
	{
		ExternalResourceConfig erc = null;
		if(externalResourceName != null && externalResourceConfigs != null)
		{
			for(int i = 0; i < externalResourceConfigs.size(); i++)
			{
				ExternalResourceConfig externalResourceConfig = (ExternalResourceConfig)externalResourceConfigs.get(i);
				if(externalResourceConfig.getName().equals(externalResourceName))
				{
					erc = externalResourceConfig;
					break;
				}
			}
		}
		return erc;
	}
	/**
	 * add a externalResource config
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param externalResourceConfig
	 *
	 */
	public void addExternalResourceConfig(ExternalResourceConfig externalResourceConfig)
	{
		if (externalResourceConfigs == null)
		{
			externalResourceConfigs = new ArrayList();
		}
		if(!hasExternalResourceConfig(externalResourceConfig.getName()))
		{
			externalResourceConfigs.add(externalResourceConfig);
		}
	}
	/**
	 * check if a externalResource config is present
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 * @param externalResourceName
	 * @return
	 *
	 */
	public boolean hasExternalResourceConfig(String externalResourceName)
	{
		return getExternalResourceConfig(externalResourceName) != null;
	}
	/**
	 * clear all the externalResource configs
	 * 
	 * @author Saurav Mohapatra
	 * @email  Saurav.Mohapatra@communiva.com
	 *
	 */
	public void clearExternalResourceConfigs()
	{
		if (externalResourceConfigs != null)
		{
			externalResourceConfigs.clear();
		}
	}
	public void addParticipantConfig(ParticipantConfig pcfg)
	{
		if (participantConfigs == null)
		{
			participantConfigs = new ArrayList();
		}
		participantConfigs.add(pcfg);
	}
	public PermissionsConfig getPermissionsConfig()
	{
		return permissionsConfig;
	}
	public void setPermissionsConfig(PermissionsConfig permissionsConfig)
	{
		this.permissionsConfig = permissionsConfig;
	}
	public String getConferenceCode()
	{
		return conferenceCode;
	}
	public void setConferenceCode(String conferenceCode)
	{
		this.conferenceCode = conferenceCode;
	}
	public String getConferenceOwnerId()
	{
		return conferenceOwnerId;
	}
	public void setConferenceOwnerId(String conferenceOwnerId)
	{
		this.conferenceOwnerId = conferenceOwnerId;
	}
	public int getMaxAudioBroadcasters()
	{
		return maxAudioBroadcasters;
	}
	public void setMaxAudioBroadcasters(int maxAudioBroadcasters)
	{
		this.maxAudioBroadcasters = maxAudioBroadcasters;
	}
	public int getMaxVideoBroadcasters()
	{
		return maxVideoBroadcasters;
	}
	public void setMaxVideoBroadcasters(int maxVideoBroadcasters)
	{
		this.maxVideoBroadcasters = maxVideoBroadcasters;
	}
	
}
