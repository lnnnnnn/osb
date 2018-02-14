package edu.nuist.osbank.dbenablebean;



import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;

import edu.nuist.osbank.abenablebean.util.FieldClassHelper;
import edu.nuist.osbank.abenablebean.util.Logger;
import edu.nuist.osbank.abenablebean.util.WrapType;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.ColDef;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.DBExtendsField;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.Relation;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;



public abstract class DBEnableBean {
	private Log logger = Logger.getLogger();
	private StringWriter sw = new StringWriter();
	private Connection conn = null; 
	private DBImpl dboper = new DBImpl();
	private Class<?> classType = null;
	private Properties storeProcesses = new Properties();
	private String tableName = "";
	private boolean loaded = false;	
	
	private int id = -1;
	private final String getById = "select * from {tableName} where id={id}";	
	
	public DBEnableBean(){
		this.initStoreProcessAndTableName();
		this.storeProcesses.setProperty("getById", this.getById);
		
	}
	//定义此函数 让题型类重写此方法 便于在数组中调用
	public String getType(){
		return "";
	}
	@SuppressWarnings("rawtypes")
	private  void setBasicAttr(DBEnableBean bean, String fieldName, WrapType w){
		String setMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		Class<?> c = FieldClassHelper.getBasicClass(w.getType());
		try {
			
			Method setter = null;
			try{
				setter = bean.getClass().getDeclaredMethod(setMethod, c);
			}catch(Exception e){
				setter = bean.getClass().getSuperclass().getDeclaredMethod(setMethod, c);
			}
			
			setter.invoke(bean, w.getValue());
		} catch (Exception e) {
			logger.error(this.classType.getName() + ": set Basic Attribute : "+ fieldName +" error!\n" + getExceptionString(e));
		}		
	}
	
	
	
	@SuppressWarnings({ "rawtypes" })
	private void setBasicAttrArry(DBEnableBean bean, String fieldName, WrapType[] w){
		String setMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		String basicType = w[0].getType();
		try {
			switch(basicType){
				case "int" : {
						int[] array = new int[w.length];
						for(int index=0; index<array.length; index++)
							array[index] = (Integer)w[index].getValue();
						Method setter = bean.getClass().getDeclaredMethod(setMethod, int[].class);
						setter.invoke(bean, array);
				}
				break;
				case "float" : {
					float[] array = new float[w.length];
					for(int index=0; index<array.length; index++)
						array[index] = (Float)w[index].getValue();
					Method setter = bean.getClass().getDeclaredMethod(setMethod, float[].class);
					setter.invoke(bean, array);
				}
				break;
				case "double" : {
					double[] array = new double[w.length];
					for(int index=0; index<array.length; index++)
						array[index] = (Double)w[index].getValue();
					Method setter = bean.getClass().getDeclaredMethod(setMethod, double[].class);
					setter.invoke(bean, array);
				}
				break;
				case "boolean" : {
					boolean[] array = new boolean[w.length];
					for(int index=0; index<array.length; index++)
						array[index] = (Boolean)w[index].getValue();
					Method setter = bean.getClass().getDeclaredMethod(setMethod, boolean[].class);
					setter.invoke(bean, array);
				}
				break;
				case "String" : {
					String[] array = new String[w.length];
					for(int index=0; index<array.length; index++)
						array[index] = (String)w[index].getValue();
					Method setter = bean.getClass().getDeclaredMethod(setMethod, String[].class);
					setter.invoke(bean, (Object)array);
				}
				break;
				case "Date" : {
					java.sql.Date[] array = new java.sql.Date[w.length];
					for(int index=0; index<array.length; index++)
						array[index] = (java.sql.Date)w[index].getValue();
					Method setter = bean.getClass().getDeclaredMethod(setMethod, java.sql.Date[].class);
					setter.invoke(bean, (Object)array);
				}
				break;
			}
		} catch (Exception e) {
			logger.error(this.classType.getName() + ": set Basic Attribute Array: "+ fieldName +" error!\n" + getExceptionString(e));
		}
		
	}	
	

