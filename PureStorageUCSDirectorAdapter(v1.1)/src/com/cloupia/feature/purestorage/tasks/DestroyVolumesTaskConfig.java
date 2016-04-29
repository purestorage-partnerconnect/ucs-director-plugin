package com.cloupia.feature.purestorage.tasks;


import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable(detachable = "true", table = "psucs_destroy_volume_suffix_range_task_config")
public class DestroyVolumesTaskConfig extends GeneralTaskConfig{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Volume PrefixName or whole name", help = "FlashArray VolumePrefixName", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String volumePreName;

    @FormField(label = "Volume SuffixStartNumber", help = "FlashArray Volume SuffixStartNumber or 'no' ", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String startNumber;

    @FormField(label = "Volume SuffixEndNumber", help = "FlashArray Volume SuffixEndNumber or 'no' ", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String endNumber;

    @FormField(label = "Eradicate", help = "Check box to eradicate volumes", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.BOOLEAN)
    @Persistent
    private boolean eradicate;

    private Boolean newVolumeTaskFlag;

    private Boolean destroyVolumeTaskFlag;

    private String hostConnection;

    private String hostGroupConnection;

    private String noRollBackVolumeName;

    public DestroyVolumesTaskConfig(){}

    public DestroyVolumesTaskConfig(NewVolumeTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.volumePreName = config.getVolumePreName();
        this.startNumber = config.getStartNumber();
        this.endNumber = config.getEndNumber();
        this.newVolumeTaskFlag = config.getNewVolumeTaskFlag();
        this.eradicate = true;
        this.noRollBackVolumeName = config.getNoRollBackVolumeName();
    }

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_DESTROY_VOLUMES_SUFFIX_RANGE;
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
    public String getStartNumber() {return startNumber;}
    public void setStartNumber(String startNumber)
    {
        this.startNumber= startNumber;
    }

    public String getEndNumber() {return endNumber;}
    public void setEndNumber(String endNumber)
    {
        this.endNumber= endNumber;
    }


    public Boolean getEradicate()
    {
        return eradicate;
    }

    public Boolean getNewVolumeTaskFlag()
    {
        return newVolumeTaskFlag;
    }
    public void setEradicate(Boolean eradicate)
    {
        this.eradicate = eradicate;
    }
    public void setDestroyVolumeTaskFlag(Boolean destroyVolumeTaskFlag)
    {
        this.destroyVolumeTaskFlag = destroyVolumeTaskFlag;
    }

    public Boolean getDestroyVolumeTaskFlag()
    {
        return destroyVolumeTaskFlag;
    }

    public void setHostConnection(String hostConnection)
    {
        this.hostConnection = hostConnection;
    }

    public String getHostConnection()
    {
        return hostConnection;
    }

    public void setHostGroupConnection(String hostGroupConnection)
    {
        this.hostGroupConnection = hostGroupConnection;
    }

    public String getHostGroupConnection()
    {
        return hostGroupConnection;
    }

    public String getNoRollBackVolumeName()
    {
        return noRollBackVolumeName;
    }

}
