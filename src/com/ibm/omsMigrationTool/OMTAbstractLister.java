package com.ibm.omsMigrationTool;

import java.time.LocalDate;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract interface OMTAbstractLister {
	
	public abstract List<Element> omtgetList(LocalDate retriveRecordsForDate);
	public abstract void initilaizeConfg();
}
