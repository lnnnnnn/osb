package edu.nuist.osbank.abenablebean.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.nuist.osbank.dbenablebean.DBEnableBeanSqlConstructor;
import edu.nuist.osbank.dbenablebean.DBImpl;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;


public class DBTableCheckor {
	Class<?> c = null;
	Field[] checkfields = null; 
	String tableName = "";

	DBImpl dboper = new DBImpl();
	
	public DBTableCheckor(){
		;
	}
	
	@SuppressWarnings("unchecked")
	public DBTableCheckor(Class<?> c){
		
		this.c = c;
		this.checkfields = c.getDeclaredFields();  
		if (c.isAnnotationPresent(SP.class)) {
			SP annotation= (SP) c.getAnnotation(SP.class);
			this.tableName = annotation.table();
		}
		//load Class
		if(this.c == null || this.checkfields==null || this.checkfields.length==0 || this.tableName.equals("")){
			System.out.println("Check...1 Error: The DBEnableBean Class is null or its' Attributes defined error or the TABLENAME according to the Class isn't defined!");
			return;
		}
		else
			System.out.println("Check...1 : The DBEnableBean Classes' Attributes defined Correct and the TABLENAME according to the Class:" + this.tableName);
		//check the table according to this Bean Class Exists
		if(isTableExist(this.tableName))
			System.out.println("Check...2 : The TABLENAME according to the Class has Founded!");
		else{
			System.out.println("Check...2 Error:The TABLENAME according to the Class has not Founded!");
			return;
		}		
		//check the primary Key
		if(checkPrimaryKey()){
			System.out.println("Check...3 : The Primary Key: id, has Founded!");
		}else{
			System.out.println("Check...3 Error: The Primary Key: id, hasn't Founded!");
		}
		//check the table field define according to the Attributes of this Bean Class
		HashMap<String, String> tableFieldMap = new HashMap<String, String>();
		tableFieldMap = getTableFieldType(this.tableName);
		
		//String[]: String[0] for ColName, String[1] for Type
		HashMap<Field, String[]> basicFields = DBEnableBeanSqlConstructor.getBasicFieldPrepareForSql(this.checkfields);
		
		Iterator<?> iter = basicFields.entrySet().iterator();
		boolean colDefFailFlag = false;
		while (iter.hasNext()) {
			Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
			Field field = entry.getKey();
			String[] fieldDef = entry.getValue();
			
			if( !checkFieldDefForMysql(tableFieldMap, field, fieldDef) ){
				colDefFailFlag = true;
				break;
			}
		}
		if(!colDefFailFlag){
			if(! "int".equals(getJavaTypeAccordingMysqlType(Integer.parseInt(tableFieldMap.get("id")), false))){
				colDefFailFlag = true;
			}
		}		
		if(!colDefFailFlag){
			System.out.println("Check...4 : The Table Field defined according to The Primary Key Type and BASIC Attribute of this CLASS have checked!");
		}else{
			System.out.println("Check...4 Error: The Table Field defined according to The Primary Key Type and BASIC Attribute of this CLASS have checked failed!");
			return;
		}
		
		//check the component attribute 
		//		String[0] for {bean, array_bean, linkedlist_bean, array_basic, linkedlist_basic}, 
			//  String[1] for tableName	
			//  String[2] for selfColName
			//  String[3] for dstColName
			//  String[4] for indexColName
		System.out.println("Check...5 : Checking The Table and its Fields defined according to this CLASS component Attributes, Please make sure that every realtion table has records !");
		
		HashMap<Field, String[]> componentFields = DBEnableBeanSqlConstructor.getComponentFieldPrepareForSql(this.checkfields, false);
		iter = componentFields.entrySet().iterator();
		boolean componentDefFailFlag = false;
		while (iter.hasNext()) {
			Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
			componentDefFailFlag = checkComponentDefForMysql(entry.getValue());
			if(componentDefFailFlag)	break;
		}
		if(!componentDefFailFlag){
			System.out.println("Check...5 : The Table and its Fields defined according to this CLASS component Attributes have checked!");
		}else{
			System.out.println("Check...5 Error: The Table and its Fields defined according to this CLASS component Attributes have checked FAILED!");
			return;
		}
		System.out.println("Check End: The Table and its Fields definition according to this CLASS Attributes has defined Correctly!");
		
	}
	
