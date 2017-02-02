package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.feature.purestorage.lovs.VolumeTabularProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "psucs_delete_schedule_snapshot_config")
public class DeleteScheduleSnapshotTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Volume Name", help = "Use ',' to seperate volumes name", mandatory = true,type=FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table= VolumeTabularProvider.TABULAR_PROVIDER)
    @UserInputField(type = PureConstants.PURE_VOLUME_LIST_TABLE_NAME)

    @Persistent
    private String volumeName;

    private Boolean scheduleSnapshotFlag;

    private Boolean deleteScheduleSnapshotFlag;

    public DeleteScheduleSnapshotTaskConfig(){}

    public DeleteScheduleSnapshotTaskConfig(ScheduleVolumeSnapshotTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.volumeName = config.getVolumeName();
        this.scheduleSnapshotFlag = config.getScheduleSnapshotFlag();
    }

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_DELETE_SCHEDULE_SNAPSHOT;
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
        this.volumeName = volumeName;
    }
    public Boolean getScheduleSnapshotFlag()
    {
        return scheduleSnapshotFlag;
    }

    public void setDeleteScheduleSnapshotFlag(Boolean deleteScheduleSnapshotFlag)
    {
        this.deleteScheduleSnapshotFlag = deleteScheduleSnapshotFlag;
    }

    public Boolean getDeleteScheduleSnapshotFlag()
    {
        return deleteScheduleSnapshotFlag;
    }

}