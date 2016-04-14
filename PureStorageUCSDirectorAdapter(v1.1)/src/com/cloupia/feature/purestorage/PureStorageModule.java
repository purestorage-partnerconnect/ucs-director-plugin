package com.cloupia.feature.purestorage;


import org.apache.log4j.Logger;



import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.cloupia.feature.purestorage.lovs.FlashArrayAccountsNameProvider;
import com.cloupia.feature.purestorage.lovs.HostGroupTabularProvider;
import com.cloupia.feature.purestorage.lovs.HostTabularProvider;
import com.cloupia.feature.purestorage.lovs.NetworkPortListTabularProvider;
import com.cloupia.feature.purestorage.lovs.PortIQNTabularProvider;
import com.cloupia.feature.purestorage.accounts.FlashArrayConvergedStackBuilder;
import com.cloupia.feature.purestorage.accounts.FlashArrayInventoryItemHandler;
import com.cloupia.feature.purestorage.accounts.FlashArrayInventoryListener;
import com.cloupia.feature.purestorage.accounts.FlashArrayTestConnectionHandler;
import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.reports.ArrayReport;
import com.cloupia.feature.purestorage.reports.ChildHostReport;
import com.cloupia.feature.purestorage.reports.FcPortsReport;
import com.cloupia.feature.purestorage.reports.HostConnectionReport;
import com.cloupia.feature.purestorage.reports.HostGroupReport;
import com.cloupia.feature.purestorage.reports.HostReport;
import com.cloupia.feature.purestorage.reports.IqnPortsReport;
import com.cloupia.feature.purestorage.reports.SpaceReport;
import com.cloupia.feature.purestorage.reports.VolumeReport;
import com.cloupia.feature.purestorage.summary.PureSummaryReport;
import com.cloupia.feature.purestorage.tasks.ConnectWWWNToHostTask;
import com.cloupia.feature.purestorage.tasks.NewVolumeTask;
import com.cloupia.feature.purestorage.tasks.NewHostTask;
import com.cloupia.feature.purestorage.tasks.ConnectHostToHGTask;
import com.cloupia.feature.purestorage.tasks.ConnectHostVolumeTask;
import com.cloupia.feature.purestorage.tasks.ResizeVolumeTask;
import com.cloupia.feature.purestorage.tasks.NewHostGroupTask;
import com.cloupia.feature.purestorage.tasks.ConnectVolumeToHGTask;
import com.cloupia.feature.purestorage.tasks.DeleteHostTask;
import com.cloupia.feature.purestorage.tasks.DeleteHGSuffixRangeTask;
import com.cloupia.feature.purestorage.tasks.DestroyVolumesTask;
import com.cloupia.feature.purestorage.tasks.ScheduleVolumeSnapshotTask;
import com.cloupia.feature.purestorage.tasks.RestoreVolumeTask;
import com.cloupia.feature.purestorage.tasks.DeleteScheduleSnapshotTask;
import com.cloupia.feature.purestorage.tasks.DisconnectVolumeHostTask;
import com.cloupia.feature.purestorage.tasks.DisconnectVolumeHGTask;
import com.cloupia.feature.purestorage.tasks.RollbackResizeVolumeTask;
import com.cloupia.feature.purestorage.lovs.PortTabularProvider;
import com.cloupia.feature.purestorage.lovs.TimeUnitProvider;
import com.cloupia.feature.purestorage.lovs.TimeClockProvider;
import com.cloupia.feature.purestorage.lovs.VolumeSizeUnitProvider;
import com.cloupia.feature.purestorage.lovs.VolumeTabularProvider;
import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.PhysicalAccountTypeManager;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.model.cIM.InfraAccountTypes;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.AbstractCloupiaModule;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.CustomFeatureRegistry;
import com.cloupia.service.cIM.inframgr.collector.controller.CollectorFactory;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputTypeRegistry;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;

public class PureStorageModule extends AbstractCloupiaModule
{
    private static Logger logger = Logger.getLogger(PureStorageModule.class);

