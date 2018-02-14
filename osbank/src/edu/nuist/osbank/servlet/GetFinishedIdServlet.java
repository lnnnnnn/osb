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
 * Servlet implementation class GetFinishedIdServlet
 */
@WebServlet("/GetFinishedIdServlet")
public class GetFinishedIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
		 
		public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String userId=request.getParameter("userId");
			
			
			int taskId=Integer.parseInt(request.getParameter("taskId"));
			LinkedList<DBEnableBean> beans=new User_Task().setUserId(userId).setTaskId(taskId).query("getByUseridAndTask");
			
			if(beans!=null&&beans.size()>0){
				User_Task ut=(User_Task)beans.get(0);
		      		int finishedId=ut.getFinishedId();
		      		
		      		//System.out.println(finishedId);
		      		response.getWriter().print(finishedId);
			}
}
}
