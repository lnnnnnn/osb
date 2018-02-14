package edu.nuist.osbank.test;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.nuist.osbank.beans.Ex_Calculate;
import edu.nuist.osbank.beans.Ex_Fillblank;
import edu.nuist.osbank.beans.Ex_Judge;
import edu.nuist.osbank.beans.Ex_ShortAnswer;
import edu.nuist.osbank.beans.Ex_SingleSelection;
import edu.nuist.osbank.beans.User_Ex;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.service.ExGetQService;
import edu.nuist.osbank.service.GetRequest;

public class GsonTest_1 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testUserEx();
		//getUserEx();
		getOneUserEx();
	}
	public static void getOneUserEx(){
		Gson gson=new Gson(); 
		String strjson="{\"userId\":\"11111111111\",\"exType\":\"ex_ss\",\"exId\":2,\"answer\":\"\",\"evaluation\":\"\"}";
		User_Ex ss=gson.fromJson(strjson,User_Ex.class);
		System.out.println(ss);
	}
	public static void getUserEx(){
		//不完整的user_ex信息
		String strjson="[{\"userId\":\"11111111111\",\"exType\":\"ex_ss\",\"exId\":2,\"answer\":\"\",\"evaluation\":\"\"},{\"userId\":\"11111111111\",\"exType\":\"ex_ss\",\"exId\":3,\"answer\":\"\",\"evaluation\":\"\"}]";
		Gson gson = new Gson();
		List<User_Ex> ues = gson.fromJson(strjson, new TypeToken<List<User_Ex>>() {
		}.getType());
		System.out.println(ues.size());
		String resJson="[";
		for (int i = 0; i < ues.size(); i++) {
			User_Ex ue = ues.get(i);
			LinkedList<DBEnableBean> beans=ue.query("getByUseridAndEx");
			
			if(beans!=null&&beans.size()>0){
				User_Ex cue=(User_Ex)beans.get(0);
				resJson+=gson.toJson(cue.new InnerUser_Ex());
				if(i<(ues.size()-1))
					resJson+=",";
				
			}
			
			
		}
		
		resJson+="]";
		
		System.out.println(resJson);
	}
	

	public static void testUserEx() {
		String strjson = "[ { " +

				"\"userId\":\"20141333007\"," + "\"exType\":\"ex_ss\"," + "\"exId\":2," + "\"answer\":\"A\","
				+ "\"evaluation\":\"正确\"" + "}," + "{" +

				"\"userId\":\"66666666666\"," + "\"exType\":\"ex_calc\"," + "\"exId\":2," + "\"answer\":\"aaaaaadddd\","
				+ "\"evaluation\":\"\"" + "}" +

				"]";
       
		System.out.println(strjson);
		Gson gson = new Gson();
		List<User_Ex> ps = gson.fromJson(strjson, new TypeToken<List<User_Ex>>() {
		}.getType());
		for (int i = 0; i < ps.size(); i++) {
			User_Ex p = ps.get(i);
			System.out.println(p.toString());
			System.out.println(p.getExType());
			if (p.getExType().equals("ex_calc")) {
				User_Ex.updateInfoByUseridAndEx(p);
			} else {
				p.insert();
			}

		}
	}
   public static void askEx(){
	   
	   String strjson = "{\"ex_ss\":[2,3,4,5], \"ex_fb\":[2,3], \"ex_judge\":[2,3], \"ex_sa\":[2,3],\"ex_calc\":[2,3]}";
		ExGetQService egq = new ExGetQService();
		List<GetRequest> list = egq.getquest(strjson);
		Gson gson = new Gson();
		String sjson = "";

		String resStr = "{\"data\" :[";
		for (int i = 0; i < list.size(); i++) {
			GetRequest gr = new GetRequest();
			gr = list.get(i);
			String str = "";

			switch (gr.getExType()) {

			case "ex_ss":
				Ex_SingleSelection ss = new Ex_SingleSelection();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = ss.setId(gr.getExId().get(j)).get();
					Ex_SingleSelection e = (Ex_SingleSelection) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;
			case "ex_fb":
				Ex_Fillblank dao = new Ex_Fillblank();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = dao.setId(gr.getExId().get(j)).get();
					Ex_Fillblank e = (Ex_Fillblank) bean;
					resStr += e.toJson();
					resStr += ",";
				}

				break;
			case "ex_judge":
				Ex_Judge jd = new Ex_Judge();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = jd.setId(gr.getExId().get(j)).get();
					Ex_Judge e = (Ex_Judge) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;
			case "ex_sa":
				Ex_ShortAnswer sa = new Ex_ShortAnswer();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = sa.setId(gr.getExId().get(j)).get();
					Ex_ShortAnswer e = (Ex_ShortAnswer) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;

			case "ex_calc":
				Ex_Calculate ca = new Ex_Calculate();
				for (int j = 0; j < gr.getExId().size(); j++) {

					DBEnableBean bean = ca.setId(gr.getExId().get(j)).get();
					Ex_Calculate e = (Ex_Calculate) bean;
					resStr += e.toJson();
					resStr += ",";
				}
				break;

				

			}

			// 返回数据格式为

			
			

		}
		resStr = resStr.substring(0, resStr.length() - 1);
		resStr += "]}";
		System.out.println(resStr);
   }
	
}
