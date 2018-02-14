package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nuist.osbank.beans.Admin;
import edu.nuist.osbank.dbenablebean.DBEnableBean;

/**
 * Servlet implementation class AdminLogin
 */
@WebServlet("/AdminLogin")
public class AdminLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

 public void service(HttpServletRequest request, HttpServletResponse response){
    	
    	System.out.println("receiveLogin");
    	String un = request.getParameter("username");
		String ps = request.getParameter("password");
		
		try {
			Admin u = new Admin();
			u.setName(un);
			u.setPassword(ps);
			LinkedList<DBEnableBean> users = u.query("loginSQL");
			if( users.size()>0 ){
				System.out.println("Login Success!!");
				response.getWriter().print("success");
				
				}
			
			else{
				System.out.println("Login ERROR!!");
				response.getWriter().print("Login ERROR!!");
			}
				

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
