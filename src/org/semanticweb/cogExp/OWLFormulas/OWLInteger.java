package org.semanticweb.cogExp.OWLFormulas;




/** Class represents integer values to be part of cardinality restrictions
 * 
 * @author marvin
 *
 */
public class OWLInteger implements OWLAtom{
	private int value;
	
	public OWLInteger(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLInteger))return false;
	    boolean valsSame = value==(((OWLInteger) other).getValue());
	    if (!valsSame){return false;}
	    // System.out.println("class name equal? " + classname + " / " + ((OWLClassName) other).getName() + " AND " + ontologyname + " / " + ((OWLClassName) other).ontologyname);
	    return valsSame;   
	}
	
	@Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + this.value;
        // for (OWLFormula form : this.tail){
        // 	hash = hash * 31 + form.hashCode();
        // }
        return hash;
    }
	
	@Override
	public String toString(){
		return "|" + value + "|";
	}
	
	@Override
	public OWLInteger clone(){
		return new OWLInteger(value);
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
