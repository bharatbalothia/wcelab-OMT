package com.ibm.omsMigrationTool.common;
import java.util.*;
import java.util.Properties;

public final class SourceConfigReader {

	Properties configFile;
	public SourceConfigReader()
	{
		configFile = new java.util.Properties();
		try {
			configFile.load(this.getClass().getClassLoader().
					getResourceAsStream("config/source/omtSource.cfg"));
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