package edu.nuist.osbank.beans;

import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;

@SP(table = "ex_calc", sps = "", create = "create table ex_calc (" + "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY,"
		+ "stem text, answer text,difdeg int,score int); ")
public class Ex_Calculate extends DBEnableBean {
	
	private String stem;
	private String answer;
	private int difdeg;
	private int score;
    public String getType(){
    	return "ex_calc";
    }
	public String getStem() {
		return stem;
	}

	public Ex_Calculate setStem(String stem) {
		this.stem = stem;
		return this;
	}

	public String getAnswer() {
		return answer;
	}

	public Ex_Calculate setAnswer(String answer) {
		this.answer = answer;
		return this;
	}

	public int getDifdeg() {
		return difdeg;
	}

	public Ex_Calculate setDifdeg(int difdeg) {
		this.difdeg = difdeg;
		return this;
	}
	
	
	public int getScore() {
		return score;
	}
	public Ex_Calculate setScore(int score) {
		this.score = score;
		return this;
	}
	public String toJson(){
		String js="{"+
				"\"type\":\"ex_calc\","+
				"\"id\":"+this.getId()+","+
				"\"stem\":\""+stem+"\","+
				
	          
				"\"answer\":\""+answer+"\","+
				"\"difdeg\":"+difdeg+""+
				"}";
	    return js;         
	}
	public class InnerEx_Calculate{
		private String type="ex_calc";
		private int id=Ex_Calculate.this.getId();
		private String stem=Ex_Calculate.this.stem;
		private String answer=Ex_Calculate.this.answer;
		private int difdeg=Ex_Calculate.this.difdeg;
	}
	public static void main(String[] args) {

		Ex_Calculate s = new Ex_Calculate().setStem("xxx").setAnswer("sssss").setDifdeg(1);
		new DBTableInitor(Ex_Calculate.class, s);

	}
}
