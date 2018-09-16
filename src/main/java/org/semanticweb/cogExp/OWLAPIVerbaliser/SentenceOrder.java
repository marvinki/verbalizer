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

/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

/**
 * @author fpaffrath
 * self explanatory
 */
public enum SentenceOrder {

	is_A_B,
	A_is_B,
	A_B_is, 
	noOrder;
	
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		switch (this) {
		case is_A_B:
			return "is_A_B";
			
		case A_is_B:
			return "A_is_B";

			
		case A_B_is:
			return "A_B_is";

		case noOrder:
			return "noOrder";
			
		default:
			return "default";
			
		}
	}
	
}
