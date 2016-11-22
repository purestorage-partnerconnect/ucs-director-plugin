package com.cloupia.feature.purestorage.tasks;


import java.util.Arrays;

import com.cisco.cuic.api.client.WorkflowInputFieldTypeDeclaration;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;


public class ScheduleVolumeSnapshotTask extends GeneralTask
{

    public ScheduleVolumeSnapshotTask()
    {
        super(PureConstants.TASK_NAME_SCHEDULE_VOLUME_SNAPSHOT, "com.cloupia.feature.purestorage.tasks.ScheduleVolumeSnapshotTaskConfig");
    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionlogger) throws Exception
    {
        ScheduleVolumeSnapshotTaskConfig config = (ScheduleVolumeSnapshotTaskConfig) context.loadConfigObject();
        super.accountName = config.getAccountName();
        super.executeCustomAction(context, actionlogger);
        actionlogger.addInfo("finished checking ScheduleVolumeSnapshotTask accountname");

        final String volumeName = config.getVolumeName();
        String protectionGroupName = volumeName + "PGroup";
        Boolean deleteScheduleSnapshotFlag = config.getDeleteScheduleSnapshotFlag();
        config.setScheduleSnapshotFlag(true);

       
        if(deleteScheduleSnapshotFlag == null)
        {
        	String cF= config.getCreateFrequency();
        	if (cF.equals(""))cF= "0";            
            int  createFrequency = Integer.valueOf(cF);
            String fU = config.getFrequencyUnit();
            if(fU.equals("m")) fU= "60";
            else if(fU.equals("h")) fU ="3600";
            else if(fU.equals("d")) fU ="3600*24";
            else fU ="0";            	
            int frequencyUnit = Integer.valueOf(fU);
            String sT = config.getSetTime();
            if(sT.equals("")) sT="0";
            int setTime = Integer.valueOf(sT) * 3600;
            String rP = config.getRetainPeriod();
            if(rP.equals("")) rP="0";
            int retainPeriod = Integer.valueOf(rP);
            String rU = config.getRetainUnit();
            if(rU.equals("m")) rU= "60";
            else if(rU.equals("h")) rU ="3600";
            else if(rU.equals("d")) rU ="3600*24";
            else rU ="0";            	
            int retainUnit = Integer.valueOf(rU);
            String rN = config.getRetainNumber();
            if(rN.equals("")) rN="0";
            int retainSnapshot = Integer.valueOf(rN);
            String d = config.getMoreDuration();
            if(d.equals("")) d="0";
            int duration = Integer.valueOf(d);
            int frequency = createFrequency * frequencyUnit;
            int period = retainPeriod * retainUnit;
            

            try
            {
                CLIENT.protectionGroups().createOnVolumes(protectionGroupName,Arrays.asList(volumeName));
            }
            catch(Exception e) {
                actionlogger.addInfo("There is already an exsiting snap schedule for Volume " + volumeName + ". So just reschedule it.");
            }

            CLIENT.protectionGroups().enableSnapshot(protectionGroupName);

            if(frequencyUnit == 3600 *24 && createFrequency >0)
            {
            	
                CLIENT.protectionGroups().setSchedule(protectionGroupName, null, null, null, frequency, setTime);
            }
            else
            {
            	
            	
                CLIENT.protectionGroups().setSchedule(protectionGroupName, null, null, null, frequency, null);
            }
            
              CLIENT.protectionGroups().setRetention(protectionGroupName, period, retainSnapshot, duration, null, null, null);
            actionlogger.addInfo("Scheduled snapshot for volume " + volumeName + "on Pure FlashArray [" + flashArrayAccount.getManagementAddress() + "]");
        
            context.getChangeTracker().undoableResourceModified("AssetType", "idstring", "Scheduled Snapshot",
                    "Snapshots have been scheduled" + config.getAccountName(),
                    new DeleteScheduleSnapshotTask().getTaskName(), new DeleteScheduleSnapshotTaskConfig(config));
            String volIdentity =accountName+"@"+volumeName;
            //String snapIdentity =accountName+"@"+snapShotName;
            
            
        	context.saveOutputValue(PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY, volIdentity);
        	actionlogger.addInfo("Volume Identity as Output is saved");
        }

        else
        {
            actionlogger.addInfo("This is a rollback task for deleted scheduled snapshot for volume" + volumeName);
            CLIENT.protectionGroups().enableSnapshot(protectionGroupName);
        }
    }
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
    	TaskOutputDefinition[] ops = new TaskOutputDefinition[1];
   		

   		ops[0] = new TaskOutputDefinition(
   				PureConstants.TASK_OUTPUT_NAME_VOLUME_IDENTITY,
   				WorkflowInputFieldTypeDeclaration.GENERIC_TEXT,
   				"Volume Identity");
        
        return ops;
    }

}