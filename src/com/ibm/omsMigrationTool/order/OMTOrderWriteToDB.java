package com.ibm.omsMigrationTool.order;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.ibm.omsMigrationTool.common.OMTConstants;
import com.ibm.omsMigrationTool.derbyDB.derbyDBConnect;



public class OMTOrderWriteToDB implements OMTConstants{

	public void writeToDB(String queueMessage) throws Exception
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		OrderSaxParserHandler orh = new OrderSaxParserHandler();
		saxParser.parse(new InputSource(new StringReader(queueMessage)), orh);
		List<Order> orderList = orh.getOrderList();
		String tableInsertValues = "";
		for(Order orderElem : orderList)
		{   
			Connection conn = derbyDBConnect.createConnection();
			String selectWhereCondition = "";
			if(orderElem != null)
			{
				selectWhereCondition = "ORDER_HEADER_KEY='"+orderElem.getOrderHeaderKey()+"'";
				if(!derbyDBConnect.checkforRecordExistence(conn,MG_ORDER_TABLE_NAME, selectWhereCondition))
				{				
					tableInsertValues = constructValuesforTableInsert(orderElem);				
					derbyDBConnect.insertRecord(conn, MG_ORDER_TABLE_NAME, tableInsertValues);

				}
				else
				{
					// TODO add entry to log for already record exist for that orderHeaderKey/orderNo.
				}
			}
			derbyDBConnect.closeConnection(conn);
		}

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

	//	public static void main(String args[]) throws Exception
	//	{
	//		String queueMessage = "<Order OrderHeaderKey='345345' OrderNo='hgdf' Status='Shipped'/>";
	//		writeToDB(queueMessage);		
	//	}
}