	private boolean checkBeanAttrForMySql(HashMap<String, String> fieldMaps, String[] fieldDef){
		//System.out.println( fieldDef[2] +"_"+ fieldDef[3]);
		String selfColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[2] )), false).toLowerCase();
		String dstColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[3] )), false).toLowerCase();
		return !( "int".equals(selfColType)  &&  "int".equals(dstColType) );
	}
	
	private boolean checkIndexedBeanAttrForMySql(HashMap<String, String> fieldMaps, String[] fieldDef){
		//System.out.println(fieldDef[2] +"_"+ fieldDef[3]);
		String selfColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[2] )), false).toLowerCase();
		String dstColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[3] )), false).toLowerCase();
		String indexColType	= getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[4] )), false).toLowerCase();
		return !( "int".equals(selfColType)  &&  "int".equals(dstColType) &&  "int".equals(indexColType) );
	}
	
	private boolean checkBasicAttrForMySql(HashMap<String, String> fieldMaps, String[] fieldDef, String basicType){
		//System.out.println(fieldDef[2] +"_"+ fieldDef[3] + "_" + getFieldMapIngoreKeyCase(fieldMaps, fieldDef[3] ));	
		return checkLinkedListBasicAttrForMysql(fieldMaps, fieldDef, basicType.substring(0, basicType.length()-2 ));
	}
	
	private boolean checkLinkedListBasicAttrForMysql(HashMap<String, String> fieldMaps, String[] fieldDef, String basicType){
		String selfColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[2] )), false).toLowerCase();;
		String dstColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[3] )), true).toLowerCase();;
		return !( "int".equals(selfColType)  &&  basicType!=null && basicType.toLowerCase().equals(dstColType.toLowerCase()) );	
	}
	
	private boolean checkIndexedBasicAttrForMySql(HashMap<String, String> fieldMaps, String[] fieldDef, String basicType){

		String selfColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[2] )), false).toLowerCase();;
		String dstColType = getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[3] )), false).toLowerCase();;
		String indexColType	= getJavaTypeAccordingMysqlType(Integer.parseInt(getFieldMapIngoreKeyCase(fieldMaps, fieldDef[4] )), false).toLowerCase();;
		//System.out.println(fieldDef[2] +"_"+ fieldDef[3] + "_" +fieldTableMaps.size() + "_"+ basicType + "_" +dstColType);
		return !( "int".equals(selfColType)  &&  basicType!=null && basicType.substring(0, basicType.length()-2).toLowerCase().equals(dstColType.toLowerCase()) &&  "int".equals(indexColType) );
	}
	
	private boolean checkComponentDefForMysql(String[] fieldDef){
		boolean flag = false;		
		
		String tName = fieldDef[1];
		System.out.println("\tThe Table: " + tName + " is checking...., " );
		HashMap<String, String> fieldTableMaps = getTableFieldType(tName);
		
		if( !isTableExist(tName) ){
			flag = true;
			System.out.println("\tThe Table: " + tName + " is checking FAILED, cann't FIND the table");
		}
		else{
			try{
				if( fieldDef[0].startsWith("bean")){
					flag = checkBeanAttrForMySql(fieldTableMaps, fieldDef);
				}else{
					String[] infos = fieldDef[0].split("_");
					if("bean".equals(infos[1])){
						if(fieldDef.length == 5) 
							flag = checkIndexedBeanAttrForMySql(fieldTableMaps, fieldDef);
						else
							flag = checkBeanAttrForMySql(fieldTableMaps, fieldDef);
					}else if("basic".equals(infos[1])){
						//System.out.println(tName + "_" +fieldDef.length);
						if(fieldDef.length == 5) 
							flag = checkIndexedBasicAttrForMySql(fieldTableMaps, fieldDef, infos[2]);
						else if(fieldDef[0].startsWith("linkedlist"))
							flag = checkLinkedListBasicAttrForMysql(fieldTableMaps, fieldDef, infos[2]);
						else if(fieldDef[0].startsWith("array"))
							flag = checkBasicAttrForMySql(fieldTableMaps, fieldDef, infos[2]);
					}
				}
			}catch(Exception e){
				flag = true;
				System.out.println("\tThe Table: " + tName + " is checking FAILED, Please Check THE RELATION TABLE DEFINATION and make sure that the table isn't EMPTY!");
			}			
		}
		if( flag ) System.out.println("\t" + tName + " Checking Failed");
		return flag;
	}
	
	
	
	private boolean checkFieldDefForMysql(HashMap<String, String> tableFieldMap, Field f, String[] fieldDef){
		boolean flag = false;
		String fieldType = this.getFieldMapIngoreKeyCase(tableFieldMap, fieldDef[0]);
		String fieldClassName = f.getType().getSimpleName();
		
		//System.out.println(fieldDef[0] + "_" + fieldType + "_" + fieldClassName );//+ getJavaTypeAccordingMysqlType(Integer.parseInt(fieldType)));
		if(fieldClassName.toLowerCase().equals(getJavaTypeAccordingMysqlType(Integer.parseInt(fieldType), false).toLowerCase())){
			//System.out.println(fieldClassName + "_" + getJavaTypeAccordingMysqlType(Integer.parseInt(fieldType)));
			flag = true;
		};
		
		return flag;
	}
	
	private String getJavaTypeAccordingMysqlType(int type, boolean isLinkedList){
		String rst = "";
		switch (type) {  
		    case Types.BIGINT:  
		        rst = "long";  
		        break;  
		    case Types.BOOLEAN:  
		    	rst = "boolean"; 
		        break;  
		    case Types.DATE:  
		        rst = "Date";
		        break;  
		    case Types.DOUBLE:  
		    	rst = "double";
		        break;  
		    case Types.FLOAT:
		    case Types.REAL:
		    	rst = "float";
		        break;  
		    case Types.INTEGER:  
		    	if(isLinkedList)
		        	rst = "Integer";
		    	else
		    		rst = "int";
		        break;  
		    case Types.SMALLINT:  
		    	if(isLinkedList)
		        	rst = "Integer";
		    	else
		    		rst = "int";
		        break;  
		    case Types.TIME:  
		    	rst = "Time";
		        break;  
		    case Types.TIMESTAMP:  
		    	rst = "Timestamp";
		        break;  
		    case Types.TINYINT:  
		    	if(isLinkedList)
		        	rst = "Integer";
		    	else
		    		rst = "int";
		        break;  
		    case Types.VARCHAR:  
		        rst = "String";
		        break;  
		    case Types.NCHAR:  
		    	rst = "String";
		        break;  
		    case Types.NVARCHAR:  
		    	rst = "String"; 
		        break;  
		    case Types.BIT:  
		    	rst = "boolean";
		        break;  
	    }  
		//System.out.println(type + "_" + rst); 
		return rst;
	}
	
	public boolean isTableExist(String tableName){
		Connection conn = this.dboper.getConnection();
		ResultSet rs;
		try {
			DatabaseMetaData meta = (DatabaseMetaData)conn.getMetaData();
			rs = meta.getTables(null, null, tableName, null);
			if(rs.next()){
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkPrimaryKey(){
		boolean flag = false;
		ResultSet rs = null;
		Connection conn = null;
		try{
			conn = this.dboper.getConnection();
			rs = conn.getMetaData().getPrimaryKeys(null,null, this.tableName);
			if (!rs.isAfterLast()) {
				rs.next();
				if("id".equals(rs.getString("COLUMN_NAME").toLowerCase())){
					flag = true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (rs != null)    rs.close();
				if (conn != null)  conn.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		return flag;
	}
	//HashMap key: String, colName; value : String, SQL type
	private HashMap<String, String> getTableFieldType(String table){
		HashMap<String, String> fieldMap = new HashMap<String, String>();
		Connection conn = this.dboper.getConnection();
		String sql = "select * from "+ table + " limit 0, 1";  
		ResultSet rs = null;
		try {
			Statement statement =conn.createStatement();  
		   
			rs = statement.executeQuery(sql);
			if (rs.next()) {  
	            ResultSetMetaData rsmd = rs.getMetaData();  
	            for (int i = 1; i <= rsmd.getColumnCount(); i++) {  
	                String name = String.valueOf(rsmd.getColumnLabel(i));  
	                String fieldType = String.valueOf(rsmd.getColumnType(i));  
	                fieldMap.put(name, fieldType);
	            }  
	        }  		
			
		} catch (SQLException e) {
			System.out.println("Check Table: "+ table +" Field Error");
			e.printStackTrace();
		} finally{
			try{
				if (rs != null)    rs.close();
				if (conn != null)  conn.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
        return fieldMap;
	}
	
	@SuppressWarnings("unchecked")
	private String getFieldMapIngoreKeyCase(HashMap<String, String> fieldMaps, String target){
		String rst = null;
		Iterator<?> iter = fieldMaps.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			if(entry.getKey().toLowerCase().equals(target.toLowerCase())){
				rst = entry.getValue();
				break;
			}
		}
		//System.out.println(target+ "_" +rst);
		return rst;
	}
}
