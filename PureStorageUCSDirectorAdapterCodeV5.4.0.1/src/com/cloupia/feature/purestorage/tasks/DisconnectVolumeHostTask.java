package com.cloupia.feature.purestorage.tasks;


import com.cloupia.feature.purestorage.constants.PureConstants;
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

        try
        {
            for(int i=0; i<volumeNameList.length; i++)
            {
                String volumeName = volumeNameList[i];
                CLIENT.hosts().disconnectVolume(hostName, volumeName);
            }
        }
        catch (PureException e)
        {
            actionlogger.addInfo("Error happened when disconnect volume with volumes" + "Exception: " + e.getMessage());
        }

        actionlogger.addInfo("Successfully Disconnected volumes with host " + hostName + " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
    }

}
