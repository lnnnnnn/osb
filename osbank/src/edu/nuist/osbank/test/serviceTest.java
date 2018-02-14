package edu.nuist.osbank.test;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import edu.nuist.osbank.beans.Ex_Statistics;
import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.beans.User_Task;
import edu.nuist.osbank.beans.User_Ex.InnerUser_Ex;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.service.ExGetQService;
import edu.nuist.osbank.service.GetRequest;

public class serviceTest {
    public static void getTaskSituation(){
 	String userId="77777777777";
   	int taskId=0;
  
    		int state;
    	
    			LinkedList<DBEnableBean> beans=new User_Task().setUserId(userId).setTaskId(taskId).query("getByUseridAndTask");
    			
    			if(beans!=null&&beans.size()>0){
    				User_Task ut=(User_Task)beans.get(0);
    				 state=ut.getTaskState();
    			}else{
    					state=0;
    			}
    		
    		
    		System.out.println((state+""));
    }
    public static void getFinishedId(){
    	String userId="77777777777";
		
		
		int taskId=1;
		LinkedList<DBEnableBean> beans=new User_Task().setUserId(userId).setTaskId(taskId).query("getByUseridAndTask");
		
		if(beans!=null&&beans.size()>0){
			User_Task ut=(User_Task)beans.get(0);
	      		int finishedId=ut.getFinishedId();
	      		
	      		System.out.println(finishedId);
	      		
		}
    }
    
    public static void updateUserEx(){
    	
    	String strjson="{\"userId\":\"88888888888\",\"exType\":\"ex_ss\",\"exId\":1,\"answer\":\"A\",\"evaluation\":\"正确\"}";

		int taskId=0;
		int finishedId=5;
	 	boolean finished=Boolean.parseBoolean("true");	
		//System.out.println(taskId);
		//System.out.println(request.getParameter("data"));
		//strjson=URLDecoder.decode(strjson,"UTF-8");
		//System.out.println(strjson);
		Gson gson = new Gson();
		String userId="";
		
			 User_Ex p=gson.fromJson(strjson,User_Ex.class);
			userId=p.getUserId();
			//System.out.println(p.toString());
			//System.out.println(p.getExType());
			
			//如果计算题 单独处理
			if(p.getExType().equals("ex_calc")){
				User_Ex.updateInfoByUseridAndEx(p);
			}else{
				 LinkedList<DBEnableBean> beans=new User_Ex().setUserId(p.getUserId()).setExType(p.getExType()).setExId(p.getExId()).query("getByUseridAndEx");
					//如果已存在此用户此题记录 更新
			      if(beans!=null&&beans.size()>0){
				
					User_Ex s = (User_Ex)beans.get(0);
				    s.setAnswer(p.getAnswer()).setEvaluation(p.getEvaluation());
				    s.update();
				    
				   
				      
				}else{
					
					p.insert();
					//exception
				}
			}		  
			LinkedList<DBEnableBean> utbeans=new User_Task().setUserId(p.getUserId()).setTaskId(taskId).query("getByUseridAndTask");
			
			 //更新user_task
		    

			//
		      if(utbeans!=null&&utbeans.size()>0){
		    	  User_Task ut=(User_Task)utbeans.get(0);
		    	  ut.setFinishedId(finishedId);
		    	  
		    	  if(finished)ut.setTaskState(1);
		    	  ut.update();
		    	  
		    	  
		      }else{//第一次作答此作业
			      new User_Task(userId,taskId,-1).setFinishedId(finishedId).insert();

		      }
    }
    public static void getStatistics(){
    	         String strjson = "{\"ex_ss\":[2,3,4,5], \"ex_fb\":[2,3], \"ex_judge\":[2,3], \"ex_sa\":[2,3],\"ex_calc\":[2,3]}";
    			ExGetQService egq = new ExGetQService();
    			List<GetRequest> list = egq.getquest(strjson);
    			Gson gson = new Gson();
    			String sjson = "";

    			String resStr = "{\"data\" :[";
    			//Gson gson=new Gson();
    			for (int i = 0; i < list.size(); i++) {
    				GetRequest gr = new GetRequest();
    				gr = list.get(i);
    				String str = "";
    				User_Ex ue=new User_Ex().setExType(gr.getExType());
    				Ex_Statistics es=new Ex_Statistics();
    				for (int j = 0; j < gr.getExId().size(); j++) {

    					DBEnableBean bean = ue.setExId(gr.getExId().get(j));
    					User_Ex e = (User_Ex)bean;
    					es.setCount(ue.getCountByEx()).setCorrect(ue.getCorrectByEx()).setWrong(ue.getWrongByEx()).setAvg(ue.getAvgByEx());
    					resStr += gson.toJson(es);
    					resStr += ",";
    				}
    			}	
    			resStr = resStr.substring(0, resStr.length() - 1);
    			resStr += "]}";
    			System.out.println(resStr);
    }
    public static void getStatistics2(){
    	 String strjson = "{\"ex_ss\":[2,3,4,5], \"ex_fb\":[2,3], \"ex_judge\":[2,3], \"ex_sa\":[2,3],\"ex_calc\":[2,3]}";
    			ExGetQService egq = new ExGetQService();
    			List<GetRequest> list = egq.getquest(strjson);
    			Gson gson = new Gson();
    			String sjson = "";

    			String resStr = "{\"data\" :[";
    			// Gson gson=new Gson();
    			for (int i = 0; i < list.size(); i++) {
    				GetRequest gr = new GetRequest();
    				gr = list.get(i);
    				String str = "";
    				User_Ex ue = new User_Ex().setExType(gr.getExType());
    				Ex_Statistics es = new Ex_Statistics();
    				for (int j = 0; j < gr.getExId().size(); j++) {
    					int exId = gr.getExId().get(j);
    					DBEnableBean bean = ue.setExId(exId);
    					User_Ex e = (User_Ex) bean;
    					System.out.println(ue);
    					es.setCount(ue.getCountByEx()).setCorrect(ue.getCorrectByEx()).setWrong(ue.getWrongByEx())
    							.setAvg(ue.getAvgByEx()).setScore(ue.getExScore())
    							.setBreak1(ue.getSectionNum(0)).setBreak2(ue.getSectionNum(1))
    							.setBreak3(ue.getSectionNum(2)).setBreak4(ue.getSectionNum(3));
    					resStr += gson.toJson(es);
    					resStr += ",";
    					//System.out.println(es);
    					System.out.println(gson.toJson(es));
    				}

    			}
    			
    			resStr = resStr.substring(0, resStr.length() - 1);
    			resStr += "]}";
    			System.out.println(resStr);
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//getTaskSituation();
		//getFinishedId();
		//updateUserEx();
		//getStatistics();
		/*String exType="ex_sa";
		int exId=2;
		String ueStr="";
		LinkedList<DBEnableBean> beans = new User_Ex().setExType(exType).setExId(exId).query("getNotgradeByEx");

		if (beans != null && beans.size() > 0) {

			User_Ex ue = (User_Ex) beans.get(0);
			Gson gson=new Gson();
			 ueStr= gson.toJson(ue.new InnerUser_Ex());
		}else{
			ueStr="{}";
		}
		System.out.println(ueStr);
	}*/
		
		getStatistics2();

}
}