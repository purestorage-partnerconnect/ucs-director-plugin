package com.cloupia.feature.purestorage.reports;

import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;

public class SpaceReport extends CloupiaNonTabularReport
{

    private static Logger logger = Logger.getLogger(SpaceReport.class);

    public SpaceReport()
    {
        super();
        this.setMgmtColumnIndex(0);
    }

    @Override
    public Class<?> getImplementationClass()
    {
        return SpaceReportImpl.class;
    }

    @Override
    public String getReportLabel()
    {
        return "Space Report";
    }

    @Override
    public String getReportName()
    {
        return "com.purestorage.flasharray.space";
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
    public int getReportType() {
        return ReportDefinition.REPORT_TYPE_SNAPSHOT;
    }

    @Override
    public int getReportHint()
    {
        return ReportDefinition.REPORT_HINT_PIECHART;
    }

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

    @Override
    public int getMenuID()
    {
        return PureConstants.SPACE_REPORT_MENU_LOCATION;
    }
    
    @Override
	public boolean showInSummary()
	{
		return true;
	}


}