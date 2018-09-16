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
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

/** Class for role names (object properties) in OWLFormula-s
 * 
 * @author marvin
 *
 */
public class OWLRoleName implements OWLAtom {
	private String rolename;
	private String ontologyname;
	// private boolean dataproperty = false;
	
	/** Constructor for OWLRoleName objects
	 * 	
	 * @param name				the short name
	 * @param ontologyname		the full uri (including short name)
	 */
	public OWLRoleName(String name, String ontologyname){
		rolename = name;
		this.ontologyname = ontologyname;
	}
	
	
	
	public String getName(){
		return rolename;
	}
	
	public String getOntologyname(){
		return ontologyname;
	}
	
	/*
	public boolean isDataproperty(){
		return dataproperty;
	}
	*/
	
	
	@Override
	public String toString(){
		return "<" + rolename + ">";
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLRoleName))return false;
	    // System.out.println("equal?" + rolename + " + " + ((OWLRoleName) other).getName() + " AND " + ontologyname + " + " + ((OWLRoleName) other).ontologyname);
	    return rolename.equals(((OWLRoleName) other).getName()) && ontologyname.equals(((OWLRoleName) other).ontologyname);   
	}
	
	@Override
	public OWLRoleName clone(){
		return new OWLRoleName(rolename, ontologyname);
	}
	
	@Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + rolename.hashCode();
        hash = hash * 31 + ontologyname.hashCode();
        return hash;
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
		OWLObjectPropertyExpression expr = dataFactory.getOWLObjectProperty(IRI.create(ontologyname));
		String str = VerbalisationManager.INSTANCE.getPropertyNLString(expr);
		// System.out.println("rolename " + ontologyname);
		// System.out.println("rolename returned: " +  str);
		return str;
	}
	
}
