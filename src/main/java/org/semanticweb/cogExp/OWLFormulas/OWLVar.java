package org.semanticweb.cogExp.OWLFormulas;



/** This class represents variables (to be used for matching in OWLFormulas)
 * 
 * @author marvin
 *
 */
public class OWLVar implements OWLAtom {
	private String varname;
	
	public OWLVar(String name){
		varname = name;
	}
	
	public String getName(){
		return varname;
	}	
	
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLVar))return false;
	    return varname.equals(((OWLVar) other).getName());
	}
	
	@Override
	public String toString(){
		return "{" + varname + "}";
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
	
	@Override
	public String toNLString() {
		return this.toString();
	}
	
}
