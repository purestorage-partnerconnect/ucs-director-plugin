package com.cloupia.feature.purestorage.accounts.status.storage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.cisco.cuic.api.client.JSON;
import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.feature.purestorage.accounts.FlashArrayTestConnectionHandler;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.cIaaS.netapp.model.StorageAccountStatus;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.array.PureArrayController;

public class StorageAccountStatusSummary {

	private static Logger logger = Logger.getLogger(StorageAccountStatusSummary.class);

	public static void  accountSummary(String accountName)throws Exception {
		FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);
		
		StorageAccountStatus accStatus = new StorageAccountStatus();
		accStatus.setAccountName(infraAccount.getAccountName());
		accStatus.setDcName(acc.getPod());
		//VERSION
		
		try
        {
            if (acc != null && status != null)
            {    
            	try
            	{
            		PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
                    
            		accStatus.setReachable(true);
                    List<PureArrayController> controllers = CLIENT.array().getControllers();
                    
                    accStatus.setLastMessage("Connection is verified");
                    StringBuilder model = new StringBuilder();
                    // For multiple controllers, show Model as:"FA-250|FA-250"
                    String delim = "";
                    for (PureArrayController controller: controllers)
                    {
                        model.append(delim).append(controller.getModel());
                        delim = "|";
                    }
                    accStatus.setModel(model.toString());
                    accStatus.setVersion(CLIENT.array().getAttributes().getVersion());
              
            	}catch(Exception e)
            	{
            		accStatus.setReachable(false);
                    accStatus.setLastMessage("Connection is unverified/unreachable");
                    logger.error(e);
            	}
                       
            }
            else
            {
                logger.error("Unable to find FlashArrayAccount " + accountName);
                
            }
        }
        catch (Exception e)
        {
        	
        	StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Failed to get connectivity information for " + accountName + ". Returning false. Exception:" + e.toString() + " StackTrace:" + sw.toString());
            
            
        }

		
		accStatus.setLicense("");
		
		accStatus.setLastUpdated(System.currentTimeMillis());
		
		accStatus.setServerAddress(acc.getServerAddress());
		persistStorageAccountStatus(accStatus);
		
	
	}
	
	public static void persistStorageAccountStatus(StorageAccountStatus ac) throws Exception
    {
        PersistenceManager pm = ObjStoreHelper.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try
        {
            tx.begin();

            String query = "accountName == '" + ac.getAccountName() + "'";

            Query q = pm.newQuery(StorageAccountStatus.class, query);
            q.deletePersistentAll();

            pm.makePersistent(ac);
            tx.commit();
        } finally
        {
            try
            {
                if (tx.isActive())
                {
                    tx.rollback();
                }
            }
            finally { pm.close(); }
        }
    }

	
	
	}
