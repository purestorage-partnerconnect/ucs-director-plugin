package com.cloupia.feature.purestorage.tasks;


import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.exceptions.PureException;
import com.purestorage.rest.hostgroup.PureHostGroup;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;


public class NewHostGroupTask extends GeneralTask
{

    public NewHostGroupTask(){
        super(PureConstants.TASK_NAME_NEW_HOSTGROUP, "com.cloupia.feature.purestorage.tasks.NewHostGroupTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        NewHostGroupTaskConfig config = (NewHostGroupTaskConfig) context.loadConfigObject();
        accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking NewHostGroupTask accountname");

        String hostGroupPreName = config.getHostGroupPreName();
        String startNumber = config.getStartNumber();
        String endNumber = config.getEndNumber();
        String hostConnection = config.getHostConnection();
        String volumeConnection = config.getVolumeConnection();
        String existHostGroup = config.getExistHostGroups();
        List<PureHostGroup> allHostGroup = CLIENT.hostGroups().list();
        List<String> allHostGroupName = new ArrayList<String>();
        List<String> noRollBackHostGroupName = new ArrayList<String>();

        for(PureHostGroup oneHostGroup : allHostGroup)
        {
            allHostGroupName.add(oneHostGroup.getName());
        }

        List<String> hostGroupNameList = new ArrayList<String>();

        if(startNumber.equals("") && endNumber.equals(""))
        {
            String hostGroupName = hostGroupPreName;
            hostGroupNameList.add(hostGroupName);
            if(allHostGroupName != null && allHostGroupName.contains(hostGroupName))
            {
                noRollBackHostGroupName.add(hostGroupName);
            }
        }

        else
        {
            if(startNumber.equals("")) startNumber = endNumber;
            if(endNumber.equals("")) endNumber = startNumber;

            for(int i = Integer.parseInt(startNumber);i <= Integer.parseInt(endNumber);i++)
            {
                String hostGroupName = hostGroupPreName + Integer.toString(i);
                hostGroupNameList.add(hostGroupName);
                if(allHostGroupName != null && allHostGroupName.contains(hostGroupName))
                {
                    noRollBackHostGroupName.add(hostGroupName);
                }
            }
        }

        config.setNewHostGroupTaskFlag(true);
        config.setNoRollBackHostGroupName(StringUtils.join(noRollBackHostGroupName, ","));

        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "Create host groups",
                "Host Groups have been created" + config.getAccountName(),
                new DeleteHGSuffixRangeTask().getTaskName(), new DeleteHGSuffixRangeTaskConfig(config));

        if(existHostGroup == null)
        {
            actionlogger.addInfo("it is not a rollback task");
            for(String hostGroupName : hostGroupNameList)
            {
                CLIENT.hostGroups().create(hostGroupName);
                actionlogger.addInfo("Creating Hostgroup : " + hostGroupName);
            }
            actionlogger.addInfo("Successfully created hostgroup(s) " + hostGroupPreName + " with range " + startNumber + "-" + endNumber +
                    " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        }

        else
        {
            actionlogger.addInfo("connectedHost is a rollback task and hostgroups are " + existHostGroup);

            HashMap<String, List<String>> hostMap = splitConnection(hostConnection);
            HashMap<String, List<String>> volumeMap = splitConnection(volumeConnection);

            if(!existHostGroup.equals(""))
            {
                String[] hostGroupArrays = existHostGroup.split(",");

                for(String oneHostGroup : hostGroupArrays)
                {
                    try
                    {
                        CLIENT.hostGroups().create(oneHostGroup);
                        actionlogger.addInfo("Creating Hostgroup : " + oneHostGroup);
                        if(hostMap != null && hostMap.containsKey(oneHostGroup))
                        {
                            CLIENT.hostGroups().addHosts(oneHostGroup, hostMap.get(oneHostGroup));
                        }
                        if(volumeMap != null && volumeMap.containsKey(oneHostGroup))
                        {
                            List<String> volumeList = volumeMap.get(oneHostGroup);
                            for(String oneVolume : volumeList)
                            {
                                CLIENT.hostGroups().connectVolume(oneHostGroup, oneVolume);
                            }
                        }
                    }
                    catch (PureException e)
                    {
                        actionlogger.addInfo("Error happens when creating host group for rollback deleting host group task" + oneHostGroup + " Exception: " + e.getMessage());
                    }
                }
                actionlogger.addInfo("Finished creating host groups for rollback deleting host group task");
            }
            else
            {
                actionlogger.addInfo("No host group need to be created for rollback deleting host group task");
            }
        }
    }

    public HashMap<String, List<String>> splitConnection(String connections)
    {
        HashMap<String, List<String>> result = new HashMap<String,List<String>>();

        if(connections.equals("")) return null;
        String[] connectionArrays = connections.split("!");
        for(String oneConnection : connectionArrays)
        {
            String[] oneConnectionArrays = oneConnection.split(":");
            result.put(oneConnectionArrays[0], Arrays.asList(oneConnectionArrays[1].split(",")));
        }

        return result;
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
        return null;
    }

}
