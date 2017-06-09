package org.semanticweb.cogExp.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLFormulas.*;


public class Sequent<FormulaType> {
	
	protected TermTree antecedent = new TermTree();
	private TermTree succedent = new TermTree();
	private TermTree subexprs = new TermTree();
	protected int id= -1;
	private int highest_init_axiomid;
	private HashMap<Integer,Integer> formulaDepth = new HashMap<Integer,Integer>(); // heuristic to create short proofs by BFSing on formulas
	
	/* ====== ADMINISTRATION =====*/
	
	public int getID(){
		return id;
	}
	
	public int getHighestInitAxiomid(){
		return highest_init_axiomid;
	}
	
	public void setHighestInitAxiomid(int id){
		highest_init_axiomid = id;
	}
	
	public int antecedentFormulaGetID(OWLFormula formula){
		return antecedent.formulaGetID(formula);
	}
	
	public int succedentFormulaGetID(OWLFormula formula){
		return succedent.formulaGetID(formula);
	}
	
	public OWLFormula antecedentGetFormula(int i){
		return antecedent.getFormula(i);
	}
	
	public OWLFormula succedentGetFormula(int i){
		return succedent.getFormula(i);
	}
	
	public int antecedentGetHighestContainedTermID(){
		return antecedent.getHighestContainedTermID();
	}
	
	public int getFormulaDepth(int formulaID){
		if (formulaDepth.get(formulaID)==null)
			return 0;
		return formulaDepth.get(formulaID);
	}
	
	public void setFormulaDepth(int formulaID, int depth){
		formulaDepth.put(formulaID, depth);
	}
	
	public String getStatistics(){
		// System.out.println("calling statistics for seq id " + id);
		// String result = "dummy";
		// System.out.println("Antecedent stats: " + antecedent.getStatistics());
		String result = "Sequent id: " + id +  " Antecedent " + antecedent.getStatistics() + " Succedent: " + succedent.getStatistics() + " Subformulas " + subexprs.getStatistics();
		return result;
	}
	
	/* ===== CONSTRUCTORS ====== */
	
	public Sequent(ArrayList<FormulaType> ant, ArrayList<FormulaType> succ) throws Exception{
		// System.out.println("DBG CONSTR CALLED ");
		for (FormulaType f1 : ant){
			addAntecedent((OWLFormula) f1);
		}
		for (FormulaType f2 : succ){
			addAntecedent((OWLFormula) f2);
		}
		subexprs = new TermTree();
		this.id = InferenceApplicationService.INSTANCE.generateSequentID();
	}
	
	public Sequent(ArrayList<FormulaType> ant, ArrayList<FormulaType> succ, int id) throws Exception{
		// System.out.println("DBG CONSTR CALLED ");
		for (FormulaType f1 : ant){
			addAntecedent((OWLFormula) f1);
		}
		for (FormulaType f2 : succ){
			addAntecedent((OWLFormula) f2);
		}
		subexprs = new TermTree();
		this.id = id;
	}
	
	public Sequent(TermTree ant, TermTree succ, int id) throws Exception{
		this.antecedent = ant;
		this.succedent = succ;
		subexprs = new TermTree();
		this.insertIntoSubexprsTree(ant);
		this.insertIntoSubexprsTree(succ);
		this.id = id;
	}
	
	public Sequent(TermTree ant, TermTree succ, TermTree subexprs, int id, HashMap<Integer,Integer> formulaDepth){
		this.antecedent = ant;
		this.succedent = succ;
		this.subexprs = subexprs;
		this.id = id;
		this.formulaDepth = formulaDepth;
	}
	
	public Sequent(TermTree ant, TermTree succ) throws Exception{
		this.antecedent = ant;
		this.succedent = succ;
		subexprs = new TermTree();
		this.insertIntoSubexprsTree(ant);
		this.insertIntoSubexprsTree(succ);
		this.id = InferenceApplicationService.INSTANCE.generateSequentID();
	}
	
