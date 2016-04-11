package com.cloupia.feature.purestorage.lovs;


import com.cloupia.service.cIM.inframgr.forms.wizard.LOVProviderIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.model.cIM.FormLOVPair;


public class TimeUnitProvider implements LOVProviderIf
{

    public static final String NAME = "time_unit_lov_provider";

    @Override
    public FormLOVPair[] getLOVs(WizardSession session)
    {
        FormLOVPair[] pairs = new FormLOVPair[3];
        pairs[0] = new FormLOVPair("Minute", "m");
        pairs[1] = new FormLOVPair("Hour", "h");
        pairs[2] = new FormLOVPair("Day", "d");
        return pairs;
    }

}