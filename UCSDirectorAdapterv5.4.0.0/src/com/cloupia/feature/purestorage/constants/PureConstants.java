package com.cloupia.feature.purestorage.constants;

public class PureConstants
{
    public static final String PURE_STORAGE_INC = "Pure Storage Inc.";

    public static final String PURE_ADAPTER_NAME = "UCSDirectorPlugin";
    public static final String PURE_ADAPTER_VERSION = "1.0";
    //public static final int PURE_ACCOUNT_TYPE = 99000;
    public static final String PURE_ACCOUNT_TYPE = "FlashArray";
    public static final String PURE_ACCOUNT_LABEL = "FlashArray";
    public static final String PURE_FLASHARRAY_ACCOUNT_LOV_NAME = "flashArray_account";
    public static final String PURE_FLASHARRAY_ACCOUNT_LOV_LABLE = "FlashArray Account";
    public static final String PURE_TIME_UNIT_LOV_NAME = "pure_time_unit";
    public static final String PURE_TIME_UNIT_LOV_LABLE = "Pure FlashArray Time Unit";
    public static final String PURE_TIME_CLOCK_LOV_NAME = "pure_time_clock";
    public static final String PURE_TIME_CLOCK_LOV_LABLE = "Pure FlashArray Time Clock";
    public static final String PURE_VOLUME_SIZE_UNIT_LOV_NAME = "pure_volume_size_unit";
    public static final String PURE_VOLUME_SIZE_UNIT_LOV_LABLE = "Pure Volume Size Unit";
    public static final String PURE_PORT_TABLE_NAME = "pure_flasharray_port_wwns";
    public static final String PURE_PORT_TABLE_LABLE = "Pure FlashArray Target WWN Ports";
    public static final String PURE_PORT_TABLE_IQN_NAME = "pure_flasharray_port_iqns";
    public static final String PURE_PORT_TABLE_IQN_LABLE = "Pure FlashArray Target IQN Ports";
    public static final String PURE_NETWORK_PORT_LIST_TABLE_NAME = "pure_flasharray_network_port_list";
    public static final String PURE_NETWORK_PORT_LIST_TABLE_LABLE = "Pure FlashArray Network Port List";


    /////////////////////////Workflow Task related/////////////////////////
    public static final String TASK_OUTPUT_NAME_NEW_VOLUME = "flashArray_task_output_new_volume";
    public static final String TASK_OUTPUT_NAME_NEW_HOST = "flashArray_task_output_new_host";
    public static final String TASK_OUTPUT_NAME_RESIZE_VOLUME = "flashArray_task_output_resize_volume";
    public static final String TASK_OUTPUT_NAME_NEW_HOSTGROUP = "flashArray_task_output_new_hostgroup";
    public static final String TASK_OUTPUT_NAME_DELETE_HOST = "flashArray_task_output_delete_host";
    public static final String TASK_OUTPUT_NAME_RESTORE_VOLUME = "falshArray_task_output_restore_volume";
    //public static final String TASK_OUTPUT_NAME_DISCONNECTED_HOSTGROUP = "flashArray_task_output_disconnected_hostgroup"; TODO:HOW TO HAVE MORE THAN ONE HOSTGROUP AS OUTPUT
    public static final String TASK_OUTPUT_NAME_SCHEDULE_VOLUME_SNAPSHOT = "flashArray_task_output_schedule_volume_snapshot";

    public static final String TASK_NAME_NEW_VOLUME = "Create Volume";
    public static final String TASK_NAME_NEW_HOST = "Create Host";
    public static final String TASK_NAME_RESIZE_VOLUME = "Resize Volume";
    public static final String TASK_NAME_NEW_HOSTGROUP = "Create Hostgroup(s)-suffix range";
    public static final String TASK_NAME_SCHEDULE_VOLUME_SNAPSHOT = "Schedule Volume Snapshot";
    public static final String TASK_NAME_RESTORE_VOLUME_FROM_SNAPSHOT = "Restore Volume From Snapshot";

    public static final String TASK_NAME_CONNECT_HOST_VOLUME = "Connect Volume(s) to Host";
    public static final String TASK_NAME_CONNECT_WWN_To_HOST = "Connect wwn(s)/iqn(s) to Host";
    public static final String TASK_NAME_CONNECT_VOLUME_HOSTGROUP = "Connect Volume(s) to Hostgroup";
    public static final String TASK_NAME_CONNECT_HOST_HOSTGROUP = "Connect Host(s) to Hostgroup";
    public static final String TASK_NAME_DELETE_HOST = "Delete Host";
    public static final String TASK_NAME_DELETE_HG_SUFFIX_RANGE = "Delete Hostgroup(s)-suffix range";
    public static final String TASK_NAME_DELETE_SCHEDULE_SNAPSHOT = "Disable Scheduled Volume Snapshot";
    public static final String TASK_NAME_DESTROY_VOLUMES_SUFFIX_RANGE = "Destroy Volume(s)-suffix range";
    public static final String TASK_NAME_DISCONNECT_VOLUMES_WITH_HOST = "Disconnect Volumes with Host";
    public static final String TASK_NAME_DISCONNECT_VOLUMES_WITH_HOSTGROUP = "Disconnect Volumes with Host Group";
    public static final String TASK_NAME_ROLLBACK_RESIZE_VOLUME_TASK = "Rollback Resize Volume Task";


    // Type names to be used in Workflow Tasks
    public static final String TASK_TYPE_VOLUME = "flashArray_task_type_volume";
    public static final String TASK_TYPE_HOST = "flashArray_task_type_host";
    public static final String TASK_TYPE_HOSTGROUP = "flashArray_task_type_hostgroup";
    public static final String TASK_TYPE_VOLUME_SNAPSHOT = "flashArray_task_type_volume_snapshot";


    // Report constants
    public static final int VOLUME_REPORT_MENU_LOCATION = 51;
    public static final int HOST_REPORT_MENU_LOCATION = 51;
    public static final int SPACE_REPORT_MENU_LOCATION = 51;
    
/////////////////////////Workflow Task Outputs/////////////////////////
public static final String TASK_OUTPUT_NAME_VOLUME_NAME = "flashArray_task_output_volume_name";
public static final String TASK_OUTPUT_NAME_VOLUME_SERIAL = "flashArray_task_output_volume_serial";
public static final String TASK_OUTPUT_NAME_HOST_NAME = "flashArray_task_output_host_name";

}
