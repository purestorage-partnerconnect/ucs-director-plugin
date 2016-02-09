package com.cloupia.feature.purestorage.tasks;


import com.purestorage.rest.host.PureHostConnection;
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

        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "ConnectVolumesToHost",
                "Volumes has been connected to host " + config.getAccountName(),
                new DisconnectVolumeHostTask().getTaskName(), new DisconnectVolumeHostTaskConfig(config));

        List<PureHostConnection> connectedVolume = CLIENT.hosts().getConnections(hostName);
        List<String> connectedVolName = new ArrayList<String>();


        for(PureHostConnection conn : connectedVolume)
        {
            connectedVolName.add(conn.getVolumeName());
        }

        for(int i=0; i<volumeNameList.length; i++)
        {
            String volumeName = volumeNameList[i];
            if(connectedVolName.contains(volumeName))
            {
                actionlogger.addInfo(volumeName + " has already been connected to host " + hostName);
                continue;
            }
            if(isLun==true && lunIdList.length>i )
            {
                
            	int lunId=Integer.parseInt(lunIdList[i]);
            	CLIENT.hosts().connectVolume(hostName, volumeName,lunId);
               
            }
            else
            {
            CLIENT.hosts().connectVolume(hostName, volumeName);
            }
        }

        actionlogger.addInfo("Successfully Connected volumes to host " + hostName + " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");

    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
        TaskOutputDefinition[] ops = new TaskOutputDefinition[2];
        ops[0] = new TaskOutputDefinition(
                "ConnectedVolume", 
                PureConstants.TASK_TYPE_VOLUME,
                "Volume Connected");
        ops[1] = new TaskOutputDefinition(
                "ConnectedHost",
                PureConstants.TASK_TYPE_HOST,
                "Host Connected");
        return ops;
    }

}
