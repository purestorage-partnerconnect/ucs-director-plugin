package com.cloupia.feature.purestorage.tasks;


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
    }

   
}
