package edu.nuist.osbank.abenablebean.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

@SuppressWarnings("rawtypes")
class WrapTypeComparator implements Comparator<WrapType> {
	  public int compare(WrapType o1, WrapType o2 ) {
	          return  o1.getIndex() - o2.getIndex() ; 
	  }
}

public class WrapType<T> {
	
		T value = null;
		String type = "";
		int index = -1;
		
		public WrapType(T value){
			this.value = value;
		}
		
		public WrapType(String type, T value){
			this.value = value;
			this.type = type;
		}
		
		public T getValue(){
			return this.value;
		}
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@SuppressWarnings("unchecked")
		public T cast(Object c){
			return (T)c;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
		
		@SuppressWarnings("rawtypes")
		public static void sortWrapBasicTypeByIndex(LinkedList<WrapType> l){
			Collections.sort( l, new WrapTypeComparator());
			//for(WrapType w : l)	System.out.println(w.getIndex());
		}

}
