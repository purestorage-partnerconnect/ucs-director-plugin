package com.cloupia.feature.purestorage.reports;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.FormLOVPair;
import com.cloupia.model.cIM.Group;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.array.PureArraySpaceMetrics;
import com.purestorage.rest.host.PureHost;
import com.purestorage.rest.host.PureHostConnection;
import com.purestorage.rest.hostgroup.PureHostGroup;
import com.purestorage.rest.hostgroup.PureHostGroupConnection;
import com.purestorage.rest.protectiongroup.PureVolumeSnapshot;
import com.purestorage.rest.volume.PureVolume;
import com.purestorage.rest.PureRestClient;

import java.util.ArrayList;
import java.util.List;

public class ArrayReportImpl implements TabularReportGeneratorIf
{
    static Logger logger = Logger.getLogger(ArrayReportImpl.class);
	
	//private String[] podAccounts;

    @Override
    public TabularReport getTabularReportReport(ReportRegistryEntry entry, ReportContext context) throws Exception
    {
        logger.info("Entering ArrayReportImpl.getTabularReport" );
        logger.info("ReportContext.getId()=" + context.getId());
        String podName;
        String accountName = null;
        List<PhysicalInfraAccount> accounts ;
        
        List<String> podAccounts = new ArrayList<String>();
        
        
        if(context.getId().contains(";"))   //Checking the Context 
        {
        	 String[] parts = context.getId().split(";");
             podName = parts[1];
           	
        }
        else
        {
           podName = context.getId();
        }
        try
        {
            accounts = AccountUtil.getAccountsByType("FlashArray");
            
            
            for (PhysicalInfraAccount account:accounts)
            {
          	  
                 //accountName = account.getAccountName();
                 
                 logger.info("Account's Pod Name :"+account.getPodName()+" and Context's pod name :"+podName);
                 logger.info("Current account:" + account.getAccountName());
                 String pod=account.getPodName();
                 String acct=account.getAccountName();
                 
                 logger.info("account....."+acct);
//                podAccounts.add("pure1");
//                logger.info("....."+podAccounts.get(0));
                 if (podName.equals(pod))
                 {
                	 logger.info("Found account.....");
                	 podAccounts.add(acct);
                	 logger.info("Found");
                	 logger.info("......### " + account.getAccountName());
                     
                 }
                
            }
        }
        catch (Exception ex)
        {
            logger.info("Exception trying to retrieve FlashArray PhysicalInfraAccount", ex);
        }
      
        TabularReport report = new TabularReport();
        report.setGeneratedTime(System.currentTimeMillis());
        report.setReportName(entry.getReportLabel());
        report.setContext(context);
        logger.info("Context :"+context);
        TabularReportInternalModel model = new TabularReportInternalModel();
        model.addTextColumn("Account Name", "Account Name");
        model.addLongNumberColumn("Total Raw Capacity(GB)", "Total Raw Capacity(GB)");
        model.addLongNumberColumn("Total Provisioned Capacity(GB)", "Total Provisioned Capacity(GB)");
        model.addTextColumn("Free Space(GB)", "Free Space(GB)", false);
        model.addTextColumn("Shared Space(GB)", "Shared Space(GB)");
        model.addTextColumn("System(GB)", "System(GB)", false);
        model.addTextColumn("Volumes Capacity(GB)", "Volumes Capacity(GB)"+ "", false);
        model.addTextColumn("Snapshots Capacity(GB)", "Snapshots Capacity(GB)", false);
        model.addDoubleColumn("Data Reduction", "De-dupe ratio"+ "", false);
        model.addNumberColumn("Number of Volumes", "Number of Volumes", false);
        model.addNumberColumn("Number of Snapshot", "Number of Snapshot"+ "", false);
        model.addNumberColumn("No. Of Hosts", "No. Of Hosts", false);
        model.addNumberColumn("No. Of HostGroups", "No. Of HostGroups"+ "", false);
        model.addTextColumn("IP Address", "IP Address", false);
        model.addTextColumn("Version", "Version"+ "", false);
        
        
        model.completedHeader();
        
        logger.info("Completed Row");
        logger.info("Accounts "+podAccounts);
        
        for(String podAccount:podAccounts)
        {
            accountName=podAccount;
            logger.info("Account Name is "+accountName);
        if (accountName != null && accountName.length() > 0)
        {
            FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
            PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
            logger.info("Getting Array Detail......");
            PureArraySpaceMetrics spaceMetrics = CLIENT.array().getSpaceMetrics();
            long provisionedCapacity = (long) 0.0;
            int noOfVolume=0,noOfSnapshot=0,noOfHost=0,noOfHostGroup=0;
            String version="",ipAddress="";
           
            List<PureVolume> volumes =  CLIENT.volumes().list();
            for (PureVolume volume: volumes)
            {
            	provisionedCapacity +=volume.getSize();
           	           	          	
            }
            version = CLIENT.array().getAttributes().getVersion();
            ipAddress =acc.getManagementAddress();
            noOfVolume =  CLIENT.volumes().list().size();
            noOfSnapshot = CLIENT.volumes().getSnapshots().size();
            noOfHost = CLIENT.hosts().list().size();
            noOfHostGroup =  CLIENT.hostGroups().list().size();
           
            long freeSpace = spaceMetrics.getCapacity() - spaceMetrics.getTotal();
            logger.info("Details are getting stored....");
            //logger.info(accountName+" "+);
            	model.addTextValue(accountName);
            	model.addLongNumberValue(spaceMetrics.getCapacity()/(1024*1024*1024)); // Name
                model.addLongNumberValue(provisionedCapacity/(1024*1024*1024)); //No. of Hosts
                model.addTextValue(""+(freeSpace/(1024*1024*1024))); // Provisioned size of attached volumes
                model.addTextValue(""+(spaceMetrics.getSharedSpace()/(1024*1024*1024))); // Provisioned size of attached volumes
                model.addTextValue(""+(spaceMetrics.getSystem()/(1024*1024*1024))); // Provisioned size of attached volumes
                model.addTextValue(""+(spaceMetrics.getVolumes()/(1024*1024*1024))); // Provisioned size of attached volumes
                model.addTextValue(""+(spaceMetrics.getSnapshots()/(1024*1024*1024))); // Provisioned size of attached volumes
                model.addDoubleValue(spaceMetrics.getDataReduction()); // Provisioned size of attached volumes
                model.addNumberValue(noOfVolume); // Provisioned size of attached volumes
                model.addNumberValue(noOfSnapshot); // Provisioned size of attached volumes
                model.addNumberValue(noOfHost); // Provisioned size of attached volumes
                model.addNumberValue(noOfHostGroup); // Provisioned size of attached volumes
                model.addTextValue(ipAddress);
                model.addTextValue(version);
                logger.info("Values is added");
                
                model.completedRow();
        }
        }

        model.updateReport(report);
        return report;
    }
}