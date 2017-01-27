package com.cloupia.feature.purestorage.tasks;


import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.exceptions.PureException;
import com.purestorage.rest.volume.PureVolume;
import com.purestorage.rest.volume.PureVolumeConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DestroyVolumesTask extends GeneralTask
{

    public DestroyVolumesTask()
    {
        super(PureConstants.TASK_NAME_DESTROY_VOLUMES_SUFFIX_RANGE, "com.cloupia.feature.purestorage.tasks.DestroyVolumesTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        DestroyVolumesTaskConfig config = (DestroyVolumesTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking DestroyVolumesTask accountname");

        String volumePreName = config.getVolumePreName();
        String startNumber = config.getStartNumber();
        String endNumber = config.getEndNumber();
        Boolean eradicate = config.getEradicate();
        String noRollBackVolumeName = config.getNoRollBackVolumeName();
        List<String> noRollBackVolumeList = new ArrayList<String>();
        Boolean newVolumeTaskFlag = config.getNewVolumeTaskFlag();
        List<String> volumeNameList = new ArrayList<String>();
        StringBuilder hostConnection = new StringBuilder();
        StringBuilder hostGroupConnection = new StringBuilder();

        if(startNumber == null )
        {
        	startNumber="";
        	
        }
        if( endNumber == null)
        {
        	
        	endNumber="";
        }
        if (eradicate == null)
        {
        	eradicate = false;
        }
        
        
        
        if(startNumber.equals("") && endNumber.equals(""))
        {
            String volumeName = volumePreName;
            volumeNameList.add(volumeName);
        }
        else
        {
            if(startNumber.equals("")) startNumber = endNumber;
            if(endNumber.equals("") ) endNumber = startNumber;

            for(int i = Integer.parseInt(startNumber);i <= Integer.parseInt(endNumber);i++)
            {
                String volumeName = volumePreName + Integer.toString(i);
                volumeNameList.add(volumeName);
            }
        }

        for(String volumeName : volumeNameList)
        {
            try
            {
                List<PureVolumeConnection> connectedHost = CLIENT.volumes().getHostConnections(volumeName);
                List<PureVolumeConnection> connectedHostGroup = CLIENT.volumes().getHostGroupConnections(volumeName);
                if(connectedHost.size()>0)
                {
                    hostConnection.append(volumeName + ":");
                    for(PureVolumeConnection host : connectedHost)
                    {
                        hostConnection.append(host.getHost() + ",");
                    }
                    hostConnection.append("!");
                }
                if(connectedHostGroup.size()>0)
                {
                    hostGroupConnection.append(volumeName + ":");
                    for(PureVolumeConnection hostGroup : connectedHostGroup)
                    {
                        hostGroupConnection.append(hostGroup.getHostGroupName() + ",");
                    }
                    hostGroupConnection.append("!");
                }
            }
            catch (PureException e)
            {
                actionlogger.addInfo("Error happens when trying to get host connection and host group connection with volume "
                + volumeName + e.getMessage());
            }
        }

        config.setDestroyVolumeTaskFlag(true);
        config.setHostConnection(hostConnection.toString());
        config.setHostGroupConnection(hostGroupConnection.toString());
        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "Destroy volumes",
                "Volumes have been destroyed on " + config.getAccountName(),
                new NewVolumeTask().getTaskName(), new NewVolumeTaskConfig(config));

        actionlogger.addInfo("starting destroying volume(s)");

        if(newVolumeTaskFlag != null)
        {
            actionlogger.addInfo("This is a rollback task for the task of creating new volumes");
            noRollBackVolumeList = Arrays.asList(noRollBackVolumeName.split(","));
        }
        String connVol="";
        for(String volumeName : volumeNameList)
        {
            try
            {
                if(noRollBackVolumeList == null || !noRollBackVolumeList.contains(volumeName))
                {
                    destroyVolume(volumeName, CLIENT, eradicate);
                    actionlogger.addInfo("Destroying Volume : " + volumeName);
                }
            }
            catch(PureException e)
            {
                actionlogger.addInfo("Error happens while destroying volume: " + volumeName + " Exception: "+ e.getMessage());
            }
            if(connVol=="")
            {
            	connVol=accountName+"@"+volumeName;	
            }
            else
            {
            	connVol=connVol+","+accountName+"@"+volumeName;
            }
        }

        actionlogger.addInfo("successfully destroying volumes " + volumePreName + " with range " + startNumber + "-" + endNumber +
                " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY, connVol);
    	actionlogger.addInfo("Volume Identities as Output is saved");
    }

    public void destroyVolume(String volumeName, PureRestClient CLIENT, Boolean eradicate)
    {
        List<PureVolumeConnection> connectedHostGroup = CLIENT.volumes().getHostGroupConnections(volumeName);
        List<PureVolumeConnection> connectedHost = CLIENT.volumes().getHostConnections(volumeName);
        List<String> hostGroupList = new ArrayList<String>();

        for(PureVolumeConnection hostGroupVolumeConnection : connectedHostGroup)
        {
            String hostGroupName = hostGroupVolumeConnection.getHostGroupName();
            if(!hostGroupList.contains(hostGroupName))
            {
                hostGroupList.add(hostGroupName);
            }
        }

        for(String hgName : hostGroupList)
        {
            CLIENT.hostGroups().disconnectVolume(hgName,volumeName);
        }

        for(PureVolumeConnection hostVolumeConnection : connectedHost)
        {
            String hostName = hostVolumeConnection.getHost();
            CLIENT.hosts().disconnectVolume(hostName,volumeName);
        }

        CLIENT.volumes().destroy(volumeName);
        if(eradicate==true)
        {
            CLIENT.volumes().destroy(volumeName, eradicate);
        }
    }
    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
    	TaskOutputDefinition[] ops = new TaskOutputDefinition[1];
   		

   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Volume Identity(s)");
   		return ops;
    }
}
