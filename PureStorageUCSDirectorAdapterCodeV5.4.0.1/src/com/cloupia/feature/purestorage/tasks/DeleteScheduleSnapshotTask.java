package com.cloupia.feature.purestorage.tasks;


import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;


public class DeleteScheduleSnapshotTask extends GeneralTask
{

    public DeleteScheduleSnapshotTask()
    {
        super(PureConstants.TASK_NAME_DELETE_SCHEDULE_SNAPSHOT, "com.cloupia.feature.purestorage.tasks.DeleteScheduleSnapshotTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        DeleteScheduleSnapshotTaskConfig config = (DeleteScheduleSnapshotTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking DeleteScheduleSnapshotTask accountname");

        final String volumeName = config.getVolumeName();
        String protectionGroupName = volumeName + "PGroup";
        Boolean scheduleSnapshotTaskFlag = config.getScheduleSnapshotFlag();
        config.setDeleteScheduleSnapshotFlag(true);

        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "Destroy Scheduled Snapshot",
                "Scheduled Snapshots has been destoryed" + config.getAccountName(),
                new ScheduleVolumeSnapshotTask().getTaskName(), new ScheduleVolumeSnapshotTaskConfig(config));

        if(!(scheduleSnapshotTaskFlag == null))
        {
            actionlogger.addInfo("This is a rollback task for scheudling snapshot for volume: " + volumeName);
        }

        CLIENT.protectionGroups().disableSnapshot(protectionGroupName);
        actionlogger.addInfo("Disabled schedule snapshot for volume " + volumeName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");

    }

}