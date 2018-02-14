package edu.nuist.osbank.beans;


import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;
@SP(
	     table="ex_ss",
	     sps="",
		 create="create table ex_ss ("+
	     "id int(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY,"+
		 "stem varchar(255), choice varchar(255),answer varchar(1),difdeg int); "		 
	  )
public class Ex_SingleSelection extends DBEnableBean {
  private String stem;
  private String choice;
  private String answer;
  private int difdeg;
  public String getType(){
  	return "ex_ss";
  }
public String getStem() {
	return stem;
}
public Ex_SingleSelection setStem(String stem) {
	this.stem = stem;
	return this;
}
public String getChoice() {
	return choice;
}
public Ex_SingleSelection setChoice(String choice) {
	this.choice = choice;
	return this;
}
public String getAnswer() {
	return answer;
}
public Ex_SingleSelection setAnswer(String answer) {
	this.answer = answer;
	return this;
}
public int getDifdeg() {
	return difdeg;
}
public Ex_SingleSelection setDifdeg(int difdeg) {
	this.difdeg = difdeg;
	return this;
}	

@Override
public String toString() {
	return "Ex_SingleSelection [stem=" + stem + ", choice=" + choice + ", answer=" + answer + ", difdeg=" + difdeg
			+ "]";
}
public String toJson(){
	String js="{"+
			"\"type\":\"ex_ss\","+
			"\"id\":"+this.getId()+","+
			"\"stem\":\""+stem+"\","+
			
            "\"option\":\""+choice+"\","+
			"\"answer\":\""+answer+"\","+
			"\"difdeg\":"+difdeg+""+
			"}";
    return js;         
}

public class InnerEx_SingleSelection{
	private String type="ex_ss";
	private int id=Ex_SingleSelection.this.getId();
	private String option=Ex_SingleSelection.this.choice;
	private String stem=Ex_SingleSelection.this.stem;
	private String answer=Ex_SingleSelection.this.answer;
	private int difdeg=Ex_SingleSelection.this.difdeg;
}
	public static void main(String[]args){
		
	  Ex_SingleSelection s=new Ex_SingleSelection().setStem("xxx").setChoice("yy").setAnswer("A").setDifdeg(12);
	  new DBTableInitor(Ex_SingleSelection.class,s);
	
	}
		

}
  
  

