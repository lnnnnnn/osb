package edu.nuist.osbank.dbenablebean;



import java.sql.Connection;

public interface DBInterface {
	public abstract Connection getConnection();
	public abstract void closeConnection(Connection conn);
}
