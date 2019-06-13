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

import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPIManagerManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;

public class OWLDataPropertyName implements OWLAtom {
	private String datapropertyname;
	private String ontologyname;
	
	public OWLDataPropertyName(String name, String ontologyname){
		datapropertyname = name;
		this.ontologyname = ontologyname;
	}
	
	public String getName(){
		return datapropertyname;
	}
	
	public String getOntologyname(){
		return ontologyname;
	}
	
	@Override
	public String toString(){
		return "<" + datapropertyname + ">";
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLDataPropertyName))return false;
	    // System.out.println("equal?" + rolename + " + " + ((OWLRoleName) other).getName() + " AND " + ontologyname + " + " + ((OWLRoleName) other).ontologyname);
	    return datapropertyname.equals(((OWLDataPropertyName) other).getName()) && ontologyname.equals(((OWLDataPropertyName) other).ontologyname);   
	}
	
	@Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + this.datapropertyname.hashCode();
        hash = hash * 31 + this.ontologyname.hashCode();
        // for (OWLFormula form : this.tail){
        // 	hash = hash * 31 + form.hashCode();
        // }
        return hash;
    }
	
	@Override
	public OWLDataPropertyName clone(){
		return new OWLDataPropertyName(datapropertyname, ontologyname);
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
		OWLDataFactory dataFactory = OWLAPIManagerManager.INSTANCE.getDataFactory();
		OWLDataPropertyExpression expr = dataFactory.getOWLDataProperty(IRI.create(datapropertyname));
		// String str = VerbalisationManager.INSTANCE.getDataPropertyNLString(expr);
		String str = expr.toString();
//		System.out.println("{Warning! Incomplete implementation} datapropertyname " + datapropertyname);
//		System.out.println("{Warning! Incomplete implementation} datapropertyname returned: " +  str);
		return str;
	}
	
}
