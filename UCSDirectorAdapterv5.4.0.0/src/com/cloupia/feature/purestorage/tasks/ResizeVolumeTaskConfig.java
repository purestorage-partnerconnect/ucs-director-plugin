package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.feature.purestorage.lovs.VolumeSizeUnitProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "psucs_resize_a_volume_task_config")
public class ResizeVolumeTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account",  mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
        lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    private String accountName;
    
    @FormField(label = "Volume Name", help = "FlashArray Volume Name", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String volumeName;

    @FormField(label = "Volume Size Unit", help = "FlashArray Volume Size Unit", mandatory = true ,type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
        lovProvider = VolumeSizeUnitProvider.NAME)
    @UserInputField(type = PureConstants.PURE_VOLUME_SIZE_UNIT_LOV_NAME)
    @Persistent
    private String volumeSizeUnit;

    @FormField(label = "Volume Size Number", help = "FlashArray Volume Size Number", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String volumeSizeNumber;

    @FormField(label = "Truncate", help = "Check box to truncate size ", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.BOOLEAN)
    @Persistent
    private boolean truncate;

    private long originSize;

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_RESIZE_VOLUME;
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
    public String getVolumeSizeUnit()
    {
        
        return volumeSizeUnit;
    }
    public void setVolumeSizeUnit(String volumeSizeUnit )
    {
        
    	this.volumeSizeUnit = volumeSizeUnit;
        
    }
    public String getVolumeSizeNumber()
    {
        return volumeSizeNumber;
    }
    public void setVolumeSizeNumber(String volumeSizeNumber)
    {
        this.volumeSizeNumber = volumeSizeNumber;
    }
    public Boolean getTruncate()
    {
        return truncate;
    }
    public void setTruncate(Boolean truncate)
    {
        this.truncate = truncate;
    }
    public void setOriginSize(long originSize)
    {
        this.originSize = originSize;
    }

    public long getOriginSize()
    {
        return originSize;
    }

}