package edu.nuist.osbank.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import edu.nuist.osbank.abenablebean.util.FileUtil;

/**
 *
 * @author iText
 */
public class ParseHtmlTable {

	 String dir;
	public static final String HTML = "pdf.html";
	public static final String DEST = "html_pdf.pdf";
	
	
	public String getDir() {
		return dir;
	}

	public ParseHtmlTable setDir(String dir) {
		this.dir = dir;
		return this;
	}

	public static void main(String[] args) throws IOException, DocumentException {
		/*File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ParseHtmlTable().createPdf(DEST);*/
		String htmlStr="";		
		new ParseHtmlTable().setDir("Webcontent/download/").convertToPdf(htmlStr);
	}

	public void convertToPdf(String htmlStr){
		System.out.println("dir is:"+dir);
		//将字符串写入html文件
		if(!htmlStr.equals(""))
		FileUtil.writeStringToFile(htmlStr, dir+HTML);
		//将html转为pdf
		File file = new File(dir+DEST);
		try {
			createPdf(dir+DEST);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createPdf(String file) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		// step 3
		document.open();
		// step 4
		System.out.println("dir is:"+dir);
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(dir+HTML), Charset.forName("UTF-8"));
		// step 5
		document.close();
	}

	
}