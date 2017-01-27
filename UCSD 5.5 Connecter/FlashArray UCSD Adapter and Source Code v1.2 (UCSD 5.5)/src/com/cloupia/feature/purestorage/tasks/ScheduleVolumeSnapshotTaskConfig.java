package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.TimeClockProvider;
import com.cloupia.feature.purestorage.lovs.TimeUnitProvider;
import com.cloupia.feature.purestorage.lovs.VolumeTabularProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "psucs_schedule_a_volume_snapshot_config")
public class ScheduleVolumeSnapshotTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Volume Name", help = "Use ',' to seperate volumes name", mandatory = true,type=FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table= VolumeTabularProvider.TABULAR_PROVIDER)
    @UserInputField(type = PureConstants.PURE_VOLUME_LIST_TABLE_NAME)
    @Persistent
    private String volumeName;

    @FormField(label="Frequency Unit",help="Choose a creating frequency unit",mandatory=true, type = FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = TimeUnitProvider.NAME)
    @UserInputField(type= PureConstants.PURE_TIME_UNIT_LOV_NAME)
    @Persistent
    private String frequencyUnit;

    @FormField(label="Creating Frequency",help="Frequency for creating a snapshot. Type number should be larger than or equal to 5 minute", mandatory=true)
    @UserInputField(type=WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String createFrequency;

    @FormField(label="Snap at ",help="If your creating frequency no less than 1 day, you can set preferred time of day to generate snapshot.",mandatory=false,
            type = FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV, lovProvider = TimeClockProvider.NAME)
    @UserInputField(type = PureConstants.PURE_TIME_CLOCK_LOV_NAME)
    @Persistent
    private String setTime;

    @FormField(label="Retain Unit",help="Choose a retaining unit",mandatory=true, type = FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = TimeUnitProvider.NAME)
    @UserInputField(type=PureConstants.PURE_TIME_UNIT_LOV_NAME)
    @Persistent
    private String retainUnit;

    @FormField(label="Retain Period",help="Time for each snapshot to retain on source. If type in 0, it will keep default value",mandatory=true)
    @UserInputField(type=WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String retainPeriod ;

    @FormField(label="Number of retaining snapshot per day",help="Number of retaining snapshot per day. If type in 0, it will keep default value",mandatory=true)
    @UserInputField(type=WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String retainNumber;

    @FormField(label="Retaining days",help="More days for retaining snapshot. If type in 0, it will keep default value",mandatory=true)
    @UserInputField(type=WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String moreDuration ;

    private Boolean scheduleSnapshotFlag;

    private Boolean deleteScheduleSnapshotFlag;

    public ScheduleVolumeSnapshotTaskConfig(){}

    public ScheduleVolumeSnapshotTaskConfig(DeleteScheduleSnapshotTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.volumeName = config.getVolumeName();
        this.deleteScheduleSnapshotFlag = config.getDeleteScheduleSnapshotFlag();
    }

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_SCHEDULE_VOLUME_SNAPSHOT;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    public String getVolumeName()
    {
        return volumeName;
    }
    public void setVolumeName(String volumeName)
    {
        this.volumeName= volumeName;
    }
    public String getCreateFrequency()
    {
               return createFrequency;
    }
    public void setCreateFrequency( String createFrequency)
    {
    	/*if (this.createFrequency.equals("")) ;
         Integer.valueOf(this.createFrequency);*/
    	this.createFrequency = createFrequency;
    }
    public String getFrequencyUnit()
    {
              return frequencyUnit;
    }
    public void setFrequencyUnit(String frequencyUnit)
    {
        this.frequencyUnit= frequencyUnit;
    }
    public String getSetTime()
    {
        
        return setTime;
    }
    public void setSetTime(String setTime)
    {
        this.setTime= setTime;
    }
    public String getRetainPeriod()
    {
        return retainPeriod;
    }
    public void setRetainPeriod(String retainPeriod)
    {
        this.retainPeriod= retainPeriod;
    }
    public String getRetainUnit()
    {
               return retainUnit;
    }
    public void setRetainUnit(String retainUnit)
    {
        this.retainUnit= retainUnit;
    }
    public String getRetainNumber()
    {
        
        return retainNumber;
    }
    public void setRetainNumber(String retainNumber)
    {
        this.retainNumber= retainNumber;
    }
    public String getMoreDuration()
    {
        
        return moreDuration;
    }
    public void setMoreDuration(String moreDuration)
    {
        this.moreDuration= moreDuration;
    }
    public void setScheduleSnapshotFlag(Boolean scheduleSnapshotFlag)
    {
     this.scheduleSnapshotFlag = scheduleSnapshotFlag;
    }

    public Boolean getScheduleSnapshotFlag()
    {
        return scheduleSnapshotFlag;
    }

    public Boolean getDeleteScheduleSnapshotFlag()
    {
        return deleteScheduleSnapshotFlag;
    }

}