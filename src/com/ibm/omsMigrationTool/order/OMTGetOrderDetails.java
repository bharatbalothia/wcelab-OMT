package com.ibm.omsMigrationTool.order;

import org.w3c.dom.Document;

import com.ibm.omsMigrationTool.OMTAbstractGetDetails;
import com.ibm.omsMigrationTool.common.MigrationConfigReader;
import com.ibm.omsMigrationTool.common.OMTConstants;

public class OMTGetOrderDetails implements OMTAbstractGetDetails,OMTConstants{


	public Document omtgetDetails(String orderHeaderKey) {
		 Document outputDocument = null;
		return outputDocument;
	}

	public void initilaizeConfg() {
		
		MigrationConfigReader src_Cfg = new MigrationConfigReader();
		
		// TODO initialize the template file location etc;
		
	}
	
}
