package org.semanticweb.cogExp.OWLFormulas;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPIManagerManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

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

	@Override
	public String toNLString() {
		OWLDataFactory dataFactory = OWLAPIManagerManager.INSTANCE.getDataFactory();
		// OWLClass cl = dataFactory.getOWLClass(IRI.create(ontologyname + classname));
		// String str = VerbalisationManager.INSTANCE.getClassNLString(cl);
		OWLObject obj = ConversionManager.toOWLAPI(this);
		String str = VerbalisationManager.INSTANCE.getClassNLString((OWLClass) obj);
		// System.out.println("classname " +  ontologyname );
		// System.out.println("classname returned: " +  str);
		return str;
	}
	
}
