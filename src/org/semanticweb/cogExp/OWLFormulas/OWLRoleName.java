package org.semanticweb.cogExp.OWLFormulas;

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
