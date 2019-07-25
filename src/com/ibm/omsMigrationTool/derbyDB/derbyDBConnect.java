package com.ibm.omsMigrationTool.derbyDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ibm.omsMigrationTool.common.MigrationConfigReader;
import com.ibm.omsMigrationTool.common.OMTConstants;
import com.ibm.omsMigrationTool.order.OMTOrderExecutor;

public class derbyDBConnect implements OMTConstants{

	//private static String dbURL = "jdbc:derby://localhost:1527/OMTB;create=true;user=me;password=mine;";
	private static String dbURL = null;
	public static Connection conn = null;
	public static Statement stmt = null;
	static MigrationConfigReader mig_cfg_reader = new MigrationConfigReader();
	static Logger log = Logger.getLogger(derbyDBConnect.class);
	public static Connection createConnection()
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			//Get a connection
			dbURL = "jdbc:derby://"+mig_cfg_reader.getProperty(DERBY_DB_HOST_NAME)+":"+
					mig_cfg_reader.getProperty(DERBY_DB_PORT)+"/"+mig_cfg_reader.getProperty(DERBY_DB_SCHEMA_NAME)+
					"user="+mig_cfg_reader.getProperty(DERBY_DB_USER_NAME)+";password="+mig_cfg_reader.getProperty(DERBY_DB_PASSWORD)+";";
			log.info("Connecting to DB:"+dbURL);
			conn = DriverManager.getConnection(dbURL); 
		}
		catch (Exception except)
		{
			except.printStackTrace();
		}
		return conn;
	}

	public static void insertRecord(Connection dbConnection, String tableName, String commaSepratedValues)
	{
		try
		{
			stmt = dbConnection.createStatement();	
			String insertStatement = "insert into " + tableName + " values (" + commaSepratedValues+")";
			if(log.isDebugEnabled())
			{
				log.debug(insertStatement);
			}
			stmt.execute(insertStatement);
			stmt.close();
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
			log.error(sqlExcept);
		}
	}

	public static boolean checkforRecordExistence(Connection conn, String tableName, String whereCondition) throws Exception {

		String selectQuery = "select count(*) as RECORDCOUNT from "+tableName+" where "+ whereCondition;
		ResultSet retrivedResultSet = derbyDBConnect.executeQuery(conn, selectQuery);
		if(retrivedResultSet.getInt("RECORDCOUNT") > 0)
		{			
			return true;
		}
		return false;
	}

	public static ResultSet updateRecord(Connection conn, String tableName, String whereCondition, String columnName,String columnValue) throws Exception {

		String updateQuery = "update "+tableName+" set "+columnName+" = "+columnValue+" where "+ whereCondition;
		ResultSet updateResultSet = derbyDBConnect.executeQuery(dbConnection, updateQuery);
		return updateResultSet;
	}
	
	public static ResultSet executeQuery(Connection dbConnection, String queryToExecute)
	
	{
		ResultSet rs = null;
		try
		{
			stmt = dbConnection.createStatement();			 
			rs = stmt.executeQuery(queryToExecute);
			stmt.close();
			dbConnection.commit();
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
		}
		return rs;
	}
	public static void closeConnection(Connection conn)
	{
		try
		{			
			if (conn != null)
			{
				//DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}           
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
		}

	}

	public static ResultSet getRecordData(Connection conn2, String mgTableName, String selectWhereCondition) {
		String selectQuery = "select count(*) as RECORDCOUNT from "+mgTableName+" where "+ selectWhereCondition;
		ResultSet retrivedResultSet = derbyDBConnect.executeQuery(conn, selectQuery);		
		return retrivedResultSet;
		
	}
}
