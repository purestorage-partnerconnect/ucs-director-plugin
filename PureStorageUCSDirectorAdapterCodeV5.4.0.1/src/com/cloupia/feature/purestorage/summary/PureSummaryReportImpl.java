package com.cloupia.feature.purestorage.summary;

import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.feature.purestorage.reports.SpaceReportImpl;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.SummaryReportInternalModel;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.array.PureArraySpaceMetrics;
import com.purestorage.rest.volume.PureVolume;


public class PureSummaryReportImpl implements TabularReportGeneratorIf {
	
	static Logger logger = Logger.getLogger(PureSummaryReportImpl.class);

	private static final String[] GROUP_ORDER = { "Overview" };

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		
		
        logger.info("Entering SpaceReportImpl.getTabularReportReport" );
        logger.info("ReportContext.getId()=" + context.getId()+context);
        String accountName;
        
        if(context.getId().contains(";"))   //Checking the Context 
        {
        	 String[] parts = context.getId().split(";");
             accountName = parts[0];
           	
        }
        else
        {
           accountName = context.getId();
        }
        int noOfVolume=0,noOfSnapshot=0,noOfHost=0,noOfHostGroup=0;
        String version="",ipAddress="",pod="";
        float dataReduction = 0;
        
        
        if (accountName != null && accountName.length() > 0)
        {
            FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
            PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
            PureArraySpaceMetrics spaceMetrics = CLIENT.array().getSpaceMetrics();
            version = CLIENT.array().getAttributes().getVersion();
            ipAddress =acc.getManagementAddress();
            noOfVolume =  CLIENT.volumes().list().size();
            noOfSnapshot = CLIENT.volumes().getSnapshots().size();
            noOfHost = CLIENT.hosts().list().size();
            noOfHostGroup =  CLIENT.hostGroups().list().size();
            dataReduction = spaceMetrics.getDataReduction();
            pod = acc.getPod();
            
            
        }
		
		
		TabularReport report = new TabularReport();
        report.setContext(context);
        report.setGeneratedTime(System.currentTimeMillis());
        report.setReportName(reportEntry.getReportLabel());

        SummaryReportInternalModel model = new SummaryReportInternalModel();        

        model.addText("Account Name", accountName, "Overview" );
        model.addText("IP Address",ipAddress , "Overview");
        model.addText("Version",version , "Overview");
        model.addText("Pod",pod , "Overview");
        model.addNumber("No. Of Volumess",noOfVolume , "Overview");
        model.addNumber("No. Of Snapshots",noOfSnapshot , "Overview");
        model.addNumber("No. Of Hosts",noOfHost , "Overview");
        model.addNumber("No. Of HostGroups", noOfHostGroup, "Overview");
        model.addDouble("Data Reduction", dataReduction, "Overview");
        
        
        
          
        
        model.setGroupOrder(GROUP_ORDER);
        model.updateReport(report);
        
        return report;
	}

}
