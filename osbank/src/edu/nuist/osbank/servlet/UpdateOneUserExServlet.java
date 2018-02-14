package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.beans.User_Task;
import edu.nuist.osbank.dbenablebean.DBEnableBean;

/**
 * Servlet implementation class UpdateOneUserExServlet
 */
@WebServlet("/UpdateOneUserExServlet")
public class UpdateOneUserExServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String strjson=request.getParameter("data");
		System.out.println(request.getParameter("taskId"));
		int taskId=Integer.parseInt(request.getParameter("taskId"));
		int finishedId=Integer.parseInt(request.getParameter("finishedId"));
		boolean finished=Boolean.parseBoolean(request.getParameter("finished"));
		/*String strjson="{\"userId\":\"88888888888\",\"exType\":\"ex_ss\",\"exId\":1,\"answer\":\"A\",\"evaluation\":\"正确\"}";

		int taskId=0;
		int finishedId=5;
	 	boolean finished=Boolean.parseBoolean("true");	*/
		//System.out.println(taskId);
		//System.out.println(request.getParameter("data"));
		//strjson=URLDecoder.decode(strjson,"UTF-8");
		//System.out.println(strjson);
		Gson gson = new Gson();
		String userId="";
		
			 User_Ex p=gson.fromJson(strjson,User_Ex.class);
			 p.setCreatedDate(new java.sql.Date(new Date().getTime()));
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
				    s.setAnswer(p.getAnswer()).setEvaluation(p.getEvaluation()).setCreatedDate(new java.sql.Date(new Date().getTime()));
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
		
		
		
	
}
