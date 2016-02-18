package com.cloupia.feature.purestorage.reports;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.accounts.FlashArrayConvergedStackBuilder;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportWithActions;

public class ChildHostReport extends CloupiaReportWithActions
{

    private static Logger logger = Logger.getLogger(ChildHostReport.class);

    public ChildHostReport()
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
        return ChildHostReportImpl.class;
    }

    @Override
    public String getReportLabel()
    {
        return "Hosts in HostGroup";
    }

    @Override
    public String getReportName()
    {
        return "com.purestorage.flasharray.childhost";
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

    @Override
    public int getMenuID()
    {
        return PureConstants.HOST_REPORT_MENU_LOCATION;
    }

    @Override
    public ContextMapRule[] getMapRules()
    {
        DynReportContext context = ReportContextRegistry.getInstance().getContextByName("com.purestorage.flasharray.hostgroup");
        ContextMapRule rule = new ContextMapRule();
        logger.info("ContextMapRule: context Id:" + context.getId());
        logger.info("ContextMapRule: ContextType:" + context.getType());
        rule.setContextName(context.getId());
        rule.setContextType(context.getType());

        ContextMapRule[] rules = new ContextMapRule[1];
        rules[0] = rule;

        return rules;
    }
    
    @Override
	public boolean showInSummary()
	{
		return true;
	}

}