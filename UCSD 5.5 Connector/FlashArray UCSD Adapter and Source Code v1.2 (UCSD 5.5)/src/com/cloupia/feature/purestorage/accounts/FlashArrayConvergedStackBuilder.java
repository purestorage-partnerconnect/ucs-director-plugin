package com.cloupia.feature.purestorage.accounts;

import com.purestorage.rest.*;
import com.purestorage.rest.array.PureArrayController;
import com.purestorage.rest.array.PureArraySpaceMetrics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.model.cIM.ConvergedStackComponentDetail;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.service.cIM.inframgr.reports.contextresolve.ConvergedStackComponentBuilderIf;
import com.cloupia.feature.purestorage.PureUtils;

public class FlashArrayConvergedStackBuilder implements ConvergedStackComponentBuilderIf{

    private static Logger logger = Logger.getLogger(FlashArrayConvergedStackBuilder.class);

    @Override
    public ConvergedStackComponentDetail buildConvergedStackComponent(String arg0)
    {
        String PhysicalInfraAccountName = "Invalid";
        String[] parts = arg0.split(";");
        int noOfVolume=0,noOfSnapshot=0,noOfHost=0,noOfHostGroup=0;
        String version="",ipAddress="",pod="";
        float dataReduction = 0;
        if (parts.length != 2)
        {
            logger.error("Expected argument to be <PODNAME;AccountName>. arg0 = " + arg0);
        }
        else
        {
            PhysicalInfraAccountName = parts[0];
        }
        ConvergedStackComponentDetail detail = new ConvergedStackComponentDetail();
        FlashArrayAccount acc = null;
        try
        {
            acc = FlashArrayAccount.getFlashArrayCredential(PhysicalInfraAccountName);
        }
        catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Unable to find FlashArrayAccount " + PhysicalInfraAccountName + " Exception:" + e.toString() + " StackTrace:" + sw.toString());
        }
        try
        {
            if (acc != null)
            {
                PureRestClient CLIENT = PureUtils.ConstructPureRestClient(acc);
                
                List<PureArrayController> controllers = CLIENT.array().getControllers();
                PureArraySpaceMetrics spaceMetrics = CLIENT.array().getSpaceMetrics();
                version = CLIENT.array().getAttributes().getVersion();
                ipAddress =acc.getManagementAddress();
                noOfVolume =  CLIENT.volumes().list().size();
                noOfSnapshot = CLIENT.volumes().getSnapshots().size();
                noOfHost = CLIENT.hosts().list().size();
                noOfHostGroup =  CLIENT.hostGroups().list().size();
                dataReduction = spaceMetrics.getDataReduction();
                pod = acc.getPod();
                StringBuilder model = new StringBuilder();
                // For multiple controllers, show Model as:"FA-250|FA-250"
                String delim = "";
                for (PureArrayController controller: controllers)
                {
                    model.append(delim).append(controller.getModel());
                    delim = "|";
                }

                detail.setOsVersion(version);
                detail.setModel(model.toString());
                detail.setMgmtIPAddr(acc.getManagementAddress());
            }
        }
        catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("exception connecting to Pure FlashArray. Exception:" + e.toString() + " StackTrace:" + sw.toString());
        }
        detail.setLabel("Pure Storage FlashArray");
        detail.setVendorLogoUrl("/app/uploads/openauto/pure5.png");
        detail.setIconUrl("/app/uploads/openauto/pure3.png");
        detail.setStatus("OK");
        detail.setVendorName("Pure Storage");
        detail.setLayerType(3); // TODO: What is the magic number 3???
        //detail.setComponentSummaryList(getSummaryReports(pod,noOfVolume,noOfSnapshot,noOfHost,noOfHostGroup));
        
        //detail.setContextType(5026);
        DynReportContext pure_context = ReportContextRegistry.getInstance().getContextByName(PureConstants.PURE_ACCOUNT_TYPE);
        detail.setContextType(pure_context.getType());
        logger.info("AccountName:" + PhysicalInfraAccountName);
        detail.setContextValue(PhysicalInfraAccountName);
        detail.setId(PhysicalInfraAccountName);
        List<String> comSummary = getSummaryReports(pod,noOfVolume,noOfSnapshot,noOfHost,noOfHostGroup,dataReduction);
       //detail.setComponentSummaryList(comSummary.toArray(new String[comSummary.size()]));
        detail.setComponentSummaryList(comSummary);
        return detail;
    }

    private List<String> getSummaryReports(String pod,int noOfVolume,int noOfSnapshot,int noOfHost,int noOfHostGroup,float dataReduction)
    {
        List<String> rpSummaryList = new ArrayList<String>();
        rpSummaryList.add("POD Name ,"+ pod);
        rpSummaryList.add("No. of Volumes ,"+ noOfVolume);
        rpSummaryList.add("No. Of Snapshots ,"+ noOfSnapshot);
        rpSummaryList.add("No. of Hosts ,"+ noOfHost);
        rpSummaryList.add("No. of HostGroups ,"+ noOfHostGroup);
        rpSummaryList.add("Data Reduction ,"+ dataReduction);



        return rpSummaryList;
    }
}
