package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.nuist.osbank.beans.Ex_SingleSelection;
import edu.nuist.osbank.beans.Ex_Statistics;
import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.service.ExGetQService;
import edu.nuist.osbank.service.GetRequest;

/**
 * Servlet implementation class GetStatisticsServlet
 */
@WebServlet("/GetStatisticsServlet")
public class GetStatisticsServlet extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println(request.getParameter("data"));
		String strjson = request.getParameter("data");
		// String strjson = "{\"ex_ss\":[2,3,4,5], \"ex_fb\":[2,3],
		// \"ex_judge\":[2,3], \"ex_sa\":[2,3],\"ex_calc\":[2,3]}";
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
			//	System.out.println(ue);
				es.setCount(ue.getCountByEx()).setCorrect(ue.getCorrectByEx()).setWrong(ue.getWrongByEx())
						.setAvg(ue.getAvgByEx()).setScore(ue.getExScore())
						.setBreak1(ue.getSectionNum(0)).setBreak2(ue.getSectionNum(1))
						.setBreak3(ue.getSectionNum(2)).setBreak4(ue.getSectionNum(3));
				resStr += gson.toJson(es);
				resStr += ",";
				//System.out.println(es);
			//	System.out.println(gson.toJson(es));
			}

		}
		
		resStr = resStr.substring(0, resStr.length() - 1);
		resStr += "]}";
		System.out.println(resStr);
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(resStr);

	}
}
