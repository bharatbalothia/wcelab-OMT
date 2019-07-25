package com.ibm.omsMigrationTool.order;

import java.time.LocalDate;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.omsMigrationTool.OMTAbstractLister;
import com.ibm.omsMigrationTool.common.OMTConstants;
import com.ibm.omsMigrationTool.common.MigrationConfigReader;

public class OMTOrderLister implements OMTAbstractLister,OMTConstants{
	int listPageSize;
	MigrationConfigReader src_Cfg;
	@Override
	public List<Element> omtgetList(LocalDate retriveRecordsForDate) {
		 List<Element> retrivedOrderElementList = null;
		return retrivedOrderElementList;
	}

	@Override
	public void initilaizeConfg() {
		
		MigrationConfigReader src_Cfg = new MigrationConfigReader();
		
		listPageSize = Integer.getInteger(src_Cfg.getProperty(LIST_PAGE_SIZE));
		// TODO Auto-generated method stub
		
	}
	
}
