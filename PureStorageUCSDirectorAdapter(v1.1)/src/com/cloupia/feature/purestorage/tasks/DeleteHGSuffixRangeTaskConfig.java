package com.cloupia.feature.purestorage.tasks;


import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable(detachable = "true", table = "psucs_delete_hg_suffix_range_task_config")
public class DeleteHGSuffixRangeTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "HostGroup PrefixName or WholeName", help = "FlashArray HostGroup PrefixName or WholeName", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String hostGroupPreName;

    @FormField(label = "HostGroup SuffixStartNumber", help = "FlashArray HostGroup SuffixStartNumber", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String startNumber;

    @FormField(label = "HostGroup SuffixEndNumber", help = "FlashArray HostGroup SuffixEndNumber", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String endNumber;

    @Persistent
    private String hostConnection;

    @Persistent
    private String volumeConnection;

    @Persistent
    private String existHostGroup;

    private Boolean newHostGroupTaskFlag;

    private String noRollBackHostGroupName;

    public DeleteHGSuffixRangeTaskConfig(){};

    public DeleteHGSuffixRangeTaskConfig(NewHostGroupTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.hostGroupPreName = config.getHostGroupPreName();
        this.startNumber = config.getStartNumber();
        this.endNumber = config.getEndNumber();
        this.newHostGroupTaskFlag = config.getNewHostGroupTaskFlag();
        this.noRollBackHostGroupName = config.getNoRollBackHostGroupName();
    }

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_DELETE_HG_SUFFIX_RANGE;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    public String getHostGroupPreName()
    {
        return hostGroupPreName;
    }
    public void setHostGroupPreName(String hostGroupPreName)
    {
        this.hostGroupPreName = hostGroupPreName;
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

    public void setHostConnection(String hostConnection){
        this.hostConnection = hostConnection;
    }

    public String getHostConnection()
    {
        return hostConnection;
    }

    public void setVolumeConnection(String volumeConnection)
    {
        this.volumeConnection = volumeConnection;
    }

    public String getVolumeConnection()
    {
        return volumeConnection;
    }

    public void setExistHostGroup(String existHostGroup)
    {
        this.existHostGroup = existHostGroup;
    }

    public String getExistHostGroup()
    {
        return existHostGroup;
    }

    public Boolean getNewHostGroupTaskFlag()
    {
        return newHostGroupTaskFlag;
    }

    public String getNoRollBackHostGroupName()
    {
        return noRollBackHostGroupName;
    }


}
