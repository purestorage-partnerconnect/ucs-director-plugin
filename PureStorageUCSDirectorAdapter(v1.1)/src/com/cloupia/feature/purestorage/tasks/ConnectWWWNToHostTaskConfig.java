package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.feature.purestorage.lovs.HostTabularProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "psucs_connect_wwns_to_host_task_config")
public class ConnectWWWNToHostTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Host Name", help = "Use ',' to seperate hosts name", mandatory = true,type=FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table= HostTabularProvider.TABULAR_PROVIDER)
    @UserInputField(type = PureConstants.PURE_HOST_LIST_TABLE_NAME)
    @Persistent
    private String hostName;
    
    @FormField(label = "Configure Fiber Channel WWPNs", help = "Fiber Channel WWPNs for Host. Use ',' to seperate WWN", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String wwns;

    @FormField(label = "Configure iSCSI IQNs", help = "iSCSI IQNs for Host. Use ',' to seperate WWN", mandatory = false)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String iqns;

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_CONNECT_WWN_To_HOST;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    public String getWWNs()
    {
        return wwns;
    }
    public void setWWNs(String wwns)
    {
        this.wwns = wwns;
    }
    public String getIQNs()
    {
        return iqns;
    }
    public void setIQNs(String iqns)
    {
        this.iqns = iqns;
    }

    public String getHostName()
    {
        return hostName;
    }

}