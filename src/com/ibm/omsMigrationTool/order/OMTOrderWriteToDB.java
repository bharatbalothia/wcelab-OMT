package com.ibm.omsMigrationTool.order;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.ibm.omsMigrationTool.OMTAbstractGetDetails;
import com.ibm.omsMigrationTool.common.MigrationConfigReader;
import com.ibm.omsMigrationTool.common.OMTCommonUtils;
import com.ibm.omsMigrationTool.common.OMTConstants;
import com.ibm.omsMigrationTool.derbyDB.derbyDBConnect;



public class OMTOrderWriteToDB implements OMTConstants{
	static Logger log = Logger.getLogger(OMTOrderWriteToDB.class);
	public boolean writeToDB(String queueMessage) throws Exception
	{
		MigrationConfigReader mig_cfg_reader = new MigrationConfigReader();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		OrderSaxParserHandler orh = new OrderSaxParserHandler();
		saxParser.parse(new InputSource(new StringReader(queueMessage)), orh);
		List<Order> orderList = orh.getOrderList();
		String tableInsertValues = "";
		for(Order migrateOrderElement : orderList)
		{   
			System.out.println(migrateOrderElement.getOrderNo());
			System.out.println(migrateOrderElement.getOrderHeaderKey());
			Connection conn = derbyDBConnect.createConnection();
			String orderHeaderKeyWhereCondition = "";
			if(migrateOrderElement != null)
			{
				String migrateOrderHeaderKey = migrateOrderElement.getOrderHeaderKey();
				orderHeaderKeyWhereCondition = DB_ORDER_HEADER_KEY +"'='"+migrateOrderHeaderKey+"'";
				if(!derbyDBConnect.checkforRecordExistence(conn,MG_ORDER_TABLE_NAME, orderHeaderKeyWhereCondition))
				{				
					tableInsertValues = constructValuesforTableInsert(migrateOrderElement);				
					derbyDBConnect.insertRecord(conn, MG_ORDER_TABLE_NAME, tableInsertValues);

				}
				else
				{
					log.info("Order record Already exist:"+migrateOrderHeaderKey);
					ResultSet retrivedOrderRecordSet = derbyDBConnect.getRecordData(conn,MG_ORDER_TABLE_NAME, orderHeaderKeyWhereCondition);
					if(retrivedOrderRecordSet.getDate(DB_IMPORTED_TS)==null)
					{
						boolean isParentImportSuccessfull = checkforParentImport(retrivedOrderRecordSet);
						if(isParentImportSuccessfull)
						{
							//update the DB record TRANSFER_INITIATED_TS column with current time stamp

							derbyDBConnect.updateRecord(conn, MG_ORDER_TABLE_NAME, orderHeaderKeyWhereCondition, DB_TRANSFER_INITIATED_TS,(new Timestamp(System.currentTimeMillis())).toLocaleString());


							Class instanceOfOrderGetDetails = Class.forName(mig_cfg_reader.getProperty(ORDER_DETAILS_GETTER_CLASS));

							OMTAbstractGetDetails orderGetDetailsInst = 
									(OMTAbstractGetDetails) instanceOfOrderGetDetails.newInstance();
							orderGetDetailsInst.initilaizeConfg();

							Document getOrderDetailsOp = orderGetDetailsInst.omtgetDetails(migrateOrderHeaderKey);

							/*
							 * update the DB record SRC_RETRIVAL_COMPLETE_TS column with current time stamp
							*/
							derbyDBConnect.updateRecord(conn, MG_ORDER_TABLE_NAME, orderHeaderKeyWhereCondition, DB_SRC_RETRIVAL_COMPLETE_TS,(new Timestamp(System.currentTimeMillis())).toLocaleString());

							/*
							 * invoke transformation method to transfom getOrderDetails output to importOrder input XML
							*/
							Document importOrderIp = OMTCommonUtils.transformXML(mig_cfg_reader.getProperty(ORDER_TRANSFORMER_XSL),getOrderDetailsOp);

							/*
							 * update the DB record XSL_TRANSFORM_COMPLETE_TS column with current time stamp
							 */

							derbyDBConnect.updateRecord(conn, MG_ORDER_TABLE_NAME, orderHeaderKeyWhereCondition, DB_XSL_TRANSFORM_COMPLETE_TS,(new Timestamp(System.currentTimeMillis())).toLocaleString());

						}
						else
						{
							log.info("Skip current Order Import until partent import complete"+migrateOrderHeaderKey);
							return false;
						}
					}
				}
			}
			derbyDBConnect.closeConnection(conn);
		}
		return true;

	}

	private boolean checkforParentImport(ResultSet retrivedOrderRecordSet) throws SQLException {
		if(retrivedOrderRecordSet.getString(DB_PARENT_ORDER_HEADER_KEY)== null)
		{
			return true;
		}
		else if (retrivedOrderRecordSet.getDate(DB_PARENT_ORDER_IMPORT_TS) == null)
		{
			return false;

		}
		else
		{
			return true;
		}
	}

	public void testLog()
	{
		log.info("from OMTWriter");
	}

	private String constructValuesforTableInsert(Order orderElem) throws Exception {

		String tableInsertValues = "";
		tableInsertValues = tableInsertValues + "'" +orderElem.getOrderHeaderKey() +"'" +',';
		tableInsertValues = tableInsertValues + "'" +orderElem.getOrderNo() +"'" +',';
		tableInsertValues = tableInsertValues + "'" +orderElem.getStatus() +"'";
		// TODO 
		//add condition to check for realtedOrderHeaderKey if not present add default and empty value to rest of the columns
		tableInsertValues = tableInsertValues + ",'',false,false,false,false,false";
		return tableInsertValues;	

	}
	//
	//		public static void main(String args[]) throws Exception
	//		{
	//			
	//			String queueMessage = "<Order OrderHeaderKey='768678' OrderNo='345345' Status='Shipped'/>";
	//			writeToDB(queueMessage);		
	//		}
}
