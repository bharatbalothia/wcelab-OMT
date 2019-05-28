
# User Journey

As part of OMT implementation, the application needs two roles to be performed to successfully complete the migration.

## Migration Tool Configuring User

This role responsibilities includes successful configuring of OMT – (OMS Migration Tool), before starting the real-time migration. The following task to be done to complete the step of OMT configuration:
	
1.	The user clone’s git project of oms-migration-tool
2.	The user opens the project in Eclipse
3.	The user navigates to the /config/source folder
	a.	The user updates the omtSource.cfg file with customized values.
	b.	The user can optionally update the input and output template XML as per their needs. The files are listed below:
		i.	ListOrder.xml
		ii.	ListOrderOutputTemplate.xml
		iii.	GetOrderDetails.xml
		iv.	GetOrderDetailsOutputTemplate.xml
		v.	ListShipment.xml
		vi.	ListShipmentOutputTemplate.xml
		vii.	GetShipmentDetails.xml
		viii.	GetShipmentDetailsOutputTemplate.xml
4.	The user can also implement OmtOrderLister, OmtOrderGetter, OmtShipmentLister, and OmtShipmentGetter classes by implementing corresponding com.ibm.omsMigrationTool.XXXinterfaces. The com.ibm.omsMigrationTool also has abstract classes to ease the implementation of custom Lister and Getter.
5.	The user navigates to the /config/target folder
	a.	The user updates the omtTarget.cfg with customized values.
6.	The user will ad transformation files in the /config/target folder to transform order/Shipment from source output XML to target. But if the value of XSLTRequired_For_Order and XSLTRequired_For_shipment flag is set to False, the migration tool writes the exact order and shipment to target. Default Transformation file name:
	a.	OrderTransformation.xslt
	b.	ShipmentTransformation.xslt
7.	The user can also implement OmtOrderWriter and OmtShipmentWriter classes by implementing corresponding com.ibm.omsMigrationTool.XXXinterfaces. The com.ibm.omsMigrationTool also has abstract classes to ease the implementation of custom Writer.
8.	After configuration, the user uses the build.xml in the project to build target DEPLOY and creates /deploy/OmsMigration.zip file
9.	The user copies the OmsMigration.zip to the execution server of the migration.
	a.	The user should have Read/Write/Execute privilege on the server folder
	b.	The user should have access to JDK.
10.	The user unzips the OmsMigration.zip and see the following file structure
	a.	oms-migration
		i.	oms-migration.sh
		ii.	oms-migration.bat
		iii.	oms-migration.jar
		iv.	config

## Migration Tool Executor User

This role responsibilities includes successful execution of OMT – (OMS Migration Tool). The following tasks can be performede by the Executor User
	
1.	The user executes the migration in 1 of 8 options below
	1.	To initialize and start the migration DB, the user will execute the following command:
			./oms-migration.sh initDB (create Derby Network Server if needed then start Derby Network Server) – This action will start the database and create the tables if its not present.
	2.	To clear the Migration DB, they user will execute the following command:
			./oms-migration.sh clearDB – This action will check for DB start and then clear the Database of all the records in both MG_ORDER and MG_SHIPMENT tables.
	3.	To stop the Migration DB, they user will execute the following command:
			./oms-migration.sh stopDB – This action will stop the DerbyDB.
	4.	To migrate orders and shipments for a date range specified in cfg file, the user will execute the following command:
			./oms-migration.sh orderandshipment (this imports first order then shipment)
	5.	To migrate only orders for a date range specified in cfg file, the user will execute the following command:
			./oms-migration.sh order (this imports just orders)
	6.	To migrate only shipments for a date range specified in cfg file, the user will execute the following command:
			./oms-migration.sh shipment (this imports just shipments)
	7.	To migrate only InFlightOrders for a date range specified in cfg file, the user will execute the following command:
			./omsmigration.sh InflightOrders (this import inflightOrders)
	8.	To migrate only InFlightShipments for a date range specified in cfg file, the user will execute the following command:
			./oms-migration.sh InflightShipments (this import inflightShipments)
	9.	To redo the migration for the records created in Derby DB on a particular date or a range, the user will execute the following command, by specifying the start and enddate:
			./oms-migration.sh redo  <forStartDate mm/dd/yyyy> <forEndDate mm/dd//yyyy>
2.	Customizing configuration values:
	1.	./oms-migration.sh class path has config folder first before config files in oms-migration.jar. The user can put a different config/source/omtSource.cfg file to get in flight orders and shipment during cutover without rebuild a jar.
	2.	The user will add configuration file in the config folder in the same structure as specified in the Eclipse.
3.	Changing log level 
		The oms-migration.sh outputs WARN level log to console. The user can alter the log level and output by creating and modifying /config/log4j.xml
4.	Monitoring Progress of migration in Derby DB:
	1.	The user can connect to the Derby Network Server to monitor the progress of the migration.
	2.	The migration tool creates and populates a record during migration in the folder of migration-db and Derby Network Server on the execution server. 
5.	DB start/clean/stop:
	1.	The user will run the following command, to start the DerbyDB before the migration
		./oms-migration.sh initDB
	2.	The user will run the following command to clear the records in the DerbyDB
		./oms-migration.sh clearDB
	3.	The user will run the following command to stop the DerbyDB after completing the migration
		./oms-migration.sh stopDB
6.	The user will run the following steps to do a migration of all closedOrders and closedShipments for a specified dateRange in source.cfg:
	1.	./oms-migration.sh initDB
	2.	./oms-migration.sh orderandshipment
	3.	./oms-migration.sh stopDB
7.	The user will run the following steps to do a migration of inflight Orders and Shipments for specified cutoff date in source.cfg:
	1.	./oms-migration.sh initDB
	2.	./oms-migration.sh InflightOrders
	3.	./oms-migration.sh InflightShipments
	4.	./oms-migration.sh stopDB
8.	Migrating closed orders and shipments on daily basis.
	1.	The user will run ./oms-migration.sh redo <forStartDate – mm/dd/yyyy> <forEndDate – mm/dd/yyyy>
		Note: The forStartDate/ forEndDate is the timestamp of record creation in Migration DB.
9.	Migrating in flight orders and shipments during cut over.
	1.	????
10.	Redo all orders and shipments that was loaded into target yesterday (or last week...)
	1.	The user will run ./oms-migration.sh redo <forStartDate – mm/dd/yyyy> <forEndDate – mm/dd/yyyy>
		Note:The forStartDate/ forEndDate is the timestamp of record creation in Migration DB.
11.	Troubleshoot a specific order migration error (how does the migration team know there was an error? how to get the detail of failure? Any manual step to recover (if needed):
	1.	To get the failure count and the list of orders/shipments failed, the user will run the following query to get the list.
		i.	Select * from MG_XXX where IS_IMPORT_FAILURE = true;
	2.	To get the pending list of orders/shipments to be processed, the user will run the following query:
		i.	Select * from MG_XXX where IS_IMPORT_FAILURE = false and IS_IMPORT_SUCCESS = false;
12.	Re-migrate a specific order or shipment.
	1.	To redo for a date or list of orders/shipments. The user will update the column ‘IS_IMPORT_SUCCESS = false’ for the related records in MG_XXX tables.
	2.	The user will run the following command to rerun the process:
		./oms-migration.sh orderandshipment
		By doing this when the writer process runs, it will pick up the above modified records and redo the migration write process.


