package com.cloupia.feature.purestorage.reports;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.model.cIM.Group;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.host.PureHost;
import com.purestorage.rest.host.PureHostConnection;
import com.purestorage.rest.hostgroup.PureHostGroup;
import com.purestorage.rest.hostgroup.PureHostGroupConnection;
import com.purestorage.rest.PureRestClient;

import java.util.List;

public class HostGroupReportImpl implements TabularReportGeneratorIf
{
    static Logger logger = Logger.getLogger(HostGroupReportImpl.class);

    @Override
    public TabularReport getTabularReportReport(ReportRegistryEntry entry, ReportContext context) throws Exception
    {
        logger.info("Entering HostGroupReportImpl.getTabularReportReport" );
        logger.info("ReportContext.getId()=" + context.getId());
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
        TabularReport report = new TabularReport();
        report.setGeneratedTime(System.currentTimeMillis());
        report.setReportName(entry.getReportLabel());
        report.setContext(context);

        TabularReportInternalModel model = new TabularReportInternalModel();
        model.addTextColumn("ID", "ID",true);
        model.addTextColumn("Name", "Host Group Name");
        model.addNumberColumn("Hosts", "No. of Hosts");
        model.addNumberColumn("Volumes", "Number of volumes", false);
        model.addDoubleColumn("Provisioned(GB)", "Provisioned size of attached volumes");
        model.addDoubleColumn("Volumes(Capacity)", "Size of volumes", false);
        model.addDoubleColumn("Reduction", "Reduction", false);
        
        
        model.completedHeader();

        if (accountName != null && accountName.length() > 0)
        {
            FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
            PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
            List<PureHostGroup> hostgroups =  CLIENT.hostGroups().list();
            for (PureHostGroup hostgroup: hostgroups)
            {
            	
            	model.addTextValue(accountName+"@"+hostgroup.getName());
            	//model.addTextValue(accountName+"@"+hostgroup.getName());
            	model.addTextValue(hostgroup.getName()); // Name
                model.addNumberValue(hostgroup.getHosts().size()); //No. of Hosts
                List<PureHostGroupConnection> connections = CLIENT.hostGroups().getConnections(hostgroup.getName());
                
                // private and shared connections cannot overlap (i.e. same vol cannot be part of both shared and private connections)
                model.addNumberValue(connections.size()); // Number of volumes
                long totalSize = 0;
                for (PureHostGroupConnection connection: connections)
                {
                	
                    
                	totalSize += CLIENT.volumes().get(connection.getVolumeName()).getSize();
                    
                }
                
                model.addDoubleValue(totalSize/(1024*1024*1024)); // Provisioned size of attached volumes
                model.addDoubleValue(CLIENT.hostGroups().getSpaceMetrics(hostgroup.getName()).getVolumes()/(1024*1024*1024)); // Provisioned size of attached volumes
                model.addDoubleValue(CLIENT.hostGroups().getSpaceMetrics(hostgroup.getName()).getDataReduction()); // Provisioned size of attached volumes
                
                model.completedRow();
            }
        }

        model.updateReport(report);
        return report;
    }
}