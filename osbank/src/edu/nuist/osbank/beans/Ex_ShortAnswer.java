package edu.nuist.osbank.beans;

import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;

@SP(table = "ex_sa", sps = "", create = "create table ex_sa (" + "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY,"
		+ "stem text, answer text,difdeg int,score int); ")
public class Ex_ShortAnswer extends DBEnableBean {
	private String stem;
	private String answer;
	private int difdeg;
	private int score;
	 public String getType(){
	    	return "ex_sa";
	    }
	public String getStem() {
		return stem;
	}

	public Ex_ShortAnswer setStem(String stem) {
		this.stem = stem;
		return this;
	}

	public String getAnswer() {
		return answer;
	}

	public Ex_ShortAnswer setAnswer(String answer) {
		this.answer = answer;
		return this;
	}

	public int getDifdeg() {
		return difdeg;
	}

	public Ex_ShortAnswer setDifdeg(int difdeg) {
		this.difdeg = difdeg;
		return this;
	}

	
	public int getScore() {
		return score;
	}
	public Ex_ShortAnswer setScore(int score) {
		this.score = score;
		return this;
	}
	public String toJson() {
		String js = "{" + "\"type\":\"ex_sa\"," + "\"id\":" + this.getId() + "," + "\"stem\":\"" + stem + "\"," +

				"\"answer\":\"" + answer + "\"," + "\"difdeg\":" + difdeg + "" + "}";
		return js;
	}

	public static void main(String[] args) {

		Ex_ShortAnswer s = new Ex_ShortAnswer().setStem("xxx").setAnswer("sssss").setDifdeg(1);
		new DBTableInitor(Ex_ShortAnswer.class, s);

	}
}
