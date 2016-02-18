package com.cloupia.feature.purestorage.tasks;


import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.exceptions.PureException;
import com.purestorage.rest.volume.PureVolume;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class NewVolumeTask extends GeneralTask
{
    public NewVolumeTask(){
        super(PureConstants.TASK_NAME_NEW_VOLUME, "com.cloupia.feature.purestorage.tasks.NewVolumeTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        NewVolumeTaskConfig config = (NewVolumeTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        actionlogger.addInfo("Accountname   : "+accountName);

        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking NewVolumeTask accountname");

        String volumePreName = config.getVolumePreName();
        String startNumber = config.getStartNumber();
        String endNumber = config.getEndNumber();
        String volumeSizeUnit = config.getVolumeSizeUnit();
        String volumeSizeNumber = config.getVolumeSizeNumber();
        Boolean destroyVolumeTaskFlag = config.getDestroyVolumeTaskFlag();
        String hostConnection = config.getHostConnection();
        String hostGroupConnection = config.getHostGroupConnection();
        List<String> volumeNameList = new ArrayList<String>();
        List<PureVolume> allVolume = CLIENT.volumes().list();
        List<String> allVolumeName = new ArrayList<String>();
        List<String> noRollBackVolumeName = new ArrayList<String>();

        actionlogger.addInfo("Parameters are initialized");
        if(startNumber == null )
        {
        	startNumber="";
        	
        }
        if( endNumber == null)
        {
        	
        	endNumber="";
        }
        for(PureVolume oneVolume : allVolume)
        {
            allVolumeName.add(oneVolume.getName());
        }
        //actionlogger.addInfo("Parameters are initialized1 "+startNumber +" e "+endNumber);
        if((startNumber.equals("") && endNumber.equals("")) || (startNumber == null && endNumber == null))
        {
            String volumeName = volumePreName;
            volumeNameList.add(volumeName);
            if(allVolumeName != null && allVolumeName.contains(volumeName))
            {
            	                noRollBackVolumeName.add(volumeName);
            }
        }

        else
        {
        	actionlogger.addInfo("Parameters are initialized5");
            if(startNumber.equals("")||startNumber == null) startNumber = endNumber;
            if(endNumber.equals("")||endNumber == null) endNumber = startNumber;
            actionlogger.addInfo("Parameters are initialized6");
            for(int i = Integer.parseInt(startNumber);i <= Integer.parseInt(endNumber);i++)
            {
                String volumeName = volumePreName + Integer.toString(i);
                volumeNameList.add(volumeName);
                if(allVolumeName != null && allVolumeName.contains(volumeName))
                {
                	actionlogger.addInfo("Parameters are initialized7");
                    noRollBackVolumeName.add(volumeName);
                }
            }
        }
        actionlogger.addInfo("Checked volume name in the volume list ");
        config.setNewVolumeTaskFlag(true);
        config.setNoRollBackVolumeName(StringUtils.join(noRollBackVolumeName, ","));
        actionlogger.addInfo("Set volume task flag ");
        
        context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "Create new volumes",
                "Volumes have been created on " + accountName,
                new DestroyVolumesTask().getTaskName(), new DestroyVolumesTaskConfig(config));

        actionlogger.addInfo("Starting creating volume(s)");

        if(destroyVolumeTaskFlag == null)
        {
            for(String volumeName : volumeNameList)
            {
                CLIENT.volumes().create(volumeName, Long.parseLong(volumeSizeNumber), volumeSizeUnit);
            }
            actionlogger.addInfo("Successfully created volume(s) " + volumePreName + " with range " + startNumber + "-" + endNumber +
                    " on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
            if((startNumber.equals("") && endNumber.equals("")) || (startNumber == null && endNumber == null))
               {
            	PureVolume volume =  CLIENT.volumes().get(volumePreName);
            	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_VOLUME_NAME, volume.getName());
            	actionlogger.addInfo("Volume Name as Output is saved");
                
            	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_VOLUME_SERIAL, volume.getSerial());
            	actionlogger.addInfo("The Serial Number of this volume is "+volume.getSerial());
            	actionlogger.addInfo("Serial number as Output is saved");
            }
        }

        else
        {
            actionlogger.addInfo("This is a rollback task for the task of destroying volumes");
            HashMap<String, List<String>> hostMap = splitConnection(hostConnection);
            HashMap<String, List<String>> hostGroupMap = splitConnection(hostGroupConnection);
            for(String volumeName : volumeNameList)
            {
                try
                {
                    CLIENT.volumes().recover(volumeName);
                    if(hostMap != null && hostMap.containsKey(volumeName))
                    {
                        List<String> hostList = hostMap.get(volumeName);
                        for(String host : hostList)
                        {
                            CLIENT.hosts().connectVolume(host, volumeName);
                        }
                    }
                    if(hostGroupMap != null && hostGroupMap.containsKey(volumeName))
                    {
                        List<String> hostGroupList = hostGroupMap.get(volumeName);
                        for(String hostGroup : hostGroupList)
                        {
                            CLIENT.hostGroups().connectVolume(hostGroup, volumeName);
                        }
                    }
                }
                catch (PureException e)
                {
                    actionlogger.addInfo("Error happens when recovering volume " + volumeName + "Exception: " + e.getMessage());
                }
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
   	public TaskOutputDefinition[] getTaskOutputDefinitions() {
   		TaskOutputDefinition[] ops = new TaskOutputDefinition[2];
   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_VOLUME_NAME,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Volume Name");

   		ops[1] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_VOLUME_SERIAL,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Serial number of volume");
   		return ops;
   	}

}
