package com.cloupia.feature.purestorage.reports;

import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.ReportNameValuePair;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.SnapshotReportCategory;
import com.cloupia.service.cIM.inframgr.SnapshotReportGeneratorIf;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.array.PureArraySpaceMetrics;
import com.purestorage.rest.protectiongroup.PureVolumeSnapshot;
import com.purestorage.rest.snmp.PureSNMPManager;
import com.purestorage.rest.volume.PureVolume;
import com.purestorage.rest.volume.PureVolumeSpaceMetrics;

public class SpaceReportImpl implements SnapshotReportGeneratorIf
{
    static Logger logger = Logger.getLogger(SpaceReportImpl.class);

    @Override
    public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception
    {
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
        SnapshotReport report = new SnapshotReport();
        report.setContext(context);
        report.setReportName(reportEntry.getReportLabel());
        report.setNumericalData(true);
        report.setDisplayAsPie(true); //THIS IS IMPORTANT!!
        report.setPrecision(0);
        

        if (accountName != null && accountName.length() > 0)
        {
            FlashArrayAccount acc = FlashArrayAccount.getFlashArrayCredential(accountName);
            PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
            PureArraySpaceMetrics spaceMetrics = CLIENT.array().getSpaceMetrics();
            long provisionedCapacity = (long) 0.0;
            int noOfVolume=0,noOfSnapshot=0;
            List<PureVolume> volumes =  CLIENT.volumes().list();
            for (PureVolume volume: volumes)
            {
            	provisionedCapacity +=volume.getSize();
            	
            	           	          	
            	noOfVolume++;
            }
            
            List<PureVolumeSnapshot> snapshots =  CLIENT.volumes().getSnapshots();
            for (PureVolumeSnapshot snapshot: snapshots)
            {
            	
            	            	           	          	
            	noOfSnapshot++;
            }
            
            
            ReportNameValuePair[] rnv = new ReportNameValuePair[5];// Available, System, Shared Space, Volumes, Snapshots

            
           double freeSpace = spaceMetrics.getCapacity() - spaceMetrics.getTotal();
           double sharedSpace = spaceMetrics.getSharedSpace()/(1024*1024*1024);
           double systemSpace = spaceMetrics.getSystem()/(1024*1024*1024);
           double volumeSpace = spaceMetrics.getVolumes()/(1024*1024*1024);
           double snapshotSpace = spaceMetrics.getSnapshots()/(1024*1024*1024);
           
           
           
           
            rnv[0] = new ReportNameValuePair("Free Space(GB)", freeSpace/(1024*1024*1024));
            rnv[1] = new ReportNameValuePair("Shared Space(GB)", sharedSpace);
            rnv[2] = new ReportNameValuePair("System(GB)", systemSpace);
            rnv[3] = new ReportNameValuePair("Volumes Capacity(GB)",volumeSpace );
            rnv[4] = new ReportNameValuePair("Snapshots Capacity(GB)", snapshotSpace);
          
            SnapshotReportCategory cat = new SnapshotReportCategory();
            cat.setCategoryName("Space Usage");
            cat.setNameValuePairs(rnv);
            
            
            report.setCategories(new SnapshotReportCategory[] { cat });
        }

        return report;
    }
}