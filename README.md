## Pure Storage FlashArray adapter for Cisco UCS Director


### Overview
The FlashArray UCSD adapter helps automate FlashStack deployments.  The FlashStack is a converged infrastructure reference architecture from Pure Storage that includes Cisco Unified Compute System, Cisco Nexus and MDS Fibre Channel and iSCSI switches.  The FlashStack reference architecture validates database and server virtualization workloads as well as document configurations that help optimize performance and resiliency.

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
The UCS Director 5.4 FlashArray adapter supports rest api 1.4 that is part of Purity 4.1, 4.5 and 4.6+ releases.  The rest api 1.4 backward compatibility is ensured across all of Purity releases starting with 4.1.

### Support
The FlashArray adapter is not supported by Pure Storage.  Cisco technical assistance center provides support for UCS Director as part of the contract included in the software license purchase.  Cisco does not provide support for 3rd party vendor adapters.  The support for the FlashArray adapter is provided by OneCloud Inc. as paid service or through community.purestorage.com at no charge on best effort basis.

