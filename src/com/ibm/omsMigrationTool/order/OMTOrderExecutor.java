package com.ibm.omsMigrationTool.order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.omsMigrationTool.OMTAbstractLister;
import com.ibm.omsMigrationTool.common.MigrationConfigReader;
import com.ibm.omsMigrationTool.common.OMTConstants;

public class OMTOrderExecutor implements OMTConstants{
	int maxQueueDepthSize = 100;
	LocalDate importOrderStartDate,importOrderEndDate;
	MigrationConfigReader src_Cfg;	
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	LinkedBlockingQueue inMemoryQueue = null;
	static Logger logger = Logger.getLogger(OMTOrderExecutor.class);
	static String log4jConfigFile = System.getProperty("user.dir")
			+"/config/log4j.properties";

	public void omtGetListToQueue () throws Exception
	{
		Document listInputDocument=null;
		Document listOutputTemplateDoc=null;
		MigrationConfigReader mig_cfg_reader = new MigrationConfigReader();

		maxQueueDepthSize = Integer.getInteger(mig_cfg_reader.getProperty(MAX_QUEUE_DEPTH));


		Class instanceOfOrderList = Class.forName(mig_cfg_reader.getProperty(ORDER_LISTER_CLASS));
		
		OMTAbstractLister orderListerInstance = 
				(OMTAbstractLister) instanceOfOrderList.newInstance();
		orderListerInstance.initilaizeConfg();

		/*
		 * Get the Import start Date and End Date for Order from Source Configuration.
		 */

		importOrderStartDate = LocalDate.parse(mig_cfg_reader.getProperty(IMPORT_START_DATE_FOR_ORDERS),dateFormatter);
		importOrderEndDate = LocalDate.parse(mig_cfg_reader.getProperty(IMPORT_END_DATE_FOR_ORDERS),dateFormatter);


		/*
		 * Iterate though start and end date to get the list of orders.
		 */


		for (LocalDate date = importOrderStartDate; date.isBefore(importOrderEndDate.plusDays(1)); date = date.plusDays(1)) {		    
			System.out.println(date);
			List<Element> retrivedOrderElementList = (List<Element>) orderListerInstance.omtgetList(date);
			for(Element orderElem:retrivedOrderElementList)
			{
				inMemoryQueue.put(orderElem.toString());   	
			}
		}

	}

	public void omtPublishEntity () 
	{
		String currentOrder =  inMemoryQueue.poll().toString();
		if(currentOrder != null)
		{
			OMTOrderWriteToDB omtWrite = new OMTOrderWriteToDB();
			try {
				logger.info("write to DB");
				boolean writeSuccess = omtWrite.writeToDB(currentOrder);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public static void main(String[] args)
	{
		PropertyConfigurator.configure(log4jConfigFile);
		logger.info("my first log 4:");

	}

}
