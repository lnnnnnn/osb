package edu.nuist.osbank.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import net.sf.json.JSONObject;

public class ExGetQService {

	
	public  List<GetRequest>  getquest(String strjson){
		
		JSONObject jsonObject = JSONObject.fromObject(strjson);
		Iterator iterator = jsonObject.keys();
		String typeKey, typeValue;
		List<GetRequest> re = new ArrayList();
		
		
		while (iterator.hasNext()) {
			GetRequest ge = new GetRequest();
			List<Integer> a=new ArrayList();// = new int[10] ;
			int j=0;
			typeKey = (String) iterator.next();
			typeValue = jsonObject.getString(typeKey);
			int len=typeValue.length();
			typeValue=typeValue.substring(1,len-1); 
			//for(int i = 0;i<typeValue.length();i=i+2)
			//	 a[j++]=Integer.parseInt(typeValue.substring(i,i+1)) ;
			String[] ids=typeValue.split(",");
			
			for(int i=0;i<ids.length;i++){
				a.add(Integer.parseInt(ids[i]));
			}
			ge.setExType(typeKey);
			ge.setExId(a);
			re.add(ge);
			 	 
		}
		
		 return re;
		
	}
	
	 
	
}
