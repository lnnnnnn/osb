package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.dbenablebean.DBEnableBean;

/**
 * Servlet implementation class GetOneUserEx
 */
@WebServlet("/GetOneUserEx")
public class GetOneUserEx extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String exType = request.getParameter("exType");
		int exId = Integer.parseInt(request.getParameter("exId"));
		
		System.out.println("getOneUE:"+exType+exId);
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
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(ueStr);
	}
}
