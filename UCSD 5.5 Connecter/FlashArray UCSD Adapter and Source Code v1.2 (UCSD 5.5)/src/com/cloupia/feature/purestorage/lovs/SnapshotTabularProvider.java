package com.cloupia.feature.purestorage.lovs;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.feature.purestorage.reports.VolumeReport;
import com.cloupia.feature.purestorage.reports.VolumeReportImpl;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.FormLOVPair;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.port.PureArrayPort;
import com.purestorage.rest.protectiongroup.PureVolumeSnapshot;
import com.purestorage.rest.volume.PureVolume;

public class SnapshotTabularProvider implements TabularReportGeneratorIf {

	public static final String TABULAR_PROVIDER = "pure_snapshot_tabular_provider";
	 static Logger logger = Logger.getLogger(SnapshotTabularProvider.class);
	
	

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		
		
        TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Account Name", "Account Name");
        model.addTextColumn("Snapshot Name", "Snapshot Name");
        model.addDoubleColumn("Size(GB)", "Size");
        model.addTextColumn("Source", "Source");
        model.addTimeColumn("Creation","Creation");
        model.completedHeader();
		List<PhysicalInfraAccount> accounts = AccountUtil.getAccountsByType("FlashArray");
        
        int i = 0;
        for (PhysicalInfraAccount account:accounts)
        {
      	  
            String accountName = account.getAccountName();
            logger.info("Found account:" + accountName);
            if (accountName != null && accountName.length() > 0)
            {
            	FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
                PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
                List<PureVolumeSnapshot> snapshots =  CLIENT.volumes().getSnapshots();
                for (PureVolumeSnapshot snapshot: snapshots)
                {
                	model.addTextValue(accountName);
                    model.addTextValue(snapshot.getName());
                    model.addDoubleValue(snapshot.getSize()/(1024*1024*1024));
                    model.addTextValue(snapshot.getSource());
                    model.addTimeValue(snapshot.getCreatedTimestamp().getTime());
                    model.completedRow();

                }
            }
        }

		model.updateReport(report);

		return report;
	}

}

