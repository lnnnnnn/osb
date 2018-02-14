package edu.nuist.osbank.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;

@SP(
	     table="user_ex",
	     sps="getByUseridAndEx: select * from user_ex where userid={userId} and extype={exType} and exid={exId};"+
	    		 "getByEx: select * from user_ex where extype={exType} and exid={exId};"+
	    		 "getNotgradeByEx: select * from user_ex where extype={exType} and exid={exId} and evaluation='';"+
	         "getCountByEx: select * from user_ex where extype={exType} and exid={exId};"+
	         "getCorrectByEx: select * from user_ex where extype={exType} and exid={exId} and evaluation='正确';"+ 
    		 "getWrongByEx: select * from user_ex where extype={exType} and exid={exId} and evaluation='错误';"+
    		 "getAvgByUseridAndEx: select avg(evaluation) from user_ex where extype={exType} and exid={exId} ;" 

	         
             ,
		 create="create table user_ex ("+
	     "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY,"+
		 "userid varchar(255), extype varchar(255),exid int,answer varchar(255),"
	     +"evaluation varchar(255),imguri varchar(255),createdDate date ); "		 
	  )
public class User_Ex extends DBEnableBean{
	private String userId;
	private String exType;
	private int exId;
	private String answer;
	private String evaluation;
	private String imgUri;
	private java.sql.Date createdDate;
	public String getUserId() {
		return userId;
	}
	public User_Ex setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	public String getExType() {
		return exType;
	}
	public User_Ex setExType(String exType) {
		this.exType = exType;
		return this;
	}
	public int getExId() {
		return exId;
	}
	public User_Ex setExId(int exId) {
		this.exId = exId;
		return this;
	}
	public String getAnswer() {
		return answer;
	}
	public User_Ex setAnswer(String answer) {
		this.answer = answer;
		return this;
	}
	public String getEvaluation() {
		return evaluation;
	}
	public User_Ex setEvaluation(String evaluation) {
		this.evaluation = evaluation;
		return this;
	}
	public String getImgUri() {
		return imgUri;
	}
	public User_Ex setImgUri(String imgUri) {
		this.imgUri = imgUri;
		return this;
	}
	
	
	public java.sql.Date getCreatedDate() {
		return createdDate;
	}
	public User_Ex setCreatedDate(java.sql.Date date) {
		this.createdDate = date;
		return this;
	}
	
