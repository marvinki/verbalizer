package org.semanticweb.cogExp.OWLFormulas;


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
	    if (!(other instanceof OWLClassName))return false;
	    boolean nameSame = individualname.equals(((OWLClassName) other).getName());
	    if (!nameSame){return false;}
	    boolean ontologynameSame = ontologyname.equals(((OWLIndividualName) other).ontologyname);
	    // System.out.println("class name equal? " + classname + " / " + ((OWLClassName) other).getName() + " AND " + ontologyname + " / " + ((OWLClassName) other).ontologyname);
	    // System.out.println(classnameSame && ontologynameSame);
	    return nameSame && ontologynameSame;   
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

	
	

	
}