	public Sequent() {
		antecedent = new TermTree();
		succedent = new TermTree();
		subexprs = new TermTree();
		// id= -1;
		this.id = InferenceApplicationService.INSTANCE.generateSequentID();
		formulaDepth = new HashMap<Integer,Integer>(); // heuristic to create short proofs by BFSing on formulas
		// System.out.println("generated sequent with id "+  this.id);
		
		// TODO Auto-generated constructor stub
	}


	
	
	
	
	public int addAntecedent(OWLFormula formula) throws Exception{
		// System.out.println("sequent add called (1)");
		// System.out.println("DBG ADDANT DELEGATE CALLED (2) ");
		if (formula==null){
			System.out.println("we are about to throw an exception for " + formula);
			throw new Exception();
		}
		int id = -100;
		// System.out.println("Sequent DEBUG (1)" + formula);
		// System.out.println("Checking -- already contained ? " + antecedent.contains(formula));
		// System.out.println("Counting " + antecedent.getAllFormulas().size());
		if (!antecedent.contains(formula)) // <-- might be the case when using an incremental sequent
			antecedent.insert(formula);
		// System.out.println("Now -- already contained ? " + antecedent.contains(formula));
		// System.out.println("Counting " + antecedent.getAllFormulas().size());
		// System.out.println("Sequent DEBUG (2)" + formula);
		insertIntoSubexprsTree(formula);
		id = antecedentFormulaGetID(formula);
		return id;
	}
	
	public int addAntecedent(OWLFormula formula, int depth) throws Exception{
		// System.out.println("sequent add called (2)");
		int id = -100;
		// System.out.println("DBG SEQ ADD ANT " + formula);
		antecedent.insert(formula);
		insertIntoSubexprsTree(formula);
		id = antecedentFormulaGetID(formula);
		setFormulaDepth(id,depth);
		return id;
	}
	
	
	public int addSuccedent(OWLFormula formula) throws Exception{
		int id = -100;
		// System.out.println("DBG SEQ ADD SUCC " + formula);
		succedent.insert(formula);
		insertIntoSubexprsTree(formula);
		id = succedentFormulaGetID(formula);
		return id;
	}
	

	/* =====  Removing ===== */
	

	
	public void removeOWLFormulaFromAntecedent(OWLFormula formula){
		antecedent.remove(formula);
		// TODO: the below function is not implemented yet
		// removeFromSubexprsTree(formula);
	}
	
	public void removeOWLFormulaFromSuccedent(OWLFormula formula){
		succedent.remove(formula);
		// removeFromSubexprsTree(formula);
	}
	
	
	
	public HashSet<OWLFormula> getAllAntecedentOWLFormulas(){
		return antecedent.getAllFormulas();
	}
	
	/* GETTING SUCCEDENT */
	
	
	
	public HashSet<OWLFormula> getAllSuccedentOWLFormulas(){
		return succedent.getAllFormulas();
	}
	
/* ==== CONTAINMENT CHECKS ==== */
	
	public boolean isEmptyP(){
		return (antecedent == null && succedent == null);
	} 
	
	public boolean antecedentContainsOrDeeplyContains (OWLFormula formula){
		return antecedent.containsOrDeeplyContains(formula);
	}
	
	public boolean succedentContainsOrDeeplyContains (OWLFormula formula){
		return succedent.containsOrDeeplyContains(formula);
	}
	
	
	
	public boolean alreadyContainedInSuccedent(OWLFormula f){
	    return succedent.contains(f);
	}
	
	public boolean alreadyContainedInAntecedent(OWLFormula f){
		// System.out.println("already contained? " + f + " "+ antecedent.contains(f));
		// System.out.println("antecedent? " + antecedent);
		// if (antecedent.contains(f)){
		// 	System.out.println(antecedent);
		// 	System.out.println("id: " + antecedentFormulaGetID(f));
		// }
		return antecedent.contains(f);
	}
	
	
	
