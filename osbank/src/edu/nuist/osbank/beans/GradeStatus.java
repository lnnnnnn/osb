package edu.nuist.osbank.beans;

import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;
@SP(
	     table="grade_status",
	     sps="",
		 create="create table grade_status("+
	     "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY,"+
		 "extype varchar(255),exid int,graded tinyint(1)); "		 
	  )
public class GradeStatus extends DBEnableBean {
	
	private String exType;
	private int exId;
	private boolean graded;
	
	
	public GradeStatus() {
		super();
	}
	public GradeStatus(String exType, int exId, boolean graded) {
		super();
		this.exType = exType;
		this.exId = exId;
		this.graded = graded;
	}
	public String getExType() {
		return exType;
	}
	public void setExType(String exType) {
		this.exType = exType;
	}
	public int getExId() {
		return exId;
	}
	public void setExId(int exId) {
		this.exId = exId;
	}
	public boolean isGraded() {
		return graded;
	}
	public void setGraded(boolean graded) {
		this.graded = graded;
	}
   
	public static void main(String[]args){
		GradeStatus gs=new GradeStatus();
		gs.setExType("ex_sa");
		gs.setExId(2);
		gs.setGraded(true);
		new DBTableInitor(GradeStatus.class,gs);
	}
}
