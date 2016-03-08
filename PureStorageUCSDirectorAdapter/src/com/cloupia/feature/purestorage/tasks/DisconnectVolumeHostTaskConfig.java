package com.cloupia.feature.purestorage.tasks;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;


@PersistenceCapable(detachable = "true", table = "psucs_disconnect_host_to_volume_task_config")
public class DisconnectVolumeHostTaskConfig extends GeneralTaskConfig
{

    @FormField(label = "FlashArray Account", help = "FlashArray Account", mandatory=true, type=FormFieldDefinition.FIELD_TYPE_EMBEDDED_LOV,
            lovProvider = FlashArrayAccountsNameProvider.NAME)
    @UserInputField(type = PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME)
    private String accountName;

    @FormField(label = "Volume Name", help = "Use ',' to seperate volumes name", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String volumeName;

    @FormField(label = "Host Name", help = "Host Name", mandatory = true)
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
    @Persistent
    private String hostName;

    public DisconnectVolumeHostTaskConfig(){}

    public DisconnectVolumeHostTaskConfig(ConnectHostVolumeTaskConfig config)
    {
        this.accountName = config.getAccountName();
        this.volumeName = config.getVolumeName();
        this.hostName = config.getHostName();
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
    public String getHostName()
    {
        return hostName;
    }
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_DISCONNECT_VOLUMES_WITH_HOST;
    }

}