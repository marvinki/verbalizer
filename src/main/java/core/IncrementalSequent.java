package org.semanticweb.cogExp.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.OWLFormulas.OWLFormulaComparator;
import org.semanticweb.cogExp.OWLFormulas.TermTree;

public class IncrementalSequent extends Sequent{
	
	private Sequent mastersequent;
	private Set<Integer> antecedents = new HashSet<Integer>(); // singles out those elements (formulaID) of the master sequent that represent the antecedent for this instance of the sequent
	private Set<Integer> succedents = new HashSet<Integer>();  // singles out those elements (formulaID) of the master sequent that represent the succedent for this instance of the sequent
	
	
	public IncrementalSequent(Sequent mastersequent){
		this.mastersequent = mastersequent;
	}
	
	public IncrementalSequent(Sequent mastersequent, Set<Integer> antecedents, Set<Integer> succedents){
		this.antecedents = antecedents;
		this.succedents = succedents;
		this.mastersequent = mastersequent;
	}
	
	public IncrementalSequent(Sequent mastersequent, Set<Integer> antecedents, Set<Integer> succedents, int id){
		this.antecedents = antecedents;
		this.succedents = succedents;
		this.mastersequent = mastersequent;
		this.id = id;
	}
	
	public IncrementalSequent(){
		mastersequent = new Sequent();
	}
	
	public int antecedentGetHighestContainedTermID(){
		return mastersequent.antecedentGetHighestContainedTermID();
	}

	public int addAntecedent(OWLFormula formula) throws Exception{
		int formid = mastersequent.addAntecedent(formula);
		antecedents.add(formid);
		return id;
	}
	
	public int addAntecedent(OWLFormula formula, int depth) throws Exception{
		int formid = mastersequent.addAntecedent(formula, depth);
		antecedents.add(formid);
		return formid;
	}
	
	public int addSuccedent(OWLFormula formula) throws Exception{
		int id = mastersequent.addSuccedent(formula);
		succedents.add(id);
		return id;
	}
	
	
	// Removing: the formula is only taken off the list, since other instances may still refer to it
	public void removeOWLFormulaFromAntecedent(OWLFormula formula){
		antecedents.remove((Integer) mastersequent.antecedentFormulaGetID(formula));
	}
	
	public void removeOWLFormulaFromSuccedent(OWLFormula formula){
		succedents.remove((Integer) mastersequent.succedentFormulaGetID(formula));
	}
	
	// Retrieval
	public HashSet<OWLFormula> getAllAntecedentOWLFormulas(){
		Set<OWLFormula> formulas = mastersequent.getAllAntecedentOWLFormulas(); // this could be made more efficient!
		HashSet<OWLFormula> results = new HashSet<OWLFormula>();
		for (OWLFormula form : formulas){
			if (antecedents.contains(mastersequent.antecedentFormulaGetID(form))){
				results.add(form);
			}
		}
		return results;
	}
	
	public HashSet<OWLFormula> getAllSuccedentOWLFormulas(){
		Set<OWLFormula> formulas = mastersequent.getAllSuccedentOWLFormulas(); // this could be made more efficient!
		HashSet<OWLFormula> results = new HashSet<OWLFormula>();
		for (OWLFormula form : formulas){
			if (succedents.contains(mastersequent.succedentFormulaGetID(form))){
				results.add(form);
			}
		}
		return results;
	}
	
	public int antecedentFormulaGetID(OWLFormula formula){
		return mastersequent.antecedentFormulaGetID(formula);
	}
	
	public int succedentFormulaGetID(OWLFormula formula){
		return mastersequent.succedentFormulaGetID(formula);
	}
	
	public OWLFormula antecedentGetFormula(int i){
		return mastersequent.antecedentGetFormula(i);
	}
	
	public OWLFormula succedentGetFormula(int i){
		return mastersequent.succedentGetFormula(i);
	}
	
	// this still needs to be implemented properly!
	public boolean antecedentContainsOrDeeplyContains (OWLFormula formula){
		return mastersequent.antecedentContainsOrDeeplyContains (formula);
	}
		
	// this still needs to be implemented properly!
	public boolean succedentContainsOrDeeplyContains (OWLFormula formula){
			return mastersequent.succedentContainsOrDeeplyContains (formula);
	}
	
	public boolean alreadyContainedInAntecedent(OWLFormula f){
		boolean containedMaster = mastersequent.alreadyContainedInAntecedent(f);
		boolean contained = false;
		int id = -1;
		if (containedMaster){
			if (f==null)
			{System.out.println("f null");}
			if (mastersequent==null)
			{System.out.println("masterseq null");}
			id = mastersequent.antecedentFormulaGetID(f);
			contained = antecedents.contains(id);
		}
	    return contained;
	}
	
