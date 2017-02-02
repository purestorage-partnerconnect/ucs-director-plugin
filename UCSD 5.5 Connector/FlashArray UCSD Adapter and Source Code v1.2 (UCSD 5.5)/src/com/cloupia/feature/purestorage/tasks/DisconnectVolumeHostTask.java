package com.cloupia.feature.purestorage.tasks;


import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.exceptions.PureException;


public class DisconnectVolumeHostTask extends GeneralTask
{

    public DisconnectVolumeHostTask(){
        super(PureConstants.TASK_NAME_DISCONNECT_VOLUMES_WITH_HOST, "com.cloupia.feature.purestorage.tasks.DisconnectVolumeHostTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        DisconnectVolumeHostTaskConfig config = (DisconnectVolumeHostTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking DisconnectVolumeHostTask accountname");

        String allVolumeName = config.getVolumeName();
        String hostName = config.getHostName();
        String[] volumeNameList = allVolumeName.split(",");
        actionlogger.addInfo("Starting disconnecting volume(s) from host " + hostName +
                " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        String connVol="";
        try
        {
            for(int i=0; i<volumeNameList.length; i++)
            {
                String volumeName = volumeNameList[i];
                CLIENT.hosts().disconnectVolume(hostName, volumeName);
                if(connVol=="")
                {
                	connVol=accountName+"@"+volumeName;	
                }
                else
                {
                	connVol=connVol+","+accountName+"@"+volumeName;
                }
            }
        }
        catch (PureException e)
        {
            actionlogger.addInfo("Error happened when disconnect volume with volumes" + "Exception: " + e.getMessage());
        }

        actionlogger.addInfo("Successfully Disconnected volumes with host " + hostName + " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
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
