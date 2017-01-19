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
import com.purestorage.rest.PureRestClient;

import java.util.List;

public class ChildHostReportImpl implements TabularReportGeneratorIf
{
    static Logger logger = Logger.getLogger(ChildHostReportImpl.class);

    @Override
    public TabularReport getTabularReportReport(ReportRegistryEntry entry, ReportContext context) throws Exception
    {
        logger.info("Entering HostReportImpl.getTabularReportReport" );
        logger.info("ReportContext.getId()=" + context.getId()+context);
        String accountName="",hostGroupName="";
        if(context.getId().contains("@"))   //Checking the Context 
        {
        	 String[] parts = context.getId().split("@");
             accountName = parts[0];
             hostGroupName = parts[1];
           	
        }
        else
        {
        	logger.error("Error in Report Generation ..Wrong Conext" + context);
            
        }
        TabularReport report = new TabularReport();
        report.setGeneratedTime(System.currentTimeMillis());
        report.setReportName(entry.getReportLabel());
        report.setContext(context);

        TabularReportInternalModel model = new TabularReportInternalModel();
        model.addTextColumn("Account Name", "Account Name");
        model.addTextColumn("Hosts", "Host Name");
        model.addTextColumn("Host Group", "Host Group");
        model.addNumberColumn("Volumes", "Number of volumes", false);
        model.addTextColumn("Connected Volumes", "Connected Volumes");
        model.addDoubleColumn("Provisioned(GB)", "Provisioned size of attached volumes");
        model.completedHeader();

        if (accountName != null && accountName.length() > 0)
        {
            FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
            PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
            List<PureHost> hosts =  CLIENT.hosts().list();
            List<PureHostGroup> hostGroups =  CLIENT.hostGroups().list();
            
            for (PureHostGroup hostgroup: hostGroups)
            {
	            if (hostgroup.getName().equals(hostGroupName))
	            {
	            	List<String> hosts1 =hostgroup.getHosts();
	            	logger.info("Hosts in Hostgroup :"+ hosts1);
	            	for (PureHost host: hosts)
	                {
	                	if(hosts1.contains(host.getName()))
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
            	
            }
        }
        

        model.updateReport(report);
        return report;
    }
}