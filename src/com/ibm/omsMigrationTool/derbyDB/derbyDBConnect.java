package com.ibm.omsMigrationTool.derbyDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class derbyDBConnect {

	private static String dbURL = "jdbc:derby://localhost:1527/OMTB;create=true;user=me;password=mine";

	public static Connection conn = null;
	public static Statement stmt = null;

	public static Connection createConnection()
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			//Get a connection
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
			System.out.println(insertStatement);
			stmt.execute(insertStatement);
			stmt.close();
		}
		catch (SQLException sqlExcept)
		{
			sqlExcept.printStackTrace();
		}
	}
	
	public static boolean checkforRecordExistence(Connection conn, String tableName, String whereCondition) throws Exception {
		
		String selectQuery = "select count(*) as RECORDCOUNT from "+tableName+" where "+ whereCondition;
		ResultSet orderRec = derbyDBConnect.executeSelect(conn, selectQuery);
		if(orderRec.getInt("RECORDCOUNT") > 0)
		{
			return true;
		}
		return false;
	}
	
	public static ResultSet executeSelect(Connection dbConnection, String selectQuery)
	{
		ResultSet rs;
		try
		{
			stmt = dbConnection.createStatement();			 
		    ResultSet rs = stmt.executeQuery(selectQuery);
			stmt.close();
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
}
