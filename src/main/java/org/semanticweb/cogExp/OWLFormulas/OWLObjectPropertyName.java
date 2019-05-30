package org.semanticweb.cogExp.OWLFormulas;

import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPIManagerManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

public class OWLObjectPropertyName implements OWLAtom {
	private String objectpropertyname;
	private String ontologyname;
	
	public OWLObjectPropertyName(String name, String ontologyname){
		objectpropertyname = name;
		this.ontologyname = ontologyname;
	}
	
	public String getName(){
		return objectpropertyname;
	}
	
	public String getOntologyname(){
		return ontologyname;
	}
	
	@Override
	public String toString(){
		return "<" + objectpropertyname + ">";
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLObjectPropertyName))return false;
	    // System.out.println("equal?" + rolename + " + " + ((OWLRoleName) other).getName() + " AND " + ontologyname + " + " + ((OWLRoleName) other).ontologyname);
	    return objectpropertyname.equals(((OWLObjectPropertyName) other).getName()) && ontologyname.equals(((OWLObjectPropertyName) other).ontologyname);   
	}
	
	@Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + this.objectpropertyname.hashCode();
        hash = hash * 31 + this.ontologyname.hashCode();
        // for (OWLFormula form : this.tail){
        // 	hash = hash * 31 + form.hashCode();
        // }
        return hash;
    }
	
	@Override
	public OWLObjectPropertyName clone(){
		return new OWLObjectPropertyName(objectpropertyname, ontologyname);
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
		OWLObjectPropertyExpression expr = dataFactory.getOWLObjectProperty(IRI.create(objectpropertyname));
		String str = VerbalisationManager.INSTANCE.getPropertyNLString(expr);
		// System.out.println("rolename " + objectpropertyname);
		// System.out.println("rolename returned: " +  str);
		return str;
	}
	
}
