package com.cloupia.feature.purestorage.tasks;


import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.exceptions.PureException;


public class RollbackResizeVolumeTask extends GeneralTask
{

    public RollbackResizeVolumeTask()
    {
        super(PureConstants.TASK_NAME_ROLLBACK_RESIZE_VOLUME_TASK, "com.cloupia.feature.purestorage.tasks.RollbackResizeVolumeTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        RollbackResizeVolumeTaskConfig config = (RollbackResizeVolumeTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking RollbackResizeVolumeTask accountname");

        String volumeName = config.getVolumeName();
        Boolean truncate = config.getTruncate();
        long originSize = config.getOriginSize();

        actionlogger.addInfo("Rollback Resizing volume " + volumeName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        if(truncate == null)
        {
        	truncate = false;
        }
        if(truncate)
        {
            try{
                CLIENT.volumes().resize(volumeName, originSize, !truncate);
            }
            catch (PureException e)
            {
                actionlogger.addInfo("Error happens when rollback ResizeVolumeTask" + "Exception: " + e.getMessage());
            }
        }
        else
        {
            actionlogger.addInfo("This task cannot be rolled back. Because some of data may be irretrievably lost!");
        }
    }
   


}
