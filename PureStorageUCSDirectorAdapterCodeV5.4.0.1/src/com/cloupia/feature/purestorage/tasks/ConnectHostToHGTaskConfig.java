package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FieldValidation;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;
import com.cloupia.service.cIM.inframgr.forms.wizard.HideFieldOnCondition;


@PersistenceCapable(detachable = "true", table = "psucs_connect_host_to_hostgroup_task_config")
public class ConnectHostToHGTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    private String accountName;

    @FormField(label = "Host Name", help = "Use ',' to seperate hosts name", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String hostName;

    @FormField(label = "HostGroup Name", help = "FlashArray HostGroup Name", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String hostGroupName;
    
    

    @Persistent
    private String testFlag;

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_CONNECT_HOST_HOSTGROUP;
    }

    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public String getHostName()
    {
        return hostName;
    }

    
    public void setHostGroupName(String hostGroupName)
    {
        this.hostGroupName = hostGroupName;
    }
    public String getHostGroupName()
    {
        return hostGroupName;
    }

    
    
    public void setTestFlag(String testFlag)
    {
        this.testFlag = testFlag;
    }

    public String getTestFlag()
    {
        return testFlag;
    }

}