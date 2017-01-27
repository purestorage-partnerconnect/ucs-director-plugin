package com.cloupia.feature.purestorage.lovs;


import com.cloupia.service.cIM.inframgr.forms.wizard.LOVProviderIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.model.cIM.FormLOVPair;


public class TimeClockProvider implements LOVProviderIf
{

    public static final String NAME = "time_clock_lov_provider";

    @Override
    public FormLOVPair[] getLOVs(WizardSession session)
    {
        FormLOVPair[] pairs = new FormLOVPair[24];

        for(int i = 0; i < 24; i++)
        {
            String lable = Integer.toString(i) + ":00";
            String value = Integer.toString(i);
            pairs[i] = new FormLOVPair(lable, value);
        }
        return pairs;
    }

}