	@SuppressWarnings("rawtypes")
	private void setBasicAttrLinkedList(DBEnableBean bean, String fieldName, LinkedList<WrapType> l){
		String setMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		
		String basicType = l.get(0).getType();
		try {
			switch(basicType){
				case "int" : {
					LinkedList<Integer> tmp = new LinkedList<Integer>();
					for(WrapType w : l) tmp.add((Integer)w.getValue());
					Method  setter = bean.getClass().getDeclaredMethod(setMethod, LinkedList.class);
					setter.invoke(bean, tmp);
				}
				break;
				case "float" : {
					LinkedList<Float> tmp = new LinkedList<Float>();
					for(WrapType w : l) tmp.add((Float)w.getValue());
					Method  setter = bean.getClass().getDeclaredMethod(setMethod, LinkedList.class);
					setter.invoke(bean, tmp);
				}
				break;
				case "double" : {
					LinkedList<Double> tmp = new LinkedList<Double>();
					for(WrapType w : l) tmp.add((Double)w.getValue());
					Method  setter = bean.getClass().getDeclaredMethod(setMethod, LinkedList.class);
					setter.invoke(bean, tmp);
				}
				break;
				case "boolean" : {
					LinkedList<Boolean> tmp = new LinkedList<Boolean>();
					for(WrapType w : l) tmp.add((Boolean)w.getValue());
					Method  setter = bean.getClass().getDeclaredMethod(setMethod, LinkedList.class);
					setter.invoke(bean, tmp);
				}
				break;
				case "String" : {
					LinkedList<String> tmp = new LinkedList<String>();
					for(WrapType w : l) tmp.add((String)w.getValue());
					Method  setter = bean.getClass().getDeclaredMethod(setMethod, LinkedList.class);
					setter.invoke(bean, tmp);
				}
				break;
				case "Date" : {
					LinkedList<Date> tmp = new LinkedList<Date>();
					for(WrapType w : l) tmp.add((Date)w.getValue());
					Method  setter = bean.getClass().getDeclaredMethod(setMethod, LinkedList.class);
					setter.invoke(bean, tmp);
				}
				break;
			}
		} catch (Exception e) {
			logger.error(this.classType.getName() + ": set Basic Attribute LinkedList: "+ fieldName +" error!\n" + getExceptionString(e));
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DBEnableBean getBeanFromResult(ResultSet rs){
		Object newInstance = null;
		try{
			newInstance = this.classType.newInstance();
		}catch(Exception e){
			logger.error(this.classType.getName() + ": getBeanFromResult : produce an new Instance error!\n");			
		}
		
		Field[] fields = this.getClass().getDeclaredFields();  	
		Field[] fatherFields = this.getClass().getSuperclass().getDeclaredFields();
		LinkedList<Field> tmpFields = new LinkedList<Field>();
		for(Field f : fields){ tmpFields.add(f); }
		for(Field f : fatherFields){
			if( f.isAnnotationPresent(DBExtendsField.class)){
				tmpFields.add( f ); 
			}
		}
		
		Field[] rstFields = new Field[ tmpFields.size() ];
		for(int i=0; i<rstFields.length; i++) rstFields[i] = tmpFields.get(i);
		
		for(Field f : rstFields){
			
			if(f.isAnnotationPresent(ColDef.class)){
				 ColDef col = f.getAnnotation(ColDef.class);
				 if(col.off()) continue;
			}
			
			String fieldName = f.getName();
			Class<?> fieldClass = f.getType();					
			String simpleName = fieldClass.getSimpleName();
			
			try{
				if(FieldClassHelper.isBasicType(simpleName)){
					WrapType attr = DBEnableBeanField.getBasicAttrFromRs(f, rs);
					if(attr == null) continue;
					this.setBasicAttr((DBEnableBean)newInstance, fieldName, attr);
				}else if(FieldClassHelper.isBasicTypeArray(simpleName)){
					WrapType[] attrs = DBEnableBeanField.getBasicArrayAttrFromRs(f, rs);
					if(attrs==null || attrs.length==0 ) continue;
					setBasicAttrArry((DBEnableBean)newInstance, fieldName, attrs);
					
				}else if(FieldClassHelper.isDBEnableBean(fieldClass)){
					DBEnableBean bean = DBEnableBeanField.getDBEnableBeanAttrFromRs(f, rs);
					if(bean == null) continue;
					String setMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					try {
						Method setter = classType.getDeclaredMethod(setMethod, fieldClass);
						setter.invoke((DBEnableBean)newInstance, f.getType().cast(bean));						
					}catch(Exception e){
						logger.error(this.classType.getName() + ": set Attribute Array: "+ fieldName +" error!\n" + getExceptionString(e));						
					}
					
				}else if(FieldClassHelper.isDBEnableBeanArray(f)){
					DBEnableBean[] beans = DBEnableBeanField.getDBEnableBeanArrayAttrFromRs(f, rs);
					if(beans==null || beans.length == 0) continue;
					String declaringName = f.getGenericType().getTypeName();
					String tmpClassName = declaringName.substring(0, declaringName.length()-2 );
					try{
						Class<?> tmpClass = Class.forName(tmpClassName);
						Object[] temp = (Object[]) java.lang.reflect.Array.newInstance( tmpClass , beans.length);
						int index = 0;
						for(DBEnableBean bean : beans){
							temp[index++] = tmpClass.cast(bean);
						}
						String setMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Method setter = classType.getDeclaredMethod(setMethod, temp.getClass());
						setter.invoke((DBEnableBean)newInstance, (Object)temp);	
					}catch(Exception e){
						logger.error(this.classType.getName() + ": set DBEnableBean Array Attribute: "+ fieldName +" error!\n" + getExceptionString(e));												
					}			
					
				}else if(FieldClassHelper.isListGenericType(fieldClass)){
					Class<?> genericClass = FieldClassHelper.getListFieldGenericTypeClass(f);
					String genericClassName = genericClass.getSimpleName();
					
					if(FieldClassHelper.isDBEnableBean(genericClass)){
						LinkedList<DBEnableBean> beans = DBEnableBeanField.getDBEnableBeanLinkedListAttrFromRs(f, rs);
						if(beans==null || beans.size() == 0) continue;
						LinkedList l = FieldClassHelper.getDBEnableBeanLinkedList(genericClass);
						for(DBEnableBean bean : beans){
							l.add(genericClass.cast(bean));
						}
						String setMethod = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Method setter = classType.getDeclaredMethod(setMethod, l.getClass());
						setter.invoke((DBEnableBean)newInstance, l);		
						
					}else if(FieldClassHelper.isBasicType(genericClassName)){
						LinkedList<WrapType> attrs = DBEnableBeanField.getBasicLinkedListAttrFromRs(f, rs);
						if(attrs==null || attrs.size() == 0) continue;
						setBasicAttrLinkedList((DBEnableBean)newInstance, fieldName, attrs);
					}						
				}
				
			}catch(Exception e){
				logger.error(this.classType.getName() + ": getBeanFromResult error!\n" + getExceptionString(e));
			}
		}
		try {
			int id = rs.getInt("id");
			((DBEnableBean) newInstance).setId(id);
		} catch (SQLException e) {
			logger.error(this.classType.getName() + ": get Id from BeanFromResult error!\n" + getExceptionString(e));
		}
		((DBEnableBean) newInstance).setLoaded(true);
		return (DBEnableBean) newInstance ;
	}
	
	
	public ResultSet getResultSet(String sql) {
		this.initConnection();
		ResultSet rs = null;
		try {
			Statement stmt = this.conn.createStatement();
			//System.out.println(sql);
			rs = stmt.executeQuery(sql);
		} catch (Exception e) {
			logger.error(this.classType.getName() + ": query error!\n" + getExceptionString(e));
		}
		return rs;
	}
	
	public LinkedList<DBEnableBean> query(String storeProcess){
		
		this.initConnection();	
		LinkedList<DBEnableBean> tmp = new LinkedList<DBEnableBean>();
		
		String sql = constructSQL(this.storeProcesses.getProperty(storeProcess));
		//System.out.println(sql);
		ResultSet rs = getResultSet(sql);
		
		try {
			//int index = 0;
			while(rs.next()){
				//System.out.println("Process index : " + index++);
				if(rs != null)
					tmp.add(getBeanFromResult(rs));
			}
		} catch (Exception e) {
			logger.error(this.classType.getName() + ": get Bean from ResultSet error!\n" + getExceptionString(e));
		}
		
		try {
			if(rs !=null ) rs.close();
			this.dboper.closeConnection(this.conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tmp;
	}
	
	public int insert(){
		int returnID = -1;
		
		this.initConnection();
		Field[] fields = this.getClass().getDeclaredFields(); 
		Field[] fatherFields = this.getClass().getSuperclass().getDeclaredFields();
		LinkedList<Field> tmpFields = new LinkedList<Field>();
		for(Field f : fields){ tmpFields.add(f); }
		for(Field f : fatherFields){
			if( f.isAnnotationPresent(DBExtendsField.class)){
				tmpFields.add( f ); 
			}
		}
		
		Field[] rstFields = new Field[ tmpFields.size() ];
		for(int i=0; i<rstFields.length; i++) rstFields[i] = tmpFields.get(i);
		try{
			PreparedStatement insertBean = DBEnableBeanSqlConstructor.getInsertBeanStatement(this, this.conn, this.tableName, DBEnableBeanSqlConstructor.getBasicFieldPrepareForSql( rstFields ));
			this.conn.setAutoCommit(false);
			insertBean.executeUpdate();
			ResultSet rs = insertBean.getGeneratedKeys();
			while(rs.next()){
				returnID = rs.getInt(1);
				this.id = returnID;
			}
			LinkedList<PreparedStatement> insertSqls = DBEnableBeanSqlConstructor.getInsertRelationStatement( this, this.conn, DBEnableBeanSqlConstructor.getComponentFieldPrepareForSql(fields ));
			for(PreparedStatement p : insertSqls){
				p.executeUpdate();
			}
			
			this.conn.commit();
			rs.close();
			insertBean.close();
			this.dboper.closeConnection(this.conn);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(this.classType.getName() + ": Insert Bean and Beans' relations into table error!\n" + getExceptionString(e));
		}
		return returnID;
	}
	
	public void update(){
		this.initConnection();
		Field[] fields = this.getClass().getDeclaredFields();  
		
		try{
			HashMap<Field, String[]> componentFeldMap = DBEnableBeanSqlConstructor.getComponentFieldPrepareForSql(fields );
			DBEnableBeanSqlConstructor.deleteAllRelationBeforeUpdate(this, this.conn, componentFeldMap);
			
			PreparedStatement updateBean = DBEnableBeanSqlConstructor.getUpdateBeanStatement(this, this.conn, this.tableName, DBEnableBeanSqlConstructor.getBasicFieldPrepareForSql(fields));
			updateBean.executeUpdate();
			LinkedList<PreparedStatement> insertSqls = DBEnableBeanSqlConstructor.getInsertRelationStatement( this, this.conn, componentFeldMap);
			for(PreparedStatement p : insertSqls){
				p.executeUpdate();
			}
			this.conn.commit();
			
			for(PreparedStatement p : insertSqls){
				p.close();
			}
			updateBean.close();
			this.dboper.closeConnection(this.conn);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(this.classType.getName() + ": update Bean and Beans' relations into table error!\n" + getExceptionString(e));
		}
		return;
	}
	
	public int persist(){
		int result = -1;
		if(this.id == -1 && !this.isLoaded()){
			result = insert();
		}else{
			result = this.id;
			update();
		}
		return result;
	}
	
	public DBEnableBean get(){
		LinkedList<DBEnableBean> beans = query("getById");
		return  beans == null ? null : beans.get(0).setLoaded(true) ;
	}
	
	public <T> T get(Class<T> c){
		LinkedList<DBEnableBean> beans = query("getById");
		return  beans == null ? null : c.cast(beans.get(0).setLoaded(true)) ;
	}
	

	public DBEnableBean load(){
		if( !this.isLoaded() ) 
			return 	this.get();
		else return this;
	}
	
	public void delete(){
		this.initConnection();
		String sql = "delete from " + this.tableName + " where id=" + this.id;
		Field[] fields = this.getClass().getDeclaredFields();  	
		try {
			PreparedStatement p = this.conn.prepareStatement(sql);
			p.executeUpdate();
			DBEnableBeanSqlConstructor.deleteAllRelationBeforeUpdate(this, this.conn, DBEnableBeanSqlConstructor.getComponentFieldPrepareForSql(fields));
			this.conn.commit();
			
			p.close();
			this.dboper.closeConnection(this.conn);
		} catch (SQLException e) {
			logger.error(this.classType.getName() + ": delete Bean from table error!\n" + getExceptionString(e));
		}
	}
	
	public static void delete(LinkedList<DBEnableBean> beans){
		for(DBEnableBean bean : beans){
			bean.delete();
		}
	} 
	
	public Connection initConnection(){
		try {
			if(this.conn == null || this.conn.isClosed() )
				this.conn = this.dboper.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.conn;
	}
	
	protected void initStoreProcessAndTableName(){	
		this.classType = this.getClass();
		
		if (this.classType.isAnnotationPresent(SP.class)) {
			SP annotation= (SP) this.classType.getAnnotation(SP.class);
			this.tableName = annotation.table();
			
			try{
				String[] storeProcessValue = annotation.sps().split(";");
				for(String sp : storeProcessValue){
					String key = sp.split(":")[0];
					String value = sp.split(":")[1];
					this.storeProcesses.setProperty(key.trim(), value.trim());	
				}
			}catch(Exception e){
				logger.info(this.classType.getName() + ": Store Processes hasn't define or define error!");
			}
		}else if( this.getClass().getSuperclass().isAnnotationPresent(SP.class)){
			SP annotation= (SP) this.getClass().getSuperclass().getAnnotation(SP.class);
			try{
				String[] storeProcessValue = annotation.sps().split(";");
				for(String sp : storeProcessValue){
					String key = sp.split(":")[0];
					String value = sp.split(":")[1];
					this.storeProcesses.setProperty(key.trim(), value.trim());	
				}
			}catch(Exception e){
				logger.info(this.classType.getName() + ": Store Processes hasn't define or define error!");
			}
		}else{
			logger.error(this.classType.getName() + ": has not define Store Processes!");
		}
	}
	
	public String getTableName(){
		return this.tableName;
	}
	
	public String constructSQL(String sp){
		
		Matcher m = getMatcher(sp);
	    StringBuffer sql2 = new StringBuffer();
	    //System.out.println("---------------" + sp);    
	    while (m.find()) {
	    	
	    	String param = m.group("attr");
	    	Object value = getBeanAttr(param);
		    
		    if(value != null){
		    	String valueType = value.getClass().getSimpleName().toLowerCase();
	            if(valueType.equals("string") && !param.toLowerCase().equals("tablename"))
	            {
	            	m.appendReplacement(sql2, "'" + value + "'");
	            }else if(value instanceof DBEnableBean){
	            	Field[] fields = this.getClass().getDeclaredFields();
	            	Field f = null;
	            	for(Field tmp : fields){
	            		if(tmp.getName().toLowerCase().equals(param.toLowerCase())){
	            			f = tmp; break;
	            		}
	            	}
	            	if( f != null){
	            		if(f.isAnnotationPresent(Relation.class)){
	   	   				 	Relation relation = f.getAnnotation(Relation.class);
	   	   				 	int beanId = ((DBEnableBean)value).getId();
	   	   				 	String tableName = relation.rTable();
	   	   				 	String sIdCol = relation.sIdCol(); 
	   	   				 	String dIdCol = relation.dIdCol(); 
	   	   				 	String sql = "select "+ sIdCol +" from "+ tableName +" where " + dIdCol + "=" + beanId;
	   	   				 	
			   	   			ResultSet rs = null;
			   	 			try {
			   	 				Statement stmt = this.initConnection().createStatement();
			   	 				rs = stmt.executeQuery(sql);
			   	 				rs.next();
			   	 				int dataId = rs.getInt(sIdCol);
			   	 				
			   	 				DBEnableBean bean = this.getClass().newInstance().setId(dataId);
			   	 				sql = bean.constructSQL(this.getById);
			   	 				stmt.close();
			   	 				return sql;
			   	 			} catch (Exception e) {
				   	 			this.logger.error("DBEnableBean(constructSql): To Construct sql command Error, the sql:" + sp);
			    		    	return "";
			   	 			}
	   	            	}
	   	   			
	            	}else{
	            		this.logger.error("DBEnableBean(constructSql): To Construct sql command Error, the sql:" + sp);
	    		    	return "";
	            	}
	            	
	            }else{
	            	m.appendReplacement(sql2,  value+"");
	            }
		    }else{
		    	this.logger.error("DBEnableBean(constructSql): To Construct sql command Error, the sql:" + sp);
		    	return "";
	        }
		 }	        
	     m.appendTail(sql2);
	     //System.out.println(sql2);
	     return sql2.toString();
	}
	
	protected Matcher getMatcher(String sp) {
	        String rule = "\\{(?<attr>[\\w]+)\\}";
	        Pattern r = Pattern.compile(rule);
	        return r.matcher(sp);
	}
	
	private Object getBeanAttr(String attrName){
    	Object value = null; 
		
    	if(attrName.toLowerCase().equals("tablename"))
    		return this.getTableName(); 
    	try{
    		if(this.classType == null)
    			this.classType = this.getClass();
        	String getter = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
            Method getmethod = this.classType.getMethod(getter);
            value = getmethod.invoke(this);
            
    	}catch(Exception e){
	    	String msg = this.classType.getName() + ": getBeanAttr Error, ATTRNAME:" + attrName; 
	    	logger.error(msg + "\n" + getExceptionString(e));
    	}
    	
        return value;
    }
	
	protected void finalize(){
		try{
			super.finalize();
			if(this.conn != null) dboper.closeConnection(conn);
		}catch(Exception e){
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected String getExceptionString(Exception e){
		e.printStackTrace(new PrintWriter(sw,true));  
    	return sw.toString();   
	}
	
	public boolean isLoaded() {
		return loaded;
	}

	public DBEnableBean setLoaded(boolean loaded) {
		this.loaded = loaded;
		return this;
	}

	public int getId(){
		return this.id;
	}
	public DBEnableBean setId(int id) {
		this.id = id;
		return this;
	}

	public Properties getStoreProcesses() {
		return storeProcesses;
	}

	
	public DBEnableBean setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public void closeConn(){
		this.dboper.closeConnection(this.conn);
	}
	
}
