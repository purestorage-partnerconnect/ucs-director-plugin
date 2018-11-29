## Pure Storage FlashArray adapter for Cisco UCS Director


### Overview
The FlashArray UCS Director adapter helps automate FlashStack deployments.  FlashStack is a converged infrastructure reference architecture from Pure Storage that includes Cisco Unified Compute System (UCS), Cisco Nexus and MDS Fibre Channel and ethernet switches.

http://flashstack.com

### Contents
The UCS Director repository includes folders containing adapter source code, workflows and compiled adapter zip file.

### Supported Storage Version
* Any FlashArray Hardware running:
* Purity 4.x
* Purity 5.x

##### Capacity and inventory report views
* Inventory Report
* Storage capacity Report
* Volume Report
* Host Report
* Host Group Report
* Protection Group Report
* Snapshot Report
* Network Report
* Host Connections Report
* FC Target Report
* IQN Target Report
* Pod Report
* Volume Group Report
* System Repot
* Space Report
* CloudSense Report

##### Five end to end workflows for ESXi deployments that include use of BMA (bare metal agent) PXE boot server.
* Advanced Fibre Channel end to end workflow that includes setup of Unified Compute System(UCS) template
* Advanced iSCSI end to end workflow that includes setup of UCS template
* Basic Fibre Channel end to end workflow that requires UCS template already setup on UCS Fabric Interconnect
* Basic iSCSI end to end workflow that requires UCS template already setup on UCS Fabric Interconnect
* VM provisioning specific to Pure Storage FlashArray

##### Additional Workflows
* Add FlashArray Protection Group
* Copy Snapshot from Volume
* Create FlashArray Host
* Create FlashArray Host Group
* Create FlashArray Host Group with Hosts
* Create FlashArray Host and Add initiators
* Create Volume
* Destroy FlashArray Protection Group
* Destroy FlashArray Snapshot
* Destroy Volume
* Eradicate Volume
* Remove FlashArray Host
* Remove FlashArray Host Group
* Resize FlashArray Volume
* Resize and expand Pure Storage Datastore

##### List of Tasks (all tasks include rollback tasks)
* Create Volume
* Destroy Volume(s)-suffix
* Clone Volume 
* Restore Volume 
* Create Volume Group
* Delete Volume Group
* Move Volume Into Volume Group
* Move Volume Out from Volume Group
* Create Host
* Delete Host
* Connect Volume(s) to Host
* Disconnect Volume(s) with Host
* Connect WWN(s)/IQN(s) to Host Create Host Group
* Delete Host Group
* Connect Host to Host Group
* Connect Volume(s) to Host Group
* Disconnect Volume(s) with Host Group
* Disconnect Hosts(s) with Host Group
* Add Volume to ProtectionGroup
* Add Host to ProtectionGroup
* Add Target to ProtectionGroup
* Create ProtectionGroup
* Create ProtectionGroup with HostGroup
* Delete ProtectionGroup
* Remove Target from ProtectionGroup
* Remove Volume From ProtectionGroup
* Clone Snapshot
* Delete Schedule Snapshot
* Delete Snapshot
* Schedule Volume Snapshot
* Create Vlan
* Delete Vlan
* Create Pod
* Delete Pod
* Move Volume into Pod
* Move Volume Out from Pod
* Add Array to Pod
* Connect Array

### Support
The Pure Storage plugin for UCS Dirctor is supported by Pure Storage through its normal support channels.
