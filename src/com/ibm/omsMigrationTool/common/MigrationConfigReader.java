package com.ibm.omsMigrationTool.common;
import java.util.*;
import java.util.Properties;

public final class MigrationConfigReader {

	Properties configFile;
	public MigrationConfigReader()
	{
		configFile = new java.util.Properties();
		try {
			configFile.load(this.getClass().getClassLoader().
					getResourceAsStream("config/migration.cfg"));
		}catch(Exception eta){
			eta.printStackTrace();
		}
	}

	public String getProperty(String key)
	{
		String value = this.configFile.getProperty(key);
		return value;
	}
}