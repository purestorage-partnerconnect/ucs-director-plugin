package com.cloupia.feature.purestorage.tasks;


import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;


public class RestoreVolumeTask extends GeneralTask
{

    public RestoreVolumeTask()
    {
        super(PureConstants.TASK_NAME_RESTORE_VOLUME_FROM_SNAPSHOT, "com.cloupia.feature.purestorage.tasks.RestoreVolumeTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        RestoreVolumeTaskConfig config = (RestoreVolumeTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking RestoreVolumeTask accountname");

        String volumeName = config.getVolumeName();
        String snapShotName = config.getSnapshotName();

        actionlogger.addInfo("Restoring " + volumeName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        CLIENT.volumes().create(volumeName, snapShotName, true);
        
        actionlogger.addInfo("Successfully restore volume " + volumeName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        String volIdentity =accountName+"@"+volumeName;
        String snapIdentity =accountName+"@"+snapShotName;
        
        
    	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY, volIdentity);
    	actionlogger.addInfo("Volume Identity as Output is saved");
    	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_SNAPSHOT_IDENTITY, snapIdentity);
    	actionlogger.addInfo("Snapshot Identity as Output is saved");
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
    	TaskOutputDefinition[] ops = new TaskOutputDefinition[2];
   		

   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Volume Identity");
        ops[1] = new TaskOutputDefinition(
                PureConstants.TASK_OUTPUT_NAME_SNAPSHOT_IDENTITY,
                WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
                "Snapshot Identity");
        return ops;
    }


   
}
