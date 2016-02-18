package com.cloupia.feature.purestorage.tasks;


import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.exceptions.PureException;
import com.purestorage.rest.host.PureHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewHostTask extends GeneralTask
{

    public NewHostTask(){
        super(PureConstants.TASK_NAME_NEW_HOST, "com.cloupia.feature.purestorage.tasks.NewHostTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        NewHostTaskConfig config = (NewHostTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking NewHostTask accoutname");

        String hostName = config.getHostName();
        String wwns = config.getWwns();
        String iqns = config.getIqns();
        Boolean deleteHostFlag = config.getDeleteHostFlag();
        String privateVolumes = config.getPrivateVolumes();
        String hostGroupName = config.getHostGroupName();
        List<PureHost> allHost = CLIENT.hosts().list();
        List<String> allHostName = new ArrayList<String>();
        Boolean existHost = false;

        if(wwns == null)
        {
        	wwns="";
        }
        if(iqns == null)
        {
        	iqns="";
        }
        
        
        for(PureHost oneHost : allHost)
        {
            if(oneHost.getName().equals(hostName))
            {
                existHost = true;
            }
        }
        config.setNewHostFlag(true);
        config.setExistHost(existHost);

        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "NewHost",
                "NewHost has been created on " + config.getAccountName(),
                new DeleteHostTask().getTaskName(), new DeleteHostTaskConfig(config));

        if(deleteHostFlag == null)
        {
            actionlogger.addInfo("Start creating host " + hostName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
            CLIENT.hosts().create(hostName);

            if(!wwns.equals(""))
            {
                String[] wwnArray = wwns.split(",");
                CLIENT.hosts().addWwnList(hostName, Arrays.asList(wwnArray));
            }
            
            if(!iqns.equals("")){
                String[] iqnArray = iqns.split(",");
                CLIENT.hosts().addIqnList(hostName, Arrays.asList(iqnArray));
            }
            actionlogger.addInfo("Successfully created host " + hostName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
            /*context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_HOST_NAME, hostName);
        	actionlogger.addInfo("Host Name as Output is saved");
            */
        }

        else
        {
            actionlogger.addInfo("This is a rollback task to create the deleted host " + hostName);

            try
            {
                CLIENT.hosts().create(hostName);
            }
            catch(PureException e)
            {
                actionlogger.addInfo("Error happens when rollback task trys to create the deleted host " + " Exception: " + e.getMessage());
            }

            if(!wwns.equals(""))
            {
                String[] wwnArray = wwns.split(",");
                try
                {
                    CLIENT.hosts().addWwnList(hostName, Arrays.asList(wwnArray));
                }catch (PureException e)
                {
                    actionlogger.addInfo("Error happens when rollback task trys to add wwns to the deleted host " + " Exception: " + e.getMessage());
                }
            }
            
            if(!iqns.equals(""))
            {
                String[] iqnArray = iqns.split(",");
                try
                {
                    CLIENT.hosts().addIqnList(hostName, Arrays.asList(iqnArray));
                }
                catch (PureException e)
                {
                    actionlogger.addInfo("Error happens when rollback task trys to add iqns to the deleted host " + " Exception: " + e.getMessage());
                }
            }
        }

        if(privateVolumes != null && !privateVolumes.equals(""))
        {
            String[] volumes = privateVolumes.split(",");
            for(String volume : volumes)
            {
                try
                {
                    CLIENT.hosts().connectVolume(hostName, volume);
                }
                catch (PureException e)
                {
                    actionlogger.addInfo("Error happens when connecting with volume " + volume + " Exception: " + e.getMessage());
                }
            }
        }

        //actionlogger.addInfo("hostGroup name is " + hostGroupName);

        if(hostGroupName != null && ! hostGroupName.equals(""))
        {
            List<String> hostList = new ArrayList<String>();
            hostList.add(hostName);
            try
            {
                CLIENT.hostGroups().addHosts(hostGroupName, hostList);
            }
            catch (PureException e)
            {
                actionlogger.addInfo("Error happens when connecting with host group " + hostGroupName + " Exception: " + e.getMessage());
            }
        }
    }

   /* @Override
   	public TaskOutputDefinition[] getTaskOutputDefinitions() {
   		TaskOutputDefinition[] ops = new TaskOutputDefinition[1];
   		//NOTE: If you want to use the output of this task as input to another task. Then the second argument 
   		//of the output definition MUST MATCH the type of UserInputField in the config of the task that will
   		//be receiving this output.  Take a look at HelloWorldConfig as an example.
   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_HOST_NAME,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Host Name");
   		return ops;
    }
*/}