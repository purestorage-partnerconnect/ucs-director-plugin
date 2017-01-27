package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.feature.purestorage.lovs.HostGroupTabularProvider;
import com.cloupia.feature.purestorage.lovs.VolumeTabularProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "psucs_disconnect_volume_with_hostgroup_task_config")
public class DisconnectVolumeHGTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    @Persistent
    private String accountName;

    @FormField(label = "Volume Name", help = "Use ',' to seperate volumes name", mandatory = true,multiline = true,type=FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table= VolumeTabularProvider.TABULAR_PROVIDER)
    @UserInputField(type = PureConstants.PURE_VOLUME_LIST_TABLE_NAMES)
    @Persistent
    private String volumeName;

    @FormField(label = "HostGroup Name", help = "FlashArray HostGroup Name", mandatory = true,type=FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table= HostGroupTabularProvider.TABULAR_PROVIDER)
    @UserInputField(type = PureConstants.PURE_HOSTGROUP_NAME)
    @Persistent
    private String hostGroupName;

    @Persistent
    private String testFlag;


    public DisconnectVolumeHGTaskConfig(){}

    public DisconnectVolumeHGTaskConfig(ConnectVolumeToHGTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.volumeName = config.getVolumeName();
        this.hostGroupName = config.getHostGroupName();
        this.testFlag = config.getTestFlag();
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
    public String getHostGroupName()
    {
        return hostGroupName;
    }
    public void setHostGroupName(String hostGroupName)
    {
        this.hostGroupName = hostGroupName;
    }
    
    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_DISCONNECT_VOLUMES_WITH_HOSTGROUP;
    }

    public String getTestFlag()
    {
        return testFlag;
    }

}