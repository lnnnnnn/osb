package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.beans.User_Ex.InnerUser_Ex;
import edu.nuist.osbank.dbenablebean.DBEnableBean;

/**
 * Servlet implementation class GetUserExServlet
 */
@WebServlet("/GetUserExServlet")
public class GetUserExServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
   
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		{
			System.out.println(request.getParameter("data"));
			String strjson=request.getParameter("data");
			Gson gson = new Gson();
			List<User_Ex> ues = gson.fromJson(strjson, new TypeToken<List<User_Ex>>() {
			}.getType());
			String resJson="{\"data\":[";
			for (int i = 0; i < ues.size(); i++) {
				User_Ex ue = ues.get(i);
				LinkedList<DBEnableBean> beans=ue.query("getByUseridAndEx");
				
				if(beans!=null&&beans.size()>0){
					User_Ex cue=(User_Ex)beans.get(0);
					resJson+=gson.toJson(cue.new InnerUser_Ex());
					if(i<(ues.size()-1))
						resJson+=",";
				}
				
				
			}
			
			resJson+="]}";
			
			System.out.println(resJson);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(resJson);
			
			
		}
	
}
}
