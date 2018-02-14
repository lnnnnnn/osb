package edu.nuist.osbank.abenablebean.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.DBEnableBeanSqlConstructor;
import edu.nuist.osbank.dbenablebean.DBImpl;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.Relation;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;




public class DBTableInitor {
	Class<?> c = null;
	Field[] checkfields = null;
	String tableName = "";
	DBTableCheckor dbCheckor = new DBTableCheckor();
	SP annotation = null;
	DBImpl dboper = new DBImpl();
	
	@SuppressWarnings("unchecked")
	public DBTableInitor(Class<?> c, DBEnableBean bean){
		this.c = c;
		this.checkfields = c.getDeclaredFields();  
		if (c.isAnnotationPresent(SP.class)) {
			annotation = (SP) c.getAnnotation(SP.class);
			this.tableName = annotation.table();
		}
		
		if(this.c == null || this.checkfields==null || this.checkfields.length==0 || this.tableName.equals("")){
			System.out.println("Initor Error: The DBEnableBean Class is null or its' Attributes defined error or the TABLENAME according to the Class isn't defined!");
			return;
		}
		//check and init table ACCording the SP definition
		if( !dbCheckor.isTableExist(this.tableName)){
			String sql = this.annotation.create();
			if("".equals(sql)){
				System.out.println("Initor Error: The DBEnableBean Class Create TABLE SQL isn't defined!");
				return;
			}
			//System.out.println(sql);
			this.createTable(sql);
		}
		//check and init the relation tables of component attributes of the class
		HashMap<Field, String[]> componentFields = DBEnableBeanSqlConstructor.getComponentFieldPrepareForSql(this.checkfields, false);
		Iterator<?> iter = componentFields.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
			Field field = entry.getKey();
			String[] fieldDef = entry.getValue();
			initRelationTable(field, fieldDef);
		}
		bean.persist();	
		
		System.out.println("Initor Ended: The Table of this Class: " + c.getName() + " has inited, and the example bean has inserted into these tables. ");
		
	}
	
	//	String[0] for {bean, array_bean, linkedlist_bean, array_basic, linkedlist_basic}, 
		//  String[1] for tableName	
		//  String[2] for selfColName
		//  String[3] for dstColName
		//  String[4] for indexColName
	private void initRelationTable(Field f, String[] fieldDef){
		
		String dColType ="";
		if (f.isAnnotationPresent(Relation.class)) {
			Relation annotation = (Relation) f.getAnnotation( Relation.class );
			dColType = annotation.dType();
		}
		if(fieldDef[0].startsWith("bean")){
			if(!dbCheckor.isTableExist(fieldDef[1])){
				String sql = "create table " + fieldDef[1] + " ( "+ fieldDef[2] +" int(11) NOT NULL, "+ fieldDef[3] +" int(11) NOT NULL ) ENGINE = MYISAM;";
				createTable(sql);
			}
		}else{
			String[] infos = fieldDef[0].split("_");
			if("bean".equals(infos[1])){
				if(fieldDef.length == 5) // bean array and indexed
				{
					if(!dbCheckor.isTableExist(fieldDef[1])){
						String sql = "create table " + fieldDef[1] + " ( "+ fieldDef[2] +" int(11) NOT NULL, "+ fieldDef[3] +" int(11) NOT NULL, "+ fieldDef[4] +" int(11) NOT NULL ) ENGINE = MYISAM;";
						System.out.println(sql);
						createTable(sql);
					}
				}else{ // bean array and not indexed
					if(!dbCheckor.isTableExist(fieldDef[1])){
						String sql = "create table " + fieldDef[1] + " ( "+ fieldDef[2] +" int(11) NOT NULL, "+ fieldDef[3] +" int(11) NOT NULL ) ENGINE = MYISAM;";
						System.out.println(sql);
						createTable(sql);
					}
				}	
			}else if("basic".equals(infos[1])){
				if(fieldDef.length == 5) // basic array and indexed
				{
					if( "".equals(dColType)) dColType = getDefaultTypeAccordingAttrClass(f, fieldDef[0].startsWith("linkedlist"));
					if(!dbCheckor.isTableExist(fieldDef[1])){
						String sql = "create table " + fieldDef[1] + " ( "
										+ fieldDef[2] +" int(11) NOT NULL, "
										+ fieldDef[3] +" "+ dColType +" NOT NULL , "
										+ fieldDef[4] +" int(11) NOT NULL ) ENGINE = MYISAM;";
						System.out.println(sql);
						createTable(sql);
					}
				}else{
					if( "".equals(dColType)) dColType = getDefaultTypeAccordingAttrClass(f, fieldDef[0].startsWith("linkedlist"));
					if(!dbCheckor.isTableExist(fieldDef[1])){
						String sql = "create table " + fieldDef[1] + " ( "
										+ fieldDef[2] +" int(11) NOT NULL, "
										+ fieldDef[3] +" "+ dColType +" NOT NULL ) ENGINE = MYISAM;";
						System.out.println(sql);
						createTable(sql);
					}
				}	
			}
		}
		
	}
	
	private String getDefaultTypeAccordingAttrClass(Field f, boolean isLinkedList){
		String rst = "";
		String simpleName = "";
		if(isLinkedList){
			simpleName = FieldClassHelper.getListFieldGenericTypeClassSimpleName(f);
		}else{
			simpleName = f.getGenericType().getTypeName();
			simpleName = simpleName.substring(0, simpleName.length()-2);
		}

		switch(simpleName.toLowerCase()){
			case "int":
			case "integer" :
				rst = " int(11) ";
				break;
			case "float":
				rst = " FLOAT ";
				break;
			case "date":
			case "java.sql.date" :
			case "java.util.date" :	
				rst = " DATE ";
				break;
			case "double" :
				rst = " DOUBLE ";
				break;
			case "string" :
			case "java.lang.string" :
				rst = " VARCHAR( 255 ) ";
				break;	
			case "boolean" :
				rst = " BOOL ";
				break;	
		}
		//System.out.println( simpleName + "_" + rst );
		return rst;
	}
	
	private void createTable(String sql){
		Connection conn = dboper.getConnection();
		try {
			Statement statement =conn.createStatement();  
			statement.execute(sql);
		} catch (SQLException e) {
			System.out.println("Initor...Error: Create Table: "+ this.tableName +" Error!");
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
