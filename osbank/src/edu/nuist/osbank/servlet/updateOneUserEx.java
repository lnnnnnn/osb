package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.nuist.osbank.beans.GradeStatus;
import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.dbenablebean.DBEnableBean;

/**
 * Servlet implementation class updateOneUserEx
 */
@WebServlet("/updateOneUserEx")
public class updateOneUserEx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateOneUserEx() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String ueStr=request.getParameter("ueStr");
		Gson gson=new Gson();
		User_Ex ue=gson.fromJson(ueStr,User_Ex.class);
		System.out.println(ue);
LinkedList<DBEnableBean> beans=ue.query("getByUseridAndEx");
		
		if(beans!=null&&beans.size()>0){
			
				User_Ex s = (User_Ex)beans.get(0);
			     s.setEvaluation(ue.getEvaluation());
			     s.update();
			}else{
				
				
			}
		//处理 gradeStatus
		String sql="select * from grade_status where extype='"+ue.getExType()+"' and exid='"+ue.getExId()+"'";
		ResultSet rs=ue.getResultSet(sql);
	//	System.out.println(sql);
	//	System.out.println(rs);
			try {
				if(rs.next()) {
					
				}else{
					new GradeStatus(ue.getExType(),ue.getExId(),true).insert();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
