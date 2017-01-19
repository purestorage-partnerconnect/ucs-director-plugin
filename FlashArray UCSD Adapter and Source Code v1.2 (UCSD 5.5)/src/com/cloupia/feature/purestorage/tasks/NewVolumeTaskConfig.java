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


@PersistenceCapable(detachable = "true", table = "psucs_new_volume_task_config")
public class NewVolumeTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Volume Prefix Name or Whole Name", help = "Letters, numbers, -, and _", mandatory = true )
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String volumePreName;

    @FormField(label = "Volume suffix Start Number", help = "FlashArray Volume suffix Start Number", validate = true, mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String startNumber="";

    @FormField(label = "Volume suffix End Number", help = "FlashArray Volume suffix End Number", validate = true, mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String endNumber ="";

    @FormField(label = "Volume Size Unit", help = "FlashArray Volume Size Unit", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = VolumeSizeUnitProvider.NAME)
    @UserInputField(type = PureConstants.PURE_VOLUME_SIZE_UNIT_LOV_NAME)
    @Persistent
    private String volumeSizeUnit;

    @FormField(label = "Volume Size Number", help = "Numbers", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String volumeSizeNumber;

    private Boolean newVolumeTaskFlag;

    private Boolean destroyVolumeTaskFlag;

    private String hostConnection;

    private String hostGroupConnection;

    private String noRollBackVolumeName;

    public NewVolumeTaskConfig(){}

    public NewVolumeTaskConfig(DestroyVolumesTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.volumePreName = config.getVolumePreName();
        this.startNumber = config.getStartNumber();
        this.endNumber = config.getEndNumber();
        this.destroyVolumeTaskFlag = config.getDestroyVolumeTaskFlag();
        this.hostConnection = config.getHostConnection();
        this.hostGroupConnection = config.getHostGroupConnection();
    }

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_NEW_VOLUME;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    public String getVolumePreName()
    {
        return volumePreName;
    }
    public void setVolumePreName(String volumePreName)
    {
        this.volumePreName = volumePreName;
    }
    public String getStartNumber() { return startNumber; }

    public void setStartNumber(String startNumber)
    {
        this.startNumber = startNumber;
    }
    public String getEndNumber() {return endNumber;}
    public void setEndNumber(String endNumber)
    {
        this.endNumber = endNumber;
    }
    public String getVolumeSizeUnit()
    {
        return volumeSizeUnit;
    }
    public void setVolumeSizeUnit(String volumeSizeUnit)
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
    public void setNewVolumeTaskFlag(Boolean newVolumeTaskFlag)
    {
        this.newVolumeTaskFlag = newVolumeTaskFlag;
    }

    public Boolean getNewVolumeTaskFlag()
    {
        return newVolumeTaskFlag;
    }

    public Boolean getDestroyVolumeTaskFlag()
    {
        return destroyVolumeTaskFlag;
    }

    public String getHostConnection()
    {
        return hostConnection;
    }

    public String getHostGroupConnection()
    {
        return hostGroupConnection;
    }

    public void setNoRollBackVolumeName(String noRollBackVolumeName)
    {
        this.noRollBackVolumeName = noRollBackVolumeName;
    }

    public String getNoRollBackVolumeName()
    {
        return noRollBackVolumeName;
    }

}