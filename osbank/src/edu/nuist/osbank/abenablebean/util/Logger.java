package edu.nuist.osbank.abenablebean.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Logger {
	
	public static Log getLogger(){
		
		String logName = Configuration.getLogger().toLowerCase().trim();
		
		if(logName.equals("root")){
			return LogFactory.getLog("rootLogger");
		}else if(logName.equals("report")){
			return LogFactory.getLog("reportLog");
		}else
			return  LogFactory.getLog(logName);
	}
}
