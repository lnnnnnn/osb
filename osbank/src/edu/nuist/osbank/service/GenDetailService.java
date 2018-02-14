package edu.nuist.osbank.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.nuist.osbank.beans.Ex_Calculate;
import edu.nuist.osbank.beans.Ex_Fillblank;
import edu.nuist.osbank.beans.Ex_Judge;
import edu.nuist.osbank.beans.Ex_ShortAnswer;
import edu.nuist.osbank.beans.Ex_SingleSelection;
import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import net.sf.json.JSONObject;

public class GenDetailService {
    ArrayList<DBEnableBean> exJsons;
    ArrayList<User_Ex> ueJsons;
    ArrayList<String> permuAns=new ArrayList() ;
   
    
    
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		ArrayList<DBEnableBean> exJsons=new ArrayList();
	       
	       Ex_Judge s = new Ex_Judge().setStem("xxx").setAnswer("F").setDifdeg(1);
	       exJsons.add(s);
	       
	       ArrayList<User_Ex> ueJsons=new ArrayList();
	       
	       User_Ex ue=new User_Ex();
			ue.setExType("ex_judge").setExId(2).setCreatedDate(new java.sql.Date(new Date().getTime())).setAnswer("F");
            ueJsons.add(ue);
            
            GenDetailService gd=  new GenDetailService();
            gd.setExJsons(exJsons);
            gd.setUeJsons(ueJsons);
            
       /*List<DBEnableBean> exJsons=new ArrayList();
       
       Ex_Judge s = new Ex_Judge().setStem("xxx").setAnswer("F").setDifdeg(1);
       exJsons.add(s);
       
       System.out.println(exJsons.get(0).getType());*/
		/*String str="{\"1\":\"[\"a\",\"b\"]\"}";
		 JSONObject obj=JSONObject.fromObject(str);*/
		 
