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
import com.purestorage.rest.volume.PureVolume;

public class VolumeTabularProvider implements TabularReportGeneratorIf {

	public static final String TABULAR_PROVIDER = "pure_volume_tabular_provider";
	 static Logger logger = Logger.getLogger(VolumeTabularProvider.class);
	
	

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		
		
        TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Account Name", "Account Name");
        model.addTextColumn("Volume Name", "Volume Name");
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
                List<PureVolume> volumes =  CLIENT.volumes().list();
                for (PureVolume volume: volumes)
                {
                	model.addTextValue(accountName);
                    model.addTextValue(volume.getName());
                    model.addDoubleValue(volume.getSize()/(1024*1024*1024));
                    model.addTextValue(volume.getSource());
                    model.addTimeValue(volume.getCreatedTimestamp().getTime());
                    model.completedRow();

                }
            }
        }

		model.updateReport(report);

		return report;
	}

}

