package edu.nuist.osbank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.nuist.osbank.beans.Ex_Calculate;
import edu.nuist.osbank.beans.Ex_Fillblank;
import edu.nuist.osbank.beans.Ex_Judge;
import edu.nuist.osbank.beans.Ex_ShortAnswer;
import edu.nuist.osbank.beans.Ex_SingleSelection;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.service.ExGetQService;
import edu.nuist.osbank.service.GetRequest;

/**
 * Servlet implementation class AskExServlet
 */
@WebServlet("/AskExServlet")
public class AskExServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println(request.getParameter("data"));
		String strjson=request.getParameter("data");
		//String strjson = "{\"ex_ss\":[2,3,4,5], \"ex_fb\":[2,3], \"ex_judge\":[2,3], \"ex_sa\":[2,3],\"ex_calc\":[2,3]}";
		ExGetQService egq = new ExGetQService();
		List<GetRequest> list = egq.getquest(strjson);
		Gson gson = new Gson();
		String sjson = "";

		String resStr = "{\"data\" :[";
		for (int i = 0; i < list.size(); i++) {
			GetRequest gr = new GetRequest();
			gr = list.get(i);
			String str = "";

			switch (gr.getExType()) {

			case "ex_ss":
				Ex_SingleSelection ss = new Ex_SingleSelection();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = ss.setId(gr.getExId().get(j)).get();
					Ex_SingleSelection e = (Ex_SingleSelection) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;
			case "ex_fb":
				Ex_Fillblank dao = new Ex_Fillblank();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = dao.setId(gr.getExId().get(j)).get();
					Ex_Fillblank e = (Ex_Fillblank) bean;
					resStr += e.toJson();
					resStr += ",";
				}

				break;
			case "ex_judge":
				Ex_Judge jd = new Ex_Judge();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = jd.setId(gr.getExId().get(j)).get();
					Ex_Judge e = (Ex_Judge) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;
			case "ex_sa":
				Ex_ShortAnswer sa = new Ex_ShortAnswer();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = sa.setId(gr.getExId().get(j)).get();
					Ex_ShortAnswer e = (Ex_ShortAnswer) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;

			case "ex_calc":
				Ex_Calculate ca = new Ex_Calculate();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = ca.setId(gr.getExId().get(j)).get();
					Ex_Calculate e = (Ex_Calculate) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;

				

			}

			// 返回数据格式为

			
			

		}
		resStr = resStr.substring(0, resStr.length() - 1);
		resStr += "]}";
		System.out.println(resStr);
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(resStr);
	}

}
