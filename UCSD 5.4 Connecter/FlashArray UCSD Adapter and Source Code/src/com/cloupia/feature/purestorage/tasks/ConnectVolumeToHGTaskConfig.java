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
import com.cloupia.service.cIM.inframgr.forms.wizard.FieldValidation;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;
import com.cloupia.service.cIM.inframgr.forms.wizard.HideFieldOnCondition;


@PersistenceCapable(detachable = "true", table = "psucs_connect_volume_to_hostgroup_task_config")
public class ConnectVolumeToHGTaskConfig extends GeneralTaskConfig
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
	@FormField(label = "Specify LUN", validate = true, help = "Select if you want to give LUN as Admin Input.", type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	@UserInputField(type = WorkflowInputFieldTypeDeclaration.BOOLEAN)
	private boolean isStatusChange = false;
	
	@FormField(label = "LUN Id", help = "Use ',' to seperate the LUN Ids", mandatory = true )
	@HideFieldOnCondition(field = "isStatusChange", op = FieldValidation.OP_EQUALS, value = "false")
	@UserInputField(type =  WorkflowInputFieldTypeDeclaration.GENERIC_TEXT)
	@Persistent
	private String   allLunId;


    @Persistent
    private String testFlag;

    public String getDisplayLabel()
    {
        return PureConstants.TASK_NAME_CONNECT_VOLUME_HOSTGROUP;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public void setHostGroupName(String hostGroupName)
    {
        this.hostGroupName = hostGroupName;
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

    public boolean getIsStatusChange() {
		return isStatusChange;
	}

	public void setIsStatusChange(boolean isStatusChange) {
		this.isStatusChange = isStatusChange;
	}
		
	public String getAllLunId() {
		return allLunId;
	}

	public void setAllLunId(String allLunId) {
		this.allLunId = allLunId;
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