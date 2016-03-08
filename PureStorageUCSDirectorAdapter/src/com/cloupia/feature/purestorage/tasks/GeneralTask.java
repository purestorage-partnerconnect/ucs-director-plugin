package com.cloupia.feature.purestorage.tasks;


import org.apache.log4j.Logger;
import com.cloupia.feature.purestorage.PureUtils;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;
import com.purestorage.rest.PureRestClient;


public abstract class GeneralTask extends AbstractTask
{

    static Logger logger = Logger.getLogger(GeneralTask.class);
    private long configEntryId = 0;
    protected FlashArrayAccount flashArrayAccount = null;
    protected PureRestClient CLIENT = null;
    protected String accountName = null;
    private String taskName;
    private String taskConfigClassName;

    public GeneralTask(String taskName, String taskConfigClassName)
    {

        this.taskName = taskName;
        this.taskConfigClassName = taskConfigClassName;

    }

    @Override
    public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger actionLogger) throws Exception
    {

        actionLogger.addInfo("========Entering " + taskName + " .executeCustomAction()");
        configEntryId = context.getConfigEntry().getConfigEntryId();
        actionLogger.addInfo("Accountname   : "+accountName);
        if (accountName == null)
        {
            throw new Exception("No config found for custom action " + context.getActionDef().getName() + " entryId " + configEntryId);
        }

        flashArrayAccount = FlashArrayAccount.getFlashArrayCredential(accountName);
        CLIENT = PureUtils.ConstructPureRestClient(flashArrayAccount);

    }

    @SuppressWarnings("rawtypes")
    @Override
    public TaskConfigIf getTaskConfigImplementation()
    {

        try
        {
            Class taskConfigClass = Class.forName(taskConfigClassName);
            logger.info("get taskconfig "+taskConfigClassName);
            return(TaskConfigIf)taskConfigClass.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            logger.error(e.getMessage());
            return null;
        }
        catch (InstantiationException e)
        {
            logger.error(e.getMessage());
            return null;
        }
        catch (IllegalAccessException e)
        {
            logger.error(e.getMessage());
            return null;
        }

    }

    @Override
    public String getTaskName()
    {
        return taskName;
    }

    @Override
    public TaskOutputDefinition[] getTaskOutputDefinitions()
    {
        return null;
    }

}
