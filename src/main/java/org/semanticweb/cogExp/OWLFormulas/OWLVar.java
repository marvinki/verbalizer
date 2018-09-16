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

package org.semanticweb.cogExp.OWLFormulas;



/** This class represents variables (to be used for matching in OWLFormulas)
 * 
 * @author marvin
 *
 */
public class OWLVar implements OWLAtom {
	private String varname;
	
	public OWLVar(String name){
		varname = name;
	}
	
	public String getName(){
		return varname;
	}	
	
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLVar))return false;
	    return varname.equals(((OWLVar) other).getName());
	}
	
	@Override
	public String toString(){
		return "{" + varname + "}";
	}
	
	@Override
	public boolean isSymb() {
		return false;
	}

	@Override
	public boolean isVar() {
		return true;
	}

	@Override
	public boolean isClassName() {
		return false;
	}
	
	@Override
	public boolean isIndividual() {
		return false;
	}
	
	@Override
	public String toNLString() {
		return this.toString();
	}
	
}
