package org.semanticweb.cogExp.OWLFormulas;

import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPIManagerManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
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
		System.out.println("{Note: this method has not been fully implemented!} individualname " + individualname);
		System.out.println("{Note: this method has not been fully implemented!} individualname returned: " +  str);
		return str;
	}

	
}
