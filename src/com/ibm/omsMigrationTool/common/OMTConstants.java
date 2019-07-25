package com.ibm.omsMigrationTool.common;

public interface OMTConstants {
	
	// MIGRATION CONFIGUATION NAMES
	public static final String LIST_PAGE_SIZE = "ListPageSize";
	public static final String MAX_QUEUE_DEPTH = "MaxQueueDepth";
	public static final String IMPORT_START_DATE_FOR_ORDERS = "ImportStartDate_For_Orders";
	public static final String IMPORT_END_DATE_FOR_ORDERS = "ImportStartDate_For_Orders";
	public static final String IMPORT_START_DATE_FOR_SHIPMENTS = "ImportStartDate_For_Shipments";
	public static final String IMPORT_END_DATE_FOR_SHIPMENTS = "ImportStartDate_For_Shipments";
	public static final String ORDER_LISTER_CLASS = "ORDER_LISTER_CLASS";
	public static final String ORDER_DETAILS_GETTER_CLASS = "ORDER_DETAILS_GETTER_CLASS";
	public static final String ORDER_TRANSFORMER_XSL = "ORDER_TRANSFORMER_XSL";
	
	//DERBY DB CONFIGURATION
	
	public static final String DERBY_DB_USER_NAME = "DERBY_DB_USER_NAME";
	public static final String DERBY_DB_PASSWORD = "DERBY_DB_PASSWORD";
	public static final String DERBY_DB_HOST_NAME = "DERBY_DB_HOST_NAME";
	public static final String DERBY_DB_PORT = "DERBY_DB_PORT";
	public static final String DERBY_DB_SCHEMA_NAME = "DERBY_DB_SCHEMA_NAME";
	

	//XML Attribute Names
	
	public static final String ORDER_HEADER_KEY = "OrderHeaderKey";
	public static final String ORDER_NO = "OrderNo";
	public static final String STATUS_ATTR = "Status";
	
	
	// DB TABLE NAMES NAMES
	
	public static final String MG_ORDER_TABLE_NAME = "MG_ORDER";
	public static final String MG_SHIPMENT_TABLE_NAME = "MG_SHIPMENT";

	//TABLE_COLUMN_NAMES
	
	public static final String DB_ORDER_HEADER_KEY = "ORDER_HEADER_KEY";	
	public static final String DB_ORDER_NO = "ORDER_NO";
	public static final String DB_ORDER_STATUS  = "ORDER_STATUS";
	public static final String DB_PARENT_ORDER_HEADER_KEY = "PARENT_ORDER_HEADER_KEY";
	public static final String DB_PARENT_ORDER_IMPORT_TS = "PARENT_ORDER_IMPORT_TS";
	public static final String DB_TRANSFER_INITIATED_TS = "TRANSFER_INITIATED_TS";
	public static final String DB_SRC_RETRIVAL_COMPLETE_TS = "SRC_RETRIVAL_COMPLETE_TS";
	public static final String DB_XSL_TRANSFORM_COMPLETE_TS = "XSL_TRANSFORM_COMPLETE_TS";
	public static final String DB_IMPORTED_TS = "IMPORTED_TS";
	public static final String DB_IMPORT_FAILURE_TS = "IMPORT_FAILURE_TS";
	public static final String DB_SHIPMENT_HEADERY_KEY = "SHIPMENT_HEADERY_KEY";
	public static final String DB_SHIPMENT_NO = "SHIPMENT_NO";
	public static final String DB_SHIPMENT_STATUS  = "SHIPMENT_STATUS";


}