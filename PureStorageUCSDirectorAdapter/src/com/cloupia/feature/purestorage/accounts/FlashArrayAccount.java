package com.cloupia.feature.purestorage.accounts;

import java.util.List;

import com.cisco.cuic.api.client.JSON;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.cIaaS.network.model.DeviceCredential;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.model.cIM.InfraAccount;
import com.cloupia.service.cIM.inframgr.collector.view2.ConnectorCredential;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.log4j.Logger;

@PersistenceCapable(detachable = "true", table = "flasharray_account")
public class FlashArrayAccount extends AbstractInfraAccount implements ConnectorCredential
{

    static Logger logger = Logger.getLogger(FlashArrayAccount.class);

    // ManagementAddress
    @Persistent
    @FormField(label = "ManagementAddress", mandatory = true, 
               help = "Endpoint used to manage the array using the REST API", type = FormFieldDefinition.FIELD_TYPE_TEXT)
    private String managementAddress;
    
    public String getManagementAddress()
    {
        return managementAddress;
    }

    public void setManagementAddress(String managementAddress)
    {
        this.managementAddress = managementAddress;
    }

    // Password
    @Persistent
    @FormField(label = "Password", mandatory = true, help = "Password", type = FormFieldDefinition.FIELD_TYPE_PASSWORD)
    private String password;

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public void setPassword(String password)
    {
        this.password = password;
    }

    // Username
    @Persistent
    @FormField(label = "Username", mandatory = true, help = "Username", type = FormFieldDefinition.FIELD_TYPE_TEXT)
    private String username;

    @Override
    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    @Override
    public String getPolicy()
    {
        return null;
    }

    @Override
    public boolean isCredentialPolicy()
    {
        return false;
    }

    @Override
    public void setCredentialPolicy(boolean isCredentialPolicy)
    {
        assert false;
    }

    @Override
    public void setPolicy(String policy)
    {
        assert false;
    }

    @Override
    public void setPort(int port)
    {
    }

    @Override
    public void setProtocol(String protocol)
    {
    }

    public String getServerAddress()
    {
        return this.managementAddress;
    }

    @Override
    public InfraAccount toInfraAccount() 
    {
        try {
            ObjStore<InfraAccount> store = ObjStoreHelper
                    .getStore(InfraAccount.class);
            String cquery = "server == '"
                    + managementAddress + "' && userID == '" + username    + "'";
            logger.debug("query = " + cquery);

            List<InfraAccount> accList = store.query(cquery);
            if (accList != null && accList.size() > 0)
                return accList.get(0);
            else
                return null;
        } catch (Exception e) {
            logger.error("Exception while mapping DeviceCredential to InfraAccount for server: "
                    + managementAddress + ": " + e.getMessage());
        }

        return null;
    }

    public static FlashArrayAccount getFlashArrayCredential(String accountName) throws Exception
    {
        PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);
        if (acc == null)
        {
            throw new Exception("Unable to find the account:" + accountName);
        }
        String json = acc.getCredential();
        AbstractInfraAccount specificAcc  =  (AbstractInfraAccount) JSON.jsonToJavaObject(json, FlashArrayAccount.class);
        specificAcc.setAccount(acc);

        return (FlashArrayAccount) specificAcc;
    }
}
