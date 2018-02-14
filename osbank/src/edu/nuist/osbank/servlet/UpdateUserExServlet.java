package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.beans.User_Task;

/**
 * Servlet implementation class UpdateUserExServlet
 */
@WebServlet("/UpdateUserExServlet")
public class UpdateUserExServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String strjson=request.getParameter("data");
		String taskId=request.getParameter("taskId");
		//System.out.println(taskId);
		//System.out.println(request.getParameter("data"));
		//strjson=URLDecoder.decode(strjson,"UTF-8");
		//System.out.println(strjson);
		Gson gson = new Gson();
		String userId="";
		List<User_Ex> ps = gson.fromJson(strjson, new TypeToken<List<User_Ex>>(){}.getType());
		System.out.println(ps.size());
		for(int i = 0; i < ps.size() ; i++)
		{
			User_Ex p = ps.get(i);
			userId=p.getUserId();
			//System.out.println(p.toString());
			//System.out.println(p.getExType());
			if(p.getExType().equals("ex_calc")){
				User_Ex.updateInfoByUseridAndEx(p);
			}else{
				p.insert();
			}
			
		}
		// 向用户-作业 表中插入一行数据
		//new User_Task(userId,Integer.parseInt(taskId)).insert();
	}

}