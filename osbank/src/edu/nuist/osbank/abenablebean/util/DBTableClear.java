package edu.nuist.osbank.abenablebean.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.nuist.osbank.dbenablebean.DBEnableBeanSqlConstructor;
import edu.nuist.osbank.dbenablebean.DBImpl;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;



public class DBTableClear {
	//TRUNCATE TABLE  `forTest`
	
	Class<?> c = null;
	Field[] checkfields = null;
	String tableName = "";
	DBTableCheckor dbCheckor = new DBTableCheckor();
	SP annotation = null;
	DBImpl dboper = new DBImpl();
	
	@SuppressWarnings("unchecked")
	public DBTableClear(Class<?> c){
		this.c = c;
		this.checkfields = c.getDeclaredFields();  
		if (c.isAnnotationPresent(SP.class)) {
			annotation = (SP) c.getAnnotation(SP.class);
			this.tableName = annotation.table();
		}
		
		if(this.c == null || this.checkfields==null || this.checkfields.length==0 || this.tableName.equals("")){
			System.out.println("Clear Error: The DBEnableBean Class is null or its' Attributes defined error or the TABLENAME according to the Class isn't defined!");
			return;
		}
		if( dbCheckor.isTableExist(this.tableName)){
			clearTable(this.tableName);
		}
		
		HashMap<Field, String[]> componentFields = DBEnableBeanSqlConstructor.getComponentFieldPrepareForSql(this.checkfields);
		Iterator<?> iter = componentFields.entrySet().iterator();
		//		String[0] for {bean, array_bean, linkedlist_bean, array_basic, linkedlist_basic}, 
			//  String[1] for tableName	
			//  String[2] for selfColName
			//  String[3] for dstColName
			//  String[4] for indexColName
		while(iter.hasNext()){
			Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
			String[] fieldDef = entry.getValue();
			if( dbCheckor.isTableExist( fieldDef[1] ) ){
				clearTable( fieldDef[1] );
			}
		}
		System.out.println("The table and Relation table of this Class:" + c.getName() + " has cleared");
	}
	
	private void clearTable(String tableName){
		Connection conn = dboper.getConnection();
		try {
			String sql = "TRUNCATE TABLE "+tableName ;
			System.out.println(sql);
			Statement statement =conn.createStatement();  
			statement.execute(sql);
		} catch (SQLException e) {
			System.out.println("Clear... Error: Clear Table: "+ this.tableName +" Error!");
			e.printStackTrace();
		} finally{
			try{
				if (conn != null)  conn.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	
}
