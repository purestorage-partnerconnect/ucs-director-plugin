package com.cloupia.feature.purestorage.tasks;


import com.purestorage.rest.exceptions.PureException;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;


public class DisconnectVolumeHGTask extends GeneralTask
{

    public DisconnectVolumeHGTask()
    {
        super(PureConstants.TASK_NAME_DISCONNECT_VOLUMES_WITH_HOSTGROUP, "com.cloupia.feature.purestorage.tasks.DisconnectVolumeHGTaskConfig");
    }
    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        DisconnectVolumeHGTaskConfig config = (DisconnectVolumeHGTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking DisconnectVolumeToHGTask accountname");

        String allVolumeName = config.getVolumeName();
        String hostGroupName = config.getHostGroupName();
        String[] volumeNameList = allVolumeName.split(",");
        String testFlag = config.getTestFlag();

        actionlogger.addInfo("get test Flag: " + testFlag);


        for(int i=0; i<volumeNameList.length; i++)
        {
            String volumeName = volumeNameList[i];
            try
            {
                CLIENT.hostGroups().disconnectVolume(hostGroupName, volumeName);
            }
            catch (PureException e)
            {
                actionlogger.addInfo("Error happens when disconnecting " + volumeName + " with host group " + hostGroupName );
            }
        }


        actionlogger.addInfo("Successfully Connected volume(s) to hostgroup " + hostGroupName + " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
    }

}
