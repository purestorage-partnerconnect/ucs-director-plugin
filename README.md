## Pure Storage FlashArray adapter for Cisco UCS Director


### Overview
The FlashArray UCS Director adapter helps automate FlashStack deployments.  The FlashStack is a converged infrastructure reference architecture from Pure Storage that includes Cisco Unified Compute System, Cisco Nexus and MDS Fibre Channel and iSCSI switches.  The FlashStack reference architecture validates database and server virtualization workloads as well as document configurations that help optimize performance and resiliency.

### Contents
The UCS Director repository includes folders containing adapter source code, five end to end workflows and compiled adapter zip file.


### Features

##### Capacity and inventory report views
* Volumes, Hosts, Hostgroups

##### Cloudsense capacity report

##### Five end to end workflows for ESXi deployments that include use of BMA (bare metal agent) PXE boot server.
* Advanced Fibre Channel end to end workflow that includes setup of Unified Compute System(UCS) template
* Advanced iSCSI end to end workflow that includes setup of UCS template
* Basic Fibre Channel end to end workflow that requires UCS template already setup on UCS Fabric Interconnect
* Basic iSCSI end to end workflow that requires UCS template already setup on UCS Fabric Interconnect
* VM provisioning specific to Pure Storage FlashArray

##### List of Tasks (all tasks include rollback tasks)
* Connect Volume to Host(s)
* Connect Volume(s) to Hostgroup
* Connect wwn(s)/iqn(s) to Host
* Create Host
* Create Hostgroup(s) * suffix range
* Create Volume
* Delete Host
* Delete Hostgroup(s) * suffix range
* Destroy Volume(s) * suffix range
* Disable Scheduled Volume Snapshot
* Disconnect Volumes with Host
* Disconnect Volumes with Host Group
* Resize Volume
* Restore Volume from Snapshot
* Rollback Resize Volume Task
* Schedule Volume Snapshot

### Compatibility
The Pure Storage UCS Director adapter supports REST API 1.4 and later versions that is part of Purity 4.1, 4.5 and 4.6+ releases.  The rest api 1.4+ backward compatibility is ensured across all of Purity releases starting with 4.1.

### Support
The Pure Storage plugin for UCS Dirctor is supported by Pure Storage through its normal support channels.
