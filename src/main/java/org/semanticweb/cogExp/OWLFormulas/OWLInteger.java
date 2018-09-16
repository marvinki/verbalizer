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




/** Class represents integer values to be part of cardinality restrictions
 * 
 * @author marvin
 *
 */
public class OWLInteger implements OWLAtom{
	private int value;
	
	public OWLInteger(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLInteger))return false;
	    boolean valsSame = value==(((OWLInteger) other).getValue());
	    if (!valsSame){return false;}
	    // System.out.println("class name equal? " + classname + " / " + ((OWLClassName) other).getName() + " AND " + ontologyname + " / " + ((OWLClassName) other).ontologyname);
	    return valsSame;   
	}
	
	@Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + this.value;
        // for (OWLFormula form : this.tail){
        // 	hash = hash * 31 + form.hashCode();
        // }
        return hash;
    }
	
	@Override
	public String toString(){
		return "|" + value + "|";
	}
	
	@Override
	public OWLInteger clone(){
		return new OWLInteger(value);
	}
	
	@Override
	public boolean isSymb() {
		return false;
	}

	@Override
	public boolean isVar() {
		return false;
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
