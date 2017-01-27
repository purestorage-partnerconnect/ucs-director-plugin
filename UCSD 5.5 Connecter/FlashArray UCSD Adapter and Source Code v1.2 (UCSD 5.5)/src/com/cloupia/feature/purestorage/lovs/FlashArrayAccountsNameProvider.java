package com.cloupia.feature.purestorage.lovs;

import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.model.cIM.FormLOVPair;
import com.cloupia.service.cIM.inframgr.forms.wizard.LOVProviderIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

import java.util.List;

import org.apache.log4j.Logger;

public class FlashArrayAccountsNameProvider implements LOVProviderIf 
{
       public static final String NAME = "FlashArrayAccountsNameProvider";

       static Logger logger = Logger.getLogger(FlashArrayAccount.class);
       
       public FormLOVPair[] getLOVs(WizardSession session) {

          FormLOVPair[] lov = null;
          try
          {
              List<PhysicalInfraAccount> accounts = AccountUtil.getAccountsByType("FlashArray");
              lov = new FormLOVPair[accounts.size()];
              int i = 0;
              for (PhysicalInfraAccount account:accounts)
              {
            	  
                  String accountName = account.getAccountName();
                  logger.info("Found account:" + accountName);
                  lov[i++] = new FormLOVPair(accountName,accountName);
              }
          }
          catch (Exception ex)
          {
              logger.info("Exception trying to retrieve FlashArray PhysicalInfraAccount", ex);
          }
          return lov;
       }
    }
