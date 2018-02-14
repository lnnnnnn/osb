package edu.nuist.osbank.abenablebean.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;

import edu.nuist.osbank.dbenablebean.DBEnableBean;



public class FieldClassHelper {
	public static final String[] basicTypes = {"int", "long", "integer","boolean", "float", "double", "Date", "Timestamp", "String"};
	
	public static boolean isBasicType(String simpleName){
		boolean flag = false;
		for(String s : basicTypes){
			if(simpleName.trim().toLowerCase().equals(s.toLowerCase())){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public static boolean isArray(String simpleName){
		return simpleName.trim().endsWith("[]");
	}
	
	public static boolean isBasicTypeArray(String simpleName){
		boolean flag = false;
		if(isArray(simpleName)){
			String tmp = simpleName.trim().substring(0, simpleName.length()-2);
			flag = isBasicType(tmp);
		}
		return flag;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static boolean isListGenericType(Class c){
		return c.getSimpleName().equals("LinkedList");
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isDBEnableBean(Class c){
		boolean flag = false;
		if(c.equals(DBEnableBean.class))	return true;
		Class tmp  = c;
		String oldClassName = tmp.getName();
		tmp = c.getSuperclass();
		while( tmp != null && !oldClassName.equals(tmp.getName()) ){
			oldClassName = tmp.getName();
			if(tmp.getName().indexOf("DBEnableBean") != -1) {
				flag = true;
				break;
			}
			tmp = tmp.getSuperclass();
		}
		return flag; 
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getListFieldGenericTypeClass(Field f){
		String simpleName = f.getGenericType().getTypeName();
		
		if(simpleName.endsWith("[]")){
			try{
				Class<?> c = Class.forName(simpleName.substring(0, simpleName.length()-2 ));
				return c;
			}catch(Exception e){
				return null;
			}
		}
		ParameterizedType pt =  (ParameterizedType) f.getGenericType(); 
        Class lll = (Class)pt.getActualTypeArguments()[0];  
        return lll;
	}
	
	public static String getListFieldGenericTypeClassSimpleName(Field f){
		return getListFieldGenericTypeClass(f).getSimpleName();
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Class getBasicClass(String basicType){
		Class c = null;
		switch(basicType.toLowerCase()){
			case "int" :
				c = int.class;
				break;
			case "double" :
				c = double.class;
				break;
			case "float" :
				c = float.class;
				break;
			case "string" :
				c = String.class;
				break;
			case "boolean" :
				c = boolean.class;
				break;
			case "date" :
				c = Date.class;
				break;
			case "timestamp" :
				c = Timestamp.class;
				break;
			case "long" :
				c = long.class;
				break;
		}
		return c;
	}
	
	@SuppressWarnings("rawtypes")
	public static LinkedList getBasicLinkedList(String basicType){
		LinkedList c = null;
		switch(basicType){
			case "int" :
				c = new LinkedList<Integer>();
				break;
			case "double" :
				c = new LinkedList<Double>();
				break;
			case "float" :
				c = new LinkedList<Float>();
				break;
			case "String" :
				c = new LinkedList<String>();
				break;
			case "boolean" :
				c = new LinkedList<Boolean>();
				break;
			case "Date" :
				c = new LinkedList<Date>();
				break;
		}
		return c;
	}
	
	public static Object getBasicArray(int length, String basicType){
		Object o = null;
		switch(basicType){
			case "int" :
				o = new int[length];
				break;
			case "double" :
				o = new double[length];
				break;
			case "float" :
				o = new float[length];
				break;
			case "String" :
				o = new String[length];
				break;
			case "boolean" :
				o = new boolean[length];
				break;
			case "Date" :
				o = new Date[length];
				break;
		}
		return o;
	}
	
	public static boolean isDBEnableBeanArray(Field f){
		boolean flag = false;
		
		String declaringName = f.getGenericType().getTypeName();
		
		if(declaringName.endsWith("[]")){
			String tmpClassName = declaringName.substring(0, declaringName.length()-2 );
			try{
				Class<?> tmp = Class.forName(tmpClassName);
				flag = isDBEnableBean(tmp);
			}catch(Exception e){
			}			
		}
		return flag;
	}
	
	public static  <T> LinkedList<T> getDBEnableBeanLinkedList(Class<T> c){
		return new LinkedList<T>();
	}
}
