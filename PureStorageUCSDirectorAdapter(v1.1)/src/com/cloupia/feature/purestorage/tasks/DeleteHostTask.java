package com.cloupia.feature.purestorage.tasks;


import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.host.PureHostConnection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class DeleteHostTask extends GeneralTask
{

    public DeleteHostTask()
    {
        super(PureConstants.TASK_NAME_DELETE_HOST, "com.cloupia.feature.purestorage.tasks.DeleteHostTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        DeleteHostTaskConfig config = (DeleteHostTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking DeleteHostTask accountname");

        String hostName = config.getHostName();
        Boolean newHostTaskFlag = config.getNewHostFlag();
        Boolean existHost = config.getExistHost();
        String hostGroupName = "";
        String wwns = "";
        String iqns = "";
        StringBuilder privateVolumes = new StringBuilder();
        List<String> wwnsList = CLIENT.hosts().get(hostName).getWwnList();
        List<String> iqnsList = CLIENT.hosts().get(hostName).getIqnList();
        List<PureHostConnection> privateConnectedVolumes = CLIENT.hosts().getPrivateConnections(hostName);
        List<PureHostConnection> sharedConnectedVolumes = CLIENT.hosts().getSharedConnections(hostName);
        config.setDeleteHostFlag(true);

        if(newHostTaskFlag != null)
        {
            actionlogger.addInfo("This is a rollback task to delete a new host " + hostName);
        }
        actionlogger.addInfo("Deleting host " + hostName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");

        wwns = StringUtils.join(wwnsList, ",");
        iqns = StringUtils.join(iqnsList, ",");

        for(PureHostConnection volumeConnection : privateConnectedVolumes)
        {
            String volumeName = volumeConnection.getVolumeName();
            privateVolumes.append(volumeName + ",");
        }
        actionlogger.addInfo("private volume is " + privateVolumes);

        if(sharedConnectedVolumes.size()>0)
        {
            hostGroupName = sharedConnectedVolumes.get(0).getHostGroupName();
            actionlogger.addInfo("hostgroup name is " + hostGroupName);
        }

        config.setWwns(wwns);
        config.setIqns(iqns);
        config.setPrivateVolumes(privateVolumes.toString());
        config.setHostGroupName(hostGroupName);

        
        if(existHost != null && !existHost)
        {
            for(PureHostConnection volumeConnection : privateConnectedVolumes)
            {
                String volumeName = volumeConnection.getVolumeName();
                CLIENT.hosts().disconnectVolume(hostName, volumeName);
            }
            if(!hostGroupName.equals(""))
            {
                List<String> tempHostList  = new ArrayList<String>();
                tempHostList.add(hostName);
                CLIENT.hostGroups().removeHosts(hostGroupName, tempHostList);
            }
            CLIENT.hosts().delete(hostName);
            actionlogger.addInfo("Successfully deleted host " + hostName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        
            context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "DeleteHost",
                    "Host has been deleted on " + config.getAccountName(),
                    new NewHostTask().getTaskName(), new NewHostTaskConfig(config));
            String hostIdentity =accountName+"@"+hostName;
            
            
        	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_HOST_IDENTITY, hostIdentity);
        	actionlogger.addInfo("Host Identity as Output is saved");
        }

        else
        {
            actionlogger.addInfo("This host cannot be deleted!");
        }

    
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
    	TaskOutputDefinition[] ops = new TaskOutputDefinition[1];
   		

   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_HOST_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Host Identity");
   		return ops;
    }


}
