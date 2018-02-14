package edu.nuist.osbank.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nuist.osbank.service.ParseHtmlTable;

/**
 * Servlet implementation class ConvertToPdfServlet
 */
@WebServlet("/ConvertToPdfServlet")
public class ConvertToPdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		String reqStr=request.getParameter("pdfContent");
		System.out.println(reqStr);
		String htmlStr="";		
		
		//htmlStr = URLDecoder.decode(reqStr,"UTF-8");
		htmlStr=reqStr;
		System.out.println("the pdfcontent is:"+htmlStr);
		
		//生成html_pdf.pdf
		String dir = getServletContext().getRealPath("/download/");
		System.out.println(dir);
		new ParseHtmlTable().setDir(dir).convertToPdf(htmlStr);
	
	}
}
