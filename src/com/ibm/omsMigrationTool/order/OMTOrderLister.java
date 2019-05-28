package com.ibm.omsMigrationTool.order;

import java.time.LocalDate;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.omsMigrationTool.OMTAbstractLister;
import com.ibm.omsMigrationTool.common.OMTConstants;
import com.ibm.omsMigrationTool.common.SourceConfigReader;

public class OMTOrderLister implements OMTAbstractLister,OMTConstants{
	int listPageSize;
	SourceConfigReader src_Cfg;
	@Override
	public List<Element> omtgetList(LocalDate retriveRecordsForDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initilaizeConfg() {
		
		SourceConfigReader src_Cfg = new SourceConfigReader();
		
		listPageSize = Integer.getInteger(src_Cfg.getProperty(LIST_PAGE_SIZE));
		// TODO Auto-generated method stub
		
	}
	
}
