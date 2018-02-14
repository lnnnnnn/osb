package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nuist.osbank.beans.User_Task;
import edu.nuist.osbank.dbenablebean.DBEnableBean;

/**
 * Servlet implementation class getTaskSituationServlet
 */
@WebServlet("/getTaskSituationServlet")
public class getTaskSituationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//-1,1表示未完成，已完成;0表示无记录未作答
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId=request.getParameter("userId");
	
		int taskNum=Integer.parseInt(request.getParameter("taskNum"));
		/*int state;
    		int taskId=Integer.parseInt(request.getParameter("taskId"));
		LinkedList<DBEnableBean> beans=new User_Task().setUserId(userId).setTaskId(taskId).query("getByUseridAndTask");
		
		if(beans!=null&&beans.size()>0){
			User_Task ut=(User_Task)beans.get(0);
			 state=ut.getTaskState();
		}else{
				state=0;
		}
		System.out.println(taskId+" :"+state);
		response.getWriter().print(state+"");*/
		
	String resStr="";//以一行01串代表是否存在
		
		for(int i=0;i<taskNum;i++){
			int state;
			LinkedList<DBEnableBean> beans=new User_Task().setUserId(userId).setTaskId(i).query("getByUseridAndTask");
			
			if(beans!=null&&beans.size()>0){
				User_Task ut=(User_Task)beans.get(0);
				 state=ut.getTaskState();
			}else{
					state=0;
			}
			
			resStr+=(state+",");
			
		}
		resStr=resStr.substring(0,resStr.length()-1);
		response.getWriter().print(resStr);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
