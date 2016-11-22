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
import com.purestorage.rest.host.PureHost;
import com.purestorage.rest.host.PureHostConnection;
import com.purestorage.rest.port.PureArrayPort;
import com.purestorage.rest.volume.PureVolume;

public class HostTabularProvider implements TabularReportGeneratorIf {

	public static final String TABULAR_PROVIDER = "pure_host_tabular_provider";
	 static Logger logger = Logger.getLogger(HostTabularProvider.class);
	
	

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		
		
        TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Account Name", "Account Name");
        model.addTextColumn("Name", "Host Name");
        model.addTextColumn("HostGroup", "HostGroup");
        model.addNumberColumn("Volumes", "Number of volumes", false);
        model.addTextColumn("Connected Volumes", "Connected Volumes");
        model.addDoubleColumn("Provisioned(GB)", "Provisioned size of attached volumes");
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
                List<PureHost> hosts =  CLIENT.hosts().list();
                for (PureHost host: hosts)
                {
                	model.addTextValue(accountName);
                    model.addTextValue(host.getName()); // Name
                    model.addTextValue(host.getHostGroupName()); //HostGroup
                    List<PureHostConnection> privateConnections = CLIENT.hosts().getPrivateConnections(host.getName());
                    List<PureHostConnection> sharedConnections = CLIENT.hosts().getSharedConnections(host.getName());
                    // private and shared connections cannot overlap (i.e. same vol cannot be part of both shared and private connections)
                    model.addNumberValue(privateConnections.size() + sharedConnections.size()); // Number of volumes
                    long totalSize = 0;
                    String connVolumes="";
                    for (PureHostConnection connection: privateConnections)
                    {
                        totalSize += CLIENT.volumes().get(connection.getVolumeName()).getSize();
                        if(connVolumes=="")
                        {
                        	connVolumes=connection.getVolumeName();	
                        }
                        else
                        {
                        connVolumes=connVolumes+","+connection.getVolumeName();
                        }
                    }
                    for (PureHostConnection connection: sharedConnections)
                    {
                        totalSize += CLIENT.volumes().get(connection.getVolumeName()).getSize();
                        if(connVolumes=="")
                        {
                        	connVolumes=connection.getVolumeName();	
                        }
                        else
                        {
                        connVolumes=connVolumes+","+connection.getVolumeName();
                        }
                    }
                    model.addTextValue(connVolumes);
                    model.addDoubleValue(totalSize/(1024*1024*1024)); // Provisioned size of attached volumes
                    model.completedRow();
                }
            }
        }

		model.updateReport(report);

		return report;
	}

}

