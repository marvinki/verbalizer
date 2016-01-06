package org.semanticweb.cogExp.OWLFormulas;



/** Class representing variables to stand for role names (to enable matching in OWLFormula-s)
 * 
 * @author marvin
 *
 */
public class OWLRoleVar implements OWLAtom {
	private String rolename;
	
	public OWLRoleVar(String name){
		rolename = name;
	}
	
	public String getName(){
		return rolename;
	}
	
	
	@Override
	public String toString(){
		return "<{" + rolename + "}>";
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLRoleVar))return false;
	    // System.out.println("equal?" + rolename + " + " + ((OWLRoleName) other).getName() + " AND " + ontologyname + " + " + ((OWLRoleName) other).ontologyname);
	    return rolename.equals(((OWLRoleVar) other).getName());   
	}
	
	@Override
	public OWLRoleVar clone(){
		return new OWLRoleVar(rolename);
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
	
}
