package edu.nuist.osbank.dbenablebean;



import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import edu.nuist.osbank.abenablebean.util.FieldClassHelper;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.ColDef;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.Relation;



public class DBEnableBeanSqlConstructor {
		//String[]: String[0] for ColName, String[1] for Type
	 	public static HashMap<Field, String[]> getBasicFieldPrepareForSql(Field[] fields){
			HashMap<Field, String[]> tmp = new HashMap<Field, String[]>();
			
			for(Field f : fields){			
				//get the colName according to the field
				String colName = "";
				if(f.isAnnotationPresent(ColDef.class)){
					 ColDef col = f.getAnnotation(ColDef.class);
					 if(col.off()) continue;
					 else if( "".equals(colName=col.value()) ) colName=f.getName() ;
				}else
					colName = f.getName();
				
				String simpleName = f.getType().getSimpleName();				
				if(FieldClassHelper.isBasicType(simpleName)){
					//System.out.println(colName + "_" + simpleName);
					tmp.put(f, new String[]{ colName, simpleName });
				}else
					continue;			
			}
			return tmp;		
		}
	 	
	 	private static Method getMethod(Field f, DBEnableBean bean, String  type) throws NoSuchMethodException, SecurityException{
	 		String getter = "";
	 		
	 		if(type.equals("boolean"))
	 			getter = "is"+ f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
	 		else
	 			getter = "get"+ f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1); 
	 		Method m = null;
	 		m = bean.getClass().getMethod(getter);
	 		return m;
	 	}
	 	
	 	private static void setParameters(PreparedStatement insert, String[] types, Object[] fieldValues) throws SQLException{
	 		for(int i=0; i<types.length; i++){
	 			//System.out.println(types[i] + "_" + fieldValues[i]);
	 			switch(types[i].toLowerCase()){
	 				case "int" :
	 					insert.setInt(i+1, (int)fieldValues[i]);
	 					break;
	 				case "long" :
	 					insert.setLong(i+1, (long)fieldValues[i]);
						break;
	 				case "string" :
	 					insert.setString(i+1, (String)fieldValues[i]);
	 					break;	 				
	 				case "double" :
	 					insert.setDouble(i+1, (double)fieldValues[i]);
	 					break;	 					
	 				case "float" :
	 					insert.setFloat(i+1, (float)fieldValues[i]);
	 					break;	
	 				case "boolean" :
	 					insert.setBoolean(i+1, (boolean)fieldValues[i]);
	 					break;
	 				case "date" :
	 					insert.setDate(i+1, (Date)fieldValues[i]);
	 					break;
	 				case "timestamp":
	 					insert.setTimestamp(i+1, (Timestamp)fieldValues[i]);
	 					break;
	 				default:
	 					System.out.println( types[i] + "_" + fieldValues[i] );
	 			}
	 		}	
	 		
	 	}
		
		@SuppressWarnings("unchecked")
		public static PreparedStatement getInsertBeanStatement(DBEnableBean bean, Connection conn, String tableName, HashMap<Field, String[]> fieldMap) throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			Iterator<?> iter = fieldMap.entrySet().iterator();
			
			String sql = "insert into "+tableName+" (";
			String values = " values ( ";
			String[] types = new String[fieldMap.size()];
			Object[] fieldValues = new Object[fieldMap.size()];
			
			int index = 0;
			while (iter.hasNext()) {
				Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
				Field key = entry.getKey();
				String[] val = entry.getValue();
				sql += val[0] +", ";
				values += "?, ";
				types[ index ] = val[1];
				Method m = getMethod(key, bean, val[1]);
				fieldValues[ index ] = m.invoke(bean);
				if( fieldValues[ index ] == null)
					if( types[index].equals("Date") )
						fieldValues[ index ] = java.sql.Date.valueOf("1970-09-01");
					else if( types[index].equals("String") )
						fieldValues[ index ] = "";
				index++;
			}
			sql = sql.substring(0, sql.length()-2) + " )" + values.substring(0, values.length()-2) + " )";
			
		//	System.out.println("DBenablebeanSqlConstructor:125");
		//	System.out.println(sql);
			PreparedStatement insert = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setParameters(insert, types, fieldValues);
			return insert;
		} 
		
		private static Method getMethodForComponent(Field f, DBEnableBean bean, String typeDef) throws NoSuchMethodException, SecurityException, ClassNotFoundException{
			Class<?> c = bean.getClass();
			String fieldName = f.getName();
			String getter = "get"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method m = c.getMethod( getter );
			return m;
		}
		
		@SuppressWarnings("unchecked")
		private static LinkedList<PreparedStatement> getPreparedStatementForComponent(DBEnableBean bean, Connection conn, Method m, String[] val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
			String typeDef = val[0];
			LinkedList<PreparedStatement> statements = new LinkedList<PreparedStatement>();
			//System.out.println( val[0] + "," + val[1] + "," + val[2] ); //val[4] is index colName
			
			boolean indexed = ((val.length==5) ? true : false); 			
			if(m.invoke(bean) == null) return statements;
			if(typeDef.equals("bean")){
				DBEnableBean beanAttr = (DBEnableBean)m.invoke(bean);
				String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
				PreparedStatement insert = conn.prepareStatement(sql);
				insert.setInt(1, bean.getId());
				insert.setInt(2, beanAttr.getId());
				statements.add(insert);
			}else if(typeDef.startsWith("array")){
				String realType = typeDef.split("_")[1];
				if(realType.equals("bean")){
					DBEnableBean[] beanAttrs = (DBEnableBean[])m.invoke(bean);
					for(int i=0; i<beanAttrs.length; i++){
						if(indexed){
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +", "+ val[4] +" ) values ( ?, ?, ?)";
							
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setInt(2, beanAttrs[i].getId());
							insert.setInt(3, i);
							statements.add(insert);
						}else{
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setInt(2, beanAttrs[i].getId());
							statements.add(insert);
						}
					}
				}else if(realType.equals("basic")){
					
					String basicType = typeDef.split("_")[2].substring(0, typeDef.split("_")[2].length()-2);
					String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
					if(indexed)
						sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +", "+ val[4] +" ) values ( ?, ?, ?)";			
					//System.out.println(sql);
					switch(basicType){						
						case "int" : {
							int[] attrs = (int[])m.invoke(bean);
							for(int i=0; i<attrs.length; i++){
								PreparedStatement insert = conn.prepareStatement(sql);
								insert.setInt(1, bean.getId());
								insert.setInt(2, attrs[i]);
								if(indexed) insert.setInt(3, i);
								statements.add(insert);
							}}
							break;
						case "double" :{
							double[] attrs = (double[])m.invoke(bean);
							for(int i=0; i<attrs.length; i++){
								PreparedStatement insert = conn.prepareStatement(sql);
								insert.setInt(1, bean.getId());
								insert.setDouble(2, attrs[i]);
								if(indexed) insert.setInt(3, i);
								statements.add(insert);
							}}
							break;
						case "float" : {
							float[] attrs = (float[])m.invoke(bean);
							for(int i=0; i<attrs.length; i++){
								PreparedStatement insert = conn.prepareStatement(sql);
								insert.setInt(1, bean.getId());
								insert.setFloat(2, attrs[i]);
								if(indexed) insert.setInt(3, i);
								statements.add(insert);
							}}
							break;
						case "string" : {
							String[] attrs = (String[])m.invoke(bean);
							for(int i=0; i<attrs.length; i++){
								PreparedStatement insert = conn.prepareStatement(sql);
								insert.setInt(1, bean.getId());
								insert.setString(2, attrs[i]);
								if(indexed){ insert.setInt(3, i);} 
								statements.add(insert);
							}}
							break;
						case "boolean" :  {
							boolean[] attrs = (boolean[])m.invoke(bean);
							for(int i=0; i<attrs.length; i++){
								PreparedStatement insert = conn.prepareStatement(sql);
								insert.setInt(1, bean.getId());
								insert.setBoolean(2, attrs[i]);
								if(indexed) insert.setInt(3, i);
								statements.add(insert);
							}}
							break;
						case "date" :  {
							Date[] attrs = (Date[])m.invoke(bean);
							for(int i=0; i<attrs.length; i++){
								PreparedStatement insert = conn.prepareStatement(sql);
								insert.setInt(1, bean.getId());
								insert.setDate(2, attrs[i]);
								if(indexed) insert.setInt(3, i);
								statements.add(insert);
							}}
							break;
					}
				}
			}else if( typeDef.startsWith("linkedlist")){
				if(m.invoke(bean) == null) return statements;
				String realType = typeDef.split("_")[1];
				if(realType.equals("bean")){
					LinkedList<DBEnableBean> beanAttrs = (LinkedList<DBEnableBean>)m.invoke(bean);
					for(DBEnableBean beanAttr : beanAttrs){
						String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";						
						PreparedStatement insert = conn.prepareStatement(sql);
						insert.setInt(1, bean.getId());
						insert.setInt(2, beanAttr.getId());
						statements.add(insert);
					}
				}else if(realType.equals("basic")){
					switch(typeDef.split("_")[2]){
					case "int" :
					case "integer" : {
						LinkedList<Integer> attrs = (LinkedList<Integer>)m.invoke(bean);
						for(int attr : attrs){
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setInt(2, attr);
							statements.add(insert);
						}}
						break;
					case "double" :{
						LinkedList<Double> attrs = (LinkedList<Double>)m.invoke(bean);
						for(double attr : attrs){
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setDouble(2, attr);
							statements.add(insert);
						}}
						break;
					case "float" : {
						LinkedList<Float> attrs = (LinkedList<Float>)m.invoke(bean);
						for(float attr : attrs){
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setFloat(2, attr);
							statements.add(insert);
						}}
						break;
					case "string" : {
						LinkedList<String> attrs = (LinkedList<String>)m.invoke(bean);
						for(String attr : attrs){
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setString(2, attr);
							statements.add(insert);
						}}
						break;
					case "boolean" :  {
						LinkedList<Boolean> attrs = (LinkedList<Boolean>)m.invoke(bean);
						for(boolean attr : attrs){
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setBoolean(2, attr);
							statements.add(insert);
						}}
						break;
					case "date" :  {
						LinkedList<Date> attrs = (LinkedList<Date>)m.invoke(bean);
						for(Date attr : attrs){
							String sql = "insert into " + val[1] + " ( "+ val[2] +", "+ val[3] +" ) values ( ?, ? )";
							PreparedStatement insert = conn.prepareStatement(sql);
							insert.setInt(1, bean.getId());
							insert.setDate(2, attr);
							statements.add(insert);
						}}
						break;
					}
				}
			}
			return statements;
		}
		
		
		@SuppressWarnings("unchecked")
		public static LinkedList<PreparedStatement> getInsertRelationStatement(DBEnableBean bean, Connection conn, HashMap<Field, String[]> fieldMap) throws NoSuchMethodException, SecurityException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
			
			Iterator<?> iter = fieldMap.entrySet().iterator();
			LinkedList<PreparedStatement> rst = new LinkedList<PreparedStatement>();
			
			while (iter.hasNext()) {
				Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
				Field key = entry.getKey();
				String[] val = entry.getValue();
				
				Method m = getMethodForComponent(key, bean, val[0]);
				rst.addAll(getPreparedStatementForComponent(bean, conn, m, val));
			}
			return rst;
		}
		
		public static HashMap<Field, String[]> getComponentFieldPrepareForSql(Field[] fields){
			return getComponentFieldPrepareForSql(fields, true);
		}
		
		//String[]: 
		//	String[0] for {bean, array_bean, linkedlist_bean, array_basic, linkedlist_basic}, 
		//  String[1] for tableName	
		//  String[2] for selfColName
		//  String[3] for dstColName
		//  String[4] for indexColName
		//  when onlyDealFlag is true, the map will contain the attribute annotated by coDeal=true;  
		public static HashMap<Field, String[]> getComponentFieldPrepareForSql(Field[] fields, boolean onlyDealFlag){
			HashMap<Field, String[]> tmp = new HashMap<Field, String[]>();
			
			for(Field f : fields){		
				
				if(f.isAnnotationPresent(ColDef.class)){
					 ColDef col = f.getAnnotation(ColDef.class);
					 if(col.off()) continue;
				}
							
				Class<?> c = f.getType();
				String simpleName = c.getSimpleName();
				
				if(FieldClassHelper.isBasicType(simpleName)){
					continue;
				}else{
					String rTableName;
					String selfColName;
					String dstColName;
					String indexColName;
					boolean indexed;
					if(f.isAnnotationPresent(Relation.class)){
						Relation r = f.getAnnotation(Relation.class);
						rTableName = r.rTable();
						selfColName = r.sIdCol();
						dstColName = r.dIdCol();
						indexColName = r.indexCol();
						indexed = r.indexed();
						
						if( !r.coDeal() && onlyDealFlag) continue;
					}else continue;
					
					if(FieldClassHelper.isDBEnableBean(c)){
						tmp.put( f, new String[]{ "bean", rTableName, selfColName, dstColName } );
					}else if(FieldClassHelper.isBasicTypeArray(simpleName)){
						if(indexed){
							tmp.put( f, new String[]{ "array_basic_"+simpleName.toLowerCase(), rTableName, selfColName, dstColName, indexColName } );							
						}else{
							tmp.put( f, new String[]{ "array_basic_"+simpleName.toLowerCase(), rTableName, selfColName, dstColName } );							
						}							
					}else if(FieldClassHelper.isDBEnableBeanArray(f)){
						if(indexed){
							tmp.put( f, new String[]{ "array_bean_"+simpleName.toLowerCase(), rTableName, selfColName, dstColName, indexColName } );														
						}else{
							tmp.put( f, new String[]{ "array_bean", rTableName, selfColName, dstColName } );							
						}
					}else if(FieldClassHelper.isListGenericType(c)){
						String gSimpleName = FieldClassHelper.getListFieldGenericTypeClassSimpleName(f);
						if(FieldClassHelper.isBasicType(gSimpleName)){
							tmp.put( f, new String[]{ "linkedlist_basic_"+gSimpleName.toLowerCase(), rTableName, selfColName, dstColName } );
						}else if(FieldClassHelper.isDBEnableBean(FieldClassHelper.getListFieldGenericTypeClass(f))){
							tmp.put( f, new String[]{ "linkedlist_bean", rTableName, selfColName, dstColName } );
						}
					}
				}
			}
			return tmp;
		}
		
		@SuppressWarnings("unchecked")
		public static PreparedStatement getUpdateBeanStatement(DBEnableBean bean, Connection conn, String tableName, HashMap<Field, String[]> fieldMap) throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			Iterator<?> iter = fieldMap.entrySet().iterator();
			
			String sql = "update "+tableName+" set ";
			String end = " where id=?";
			String[] types = new String[fieldMap.size() + 1];
			Object[] fieldValues = new Object[fieldMap.size() + 1];
			int index = 0;
			while (iter.hasNext()) {
				Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
				Field key = entry.getKey();
				String[] val = entry.getValue();
				sql += val[0] +"=?, ";
				types[ index ] = val[1];
				Method m = getMethod(key, bean, val[1]);
				fieldValues[ index ] = m.invoke(bean);
				index++;
			}
			
			types[types.length - 1] = "int";
			fieldValues[fieldValues.length - 1] = bean.getId();

			sql = sql.substring(0, sql.length()-2) + end;
			//System.out.println(sql);
			PreparedStatement insert = conn.prepareStatement(sql);
			setParameters(insert, types, fieldValues);
			return insert;
		} 
		
		//String[]: 
		//	String[0] for {bean, array_bean, linkedlist_bean, array_basic, linkedlist_basic}, 
		//  String[1] for tableName	
		//  String[2] for selfColName
		//  String[3] for dstColName
		
		@SuppressWarnings("unchecked")
		public static void deleteAllRelationBeforeUpdate(DBEnableBean bean, Connection conn, HashMap<Field, String[]> componentFeldMap) throws SQLException{
			Iterator<?> iter = componentFeldMap.entrySet().iterator();
			conn.setAutoCommit(false);
			while (iter.hasNext()) {
				Map.Entry<Field, String[]> entry = (Map.Entry<Field, String[]>) iter.next();
				String[] val = entry.getValue();
				
				String sql = "delete from " + val[1] + " where " + val[2] + "=?";
				
				PreparedStatement delete = conn.prepareStatement(sql);
				delete.setInt(1, bean.getId());
				delete.executeUpdate();
			}
			conn.commit();
		}
		
}
