
# User Journey

As part of OMT implementation, the application needs two roles to be performed to successfully complete the migration.

## Configure and Compile Migration Tool

In this journey, the migration configuration user configures the Order Migration Tool based on the Source and Target environments. This is a planned one time only activity for a migration.

### Epic 1? Configure Migration Tool for a migration (//TODO: Link to Epic)

1. The user clone’s git project of oms-migration-tool
2. The user opens the project in Eclipse
3. The user run the createDB.sh script to setup DerbyDB in the migration Server and specify the installed path in source.properties file. 
4. The user navigates to the /config/source folder
	1. The user updates the Source.properties file.
	1. The user can optionally update the input and output template XML as per their needs. The files are listed below:
		1. ListOrder.xml
		1. ListOrderOutputTemplate.xml
		1. GetOrderDetails.xml
		1. GetOrderDetailsOutputTemplate.xml
		1. ListShipment.xml
		1. ListShipmentOutputTemplate.xml
		1. GetShipmentDetails.xml
		1. GetShipmentDetailsOutputTemplate.xml

5. The user can also implement OrderLister, OrderGetter, ShipmentLister, and ShipmentGetter classes by implementing corresponding com.ibm.orderMigrationTool.XXXinterfaces. The com.ibm.orderMigrationTool also has abstract classes to ease the implementation of custom Lister and Getter. The Source.properties provides the ability to specify custom Lister or Getter classes. 

6. The user navigates to the /config/target folder
	1. The user updates the Target.properties file.

7. The user can add transformation files in the /config/target folder to transform orders and/or shipments from source output XML to target. The migration tool writes the exact order or shipment to target if no transformation files presents. The transformation file names are:
	
	1. OrderTransformation.xslt
	2. ShipmentTransformation.xslt

8. The user can also implement OrderWriter and ShipmentWriter classes by implementing corresponding com.ibm.orderMigrationTool.XXXinterfaces. The com.ibm.orderMigrationTool also has abstract classes to ease the implementation of custom Writer.

9. After configuration, the user uses the build.xml in the project to build target DEPLOY and creates /deploy/OmsMigration.zip file

10. The user copies the OmsMigration.zip to the execution server of the migration.
	1. The user should have Read/Write/Execute privilege on the server folder
	2. The user should have access to JDK.
11.	The user unzips the OmsMigration.zip and see the following file structure
	- oms-migration
		- oms-migration.sh
		- oms-migration.bat
		- oms-migration.jar
		- config
			- source
				- all source files
			- target
				- all target files

## Execute Migration

### Epic 1? Run migration Test mode for closed orders/shipments for a day (//TODO: link to the Epic)

User is running Order Migration Tool first time to test the generated target importOrder/importShipment input XML, with the intent to use that XML to manually import it to target system using one migration server and one JVM.

1. The user reviews the ```./config/source/source.properties``` to confirm the start date and end date.
2. The user executes the migration with ```./oms-migration.sh testOrder``` for orders and  ```./oms-migration.sh testShipment``` for Shipments. This instructs Order Migration Tool to:

	1. Start Derby Network Server if it is not running
	2. List and retrieve orders/shipments from source system
	3. Export the importOrder/importShipment XML to the file system. 
	4. Exit the JVM.
	5. After completion the user should execute clearDB to remove all the test records from the DB, with ``` ./oms-migration.sh clearDB```.
	If the user intent to test the PurchaseOrder XML, then the User can specify the filter with DOCUMENT_TYPE in ListOrder.xml and generate only the importOrder XML for PurchaseOrder, similarly it can be done for all type of Orders.


### Epic 2? Run migration Test mode for InFlightOrders/InFlightShipments for a day (//TODO: link to the Epic)

User is running Order Migration Tool first time to test the generated target importOrder/importShipment input XML, with the intent to use that XML to manually import it to target system using one migration server and one JVM for InflightOrders/InflightShipments.

1. The user reviews the ```./config/source/source.properties``` to confirm the start date and end date. 
2. The user executes the migration with ```./oms-migration.sh testInflightOrders``` for orders and  ```./oms-migration.sh testInflightShipments``` for shipments. This instructs Order Migration Tool to:
	 
	1. Start Derby Network Server if it is not running
	2. List and retrieve orders/shipments from source system
	3. Export the generated XML for importOrder/importShipments to file system. 
	4. Exit the JVM. 
	5. After completion the user should execute clearDB to remove all the test records from the DB, with ``` ./oms-migration.sh clearDB```.
	If the user intent to test the PurchaseOrder XML, then the User can specify the filter with DOCUMENT_TYPE in ListOrder.xml and generate only the importOrder XML for PurchaseOrder, similarly it can be done for all type of Orders. It can be repeated for one day or for a date range.

### Epic 3? Run migration first time for all closed orders and shipments (//TODO: link to the Epic)

User is running Order Migration Tool first time with the intent to migration all closed orders and shipments from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source/source.properties``` to confirm the start date is before the oldest orders and shipments in source system. 
2. In this case, the migration configuration user has already configured proper parameters for source and target. The user executes the migration with ```./oms-migration.sh orderandshipment```. This instructs Order Migration Tool to:
	
	1. Start Derby Network Server if it is not running
	2. List and retrieve orders from source system
	3. Import orders into target system
	4. List and retrieve shipments from source system
	5. Import shipments into target system
	6. Exit the JVM once all orders and shipments are processed (success or fail) by createts
	7. The Derby Network Server will still be running after JVM exist to allow reporting and analysis. The user can stop the Derby Network Server manually. 

### Epic 4? Run migration after initial migration (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration newly closed orders and shipments from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh orderandshipment```. This instructs Order Migration Tool to:
	1. Skip successfully imported orders/shipments base on Derby DB and import new ones into target system.

