package com.cloupia.feature.purestorage.accounts;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.accounts.status.storage.StorageAccountStatusSummary;
import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalConnectivityTestHandler;

import com.purestorage.rest.PureRestClient;

public class FlashArrayTestConnectionHandler extends PhysicalConnectivityTestHandler
{

    private static Logger logger = Logger.getLogger(FlashArrayTestConnectionHandler.class);

    @Override
    public PhysicalConnectivityStatus testConnection(String accountName)
    {
        PhysicalConnectivityStatus status = null;
        FlashArrayAccount acc = null;
        try
        {
            status = new PhysicalConnectivityStatus(AccountUtil.getAccountByName(accountName));
            status.setConnectionOK(true);
            status.setVendor(PureConstants.PURE_STORAGE_INC);
            acc = FlashArrayAccount.getFlashArrayCredential(accountName);
        }
        catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Unable to find FlashArrayAccount " + accountName + " persisted. Exception:" + e.toString() + " StackTrace:" + sw.toString());
            status.setConnectionOK(false);
            status.setErrorMsg(e.toString());
            return null;
        }
        try
        {
            if (acc != null && status != null)
            {
                PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
                StorageAccountStatusSummary.accountSummary(accountName);
                return status;
            }
            else
            {
                logger.error("Unable to find FlashArrayAccount " + accountName);
                status.setConnectionOK(false);
                status.setErrorMsg("Unable to find FlashArrayAccount " + accountName);
                return null;
            }
        }
        catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Failed to get connectivity information for " + accountName + ". Returning false. Exception:" + e.toString() + " StackTrace:" + sw.toString());
            status.setConnectionOK(false);
            status.setErrorMsg(e.toString());
            return status;
        }
    }
}
