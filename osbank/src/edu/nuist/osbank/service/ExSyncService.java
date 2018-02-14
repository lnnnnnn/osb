package edu.nuist.osbank.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import edu.nuist.osbank.abenablebean.util.DBTableCheckor;
import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.beans.Admin;
import edu.nuist.osbank.beans.Ex_Calculate;
import edu.nuist.osbank.beans.Ex_Fillblank;
import edu.nuist.osbank.beans.Ex_Judge;
import edu.nuist.osbank.beans.Ex_ShortAnswer;
import edu.nuist.osbank.beans.Ex_SingleSelection;
import edu.nuist.osbank.beans.GradeStatus;
import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.beans.User_Task;
import net.sf.json.JSONObject;

public class ExSyncService {

	public static void main(String[] args) {
		syncExData();
	}

	public static void syncExData() {
		initEnvironment();
		String fStr = readFromTxt();
		JSONObject jsonObject = JSONObject.fromObject(fStr);
		Iterator iterator = jsonObject.keys();
		String typeKey, typeValue;
		//遍历题型
		while (iterator.hasNext()) {
			typeKey = (String) iterator.next();
			typeValue = jsonObject.getString(typeKey);
			int len=typeValue.length();
			typeValue=typeValue.substring(1,len-1);//去除中括号[]
			typeValue=typeValue.replace("},{", "},,,{");//便于用,,,分割字符串
			switch (typeKey){
			
			case "ex_ss":{
				Ex_SingleSelection ss=new Ex_SingleSelection();
                String[] exs=typeValue.split(",,,");
                
                for(String item:exs){
                	JSONObject exj=JSONObject.fromObject(item);
                	ss.setStem(exj.getString("stem")).setChoice(exj.getString("option"))
                	.setAnswer(exj.getString("answer")).setDifdeg(exj.getInt("difdeg"));
                	
                	ss.insert();
                }
                
                break;
			}
			
			case "ex_judge":{
				Ex_Judge ss=new Ex_Judge();
                String[] exs=typeValue.split(",,,");
                
                for(String item:exs){
                	JSONObject exj=JSONObject.fromObject(item);
                	ss.setStem(exj.getString("stem"))
                	.setAnswer(exj.getString("answer")).setDifdeg(exj.getInt("difdeg"));
                	
                	ss.insert();
                }
                
                break;
			}
			
			
			case "ex_fb":{
				Ex_Fillblank ss=new Ex_Fillblank();
                String[] exs=typeValue.split(",,,");
               
                for(String item:exs){
                	JSONObject exj=JSONObject.fromObject(item);
                	ss.setStem(exj.getString("stem"))
                	.setAnswer(exj.getString("answer")).setDifdeg(exj.getInt("difdeg"));
                	
                	ss.insert();
                }
                
                break;
			}
			
			case "ex_sa":{
				Ex_ShortAnswer ss=new Ex_ShortAnswer();
                String[] exs=typeValue.split(",,,");
               
                for(String item:exs){
                	JSONObject exj=JSONObject.fromObject(item);
                	ss.setStem(exj.getString("stem"))
                	.setAnswer(exj.getString("answer")).setDifdeg(exj.getInt("difdeg"))
                	.setScore(exj.getInt("score"));;
                	
                	ss.insert();
                }
                
                break;
			}
			
			case "ex_calc":{
				Ex_Calculate ss=new Ex_Calculate();
                String[] exs=typeValue.split(",,,");
               
                for(String item:exs){
                	JSONObject exj=JSONObject.fromObject(item);
                	ss.setStem(exj.getString("stem"))
                	.setAnswer(exj.getString("answer")).setDifdeg(exj.getInt("difdeg"))
                	.setScore(exj.getInt("score"));
                	
                	ss.insert();
                }
                
                break;
			}
			}
		}
	}

	public static String readFromTxt() {
		String rtStr = "";
		String fileName = "C:\\exjson.txt";
		File file = new File(fileName);
		StringBuffer stringBuffer = new StringBuffer();
		try {
			InputStream is = new FileInputStream(file);
			// 由于系统编码与程序编码不同 需指定编码格式 防止中文乱码
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
			String str = "";
			while ((str = br.readLine()) != null) {
				stringBuffer.append(str);
			}
			rtStr = new String(stringBuffer.toString().getBytes());
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(rtStr);
		return rtStr;
	}
	
	public static void initEnvironment(){
		boolean LinkTableIsExist = new DBTableCheckor(Ex_SingleSelection.class).isTableExist(new Ex_SingleSelection().getTableName());
		if (!LinkTableIsExist) new DBTableInitor(Ex_SingleSelection.class, new Ex_SingleSelection());
	
		 LinkTableIsExist = new DBTableCheckor(Ex_Fillblank.class).isTableExist(new Ex_Fillblank().getTableName());
		if (!LinkTableIsExist) new DBTableInitor(Ex_Fillblank.class, new Ex_Fillblank());
	
		 LinkTableIsExist = new DBTableCheckor(Ex_Judge.class).isTableExist(new Ex_Judge().getTableName());
		if (!LinkTableIsExist) new DBTableInitor(Ex_Judge.class, new Ex_Judge());
	
		 LinkTableIsExist = new DBTableCheckor(Ex_ShortAnswer.class).isTableExist(new Ex_ShortAnswer().getTableName());
		if (!LinkTableIsExist) new DBTableInitor(Ex_ShortAnswer.class, new Ex_ShortAnswer());
	
		 LinkTableIsExist = new DBTableCheckor(Ex_Calculate.class).isTableExist(new Ex_Calculate().getTableName());
		if (!LinkTableIsExist) new DBTableInitor(Ex_Calculate.class, new Ex_Calculate());
	
		 LinkTableIsExist = new DBTableCheckor(User_Ex.class).isTableExist(new User_Ex().getTableName());
		if (!LinkTableIsExist) new DBTableInitor(User_Ex.class, new User_Ex());
		
		 LinkTableIsExist = new DBTableCheckor(User_Task.class).isTableExist(new User_Task().getTableName());
			if (!LinkTableIsExist) new DBTableInitor(User_Task.class, new User_Task());
        
			
			 LinkTableIsExist = new DBTableCheckor(GradeStatus.class).isTableExist(new GradeStatus().getTableName());
				if (!LinkTableIsExist) new DBTableInitor(GradeStatus.class, new GradeStatus());

				
				 LinkTableIsExist = new DBTableCheckor(Admin.class).isTableExist(new Admin().getTableName());
					if (!LinkTableIsExist) new DBTableInitor(Admin.class, new Admin().setName("admin").setPassword("admin"));

				
	}
}
