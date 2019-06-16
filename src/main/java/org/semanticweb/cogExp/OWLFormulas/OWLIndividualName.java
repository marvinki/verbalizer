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
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class OWLIndividualName implements OWLAtom{
	private String individualname;
	private String ontologyname;
	
	public OWLIndividualName(String name, String ontologyname){
		individualname = name;
		this.ontologyname = ontologyname;
	}
	
	public String getName(){
		return individualname;
	}
	
	public String getOntologyname(){
		return ontologyname;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLIndividualName))return false;
	    boolean nameSame = individualname.equals(((OWLIndividualName) other).getName());
	    if (!nameSame){return false;}
	    boolean ontologynameSame = ontologyname.equals(((OWLIndividualName) other).ontologyname);
	    // System.out.println("class name equal? " + nameSame + " / " + ((OWLIndividualName) other).getName() + " AND " + ontologyname + " / " + ((OWLIndividualName) other).ontologyname);
	    // System.out.println(nameSame && ontologynameSame);
	    return nameSame && ontologynameSame;   
	}
	
	public int hashCode() {
        int hash = 1;
        hash = hash * 17 + individualname.hashCode();
        hash = hash * 31 + ontologyname.hashCode();
        return hash;
    }
	
	@Override
	public String toString(){
		return "[[" + individualname + "]]";
	}
	
	@Override
	public OWLClassName clone(){
		return new OWLClassName(individualname, ontologyname);
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
		return true;
	}

	
	@Override
	public String toNLString() {
		OWLDataFactory dataFactory = OWLAPIManagerManager.INSTANCE.getDataFactory();
		OWLNamedIndividual cl = dataFactory.getOWLNamedIndividual(IRI.create(individualname));
		// String str = VerbalisationManager.INSTANCE.getClassNLString(cl);
		String str = cl.toString(); // <--- insert code for rendering individuals here!
//		System.out.println("{Note: this method has not been fully implemented!} individualname " + individualname);
//		System.out.println("{Note: this method has not been fully implemented!} individualname returned: " +  str);
		return str;
	}

	
}
