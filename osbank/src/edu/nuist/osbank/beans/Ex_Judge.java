package edu.nuist.osbank.beans;

import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;

@SP(table = "ex_judge", sps = "", create = "create table ex_judge ("
		+ "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY," + "stem varchar(255), answer varchar(1),difdeg int); ")
public class Ex_Judge extends DBEnableBean {
	private String stem;
	private String answer;
	private int difdeg;
	 public String getType(){
	    	return "ex_judge";
	    }
	public String getStem() {
		return stem;
	}

	public Ex_Judge setStem(String stem) {
		this.stem = stem;
		return this;
	}

	public String getAnswer() {
		return answer;
	}

	public Ex_Judge setAnswer(String answer) {
		this.answer = answer;
		return this;
	}

	public int getDifdeg() {
		return difdeg;
	}

	public Ex_Judge setDifdeg(int difdeg) {
		this.difdeg = difdeg;
		return this;
	}
	
	public String toJson(){
		String js="{"+
				"\"type\":\"ex_judge\","+
				"\"id\":"+this.getId()+","+
				"\"stem\":\""+stem+"\","+
				
	          
				"\"answer\":\""+answer+"\","+
				"\"difdeg\":"+difdeg+""+
				"}";
	    return js;         
	}
	public class InnerEx_Judge{
		private String type="ex_judge";
		private int id=Ex_Judge.this.getId();
		private String stem=Ex_Judge.this.stem;
		private String answer=Ex_Judge.this.answer;
		private int difdeg=Ex_Judge.this.difdeg;
	}
	public static void main(String[] args) {

		Ex_Judge s = new Ex_Judge().setStem("xxx").setAnswer("F").setDifdeg(1);
		new DBTableInitor(Ex_Judge.class, s);

	}
}
