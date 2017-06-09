package org.semanticweb.cogExp.OWLFormulas;



public class OWLClassName implements OWLAtom{
	private String classname;
	private String ontologyname;
	
	public OWLClassName(String name, String ontologyname){
		classname = name;
		this.ontologyname = ontologyname;
	}
	
	public String getName(){
		return classname;
	}
	
	public String getOntologyname(){
		return ontologyname;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof OWLClassName))return false;
	    OWLClassName otherclassname = ((OWLClassName) other);
	    if (classname==null && otherclassname.getName()!=null) return false;
	    boolean classnameSame = classname.equals(otherclassname.getName());
	    if (!classnameSame){return false;}
	    boolean ontologynameSame = ontologyname.equals(otherclassname.ontologyname);
	    // System.out.println("class name equal? " + classname + " / " + ((OWLClassName) other).getName() + " AND " + ontologyname + " / " + ((OWLClassName) other).ontologyname);
	    // System.out.println(classnameSame && ontologynameSame);
	    return classnameSame && ontologynameSame;   
	}
	
	 @Override
	    public int hashCode() {
	        int hash = 1;
	        hash = hash * 17 + classname.hashCode();
	        hash = hash * 31 + ontologyname.hashCode();
	        return hash;
	    }
	

	public String toSimpleString(){
		return "[" + classname + "]";
	}
	
	@Override
	public String toString(){
		return "[" + ontologyname + ":" + classname + "]";
	}
	
	@Override
	public OWLClassName clone(){
		return new OWLClassName(classname, ontologyname);
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
		return true;
	}

	@Override
	public boolean isIndividual() {
		return false;
	}

	
}
