package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nuist.osbank.beans.User_Ex;

/**
 * Servlet implementation class GetGradeStatus
 */
@WebServlet("/GetGradeStatus")
public class GetGradeStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGradeStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String exType=request.getParameter("exType");
		int exId=Integer.parseInt(request.getParameter("exId"));
		int status=-1;
		try {
		 status= new User_Ex().setExType(exType).setExId(exId).getStatus();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(status);
		response.getWriter().println(status);
	}

}
