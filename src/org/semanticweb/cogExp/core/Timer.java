package org.semanticweb.cogExp.core;

import java.util.HashMap;
import java.util.Set;

public enum Timer {
	INSTANCE;
		private HashMap<String, Long> timings = new HashMap<String, Long>();
		private HashMap<String, Long> beginnings = new HashMap<String, Long>();
		
		public void reset(){
			timings = new HashMap<String, Long>();
			beginnings = new HashMap<String, Long>();
		}
		
		public void start(String process){
			beginnings.put(process, System.currentTimeMillis());
			if (!timings.containsKey(process)){
				timings.put(process, new Long(0));
			}
		}
		
		public void stop(String process){
			Long start = beginnings.get(process);
			Long previous = timings.get(process);
			beginnings.remove(process);
			timings.put(process, previous + (System.currentTimeMillis() - start));
		}
		
		public Long report(String process){
			return timings.get(process);
		}
		
		public void report(){
			Set<String> keys = timings.keySet();
			for(String k : keys){
				System.out.println(k + " " + timings.get(k));
		}
		}
		

}
