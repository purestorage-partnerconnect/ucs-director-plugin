package com.cloupia.feature.purestorage.reports;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportWithActions;

public class VolumeReport extends CloupiaReportWithActions
{

    private static Logger logger = Logger.getLogger(VolumeReport.class);

    public VolumeReport()
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
        return VolumeReportImpl.class;
    }

    @Override
    public String getReportLabel()
    {
        return "Volumes";
    }

    @Override
    public String getReportName()
    {
        return "com.purestorage.flasharray.volume";
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
        return PureConstants.VOLUME_REPORT_MENU_LOCATION;
    }

    @Override
    public ContextMapRule[] getMapRules()
    {
        DynReportContext context = ReportContextRegistry.getInstance().getContextByName(PureConstants.PURE_ACCOUNT_TYPE);
        logger.info("ContextMapRule: context Id:" + context.getId());
        logger.info("ContextMapRule: ContextType:" + context.getType());
        ContextMapRule rule = new ContextMapRule();
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