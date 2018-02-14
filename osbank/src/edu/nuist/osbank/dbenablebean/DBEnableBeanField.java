package edu.nuist.osbank.dbenablebean;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

import edu.nuist.osbank.abenablebean.util.FieldClassHelper;
import edu.nuist.osbank.abenablebean.util.WrapType;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.ColDef;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.Relation;




public class DBEnableBeanField {
	
	
	@SuppressWarnings({ "rawtypes"})
	private static WrapType getValueFromRs(String type, String colName, ResultSet rs) throws Exception{
		WrapType tmp = null;
			switch(type.toLowerCase()){
				case "int" :
				case "integer" :
					tmp = new WrapType<Integer>("int", rs.getInt(colName));
					break;
				case "long" :
					System.out.println((Long)rs.getLong(colName));
					tmp = new WrapType<Long>("long", (Long)rs.getLong(colName));
					break;
				case "float" :
					tmp = new WrapType<Float>("float", rs.getFloat(colName));
					break;
				case "double" :
					tmp = new WrapType<Double>("double", rs.getDouble(colName));
					break;
				case "boolean" :
					tmp = new WrapType<Boolean>("boolean", rs.getBoolean(colName));
					break;
				case "string" :
					tmp = new WrapType<String>("String", rs.getString(colName));
					break;
				case "date" :
					Date date = rs.getDate(colName); 
					tmp = new WrapType<Date>("Date", date);
					break;
				case "timestamp" :
					Timestamp stamp = rs.getTimestamp(colName); 
					tmp = new WrapType<Date>("Timestamp", stamp);
					break;
				default:
					System.out.println(type);
			}
		return tmp;
	}
	
	@SuppressWarnings("rawtypes")
	public static WrapType getBasicAttrFromRs(Field f, ResultSet rs) throws Exception{
		String colName;
		if(f.isAnnotationPresent(ColDef.class)){
			 ColDef col = f.getAnnotation(ColDef.class);
			 if(col.off())
				 return null;
			 else{
				 colName = col.value();			 
				 return getValueFromRs(f.getType().getSimpleName(), colName, rs);
			 }
		}else{
			colName = f.getName();
			return getValueFromRs(f.getType().getSimpleName(), colName, rs);
		}		
	}
	

	@SuppressWarnings("rawtypes")
	public static LinkedList<WrapType> getBasicLinkedListAttrFromRs(Field f, ResultSet rs) throws Exception{
		String rTableName;
		String selfColName;
		String dstColName;
		String indexColName;
		
		String fieldTypeName = f.getType().getSimpleName();
		String basicType = fieldTypeName.substring(0, fieldTypeName.length()-2);
		if(!FieldClassHelper.isBasicType(basicType)){
			if(FieldClassHelper.isDBEnableBeanArray(f))
				basicType = "int";
			else{				
				Class c = FieldClassHelper.getListFieldGenericTypeClass(f);
				if(FieldClassHelper.isDBEnableBean(c)){
					basicType = "int";
				}else{
					basicType = FieldClassHelper.getListFieldGenericTypeClassSimpleName(f).toLowerCase();
					if(!FieldClassHelper.isBasicType(basicType)) return null;
				}				
			}
		}
		
		if(f.isAnnotationPresent(Relation.class)){
			Relation r = f.getAnnotation(Relation.class);
			rTableName = r.rTable();
			selfColName = r.sIdCol();
			dstColName = r.dIdCol();
			indexColName = r.indexCol();
			
			String sql = "select "+ dstColName +" from "+ rTableName +" where " + selfColName + "=" + getValueFromRs("int", "id", rs).getValue();
			if(r.indexed()) 
				sql = "select "+ dstColName +", "+ indexColName +" from "+ rTableName +" where " + selfColName + "=" + getValueFromRs("int", "id", rs).getValue();			
			//System.out.println(sql);
			DBImpl dboper = new DBImpl();
			Connection conn = dboper.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs_tmp = stmt.executeQuery(sql);
			
			LinkedList<WrapType> l = new LinkedList<WrapType>();
			while(rs_tmp.next()){
				WrapType w = getValueFromRs(basicType, dstColName, rs_tmp);
				if(r.indexed()){
					w.setIndex( (Integer)getValueFromRs("int", indexColName, rs_tmp).getValue() );
				} 
				l.add(w);
			}
			
			try{
				stmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			if(r.indexed()) WrapType.sortWrapBasicTypeByIndex(l);
			return l;
		}
		return null;
	}
	

	@SuppressWarnings("rawtypes")
	public static WrapType[] getBasicArrayAttrFromRs(Field f, ResultSet rs) throws Exception{
		LinkedList<WrapType> l = getBasicLinkedListAttrFromRs( f, rs);
		WrapType[] re = null;
		if( l!=null && l.size()>0 ){
			re = new WrapType[l.size()];
			int index = 0;
			for(WrapType w : l){
				re[index++] = w;	
			}
		}
		return re;
	}	

	public static DBEnableBean getDBEnableBeanAttrFromRs(Field f, ResultSet rs) throws Exception{

		DBEnableBean tmp = null;
		if(f.isAnnotationPresent(Relation.class)){
			Relation r = f.getAnnotation(Relation.class);
			String rTableName = r.rTable();
			String selfColName = r.sIdCol();
			String dstColName = r.dIdCol();

			String sql = "select "+ dstColName +" from "+ rTableName +" where " + selfColName + "=" + getValueFromRs("int", "id", rs).getValue();
			//System.out.println( sql );
			DBImpl dboper = new DBImpl();
			try{
				Connection conn = dboper.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs_tmp = stmt.executeQuery(sql);
				
				if(rs_tmp.next()){
					int relatinBeanId;
					relatinBeanId = rs_tmp.getInt(dstColName);
					Class<?> c = f.getType();
					tmp = ((DBEnableBean)c.newInstance()).setId(relatinBeanId);
				}
				stmt.close();
				conn.close();
			}catch (Exception e) {
		        e.printStackTrace();
		    }
			
				
		}		
		return tmp;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static LinkedList<DBEnableBean> getDBEnableBeanLinkedListAttrFromRs(Field f, ResultSet rs) throws Exception{
		
		Class<?> fieldCLass = FieldClassHelper.getListFieldGenericTypeClass(f);

		if( fieldCLass == null)
			return null;
		LinkedList<WrapType> beanIds = (LinkedList<WrapType>)(getBasicLinkedListAttrFromRs(f, rs));
		LinkedList<DBEnableBean> beans = null;
		
		if(beanIds != null && beanIds.size()>0){
			beans = new LinkedList<DBEnableBean>();
			for(WrapType i : beanIds){
				DBEnableBean tmp = (DBEnableBean)fieldCLass.newInstance();
				tmp.setId((int)(i.getValue()));				
				beans.add(tmp);
			}
		}
		return beans;
	}
	
	public static DBEnableBean[] getDBEnableBeanArrayAttrFromRs(Field f, ResultSet rs) throws Exception{
		LinkedList<DBEnableBean> beans =  getDBEnableBeanLinkedListAttrFromRs( f, rs);
		if( beans!=null && beans.size()>0 ){
			DBEnableBean[] beanArray = new DBEnableBean[beans.size()];
			int index = 0;
			for(DBEnableBean bean : beans){
				beanArray[index++] = bean;
			}
			return beanArray;
		}		
		return null;
	}
	
}
