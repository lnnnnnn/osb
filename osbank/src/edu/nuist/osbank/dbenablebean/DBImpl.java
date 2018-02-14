package edu.nuist.osbank.dbenablebean;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBImpl implements DBInterface{
	private	 static String path = "applicationContext.xml";
	private static String dataSource = "dataSource";
	private static ApplicationContext ctx = null;
	private static DataSource ds = null;
	
	static{
		ctx = new ClassPathXmlApplicationContext( path );
		ds = (DataSource) ctx.getBean(dataSource);
	}

	public Connection getConnection(){
		Connection conn = null;
		try{
			conn = ds.getConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	/*
	public Connection getConnection(){
		Connection conn = null;
		try{
	        Class.forName("com.mysql.jdbc.Driver");  
	        conn = (Connection)DriverManager.getConnection(Configuration.getDbUrl(), Configuration.getDbUser(), Configuration.getDbpassword());   	
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}*/
	
	public void closeConnection(Connection conn){
		try{
			conn.close();
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		DBImpl impl = new DBImpl();
		Connection conn = impl.getConnection();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs_tmp = stmt.executeQuery("select * from articles");
			rs_tmp.next();
			System.out.println( rs_tmp.getString("stract"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
