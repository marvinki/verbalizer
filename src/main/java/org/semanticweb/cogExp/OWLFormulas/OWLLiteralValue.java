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
	
}
