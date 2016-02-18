package com.cloupia.feature.purestorage.tasks;


import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;


public class ResizeVolumeTask extends GeneralTask
{

    public ResizeVolumeTask()
    {
        super(PureConstants.TASK_NAME_RESIZE_VOLUME, "com.cloupia.feature.purestorage.tasks.ResizeVolumeTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        ResizeVolumeTaskConfig config = (ResizeVolumeTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking ResizeVolumeTask accountname");

        String volumeName = config.getVolumeName();
        String volumeSizeNumber = config.getVolumeSizeNumber();
        String vSU=config.getVolumeSizeUnit();
        int count = 0;
        if(vSU.equals("KB")) count = 1;
        if(vSU.equals("MB")) count = 2;
        if(vSU.equals("GB")) count = 3;
        if(vSU.equals("TB")) count = 4;
        if(vSU.equals("PB")) count = 5;
        
        double volumeSizeUnit = Math.pow(1024, count);
        long resetSize = (new Double(volumeSizeUnit)).longValue() * Long.parseLong(volumeSizeNumber);
        Boolean truncate = config.getTruncate();
        long originSize = CLIENT.volumes().get(volumeName).getSize();
        config.setOriginSize(originSize);
    
        if(truncate == null)
        {
        truncate = false;
        }
        
        if(truncate)
        {
            context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "Resize volume ",
                    "Volume size has been resized" + config.getAccountName(),
                    new RollbackResizeVolumeTask().getTaskName(), new RollbackResizeVolumeTaskConfig(config));
        }

        actionlogger.addInfo("Resizing volume " + volumeName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        CLIENT.volumes().resize(volumeName, resetSize, truncate);
        context.saveOutputValue(PureConstants.TASK_TYPE_VOLUME, volumeName);
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
        TaskOutputDefinition[] ops = new TaskOutputDefinition[1];
        ops[0] = new TaskOutputDefinition(
                PureConstants.TASK_OUTPUT_NAME_RESIZE_VOLUME,
                PureConstants.TASK_TYPE_VOLUME,
                "Volume Resized");
        return ops;
    }

}
