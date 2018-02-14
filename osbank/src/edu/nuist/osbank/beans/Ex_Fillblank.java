package edu.nuist.osbank.beans;

import java.util.LinkedList;

import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;

@SP(table = "ex_fb",sps = "", create = "create table ex_fb (" + "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY,"
		+ "stem varchar(255), answer varchar(255),difdeg int); ")
public class Ex_Fillblank extends DBEnableBean {
	private String stem;
	private String answer;
	private int difdeg;
	 public String getType(){
	    	return "ex_fb";
	    }
	public String getStem() {
		return stem;
	}

	public Ex_Fillblank setStem(String stem) {
		this.stem = stem;
		return this;
	}

	public String getAnswer() {
		return answer;
	}

	public Ex_Fillblank setAnswer(String answer) {
		this.answer = answer;
		return this;
	}

	public int getDifdeg() {
		return difdeg;
	}

	public Ex_Fillblank setDifdeg(int difdeg) {
		this.difdeg = difdeg;
		return this;
	}
	public Ex_Fillblank getData(int id){
		Ex_Fillblank s = new Ex_Fillblank();
		s.setId(id);
		
		LinkedList<DBEnableBean> list = s.query("find");
		if(list.size()>0)
		{
			s = (Ex_Fillblank)list.get(0);
			return s;
		}
		else 
			return null;
		
	}
	
	// 无法使用Gson 自定义toJson 
	
	public String toJson(){
		String js="{"+
				"\"type\":\"ex_fb\","+
				"\"id\":"+this.getId()+","+
				"\"stem\":\""+stem+"\","+
				"\"answer\":"+answer+","+
				"\"difdeg\":"+difdeg+""+
				"}";
	    return js;         
	}
	public class InnerEx_Fillblank{
		private String type="ex_fb";
		private int id=Ex_Fillblank.this.getId();
		private String stem=Ex_Fillblank.this.stem;
		private String answer=Ex_Fillblank.this.answer;
		private int difdeg=Ex_Fillblank.this.difdeg;
	}
	public static void main(String[] args) {

		Ex_Fillblank s = new Ex_Fillblank().setStem("xxx").setAnswer("{\"1\":\"aa\"}").setDifdeg(1);
		new DBTableInitor(Ex_Fillblank.class, s);

	}
}