### Epic 5? Run migration for only orders or only shipments after initial migraiton (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration newly closed orders (only orders) from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh order``` for orders and  ```./oms-migration.sh shipment``` for shipmens.This instructs Order Migration Tool to:
	1. Skip successfully imported orders / import shipments base on Derby DB and import new ones into target system.	


### Epic 6? Redo all orders and shipments that was loaded into target yesterday or a date-range (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to re-migrate the orders and shipments loaded for a set of orders/shipments using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. In Derby BD, Filter the list of Order/Shipment for a target date and modify the records for the set of orders/shipments with IMPORTED_TS to NULL. 
3. The user executes the migration with  ```./oms-migration.sh redo``` This instructs Order Migration Tool to:
	1. Start Derby Network Server if it is not running. The database folder in ```./data/omt``` should already exist.	
	2. Retrieve orders from source system
	3. Skip successfully imported orders base on Derby DB and import new ones into target system
	4. Retrieve shipments from source system
	5. Skip successfully imported shipments base on Derby DB and import new ones into target system	
	6. Exit the JVM once all orders and shipments are processed (success or fail) by createts
	7. The Derby Network Server will still be running after JVM exist to allow reporting and analysis. The user can stop the Derby Network Server manually. 

### Epic 7 Re-migrate a specific Order/Shipment or a set of Order/Shipment(//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to re-migrate a specific order/shipment using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration.
2. To redo for a order/shipment, the user will update the column ‘IS_IMPORT_SUCCESS = NULL’ for the related records in MG_XXX tables.
3. The user executes the migration with ./oms-migration.sh order for ORDER and ./oms-migration.sh shipment for SHIPMENT.
(By doing this when the writer process runs, it will pick up the above modified records and redo the migration write process)


### Epic 8 To get the list of failure count and pending list of Orders/Shipments. (//TODO: link to Epic)

1. To get the failure count and the list of orders/shipments failed, the user will run the following query to get the list.
	i.	Select * from MG_ORDER where IMPORT_FAILURE_TS != NULL; for Orders.
	ii.	Select * from MG_SHIPMENT where IMPORT_FAILURE_TS != NULL; for Shipments.
2.To get the pending list of orders/shipments to be processed, the user will run the following query:
	i.	Select * from MG_ORDER where IMPORT_FAILURE_TS = NULL and IMPORT_TS = NULL; for Orders.
	ii.	Select * from MG_SHIPMENT where IMPORT_FAILURE_TS = NULL and IMPORT_TS = NULL; for Shipments.
	
### Epic 9 Run migration for only inFlight Orders or iFlight Shipments after initial migraiton (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration the inFlight Orders (only orders) from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh inFlightOrders``` for orders and ```./oms-migration.sh inFlightShipments``` for Shipments.


### Epic 10 Monitoring Progress of migration in Derby DB (//TODO: link to Epic)

User is running Order Migration Tool one migration server and one JVM.

1. The user can connect to the Derby Network Server to monitor the progress of the migration.
2. The migration tool creates and populates a record during migration in the folder of migration-db and Derby Network Server on the execution server. 

### Epic 11 TroubShooting - Looking for Failure reasons (//TODO: link to Epic)

User is running Order Migration Tool one migration server and one JVM.

1. The user can connect to the Derby Network Server to monitor the progress of the migration.
2. The user queries for the order/Shipments and look for the value in FAILURE_REASON column. Depending on the FAILURE_REASON, the user will fix the error in Getter/Transformer/Writer.
3. If the user doesn't find the entry in the DERBY_DB after running the migration tool, then the lister is not getting the data from the source.
4. Further details on error/exception, will be logged to the log file.

### Epic 12? Run migration for customers (//TODO: link to the Epic)

User is running Order Migration Tool first time with the intent to migration all closed orders and shipments from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source/source.properties``` to confirm the start date is before the oldest orders and shipments in source system. 
2. In this case, the migration configuration user has already configured proper parameters for source and target. The user executes the migration with ```./oms-migration.sh customer```. This instructs Order Migration Tool to:
	
	1. Start Derby Network Server if it is not running
	2. List and retrieve customers from source system
	3. Import customers into target system
	4. Exit the JVM once all customers are processed (success or fail) by createts
	5. The Derby Network Server will still be running after JVM exist to allow reporting and analysis. The user can stop the Derby Network Server manually. 

### Epic 13? Run migration to test Customer Migration (//TODO: link to the Epic)

User is running Order Migration Tool first time to test the generated target importOrder/importShipment input XML, with the intent to use that XML to manually import it to target system using one migration server and one JVM for InflightOrders/InflightShipments.

1. The user reviews the ```./config/source/source.properties``` to confirm the start date and end date. 
2. The user executes the migration with ```./oms-migration.sh testCUstomer``` for orders and  ```./oms-migration.sh testInflightShipments``` for shipments. This instructs Order Migration Tool to:
	 
	1. Start Derby Network Server if it is not running
	2. List and retrieve customers from source system
	3. Export the generated XML for manageCustomer to file system. 
	4. Exit the JVM. 
	5. After completion the user should execute clearDB to remove all the test records from the DB, with ``` ./oms-migration.sh clearDB```.
		
### Epic 14 Run migration for only inFlight Customer (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration the Customers created during inFlight period from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh inFlightCustomers``` for Customers.