    @Override
    public AbstractTask[] getTasks()
    {

        AbstractTask[] tasks = new AbstractTask[17];

        tasks[0] = new DestroyVolumesTask();
        tasks[1] = new NewVolumeTask();
        tasks[2] = new NewHostTask();
        tasks[3] = new ConnectHostVolumeTask();
        tasks[4] = new ResizeVolumeTask();
        tasks[5] = new NewHostGroupTask();
        tasks[6] = new ConnectVolumeToHGTask();
        tasks[7] = new DeleteHostTask();
        tasks[8] = new DeleteHGSuffixRangeTask();
        tasks[9] = new RestoreVolumeTask();
        tasks[10] = new ScheduleVolumeSnapshotTask();
        tasks[11] = new DeleteScheduleSnapshotTask();
        tasks[12] = new DisconnectVolumeHostTask();
        tasks[13] = new DisconnectVolumeHGTask();
        tasks[14] = new RollbackResizeVolumeTask();
        tasks[15] = new ConnectWWWNToHostTask();
        tasks[16] = new ConnectHostToHGTask();

        return tasks;
    }

    @Override
    public CollectorFactory[] getCollectors()
    {
        return new CollectorFactory[0];
    }

    @Override
    public CloupiaReport[] getReports()
    {
        CloupiaReport[] reports = new CloupiaReport[10];
        reports[0] = new PureSummaryReport();
        reports[1] = new VolumeReport();
        reports[2] = new HostReport();
        reports[3] = new SpaceReport();
        reports[4] = new HostGroupReport();
        reports[5] = new ChildHostReport();
        reports[6] = new ArrayReport();
        reports[7] = new HostConnectionReport();
        reports[8] = new FcPortsReport();
        reports[9] = new IqnPortsReport();
        
        return reports;
    }

    @Override
    public void onStart(CustomFeatureRegistry cfr)
    {
        try
        {
            ReportContextRegistry.getInstance().register(PureConstants.PURE_ACCOUNT_TYPE, PureConstants.PURE_ACCOUNT_LABEL);
            ReportContextRegistry.getInstance().register("com.purestorage.flasharray.hostgroup", "HostGroup Report");
            
            //ReportContextRegistry.getInstance().register("FlashArray", "FlashArray");
            cfr.registerLovProviders(FlashArrayAccountsNameProvider.NAME, new FlashArrayAccountsNameProvider());
            cfr.registerLovProviders(TimeUnitProvider.NAME, new TimeUnitProvider());
            cfr.registerLovProviders(TimeClockProvider.NAME, new TimeClockProvider());
            cfr.registerLovProviders(VolumeSizeUnitProvider.NAME, new VolumeSizeUnitProvider());
            cfr.registerTabularField(PortTabularProvider.TABULAR_PROVIDER, PortTabularProvider.class, "1", "1");
            cfr.registerTabularField(PortIQNTabularProvider.TABULAR_PROVIDER, PortIQNTabularProvider.class, "1", "1");
            cfr.registerTabularField(NetworkPortListTabularProvider.TABULAR_PROVIDER, NetworkPortListTabularProvider.class, "2", "2");
            cfr.registerTabularField(VolumeTabularProvider.TABULAR_PROVIDER, VolumeTabularProvider.class, "1", "1");
            cfr.registerTabularField(HostTabularProvider.TABULAR_PROVIDER, HostTabularProvider.class, "1", "1");
            cfr.registerTabularField(HostGroupTabularProvider.TABULAR_PROVIDER, HostGroupTabularProvider.class, "1", "1");
            
            
            
            String fieldName = PortTabularProvider.TABULAR_PROVIDER;//your tabel name
			WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
			sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
		                "pureFlashArrayTargetPortMultiWwpn(s)", "Pure Storage FlashArray Target WWPN(s)",
		                FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,fieldName, true));
			
			cfr.registerWorkflowInputFieldType(PureConstants.PURE_VOLUME_LIST_TABLE_NAME, PureConstants.PURE_VOLUME_LIST_TABLE_LABLE,
            		FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, VolumeTabularProvider.TABULAR_PROVIDER);
			cfr.registerWorkflowInputFieldType(PureConstants.PURE_HOST_LIST_TABLE_NAME, PureConstants.PURE_HOST_LIST_TABLE_LABLE,
            		FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, HostTabularProvider.TABULAR_PROVIDER);
			cfr.registerWorkflowInputFieldType(PureConstants.PURE_HOSTGROUP_NAME, PureConstants.PURE_HOSTGROUP_LIST_TABLE_LABLE,
            		FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, HostGroupTabularProvider.TABULAR_PROVIDER);
			
