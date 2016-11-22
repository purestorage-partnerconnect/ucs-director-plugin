package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.feature.purestorage.lovs.VolumeSizeUnitProvider;
import com.cloupia.feature.purestorage.lovs.VolumeTabularProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "psucs_rollback_resize_the_volume_task_config")
public class RollbackResizeVolumeTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account",  mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Volume Name", help = "Use ',' to seperate volumes name", mandatory = true,type=FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table= VolumeTabularProvider.TABULAR_PROVIDER)
    @UserInputField(type = PureConstants.PURE_VOLUME_LIST_TABLE_NAME)
    @Persistent
    private String volumeName;

    @FormField(label = "Truncate", help = "Check box to truncate size ", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.BOOLEAN)
    @Persistent
    private boolean truncate;
    
    @Persistent
    private long originSize =0;

    public RollbackResizeVolumeTaskConfig(){}

    public RollbackResizeVolumeTaskConfig(ResizeVolumeTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.volumeName = config.getVolumeName();
        this.truncate = config.getTruncate();
        this.originSize = config.getOriginSize();
    }

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_ROLLBACK_RESIZE_VOLUME_TASK;
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
    public Boolean getTruncate()
    {
        return truncate;
    }
    public void setTruncate(Boolean truncate)
    {
        this.truncate = truncate;
    }
    public long getOriginSize()
    {
        return originSize;
    }

}