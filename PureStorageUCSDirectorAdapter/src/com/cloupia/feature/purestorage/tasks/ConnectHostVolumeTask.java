package com.cloupia.feature.purestorage.tasks;


import com.purestorage.rest.host.PureHostConnection;
import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

import java.util.ArrayList;
import java.util.List;


public class ConnectHostVolumeTask extends GeneralTask
{
    public ConnectHostVolumeTask(){
        super(PureConstants.TASK_NAME_CONNECT_HOST_VOLUME, "com.cloupia.feature.purestorage.tasks.ConnectHostVolumeTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        ConnectHostVolumeTaskConfig config = (ConnectHostVolumeTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking ConnectHostVolumeTask accountname");

        String allVolumeName = config.getVolumeName();
        String hostName = config.getHostName();
        boolean isLun = config.getIsStatusChange();
        String lunIds = config.getAllLunId();
        if(lunIds==null)
        {
        	lunIds="";
        }
        String[] lunIdList = lunIds.split(",");
        String[] volumeNameList = allVolumeName.split(",");
        actionlogger.addInfo("Starting connecting volume(s) to host " + hostName +
                " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");

        

        List<PureHostConnection> connectedVolume = CLIENT.hosts().getConnections(hostName);
        List<String> connectedVolName = new ArrayList<String>();


        for(PureHostConnection conn : connectedVolume)
        {
            connectedVolName.add(conn.getVolumeName());
        }
String connVol="";
        for(int i=0; i<volumeNameList.length; i++)
        {
            String volumeName = volumeNameList[i];
            if(connectedVolName.contains(volumeName))
            {
                actionlogger.addInfo(volumeName + " has already been connected to host " + hostName);
                continue;
            }
            if(isLun==true && lunIds!="" && lunIdList.length>i )
            {
                
            	int lunId=Integer.parseInt(lunIdList[i]);
            	CLIENT.hosts().connectVolume(hostName, volumeName,lunId);
               
            }
            else
            {
            CLIENT.hosts().connectVolume(hostName, volumeName);
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

        actionlogger.addInfo("Successfully Connected volumes to host " + hostName + " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "ConnectVolumesToHost",
                "Volumes has been connected to host " + config.getAccountName(),
                new DisconnectVolumeHostTask().getTaskName(), new DisconnectVolumeHostTaskConfig(config));
String hostIdentity =accountName+"@"+hostName;
        
        context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY, connVol);
    	actionlogger.addInfo("Volume Identities as Output is saved");
    	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_HOST_IDENTITY, hostIdentity);
    	actionlogger.addInfo("Host Identity as Output is saved");
    
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
    	TaskOutputDefinition[] ops = new TaskOutputDefinition[2];
   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Volume Identity(s)");

   		ops[1] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_HOST_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Host Identity");
   		return ops;
    }

}