	public List<OWLFormula> findMatchingFormulasInAntecedent(OWLFormula formula){
		return antecedent.findMatchingFormulas(formula); 
	}
	
	public List<OWLFormula> findMatchingFormulasInSuccedent(OWLFormula formula){
		return succedent.findMatchingFormulas(formula); 
	}
	
	public List<OWLFormula> findMatchingFormulasInAntecedentDeeply(OWLFormula formula){
		return antecedent.matchDeeply(formula); 
	}
	
	public List<OWLFormula> findMatchingFormulasInSuccedentDeeply(OWLFormula formula){
		return succedent.matchDeeply(formula); 
	}
	
	/* ===== CLONING/COPYING ==== */
	
	@Override
	public Sequent<FormulaType> clone(){
		TermTree newant = antecedent.clone();
		TermTree newsucc = succedent.clone();
		TermTree subexprs = this.subexprs.clone();
		HashMap<Integer,Integer> depths = (HashMap<Integer,Integer>) this.formulaDepth.clone();
		int newid = InferenceApplicationService.INSTANCE.generateSequentID();
		// System.out.println("DEBUG -- sequent is cloned, original id " + id + " newid " + newid);
		return new Sequent<FormulaType>(newant,newsucc,subexprs,newid, depths);
	}
	
	
	
	public boolean equal(Sequent seq2){
		// System.out.println("Sequent equals called");
		Set antecedent2 = seq2.getAllAntecedentOWLFormulas();
		Set succedent2 = seq2.getAllSuccedentOWLFormulas();
		boolean equalP = true;
		for (Object ant1: getAllAntecedentOWLFormulas()){
			boolean foundP = false;
			for (Object ant2: antecedent2){
					if(ant1.equals(ant2)){
						foundP = true;
					}
			}
			if(!foundP){equalP=false;
					return false;
			}
			// System.out.println(foundP);
		}
		for (Object ant1: antecedent2){
			boolean foundP = false;
			for (Object ant2: getAllAntecedentOWLFormulas()){
					if(ant1.equals(ant2)){
						foundP = true;
					}
			}
			if(!foundP){equalP=false;
			return false;
			}
			// System.out.println(foundP);
		}
		for (Object suc1: getAllSuccedentOWLFormulas()){
			boolean foundP = false;
			for (Object suc2: succedent2){
					if(suc1.equals(suc2)){
						foundP = true;
					}
			}
			if(!foundP){equalP=false;
			return false;
			}
			// System.out.println(foundP);
		}
		for (Object suc1: succedent2){
			boolean foundP = false;
			for (Object suc2: getAllSuccedentOWLFormulas()){
					if(suc1.equals(suc2)){
						foundP = true;
					}
			}
			if(!foundP){equalP=false;
			return false;
			}
			// System.out.println(foundP);
		}
		return equalP;
	}
	
	

	
	
	public static OWLFormula getOWLSubformula(OWLFormula formula, List<Integer> pos){
		if (pos==null | pos.size()==0){
			return formula;
		} 
		else{
			int i = pos.remove(0);
			return getOWLSubformula(formula.getArgs().get(i),pos);
		}
	}
	
