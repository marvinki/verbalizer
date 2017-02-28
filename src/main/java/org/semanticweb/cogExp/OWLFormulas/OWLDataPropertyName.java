package org.semanticweb.cogExp.OWLFormulas;


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
	
}
