package com.cloupia.feature.purestorage.reports;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.accounts.FlashArrayConvergedStackBuilder;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportWithActions;
import com.cloupia.service.cIM.inframgr.reports.simplified.actions.DrillDownAction;

public class ArrayReport extends CloupiaReportWithActions
{

    private static Logger logger = Logger.getLogger(ArrayReport.class);

    public ArrayReport()
    {
        super();
        this.setMgmtColumnIndex(0);
    }

    @Override
    public CloupiaReportAction[] getActions()
    {
    	return null;
    }

    @Override
    public Class<?> getImplementationClass()
    {
        return ArrayReportImpl.class;
    }

    @Override
    public String getReportLabel()
    {
        return "Array Report";
    }

    @Override
    public String getReportName()
    {
        return "com.purestorage.flasharray.array";
    }

    @Override
    public boolean isEasyReport()
    {
        return false;
    }

    @Override
    public boolean isLeafReport()
    {
        return true;
    }

   /* @Override
    public int getMenuID()
    {
        return PureConstants.HOST_REPORT_MENU_LOCATION;
    }*/

    @Override
    public ContextMapRule[] getMapRules()
    {
        DynReportContext context = ReportContextRegistry.getInstance().getContextByName(PureConstants.PURE_ACCOUNT_TYPE);
        ContextMapRule rule = new ContextMapRule();
        logger.info("ContextMapRule: context Id:" + context.getId());
        logger.info("ContextMapRule: ContextType:" + context.getType());
        rule.setContextName(context.getId());
        rule.setContextType(context.getType());

        ContextMapRule[] rules = new ContextMapRule[1];
        rules[0] = rule;

        return rules;
    }
    
    

}