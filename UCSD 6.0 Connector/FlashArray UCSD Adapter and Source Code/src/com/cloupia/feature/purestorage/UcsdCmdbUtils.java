package com.cloupia.feature.purestorage;

import org.apache.log4j.Logger;

import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.model.cIM.ChangeRecord;

public class UcsdCmdbUtils {
	
	 static Logger logger = Logger.getLogger(UcsdCmdbUtils.class);

	 public static void updateRecord(String resourceType,String description,int changeType,String user,String resourceName,String additionalInfo )
	 {
		 ChangeRecord rec = new ChangeRecord();
         rec.setTime(System.currentTimeMillis());
         rec.setResourceType(resourceType);
         rec.setDescription(description);
         rec.setChangeType(changeType);
         rec.setChangeByUser(user);
         rec.setChangedResourceName(resourceName);
         rec.setAdditionalInfo(additionalInfo);
         
         ObjStore<ChangeRecord> catStore = ObjStoreHelper.getStore(ChangeRecord.class);
         try {
			catStore.insert(rec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	 }

}
