package com.cloupia.feature.purestorage.tasks;


import com.cloupia.service.cIM.inframgr.TaskConfigIf;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable(detachable = "true", table = "psucs_general_task_config")
public class GeneralTaskConfig implements TaskConfigIf
{

    @Persistent
    private long configEntryId;

    @Persistent
    private long actionId;


    @Override
    public long getActionId(){ return actionId;}

    @Override
    public long getConfigEntryId(){return configEntryId;}

    @Override
    public String getDisplayLabel(){return null;}

    @Override
    public void setActionId(long actionId){ this.actionId = actionId;}

    @Override
    public void setConfigEntryId(long configEntryId){ this.configEntryId = configEntryId;}

}
