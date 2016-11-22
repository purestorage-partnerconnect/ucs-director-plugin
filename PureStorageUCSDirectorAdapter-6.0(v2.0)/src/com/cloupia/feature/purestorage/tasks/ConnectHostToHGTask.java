package com.cloupia.feature.purestorage.tasks;


import com.purestorage.rest.hostgroup.PureHostGroup;
import com.purestorage.rest.hostgroup.PureHostGroupConnection;
import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

import java.util.ArrayList;
import java.util.List;


public class ConnectHostToHGTask extends GeneralTask
{

    public ConnectHostToHGTask()
    {
        super(PureConstants.TASK_NAME_CONNECT_HOST_HOSTGROUP, "com.cloupia.feature.purestorage.tasks.ConnectHostToHGTaskConfig");
    }
    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        ConnectHostToHGTaskConfig config = (ConnectHostToHGTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking ConnectVolumeToHGTask accountname");


        String allHostName = config.getHostName();
        String hostGroupName = config.getHostGroupName();
        
        String[] hostNameList = allHostName.split(",");
        List<String> connectedHostName = null;
        List<PureHostGroup> hostGroups =  CLIENT.hostGroups().list();
        		 for (PureHostGroup hostgroup: hostGroups)
                 {
     	         if(hostgroup.getName().equals(hostGroupName))
     	         {
        			 connectedHostName = hostgroup.getHosts();
     	         }
                 }
        String testFlag = "purestorage Lian Li testing input flag";
        config.setTestFlag(testFlag);

        

       /* context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "ConnectHostsToHostGroup",
                "Hosts has been connected to hostGroup " + config.getAccountName(),
                new DisconnectHostHGTask().getTaskName(), new DisconnectHostHGTaskConfig(config));
*/
        actionlogger.addInfo("Starting connecting host(s) to hostgroup " + hostGroupName +
                " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        
      String connHost="";
        List<String> connectingHostName = new ArrayList<String>();
        for(int i=0; i<hostNameList.length; i++)
        {
            String hostName = hostNameList[i];
            if(connectedHostName.contains(hostName))
            {
                actionlogger.addInfo(hostName + " has already been connected to host group" + hostGroupName);
                continue;
            }
            connectingHostName.add(hostName);
            if(connHost=="")
            {
            	connHost=accountName+"@"+hostName;	
            }
            else
            {
            	connHost=connHost+","+accountName+"@"+hostName;
            }
                        
        }
        if(connectingHostName.isEmpty())
        {
        	actionlogger.addInfo(" All Host has already been connected to host group" + hostGroupName);
            
        }
        else
        {
        CLIENT.hostGroups().addHosts(hostGroupName, connectingHostName);
        }
        
        actionlogger.addInfo("Successfully Connected host(s) to hostgroup " + hostGroupName + " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        String hostGroupIdentity =accountName+"@"+hostGroupName;
        
        context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_HOST_IDENTITY, connHost);
    	actionlogger.addInfo("Host Identities as Output is saved");
    	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_HOSTGROUP_IDENTITY, hostGroupIdentity);
    	actionlogger.addInfo("Host Group Identity as Output is saved");
    
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
    	TaskOutputDefinition[] ops = new TaskOutputDefinition[2];
   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_HOST_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Host Identity(s)");

   		ops[1] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_HOSTGROUP_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Host Group Identity");
   		return ops;
    }

}
