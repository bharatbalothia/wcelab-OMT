package com.ibm.omsMigrationTool;

import org.w3c.dom.Document;

public abstract interface OMTAbstractGetDetails {
	public abstract Document omtgetDetails(String headerKey);
	public abstract void initilaizeConfg();
}

