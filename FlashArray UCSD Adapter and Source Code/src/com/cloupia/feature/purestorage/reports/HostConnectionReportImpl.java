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
import com.purestorage.rest.port.PureArrayInitiatorPort;
import com.purestorage.rest.port.PureArrayPort;
import com.purestorage.rest.volume.PureVolume;
import com.purestorage.rest.PureRestClient;

import java.util.ArrayList;
import java.util.List;

public class HostConnectionReportImpl implements TabularReportGeneratorIf
{
    static Logger logger = Logger.getLogger(HostConnectionReportImpl.class);

    @Override
    public TabularReport getTabularReportReport(ReportRegistryEntry entry, ReportContext context) throws Exception
    {
        logger.info("Entering HCReportImpl.getTabularReportReport" );
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
        model.addTextColumn("Host Name", "Host Name");
        model.addTextColumn("Initiator WWPN", "Initiator WWPN");
		
		model.addTextColumn("Initiator IQN", "Initiator IQN");
		model.addTextColumn("Connectivity", "Connectivity");
		model.addTextColumn("Target Interface(s)", "Target Interface(s)");
		
		model.addTextColumn("Target WWPN(s)", "Target WWPN(s)",true);
		model.addTextColumn("Target IQN(s)", "Target IQN(s)",true);
		model.addTextColumn("Initiator Portal", "Initiator Portal",true);
		model.addTextColumn("Target Portal", "Target Portal",true);
		
        
        model.completedHeader();

        if (accountName != null && accountName.length() > 0)
        {
            FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
            PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
            List<PureArrayInitiatorPort> ports =  CLIENT.ports().listInitiatorPorts();
            List<PureHost> hosts =  CLIENT.hosts().list();
            for (PureHost host: hosts)
            {
            	logger.info("## The Current host is "+host.getName());
            	if(host.getWwnList().isEmpty()&&host.getIqnList().isEmpty())
            	{
            		logger.info("No WWPN or IQN ");
            		model.addTextValue(accountName);
                    model.addTextValue(host.getName());
                    model.addTextValue("");	
                    model.addTextValue("");
                    model.addTextValue("NONE");
                    
                    model.addTextValue("");
                    model.addTextValue("");
                    model.addTextValue("");
                    model.addTextValue("");
        			
        			model.addTextValue("");
        			model.completedRow();
            		
            	}
            	else
            	{
            		String ini_wwpn="",ini_iqn="",conn="NONE",intf="",tar_wwpn="",tar_iqn="",ini_portal="",tar_portal="";
            	List<String>  lsWWN=host.getWwnList();
            	
            	if(lsWWN==null||lsWWN.isEmpty())
        		{
        			
        			logger.info("## WWN list is empty "+host.getName());
        		}
            	else
            	{
            		for(int i=0;i<lsWWN.size();i++)
            		{
            			ini_wwpn="";
            			
    					ini_iqn="";
    					conn="NONE";
    					intf="";tar_wwpn="";tar_iqn="";ini_portal="";tar_portal="";
                        
            			
            			for (PureArrayInitiatorPort port: ports){
                		
                		
                		 if(lsWWN.get(i).equalsIgnoreCase(port.getWwn()))
                		{
                			logger.info("## "+host.getName());
                    		logger.info("## "+host.getWwnList());
                    		//logger.info("## "+host.getIqnList());
                			ini_wwpn=getPorts(port.getWwn());
                			logger.info("## WWN port is "+ini_wwpn);
                			if(port.getTarget()==null||port.getTarget().isEmpty())
                    		{
                    			intf="";	
                    			conn="NONE";
                    			logger.info("## the target for WWN is empty ");
                    		}
                    		else
                    		{
                    			if(intf=="")
                    			{
                    				intf=port.getTarget();
                        			conn="Redundant";
                        			logger.info("## target is "+intf);
                    				
                    			}else
                    			{
                    			intf=intf+","+port.getTarget();
                    			conn="Redundant";
                    			logger.info("## target2 is "+intf);
                    			}
                    		}
                    		
                    		
                    		if(port.getTargetWwn()==null||port.getTargetWwn().isEmpty())
                    		{
                    			tar_wwpn="";
                    			logger.info("## the target WWN is empty ");
                    			
                    		}
                    		else
                    		{
                    			if(tar_wwpn=="")
                    			{
                    			tar_wwpn=getPorts(port.getTargetWwn());	
                    			logger.info("## the target WWN is  ");
                    			logger.info("## "+tar_wwpn);
                    			}
                    			else
                    			{
                    				tar_wwpn=tar_wwpn+","+getPorts(port.getTargetWwn());	
                        			logger.info("## the target WWN 2 is  ");
                        			logger.info("## "+tar_wwpn);	
                    			}
                    		}
                    		
                		}
                		 
                		}
            			if(i<lsWWN.size())
	            		{
	            			model.addTextValue(accountName);
	                        model.addTextValue(host.getName());
	                        model.addTextValue(ini_wwpn);	
	                        model.addTextValue(ini_iqn);
	                        model.addTextValue(conn);
	                        
	                        model.addTextValue(intf);
	                        model.addTextValue(tar_wwpn);
	                        model.addTextValue(tar_iqn);
	                        model.addTextValue(ini_portal);
	            			
	            			model.addTextValue(tar_portal);
	            			model.completedRow();
	                		
	            		}
            		
            			
            		}	
            	}
                        	
            	List<String> lsIQN=host.getIqnList();
            	if(lsIQN==null||lsIQN.isEmpty())
        		{
        			
        			logger.info("## IQN list is empty "+host.getName());
        		}
            	else
            	{
            		logger.info("## "+host.getName());
            		logger.info("## "+lsIQN.size());
            		
            		for(int i=0;i<lsIQN.size();i++)
            		{
            			ini_wwpn="";
            			
    					ini_iqn="";
    					conn="NONE";
    					intf="";tar_wwpn="";tar_iqn="";ini_portal="";tar_portal="";
            			logger.info("## inside "+lsIQN.size()); 
	            		for (PureArrayInitiatorPort port: ports)
	                    {
	            			logger.info("## inside port loop "+lsIQN.get(i)+" "+port.getIqn()); 
	            		if(lsIQN.get(i).equalsIgnoreCase(port.getIqn()))
	            		{
	            			logger.info("## "+host.getName());
	                		//logger.info("## "+host.getWwnList());
	                		logger.info("## "+host.getIqnList());
	            			ini_iqn=port.getIqn();
	            			logger.info("## Initiator IQN is "+ini_iqn);
	            			
	            			if(port.getTargetIqn()==null||port.getTargetIqn().isEmpty())
	                		{
	                			tar_iqn="";	
	                			logger.info("## Target IQN is empty");
	                		}
	                		else
	                		{
	                			//tar_iqn=port.getTargetIqn();
	                			//logger.info("## Target IQN is "+tar_iqn);
	                			if(tar_iqn=="")
                    			{
	                				tar_iqn=port.getTargetIqn();
	                				logger.info("## Target IQN is "+tar_iqn);                    			}
                    			else
                    			{
                    				tar_iqn=tar_iqn+","+port.getTargetIqn();	
                    				logger.info("## Target IQN is "+tar_iqn);   	
                    			}
                    	
	                			
	                		}
	                		
	                		
	                		if(port.getTarget()==null||port.getTarget().isEmpty())
	                		{
	                			intf="";	
	                			conn="NONE";
	                			logger.info("## the target for IQN is empty ");
	                		}
	                		else
	                		{
	                			//intf=port.getTarget();
	                			if(intf=="")
                    			{
                    				intf=port.getTarget();
                        			conn="Redundant";
                        			logger.info("## target is "+intf);
                    				
                    			}else
                    			{
                    			intf=intf+","+port.getTarget();
                    			conn="Redundant";
                    			logger.info("## target2 is "+intf);
                    			}
	                		}
	                		
	                		if(port.getPortal()==null||port.getPortal().isEmpty())
	                		{
	                			ini_portal="";
	                			
	                			logger.info("## Initiator portal is empty");
	                		}
	                		else
	                		{
	                			//ini_portal=port.getPortal();
	                			//logger.info("## Initiator portal is "+ini_portal);
	                			if(ini_portal=="")
                    			{
	                				ini_portal=port.getPortal();
	                				logger.info("## initial portal  is "+ini_portal);                    			}
                    			else
                    			{
                    				ini_portal=ini_portal+","+port.getPortal();	
                    				logger.info("## initial portal  is "+ini_portal);    	
                    			}
	                			
	                		}
	                		
	                		if(port.getTargetPortal()==null||port.getTargetPortal().isEmpty())
	                		{
	                			tar_portal="";	
	                			logger.info("## target portal is empty ");
	                		}
	                		else
	                		{
	                			if(tar_portal=="")
                    			{
	                				tar_portal=port.getTargetPortal();
	                				logger.info("## initial portal  is "+tar_portal);                    			}
                    			else
                    			{
                    				tar_portal=ini_portal+","+port.getTargetPortal();	
                    				logger.info("## initial portal  is "+tar_portal);    	
                    			}
	                		}
	                		
	            		  }
	                    }
	            		if(i<lsIQN.size())
	            		{
	            			model.addTextValue(accountName);
	                        model.addTextValue(host.getName());
	                        model.addTextValue(ini_wwpn);	
	                        model.addTextValue(ini_iqn);
	                        model.addTextValue(conn);
	                        
	                        model.addTextValue(intf);
	                        model.addTextValue(tar_wwpn);
	                        model.addTextValue(tar_iqn);
	                        model.addTextValue(ini_portal);
	            			
	            			model.addTextValue(tar_portal);
	            			model.completedRow();
	                		
	            		}
            		}
            		
                		
            	}
            	
            	
            	
    			
    		}

	
            	
            }
                    
    }

        model.updateReport(report);
        return report;
    }
    String getPorts(String pot)
    {
    	String wwpn =pot;
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
		return wwn;
    }
}