	public boolean alreadyContainedInSuccedent(OWLFormula f){
		boolean containedMaster = mastersequent.alreadyContainedInSuccedent(f);
		boolean contained = false;
		int id = -1;
		if (containedMaster){
			id = mastersequent.succedentFormulaGetID(f);
			contained = succedents.contains(id);
		}
	    return contained;
 		
	}
	
	public List<OWLFormula> findMatchingFormulasInAntecedent(OWLFormula formula){
		List<OWLFormula> found = mastersequent.findMatchingFormulasInAntecedent(formula);
		List<OWLFormula> result = new ArrayList<OWLFormula>();
		for (OWLFormula form : found){
			if (antecedents.contains(mastersequent.antecedentFormulaGetID(form))){
				result.add(form);
			}
		}
		return result; 
	}
	
	public List<OWLFormula> findMatchingFormulasInSuccedent(OWLFormula formula){
		List<OWLFormula> found = mastersequent.findMatchingFormulasInSuccedent(formula);
		List<OWLFormula> result = new ArrayList<OWLFormula>();
		for (OWLFormula form : found){
			if (succedents.contains(mastersequent.succedentFormulaGetID(form))){
				result.add(form);
			}
		}
		return result; 
	}
	
	
	public List<OWLFormula> findMatchingFormulasInAntecedentDeeply(OWLFormula formula){
		// TODO: this could be made more efficient, maybe? By exluding nodes that "should not be there"...
		List<OWLFormula> results = mastersequent.findMatchingFormulasInAntecedentDeeply(formula);
		return results; 
	}
	
	public List<OWLFormula> findMatchingFormulasInSuccedentDeeply(OWLFormula formula){
		// TODO: this could be made more efficient, maybe? By exluding nodes that "should not be there"...
		List<OWLFormula> results = mastersequent.findMatchingFormulasInSuccedentDeeply(formula);
		return results; 
	}
	
	
/* ===== CLONING/COPYING ==== */
	
	@Override
	public IncrementalSequent clone(){
		Set<Integer> newant = new HashSet<Integer>();
		newant.addAll(antecedents);
		Set<Integer> newsucc = new HashSet<Integer>();
		newsucc.addAll(succedents);
		IncrementalSequent clone = new IncrementalSequent(mastersequent, newant,newsucc); //,newid);
		return clone;
	}
	
	 /* === SUBEXPRESSIONS === */
	
	public void insertIntoSubexprsTree(OWLFormula formula) throws Exception{
		mastersequent.insertIntoSubexprsTree(formula);
	}
	
	public boolean subexprInSequent(OWLFormula formula){
		return mastersequent.subexprInSequent(formula);
	}
	
	public Set<OWLFormula> getAllSubExprs(){
		return mastersequent.getAllSubExprs();
	}
	
	public int getFormulaDepth(int formulaID){
		return mastersequent.getFormulaDepth(formulaID);
	}
	
	public void setFormulaDepth(int formulaID, int depth){
		mastersequent.setFormulaDepth(formulaID, depth);
	}
	
	public Sequent getMasterSequent(){
		return mastersequent;
	}
	
	public void reportSubexpressionFormulas(){
		System.out.println("Asking for subexpressions report from id " + id);
		mastersequent.reportSubexpressionFormulas();
	}
	
	public void reportAntecedent(){
		System.out.println("ANTECEDENT REPORT for incremental sequent " + id + " with mastersequent " + mastersequent.id);
		Set<OWLFormula> exprs = mastersequent.getAllAntecedentOWLFormulas();
		List<OWLFormula> sortedexprs = new ArrayList<>(exprs);
		Comparator<OWLFormula> c = new OWLFormulaComparator();
		sortedexprs.sort(c);
		int i = 0;
		for (OWLFormula form : sortedexprs){
			System.out.println(i + ": " + form.prettyPrint() + " {" + getFormulaDepth(antecedentFormulaGetID(form)) + "}");
			i++;
		}
		System.out.println("\n");
	}
	
	/* ==== AMPUTATION === */
	
	public Sequent amputateDepth(int depth){
		Sequent newmastersequent = mastersequent.amputateDepth(depth);
		// now wrap this into an incremental sequent object
		Set<Integer> newant = new HashSet<Integer>(); // maybe this could be made more efficient by 
												      // simply reusing/sharing them?
		newant.addAll(antecedents);
		Set<Integer> newsucc = new HashSet<Integer>();
		newsucc.addAll(succedents);
		return new IncrementalSequent(newmastersequent,newant,newsucc); 
		
	}
	
	public TermTree getAntecedentTree(){
		return mastersequent.antecedent;
	}
	
}
