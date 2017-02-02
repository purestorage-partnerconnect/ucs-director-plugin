package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

import java.util.ArrayList;
import java.util.List;


@PersistenceCapable(detachable = "true", table = "psucs_new_hostgroup_task_config")
public class NewHostGroupTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Host Group Prefix Name or Whole Name", help = "FlashArray Host Group Prefix Name or Whole Name", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String hostGroupPreName;

    @FormField(label = "Host Group suffix Start Number", help = "FlashArray Host Group suffix Start Number", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String startNumber;

    @FormField(label = "Host Group suffix End Number", help = "FlashArray Host Group suffix End Number", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String endNumber;

    @Persistent
    private String hostConnection;

    @Persistent
    private String volumeConnection;

    @Persistent
    private String existHostGroups;

    private Boolean newHostGroupTaskFlag;

    private String noRollBackHostGroupName;

    public NewHostGroupTaskConfig() {}

    public NewHostGroupTaskConfig(DeleteHGSuffixRangeTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.hostGroupPreName = config.getHostGroupPreName();
        this.startNumber = config.getStartNumber();
        this.endNumber = config.getEndNumber();
        this.hostConnection = config.getHostConnection();
        this.volumeConnection = config.getVolumeConnection();
        this.existHostGroups = config.getExistHostGroup();
    }


    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_NEW_HOSTGROUP;
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
        this.startNumber = startNumber;
    }
    public String getEndNumber() {return endNumber;}
    public void setEndNumber(String endNumber)
    {
        this.endNumber = endNumber;
    }
    public String getHostConnection()
    {
        return hostConnection;
    }

    public String getVolumeConnection()
    {
        return volumeConnection;
    }

    public String getExistHostGroups()
    {
        return existHostGroups;
    }

    public void setNewHostGroupTaskFlag(Boolean newHostGroupTaskFlag)
    {
        this.newHostGroupTaskFlag = newHostGroupTaskFlag;
    }

    public Boolean getNewHostGroupTaskFlag()
    {
        return newHostGroupTaskFlag;
    }

    public void setNoRollBackHostGroupName(String noRollBackHostGroupName)
    {
        this.noRollBackHostGroupName = noRollBackHostGroupName;
    }

    public String getNoRollBackHostGroupName()
    {
        return noRollBackHostGroupName;
    }

}