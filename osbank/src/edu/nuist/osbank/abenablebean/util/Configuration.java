package edu.nuist.osbank.abenablebean.util;

import java.util.Properties;

public class Configuration {
	
	private static Properties props = null; 
	public static int FILE_TXTRECODE_LENGTH = 2048;
	public static final String FILEEXTENDNAMETABLE = "fileExtend.properties";
	public static final String DataSourceAttrKey = "DATASOURCES";
	
	static{/*
		try{
			String configFilePath = Configuration.class.getClassLoader().getResource("").getFile(); //Configuration.class.getResource("/").getFile().toString();
			configFilePath = configFilePath.substring(0, configFilePath.indexOf("bin/"));
			props = new Properties();
			System.out.println(configFilePath + "config.properties");
			props.load(new FileInputStream(configFilePath + "config.properties"));	
		}catch(Exception e){
			System.out.println("System Configuration Inition Error!");
		}
		*/
	}

	public static String getLogger() {
		if(props == null)
			return "report";
		return props.getProperty("logger");
	}

	public static String getDbUrl() {
		if(props == null) return "jdbc:mysql://localhost:3306/onezetta";
		return props.getProperty("dburl");
	}

	public static String getDbUser() {
		if(props == null) return "root";		
		return props.getProperty("dbuser");
	}
	public static String getDbpassword() {
		if(props == null) return "123456";	
//		if(props == null) return "root";	
		return props.getProperty("dbpassword");
	}
	
	public  static void  main(String[] args){
		
		System.out.println(Configuration.getLogger());
	}
}
