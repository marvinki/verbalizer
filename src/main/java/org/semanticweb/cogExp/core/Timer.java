/*
 *     Copyright 2012-2018 Ulm University, AI Institute
 *     Main author: Marvin Schiller, contributors: Felix Paffrath, Chunhui Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

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