	public OWLFormula getOWLSubformula(SequentSinglePosition pos){
		if (pos.getSequentPart()==SequentPart.ANTECEDENT){
			if (this.antecedentGetFormula(pos.getToplevelPosition())!=null){
				OWLFormula formula = this.antecedentGetFormula(pos.getToplevelPosition());
				List<Integer> poslist = new ArrayList(pos.getPosition());
				poslist.remove(0); // we have already used the first index to find the formula as such
				return getOWLSubformula(formula, poslist);
			}
		} else // succedent
		{
			if (this.succedentGetFormula(pos.getToplevelPosition())!=null){
				OWLFormula formula = this.succedentGetFormula(pos.getToplevelPosition());
				List<Integer> poslist = new ArrayList(pos.getPosition());
				poslist.remove(0); // we have already used the first index to find the formula as such
				return getOWLSubformula(formula, poslist);
			}
		}
		return null;
	}
	
	
	public List<OWLFormula> retrieveOWLFormulas(SequentSinglePosition pos){	
		// System.out.println("function called for single position, pos " + pos.getToplevelPosition());
		List formulas = new ArrayList();
		if (pos.getSequentPart()==SequentPart.ANTECEDENT){
			if (this.antecedentGetFormula(pos.getToplevelPosition())!=null){
				formulas.add(this.antecedentGetFormula(pos.getToplevelPosition()));
			} else{
				formulas = new ArrayList<OWLFormula>(getAllAntecedentOWLFormulas());
				List<OWLFormula>  list = new ArrayList<OWLFormula>();
				OWLFormula formula = (OWLFormula) formulas.get(pos.getToplevelPosition());
				list.add(formula);
				return list;
			}
		}
		else formulas.add(this.succedentGetFormula(pos.getToplevelPosition()));
		// System.out.println("formulas : " + formulas);
		return formulas;
	}
	
	
	public List<OWLFormula> retrieveOWLFormulas(SequentPositionInNode pos){
		// System.out.println("retrieve Formulas with SequentPositionInNode called.");
		SequentPosition seqpos = pos.getSequentPosition();
		return retrieveOWLFormulas(seqpos);
	}
	

	
	public List<OWLFormula> retrieveOWLFormulas(SequentPosition pos){
		// System.out.println("retrieve Formulas with SequentPosition called.");
		if (pos instanceof SequentSinglePosition) return retrieveOWLFormulas((SequentSinglePosition) pos);
		if (pos instanceof SequentMultiPosition) return retrieveOWLFormulas((SequentMultiPosition) pos);
		return null;
	}
	
	
	
	public List<OWLFormula> retrieveOWLFormulas(SequentMultiPosition pos){
		List formulas = new ArrayList();
		List<OWLFormula>  list = new ArrayList<OWLFormula>();
		if (pos.getSequentPart()==SequentPart.ANTECEDENT){
				formulas = new ArrayList<OWLFormula>(getAllAntecedentOWLFormulas());
		}
		if (pos.getSequentPart()==SequentPart.SUCCEDENT){
			formulas = new ArrayList<OWLFormula>(getAllSuccedentOWLFormulas());
		}	
		Integer[][] positions = pos.getPositions();
		for (int i = 0; i < positions.length; i++){
			OWLFormula formula = (OWLFormula) formulas.get(positions[i][0]);
			list.add(formula);
		}
		return list;
	}
	
	public List<OWLFormula> retrieveOWLFormulas(RuleBinding bin){
		List formulas = new ArrayList();
		List<OWLFormula>  list = new ArrayList<OWLFormula>();
		
		HashMap bindings = bin.getBindings();
		Collection all_positions = bindings.values();
		// System.out.println(all_positions);
		for (Object b : all_positions){
			if (b instanceof SequentSinglePosition){
				list.addAll(retrieveOWLFormulas((SequentSinglePosition) b));
			}
			if (b instanceof SequentMultiPosition){
			    list.addAll(retrieveOWLFormulas((SequentMultiPosition) b));
			}	
			if (b instanceof SequentPositionInNode){
			    list.addAll(retrieveOWLFormulas(((SequentPositionInNode) b).getSequentPosition()));
			}	
		}
		// System.out.println("retrieveOWLFormulas - ruleBinding returns" + list);
		return list;
	}
	
	
	public Comparator<OWLFormula> formulaDescComparatorAnt() {
	    return new Comparator<OWLFormula>() {
	      // @Override
	      @Override
		public int compare(OWLFormula first, OWLFormula second) {
	        return Integer.valueOf(antecedentFormulaGetID(second)).compareTo(Integer.valueOf(antecedentFormulaGetID(first)));
	      }
	    };
	  }
	
