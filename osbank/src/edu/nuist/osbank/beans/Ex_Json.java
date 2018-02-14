package edu.nuist.osbank.beans;

public class Ex_Json {
	/*"type": "ex_calc",
	"id": 2,
	"stem": "假设有 4 道作业，它们提交的时刻及执行时间由下表给出，计算在单道程序环境下，采用先来先服务调度算法和最短作业优先算法的平均周转时间和平均带权周转时间，并指出它们的调度顺序。<table><tr><td>作业号</td><td>提交时刻 ( 小时 )</td><td>执行时间 ( 小时 )</td></tr><tr><td>1</td><td>10:00</td><td>2</td></tr><tr><td>2</td><td>10:20</td><td>1</td></tr><tr><td>3</td><td>10:40</td><td>0.5</td></tr><tr><td>4</td><td>10:50</td><td>0.4</td></tr></table>",
	"answer": "略",
	"difdeg": 4*/
	
	private String type;
    private String id;
    private String stem;
    private String answer;
    private String difdeg;
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStem() {
		return stem;
	}

	public void setStem(String stem) {
		this.stem = stem;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getDifdeg() {
		return difdeg;
	}

	public void setDifdeg(String difdeg) {
		this.difdeg = difdeg;
	}

	public Ex_Json(String type, String id, String stem, String answer, String difdeg) {
		super();
		this.type = type;
		this.id = id;
		this.stem = stem;
		this.answer = answer;
		this.difdeg = difdeg;
	}
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ex_Json e=new Ex_Json("ex_ss","2","dfsd","A","3");
		System.out.println(e.type);
	}

}