			String tabVol = VolumeTabularProvider.TABULAR_PROVIDER;//your tabel name
			WorkflowInputTypeRegistry volMulInputType = WorkflowInputTypeRegistry.getInstance();
			volMulInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
					PureConstants.PURE_VOLUME_LIST_TABLE_NAMES, PureConstants.PURE_VOLUME_LIST_TABLE_LABLES,
		                FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,tabVol, true));
			
			String tabHost = HostTabularProvider.TABULAR_PROVIDER;//your tabel name
			WorkflowInputTypeRegistry hostMulInputType = WorkflowInputTypeRegistry.getInstance();
			hostMulInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
					PureConstants.PURE_HOST_LIST_TABLE_NAMES, PureConstants.PURE_HOST_LIST_TABLE_LABLES,
		                FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,tabHost, true));
			
            cfr.registerWorkflowInputFieldType(PureConstants.PURE_PORT_TABLE_NAME, PureConstants.PURE_PORT_TABLE_LABLE,
            		FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, PortTabularProvider.TABULAR_PROVIDER);
            cfr.registerWorkflowInputFieldType(PureConstants.PURE_PORT_TABLE_IQN_NAME, PureConstants.PURE_PORT_TABLE_IQN_LABLE,
            		FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, PortIQNTabularProvider.TABULAR_PROVIDER);
            cfr.registerWorkflowInputFieldType(PureConstants.PURE_NETWORK_PORT_LIST_TABLE_NAME, PureConstants.PURE_NETWORK_PORT_LIST_TABLE_LABLE,
            		FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, NetworkPortListTabularProvider.TABULAR_PROVIDER);
           
            cfr.registerWorkflowInputFieldType(PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_NAME, PureConstants.PURE_FLASHARRAY_ACCOUNT_LOV_LABLE,
                    6, FlashArrayAccountsNameProvider.NAME);
            cfr.registerWorkflowInputFieldType(PureConstants.PURE_TIME_UNIT_LOV_NAME, PureConstants.PURE_TIME_UNIT_LOV_LABLE,
                    6, TimeUnitProvider.NAME);
            cfr.registerWorkflowInputFieldType(PureConstants.PURE_TIME_CLOCK_LOV_NAME, PureConstants.PURE_TIME_CLOCK_LOV_LABLE,
                    6, TimeClockProvider.NAME);
            cfr.registerWorkflowInputFieldType(PureConstants.PURE_VOLUME_SIZE_UNIT_LOV_NAME, PureConstants.PURE_VOLUME_SIZE_UNIT_LOV_LABLE,
                    6, VolumeSizeUnitProvider.NAME);
            createFlashArrayAccountType();
        }
        catch (Exception e)
        {
            logger.error("Could not register FlashArray account.", e);
        }
    }

    private void createFlashArrayAccountType() throws Exception
    {
        logger.info("Creating FlashArray account type.");

        AccountTypeEntry entry = new AccountTypeEntry();
        entry.setCredentialClass(FlashArrayAccount.class);
        entry.setAccountType("FlashArray");
        entry.setAccountLabel("FlashArray");
        entry.setVendor("Pure Storage");
        entry.setCategory(InfraAccountTypes.CAT_STORAGE);
        entry.setContextType(ReportContextRegistry.getInstance().getContextByName(PureConstants.PURE_ACCOUNT_TYPE).getType());
        entry.setAccountClass(AccountTypeEntry.PHYSICAL_ACCOUNT);
        entry.setInventoryTaskPrefix("FlashArray Task");
        entry.setWorkflowTaskCategory("FlashArray Tasks");
        entry.setInventoryFrequencyInMins(15);
        entry.setPodTypes(new String[]{"GenericPod","FlashStack"});
        entry.setIconPath("/app/uploads/openauto/pure4.png");

        entry.setTestConnectionHandler(new FlashArrayTestConnectionHandler());
        entry.setInventoryListener(new FlashArrayInventoryListener());
        entry.setConvergedStackComponentBuilder(new FlashArrayConvergedStackBuilder());

        entry.createInventoryRoot("flasharray.inventory.root", FlashArrayInventoryItemHandler.class);
        PhysicalAccountTypeManager.getInstance().addNewAccountType(entry);
    }
}
