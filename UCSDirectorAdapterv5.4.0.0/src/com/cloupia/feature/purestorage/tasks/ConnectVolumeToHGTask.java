package com.cloupia.feature.purestorage.tasks;


import com.purestorage.rest.hostgroup.PureHostGroupConnection;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

import java.util.ArrayList;
import java.util.List;


public class ConnectVolumeToHGTask extends GeneralTask
{

    public ConnectVolumeToHGTask()
    {
        super(PureConstants.TASK_NAME_CONNECT_VOLUME_HOSTGROUP, "com.cloupia.feature.purestorage.tasks.ConnectVolumeToHGTaskConfig");
    }
    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        ConnectVolumeToHGTaskConfig config = (ConnectVolumeToHGTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking ConnectVolumeToHGTask accountname");


        String allVolumeName = config.getVolumeName();
        String hostGroupName = config.getHostGroupName();
        boolean isLun = config.getIsStatusChange();
        String lunIds = config.getAllLunId();
        if(lunIds==null)
        {
        	lunIds="";
        }
        String[] lunIdList = lunIds.split(",");
        String[] volumeNameList = allVolumeName.split(",");
        List<PureHostGroupConnection> connectedVolume = CLIENT.hostGroups().getConnections(hostGroupName);
        List<String> connectedVolName = new ArrayList<String>();
        String testFlag = "purestorage Lian Li testing input flag";
        config.setTestFlag(testFlag);

        for(PureHostGroupConnection connVolume : connectedVolume)
        {
            connectedVolName.add(connVolume.getVolumeName());
        }

        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "ConnectVolumesToHostGroup",
                "Volumes has been connected to hostGroup " + config.getAccountName(),
                new DisconnectVolumeHGTask().getTaskName(), new DisconnectVolumeHGTaskConfig(config));

        actionlogger.addInfo("Starting connecting volume(s) to hostgroup " + hostGroupName +
                " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        
      

        for(int i=0; i<volumeNameList.length; i++)
        {
            String volumeName = volumeNameList[i];
            if(connectedVolName.contains(volumeName))
            {
                actionlogger.addInfo(volumeName + " has already been connected to host group" + hostGroupName);
                continue;
            }
            if(isLun==true && lunIdList.length>i )
            {
                
            	int lunId=Integer.parseInt(lunIdList[i]);
            	CLIENT.hostGroups().connectVolume(hostGroupName, volumeName,lunId);
               
            }
            else
            {
            CLIENT.hostGroups().connectVolume(hostGroupName, volumeName);
        }
            
        }

        actionlogger.addInfo("Successfully Connected volume(s) to hostgroup " + hostGroupName + " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
       return null;
    }

}
