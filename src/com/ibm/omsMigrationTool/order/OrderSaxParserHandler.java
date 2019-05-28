package com.ibm.omsMigrationTool.order;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OrderSaxParserHandler extends DefaultHandler {

	// List to hold Employees object
	private List<Order> orderList = null;	
	private Order order = null;
	private StringBuilder data = null;

	// getter method for employee list
	public List<Order> getOrderList() {
		return orderList;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("Order")) {
			
					
			order = new Order();
			order.setOrderHeaderKey(attributes.getValue("OrderHeaderKey"));
			order.setOrderNo(attributes.getValue("OrderNo"));
			order.setStatus(attributes.getValue("Status"));
			if (orderList == null)
				orderList = new ArrayList<>();
		}
		// create the data container
		data = new StringBuilder();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
				
		if (qName.equalsIgnoreCase("Order")) {
			// add Employee object to list
			orderList.add(order);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}