	public Comparator<OWLFormula> formulaAscComparatorAnt() {
	    return new Comparator<OWLFormula>() {
	      // @Override
	      @Override
		public int compare(OWLFormula first, OWLFormula second) {
	        return Integer.valueOf(antecedentFormulaGetID(first)).compareTo(Integer.valueOf(antecedentFormulaGetID(second)));
	      }
	    };
	  }
	
	public Comparator<OWLFormula> formulaDescComparatorSucc() {
	    return new Comparator<OWLFormula>() {
	      // @Override
	      @Override
		public int compare(OWLFormula first, OWLFormula second) {
	        return Integer.valueOf(succedentFormulaGetID(second)).compareTo(Integer.valueOf(succedentFormulaGetID(first)));
	      }
	    };
	  }
	
	public String antecedentToString(){
		return antecedent.toString();
	}
	
	/* ==== SUBEXPRESSIONS ==== */
	
	public void insertIntoSubexprsTree(OWLFormula formula) throws Exception{
			if(!subexprs.contains(formula)){
			subexprs.insert(formula);
			// System.out.println("INSERTING into SUBEXPRSTREE " + formula);
			}
			if (!(formula.getArgs()==null || formula.getArgs().size()==0)){
					for (OWLFormula child : formula.getArgs()){
						insertIntoSubexprsTree(child);
					}
			}
	}
	
	public void insertIntoSubexprsTree(TermTree tree) throws Exception{
		Set<OWLFormula> allforms = tree.getAllFormulas();
		for (OWLFormula formula : allforms){
			insertIntoSubexprsTree(formula);
			}
	}	
	
	public boolean subexprInSequent(OWLFormula formula){
		// System.out.println(" Subexprs tree " + subexprs);
		return subexprs.contains(formula);
	}
	
	public Set<OWLFormula> getAllSubExprs(){
		return subexprs.getAllFormulas();
	}
	
	/// not needed at the moment!
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet(rest)) {
	    	Set<T> newSet = new HashSet<T>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }		
	    return sets;
	}
	
	public Sequent amputateDepth(int depth){
		// System.out.println("sequent amputation taking place");
		Sequent clone = this.clone(); // produce ordinary clone
		// amputate antecedent
		Set<OWLFormula> allAntecedentForms = antecedent.getAllFormulas();
				// clone.antecedent.getAllFormulas(); <-- changed this
		for (OWLFormula ant : allAntecedentForms){
			int antdepth = getFormulaDepth(antecedent.formulaGetID(ant));
			if (antdepth>depth){
				clone.antecedent.remove(ant);
				// System.out.println("DEBUG removing {" + antdepth + "} " + ant);
				// System.out.println("DEBUG all formulas " + clone.antecedent.getAllFormulas());
				}
		}
		// amputate succedent -- not necessary!
		/*
		Set<OWLFormula> allSuccedentForms = clone.succedent.getAllFormulas();
		for (OWLFormula succ : allSuccedentForms){
			int succdepth = getFormulaDepth(succedent.formulaGetID(succ));
			if (succdepth>depth)
				clone.antecedent.remove(succ);
		}
		*/
		// System.out.println("returning clone with id " + clone.getID() + " based on " + this.getID());
		return clone;
	}
	
	public String antecedentPrintIDMapping(){
		return antecedent.printIDMapping();
	}
	
	public void reportSubexpressionFormulas(){
		System.out.println("SUBEXPRESSIONS REPORT for sequent " + id);
		Set<OWLFormula> exprs = subexprs.getAllFormulas();
		List<OWLFormula> sortedexprs = new ArrayList<>(exprs);
		Comparator<OWLFormula> c = new OWLFormulaComparator();
		sortedexprs.sort(c);
		for (OWLFormula form : sortedexprs){
			System.out.println(form.prettyPrint());
		}
		System.out.println("\n");
	}
	
	public void reportAntecedent(){
		System.out.println("ANTECEDENT REPORT for sequent " + id);
		Set<OWLFormula> exprs = antecedent.getAllFormulas();
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
	
}
