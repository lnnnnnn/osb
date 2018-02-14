package edu.nuist.osbank.beans;

import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;

@SP(
	     table="user_task",
	     sps="getByUseridAndTask: select * from user_task where userid={userId} and taskid={taskId};",
		 create="create table user_task ("+
	     "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY,"+
		 "userid varchar(255), taskid int, taskstate int,finishedid int); "		 
	  )
public class User_Task extends DBEnableBean{
	
	private String userId;
	private int taskId;
	private int taskState;//-1,1表示未完成，已完成;无记录表示未作答
	private int finishedId;
	public String getUserId() {
		return userId;
	}
	public User_Task setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	public int getTaskId() {
		return taskId;
	}
	public User_Task setTaskId(int taskId) {
		this.taskId = taskId;
		return this;
	}
	
	
	public int getTaskState() {
		return taskState;
	}
	public User_Task setTaskState(int taskState) {
		this.taskState = taskState;
		return this;
	}
	
	
	public int getFinishedId() {
		return finishedId;
	}
	public User_Task setFinishedId(int finishedid) {
		this.finishedId = finishedid;
		return this;
	}
	public User_Task(String userId, int taskId,int taskState) {
		super();
		this.userId = userId;
		this.taskId = taskId;
		this.taskState=taskState;
	}
	public User_Task() {
		super();
	}
	
	
	public static void main(String[]args){
		
		User_Task s1=new User_Task("77777777777",0,1);
		User_Task s2=new User_Task("77777777777",1,-1).setFinishedId(3);
		User_Task s=new User_Task("77777777777",2,0);
		 new DBTableInitor(User_Task.class,s);
		
		  s1.insert();
		  s2.insert();
		}
	

}
