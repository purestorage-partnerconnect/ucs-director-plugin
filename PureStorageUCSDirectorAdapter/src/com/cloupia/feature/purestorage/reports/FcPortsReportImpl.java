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
import com.purestorage.rest.port.PureArrayPort;
import com.purestorage.rest.volume.PureVolume;
import com.purestorage.rest.PureRestClient;

import java.util.ArrayList;
import java.util.List;

public class FcPortsReportImpl implements TabularReportGeneratorIf
{
    static Logger logger = Logger.getLogger(FcPortsReportImpl.class);

    @Override
    public TabularReport getTabularReportReport(ReportRegistryEntry entry, ReportContext context) throws Exception
    {
        logger.info("Entering FCPortsReportImpl.getTabularReportReport" );
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
        model.addTextColumn("Account Name", "Account Name");
        model.addTextColumn("Name", "Name");
		model.addTextColumn("WWPN", "WWPN");
		
        model.addTextColumn("Speed(GB/S)","Speed(GB/S)");
        model.completedHeader();

        if (accountName != null && accountName.length() > 0)
        {
            FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
            PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
            List<PureArrayPort> ports =  CLIENT.ports().list();
            for (PureArrayPort port: ports)
            {
            	if(port.getName().contains("FC"))
            	{
            		String speed ="";
            		long sp = CLIENT.hardware().getHardwareAttributes(port.getName()).getSpeed();
            		if(sp==0)
            		{
            		speed="0";	
            		}
            		else
            		{
            		sp=sp/1000000000;
            		speed=""+sp;
            		}
            		String wwpn =port.getWwn();
            		List<String> strings = new ArrayList<String>();
            		int index = 0;
            		while (index < wwpn.length()) {
            		    strings.add(wwpn.substring(index, Math.min(index + 2,wwpn.length())));
            		    index += 2;
            		}
            		logger.info(strings);
            		String wwn=strings.get(0);
            		for (int i1 = 1; i1 < strings.size(); i1++) {
            			//System.out.println(strings.get(i));
            			wwn=wwn+":"+strings.get(i1);
            		}
            		//wwn=wwn+":"+strings.get(strings.size()-1);
            		logger.info(wwn);

            model.addTextValue(accountName);
            model.addTextValue(port.getName());
            model.addTextValue(wwn);	
			
			model.addTextValue(speed);
			model.completedRow();
            	}
			
			
			
		}

        
    }

        model.updateReport(report);
        return report;
    }
}