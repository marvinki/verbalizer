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


/** Class represents literal values to be part of OWLFormula-s
 * 
 * @author marvin
 *
 */
public class OWLLiteralValue implements OWLAtom{
	private String value;
	private OWLLiteralType type;
	// private String ontologyname;
	
	public OWLLiteralValue(String value, OWLLiteralType type){
		this.value = value;
		this.type = type;
		// this.ontologyname = ontologyname;
	}
	
	public String getValue(){
		return value;
	}
	
	public OWLLiteralType getType(){
		return type;
	}
	
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLLiteralValue))return false;
	    boolean nameSame = value.equals(((OWLLiteralValue) other).getValue());
	    if (!nameSame){return false;}
	    boolean typeSame = type.equals(((OWLLiteralValue) other).type);
	    // System.out.println("class name equal? " + classname + " / " + ((OWLClassName) other).getName() + " AND " + ontologyname + " / " + ((OWLClassName) other).ontologyname);
	    return nameSame && typeSame;   
	}
	
	@Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + value.hashCode();
        hash = hash * 31 + type.hashCode();
        return hash;
    }
	
	@Override
	public String toString(){
		return "<<" + value + ">>";
	}
	
	@Override
	public OWLLiteralValue clone(){
		return new OWLLiteralValue(value, type);
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
