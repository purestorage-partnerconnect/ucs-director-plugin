package com.cloupia.feature.purestorage.tasks;


import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable(detachable = "true", table = "psucs_delete_host_task_config")
public class DeleteHostTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    private String accountName;

    @FormField(label = "Host Name", help = "FlashArray Host Name", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String hostName;

    private String wwns;

    private String iqns;

    private String privateVolumes;

    private String hostGroupName;

    private Boolean deleteHostFlag;

    private Boolean newHostFlag;

    private Boolean existHost;

    public DeleteHostTaskConfig() {}

    public DeleteHostTaskConfig(NewHostTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.hostName = config.getHostName();
        this.newHostFlag = config.getNewHostFlage();
        this.existHost = config.getExistHost();
    }

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_DELETE_HOST;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    public String getHostName()
    {
        return hostName;
    }
    public void setHostName(String hostName){
        this.hostName = hostName;
    }

    
    public void setWwns(String wwns){
        this.wwns = wwns;
    }

    public String getWwns()
    {
        return wwns;
    }

    public void setIqns(String iqns)
    {
        this.iqns = iqns;
    }

    public String getIqns()
    {return iqns;}

    public void setPrivateVolumes(String privateVolumes)
    {
        this.privateVolumes = privateVolumes;
    }

    public String getPrivateVolumes()
    {
        return privateVolumes;
    }

    public void setHostGroupName(String hostGroupName)
    {
        this.hostGroupName = hostGroupName;
    }

    public String getHostGroupName()
    {
        return hostGroupName;
    }

    public void setDeleteHostFlag(Boolean deleteHostFlag)
    {
        this.deleteHostFlag = deleteHostFlag;
    }

    public Boolean getDeleteHostFlag()
    {
        return deleteHostFlag;
    }

    public Boolean getNewHostFlag()
    {
        return newHostFlag;
    }

    public Boolean getExistHost()
    {
        return existHost;
    }

}
