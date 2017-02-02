package com.cloupia.feature.purestorage.tasks;


import com.purestorage.rest.host.PureHost;
import com.purestorage.rest.host.PureHostConnection;
import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConnectWWWNToHostTask extends GeneralTask
{
    public ConnectWWWNToHostTask(){
        super(PureConstants.TASK_NAME_CONNECT_WWN_To_HOST, "com.cloupia.feature.purestorage.tasks.ConnectWWWNToHostTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
    	ConnectWWWNToHostTaskConfig config = (ConnectWWWNToHostTaskConfig) context.loadConfigObject();
    	super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking NewHostTask accoutname");

        String hostName = config.getHostName();
        String wwns = config.getWWNs();
        String iqns = config.getIQNs();
        
        List<PureHost> allHost = CLIENT.hosts().list();
        List<String> allHostName = new ArrayList<String>();
        Boolean existHost = false;
        
        if (wwns == null)
        {
        wwns = "";	
        }
        if (iqns == null)
        {
        iqns = "";	
        }
        for(PureHost oneHost : allHost)
        {
            if(oneHost.getName().equals(hostName))
            {
                existHost = true;
            }
        }
        
        if(existHost== true)
        {
        	 if(!wwns.equals(""))
             {
                 String[] wwnArray = wwns.split(",");
                 CLIENT.hosts().addWwnList(hostName, Arrays.asList(wwnArray));
             }
             
             if(!iqns.equals("")){
                 String[] iqnArray = iqns.split(",");
                 CLIENT.hosts().addIqnList(hostName, Arrays.asList(iqnArray));
             }
             actionlogger.addInfo("Successfully added wwns/iqns in host: " + hostName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
             
        }
        
String hostIdentity =accountName+"@"+hostName;
        
        
    	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_HOST_IDENTITY, hostIdentity);
    	actionlogger.addInfo("Host Identity as Output is saved");
    
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