	@Override
	public String toString() {
		return "User_Ex [userId=" + userId + ", exType=" + exType + ", exId=" + exId + ", answer=" + answer
				+ ", evaluation=" + evaluation + ", imgUri=" + imgUri + ", createdDate=" + createdDate + "]";
	}
	
public static void main(String[]args){
		
		//User_Ex s=new User_Ex().setUserId("21041333333").setExId(3).setAnswer("T").setExType("ex_judge");
		//new DBTableInitor(User_Ex.class,s);
		//User_Ex.updateImgUriByUseridAndEx("21041333333", "ex_judge", 3, "cc.jpg");
		/*User_Ex s=new User_Ex();
		s.setExType("ex_ss").setExId(2).setCreatedDate(new java.sql.Date(new Date().getTime()));
		//new DBTableInitor(User_Ex.class,s);
		s.insert();
		System.out.println(s);*/
		//DBEnableBean bean =new DBEnableBean();
		
		/*try {
			System.out.println(new User_Ex().setExType("ex_sa").setExId(2).getStatus());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		User_Ex s=new User_Ex();
		s.setExType("ex_sa").setExId(2);
		System.out.println(s.getSectionNum(2));
		/*s.insert();
		
		s.setExType("ex_sa").setExId(3).setCreatedDate(new java.sql.Date(new Date().getTime())).setUserId("20141398013").setAnswer("effsdas");
		s.insert();
		
		s.setExType("ex_sa").setExId(3).setCreatedDate(new java.sql.Date(new Date().getTime())).setUserId("20141344051").setAnswer("effsdas");
		s.insert();*/

	}
public int getExScore(){
	int score=-1;
	if(exType.equals("ex_sa")){
		Ex_ShortAnswer sa=(Ex_ShortAnswer)new Ex_ShortAnswer().setId(exId).get();
		//System.out.println(sa.getScore());
		 score=sa.getScore();
	}else if(exType.equals("ex_calc")){
		Ex_Calculate sa=(Ex_Calculate)new Ex_Calculate().setId(exId).get();
		//System.out.println(sa.getScore());
		 score=sa.getScore();
	}
	return score;
}
	public int getSectionNum(int section){
		int num=-1;
		double []perc=new double[5];
		for(int i=0;i<5;i++){
			perc[i]=i*0.25;
			//System.out.println(perc[i]);
		}
		//System.out.println(exType);
		//System.out.println(exType=="ex_sa");
		if(exType.equals("ex_sa")){
			Ex_ShortAnswer sa=(Ex_ShortAnswer)new Ex_ShortAnswer().setId(exId).get();
			//System.out.println(sa.getScore());
			int score=sa.getScore();
			double left=score*perc[section],right=score*perc[section+1];
			String sql="select count(*) from user_ex where extype='"+getExType()+"' and exid='"+getExId()+"'"+"and evaluation between "+left+" and "+ right+";";
			
			//System.out.println(sql);
			ResultSet rs=getResultSet(sql);
			try {
				if(rs.next()) {
				num = rs.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(exType.equals("ex_calc")){
			Ex_Calculate sa=(Ex_Calculate)new Ex_Calculate().setId(exId).get();
			//System.out.println(sa.getScore());
			int score=sa.getScore();
			double left=score*perc[section],right=score*perc[section+1];
			String sql="select count(*) from user_ex where extype='"+getExType()+"' and exid='"+getExId()+"'"+"and evaluation between "+left+" and "+ right+";";
			
			//System.out.println(sql);
			ResultSet rs=getResultSet(sql);
			try {
				if(rs.next()) {
				num = rs.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return num;
	}
	
	public static void updateImgUriByUseridAndEx(String userId,String exType,int exid,String imgUri){
		LinkedList<DBEnableBean> beans=new User_Ex().setUserId(userId).setExType(exType).setExId(exid).query("getByUseridAndEx");
		
		if(beans!=null&&beans.size()>0){
			
				User_Ex s = (User_Ex)beans.get(0);
			     s.setImgUri(imgUri);
			     s.update();
			}else{
				
				User_Ex s=new User_Ex();
				s.setUserId(userId).setExType(exType).setExId(exid).setImgUri(imgUri).setCreatedDate(new java.sql.Date(new Date().getTime()));
				
				s.insert();
			}
		
	}
	public static void updateInfoByUseridAndEx(User_Ex ue ){
             LinkedList<DBEnableBean> beans=new User_Ex().setUserId(ue.getUserId()).setExType(ue.getExType()).setExId(ue.getExId()).setCreatedDate(new java.sql.Date(new Date().getTime())).query("getByUseridAndEx");
		
		      if(beans!=null&&beans.size()>0){
			
				User_Ex s = (User_Ex)beans.get(0);
			    s.setAnswer(ue.getAnswer()).setEvaluation(ue.getEvaluation());
			    s.update();
			}else{
				ue.insert();
				//exception
				
			}
	}
	public int getCountByEx(){
		String sql="select count(*) from user_ex where extype='"+getExType()+"' and exid='"+getExId()+"';";
		
		//System.out.println(sql);
		ResultSet rs=getResultSet(sql);
		int cnt=0;
		try {
			if(rs.next()) {
			cnt = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cnt;
	}
	public int getCorrectByEx(){
		String sql="select count(*) from user_ex where extype='"+getExType()+"' and exid='"+getExId()+"'and evaluation='正确'";
		
		//System.out.println(sql);
		ResultSet rs=getResultSet(sql);
		int cnt=0;
		try {
			if(rs.next()) {
			cnt = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cnt;
	}
	public int getWrongByEx(){
		String sql="select count(*) from user_ex where extype='"+getExType()+"' and exid='"+getExId()+"'and evaluation='错误'";
		
		//System.out.println(sql);
		ResultSet rs=getResultSet(sql);
		int cnt=0;
		try {
			if(rs.next()) {
			cnt = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cnt;
	}
	public double getAvgByEx(){
String sql="select avg(evaluation) from user_ex where extype='"+getExType()+"' and exid='"+getExId()+"'";
System.out.println(sql);
		
		ResultSet rs=getResultSet(sql);
		double avg=0;
		try {
			if(rs.next()) {
			avg = rs.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avg;
	}
	
	public int getStatus() throws SQLException{
		int res=0;//0 1 2分别代表未批改，批改中，批改完。
		
		String sql="select * from grade_status where extype='"+getExType()+"' and exid='"+getExId()+"'";
		ResultSet rs=getResultSet(sql);
	//	System.out.println(sql);
	//	System.out.println(rs);
			if(rs.next()) {//已经批改过 继续判断是否此题目的所有学生都批改过
				 String sql2="select * from user_ex where extype='"+getExType()+"' and exid='"+getExId()+"'" +"and evaluation=''";
				//	System.out.println(sql2);
					ResultSet rs2=getResultSet(sql2);
					//System.out.println(rs2);
						if(rs2.next()) {//如果此题有用户尚未给打分
							res=1;
						}else{
							res=2;//此题目已全部批阅
							
						}
					
					
			}

		return res;
	}
		//public 修饰符便于不同包中显示
	public class InnerUser_Ex{
		private String userId=User_Ex.this.userId;
		private String exType=User_Ex.this.exType;
		private int exId=User_Ex.this.exId;
		private String answer=User_Ex.this.answer;
		private String evaluation=User_Ex.this.evaluation;
		private String imgUri=User_Ex.this.imgUri;;
		
		
	}
}
