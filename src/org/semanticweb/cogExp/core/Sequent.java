package org.semanticweb.cogExp.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import org.semanticweb.cogExp.OWLFormulas.*;


public class Sequent<FormulaType> {
	
	private TermTree antecedent = new TermTree();
	private TermTree succedent = new TermTree();
	private TermTree subexprs = new TermTree();
	private int id= -1;
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
		// TODO Auto-generated constructor stub
	}


	
	
	
	
	public int addAntecedent(OWLFormula formula) throws Exception{
		// System.out.println("DBG ADDANT DELEGATE CALLED (2) ");
		if (formula==null)
			throw new Exception();
		int id = -100;
		// System.out.println("Sequent DEBUG (1)" + formula);
		antecedent.insert(formula);
		// System.out.println("Sequent DEBUG (2)" + formula);
		insertIntoSubexprsTree(formula);
		id = antecedentFormulaGetID(formula);
		return id;
	}
	
	public int addAntecedent(OWLFormula formula, int depth) throws Exception{
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
	
	/* GETTING ANTECEDENT */
	
	/*
	public ArrayList<FormulaType> getAntecedent(){
		ArrayList<FormulaType> results =  new ArrayList<FormulaType>();
		HashSet<OWLFormula> formulas =  antecedent.getAllFormulas();
		for (OWLFormula form : formulas){
			// System.out.println("DBG SEQ GET from " + form);
			OWLObject owlform = form.toOWLAPI();
			// System.out.println("DBG SEQ GET to " + owlform);
			FormulaType typeform = (FormulaType) owlform;
			results.add(typeform);
		}
		return results;
	}
	*/
	
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
		return new Sequent<FormulaType>(newant,newsucc,subexprs,InferenceApplicationService.INSTANCE.generateSequentID(), depths);
	}
	
	
	
	/* 
	public static java.lang.String NLString(Object ob){
		java.lang.String resultstring ="";
		Pattern somepattern = Pattern.compile("\\#(.*?)\\>"); 
		if (ob instanceof OWLSubClassOfAxiom){
			resultstring = resultstring + NLString(((OWLSubClassOfAxiom) ob).getSubClass()) +  " is a " + NLString(((OWLSubClassOfAxiom) ob).getSuperClass());
		} else {
			if (ob instanceof OWLClass || ob instanceof OWLProperty || ob instanceof OWLPropertyRange || ob instanceof OWLDataProperty || ob instanceof OWLIndividual){
				String classstring = ob.toString();
				Matcher m = somepattern.matcher(classstring);
				String s = "";
				while (m.find()){
					 s = m.group(1);
				}
				resultstring = resultstring + s;
			}
			else {
			resultstring = resultstring + ob.toString();
		}	
		}
		return resultstring;
	}
	
	*/
	
	/*
	public static java.lang.String shorthandString(Object ob){
		 Pattern somepattern = Pattern.compile("\\#(.*?)\\>"); 
		java.lang.String resultstring ="";
		// System.out.println(ob.getClass());
		// "≣"
		
		if (ob instanceof OWLDataHasValue){
			OWLDataHasValue axiom = (OWLDataHasValue) ob;
			resultstring = resultstring + "hasValue" + "(" + shorthandString(axiom.getProperty());
			resultstring = resultstring + "," + axiom.getValue() + ")";
			return resultstring;
		} 
		if (ob instanceof OWLObjectHasValue){
			OWLObjectHasValue axiom = (OWLObjectHasValue) ob;
			resultstring = resultstring + "hasValue" + "(" + shorthandString(axiom.getProperty());
			resultstring = resultstring + "," + shorthandString(axiom.getValue()) + ")";
			return resultstring;
		} 
	
		if (ob instanceof OWLDifferentIndividualsAxiom){
			OWLDifferentIndividualsAxiom axiom = (OWLDifferentIndividualsAxiom) ob;
			List<OWLIndividual> exprs =  axiom.getIndividualsAsList();
			resultstring = resultstring + "different(";
			boolean firstp = true;
			for (OWLIndividual exp : exprs){
				if (!firstp) {resultstring = resultstring + ",";}
				firstp = false;
				resultstring = resultstring + shorthandString(exp);
			}
			resultstring = resultstring + ")";
		} 
		else
		if (ob instanceof OWLDatatype){
			OWLDatatype axiom = (OWLDatatype) ob;
			if (axiom.isInteger()) {resultstring = resultstring +  "Integer";}
			else {resultstring = resultstring;}
		} 
		else
		if (ob instanceof OWLFunctionalDataPropertyAxiom){
			OWLFunctionalDataPropertyAxiom axiom = (OWLFunctionalDataPropertyAxiom) ob;
			resultstring = resultstring + "functional" + "(" + shorthandString(axiom.getProperty());
			resultstring = resultstring + ")";
		} 
		else
			if (ob instanceof OWLFunctionalObjectPropertyAxiom){
				OWLFunctionalObjectPropertyAxiom axiom = (OWLFunctionalObjectPropertyAxiom) ob;
				resultstring = resultstring + "functional" + "(" + shorthandString(axiom.getProperty());
				resultstring = resultstring + ")";
			} 
		else
			if (ob instanceof OWLDataMinCardinality){
				OWLDataMinCardinality axiom = (OWLDataMinCardinality) ob;
				resultstring = resultstring + "min#" + "(" + shorthandString(axiom.getCardinality()) + ","+ shorthandString(axiom.getProperty());
				resultstring = resultstring + "," +  shorthandString(axiom.getFiller()) +")";
			} 
			else
		if (ob instanceof OWLObjectMinCardinality){
			OWLObjectMinCardinality axiom = (OWLObjectMinCardinality) ob;
			resultstring = resultstring + ">=" + axiom.getCardinality() + "(" + shorthandString(axiom.getProperty()) + "." + shorthandString(axiom.getFiller());
			resultstring = resultstring + ")";
		} 
		else
			if (ob instanceof OWLObjectMaxCardinality){
				OWLObjectMaxCardinality axiom = (OWLObjectMaxCardinality) ob;
				resultstring = resultstring + "<=" + axiom.getCardinality() + "(" + shorthandString(axiom.getProperty()) + "." + shorthandString(axiom.getFiller());
				resultstring = resultstring + ")";
			} 
			else
			if (ob instanceof OWLObjectExactCardinality){
				OWLObjectExactCardinality axiom = (OWLObjectExactCardinality) ob;
				resultstring = resultstring + "=" + axiom.getCardinality() + "(" + shorthandString(axiom.getProperty()) + "." + shorthandString(axiom.getFiller());
				resultstring = resultstring + ")";
			} 
			else
			if (ob instanceof OWLSymmetricObjectPropertyAxiom){
				OWLSymmetricObjectPropertyAxiom axiom = (OWLSymmetricObjectPropertyAxiom) ob;
				resultstring = resultstring + "symm" + "(" + shorthandString(axiom.getProperty());
				resultstring = resultstring + ")";
			} 
			else
				if (ob instanceof OWLTransitiveObjectPropertyAxiom){
					OWLTransitiveObjectPropertyAxiom axiom = (OWLTransitiveObjectPropertyAxiom) ob;
					resultstring = resultstring + "trans" + "(" + shorthandString(axiom.getProperty());
					resultstring = resultstring + ")";
				} 
				else
		if (ob instanceof OWLEquivalentClassesAxiom){
			List<OWLClassExpression> exprs =  ((OWLEquivalentClassesAxiom) ob).getClassExpressionsAsList();
			resultstring = resultstring + "equivalent(";
			boolean firstp = true;
			for (OWLClassExpression exp : exprs){
				if (!firstp) {resultstring = resultstring + ",";}
				firstp = false;
				resultstring = resultstring + shorthandString(exp);
			}
			resultstring = resultstring + ")";
		} 
		else
		if (ob instanceof OWLDisjointClassesAxiom){
			List<OWLClassExpression> exprs =  ((OWLDisjointClassesAxiom) ob).getClassExpressionsAsList();
			resultstring = resultstring + "disjoint(" +  shorthandString(exprs.get(0)) +  "," + shorthandString(exprs.get(1)) + ")";
		} 
		else
		if (ob instanceof OWLInverseObjectPropertiesAxiom){
				OWLObjectPropertyExpression expr1 =  ((OWLInverseObjectPropertiesAxiom) ob).getFirstProperty();
				OWLObjectPropertyExpression expr2 =  ((OWLInverseObjectPropertiesAxiom) ob).getSecondProperty();
				resultstring = resultstring + "inverse(" +  shorthandString(expr1) + ","+ shorthandString(expr2) + ")";
			} 
			else
		if (ob instanceof OWLPropertyRangeAxiom){
			resultstring = resultstring + "range(" +  shorthandString(((OWLPropertyRangeAxiom) ob).getProperty()) +  "," + shorthandString(((OWLPropertyRangeAxiom) ob).getRange()) + ")";
		}
		else
			if (ob instanceof OWLPropertyDomainAxiom){
				resultstring = resultstring + "domain(" +  shorthandString(((OWLPropertyDomainAxiom) ob).getProperty()) +  "," + shorthandString(((OWLPropertyDomainAxiom) ob).getDomain()) + ")";
			}
			else
		if (ob instanceof OWLSubClassOfAxiom){
			resultstring = resultstring + shorthandString(((OWLSubClassOfAxiom) ob).getSubClass()) +  "⊑" + shorthandString(((OWLSubClassOfAxiom) ob).getSuperClass());
		}
		else
			if (ob instanceof OWLSubObjectPropertyOfAxiom){
				resultstring = resultstring + shorthandString(((OWLSubObjectPropertyOfAxiom) ob).getSubProperty()) +  "⊑" + shorthandString(((OWLSubObjectPropertyOfAxiom) ob).getSuperProperty());
			}
			else
		if (ob instanceof OWLClassExpression && ((OWLClassExpression) ob).isOWLThing()){
			resultstring = resultstring + "⊤";
		}
		else
		if (ob instanceof OWLClassExpression && ((OWLClassExpression) ob).isOWLNothing()){
				resultstring = resultstring + "⊥";
			}
			else
		if (ob instanceof OWLObjectUnionOf){
			// System.out.println("debug union");
			// System.out.println(((OWLObjectUnionOf) ob).getOperandsAsList().size());
			if (((OWLObjectUnionOf) ob).getOperandsAsList().size()==2){
			resultstring = resultstring + 
					"("+ shorthandString(((OWLObjectUnionOf) ob).getOperandsAsList().get(0)) 
					+ "⊔" + shorthandString(((OWLObjectUnionOf) ob).getOperandsAsList().get(1)) + ")"; 
		} else  {// resultstring = resultstring + ob.toString();
			boolean firstp = true;
			resultstring = resultstring + "(";
			for (OWLClassExpression exp: ((OWLObjectUnionOf) ob).getOperandsAsList()){
				if (!firstp){ resultstring = resultstring + "⊔";}
				firstp = false;
				resultstring = resultstring + shorthandString(exp);
			}
			resultstring = resultstring + ")";
		}
		}
		else
		if (ob instanceof OWLObjectAllValuesFrom){
			// System.out.println("debug all");
			resultstring = resultstring + "∀" 
		+ shorthandString(((OWLObjectAllValuesFrom) ob).getProperty()) 
		+ "." + shorthandString(((OWLObjectAllValuesFrom) ob).getFiller());
		}
		else
			if (ob instanceof OWLObjectSomeValuesFrom){
				// System.out.println("debug some");
				resultstring = resultstring + "∃" 
			+ shorthandString(((OWLObjectSomeValuesFrom) ob).getProperty()) 
			+ "." + shorthandString(((OWLObjectSomeValuesFrom) ob).getFiller());
			}
			else
		if (ob instanceof OWLObjectIntersectionOf){
			// System.out.println("debug intersect");
			boolean firstp = true;
			resultstring = resultstring + "(";
			for (OWLClassExpression exp: ((OWLObjectIntersectionOf) ob).getOperandsAsList()){
				if (!firstp){ resultstring = resultstring + "⊓";}
				firstp = false;
				resultstring = resultstring + shorthandString(exp);
			}
			resultstring = resultstring + ")"; 
		} 
		else{
		if  (ob instanceof OWLObjectComplementOf){
			// System.out.println("debug compl");
			resultstring = resultstring + "¬" + "(" + shorthandString(((OWLObjectComplementOf) ob).getOperand()) + ")";			
		} else {
			if (ob instanceof OWLClass || ob instanceof OWLProperty || ob instanceof OWLPropertyRange || ob instanceof OWLDataProperty || ob instanceof OWLIndividual){
				String classstring = ob.toString();
				Matcher m = somepattern.matcher(classstring);
				String s = "";
				while (m.find()){
					 s = m.group(1);
				}
				resultstring = resultstring + s;
			}
			else {
			resultstring = resultstring + ob.toString();
		}	
		}
		}
		return resultstring;
	}
*/
	
	/* 
	public static java.lang.String pseudoNLStringExists(OWLObjectSomeValuesFrom existsexpr){
		OWLObjectPropertyExpression property = existsexpr.getProperty();
		OWLClassExpression filler = existsexpr.getFiller();
		java.lang.String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(property);
		java.lang.String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
		if (part1.equals("") && part2.equals("") || part1==null && part2==null){
			part1= "has " + existsexpr.getProperty().getNamedProperty().getIRI().getFragment() + "-successor ";
		}
		// System.out.println("PSEUDO NL STRING EXISTS PART2 " + part2);
		// System.out.println("PSEUDO NL STRING EXISTS RETURNS " + part1 + pseudoNLString(filler) + part2);
		return part1 + pseudoNLString(filler) + part2;
	}
	*/
	
	/*
	public static java.lang.String pseudoNLStringClass(OWLClass classexpr){
		java.lang.String str = VerbalisationManager.INSTANCE.getClassNLString(classexpr);
		return str;
	}
	*/
	
	/*
	public static java.lang.String pseudoNLSimpleIntersection(OWLObjectIntersectionOf ints){
		java.lang.String str = VerbalisationManager.INSTANCE.getSimpleIntersectionNLString(ints);
		return str;
	}
	*/
	
	/*
	
	public static String pseudoNLString(OWLClassExpression ob){
		VerbaliseClassExpressionVisitor verbaliseVisitor = new VerbaliseClassExpressionVisitor();
		return ob.accept(verbaliseVisitor);
	}
	
	
	public static String pseudoNLString(OWLAxiom ob){
		VerbaliseAxiomVisitor verbaliseVisitor = new VerbaliseAxiomVisitor();
		return ob.accept(verbaliseVisitor);
	}
	
	*/
	
	
	
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
	
	

	/*
	public List<FormulaType> retrieveFormulas(SequentSinglePosition pos){	
		List formulas = new ArrayList();
		if (pos.getSequentPart()==SequentPart.ANTECEDENT){
			if (this.antecedentGetFormula(pos.getToplevelPosition())!=null){
				formulas.add((FormulaType) this.antecedentGetFormula(pos.getToplevelPosition()).toOWLAPI());
			} else{
				formulas = getAntecedent();
				List<FormulaType>  list = new ArrayList<FormulaType>();
				FormulaType formula = (FormulaType) formulas.get(pos.getToplevelPosition());
				list.add(formula);
				return list;
			}
		}
		else formulas.add((FormulaType) this.succedentGetFormula(pos.getToplevelPosition()).toOWLAPI());
		return formulas;
	}
	*/
	
	
	/*
	public List<FormulaType> retrieveFormulas(SequentPositionInNode pos){
		// System.out.println("retrieve Formulas with SequentPositionInNode called.");
		SequentPosition seqpos = pos.getSequentPosition();
		return retrieveFormulas(seqpos);
	}
	*/
	
	/*
	public List<FormulaType> retrieveFormulas(SequentPosition pos){
		// System.out.println("retrieve Formulas with SequentPosition called.");
		if (pos instanceof SequentSinglePosition) return retrieveFormulas((SequentSinglePosition) pos);
		if (pos instanceof SequentMultiPosition) return retrieveFormulas((SequentMultiPosition) pos);
		return null;
	}
	*/
	
	/*
	public List<FormulaType> retrieveFormulas(SequentMultiPosition pos){
		List formulas = new ArrayList();
		List<FormulaType>  list = new ArrayList<FormulaType>();
		if (pos.getSequentPart()==SequentPart.ANTECEDENT){
				formulas = getAntecedent();
		}
		if (pos.getSequentPart()==SequentPart.SUCCEDENT){
			formulas = getSuccedent();
		}	
		Integer[][] positions = pos.getPositions();
		for (int i = 0; i < positions.length; i++){
			FormulaType formula = (FormulaType) formulas.get(positions[i][0]);
			list.add(formula);
		}
		return list;
	}
	*/
	
	/*
	public List<FormulaType> retrieveFormulas(RuleBinding bin){
		List formulas = new ArrayList();
		List<FormulaType>  list = new ArrayList<FormulaType>();
		
		HashMap bindings = bin.getBindings();
		// System.out.println("DEBUG bindings " + bindings);
		Collection all_positions = bindings.values();
		// System.out.println("DEBUG all positions " + all_positions);
		for (Object b : all_positions){
			if (b instanceof SequentSinglePosition){
				list.addAll(retrieveFormulas((SequentSinglePosition) b));
			}
			if (b instanceof SequentMultiPosition){
			    list.addAll(retrieveFormulas((SequentMultiPosition) b));
			}	
			if (b instanceof SequentPositionInNode){
			    list.addAll(retrieveFormulas(((SequentPositionInNode) b).getSequentPosition()));
			}	
		}
		
		return list;
	}
	*/
	
	public OWLFormula getOWLSubformula(OWLFormula formula, List<Integer> pos){
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
	
	public void insertIntoSubexprsTree(OWLFormula formula) throws Exception{
			if(!subexprs.contains(formula)){
			subexprs.insert(formula);}
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
		Sequent clone = this.clone(); // produce ordinary clone
		// amputate antecedent
		Set<OWLFormula> allAntecedentForms = clone.antecedent.getAllFormulas();
		for (OWLFormula ant : allAntecedentForms){
			int antdepth = getFormulaDepth(antecedent.formulaGetID(ant));
			if (antdepth>depth){
				clone.antecedent.remove(ant);
				// System.out.println("DEBUG removing " + ant);
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
		return clone;
	}
	
	
}
