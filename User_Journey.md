
# User Journey

As part of OMT implementation, the application needs two roles to be performed to successfully complete the migration.

## Configure and Compile Migration Tool

In this journey, the migration configuration user configures the Order Migration Tool based on the Source and Target environments. This is a planned one time only activity for a migration.

### Epic 1? Configure Migration Tool for a migration (//TODO: Link to Epic)

1. The user clone’s git project of oms-migration-tool
2. The user opens the project in Eclipse
3. The user navigates to the /config/source folder
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

1. The user can also implement OrderLister, OrderGetter, ShipmentLister, and ShipmentGetter classes by implementing corresponding com.ibm.orderMigrationTool.XXXinterfaces. The com.ibm.orderMigrationTool also has abstract classes to ease the implementation of custom Lister and Getter. The Source.properties provides the ability to specify custom Lister or Getter classes. 

1. The user navigates to the /config/target folder
	1. The user updates the Target.properties file.

1. The user can add transformation files in the /config/target folder to transform orders and/or shipments from source output XML to target. ~~But if the value of XSLTRequired_For_Order and XSLTRequired_For_shipment flag is set to False, the~~ The migration tool writes the exact order or shipment to target if no transformation files presents. The transformation file names are:
	
	1. OrderTransformation.xslt
	1. ShipmentTransformation.xslt

7. The user can also implement OrderWriter and ShipmentWriter classes by implementing corresponding com.ibm.orderMigrationTool.XXXinterfaces. The com.ibm.orderMigrationTool also has abstract classes to ease the implementation of custom Writer.

8. After configuration, the user uses the build.xml in the project to build target DEPLOY and creates /deploy/OmsMigration.zip file

9. The user copies the OmsMigration.zip to the execution server of the migration.
	1. The user should have Read/Write/Execute privilege on the server folder
	1. The user should have access to JDK.
10.	The user unzips the OmsMigration.zip and see the following file structure
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

### Epic 2? Run migration first time for all closed orders and shipments (//TODO: link to the Epic)

User is running Order Migration Tool first time with the intent to migration all closed orders and shipments from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source/source.properties``` to confirm the start date is before the oldest orders and shipments in source system. 
1. In this case, the migration configuration user has already configured proper parameters for source and target. The user executes the migration with ```./oms-migration.sh orderandshipment```. This instructs Order Migration Tool to:
	1. Create Derby Network Server if it doesn't exist - [AR] I would suggest to move this to one time configuration by Configure Migration Tool User and specify the path in our source.cfg file. 
	2. Start Derby Network Server if it is not running
	3. List and retrieve orders from source system
	4. Import orders into target system
	5. List and retrieve shipments from source system
	6. Import shipments into target system
	7. Exit the JVM once all orders and shipments are processed (success or fail) by createts
	8. The Derby Network Server will still be running after JVM exist to allow reporting and analysis. The user can stop the Derby Network Server manually. 

### Epic 3? Run migration after initial migration (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration newly closed orders and shipments from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
1. The user executes the migration with ```./oms-migration.sh orderandshipment```. This instructs Order Migration Tool to:
	1. Start Derby Network Server if it is not running. The database folder in ```./data/omt``` should already exist.
	2. List and retrieve orders from source system
	3. Skip successfully imported orders base on Derby DB and import new ones into target system
	4. List and retrieve shipments from source system
	5. Skip successfully imported shipments base on Derby DB and import new ones into target system
	6. Exit the JVM once all orders and shipments are processed (success or fail) by createts
	7. The Derby Network Server will still be running after JVM exist to allow reporting and analysis. The user can stop the Derby Network Server manually. 

### Epic 4? Run migration for only orders after initial migraiton (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration newly closed orders (only orders) from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh order```

### Epic 5? Run migration for only shipments after initial migraiton (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration newly closed orders (only orders) from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh shipment```

### Epic 6? Redo all orders and shipments that was loaded into target yesterday or a date-range (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to re-migrate the orders and shipments loaded yesterday using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ./oms-migration.sh redo <forStartDate – mm/dd/yyyy> <forEndDate – mm/dd/yyyy>
		Note:The forStartDate/ forEndDate is the timestamp of record creation in Migration DB.

### Epic 7 Re-migrate a specific order (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to re-migrate a specific order using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration.
2. To redo for a order-date or list of orders. The user will update the column ‘IS_IMPORT_SUCCESS = false’ for the related records in MG_XXX tables.
3. The user executes the migration with ./oms-migration.sh order
(By doing this when the writer process runs, it will pick up the above modified records and redo the migration write process)

### Epic 8 Re-migrate a specific Shipment (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to re-migrate a specific shipment using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration.
2. To redo for a order-date or list of shipments. The user will update the column ‘IS_IMPORT_SUCCESS = false’ for the related records in MG_XXX tables.
3. The user executes the migration with ./oms-migration.sh shipment
(By doing this when the writer process runs, it will pick up the above modified records and redo the migration write process)

### Epic 9 To get the list of failure count and pending list of Orders/Shipments. (//TODO: link to Epic)

1. To get the failure count and the list of orders/shipments failed, the user will run the following query to get the list.
	i.	Select * from MG_XXX where IS_IMPORT_FAILURE = true;
2.To get the pending list of orders/shipments to be processed, the user will run the following query:
	i.	Select * from MG_XXX where IS_IMPORT_FAILURE = false and IS_IMPORT_SUCCESS = false;

### Epic 10? Run migration for only inFlight Orders after initial migraiton (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration the inFlight Orders (only orders) from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh inFlightOrders```

### Epic 11? Run migration for only inFlight Shipments after initial migraiton (//TODO: link to Epic)

User is running Order Migration Tool after previous run or runs. User's intent is to migration the inFlight Shipments (only shipments) from source to target using one migration server and one JVM.

1. The user reviews the ```./config/source``` and ```./config/target``` to confirm parameters are valid to the user's intented migration. 
2. The user executes the migration with ```./oms-migration.sh inFlightShipments```

### Epic 12? Monitoring Progress of migration in Derby DB (//TODO: link to Epic)

User is running Order Migration Tool one migration server and one JVM.

1. The user can connect to the Derby Network Server to monitor the progress of the migration.
2. The migration tool creates and populates a record during migration in the folder of migration-db and Derby Network Server on the execution server. 


# TBD Content

This role responsibilities includes successful execution of OMT – (OMS Migration Tool). The following tasks can be performede by the Executor User

1.	The user executes the migration in 1 of 8 options below
	1.To initialize and start the migration DB, the user will execute the following command:
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


