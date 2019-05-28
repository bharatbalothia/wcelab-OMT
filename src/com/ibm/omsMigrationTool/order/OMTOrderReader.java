package com.ibm.omsMigrationTool.order;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.omsMigrationTool.OMTAbstractLister;
import com.ibm.omsMigrationTool.common.OMTConstants;
import com.ibm.omsMigrationTool.common.SourceConfigReader;
import com.ibm.omsMigrationTool.derbyDB.derbyDBConnect;

public class OMTOrderReader implements OMTConstants{
	int maxQueueDepthSize = 100;
	LocalDate importOrderStartDate,importOrderEndDate;
	SourceConfigReader src_Cfg;	
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public void omtGetOrderListToQueue (LinkedBlockingQueue inMemoryQueue ) throws Exception
	{
		Document listInputDocument=null;
		Document listOutputTemplateDoc=null;
		SourceConfigReader src_Cfg = new SourceConfigReader();
		
		maxQueueDepthSize = Integer.getInteger(src_Cfg.getProperty(MAX_QUEUE_DEPTH));
		
		
		Class instanceOfOrderList = Class.forName(src_Cfg.getProperty(ORDER_LISTER_CLASS)); 
		OMTAbstractLister orderListerInstance = 
                (OMTAbstractLister) instanceOfOrderList.newInstance();
		orderListerInstance.initilaizeConfg();
		
		/*
		 * Get the Import start Date and End Date for Order from Source Configuration.
		 */
		
		importOrderStartDate = LocalDate.parse(src_Cfg.getProperty(IMPORT_START_DATE_FOR_ORDERS),dateFormatter);
		importOrderEndDate = LocalDate.parse(src_Cfg.getProperty(IMPORT_END_DATE_FOR_ORDERS),dateFormatter);
		
		
		/*
		 * Iterate though start and end date to get the list of orders.
		 */
		
		
		for (LocalDate date = importOrderStartDate; date.isBefore(importOrderEndDate.plusDays(1)); date = date.plusDays(1)) {		    
		    System.out.println(date);
		    List<Element> retrivedOrderElementList = orderListerInstance.omtgetList(date);
		    for(Element orderElem:retrivedOrderElementList)
		    {
		    	inMemoryQueue.add(orderElem.toString());   	
		    }
		}
		
	}

	
    public static void main(String[] args)
    {

    }

}
