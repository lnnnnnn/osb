package edu.nuist.osbank.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.nuist.osbank.service.ParseHtmlTable;

/**
 * Servlet implementation class ServletDownload
 */
@WebServlet("/ServletDownload")
public class ServletDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	/*	String reqStr=request.getParameter("pdfContent");
		System.out.println(reqStr);
		String htmlStr="";		
		
		//htmlStr = URLDecoder.decode(reqStr,"UTF-8");
		htmlStr=reqStr;
		System.out.println("the pdfcontent is:"+htmlStr);
		
		//生成html_pdf.pdf
		String dir = getServletContext().getRealPath("/download/");
		System.out.println(dir);
		new ParseHtmlTable().setDir(dir).convertToPdf(htmlStr);*/
		
		    String filename ="html_pdf.pdf";  
	        System.out.println(filename);  
	          
	        //设置文件MIME类型  
	        response.setContentType(getServletContext().getMimeType(filename));  
	        //设置Content-Disposition  
	        response.setHeader("Content-Disposition", "attachment;filename="+filename);  
	        //读取目标文件，通过response将目标文件写到客户端  
	        //获取目标文件的绝对路径  
	        String fullFileName = getServletContext().getRealPath("/download/" + filename);  
	        System.out.println("the path of pdf:"+fullFileName);  
	        //读取文件  
	        InputStream in = new FileInputStream(fullFileName);  
	        OutputStream out = response.getOutputStream();  
	          
	        //写文件  
	        int b;  
	        while((b=in.read())!= -1)  
	        {  
	            out.write(b);  
	        }  
	          
	        in.close();  
	        out.close(); 
	}

	

}
