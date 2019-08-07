#!/bin/sh

## This script will download, install, and set up Derby DB for the Sterling
## Order Migration Tool. Script was written for bash on CentOS/RHEL
##

## PREREQUISITES
## Installed and Configured JDK 1.8
## Sudoer access

## Update the variable value Below  
## JAVA_SECURITY_PATH=/path/to/your/java/security/java.policy file
	JAVA_SECURITY_PATH='/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.222.b10-0.el7_6.x86_64/jre/lib/security'
	USR_HOME='/home/sterling'

## Step 1 - USE yum to install bsdtar

	sudo yum install bsdtar

## Step 2 - Unzip,configure, and install  Derby Network Server

	umask 002
	sudo mkdir /opt/omt
	sudo mkdir /opt/omt/derby
	sudo chown sterling:sterling /opt/omt
	sudo chown sterling:sterling /opt/omt/derby
	mkdir /opt/omt/derby/databases
	mkdir /opt/omt/derby/sqlfiles
	cd /opt
	sudo chmod -R 777 ./omt
		
	export DERBY_INSTALL=/opt/omt/derby
	export DERBY_HOME=/opt/omt/derby
	
	## Step 3 - Download derby bin distro

	curl -L -k -o "$USR_HOME/db-derby-10.14.2.0-bin.zip"  "apache.osuosl.org//db/derby/db-derby-10.14.2.0/db-derby-10.14.2.0-bin.zip"

	mv $USR_HOME/db-derby-10.14.2.0-bin.zip $DERBY_INSTALL
	cd $DERBY_INSTALL
	bsdtar --strip-components=1 -xvf ./db-derby-10.14.2.0-bin.zip
	chmod -R 755 $DERBY_INSTALL/*
	

## Step 3 - Create derby.properties in $DERBY_INSTALL

	printf "derby.authentication.provider=BUILTIN\nderby.infolog.append=true\nderby.storage.pageSize=8192\nderby.storage.pageReservedSpace=60\nderby.user.Migrator=Migrator\nderby.drda.host=0.0.0.0" > 	$DERBY_INSTALL/derby.properties

## Step 4 - Set the java sercurity permissions
## Append to the end of java.policy

	lineno=$(grep -n '};' $JAVA_SECURITY_PATH/java.policy.mrl | tail -n1 | cut -f1 -d:)
	text="\ \t//Added for Derby Permissions\n\tpermission java.lang.RuntimePermission \"createClassLoader\";\n\tpermission java.util.PropertyPermission \"derby.*\", \"read\";\n\tpermission java.io.FilePermission \"/opt/omt/derby/databases/-\", \"read,write,delete\";\n\tpermission java.io.FilePermission \"\${derby.system.home}\${i/}derby.log\", \"read,write,delete\";\n\tpermission java.io.FilePermission \"\${derby.system.home}/-\", \"read,write,delete\";\n"
	line=$lineno
	line+=i

	sudo sed -i "$line $text" $JAVA_SECURITY_PATH/java.policy

## Step 5 - Open port for DERBY

	sudo firewall-cmd --permanent --zone=public --add-port=1527/tcp
	sudo firewall-cmd --reload

## Step 6 - Create SQL file to Create tables

	printf "CREATE TABLE MG_ORDER (ORDER_HEADER_KEY VARCHAR(24) NOT NULL PRIMARY KEY,ORDER_NO VARCHAR(40) NOT NULL,ORDER_STATUS VARCHAR(40) NOT NULL,PARENT_ORDER_HEADER_KEY VARCHAR(24),PARENT_ORDER_IMPORT_TS TIMESTAMP,TRANSFER_INITIATED_TS TIMESTAMP,SRC_RETRIVAL_COMPLETE_TS TIMESTAMP,XSL_TRANSFORM_COMPLETE_TS TIMESTAMP,IMPORTED_TS TIMESTAMP,IMPORT_FAILURE_TS TIMESTAMP,FAILURE_REASON TIMESTAMP,CREATE_TS TIMESTAMP NOT NULL); \n CREATE TABLE MG_SHIPMENT (SHIPMENT_HEADERY_KEY VARCHAR(24) NOT NULL PRIMARY KEY, SHIPMENT_NO VARCHAR(40) NOT NULL, SHIPMENT_STATUS VARCHAR(15) NOT NULL,TRANSFER_INITIATED_TS TIMESTAMP,SRC_RETRIVAL_COMPLETE_TS TIMESTAMP,XSL_TRANSFORM_COMPLETE_TS TIMESTAMP,IMPORTED_TS TIMESTAMP,IMPORT_FAILURE_TS TIMESTAMP,FAILURE_REASON VARCHAR(100),CREATE_TS TIMESTAMP NOT NULL); \n CREATE TABLE MG_CUSTOMER (CUSTOMER_ID VARCHAR(24) NOT NULL PRIMARY KEY,CUSTOMER_KEY VARCHAR(24) NOT NULL, TRANSFER_INITIATED_TS TIMESTAMP,SRC_RETRIVAL_COMPLETE_TS TIMESTAMP,XSL_TRANSFORM_COMPLETE_TS TIMESTAMP,IMPORTED_TS  TIMESTAMP, IMPORT_FAILURE_TS TIMESTAMP,FAILURE_REASON VARCHAR(100),CREATE_TS TIMESTAMP NOT NULL);" > /opt/omt/derby/sqlfiles/createMGtables.sql

## Step 7 -Start Derby

        export CLASSATH=$CLASSPATH:$DERBY_INSTALL/lib/derbynet.jar:$DERBY_INSTALL/lib/derbytools.jar:$CLASSPATH:$DERBY_INSTALL/lib/derbyclient.jar:.

	#update the .bashrc profile to include the lib jars here as well and reload the profile 

	printf "DERBY_INSTALL=/opt/omt/derby\nCLASSPATH=\$CLASSPATH:\$DERBY_INSTALL/lib/derbynet.jar:\$DERBY_INSTALL/lib/derbytools.jar:$DERBY_INSTALL/lib/derbyclient.jar:\nexport CLASSPATH\n export DERBY_INSTALL" >> ~/.bashrc
	source ~/.bashrc

        $DERBY_INSTALL/bin/setNetworkServerCP
	nohup $DERBY_INSTALL/bin/startNetworkServer &
	sleep 5s
## Step 8 - Test Connection, Create Database, and create tables

	printf "\n\nThe following step will connect to the new Derby instance and\nstart the ij command interpreter. to create the new database, 'MigrationDB', issue the collowing command at the lj>> prompt:\n\n\tconnect 'jdbc:derby://localhost:1527//opt/omt/derby/databases/MigrationDB;create=true';\n\nTo create the migration tables in the new database,\nissue the following command:\n\n\trun '/opt/omt/derby/sqlfiles/createMGtables.sql';\n\nTo disconnect from Derby use the 'disconnect;'command.\nTo disconect and exit the derby ij command interprester, use the 'exit;'command.\n\n"

	read -n 1 -s -r -p "Press any key to continue"
	# use ij interpreter
        java -Dij.user=Migrator -Di.password=Migrator org.apache.derby.tools.ij

## Step 9 - Prompt user for derby shutdown

	printf "\nDo you want to stop the Derby Server(Y/N)? :"
        read YESNO
        awk -vs1="$YESNO" -vs2="Y" -vs3="$ARCHIVE_DIR" ' BEGIN {
                if ( tolower(s1) == tolower(s2) ){
                system("printf \"\n\nStopping Derby Network Server...\n\"")
                system("/opt/omt/derby/bin/stopNetworkServer")
                }
                else{
                   system("printf \"\nDerby Netowrk Server will remain running.\n\n\"")
                }}'




