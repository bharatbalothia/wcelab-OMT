package com.ibm.omsMigrationTool.common;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public final class OMTCommonUtils implements OMTConstants{
	//Private constructor
	private OMTCommonUtils(){
		
	}

	public static void sourceCfgReader() throws Exception {
		
	}
	public static String getConfigfromSource(String propertyName) throws Exception {
		String propertyValue=null;
		
		return propertyValue; 
		
	}
	
	public static String documentToString(Document inDoc) throws TransformerException
	{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		StringWriter sw = new StringWriter();
		trans.transform(new DOMSource(inDoc), new StreamResult(sw));
		return sw.toString();
	}

	public static Document transformXML(String property, Document getOrderDetailsOp) {

		Document transformedImportIp = null;
		// TODO logic to transfomXML and send the transformed XML
		return transformedImportIp;
		
	}
}
