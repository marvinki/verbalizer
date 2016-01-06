package org.semanticweb.cogExp.OWLFormulas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TermNodeLeaf extends TermNode{
	// ArrayList<OWLFormula> formulas = new ArrayList<OWLFormula>();
	HashMap<Integer, OWLFormula> formulas = new HashMap<Integer, OWLFormula>();
	
	public TermNodeLeaf(OWLAtom atom, HashMap<Integer, OWLFormula> formulas){
		super(atom);
		this.formulas = formulas;
	}
	
	public void addFormula(int i, OWLFormula formula){
		formulas.put(i, formula);
		// formulas.add(i,formula);
	}
	
	public ArrayList<OWLFormula> getFormulas(){
		return new ArrayList<OWLFormula>(formulas.values());
	}
	
	public Set<Integer> getFormulaIDs(){
		return formulas.keySet();
	}
	
	@Override
	public String toString() {
		return formulas.toString(); 
	}
	
	
	public static String print(TermNodeLeaf tnl, int indent) {
		return tnl.formulas.toString(); 
	}
	
	@Override
	public TermNodeLeaf clone(){
		HashMap<Integer,OWLFormula> newformulas = new HashMap<Integer,OWLFormula>();
		TermNodeLeaf ntn = new TermNodeLeaf(this.getHead(),newformulas);
		for (int i : formulas.keySet()){
			OWLFormula newf = formulas.get(i).clone();
			newformulas.put(i,newf);
		}
		return ntn;
	}
	
}