		 /*try{
			 
		 }catch 
			String unorder = exj.getString("unorder");
			System.out.println(unorder);*/
		 /*boolean unorderEst=false;
		 Iterator it = obj.keys();  
         while (it.hasNext()) {  
        	 
             String key = (String) it.next(); 
             
             if(key=="unordered") unorderEst=true;
             String value = obj.getString(key);  
             Array array = obj.getArray(key);  
             for(int i=0;i<array.length();i++){ 
            	 
             }
            // System.out.println(key+" "+value);
         }
         System.out.println(unorderEst);*/
		 /*String ans="[1,2,3]";
		 String[] tmpAns=ans.substring(1, ans.length()-1).split(",");
		 for(String i:tmpAns){
			 System.out.println(i);
		 }*/
		 //System.out.println(tmpAns); 
		/*String []answers={"aa","bb","cc"};
		ArrayList<String> permuAns=new GenDetailService().getAllAnswers(answers);
		
		for(int i=0;i<permuAns.size();i++){
			System.out.println(permuAns.get(i));
		}*/
		
		
	}
	
	public String[] genDetailHtml(){
		int size=exJsons.size();
		String[] detailHtmls=new String[size];
		
		return detailHtmls;
	}
	/*public  String[] genDetailItemHtml(int exId,String[] detailHtmls) {
	
		
		switch(exJsons.get(exId).getType()) {
			case "ex_ss":
				{
                    Ex_SingleSelection exi=(Ex_SingleSelection)exJsons.get(exId);
					String[] options = exi.getChoice().split("&");
					String optHtml = "<ul class=\"stem\">";
					for(int i = 0; i < options.length; i++) {
						optHtml += "<li>" + options[i] + "</li>";
					}
					optHtml += "</ul>";


					String exHtml = "<div class=\"ex-detail-item\">" +
						"<p class=\"stem\"><span class=\"question-id\">" + (exId + 1) + "、</span>" +exi.getStem() + "</p>" +
						optHtml +
												
						"<p> 你的答案：<span>" + ueJsons.get(exId).getAnswer() + "</span> 参考答案：<span>" + exi.getAnswer()  + "</span></p>" +
						"</div>";

					detailHtmls[exId]= exHtml;

					break;

				}
			case "ex_judge":
				{
					 Ex_Judge exi=(Ex_Judge)exJsons.get(exId);

					String exHtml = "<div class=\"ex-detail-item\">" +
							"<p class=\"stem\"><span class=\"question-id\">" + (exId + 1) + "、</span>" +exi.getStem() + "</p>" +
													
							"<p> 你的答案：<span>" + ueJsons.get(exId).getAnswer() + "</span> 参考答案：<span>" + exi.getAnswer()  + "</span></p>" +
							"</div>";

					detailHtmls[exId] = exHtml;

					break;
				}
			case "ex_fb":
				{
					
					 Ex_Fillblank exi=(Ex_Fillblank)exJsons.get(exId);
					String[] blanks = ueJsons.get(exId).getAnswer().split(","); //若答案中没有逗号，只有一个空，也能正确获取

				    String ansJsonStr = exi.getAnswer();
				    JSONObject exj=JSONObject.fromObject(ansJsonStr);
					
				    
				    
				   
				    
				    
				    boolean unorderEst=false;
				    String unorder="";
					 Iterator it = exj.keys();  
			         while (it.hasNext()) {  
			        	 
			             String key = (String) it.next(); 
			             
			             if(key=="unordered") unorderEst=true;
			              unorder=exj.getString(key);
			            
			         }
					String exHtml = "<div class=\"ex-detail-item\">" +
							"<p class=\"stem\"><span class=\"question-id\">" + (exId + 1) + "、</span>" +exi.getStem() + "</p>" ;

					//如果答案中不存在无序项

					if(unorderEst == false) {
						List<String>bans=new ArrayList()		;
						//遍历标准答案的answerJson 取出到bans
						 it = exj.keys();  
				         while (it.hasNext()) {  
				        	 
				             String key = (String) it.next(); 
				             
				             String value = exj.getString(key); 
				            bans.add(value);
				         }
				         
				         List<String>userAns=new ArrayList()		;
						for(int i = 0; i < blanks.length; i++) {
							String iconClass, referenceStr = "";
							userAns.add(blanks[i]); //= ;
								//var bans = ansJson.("b" + i);
							if( bans.get(i).charAt(0) == '[') { //答案为数组,即此空有多个答案，匹配其中一个则正确
								iconClass = "remove";
								String[] tmpAns=bans.get(i).substring(1, bans.get(i).length()-1).split(",");
								for(String j : tmpAns) {

									if(j == blanks[i]) {
										iconClass = "ok";
									}
									referenceStr += (j + '/');
								}
								referenceStr = referenceStr.substring(0, referenceStr.length() - 1); //去除最后一个/
							} else { //此空仅有一个答案

								referenceStr = bans.get(i);
							}

							String item="<p> 你的答案：<span>" + blanks[i] + "</span> 参考答案：<span>" + referenceStr + "</span></p>";

							exHtml += item;
						}

						exHtml += "</div>";

						detailHtmls[exId] = exHtml;
					} else { //答案无序
						//空全填对才算正确,否则错误
						ArrayList<String> permuAns=new GenDetailService().getAllAnswers(answers);
						
						for(int i=0;i<permuAns.size();i++){
							System.out.println(permuAns.get(i));
						}
						var allAnswers = getAllAnswers(unorder);

						var iconClass = "remove",
							integrateStr = "",
							showStr = "",
							userAns = []; //用户答案组合在一起
					
						String showStr="";
						for(int i = 0; i < blanks.length; i++) {

							showStr += (blanks[i] + " ");
						}

						

						String item =
							"<p> 你的答案：<span>" + showStr + "</span> 参考答案：<span>" + unorder + "</span></p>";

						exHtml += item;

						exHtml += "</div>";

						detailHtmls[exId] = exHtml;
					}

					break;
				}
			case "ex_sa":
				{
					Ex_ShortAnswer exi=(Ex_ShortAnswer)exJsons.get(exId);
					String exHtml = "<div class=\"ex-detail-item\">" +
							"<p class=\"stem\"><span class=\"question-id\">" + (exId + 1) + "、</span>" +exi.getStem() + "</p>" +


						"<p>得分：<strong class=\"score\">" + (ueJsons.get(exId).getEvaluation() == "" ? "老师尚未给你打分" : ueJsons.get(exId).getEvaluation()) + "</strong></p>" +
						"<a class=\"user-answer-tag\">你的答案</a>" +
						"<p class=\"user-answer\">" + ueJsons.get(exId).getAnswer() + "</p>" +
						"<a class=\"refer-answer-tag\">参考答案</a>" +

						"<p class=\"refer-answer\">" +exi.getAnswer() +
						"</p>" +
						"</div>";

					detailHtmls[exId] = exHtml;
					break;
				}
			case "ex_calc":
				{
					
					Ex_Calculate exi=(Ex_Calculate)exJsons.get(exId);
					String exHtml = "<div class=\"ex-detail-item\">" +
							"<p class=\"stem\"><span class=\"question-id\">" + (exId + 1) + "、</span>" +exi.getStem() + "</p>" +


						"<p>得分：<strong class=\"score\">" + (ueJsons.get(exId).getEvaluation() == "" ? "老师尚未给你打分" : ueJsons.get(exId).getEvaluation()) + "</strong></p>" +
						"<a class=\"user-answer-tag\">你的答案</a>" +
						"<br></br>"+
		                "<img  src=\"'+ueJsons[exId].imgSrc+'\"/>"+
		                "<p class=\"user-answer\">" + ueJsons.get(exId).getAnswer() + "</p>" +
						"<a class=\"refer-answer-tag\">参考答案</a>" +

						"<p class=\"refer-answer\">" + exi.getAnswer() +
						"</p>" +
						"</div>";

					detailHtmls[exId] = exHtml;
					break;
				}
		}

	}
*/	public  ArrayList<String>  getAllAnswers(String[]answers) {
		int len = answers.length;

			int []mark=new int[len] ;

		for(int j = 0; j < len; j++) mark[j] = 0;
		 String[] tmpAns =new String[len];
		permutation(0,len,mark,answers,tmpAns);
        return permuAns;
	}
		public void  permutation(int cur,int len,int[]mark,String[]answers,String[]tmpAns)

		{
			if(cur == len) {
				String tmp = "";
				for(int i = 0; i < len; i++) tmp += tmpAns[i];
				//console.log(tmp);
				permuAns.add(tmp);
			}
			for(int i = 0; i < len; i++) {
				if(mark[i]==0){
					tmpAns[cur] = answers[i];
					mark[i] = 1;
					permutation(cur + 1,len,mark,answers,tmpAns);
					mark[i] = 0;
				}
			}
		}

		
		public ArrayList<DBEnableBean> getExJsons() {
			return exJsons;
		}

		public void setExJsons(ArrayList<DBEnableBean> exJsons) {
			this.exJsons = exJsons;
		}

		public ArrayList<User_Ex> getUeJsons() {
			return ueJsons;
		}

		public void setUeJsons(ArrayList<User_Ex> ueJsons) {
			this.ueJsons = ueJsons;
		}

}
