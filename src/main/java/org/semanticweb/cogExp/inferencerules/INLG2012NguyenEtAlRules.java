package org.semanticweb.cogExp.inferencerules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.semanticweb.cogExp.core.AbstractSequentPositions;
import org.semanticweb.cogExp.core.IncrementalSequent;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.JustificationNode;
import org.semanticweb.cogExp.core.Pair;
import org.semanticweb.cogExp.core.ProofNode;
import org.semanticweb.cogExp.core.ProofTree;
import org.semanticweb.cogExp.core.RuleApplicationResults;
import org.semanticweb.cogExp.core.RuleBinding;
import org.semanticweb.cogExp.core.RuleKind;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.core.SequentList;
import org.semanticweb.cogExp.core.SequentPart;
import org.semanticweb.cogExp.core.SequentPosition;
import org.semanticweb.cogExp.core.SequentSinglePosition;
import org.semanticweb.cogExp.core.Timer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.cogExp.core.AlreadyTriedCache;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLFormulas.OWLAtom;
import org.semanticweb.cogExp.OWLFormulas.OWLClassName;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.OWLFormulas.OWLSymb;
import org.semanticweb.cogExp.OWLFormulas.TermTree;


public enum INLG2012NguyenEtAlRules implements SequentInferenceRule{
	
	RULE1neo{ // $1
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule1neo";};
		@Override
		public java.lang.String getShortName(){return "R1n";};
		

		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			Set<RuleBinding> results = new HashSet<RuleBinding>();
			HashSet<OWLFormula> antecedent = s.getAllAntecedentOWLFormulas();
			for (OWLFormula formula : antecedent){
				if (formula.getHead().equals(OWLSymb.EQUIV)){
					List<OWLFormula> tail = formula.getArgs();
					boolean containsThing = false;
					for (OWLFormula form : tail){
						if (form.isTop()){
							containsThing = true;
							break;
						}
					}
					if (containsThing){
						Set<OWLFormula> all_formulas = s.getAllAntecedentOWLFormulas();
						Set<OWLClassName> all_classnames = new HashSet<OWLClassName>();
						all_formulas.addAll(s.getAllSuccedentOWLFormulas());
						for (OWLFormula form : all_formulas){
							all_classnames.addAll(TermTree.decomposeClassNames(form));
						}
						for (OWLFormula form : tail){
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,OWLFormula.createFormulaTop(),form);
							if (!form.isTop() && !s.alreadyContainedInAntecedent(conclusion)){
								RuleBinding binding = new RuleBinding(conclusion,null);
								// SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(formula));
								// binding.insertPosition("A1", position1);
								results.add(binding);
							}
							for (OWLClassName cl : all_classnames){
								conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,new OWLFormula(cl),form);
								if (!conclusion.getArgs().get(0).equals(conclusion.getArgs().get(1)) && !s.alreadyContainedInAntecedent(conclusion)){
									RuleBinding binding = new RuleBinding(conclusion,null);
									// SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(formula));
									// binding.insertPosition("A1", position1);
									results.add(binding);
							}
							}
						}
			
					}
				}
			}
			// System.out.println("Rule 1neo DEBUG results " + results);
			return new ArrayList<RuleBinding>(results);
		}
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s, boolean ... saturate) {
			return findRuleBindings(s);
		}
		
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 1 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
		}
		
		
	}, // END RULE6new	
	
RULE1{ // $2
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule1";};
// public java.lang.String getName(){return "R1";};
@Override
public java.lang.String getShortName(){return "R1";};
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// System.out.println("nguyen-1 find rule bindings called");
			Set<RuleBinding> results = new HashSet<RuleBinding>();
			if (s.isEmptyP()) return new ArrayList<RuleBinding>();
			HashSet<OWLFormula> antecedent = s.getAllAntecedentOWLFormulas();
			// ArrayList antecedent =  s.getAntecedent();
			// System.out.println(antecedent);
			int count1 = 0;
			for (OWLFormula formula : antecedent){
				if (formula.getHead().equals(OWLSymb.EQUIV)){
					// ATTENTION!
					List<OWLFormula> tail = formula.getArgs();
					boolean containsThing = false;
					for (OWLFormula form : tail){
						if (form.isTop()){
							containsThing = true;
							break;
						}
					}
					if (containsThing){
						return  new ArrayList<RuleBinding>();
					}
					// END OF ATTENTION PART!
					// System.out.println("nguyen-1: We have equiv axiom: " + formula);
					List<OWLFormula> expressions = formula.getArgs();
					// loop for X
					int count2 = 0;
					for (OWLFormula exp1 : expressions){
						int count3 = 0;
						for (OWLFormula exp2 : expressions){
							if (!exp1.equals(exp2)){
								// first, need to check if the subset relationship exists already
								OWLFormula axiom3 = OWLFormula.createFormula(OWLSymb.SUBCL,exp1,exp2);
								// System.out.println("nguyen1: probing conclusion formula " + axiom3);
								boolean exists = false;
								boolean subsumee_exists = false;
								boolean subsumer_exists = false;
								// Loop
								for (Object checkformula : antecedent){
									if (checkformula.equals(axiom3)){
										// System.out.println(checkformula);
										exists = true;
									}
									OWLFormula negExp1 = OWLFormula.createFormula(OWLSymb.NEG, exp1);
									if (negExp1.equals(checkformula)){
										subsumee_exists = true;
										// System.out.println("subsumee exists");
									}
									if (exp2.equals(checkformula)){
										subsumer_exists = true;
										// System.out.println("subsumer exists");
									}
									if (exp2.equals(checkformula)){
										exists = true;
										// System.out.println("formula already exists");
									}
								}		
							// System.out.println(!subsumee_exists);	
							// System.out.println(!subsumer_exists);	
							// System.out.println(!exists);	
							if (!subsumee_exists && !subsumer_exists && !exists){ // -subsumee or subsumer is enough, don't need to have the exact subsumption. Otherwise, will run into loop with subset-elim-left	
							// System.out.println("Rule1: making rule binding! For " + axiom3);
								RuleBinding binding = new RuleBinding();
							List<Integer> list1 = new ArrayList<Integer>();
							// list1.add(count1);
							list1.add(s.antecedentFormulaGetID(formula));
							list1.add(count2);
							List<Integer> list2 = new ArrayList<Integer>();
							list2.add(s.antecedentFormulaGetID(formula));
							list2.add(count3);
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, list1);
							binding.insertPosition("A1", position1);
							SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, list2);
							binding.insertPosition("A2", position2);
							results.add(binding);
							} // end if not exists
							}
							count3++;
						}
						count2++;
					}
					
				}
				count1++;	
			} // end outer for loop
			// System.out.println("nguyen 1 results: " +  results);
			return new ArrayList<RuleBinding>(results);
		}
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			// System.out.println("Rule1 : application called!");
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent; // .clone();
			// ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			List<RuleApplicationResults> results = new ArrayList();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule1");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule1");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			
			List<Integer> poslist1 = pos1.getPosition();
			List<Integer> poslist2 = pos2.getPosition();
			// System.out.println(poslist1);
			// System.out.println(poslist2);
			OWLFormula formula1 = s.antecedentGetFormula(pos1.getToplevelPosition());
			if (formula1.getHead().equals(OWLSymb.EQUIV)){
				OWLFormula exp1 = formula1.getArgs().get(poslist1.get(1));
				OWLFormula exp2 = formula1.getArgs().get(poslist2.get(1));
				// construct new term
				OWLFormula axiom3 = OWLFormula.createFormula(OWLSymb.SUBCL, exp1,exp2);
			    // System.out.println("Rule 1 : adding " + axiom3);
				 if (!sequent.alreadyContainedInAntecedent(axiom3)){
						RuleApplicationResults results1 = new RuleApplicationResults();
						results1.setOriginalFormula(s);
						results1.addAddition("A1", axiom3);
						results.add(results1);	
				//	s.addAntecedent(axiom3);	
				//    newsequents.add(s);
						// System.out.println("DEBUG R1 computed depth " + InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
						results1.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
				 }
			}
			return results;
		}
		
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			return SequentList.makeANDSequentList(sequents);
		}
		
	
		
		@Override
		public List<RuleKind> qualifyRule(){
			RuleKind[] a = {RuleKind.PSEUDOELIMINATION, RuleKind.FORWARD};
			return Arrays.asList(a);
		}
		
	}, // END RULE1



	
	RULE2BIN{	// $3
		
		@Override
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule2bin";};
	@Override
	public java.lang.String getShortName(){return "R2";};
	
	
	
	private final OWLFormula prem = OWLFormula.createFormula(OWLSymb.SUBCL, 
			OWLFormula.createFormulaVar("v1"), 
			OWLFormula.createFormula(OWLSymb.INT, 
					OWLFormula.createFormulaVar("v2"),
					OWLFormula.createFormulaVar("v3")));

	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s){
		return findRuleBindings(s,false);
	}
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
		// System.out.println("DEBUG! -- find rule bindings for rule 2 called!");
		Timer.INSTANCE.start("rule2bindings");
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
	
		List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem);
		for (OWLFormula candidate : candidates){
			for (int i = 0; i<candidate.getArgs().size(); i++){
				// build resulting formula and check if contained
				OWLFormula resultformula = OWLFormula.createFormula(OWLSymb.SUBCL, candidate.getArgs().get(0), candidate.getArgs().get(1).getArgs().get(i));
				// System.out.println(" this would be the result " + resultformula);
				if (!s.alreadyContainedInAntecedent(resultformula)){
					 List<Integer> list = new ArrayList<Integer>();
					 // System.out.println(candidate);
					 //  System.out.println(s.antecedentFormulaGetID(candidate));
					 // System.out.println(s.antecedentGetFormula(s.antecedentFormulaGetID(candidate)));
					 list.add(s.antecedentFormulaGetID(candidate));
					 list.add(i);
					 SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, list);
					 RuleBinding binding = new RuleBinding();
					 binding.insertPosition("A1", position1);
					 results.add(binding);
					 if (one_suffices.length>0 && one_suffices[0]==true){return results;}
						}
					}	
		}
		Timer.INSTANCE.stop("rule2bindings");
		return results;
	}	
		
	
	@Override
	public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
		List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
		// System.out.println(" NGUYEN 2 called" + sequent  + binding.get("A1"));
		SequentPosition position1 = binding.get("A1");
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule2");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		// attention, order of formulas can get swapped when cloning.
		OWLFormula formula = sequent.antecedentGetFormula(pos1.getToplevelPosition());
		// System.out.println("formula " + formula);
		int subposition = pos1.getPosition().get(1);
		OWLFormula formula_int = formula.getArgs().get(1); 
		// System.out.println("subposition " + subposition);
		// System.out.println("formula_int " + formula_int);
		OWLFormula formula2 = formula_int.getArgs().get(subposition);
		// System.out.println("formula2 " + formula2);
		OWLFormula formula3 = OWLFormula.createFormula(OWLSymb.SUBCL, formula.getArgs().get(0), formula2);
		// OWLSubClassOfAxiom axiom3 = (OWLSubClassOfAxiom) formula3.toOWLAPI();
			if (!sequent.alreadyContainedInAntecedent(formula3)){
				RuleApplicationResults result = new RuleApplicationResults();
				result.setOriginalFormula(sequent);
				result.addAddition("A1",formula3);
				results.add(result);
			    
			}
			
		return results;
	}
	
	@Override
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
		List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
		return SequentList.makeANDSequentList(sequents);
	}
	
	@Override
	public List<RuleKind> qualifyRule(){
		RuleKind[] a = {RuleKind.FORWARD};
		return Arrays.asList(a);
	}
	
}, // END RULE2

RULE2{	// $4
	
	@Override
public java.lang.String getName(){return "INLG2012NguyenEtAlRule2";};
@Override
public java.lang.String getShortName(){return "R2";};



private final OWLFormula prem = OWLFormula.createFormula(OWLSymb.SUBCL, 
		OWLFormula.createFormulaVar("v1"), 
		OWLFormula.createFormulaVar("v2"));


@Override
public List<RuleBinding> findRuleBindings(Sequent s){
	return findRuleBindings(s,false);
}

@Override
public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
	
	Timer.INSTANCE.start("rule2bindings");
	ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();

	List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem);
	for (OWLFormula candidate : candidates){
		// find those candidates that actually have intersection as superclass, and then iterate over its arguemtents
					OWLFormula superclass = candidate.getArgs().get(1);
					if (!superclass.getHead().equals(OWLSymb.INT)){
						continue;
					}
					for (int i = 0; i<superclass.getArgs().size(); i++){
			// build resulting formula and check if contained
			OWLFormula resultformula = OWLFormula.createFormula(OWLSymb.SUBCL, candidate.getArgs().get(0), superclass.getArgs().get(i));
			// System.out.println(" this would be the result " + resultformula);
			boolean notcontained = true;
			if (s.alreadyContainedInAntecedent(resultformula))
				notcontained = false;
			if (s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(resultformula))
				notcontained = false; 
			if (notcontained){
				 List<Integer> list = new ArrayList<Integer>();
				 // System.out.println(candidate);
				 //  System.out.println(s.antecedentFormulaGetID(candidate));
				 // System.out.println(s.antecedentGetFormula(s.antecedentFormulaGetID(candidate)));
				 list.add(s.antecedentFormulaGetID(candidate));
				 list.add(i);
				 SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, list);
				 RuleBinding binding = new RuleBinding(resultformula,null);
				 binding.insertPosition("A1", position1);
				 results.add(binding);
				 if (one_suffices.length>0 && one_suffices[0]==true){return results;}
					}
				}	
	}
	Timer.INSTANCE.stop("rule2bindings");
	return results;
}	
	

@Override
public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
	List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
	
	OWLFormula formula3 = binding.getNewAntecedent();
	// SequentPosition position1 = binding.get("A1");
	// if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule2");}
	// SequentSinglePosition pos1 = (SequentSinglePosition) position1;
	// attention, order of formulas can get swapped when cloning.
	// OWLFormula formula = sequent.antecedentGetFormula(pos1.getToplevelPosition());
	// System.out.println("formula " + formula);
	// int subposition = pos1.getPosition().get(1);
	// OWLFormula formula_int = formula.getArgs().get(1); 
	// System.out.println("subposition " + subposition);
	// System.out.println("formula_int " + formula_int);
	//  OWLFormula formula2 = formula_int.getArgs().get(subposition);
	// System.out.println("formula2 " + formula2);
	// OWLFormula formula3 = OWLFormula.createFormula(OWLSymb.SUBCL, formula.getArgs().get(0), formula2);
	// OWLSubClassOfAxiom axiom3 = (OWLSubClassOfAxiom) formula3.toOWLAPI();
		if (!sequent.alreadyContainedInAntecedent(formula3)){
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",formula3);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
		    // System.out.println(" NGUYEN 2 COMPUTED PREMISE!" + formula3);
		}
		// System.out.println("check after Nguyen2; sequent: " +sequent);
		// System.out.println("check after Nguyen2; s: " + s);
	return results;
}

@Override
public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
	List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
	List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
	return SequentList.makeANDSequentList(sequents);
}

@Override
public List<RuleKind> qualifyRule(){
	RuleKind[] a = {RuleKind.FORWARD};
	return Arrays.asList(a);
}

@Override
public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
	for (OWLFormula form : formulalist){
			return form;
	}
	return null;
}

}, // END RULE2

/*
RULE3{	public java.lang.String getName(){return "INLG2012NguyenEtAlRule3";};
public java.lang.String getShortName(){return "R3";};

public List<RuleBinding> findRuleBindings(Sequent s){
	ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
	if (s.isEmptyP()) return results;
	ArrayList antecedent = s.getAntecedent();
	int count1 = 0;
	for (Object formula1 : antecedent){
		int count2 = 0;
		if (formula1 instanceof OWLObjectPropertyDomainAxiom){
			// we wound a candidate for A1, now loop for A2	
			for (Object formula2 : antecedent){	
				if (formula2 instanceof OWLSubClassOfAxiom){
					OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
					OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
					if (axiom1.getDomain().equals(axiom2.getSubClass())){
						RuleBinding binding = new RuleBinding();
						SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
						binding.insertPosition("A1", position1);
						SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
						binding.insertPosition("A2", position2);
						results.add(binding);				
					}	
				}
				count2++;
			}
		}
		count1++;
	}	
	return results;
}

public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
	SequentPosition position1 = binding.get("A1");
	SequentPosition position2 = binding.get("A2");
	Sequent s = sequent.clone();
	ArrayList antecedent = s.getAntecedent();
	ArrayList newsequents = new ArrayList<Sequent>();
	
	if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule3");}
	SequentSinglePosition pos1 = (SequentSinglePosition) position1;
	if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule3");}
	SequentSinglePosition pos2 = (SequentSinglePosition) position2;
	Object formula1 = antecedent.get(pos1.getToplevelPosition());
	Object formula2 = antecedent.get(pos2.getToplevelPosition());
	if (formula1 instanceof OWLObjectPropertyDomainAxiom && formula2 instanceof OWLSubClassOfAxiom ){
		OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
		OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
		if (axiom1.getDomain().equals(axiom2.getSubClass())){
			// construct new term
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			OWLObjectPropertyDomainAxiom axiom3 = dataFactory.getOWLObjectPropertyDomainAxiom(axiom1.getProperty(),axiom2.getSuperClass());
			if (!antecedent.contains(axiom3)){
				s.addAntecedent(axiom3);	
			    newsequents.add(s);
			}
		}
	}
	return SequentList.makeANDSequentList(newsequents);
}

public List<RuleKind> qualifyRule(){
	RuleKind[] a = {RuleKind.FORWARD};
	return Arrays.asList(a);
}

}, // END RULE3
*/

// $5
RULE3{ // dom(r,X) and SubCla(X,Y) --> dom(r,Y)
	
	@Override
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule3";};
	@Override
	public java.lang.String getShortName(){return "R3";};
	
	private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.DOMAIN, 
			OWLFormula.createFormulaRoleVar("r1"),
			OWLFormula.createFormulaVar("v1"));
	
	private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
			OWLFormula.createFormulaVar("v1"),
			OWLFormula.createFormulaVar("v2"));
			
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s){
	    // System.out.println("Rule 3: find premises called!");
	    List<RuleBinding> results = new ArrayList<RuleBinding>();
		List<OWLFormula> candidates1 = s.findMatchingFormulasInAntecedent(prem1);
		// System.out.println("candidates1: "+ candidates1.toString());
		for (int i = 0 ; i < candidates1.size(); i++){
			    List<OWLFormula> candidates2 = new ArrayList<OWLFormula>();
				// get matcher 
				try{
				List<Pair<OWLFormula,OWLFormula>> matcher = candidates1.get(i).match(prem1);
				// System.out.println("prem 2 " + prem2 +  " Matcher: " + matcher);
				// now build new formula using the matcher
				OWLFormula prem_2 = prem2.applyMatcher(matcher);
				// System.out.println("prem_2 " + prem_2);
				// now search for this formula in sequent
				candidates2 = s.findMatchingFormulasInAntecedent(prem_2);
				// System.out.println("candidates 2" + candidates2.toString());
				 // Check if found formulas actually match
				for (OWLFormula candidate2: candidates2){
					 OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.DOMAIN,
							 candidates1.get(i).getArgs().get(0),
							 candidate2.getArgs().get(1)
							 );	
							// System.out.println("conclusion: " + conclusion);
							if (!s.alreadyContainedInAntecedent(conclusion)){
									RuleBinding binding = new RuleBinding(conclusion,null);																						
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidates1.get(i)));
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate2));
									binding.insertPosition("A2", position2);
									results.add(binding);
									
							} // end if
				}
				} catch (Exception e){}
		} // end formula 1 loop
				
		
		// System.out.println("DEBUG === results " + results);			
		return results;
	}
	
	@Override
	public List<RuleApplicationResults>  computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		SequentPosition position3 = binding.get("A3");
		Sequent s = sequent; //.clone();
		ArrayList newsequents = new ArrayList<Sequent>();
		
		List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule3");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule3");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		
		OWLFormula formula1 = s.antecedentGetFormula(pos1.getToplevelPosition());
		OWLFormula formula2 = s.antecedentGetFormula(pos2.getToplevelPosition());
		// Object formula1 = antecedent.get(pos1.getToplevelPosition());
		// Object formula2 = antecedent.get(pos2.getToplevelPosition());
		// Object formula3 = antecedent.get(pos3.getToplevelPosition());
		OWLFormula conclusion = binding.getNewAntecedent();
		// System.out.println(formula1);
		// System.out.println(formula2);
		// System.out.println(formula3);
		// System.out.println(conclusion);
			if (!sequent.alreadyContainedInAntecedent(conclusion)){
				RuleApplicationResults results1 = new RuleApplicationResults();
				results1.setOriginalFormula(s);
				results1.addAddition("A1", conclusion);
				results.add(results1);	
				// System.out.println("DEBUG === " + conclusion);
				// depth bookkeeping
				results1.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			}
		return results;
	}
	
	
	@Override
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
		List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
		return SequentList.makeANDSequentList(sequents);
	}
			
}, // END RULE3


/*
RULE4{	public java.lang.String getName(){return "INLG2012NguyenEtAlRule4";};
public java.lang.String getShortName(){return "R4";};

public List<RuleBinding> findRuleBindings(Sequent s){
	ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
	if (s.isEmptyP()) return results;
	ArrayList antecedent = s.getAntecedent();
	int count1 = 0;
	for (Object formula : antecedent){
		if (formula instanceof OWLSubClassOfAxiom){
			OWLSubClassOfAxiom axiom = (OWLSubClassOfAxiom) formula;
			OWLClassExpression expression = axiom.getSubClass();
			if (expression instanceof OWLObjectUnionOf){
				OWLObjectUnionOf intersection = (OWLObjectUnionOf) expression;
				List<OWLClassExpression> operands = ((OWLObjectUnionOf) expression).getOperandsAsList();
				for (int i=0; i < operands.size() ; i++){
					// need to check that the resulting formula does not exist yet
					boolean exists = false;
					for(Object check_formula: antecedent){
						if (check_formula instanceof OWLSubClassOfAxiom 
								&& ((OWLSubClassOfAxiom) check_formula).getSuperClass().equals(axiom.getSuperClass())
								&& operands.get(i).equals(((OWLSubClassOfAxiom) check_formula).getSubClass())
								){
							exists = true;
						}
					}
					if (!exists){
					RuleBinding binding = new RuleBinding();
					List<Integer> list = new ArrayList<Integer>();
					list.add(count1); // formula in sequent
					list.add(0);      //left side of subclass
					list.add(i);      // position within union
					// System.out.println(list);
					SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, list);
					binding.insertPosition("A1", position1);
					results.add(binding);
					}
				}
			}
		}
		count1++;
	}
	return results;
}

public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
	List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
	SequentPosition position1 = binding.get("A1");
	Sequent s = sequent.clone();
	ArrayList antecedent = s.getAntecedent();
	ArrayList newsequents = new ArrayList<Sequent>();
	
	if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule4");}
	SequentSinglePosition pos1 = (SequentSinglePosition) position1;
	// System.out.println(pos1.getPosition());
	Object formula1 = antecedent.get(pos1.getToplevelPosition());
	if (formula1 instanceof OWLSubClassOfAxiom){
		OWLSubClassOfAxiom axiom = (OWLSubClassOfAxiom) formula1;
		OWLClassExpression expression1 = axiom.getSubClass();
		OWLClassExpression expression2 = axiom.getSuperClass();
		if (expression1 instanceof OWLObjectUnionOf){
			// System.out.println(expression1);
			List<OWLClassExpression> operands = ((OWLObjectUnionOf) expression1).getOperandsAsList();
			// System.out.println(pos1.getPosition().get(2));
			// System.out.println(operands);
			OWLClassExpression operand = operands.get(pos1.getPosition().get(2));				
		// construct new term
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory=manager.getOWLDataFactory();
		OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(operand,expression2);
		if (!antecedent.contains(axiom3)){
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(s);
			result.addAddition("A1",axiom3);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			}
		}		
	}
	return results;
}

public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
	List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
	List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
	return SequentList.makeANDSequentList(sequents);
}



public List<RuleKind> qualifyRule(){
	RuleKind[] a = {RuleKind.FORWARD};
	return Arrays.asList(a);
}

}, // END RULE 4
*/

//$6
RULE5new{ //SubClass(X,Y) and SubClass(X,Z) --> Subclass(X,Y^Z)
	
	@Override
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule5";};
	@Override
	public java.lang.String getShortName(){return "R5";};
	
	private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
			OWLFormula.createFormulaVar("v1"), 
			OWLFormula.createFormulaVar("v2"));

	@Override
	public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
		OWLFormula firstConjunct = conclusion.getArgs().get(1).getArgs().get(0);
		for (OWLFormula form : formulalist){
			if (form.getArgs().get(1).equals(firstConjunct))
				return form;
		}
		return null;
	}
	
	
	
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s){
		return findRuleBindings(s,false);
	}
	
public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
	List<RuleBinding> results = new ArrayList<RuleBinding>();
	List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
	Set<OWLFormula> subexprsdash = s.getAllSubExprs();
	Set<OWLFormula> intsubexprs = new HashSet<OWLFormula>();
	for (OWLFormula exp: subexprsdash){
		if (exp.getHead().equals(OWLSymb.INT))
			intsubexprs.add(exp);
			// System.out.println("Subexprs " + exp.prettyPrint());
	}
	// System.out.println("PROFILE intsubexprs size " + intsubexprs.size());
	
	
	// s.reportSubexpressionFormulas();
	// s.reportAntecedent();
	
	/*
	System.out.println("REPORT INT SUBEX");
	for (OWLFormula intsubex :intsubexprs){
		System.out.println(intsubex.prettyPrint());	
	}
	*/
	
	// Put candidates into bins with same first subformula
			HashMap<OWLFormula,Set<OWLFormula>> bins = new HashMap<OWLFormula,Set<OWLFormula>>();
			for (OWLFormula cands : candidates){
				Set<OWLFormula> res = bins.get(cands.getArgs().get(0));
				if (res!=null){
					res.add(cands);
				}
				else {
					HashSet newset = new HashSet<OWLFormula>();
					newset.add(cands);
				bins.put(cands.getArgs().get(0),newset);
				}
			}
			// System.out.println("PROFILE: bins size "  + bins.size());
			Set<OWLFormula> keys = bins.keySet();
			for (OWLFormula p1 : keys){
				// if (p1.getHead().toString().contains("holoenzyme"))
				//  System.out.println("Current p1 " + p1);
			Set<OWLFormula> pool = bins.get(p1);
			for (OWLFormula cand1 : pool){
				
			        /*
				   if (p1.getHead().toString().contains("holoenzyme")
				  		 && cand1.toString().contains("macro"))
				      System.out.println("Cand1 " + cand1.prettyPrint());
				      */
				     
				for (OWLFormula cand2 : pool){
					/*
					if (p1.getHead().toString().contains("holoenzyme")
				     		 && cand2.toString().contains("polymer")
						 )
					     System.out.println("Cand2 " + cand2.prettyPrint());
					     */
					      
					OWLFormula p2 = cand1.getArgs().get(1);
					OWLFormula p3 = cand2.getArgs().get(1);
					
					// System.out.println("p2 " + p2);
					
					
					if (!p2.equals(p3)){
						
						// Skip if this was already tried.
						List<OWLFormula> forms = new LinkedList<OWLFormula>();
						forms.add(cand1);
						forms.add(cand2);
						
						if (AlreadyTriedCache.INSTANCE.wasTried(INLG2012NguyenEtAlRules.RULE5,
								forms)){
						   // System.out.println("Rule5new: skipping previously tried case");
							continue;
						}	
						
						boolean isGood = false;
						boolean containsAll = false;
						OWLFormula goodConjunct = null;
						List<OWLFormula> conjuncts_p2 = p2.getConjuncts();
						List<OWLFormula> conjuncts_p3 = p3.getConjuncts();
						List<OWLFormula> conjuncts = new ArrayList<OWLFormula>(); // <------- PROBLEM WHEN USING SET HERE -- the order is not maintained!
						conjuncts.addAll(conjuncts_p2);
						conjuncts.addAll(conjuncts_p3);
						
						//remove duplicates if any
						Set setItems = new LinkedHashSet(conjuncts);
						conjuncts.clear();
						conjuncts.addAll(setItems);
						
						// we check that the combined conjuncts do not have overlap
						if (conjuncts.size() != conjuncts_p2.size() + conjuncts_p3.size())
						{
							AlreadyTriedCache.INSTANCE.setTried(INLG2012NguyenEtAlRules.RULE5, forms);
							continue;
						}
						// System.out.println("p_2" + p2);
						// System.out.println("p_3" + p3);
						// System.out.println("conjuncts p_2" + conjuncts_p2);
						// System.out.println("conjuncts p_3" + conjuncts_p3);
						
						/*
						if (p1.getHead().toString().contains("holoenzyme") && p2.toString().contains("macromol")
								&& p3.toString().contains("polymer") 
								)
						System.out.println("interesting: " + p2.prettyPrint() + " " + p3.prettyPrint());
						*/
							
						for (OWLFormula intexpr : intsubexprs){
							List<OWLFormula> conjunct = intexpr.getConjuncts();
							// if (conjunct.size()>2)
							// 	System.out.println("conjunct: " + conjunct);
							
							/*
							if (p1.getHead().toString().contains("holoenzyme") && p2.toString().contains("macromol")
									&& p3.toString().contains("poly") && conjunct.size()==2

									){
							  System.out.println("constructed conjuncts " + conjuncts);
							 System.out.println("conjunct " + conjunct);
							}
							*/

							
							
								// Check if found conjuncts are contained (in the right order) in subexpression conjunct
								boolean allIn = true;
								int conjunctpointer = 0;
								for (OWLFormula elem : conjuncts){
									boolean found = false;
									while (conjunctpointer < conjunct.size()){
										if (conjunct.get(conjunctpointer).equals(elem)){
											found = true;
											break;
										}
										conjunctpointer++;
									}
									if (!found)
										allIn = false;
									// if (!conjunct.contains(elem))
									// 	allIn = false;
								}
								if (allIn && conjuncts_p2.size() + conjuncts_p3.size()  ==conjunct.size()){
									containsAll = true;
									goodConjunct = intexpr;
								}
								if (allIn){
									isGood = true;
								} 
								
								/*
							    int members = 0;
							    for (OWLFormula c : conjunct){
									// if (conjuncts_p2.contains(c)) members++;
									// if (conjuncts_p3.contains(c)) members++;
									if (conjuncts.contains(c)) members++;
								}
								if (members>1) isGood = true;
								if (members>=conjunct.size()){
									containsAll=true;
									goodConjunct = intexpr;
								}
								*/
							}
							
					/*
						if (p1.getHead().toString().contains("VT608") && p2.toString().contains("OBA")
								&& p3.toString().contains("affects") && p3.toString().contains("attribute")
								)
						System.out.println(goodConjunct);
						*/
						
						/*
						 if (containsAll && goodConjunct!=null && p1.getHead().toString().contains("Spatial")
								&& cand1.toString().contains("C131")
								&& cand1.toString().contains("C409")
								&& cand1.toString().contains("C499")
								&& cand2.toString().contains("443")
								&& cand2.toString().contains("771")
								){
							System.out.println("p2 " + p2);
							System.out.println("p3 " + p3);
						System.out.println("found good conjunct " + goodConjunct);
						}
						*/
						
						if (containsAll && goodConjunct!=null){
							//  if (p1.getHead().toString().contains("TO_0000639"))
							// System.out.println("good conjunct " + VerbalisationManager.prettyPrint(goodConjunct));
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,p1,goodConjunct);
							
							/*
							if (conclusion.toString().contains("Spatial")
									&& conclusion.toString().contains("131")
									&& conclusion.toString().contains("409")
									&& conclusion.toString().contains("419")
									&& conclusion.toString().contains("443")
									&& conclusion.toString().contains("771")
									)
							System.out.println("conclusion " + conclusion + "already contained: " + s.alreadyContainedInAntecedent(conclusion));
							*/
						
							boolean notcontained = true;
							if (s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(conclusion)
									|| s.alreadyContainedInAntecedent(conclusion)){
								AlreadyTriedCache.INSTANCE.setTried(INLG2012NguyenEtAlRules.RULE5, forms);
								notcontained = false;
							}
							// if (s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(conclusion))
							// 	notcontained = false; 
							if (notcontained){
								/*
								if (conclusion.toString().contains("VT")
										&& conclusion.toString().contains("affects")
										&& conclusion.toString().contains("CHEBI")
										)
								System.out.println("conclusion passed" + conclusion);
								*/
								   //  if (p1.getHead().toString().contains("TO_0000639"))
									//   System.out.println("generated conclusion " + conclusion);
								// System.out.println("Rule 5 debug generated binding with conclusion " + conclusion);
								RuleBinding binding = new RuleBinding(conclusion,null);
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand1));
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand2));
								binding.insertPosition("A1", position1);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
							continue;
						}
						
						if (isGood){
							// System.out.println("ISGOOD");
							OWLFormula intersection = OWLFormula.createFormula(OWLSymb.INT,p2,p3);			
							if (p2.getHead().equals(OWLSymb.INT) && p3.getHead().equals(OWLSymb.INT)){
								List<OWLFormula> args = new ArrayList<OWLFormula>(p2.getArgs());
								args.addAll(p3.getArgs());
								intersection = OWLFormula.createFormula(OWLSymb.INT,args);
							}
							if (p2.getHead().equals(OWLSymb.INT) && !p3.getHead().equals(OWLSymb.INT)){
								List<OWLFormula> args = new ArrayList<OWLFormula>(p2.getArgs());
								args.add(p3);
								intersection = OWLFormula.createFormula(OWLSymb.INT,args);
							}
							if (p3.getHead().equals(OWLSymb.INT) && !p2.getHead().equals(OWLSymb.INT)){
								List<OWLFormula> args = new ArrayList<OWLFormula>(p3.getArgs());
								args.add(p2);
								intersection = OWLFormula.createFormula(OWLSymb.INT,args);
							}
							
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,p1,intersection);
							
							/*
							if (p1.getHead().toString().contains("Spatial") // && p2.toString().contains("C131")
									// && p3.toString().contains("409") && p3.toString().contains("419")
									){
							System.out.println("ISGOOD CONCLUSION " + conclusion);
							}
							*/
							
							boolean notcontained = true;
							if (s.alreadyContainedInAntecedent(conclusion))
								notcontained = false;
							// if (s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(conclusion))
							// 	notcontained = false; 
							if (notcontained){
								// System.out.println("Rule 5 debug generated binding with conclusion " + VerbalisationManager.prettyPrint(conclusion));
								RuleBinding binding = new RuleBinding(conclusion,null);
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand1));
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand2));
								binding.insertPosition("A1", position1);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
							
						} // end isGood	
					
							
						
					} // if not equals
				}// loop cand2
			} // loop cand1
			}
	return results;
}

@Override
public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
	List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
	if (binding.getNewAntecedent()!=null){ // fast track
		// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
		OWLFormula conclusionformula = binding.getNewAntecedent();
		// System.out.println("Rule 5 applying " + conclusionformula.prettyPrint());
		/*
		if (conclusionformula.toString().contains("Spatial")
				&& conclusionformula.toString().contains("131")
				&& conclusionformula.toString().contains("409")
				&& conclusionformula.toString().contains("419")
				)
			System.out.println(" Rule5 new applying " + conclusionformula);
			*/
		RuleApplicationResults result = new RuleApplicationResults();
		// if (sequent instanceof IncrementalSequent)
		// 	System.out.println("Rule 5 applying to incremental");
		// else System.out.println("Rule 5 applying to ordinary sequent");
		result.setOriginalFormula(sequent);
		result.addAddition("A1",conclusionformula);
		results.add(result);
		result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
		return results;
	} 
	return results;
}


public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
	List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
	List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
	return SequentList.makeANDSequentList(sequents);
}

@Override
public List<RuleKind> qualifyRule(){
	RuleKind[] a = {RuleKind.FORWARD};
	return Arrays.asList(a);
}

}, // END RULE5

// $7
RULE5{ //SubClass(X,Y) and SubClass(X,Z) --> Subclass(X,Y^Z)
	
	public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
		OWLFormula firstConjunct = conclusion.getArgs().get(1).getArgs().get(0);
		for (OWLFormula form : formulalist){
			if (form.getArgs().get(1).equals(firstConjunct))
				return form;
		}
		return null;
	}
	
	
	
	@Override
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule5";};
	@Override
	public java.lang.String getShortName(){return "R5";};
	
	private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
			OWLFormula.createFormulaVar("v1"), 
			OWLFormula.createFormulaVar("v2"));
	private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
			OWLFormula.createFormulaVar("v1"), 
			OWLFormula.createFormulaVar("v3"));
	private final OWLFormula concl1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
			OWLFormula.createFormulaVar("v1"), 
			OWLFormula.createFormula(OWLSymb.INT, 
					OWLFormula.createFormulaVar("v2"),
					OWLFormula.createFormulaVar("v3")));
	
	// key is the sequent ID, value is a list with pairs of potential premises
	private HashMap<Integer,List<Pair<Integer,Integer>>> premisesCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
	private HashMap<Integer,List<Pair<Integer,Integer>>> doneCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
	
	private int cheat1 = 0;
	
	private HashSet<Pair<Integer,Integer>> dontTryAgain = new HashSet <Pair<Integer,Integer>>();
	
	public List<Pair<Integer,Integer>> findPremFormulas(Sequent s){
		// System.out.println("Rule 5: find premises called!");
		Set <Pair<Integer,Integer>> resultset = new HashSet<Pair<Integer,Integer>>();
		List<Pair<Integer,Integer>> results = new ArrayList<Pair<Integer,Integer>>();
		List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
		// Collections.sort(candidates,s.formulaAscComparatorAnt());
		// System.out.println("candidates: "+ candidates);
		
		// Put candidates into bins with same first subformula
		HashMap<OWLFormula,Set<OWLFormula>> bins = new HashMap<OWLFormula,Set<OWLFormula>>();
		for (OWLFormula cands : candidates){
			Set<OWLFormula> res = bins.get(cands.getArgs().get(0));
			if (res!=null){
				res.add(cands.getArgs().get(1));
			}
			else {
				HashSet newset = new HashSet<OWLFormula>();
				newset.add(cands.getArgs().get(1));
			bins.put(cands.getArgs().get(0),newset);
			}
		}
		
		
		
		Set<OWLFormula> keys = bins.keySet();
		for (OWLFormula p1 : keys){
			List<OWLFormula> candidates2bis = new ArrayList<OWLFormula>();
				
			Set<OWLFormula> pool = bins.get(p1);
			for (OWLFormula p2 : pool){
				for (OWLFormula p3 : pool){
		
					
				// Skip if this was already tried.
				List<OWLFormula> forms = new ArrayList<OWLFormula>();
				forms.add(OWLFormula.createFormulaSubclassOf(p1,p2));
				forms.add(OWLFormula.createFormulaSubclassOf(p1,p3));
				if (AlreadyTriedCache.INSTANCE.wasTried(INLG2012NguyenEtAlRules.RULE5,
						forms)){
					// System.out.println("skipping");
					continue;
				}	
					
				// if (!candidates.get(i).getArgs().get(1).equals(form.getArgs().get(1))){
				if (!p2.equals(p3)){
				
					OWLFormula intersection = OWLFormula.createFormula(OWLSymb.INT,p2,p3);
					
					
					
					OWLFormula prem1 = OWLFormula.createFormulaSubclassOf(p1,p2);
					OWLFormula prem2 = OWLFormula.createFormulaSubclassOf(p1,p3);
					
					
					OWLFormula conclusion = intersection; // computeConclusionFormula(matcher2);
					// System.out.println("Rule 5: computed conclusion: " + VerbalisationManager.prettyPrint(conclusion));
					// System.out.println("subexpression exists? " + conclusion.getArgs().get(1));
					// System.out.println(s.subexprInSequent(conclusion.getArgs().get(1)));
					
					/*
					Set<OWLFormula> subexprs = s.getAllSubExprs();
					boolean isGood = false;
					for (OWLFormula expression : subexprs){
						if (expression.getHead().equals(OWLSymb.INT)){
							Set<OWLFormula> conjunct = expression.getConjuncts();
							int members = 0;
							for (OWLFormula c : conjunct){
								if (p2.equals(c)) members++;
								if (p3.equals(c)) members++;
							}
							if (members>1) isGood = true;
						}
					}
					*/
					
					if (s.subexprInSequent(intersection)){
					// if (s.subexprInSequent(conclusion.getArgs().get(1))){
					// if (isGood){
						// System.out.println("debug -- subexpression found!");
						
						// System.out.println("R5 get id1 " + s.antecedentFormulaGetID(prem1));
						// System.out.println("R5 get id2 " + s.antecedentFormulaGetID(prem2));
						
						resultset.add(new Pair<Integer,Integer>(
								s.antecedentFormulaGetID(prem1),
								s.antecedentFormulaGetID(prem2)
								));
					}
					else{
						AlreadyTriedCache.INSTANCE.setTried(INLG2012NguyenEtAlRules.RULE5,forms);
					}
					
				}
				}
			} // second loop

				
			
		}
		results.addAll(resultset);
		// System.out.println("DEBUG: results! " + results.size());
		return results;
	}
	
	
	
	public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
		List<RuleBinding> results = new ArrayList<RuleBinding>();
		List<Pair<Integer,Integer>> currentpairs = findPremFormulas(s);
		for (Pair<Integer,Integer> pair :currentpairs){
			OWLFormula formula1 = s.antecedentGetFormula(pair.t);
			OWLFormula formula2 = s.antecedentGetFormula(pair.u);
			OWLFormula p2= formula1.getArgs().get(1);
			OWLFormula p3= formula2.getArgs().get(1);
			
			
			try{
				
				OWLFormula intersection = OWLFormula.createFormula(OWLSymb.INT,formula1.getArgs().get(1),formula2.getArgs().get(1));
				
				if (p2.getHead().equals(OWLSymb.INT) && p3.getHead().equals(OWLSymb.INT)){
					List<OWLFormula> args = new ArrayList<OWLFormula>(p2.getArgs());
					args.addAll(p3.getArgs());
					intersection = OWLFormula.createFormula(OWLSymb.INT,args);
				}
				
				if (p2.getHead().equals(OWLSymb.INT) && !p3.getHead().equals(OWLSymb.INT)){
					List<OWLFormula> args = new ArrayList<OWLFormula>(p2.getArgs());
					args.add(p3);
					intersection = OWLFormula.createFormula(OWLSymb.INT,args);
				}
				
				if (p3.getHead().equals(OWLSymb.INT) && !p2.getHead().equals(OWLSymb.INT)){
					List<OWLFormula> args = new ArrayList<OWLFormula>(p3.getArgs());
					args.add(p2);
					intersection = OWLFormula.createFormula(OWLSymb.INT,args);
				}
				
				
				// List<Pair<OWLFormula,OWLFormula>> matcher2  = OWLFormula.getMatcher2Ary(formula1, formula2, prem1, prem2);
				// OWLFormula conclusion = computeConclusionFormula(matcher2);
				OWLFormula  conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, formula1.getArgs().get(0), intersection);
				// System.out.println("Rule 5 conclusion " + VerbalisationManager.prettyPrint(conclusion));
				boolean notcontained = true;
				if (s.alreadyContainedInAntecedent(conclusion))
					notcontained = false;
				if (s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(conclusion))
					notcontained = false; 
				if (notcontained){
				    // System.out.println("Rule 5 debug generated binding with conclusion " + conclusion);
					RuleBinding binding = new RuleBinding(conclusion,null);
					SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(formula1));
					SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(formula2));
					binding.insertPosition("A1", position1);
					binding.insertPosition("A2", position2);
					results.add(binding);
				}
			} catch (Exception e){
				}
			}
		// System.out.println("Rule 5 debug returned results : " + results);
		return results;
	}
	
	/*
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
		boolean exhaustive = false;
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		// fill cache
		List<Pair<Integer,Integer>> cache = new LinkedList<Pair<Integer,Integer>>();
		if (one_suffices.length>0 && one_suffices[0]==true){
			if (premisesCache.containsKey(s.getID())){
				cache = premisesCache.get(s.getID());
				if (cache.size()==0){
					exhaustive = true;
					List<Pair<Integer,Integer>> prems2 = findPremFormulas(s);
					List<Pair<Integer,Integer>> prems = new ArrayList<Pair<Integer,Integer>>(prems2);
					// System.out.println("Rule 5, prems to work on: " + prems.size());
					if (doneCache.containsKey(s.getID())){
						Timer.INSTANCE.start("rule5 - comp removal");
						prems.removeAll(doneCache.get(s.getID()));
						Timer.INSTANCE.stop("rule5 - comp removal");
						// System.out.println("Rule 5, prems to work on after removal: " + prems.size());
						if(prems.size()<=0){
							return results;
						}
					}
					cache.addAll(prems);
				}
			} else{
				cache = findPremFormulas(s);
				premisesCache.put(s.getID(), cache);
			}
		}
		List<Pair<Integer,Integer>> currentpairs;
		if (one_suffices.length>0 && one_suffices[0]==true){
			currentpairs = cache;
		} else{
			// System.out.println("explicitly looking for all premises, not satisficing!");
			currentpairs = findPremFormulas(s);
			
		}
		// System.out.println("currentpairs: " + currentpairs.size());
		while(currentpairs.size()>0){
			// System.out.println("currentpairs: " + currentpairs.size());
			if (premisesCache.containsKey(s.getID())){
				// System.out.println("Cache size: " +  premisesCache.get(s.getID()).size());
			}
		    Pair<Integer,Integer> pair = currentpairs.remove(0);
			OWLFormula formula1 = s.antecedentGetFormula(pair.t);
			OWLFormula formula2 = s.antecedentGetFormula(pair.u);
		    System.out.println("formula1 " + formula1 + " formula2 " + formula2);
			if (one_suffices.length>0 && one_suffices[0]==true){
			// check if the generated intersection already appears somewhere
			try{
				List<Pair<OWLFormula,OWLFormula>> matcher2  = OWLFormula.getMatcher2Ary(formula1, formula2, prem1, prem2);
				OWLFormula conclusion = computeConclusionFormula(matcher2);
				System.out.println("computed conclusion: " + conclusion);
				// if (!s.antecedentContainsOrDeeplyContains(conclusion.getArgs().get(1))){
				if (!s.subexprInSequent(conclusion.getArgs().get(1))){ // using the optimisation.
					if(!doneCache.containsKey(s.getID())){
						doneCache.put(s.getID(), new ArrayList<Pair<Integer,Integer>>());
					}
					doneCache.get(s.getID()).add(pair);
					// System.out.println("Rule 5 checking: intersection contained anywhere? " + conclusion.getArgs().get(1) + " " + s.antecedentContainsOrDeeplyContains(conclusion.getArgs().get(1)));
					break;
				}
			} catch(Exception e){}}
			
			
			Timer.INSTANCE.start("rule5 - do binding");
			if (!formula1.equals(formula2)){
				generateRuleBinding(s, formula1, formula2, results);
			}
			Timer.INSTANCE.stop("rule5 - do binding");
			if(!doneCache.containsKey(s.getID())){
				doneCache.put(s.getID(), new ArrayList<Pair<Integer,Integer>>());
			}
			doneCache.get(s.getID()).add(pair);
			// System.out.println("Rule 5, done cache size: " + doneCache.get(s.getID()).size());
			if (one_suffices.length>0 && one_suffices[0]==true && results.size()>1){
				// System.out.println("we are in satisficing mode");
				return results;
			}
		}
		if (one_suffices.length>0 && one_suffices[0]==true && results.size()==0 && !exhaustive){
			// empty cache to force it to be filled (avoid eternal loop)
			premisesCache.put(s.getID(), new LinkedList<Pair<Integer,Integer>>());
			return findRuleBindings(s,true);
		}
		return results;
	}
	*/
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s){
		return findRuleBindings(s,false);
	}
	
	public OWLFormula computeConclusionFormula(List<Pair<OWLFormula,OWLFormula>> matcher){
		OWLFormula formula3 = concl1.clone();
			return formula3.applyMatcher(matcher);
	}
	
	
	/* Old version that was sort of a local maximum wrt. speed
	
	public List<RuleBinding> findRuleBindings(Sequent s, boolean ... one_suffices){
		Timer.INSTANCE.start("rule5bindings");
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			
		List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
		Collections.sort(candidates,s.formulaAscComparatorAnt());
		
		
		
		// System.out.println("candidates: " + candidates);
		// check candidates for proper matching
		// for (int i = 0; i < candidates.size();i++){
		// 	for (int j = 0; j < candidates.size();j++){
		for (int i = 0 ; i < candidates.size(); i++){
			List<OWLFormula> candidates2 = new ArrayList<OWLFormula>();
			// get matcher (potential speed - up: do not compute candidates and matcher separately)
			try{
			List<Pair<OWLFormula,OWLFormula>> matcher = candidates.get(i).match(prem1);
			// System.out.println("prem 2 " + prem2 +  " Matcher: " + matcher);
			// now build new formula using the matcher
			OWLFormula prem_2 = prem2.applyMatcher(matcher);
			// System.out.println("prem_2 " + prem_2);
			// now search for this formula in sequent
			candidates2 = s.findMatchingFormulasInAntecedent(prem_2);
			}
			catch (Exception e){
				System.out.println(" Rule 5: this should not happen");
			}
			for (int j = 0 ; j < candidates2.size(); j++){
				if (!candidates.get(i).equals(candidates2.get(j))){
					Timer.INSTANCE.start("rule5hashchecking1");
					int iNumber = s.antecedentFormulaGetID(candidates.get(i));
					int jNumber = s.antecedentFormulaGetID(candidates2.get(j));
					 Pair<Integer,Integer> idpair = new Pair<Integer,Integer>(iNumber,
					 		jNumber);
					Timer.INSTANCE.stop("rule5hashchecking1");
					Timer.INSTANCE.start("rule5hashchecking2");
					// dontTryAgain.contains(idpair);
					// System.out.println("DONT TRY SIZE " + dontTryAgain.size());
					Timer.INSTANCE.stop("rule5hashchecking2");
					// System.out.println("don't try again: "+ dontTryAgain.contains(idpair));	
					// if(!dontTryAgain.contains(idpair)){
					// if (true){
					if(!candidates.get(i).equals(candidates2.get(j)) && !dontTryAgain.contains(idpair)){
						// System.out.println("working on " + candidates.get(i) + " and " + candidates.get(j));
							try{ // potential for improvement: complete the first matcher
								List<Pair<OWLFormula,OWLFormula>> matcher2  = OWLFormula.getMatcher2Ary(candidates.get(i), candidates2.get(j), prem1, prem2);
								// System.out.println("matcher: " + matcher);
								OWLFormula conclusion = computeConclusionFormula(matcher2);
								OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) conclusion.toOWLAPI();
								OWLObjectIntersectionOf ints = (OWLObjectIntersectionOf) subcl.getSuperClass();
					
								if (!s.alreadyContainedInAntecedent(conclusion)){
									boolean exists = false;
									List<Object> antecedent = s.getAntecedent();
									for(Object check_formula: antecedent){
										if (check_formula instanceof OWLSubClassOfAxiom 
									&& ((OWLSubClassOfAxiom) check_formula).getSuperClass() instanceof OWLObjectIntersectionOf										
									){
											List<OWLClassExpression> checkExprs = ((OWLObjectIntersectionOf) ((OWLSubClassOfAxiom) check_formula).getSuperClass()).getOperandsAsList();
											if (checkExprs.contains(ints.getOperandsAsList().get(0)) && checkExprs.contains(ints.getOperandsAsList().get(1)) 
													|| ints.getOperandsAsList().get(0).equals(((OWLSubClassOfAxiom) check_formula).getSuperClass())
													|| ints.getOperandsAsList().get(1).equals(((OWLSubClassOfAxiom) check_formula).getSuperClass())
													)
											{
												exists = true;
											}
										}
									}
								if (!exists){
									RuleBinding binding = new RuleBinding();
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidates.get(i)));
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidates2.get(j)));
									binding.insertPosition("A2", position2);
									results.add(binding);
									if (one_suffices.length>0 && one_suffices[0]==true){
										System.out.println("satisficing! :)");
										Timer.INSTANCE.stop("rule5bindings");
										cheat1 = i;
										return results;}
								} else {
								  dontTryAgain.add(idpair);
								}
								} // end if not already contained
								// System.out.println("add pair that we're not trying again");
								else {
									dontTryAgain.add(idpair);
								}
								 
							}
							catch (Exception e){
								// either way round, these two don't fit
								  dontTryAgain.add(idpair);
								  dontTryAgain.add(new Pair<Integer,Integer>(jNumber,
								 			iNumber));
								}
					}
				} 
			}
		} // end of loops
		cheat1 = 0;
		Timer.INSTANCE.stop("rule5bindings");
			return results;
	}
	
	*/
	
	@Override
	public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
		List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
		if (binding.getNewAntecedent()!=null){ // fast track
			// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
			OWLFormula conclusionformula = binding.getNewAntecedent();
			// System.out.println("Rule 5 applying " + conclusionformula);
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
		} 
		
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		OWLFormula formula1 = sequent.antecedentGetFormula(pos1.getToplevelPosition());
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		OWLFormula formula2 = sequent.antecedentGetFormula(pos2.getToplevelPosition());
		dontTryAgain.add(new Pair(pos1.getToplevelPosition(), pos2.getToplevelPosition()));
		List<Pair<OWLFormula,OWLFormula>> matcher = OWLFormula.getMatcher2Ary(formula1, formula2, prem1, prem2);
		OWLFormula conclusionformula = computeConclusionFormula(matcher);
		// System.out.println("Rule 5 is adding conclusion: " + conclusionformula);
	    // OWLSubClassOfAxiom axiom3 = (OWLSubClassOfAxiom) conclusionformula.toOWLAPI();
		RuleApplicationResults result = new RuleApplicationResults();
		result.setOriginalFormula(sequent);
		result.addAddition("A1",conclusionformula);
		results.add(result);
		result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
		return results;
	}
	
	
	
	@Override
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
		List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
		return SequentList.makeANDSequentList(sequents);
	}

	@Override
	public List<RuleKind> qualifyRule(){
		RuleKind[] a = {RuleKind.FORWARD};
		return Arrays.asList(a);
	}
	
}, // END RULE5


	
	
/*
RULE6{  // SubClass(T,X) -> SubCla(Y,X)
	
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule6";};
	public java.lang.String getShortName(){return "R6";};
	
	public List<RuleBinding> findRuleBindings(Sequent s){
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		if (s.isEmptyP()) return results;
		ArrayList succedent = s.getSuccedent();
		int count1 = 0;
		for (Object formula : succedent){
			if (formula instanceof OWLSubClassOfAxiom){
						RuleBinding binding = new RuleBinding();
						SequentPosition position1 = new SequentSinglePosition(SequentPart.SUCCEDENT, count1);
						binding.insertPosition("S1", position1);
						results.add(binding);
					}
			count1++;	
		} // end outer for loop
		return results;
	}
	
	
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("S1");
		Sequent s = sequent.clone();
		ArrayList succedent = s.getSuccedent();
		ArrayList newsequents = new ArrayList<Sequent>();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule6");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		Object formula1 = succedent.get(pos1.getToplevelPosition());
		if (formula1 instanceof OWLSubClassOfAxiom){
			OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();	
				OWLDataFactory dataFactory=manager.getOWLDataFactory();	
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(),axiom1.getSuperClass());
				if (!succedent.contains(axiom3)){
					s.addSuccedent(axiom3);	
				    newsequents.add(s);
				}
		}
		return SequentList.makeANDSequentList(newsequents);
	}
	
	public List<RuleKind> qualifyRule(){
		RuleKind[] a = {RuleKind.FORWARD};
		return Arrays.asList(a);
	}
	
}, // END RULE6
*/

// $8
RULE6neo{
	
	@Override
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule6neo";};
	@Override
	public java.lang.String getShortName(){return "R6n";};
	
	private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
			OWLFormula.createFormulaTop(),
			OWLFormula.createFormulaVar("v1"));
	

	@Override
	public List<RuleBinding> findRuleBindings(Sequent s){
		List<RuleBinding> results = new ArrayList<RuleBinding>();
		// System.out.println("Rule 6 neo DEBUG: find bindings called "
	    // );
		List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
		Set<OWLClassName> all_classnames = new HashSet<OWLClassName>();
		Set<OWLFormula> all_formulas = s.getAllAntecedentOWLFormulas();
		all_formulas.addAll(s.getAllSuccedentOWLFormulas());
		for (OWLFormula form : all_formulas){
			all_classnames.addAll(TermTree.decomposeClassNames(form));
		}
		// System.out.println("Rule 6 neo DEBUG: all classnames " + all_classnames);
		// Now check which possibilities are not covered yet.
		for (OWLFormula candidate : candidates){
			for (OWLClassName classname : all_classnames){
				OWLFormula conclusion = OWLFormula.createFormula(
						OWLSymb.SUBCL,
						new OWLFormula(classname),
						candidate.getArgs().get(1));
				// System.out.println("Rule 6 neo DEBUG: new statement " + conclusion);
				if (
					// filter out tautologies
					!conclusion.getArgs().get(0).equals(conclusion.getArgs().get(1))
					&&
					!s.alreadyContainedInAntecedent(conclusion)){
					RuleBinding binding = new RuleBinding(conclusion,null);
					SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate));
					binding.insertPosition("A1", position1);
					results.add(binding);
				}
			}
		}
		// System.out.println("Rule 6 neo DEBUG results " + results);
		return results;
	}
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s, boolean ... saturate) {
		return findRuleBindings(s);
	}
	
	@Override
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		
		List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
		List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
		// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
		return SequentList.makeANDSequentList(sequents);
	}
	
	
	@Override
	public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
		List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
		// OWLSubClassOfAxiom axiom3;
		OWLFormula conclusionformula = null;
		// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
		if (binding.getNewAntecedent()!=null){ // fast track
			// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
			conclusionformula = binding.getNewAntecedent();
		} 
		RuleApplicationResults result = new RuleApplicationResults();
		result.setOriginalFormula(sequent);
		result.addAddition("A1",conclusionformula);
		results.add(result);
		result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
		return results;
	}
	
	
}, // END RULE6new

/*
RULE7{ //SubClass(X,\exists r.T) and SubClass(X,\forall r.Y) --> Subclass(X,\exists r.Y)
	
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule7";};
	public java.lang.String getShortName(){return "R7";};
	
	public List<RuleBinding> findRuleBindings(Sequent s){
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		if (s.isEmptyP()) return results;
		ArrayList antecedent = s.getAntecedent();
		int count1 = 0;
		for (Object formula : antecedent){
			if (formula instanceof OWLSubClassOfAxiom //SubClass(X,\exists r.T) 
					&& ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectSomeValuesFrom
					&& ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula).getSuperClass()).getFiller().isOWLThing()){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
				// System.out.println(axiom1);
				// we have a subclass axiom, now need to find matching second subclass axiom
				int count2 = 0;
				for (Object formula2 : antecedent){
					if (formula2 instanceof OWLSubClassOfAxiom && count1 != count2
							&& ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLObjectAllValuesFrom
							&& ((OWLObjectAllValuesFrom) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getProperty().equals( ((OWLObjectSomeValuesFrom) (((OWLSubClassOfAxiom) formula).getSuperClass())).getProperty())
							){
							RuleBinding binding = new RuleBinding();
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
							binding.insertPosition("A1", position1);
							SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
							binding.insertPosition("A2", position2);
							results.add(binding);}						
					count2++;
					} //end inner loop	
			}
			count1++;	
		} // end outer for loop
		return results;
	}
	
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		Sequent s = sequent.clone();
		ArrayList antecedent = s.getAntecedent();
		ArrayList newsequents = new ArrayList<Sequent>();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		Object formula1 = antecedent.get(pos1.getToplevelPosition());
		Object formula2 = antecedent.get(pos2.getToplevelPosition());
		if (formula1 instanceof OWLSubClassOfAxiom 
				&& formula2 instanceof OWLSubClassOfAxiom
				&& (((OWLSubClassOfAxiom) formula1).getSuperClass()) instanceof OWLObjectSomeValuesFrom
				&& (((OWLSubClassOfAxiom) formula2).getSuperClass()) instanceof OWLObjectAllValuesFrom
				&& ((OWLObjectAllValuesFrom) (((OWLSubClassOfAxiom) formula2).getSuperClass())).getProperty().equals( ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula1).getSuperClass()).getProperty() )
				){
			OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
			OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
			OWLObjectPropertyExpression property = ((OWLObjectSomeValuesFrom) axiom1.getSuperClass()).getProperty();
			OWLClassExpression yclass = ((OWLObjectAllValuesFrom) axiom2.getSuperClass()).getFiller();
			// construct new term
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLObjectSomeValuesFrom(property,yclass));
			if (!antecedent.contains(axiom3)){
				s.addAntecedent(axiom3);	
			    newsequents.add(s);
			}
		}
		return SequentList.makeANDSequentList(newsequents);
	}
	
	public List<RuleKind> qualify(){
		RuleKind[] a = {RuleKind.FORWARD};
		return Arrays.asList(a);
	}
	
}, // END RULE7
*/

/*
RULE8{ // ObjectPropertyRange(r,Y) and SubClass(X,Y) --> ObjectPropertyRange(r,Y)
	
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule8";};
	public java.lang.String getShortName(){return "R8";};
	
	public List<RuleBinding> findRuleBindings(Sequent s){
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		if (s.isEmptyP()) return results;
		ArrayList antecedent = s.getAntecedent();
		int count1 = 0;
		for (Object formula : antecedent){
			if (formula instanceof OWLObjectPropertyRangeAxiom // ObjectPropertyRange(r,Y)
					){ 
				OWLObjectPropertyRangeAxiom axiom1 = ((OWLObjectPropertyRangeAxiom) formula);
				// System.out.println(axiom1);
				// we have a property range axiom
				int count2 = 0;
				for (Object formula2 : antecedent){
					if (formula2 instanceof OWLSubClassOfAxiom 
							&& ((OWLSubClassOfAxiom) formula2).getSubClass().equals(axiom1.getRange())
							){
							RuleBinding binding = new RuleBinding();
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
							binding.insertPosition("A1", position1);
							SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
							binding.insertPosition("A2", position2);
							results.add(binding);}						
					count2++;
					} //end inner loop	
			}
			count1++;	
		} // end outer for loop
		return results;
	}
	
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		Sequent s = sequent.clone();
		ArrayList antecedent = s.getAntecedent();
		ArrayList newsequents = new ArrayList<Sequent>();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		Object formula1 = antecedent.get(pos1.getToplevelPosition());
		Object formula2 = antecedent.get(pos2.getToplevelPosition());
		if (formula1 instanceof OWLObjectPropertyRangeAxiom 
				&& formula2 instanceof OWLSubClassOfAxiom
				&& ((OWLSubClassOfAxiom) formula2).getSubClass().equals( ((OWLObjectPropertyRangeAxiom) formula1).getRange())
				){
			OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula1;
			OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
			OWLObjectPropertyExpression property = axiom1.getProperty();
			OWLClassExpression yclass = axiom2.getSuperClass();
			// construct new term
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			OWLObjectPropertyRangeAxiom axiom3 = dataFactory.getOWLObjectPropertyRangeAxiom(property,yclass);
			if (!antecedent.contains(axiom3)){
				s.addAntecedent(axiom3);	
			    newsequents.add(s);
			}
		}
		return SequentList.makeANDSequentList(newsequents);
	}
	
	public List<RuleKind> qualifyRule(){
		RuleKind[] a = {RuleKind.FORWARD};
		return Arrays.asList(a);
	}
	
}, // END RULE8
*/

/*
RULE9{ // ObjePropDomain(r,X), and SubClass(Y, \exists (r,Z)) --> SubClass(Y,X)
	
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule9";};
	public java.lang.String getShortName(){return "R9";};
	
	public List<RuleBinding> findRuleBindings(Sequent s){
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		if (s.isEmptyP()) return results;
		ArrayList antecedent = s.getAntecedent();
		int count1 = 0;
		for (Object formula : antecedent){
			if (formula instanceof OWLObjectPropertyDomainAxiom // ObjectPropertyDomain(r,X)
					){ 
				OWLObjectPropertyDomainAxiom axiom1 = ((OWLObjectPropertyDomainAxiom) formula);
				// System.out.println(axiom1);
				// we have a subclass axiom, now need to find matching second subclass axiom
				int count2 = 0;
				for (Object formula2 : antecedent){
					if (formula2 instanceof OWLSubClassOfAxiom 
						    && ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLObjectSomeValuesFrom
							&& ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getProperty().equals(axiom1.getProperty()) 
							){
							RuleBinding binding = new RuleBinding();
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
							binding.insertPosition("A1", position1);
							SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
							binding.insertPosition("A2", position2);
							results.add(binding);}						
					count2++;
					} //end inner loop	
			}
			count1++;	
		} // end outer for loop
		return results;
	}
	
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		Sequent s = sequent.clone();
		ArrayList antecedent = s.getAntecedent();
		ArrayList newsequents = new ArrayList<Sequent>();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule9");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule9");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		Object formula1 = antecedent.get(pos1.getToplevelPosition());
		Object formula2 = antecedent.get(pos2.getToplevelPosition());
		if (formula1 instanceof OWLObjectPropertyDomainAxiom 
				&& formula2 instanceof OWLSubClassOfAxiom
				&& ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLObjectSomeValuesFrom
				){
			OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
			OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
			OWLObjectSomeValuesFrom axiom2a =  (OWLObjectSomeValuesFrom) axiom2.getSuperClass(); 
			// construct new term
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom2.getSubClass(),axiom1.getDomain());
			if (!antecedent.contains(axiom3)){
				s.addAntecedent(axiom3);	
			    newsequents.add(s);
			}
		}
		return SequentList.makeANDSequentList(newsequents);
	}
	
	public List<RuleKind> qualifyRule(){
		RuleKind[] a = {RuleKind.FORWARD};
		return Arrays.asList(a);
	}
	
}, // END RULE9
*/
/*
RULE10{ // EqCla(X,Union(Y,Z,...)) --> SubClass(Y,X)
	
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule10";};
	public java.lang.String getShortName(){return "R10";};
	
	public List<RuleBinding> findRuleBindings(Sequent s){
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		if (s.isEmptyP()) return results;
		ArrayList antecedent = s.getAntecedent();
		int count1 = 0;
		for (Object formula : antecedent){
			if (formula instanceof OWLEquivalentClassesAxiom){
				OWLEquivalentClassesAxiom axiom = (OWLEquivalentClassesAxiom) formula;
				List<OWLClassExpression> expressions = axiom.getClassExpressionsAsList();
				int count2 = 0;
				for (Object expr1 : expressions){
					int count3 = 0;
					for (Object expr2 : expressions){
						OWLClassExpression exp1 = (OWLClassExpression) expr1;
						OWLClassExpression exp2 = (OWLClassExpression) expr2;
						if (!exp1.equals(exp2) && exp2 instanceof OWLObjectUnionOf){
							OWLObjectUnionOf union = (OWLObjectUnionOf) exp2;
							List<OWLClassExpression> unionlist = union.getOperandsAsList();
							for (int i=0; i < unionlist.size() ; i++){
								RuleBinding binding = new RuleBinding();
								// position of X
								List<Integer> listX = new ArrayList<Integer>();
								listX.add(count1);
								listX.add(count2);
								SequentPosition positionX = new SequentSinglePosition(SequentPart.ANTECEDENT, listX);
								binding.insertPosition("A1", positionX);
								// position of Y
								List<Integer> listY = new ArrayList<Integer>();
								listY.add(count1);
								listY.add(count3);
								listY.add(i);
								SequentPosition positionY = new SequentSinglePosition(SequentPart.ANTECEDENT, listY);
								binding.insertPosition("A2", positionY);
								results.add(binding);
							} // end for(i)
						} //endif
						count3++;
					} //end loop expr2/count3
					count2++;
				}// end inner for loop
			} //endif
			count1++;
		} // end outer loop/count1
		return results;
	}
	
	
	
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		Sequent s = sequent.clone();
		ArrayList antecedent = s.getAntecedent();
		ArrayList newsequents = new ArrayList<Sequent>();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule10");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule10");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		Object formula1 = antecedent.get(pos1.getToplevelPosition());
		List<Integer> positions1 = pos1.getPosition();
		List<Integer> positions2 = pos2.getPosition();
		if (formula1 instanceof OWLEquivalentClassesAxiom
				&& ((OWLEquivalentClassesAxiom) formula1).getClassExpressionsAsList().get(positions2.get(1)) instanceof OWLObjectUnionOf
				){
			OWLEquivalentClassesAxiom axiom1 = ((OWLEquivalentClassesAxiom) formula1);
			OWLClassExpression expr1 = axiom1.getClassExpressionsAsList().get(positions1.get(1));
			OWLObjectUnionOf unionexpr = (OWLObjectUnionOf) axiom1.getClassExpressionsAsList().get(positions2.get(1));
			OWLClassExpression expr2 = unionexpr.getOperandsAsList().get(positions2.get(2));
			// construct new term
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(expr2,expr1);
			if (!antecedent.contains(axiom3)){
				s.addAntecedent(axiom3);	
			    newsequents.add(s);
			}
		}
		return SequentList.makeANDSequentList(newsequents);
	}
	
	
}, // END RULE10
*/

/*
RULE11{ //SubCla(X,\exists r. Y) and SubCla((>1,r,Y),Z) --> SubCla(X,Z)
	
	public java.lang.String getName(){return "INLG2012NguyenEtAlRule11";};
	public java.lang.String getShortName(){return "R11";};
	
	public List<RuleBinding> findRuleBindings(Sequent s){
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		if (s.isEmptyP()) return results;
		ArrayList antecedent = s.getAntecedent();
		int count1 = 0;
		for (Object formula : antecedent){
			if (formula instanceof OWLSubClassOfAxiom //SubClass(X,\exists r.T) 
					&& ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectSomeValuesFrom
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
				System.out.println(axiom1);
				// we have a subclass axiom, now need to find matching second subclass axiom
				int count2 = 0;
				for (Object formula2 : antecedent){
					if (formula2 instanceof OWLSubClassOfAxiom && count1 != count2
							&& ((OWLSubClassOfAxiom) formula2).getSubClass() instanceof OWLObjectMinCardinality
							&& ((OWLObjectMinCardinality) ((OWLSubClassOfAxiom) formula2).getSubClass()).getProperty().equals( ((OWLObjectSomeValuesFrom) (((OWLSubClassOfAxiom) formula).getSuperClass())).getProperty())
							&& ((OWLObjectMinCardinality) ((OWLSubClassOfAxiom) formula2).getSubClass()).getFiller().equals( ((OWLObjectSomeValuesFrom) (((OWLSubClassOfAxiom) formula).getSuperClass())).getFiller())
							){
							RuleBinding binding = new RuleBinding();
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
							binding.insertPosition("A1", position1);
							SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
							binding.insertPosition("A2", position2);
							results.add(binding);}						
					count2++;
					} //end inner loop	
			}
			count1++;	
		} // end outer for loop
		return results;
	}
	
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		Sequent s = sequent.clone();
		ArrayList antecedent = s.getAntecedent();
		ArrayList newsequents = new ArrayList<Sequent>();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule5");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		Object formula1 = antecedent.get(pos1.getToplevelPosition());
		Object formula2 = antecedent.get(pos2.getToplevelPosition());
		// System.out.println(pos1.getToplevelPosition());
		// System.out.println(pos2.getToplevelPosition());
		if (formula1 instanceof OWLSubClassOfAxiom 
				&& formula2 instanceof OWLSubClassOfAxiom
				&& (((OWLSubClassOfAxiom) formula1).getSuperClass()) instanceof OWLObjectSomeValuesFrom
				&& (((OWLSubClassOfAxiom) formula2).getSubClass()) instanceof OWLObjectMinCardinality
				&& ((OWLObjectMinCardinality) (((OWLSubClassOfAxiom) formula2).getSubClass())).getProperty().equals( ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula1).getSuperClass()).getProperty() )
				&& ((OWLObjectMinCardinality) (((OWLSubClassOfAxiom) formula2).getSubClass())).getFiller().equals( ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula1).getSuperClass()).getFiller() )
				){
			OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
			OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
			OWLClassExpression xclass = axiom1.getSubClass();
			OWLClassExpression zclass = axiom2.getSuperClass();
			// construct new term
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(xclass,zclass);
			if (!antecedent.contains(axiom3)){
				s.addAntecedent(axiom3);	
			    newsequents.add(s);
			}
		}
		return SequentList.makeANDSequentList(newsequents);
	}
	
	
}, // END RULE11
*/

	// $9
	RULE12{
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule12";};
		@Override
		public java.lang.String getShortName(){return "R12";};
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormulaVar("v2"));
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v2"), 
				OWLFormula.createFormulaVar("v3"));
		private final OWLFormula concl1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormulaVar("v3"));
		
		// key is the sequent ID, value is a list with pairs of potential premises
		private HashMap<Integer,List<Pair<Integer,Integer>>> premisesCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
		private HashMap<Integer,List<Pair<Integer,Integer>>> doneCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
		// done: sequent id and list of pairs that has been worked on already
		
		public void clearCaches(){
		 	premisesCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
		 	doneCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
		}
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
			// System.out.println("Rule 12: find bindings called; suffices "+ one_suffices);
			boolean exhaustive = false;
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			// fill cache
			List<Pair<Integer,Integer>> cache = new LinkedList<Pair<Integer,Integer>>();
			if (one_suffices.length>0 && one_suffices[0]==true){
				//System.out.println("here");
				if (premisesCache.containsKey(s.getID())){
					cache = premisesCache.get(s.getID());
					if (cache.size()==0){
						exhaustive = true;
						List<Pair<Integer,Integer>> prems = findPremFormulas(s);
						if (doneCache.containsKey(s.getID())){
							prems.removeAll(doneCache.get(s.getID()));
						}
						// System.out.println("adding " + prems.size() + " prems to cache");
						cache.addAll(prems);
						if(prems.size()<=0){
							return results;
						}
					}
				} else{
					cache = findPremFormulas(s);
					// System.out.println("filled " + cache.size());
					premisesCache.put(s.getID(), cache);
				}
			}
			List<Pair<Integer,Integer>> currentpairs;
			if (one_suffices.length>0 && one_suffices[0]==true){
				// System.out.println("case 1");
				currentpairs = cache;
			} else{
				// System.out.println("case 2");
				currentpairs = findPremFormulas(s);
				// System.out.println("current pairs " + currentpairs.size());
			}
			// System.out.println("Rule 12: Current pairs: " + currentpairs);
			while(currentpairs.size()>0){
				// System.out.println("Rule 12 -- checkpoint 1 " + results.size() + " bindings");
			    Pair<Integer,Integer> pair = currentpairs.remove(0);
			    // System.out.println("currentpair " + pair + " " + s.antecedentGetFormula(pair.t) + "  " + s.antecedentGetFormula(pair.u));
			    
			    if (one_suffices.length>0 && one_suffices[0]==true){
			    	if (pair.t>s.getHighestInitAxiomid()){
			    		// System.out.println("Rule 12: discarded pair! (SATURATION MODE) ")
			    		
			    		if(!doneCache.containsKey(s.getID())){
							doneCache.put(s.getID(), new LinkedList<Pair<Integer,Integer>>());
						}
						
			    		break;
			    	}
			    }
				OWLFormula formula1 = s.antecedentGetFormula(pair.t);
				OWLFormula formula2 = s.antecedentGetFormula(pair.u);
				// System.out.println("Rule 12: working on formulas " + formula1 + " AND " + formula2);
				if (!formula1.equals(formula2)){
					generateRuleBinding(s, formula1, formula2, results);
				}
				if(!doneCache.containsKey(s.getID())){
					doneCache.put(s.getID(), new LinkedList<Pair<Integer,Integer>>());
				}
				doneCache.get(s.getID()).add(pair);
				// System.out.println("Rule 12, done cache size: " + doneCache.get(s.getID()).size());
				if (one_suffices.length>0 && one_suffices[0]==true && results.size()>0){
					// System.out.println("Rule 12 -- returning " + results.size() + " bindings");
					return results;
				}
			}
			if (one_suffices.length>0 && one_suffices[0]==true && results.size()==0 && !exhaustive){
				// empty cache to force it to be filled (avoid eternal loop)
				premisesCache.put(s.getID(), new LinkedList<Pair<Integer,Integer>>());
				// System.out.println("Rule 12 -- returning " + results.size() + " bindings");
				return findRuleBindings(s,true);
			}
			// System.out.println("Rule 12 -- returning " + results.size() + " bindings");
			return results;
		}
		
		public List<Pair<Integer,Integer>> findPremFormulas(Sequent s){
			List<Pair<Integer,Integer>> results = new LinkedList<Pair<Integer,Integer>>();
			List<OWLFormula> candidates_pre = s.findMatchingFormulasInAntecedent(prem1);
			// Set<OWLFormula> candidatesSet = new HashSet<OWLFormula>(candidates_pre);
			Set<OWLFormula> candidatesSet = new HashSet<OWLFormula>();
			for(OWLFormula form : candidates_pre){
				if (!form.getArgs().get(0).equals(form.getArgs().get(1)))
					candidatesSet.add(form);
			}
			// System.out.println("candidates set " + candidatesSet.size());
			// List<OWLFormula> candidates = new LinkedList(candidatesSet);
			// Collections.sort(candidates,s.formulaAscComparatorAnt());
			// System.out.println("candidates " + candidates);
			// for (int i = 0 ; i < candidate.size(); i++){
			for (OWLFormula cand : candidatesSet){
				List<OWLFormula> candidates2 = new LinkedList<OWLFormula>();
				// get matcher (potential speed - up: do not compute candidates and matcher separately)
				try{
				List<Pair<OWLFormula,OWLFormula>> matcher = cand.match(prem1);
				// System.out.println("candidate " + candidates.get(i) + "prem 2 " + prem2 +  " Matcher: " + matcher);
				// now build new formula using the matcher
				OWLFormula prem_2 = prem2.applyMatcher(matcher);
				// System.out.println("prem_2 " + prem_2);
				// now search for this formula in sequent
				candidates2 = s.findMatchingFormulasInAntecedent(prem_2);
				// System.out.println("candidates2 " + candidates2);
				}
				catch (Exception e){
					System.out.println(" Rule 12: this should not happen");
				}
				for (int j = 0 ; j < candidates2.size(); j++){
					OWLFormula cand1 = cand;
					OWLFormula cand2 = candidates2.get(j);
					//
					if (!cand1.equals(cand2)
							&& // disallow tautologies!!!
								!cand1.getArgs().get(0).equals(cand2.getArgs().get(1))
								// or cases where where nothing new comes out!
								&& !cand2.getArgs().get(1).equals(cand2.getArgs().get(0)) // the first case is treated above
							){
						results.add(new Pair<Integer,Integer>(
								s.antecedentFormulaGetID(cand1),
								s.antecedentFormulaGetID(cand2)
								));
					}
				}
			}
			return results;
		}
		
		public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
			OWLFormula p1 = null;
			for (OWLFormula f : formulalist)
				if (f.getArgs().get(0).equals(conclusion.getArgs().get(0)))
					p1 = f;
			return p1;
		}
		
		public OWLFormula getP2(List<OWLFormula> formulalist, OWLFormula conclusion){
			OWLFormula p2 = null;
			for (OWLFormula f : formulalist)
				if (f.getArgs().get(1).equals(conclusion.getArgs().get(1)))
					p2 = f;
			return p2;
		}
		
		public void generateRuleBinding(Sequent s, OWLFormula candidate1, OWLFormula candidate2, ArrayList<RuleBinding> results){
			try{
				// List<Pair<OWLFormula,OWLFormula>> matcher2  = OWLFormula.getMatcher2Ary(candidate1, candidate2,prem1, prem2);
				// System.out.println("COMPUTED MATCHER: " + matcher2);
				// OWLFormula conclusion = computeConclusionFormula(matcher2);
				// System.out.println("Rule 12 : conclusion " + conclusion);
				OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,candidate1.getArgs().get(0),candidate2.getArgs().get(1));
				boolean notcontained = true;
				if (s.alreadyContainedInAntecedent(conclusion) || s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(conclusion))
					notcontained = false; 
				if (notcontained){
					RuleBinding binding = new RuleBinding(conclusion,null);
					SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate1));
					binding.insertPosition("A1", position1);
					SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate2));
					binding.insertPosition("A2", position2);
					results.add(binding);
				}
				else{
					List<OWLFormula> forms = new ArrayList<OWLFormula>();
					forms.add(candidate1);
					forms.add(candidate2);
					AlreadyTriedCache.INSTANCE.setTried(this,forms);
				}
			}
			catch (Exception e){
			}
		}
		
		private HashSet<Pair<Integer,Integer>> dontTryAgain = new HashSet <Pair<Integer,Integer>>();
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			return findRuleBindings(s,false);
		}
		
		public List<Pair<OWLFormula,OWLFormula>> getMatcher(OWLFormula candidate1, OWLFormula candidate2) throws Exception{
			List<OWLFormula> pattern = new ArrayList<OWLFormula>();
			pattern.add(prem1);
			pattern.add(prem2);
			List<OWLFormula> pattern2 = new ArrayList<OWLFormula>();
			pattern2.add(candidate1);
			pattern2.add(candidate2);
			List<Pair<OWLFormula,OWLFormula>> matcher = OWLFormula.match(pattern2, pattern);
			// System.out.println("matcher: " + matcher);
			return matcher;
		}
		
		public OWLFormula computeConclusionFormula(List<Pair<OWLFormula,OWLFormula>> matcher){
			// System.out.println("called compute conclusion formula");
			OWLFormula formula3 = concl1.clone();
				return formula3.applyMatcher(matcher);
		}
		
		
		
		
		
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula;
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
				// System.out.println("Rule 12 DEBUG, computed conclusion: " + conclusionformula);
			} else{
			
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			OWLFormula formula1 = sequent.antecedentGetFormula(pos1.getToplevelPosition());
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			OWLFormula formula2 = sequent.antecedentGetFormula(pos2.getToplevelPosition());
			List<Pair<OWLFormula,OWLFormula>> matcher = getMatcher(formula1, formula2);
			conclusionformula = computeConclusionFormula(matcher);
			
			if (conclusionformula.getArgs().get(0).equals(conclusionformula.getArgs().get(1))) throw new RuntimeException();	
			
			}
			
	
			
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
		}
		
		
	}, // END RULE12
	
	
	// $10
		RULE12new{
			
			@Override
			public java.lang.String getName(){return "INLG2012NguyenEtAlRule12";};
			@Override
			public java.lang.String getShortName(){return "R12";};
			
			private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
					OWLFormula.createFormulaVar("v1"), 
					OWLFormula.createFormulaVar("v2"));
			private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
					OWLFormula.createFormulaVar("v2"), 
					OWLFormula.createFormulaVar("v3"));
			
			
			@Override
			public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
				for (OWLFormula form : formulalist){
					if (form.getArgs().get(0).equals(conclusion.getArgs().get(0)))
						return form;
				}
				return null;
			}
			
			@Override
			public OWLFormula getP2(List<OWLFormula> formulalist, OWLFormula conclusion){
				for (OWLFormula form : formulalist){
					if (form.getArgs().get(1).equals(conclusion.getArgs().get(1)))
						return form;
				}
				return null;
			}
			
			public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
				ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
				List<OWLFormula> candidates1 = s.findMatchingFormulasInAntecedent(prem1);
				// Find candidates for second formulas, sort them into bins 
				HashMap<OWLFormula,List<OWLFormula>> bins = new HashMap<OWLFormula,List<OWLFormula>>();
						for (OWLFormula cands : candidates1){
							List<OWLFormula> res = bins.get(cands.getArgs().get(1));
							if (res==null){
								List<OWLFormula> candidates2 = new LinkedList<OWLFormula>();
								// get matcher (potential speed - up: do not compute candidates and matcher separately)
								
								try{
									List<Pair<OWLFormula,OWLFormula>> matcher = cands.match(prem1);
									OWLFormula prem_2 = prem2.applyMatcher(matcher);
									candidates2 = s.findMatchingFormulasInAntecedent(prem_2);	
								}
								catch (Exception e){
									System.out.println(" Rule 12: this should not happen");
								} // end try-catch
								// System.out.println(cands + " matched with " + candidates2);
							bins.put(cands.getArgs().get(1),candidates2);
							} // end if
						}
						for (OWLFormula cand1 : candidates1){
							if (cand1.getArgs().get(0).equals(cand1.getArgs().get(1)))
								continue;
							List<OWLFormula> candidates2 = bins.get(cand1.getArgs().get(1));
							for (OWLFormula cand2 : candidates2){
								if (cand2.getArgs().get(0).equals(cand2.getArgs().get(1)))
									continue;
								if (cand1.getArgs().get(0).equals(cand2.getArgs().get(1)))
									continue;
								List<OWLFormula> forms = new ArrayList<OWLFormula>();
								forms.add(cand1);
								forms.add(cand2);
								if (AlreadyTriedCache.INSTANCE.wasTried(this,forms))
									continue;
								OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,cand1.getArgs().get(0),cand2.getArgs().get(1));
								boolean notcontained = true;
								if (s.alreadyContainedInAntecedent(conclusion) || s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(conclusion))
									notcontained = false; 
								if (notcontained){
									RuleBinding binding = new RuleBinding(conclusion,null);
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand1));
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand2));
									binding.insertPosition("A2", position2);
									results.add(binding);
								}
								else{
									AlreadyTriedCache.INSTANCE.setTried(this,forms);
								}
							} // end loop cand2	
								
						} // end loop cand1
					
					return results;
				}
			
			/*
			@Override
			public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
				ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
				List<OWLFormula> candidates1 = s.findMatchingFormulasInAntecedent(prem1);
				for (OWLFormula cand1 : candidates1){
					if (cand1.getArgs().get(0).equals(cand1.getArgs().get(1)))
						continue;
					List<OWLFormula> candidates2 = new LinkedList<OWLFormula>();
					// get matcher (potential speed - up: do not compute candidates and matcher separately)
					
					try{
						List<Pair<OWLFormula,OWLFormula>> matcher = cand1.match(prem1);
						OWLFormula prem_2 = prem2.applyMatcher(matcher);
						candidates2 = s.findMatchingFormulasInAntecedent(prem_2);	
					}
					catch (Exception e){
						System.out.println(" Rule 12: this should not happen");
					}
					
					// OWLFormula prem_2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
					// 		cand1.getArgs().get(1),
					// 		OWLFormula.createFormulaVar("v3"));
					// candidates2 = s.findMatchingFormulasInAntecedent(prem_2);	
				   
					
					for (OWLFormula cand2 : candidates2){
						if (cand2.getArgs().get(0).equals(cand2.getArgs().get(1)))
							continue;
						if (cand1.getArgs().get(0).equals(cand2.getArgs().get(1)))
							continue;
						List<OWLFormula> forms = new ArrayList<OWLFormula>();
						forms.add(cand1);
						forms.add(cand2);
						if (AlreadyTriedCache.INSTANCE.wasTried(this,forms))
							continue;
						OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,cand1.getArgs().get(0),cand2.getArgs().get(1));
						boolean notcontained = true;
						if (s.alreadyContainedInAntecedent(conclusion) || s instanceof IncrementalSequent && ((IncrementalSequent) s).getMasterSequent().alreadyContainedInAntecedent(conclusion))
							notcontained = false; 
						if (notcontained){
							RuleBinding binding = new RuleBinding(conclusion,null);
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand1));
							binding.insertPosition("A1", position1);
							SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand2));
							binding.insertPosition("A2", position2);
							results.add(binding);
						}
						else{
							AlreadyTriedCache.INSTANCE.setTried(this,forms);
						}
					} // end loop cand2	
						
				} // end loop cand1
			
			return results;
		}
		*/	
			
			@Override
			public List<RuleBinding> findRuleBindings(Sequent s){
				return findRuleBindings(s,false);
			}
			
				
			
			@Override
			public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
				List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
				// System.out.println("Rule12new breakpoint (1)");
				List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
				// System.out.println("Rule12new breakpoint (2)");
				return SequentList.makeANDSequentList(sequents);
			}
			
			
			@Override
			public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
				// System.out.println("Rule12new computeRuleApplicationResults called");
				List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
				
				OWLFormula conclusionformula = null;
				if (binding.getNewAntecedent()!=null){ // fast track		
					conclusionformula = binding.getNewAntecedent();
				} 
					
				RuleApplicationResults result = new RuleApplicationResults();
				result.setOriginalFormula(sequent);
				result.addAddition("A1",conclusionformula);
				results.add(result);
				result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
				// System.out.println("Rule12new computeRuleApplicationResults done");
				return results;
			}
			
			
		}, // END RULE12
	
	
	/*
	RULE12thing{
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule12thing";};
		public java.lang.String getShortName(){return "12t";};
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaTop(),
				OWLFormula.createFormulaVar("v1"));
		

		public List<RuleBinding> findRuleBindings(Sequent s){
			List<RuleBinding> results = new ArrayList<RuleBinding>();
			// System.out.println("Rule 6 neo DEBUG: find bindings called "
		    // );
			List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
			Set<OWLClassName> all_classnames = new HashSet<OWLClassName>();
			Set<OWLFormula> all_formulas = s.getAllAntecedentOWLFormulas();
			all_formulas.addAll(s.getAllSuccedentOWLFormulas());
			for (OWLFormula form : all_formulas){
				all_classnames.addAll(TermTree.decomposeClassNames(form));
			}
			// System.out.println("Rule 6 neo DEBUG: all classnames " + all_classnames);
			// Now check which possibilities are not covered yet.
			for (OWLFormula candidate : candidates){
				for (OWLClassName classname : all_classnames){
					OWLFormula conclusion = OWLFormula.createFormula(
							OWLSymb.SUBCL,
							new OWLFormula(classname),
							candidate.getArgs().get(1));
					// System.out.println("Rule 6 neo DEBUG: new statement " + conclusion);
					if (
						// filter out tautologies
						!conclusion.getArgs().get(0).equals(conclusion.getArgs().get(1))
						&&
						!s.alreadyContainedInAntecedent(conclusion)){
						RuleBinding binding = new RuleBinding(conclusion,null);
						SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate));
						binding.insertPosition("A1", position1);
						results.add(binding);
					}
				}
			}
			// System.out.println("Rule 6 neo DEBUG results " + results);
			return results;
		}
		
		public List<RuleBinding> findRuleBindings(Sequent s, boolean ... saturate) {
			return findRuleBindings(s);
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
		}
		
		
	}, // END RULE12thing
	*/
	
	/*
RULE12OWL{
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule12";};
		public java.lang.String getShortName(){return "R12";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			return findRuleBindings(s,false);
		}
		
		public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
			
			
	
			
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom){
							OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
							if (axiom1.getSuperClass().equals(axiom2.getSubClass())){
								// need to make sure the generated axiom is not there already
								
								// Build the new object
								OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
								OWLDataFactory dataFactory=manager.getOWLDataFactory();
								OWLSubClassOfAxiom newaxiom = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),axiom2.getSuperClass());
								if (!s.alreadyContainedInAntecedent(newaxiom)){
									RuleBinding binding = new RuleBinding();
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
									binding.insertPosition("A2", position2);
									results.add(binding);
									if (one_suffices.length>0 && one_suffices[0]==true){return results;}
									}
							}
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = sequent.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom && formula2 instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),axiom2.getSuperClass());
				RuleApplicationResults result = new RuleApplicationResults();
				result.setOriginalFormula(s);
				result.addAddition("A1",axiom3);
				results.add(result);
		}
			return results;
		}
		
		
	}, // END RULE12
	
*/
	
	/*
RULE13{  // SubClaOf(X, ObjeComplOf(X)) --> SubClaOf(X,\bot) 
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule13";};
		public java.lang.String getShortName(){return "R13";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					OWLClassExpression superCl = axiom1.getSuperClass();
					OWLClassExpression subCl = axiom1.getSubClass();
					if (superCl instanceof OWLObjectComplementOf){
						if (subCl.getObjectComplementOf().equals(superCl)){
							RuleBinding binding = new RuleBinding();
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
							binding.insertPosition("A1", position1);
							results.add(binding);
						}
					}
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				Object formula2 = axiom1.getSuperClass();
				if (formula2 instanceof OWLObjectComplementOf && axiom1.getSubClass().getObjectComplementOf().equals(formula2)){
					OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
					OWLDataFactory dataFactory=manager.getOWLDataFactory();
					OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLNothing());
					if (!antecedent.contains(axiom3)){
						s.addAntecedent(axiom3);	
					    newsequents.add(s);
					}
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE13
	*/
	
	/*
RULE14{
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule14";};
		public java.lang.String getShortName(){return "R14";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubObjectPropertyOfAxiom){
					OWLSubObjectPropertyOfAxiom axiom1 = (OWLSubObjectPropertyOfAxiom) formula;
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubObjectPropertyOfAxiom){
							OWLSubObjectPropertyOfAxiom axiom2 = (OWLSubObjectPropertyOfAxiom) formula2;
							if (axiom1.getSuperProperty().equals(axiom2.getSubProperty())){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubObjectPropertyOfAxiom && formula2 instanceof OWLSubObjectPropertyOfAxiom ){
				OWLSubObjectPropertyOfAxiom axiom1 = (OWLSubObjectPropertyOfAxiom) formula1;
				OWLSubObjectPropertyOfAxiom axiom2 = (OWLSubObjectPropertyOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubObjectPropertyOfAxiom axiom3 = dataFactory.getOWLSubObjectPropertyOfAxiom(axiom1.getSubProperty(),axiom2.getSuperProperty());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE14
	*/
	
	// $11
	RULE15{ //SubCla(X,\exists r. Y) and SubCla(Y,Z) --> SubCla(X, \exists r. Z)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule15";};
		@Override
		public java.lang.String getShortName(){return "R15";};
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			return findRuleBindings(s,false);
		}
		
		@Override
		public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
			for (OWLFormula form : formulalist){
				if (form.getArgs().get(0).equals(conclusion.getArgs().get(0)) && form.getArgs().get(1).getHead().equals(OWLSymb.EXISTS))
					return form;
			}
			return null;
		}
		
		@Override
		public OWLFormula getP2(List<OWLFormula> formulalist, OWLFormula conclusion){
			for (OWLFormula form : formulalist){
				if (!form.getArgs().get(0).equals(conclusion.getArgs().get(0)) || !form.getArgs().get(1).getHead().equals(OWLSymb.EXISTS))
					return form;
			}
			return null;
		}
		
		public void clearCaches(){
			premisesCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
			doneCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
		}
		
		// key is the sequent ID, value is a list with pairs of potential premises
		private HashMap<Integer,List<Pair<Integer,Integer>>> premisesCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
		private HashMap<Integer,List<Pair<Integer,Integer>>> doneCache = new HashMap<Integer,List<Pair<Integer,Integer>>>();
		
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v2")));
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v2"), 
				OWLFormula.createFormulaVar("v3"));
		private final OWLFormula concl = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v3")));
		
		public OWLFormula computeConclusionFormula(List<Pair<OWLFormula,OWLFormula>> matcher){
			OWLFormula formula3 = concl.clone();
			OWLFormula result = formula3.applyMatcher(matcher);
			// System.out.println("Rule 15: computed conclusion: " + result);
				return result;
		}
		
		/* NEW 15 */
		public List<RuleBinding> findRuleBindings(Sequent s, boolean... one_suffices){
			List<RuleBinding> results = new ArrayList<RuleBinding>();
			List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
			Set<OWLFormula> badConcls = new HashSet<OWLFormula>();
			// System.out.println(candidates);
			// System.out.println("Candidate existential restrictions " + candidates.size());
			HashMap<OWLFormula,List<OWLFormula>> subclassesCache = new HashMap<OWLFormula,List<OWLFormula>>();
			int examinedCount = 0;
			int tautologicalCount = 0;
			int skippedCount = 0;
			int fetchedCount = 0;
			// debug
			/* 
			for (int i = 0 ; i < candidates.size(); i++){
				System.out.println(VerbalisationManager.prettyPrint(candidates.get(i)));
			} */
				
			for (int i = 0 ; i < candidates.size(); i++){
				if (candidates.get(i).getArgs().get(0).getHead().equals(OWLSymb.BOT))
					continue; // throw out unproductive case
				try{
					OWLFormula cand1 = candidates.get(i);
					// System.out.println("cand1 " + cand1.prettyPrint());
					// List<Pair<OWLFormula,OWLFormula>> matcher = candidates.get(i).match(prem1);
					// OWLFormula prem_2 = prem2.applyMatcher(matcher);
					OWLFormula pivot = candidates.get(i).getArgs().get(1).getArgs().get(1);
					List<OWLFormula>  candidates2_pre;
					if (subclassesCache.containsKey(pivot)){
						candidates2_pre = subclassesCache.get(pivot);
						fetchedCount++;
					} else{
					OWLFormula prem_2 = OWLFormula.createFormula(OWLSymb.SUBCL,
											pivot,
											OWLFormula.createFormulaVar("v3"));
				    // System.out.println(" prem_2  " + prem_2);
					// System.out.println(prem_2bis);
					candidates2_pre = s.findMatchingFormulasInAntecedent(prem_2);
					subclassesCache.put(pivot,candidates2_pre);
					}
					// s.reportAntecedent();
					// System.out.println(" 2nd candidates " + candidates2_pre);
					for(OWLFormula cand2 : candidates2_pre){
						examinedCount++;
						if (cand2.getArgs().get(1).equals(cand2.getArgs().get(0))){
							tautologicalCount++;
							continue; // abort trivial case
						}
						
						// Skip if this was already tried.
						List<OWLFormula> forms = new LinkedList<OWLFormula>();
						forms.add(cand1);
						forms.add(cand2);
						
						if (AlreadyTriedCache.INSTANCE.wasTried(INLG2012NguyenEtAlRules.RULE15,
								forms)){
							// System.out.println("skipping");
							skippedCount++;
							continue;
						}	
						
						// if (cand2.getArgs().get(1).getHead().equals(OWLSymb.TOP))
						// 	continue; // throw out unproductive case <-- we get incomplete if we do that.
						 // if (cand2.getArgs().get(1).getHead().equals(OWLSymb.TOP) && cand1.getArgs().get(0).equals(cand1.getArgs().get(1)))
						 // 	continue; // throw out unproductive case
						// if (cand1.getArgs().get(0).getHead().equals(OWLSymb.BOT)){
							// System.out.println("throwing out " + cand1);
						// 	continue; // abort trivial case <-- above.
						// }
						// restriction to avoid cyclicity: SECOND FORMULA MAY NOT BE CYCLIC!!!
						// if (!testImpactContainsSubformula(cand2.getArgs().get(1),cand2.getArgs().get(0))){
						if (!OWLFormula.containsSubformula(cand2.getArgs().get(1),cand2.getArgs().get(0))){
							// now filter out those conclusions that already exist
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL,
									candidates.get(i).getArgs().get(0), // v1
									OWLFormula.createFormula(OWLSymb.EXISTS, 
											cand1.getArgs().get(1).getArgs().get(0), //r1
											cand2.getArgs().get(1) // v3
											)
									);
							// System.out.println(" 2nd candidate filtered " + cand2);
							// System.out.println("(1) considering binding for " + conclusion);
							// System.out.println(s.alreadyContainedInAntecedent(conclusion));
							// System.out.println("prem 1 " + cand1.prettyPrint());
							// System.out.println("prem 2 " + cand2.prettyPrint());
							// System.out.println("conclusion " + conclusion.prettyPrint());
							if (badConcls.contains(conclusion))
								continue;
							if (!s.alreadyContainedInAntecedent(conclusion) && 
									// constructed conclusion (the superclass) must be of relevance!!!
									((testImpactContainsOrDeeplyContains(s,conclusion.getArgs().get(1))) ||
									// ((s.antecedentContainsOrDeeplyContains(conclusion.getArgs().get(1))) || 
								    (s.succedentContainsOrDeeplyContains(conclusion.getArgs().get(1)))))	{
									// s.reportAntecedent();
									// System.out.println("good!");
									// further restriction to avoid cyclicity: subsumed of second formula may not be subclass of cyclic part
									// find all further subsumed formulas of subsumed formula (according to current status)
								
									// now also check cyclicity with those 
									// System.out.println("entering if");
									boolean allclear = true;
									boolean chain = false;
									if (isCyclicBinaryExistsChain(cand2.getArgs().get(1),null)){
										allclear = false;
										chain = true;
									}
									if (!cand2.getArgs().get(0).isClassFormula()){
										List<OWLFormula> furtherSubsumed = s.findMatchingFormulasInAntecedent(OWLFormula.createFormula(OWLSymb.SUBCL,OWLFormula.createFormulaVar("v4"), cand2.getArgs().get(0)));
										// System.out.println("further subsumed " + furtherSubsumed.size());
										for (OWLFormula subcand : furtherSubsumed){
											// System.out.println("Dealing with " + cand2 + " subcand " + subcand);
											// System.out.println("Rule 15 checking out also " + subcand.getArgs().get(0) + " in " + cand2.getArgs().get(1));
											if (OWLFormula.containsSubformula(cand2.getArgs().get(1),subcand.getArgs().get(0)))
												// System.out.println("Rule 15 found " + subcand.getArgs().get(0) + " in " + cand.getArgs().get(1));	
												allclear = false;
												break;
										}
									}
									if (allclear){
										// now construct the binding!
											// System.out.println("creating binding for " + conclusion.prettyPrint());
											// System.out.println(s.alreadyContainedInAntecedent(conclusion));
											RuleBinding binding = new RuleBinding(conclusion,null);
											SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand1));
											binding.insertPosition("A1", position1);
											SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand2));
											binding.insertPosition("A2", position2);
											results.add(binding);
											continue; // <--- to not cache this case as unsuccessful!
									} // endif allclear
									else{
										if (chain)
											AlreadyTriedCache.INSTANCE.setTried(INLG2012NguyenEtAlRules.RULE15, forms);
									}
							}  // endif alreadycontained
							else{
								if (s.alreadyContainedInAntecedent(conclusion)){
									// conclusion already derived
									AlreadyTriedCache.INSTANCE.setTried(INLG2012NguyenEtAlRules.RULE15, forms);
								}
								// System.out.println("not good!");
								badConcls.add(conclusion);
							}
						} // endif containsSubformula
						else{
							// cand2 is cyclic
							AlreadyTriedCache.INSTANCE.setTried(INLG2012NguyenEtAlRules.RULE15, forms);
						}
						// AlreadyTriedCache.INSTANCE.setTried(INLG2012NguyenEtAlRules.RULE15, forms);
					} // end loop candidates2	
				} // end try
				catch (Exception e){}
			} // end for
				// Statistics
			// System.out.println("Examined " + examinedCount + " tautological " + tautologicalCount + " skipped " + skippedCount + " fetched " + fetchedCount);
			
			
				return results;
		}
		
		public boolean testImpactContainsSubformula(OWLFormula a, OWLFormula b){
			// throw new RuntimeException();
			// for (int i =0; i<1000;i++)
			// 	System.out.print(".");
			// System.out.println(".");
			return OWLFormula.containsSubformula(a,b);
		}
		
		public boolean testImpactContainsOrDeeplyContains(Sequent s, OWLFormula a){
			// throw new RuntimeException();
			// for (int i =0; i<1000;i++)
			// 	System.out.print(".");
			// System.out.println(".");
			return s.subexprInSequent(a);
			// return s.antecedentContainsOrDeeplyContains(a);
		}
	
		// not used at the moment.
		public List<Pair<Integer,Integer>> findPremFormulas(Sequent s){
			Timer.INSTANCE.start("rule15 - find prem formulas");
			List<Pair<Integer,Integer>> results = new ArrayList<Pair<Integer,Integer>>();
			List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
			// Collections.sort(candidates,s.formulaAscComparatorAnt());  <-- not needed, really!
			
			
			// check candidates for proper matching
			// for (int i = 0; i < candidates.size();i++){
			// 	for (int j = 0; j < candidates.size();j++){
			for (int i = 0 ; i < candidates.size(); i++){
				List<OWLFormula> candidates2 = new ArrayList<OWLFormula>();
				// get matcher (potential speed - up: do not compute candidates and matcher separately)
				try{
					
				List<Pair<OWLFormula,OWLFormula>> matcher = candidates.get(i).match(prem1);
				// System.out.println("prem 2 " + prem2 +  " Matcher: " + matcher);
				// now build new formula using the matcher
				OWLFormula prem_2 = prem2.applyMatcher(matcher);
				// System.out.println("prem_2 " + prem_2);
				// now search for this formula in sequent
				List<OWLFormula> candidates2_pre = new ArrayList<OWLFormula>();
				candidates2_pre = s.findMatchingFormulasInAntecedent(prem_2);
				// restriction to avoid cyclicity: SECOND FORMULA MAY NOT BE CYCLIC!!!
				for(OWLFormula cand : candidates2_pre){
					if (!testImpactContainsSubformula(cand.getArgs().get(1),cand.getArgs().get(0))){
					//if (!OWLFormula.containsSubformula(cand.getArgs().get(1),cand.getArgs().get(0))){
						// further restriction to avoid cyclicity: subsumed of second formula may not be subclass of cyclic part
						// find all further subsumed formulas of subsumed formula (according to current status)
						List<OWLFormula> furtherSubsumed = 
						s.findMatchingFormulasInAntecedent(OWLFormula.createFormula(OWLSymb.SUBCL,OWLFormula.createFormulaVar("v4"), cand.getArgs().get(0)));
						// now also check cyclicity with those 
						boolean allclear = true;
						for (OWLFormula subcand : furtherSubsumed){
							// System.out.println("Rule 15 checking out also " + subcand.getArgs().get(0) + " in " + cand.getArgs().get(1));
							if (OWLFormula.containsSubformula(cand.getArgs().get(1),subcand.getArgs().get(0)))
								// System.out.println("Rule 15 found " + subcand.getArgs().get(0) + " in " + cand.getArgs().get(1));	
								allclear = false;
						}
						if (isCyclicBinaryExistsChain(cand.getArgs().get(1),null))
							allclear = false;
						if (allclear)
						candidates2.add(cand);
					}
				}
				
				// System.out.println("candidates2 " + candidates2);
				}
				catch (Exception e){
					System.out.println(" Rule 15: this should not happen");
					e.printStackTrace();
				}
				for (int j = 0 ; j < candidates2.size(); j++){
					if (!candidates.get(i).equals(candidates2.get(j))){
						results.add(new Pair<Integer,Integer>(
								s.antecedentFormulaGetID(candidates.get(i)),
								s.antecedentFormulaGetID(candidates2.get(j))
								));
					}
				}
			}
			Timer.INSTANCE.stop("rule15 - find prem formulas");
			return results;
		}
		
	
		
		// not used at the moment
		public void generateRuleBinding(Sequent s, OWLFormula candidate1, OWLFormula candidate2, ArrayList<RuleBinding> results){
			try{
				List<Pair<OWLFormula,OWLFormula>> matcher2  = OWLFormula.getMatcher2Ary(candidate1, candidate2,prem1, prem2);
				// System.out.println("COMPUTED MATCHER: " + matcher2);
				OWLFormula conclusion = computeConclusionFormula(matcher2);
				// System.out.println("Rule 15 : conclusion " + conclusion);
				if (!s.alreadyContainedInAntecedent(conclusion)){
					RuleBinding binding = new RuleBinding(conclusion,null);
					SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate1));
					binding.insertPosition("A1", position1);
					SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate2));
					binding.insertPosition("A2", position2);
					results.add(binding);
				}
			}
			catch (Exception e){
			}
		}
			
		
	
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			return SequentList.makeANDSequentList(sequents);
		}
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			// Timer.INSTANCE.start("rule15 - application");
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// SequentPosition position1 = binding.get("A1");
			// SequentPosition position2 = binding.get("A2");
			// ArrayList newsequents = new ArrayList<Sequent>();
			
			/*
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule15");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			OWLFormula formula1 = sequent.antecedentGetFormula(pos1.getToplevelPosition());
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule15");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			OWLFormula formula2 = sequent.antecedentGetFormula(pos2.getToplevelPosition());
			List<Pair<OWLFormula,OWLFormula>> matcher = OWLFormula.getMatcher2Ary(formula1, formula2, prem1, prem2);
			OWLFormula conclusionformula = computeConclusionFormula(matcher);	
			*/
			
			OWLFormula conclusionformula = binding.getNewAntecedent();
			
			/*
			if (sequent.alreadyContainedInAntecedent(conclusionformula)){
				// System.out.println("Rule 15 : formula already contained");
				throw new Exception("useless inference");
			}
			*/
		    // OWLSubClassOfAxiom axiom3 = (OWLSubClassOfAxiom) conclusionformula.toOWLAPI();
			RuleApplicationResults result = new RuleApplicationResults();
			Sequent s = sequent; //.clone();
			result.setOriginalFormula(s);
			result.addAddition("A1",conclusionformula);
			results.add(result);
			// depth bookkeeping
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			// Timer.INSTANCE.stop("rule15 - application");
			return results;
		}
		
		
		
		
	}, // END RULE15
	
	/*
	RULE16{ // EqCla(X,Int(Y,Z,...)) --> SubClass(X,Y)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule16";};
		public java.lang.String getShortName(){return "R16";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLEquivalentClassesAxiom){
					OWLEquivalentClassesAxiom axiom = (OWLEquivalentClassesAxiom) formula;
					List<OWLClassExpression> expressions = axiom.getClassExpressionsAsList();
					int count2 = 0;
					for (Object expr1 : expressions){
						int count3 = 0;
						for (Object expr2 : expressions){
							OWLClassExpression exp1 = (OWLClassExpression) expr1;
							OWLClassExpression exp2 = (OWLClassExpression) expr2;
							if (!exp1.equals(exp2) && exp2 instanceof OWLObjectIntersectionOf){
								OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) exp2;
								List<OWLClassExpression> intersectionlist = intersection.getOperandsAsList();
								for (int i=0; i < intersectionlist.size() ; i++){
									RuleBinding binding = new RuleBinding();
									// position of X
									List<Integer> listX = new ArrayList<Integer>();
									listX.add(count1);
									listX.add(count2);
									SequentPosition positionX = new SequentSinglePosition(SequentPart.ANTECEDENT, listX);
									binding.insertPosition("A1", positionX);
									// position of Y
									List<Integer> listY = new ArrayList<Integer>();
									listY.add(count1);
									listY.add(count3);
									listY.add(i);
									SequentPosition positionY = new SequentSinglePosition(SequentPart.ANTECEDENT, listY);
									binding.insertPosition("A2", positionY);
									results.add(binding);
								} // end for(i)
							} //endif
							count3++;
						} //end loop expr2/count3
						count2++;
					}// end inner for loop
				} //endif
				count1++;
			} // end outer loop/count1
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule10");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule10");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			List<Integer> positions1 = pos1.getPosition();
			List<Integer> positions2 = pos2.getPosition();
			if (formula1 instanceof OWLEquivalentClassesAxiom
					&& ((OWLEquivalentClassesAxiom) formula1).getClassExpressionsAsList().get(positions2.get(1)) instanceof OWLObjectIntersectionOf
					){
				OWLEquivalentClassesAxiom axiom1 = ((OWLEquivalentClassesAxiom) formula1);
				OWLClassExpression expr1 = axiom1.getClassExpressionsAsList().get(positions1.get(1));
				OWLObjectIntersectionOf intersectionexpr = (OWLObjectIntersectionOf) axiom1.getClassExpressionsAsList().get(positions2.get(1));
				OWLClassExpression expr2 = intersectionexpr.getOperandsAsList().get(positions2.get(2));
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(expr1,expr2);
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE16
	*/
	
	/*
	RULE17{ // ObjectPropDomain(r,X), and SubClass(\all (r,\bot),X) --> SubClass(\top,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule17";};
		public java.lang.String getShortName(){return "R17";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyDomainAxiom // ObjectPropertyDomain(r,X)
						){ 
					OWLObjectPropertyDomainAxiom axiom1 = ((OWLObjectPropertyDomainAxiom) formula);
					// System.out.println(axiom1);
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom 
							    && ((OWLSubClassOfAxiom) formula2).getSubClass() instanceof OWLObjectAllValuesFrom
								&& ((OWLObjectAllValuesFrom) ((OWLSubClassOfAxiom) formula2).getSubClass()).getProperty().equals(axiom1.getProperty()) 
								&& ((OWLObjectAllValuesFrom) ((OWLSubClassOfAxiom) formula2).getSubClass()).getFiller().isOWLNothing() 
								){
							//need to check that resulting axiom is not there already
							boolean exists = false;
							for (Object formula_check: antecedent){
								if (formula_check instanceof OWLSubClassOfAxiom &&
										((OWLSubClassOfAxiom) formula_check).getSubClass().isOWLThing()
										&& ((OWLSubClassOfAxiom) formula_check).getSuperClass().equals(axiom1.getDomain())
										){
									exists = true;
								}
							}
								if (!exists){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}
						}
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			ArrayList<RuleApplicationResults> results = new ArrayList();
			
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule17");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule17");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyDomainAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					&& ((OWLSubClassOfAxiom) formula2).getSubClass() instanceof OWLObjectAllValuesFrom
					){
				OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(),axiom1.getDomain());
				if (!antecedent.contains(axiom3)){
					RuleApplicationResults results1 = new RuleApplicationResults();
					results1.setOriginalFormula(s);
					results1.addAddition("A1", axiom3);
					results.add(results1);
					// depth bookkeeping
					results1.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
				}
				}
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		
		public void expandTactic(ProofTree tree, ProofNode<Sequent,java.lang.String,AbstractSequentPositions> source, JustificationNode<java.lang.String,AbstractSequentPositions> justification) throws Exception{
			// necessary to deal with or alternatives
			HierarchNode hnode = null;
			List<HierarchNode<String,AbstractSequentPositions>> hnodes = source.getJustifications();
			for(HierarchNode h: hnodes){
				if(h.getJustifications().contains(justification)){
					hnode = h;
				}
			}
			if (hnode == null) throw new Exception("fatal error");
			//
			AbstractSequentPositions positions = justification.getPositions();
			List<Integer> original_premiseids =  justification.getPremises();
			if (positions instanceof RuleBindingForNode){
					// ln("positions is instance of rule binding for node");
					RuleBindingForNode rb = (RuleBindingForNode) positions;
					// we keep track of the original set of justifications, to find out which is the new one that is created.
						List<JustificationNode> original_jnodes =  new ArrayList<JustificationNode>(tree.getProofNode(rb.getNodeId()).getJustifications());
						Sequent original_seq = new Sequent(source.getContent().getAntecedent(),source.getContent().getSuccedent());
					//
					// apply Defn. Domain
					List<RuleApplicationResults> results = INLG2012NguyenEtAlRules.RULE1.computeRuleApplicationResults(original_seq, rb);
					InferenceApplicationService.INSTANCE.applySequentInferenceRule(tree, rb, INLG2012NguyenEtAlRules.RULE1); // this could be made more efficient, by using the results computed above
					// we pinpoint the newly added sequent
						JustificationNode newj = tree.getNewestJustification();
						int newnodeid = (Integer) newj.getPremises().get(0);	
						ProofNode newnode1 = tree.getProofNode(newnodeid);
						Sequent newsequent1 = (Sequent) newnode1.getContent();
					// now we apply AdditionalDLRules.FORALLWEAKENING
						List<RuleBindingForNode> forallweakeningbindings = InferenceApplicationService.INSTANCE.findRuleBindingsWhereInferenceApplicable(tree, AdditionalDLRules.FORALLWEAKENING);
						// now need to find the right binding (i.e. the binding, where the formula at the binding position is syntactically equal to the formula that has been added by the results as "A1")
						RuleBindingForNode right_binding = null;
						for (RuleBindingForNode rbfn : forallweakeningbindings){
								// get position & formula of the binding
								SequentPosition position1 = rbfn.getRuleBinding().get("A1");
								if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position!");}
								SequentSinglePosition pos1 = (SequentSinglePosition) position1;
								List<Integer> poslist1 = pos1.getPosition();
								Object formula1 = newsequent1.getAntecedent().get(pos1.getToplevelPosition());
								// get formula that has been added as "A1"
								Object formula2 = results.get(0).getAddition("A1"); // we can take the first results element, since Rule1 only produces one new sequent
								OWLObject f1 = (OWLObject) formula1;
								OWLObject f2 = (OWLObject) formula2;
								if (f1.equals(f2)){
									right_binding = rbfn;
								}
						} // end loop for weakening bindings
						// now apply forall weakening
						// InferenceApplicationService.INSTANCE.applySequentInferenceRule(tree, right_binding, AdditionalDLRules.FORALLWEAKENING); 
						 InferenceApplicationService.INSTANCE.applySequentInferenceRuleToFillGap(tree, right_binding, AdditionalDLRules.FORALLWEAKENING,original_premiseids,hnode); 
			}
			tree.print();
			// TODO: CONTINUE HERE!!!
			return;
		}
		
	}, // END RULE17
	*/
	
	/*
RULE18{ // ObjePropRng(r,X) and SymObjProp(r) --> ObjPropDom(r,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule18";};
		public java.lang.String getShortName(){return "R18";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyRangeAxiom // ObjectPropertyRange(r,X)
						){ 
					OWLObjectPropertyRangeAxiom axiom1 = ((OWLObjectPropertyRangeAxiom) formula);
					// System.out.println(axiom1);
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSymmetricObjectPropertyAxiom
								&& ((OWLSymmetricObjectPropertyAxiom) formula2).getProperty().equals(axiom1.getProperty()) 
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}						
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyRangeAxiom 
					&& formula2 instanceof OWLSymmetricObjectPropertyAxiom
					&& ((OWLSymmetricObjectPropertyAxiom) formula2).getProperty().equals( ((OWLObjectPropertyRangeAxiom) formula1).getProperty())
					){
				OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula1;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLObjectPropertyDomainAxiom axiom3 = dataFactory.getOWLObjectPropertyDomainAxiom(axiom1.getProperty(),axiom1.getRange());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE18
	*/
	
	/*
RULE19{ // SubCla(Y,X) and SubCla(-Y,X) --> SubCla(\top,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule19";};
		public java.lang.String getShortName(){return "R19";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom // SubCla(Y,X) 
						){ 
					OWLSubClassOfAxiom axiom1 = ((OWLSubClassOfAxiom) formula);
					System.out.println(axiom1);
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom
								&& ((OWLSubClassOfAxiom) formula2).getSubClass().equals(axiom1.getSubClass().getObjectComplementOf()) 
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}						
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					&& ((OWLSubClassOfAxiom) formula2).getSubClass().equals( ((OWLSubClassOfAxiom) formula1).getSubClass().getObjectComplementOf())
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(),axiom1.getSuperClass());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE19
	*/
	
	
	/*
RULE20{  // ObjePropDom(r,\bot) --> SubCla(\top,\forall r.\bot)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule20";};
		public java.lang.String getShortName(){return "R20";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyDomainAxiom){
					OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula;
					if (axiom1.getDomain().isOWLNothing()){
						RuleBinding binding = new RuleBinding();
						SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
						binding.insertPosition("A1", position1);
						results.add(binding);
				
					}
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule20");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyDomainAxiom){
				OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
				if (axiom1.getDomain().isOWLNothing()){
					OWLObjectPropertyExpression property = axiom1.getProperty();
					OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
					OWLDataFactory dataFactory=manager.getOWLDataFactory();
					OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(),dataFactory.getOWLObjectAllValuesFrom(property,dataFactory.getOWLNothing()));
					if (!antecedent.contains(axiom3)){
						s.addAntecedent(axiom3);	
					    newsequents.add(s);
					}
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE20
	*/
	
	/*
RULE21{  // ObjePropRan(r,\bot) --> SubCla(\top,\forall r.\bot)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule21";};
		public java.lang.String getShortName(){return "R21";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyRangeAxiom){
					OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula;
					if (axiom1.getRange().isOWLNothing()){
						RuleBinding binding = new RuleBinding();
						SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
						binding.insertPosition("A1", position1);
						results.add(binding);
				
					}
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyRangeAxiom){
				OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula1;
				if (axiom1.getRange().isOWLNothing()){
					OWLObjectPropertyExpression property = axiom1.getProperty();
					OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
					OWLDataFactory dataFactory=manager.getOWLDataFactory();
					OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(),dataFactory.getOWLObjectAllValuesFrom(property,dataFactory.getOWLNothing()));
					if (!antecedent.contains(axiom3)){
						s.addAntecedent(axiom3);	
					    newsequents.add(s);
					}
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE21
	*/
	
	/*
RULE22{ // Disjoint(X,Y) and SubCla(Z,X) and SubCla(W,Y) --> Disjoint(Z,Y)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule22";};
		public java.lang.String getShortName(){return "R22";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLDisjointClassesAxiom // Disjoint(X,Y)
						){ 
					OWLDisjointClassesAxiom axiom1 = ((OWLDisjointClassesAxiom) formula);
					// we have a disjoint classes axiom, now need to pick two elements
					Set<OWLClassExpression> disjointclasses = axiom1.getClassExpressions();
					int count1a = 0;
					for (OWLClassExpression cl1: disjointclasses){
						int count1b= 0;
						for (OWLClassExpression cl2: disjointclasses){
							if (!cl1.equals(cl2)){
								int count2 = 0;
								for (Object formula2 : antecedent){
									if (formula2 instanceof OWLSubClassOfAxiom
											&& ((OWLSubClassOfAxiom) formula2).getSuperClass().equals(cl1)
											){
										int count3 = 0;
										for (Object formula3 : antecedent){
											if (formula3 instanceof OWLSubClassOfAxiom
													&& ((OWLSubClassOfAxiom) formula3).getSuperClass().equals(cl2)
													){
												RuleBinding binding = new RuleBinding();
												List<Integer> list1a = new ArrayList<Integer>();
												list1a.add(count1);
												list1a.add(count1a);
												SequentPosition position1a = new SequentSinglePosition(SequentPart.ANTECEDENT, list1a);
												binding.insertPosition("A1a", position1a);
												List<Integer> list1b = new ArrayList<Integer>();
												list1b.add(count1);
												list1b.add(count1b);
												SequentPosition position1b = new SequentSinglePosition(SequentPart.ANTECEDENT, list1b);
												binding.insertPosition("A1b", position1b);
												SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
												binding.insertPosition("A2", position2);
												SequentPosition position3 = new SequentSinglePosition(SequentPart.ANTECEDENT, count3);
												binding.insertPosition("A3", position3);
												results.add(binding);}			// endif	
											count3++;
										} // end loop formula3
									} // endif
									count2++;	
								} // end loop formula2
							} // end if (not equal)
						count1b++;		
						} // loop cl2
						count1a++;		
					} // loop cl1
				} // endif
				count1++;
			} // end outler loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1a = binding.get("A1a");
			SequentPosition position1b = binding.get("A1b");
			SequentPosition position2 = binding.get("A2");
			SequentPosition position3 = binding.get("A3");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1a instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule22");}
			SequentSinglePosition pos1a = (SequentSinglePosition) position1a;
			if (!(position1b instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule22");}
			SequentSinglePosition pos1b = (SequentSinglePosition) position1b;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule22");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			if (!(position3 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule22");}
			SequentSinglePosition pos3 = (SequentSinglePosition) position3;
			Object formula1 = antecedent.get(pos1a.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			Object formula3 = antecedent.get(pos3.getToplevelPosition());
			if (formula1 instanceof OWLDisjointClassesAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					&& formula3 instanceof OWLSubClassOfAxiom
					){
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				OWLSubClassOfAxiom axiom3 = (OWLSubClassOfAxiom) formula3;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLDisjointClassesAxiom axiom4 = dataFactory.getOWLDisjointClassesAxiom(axiom2.getSubClass(),axiom3.getSubClass());
				if (!antecedent.contains(axiom4)){
					s.addAntecedent(axiom4);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE22
	*/
	
	// TACTIC!
RULE23Repeat{ // SubCla(X, \exists r.Y) and SubCla(Y, \exists r.Z) and transitive(r) --> SubCla(X, \exists r.Z) .. repeatedly
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule23Repeat";};
		@Override
		public java.lang.String getShortName(){return "23r";};
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v2")));
		
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v2"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v3")));
		
		private final OWLFormula prem3 = OWLFormula.createFormula(OWLSymb.TRANSITIVE,
				OWLFormula.createFormulaRoleVar("r1")); 
		
		
		private final OWLFormula result = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v3")));
				
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// first check for transitivity of roles.
			List<RuleBinding> results = new ArrayList<RuleBinding>();
			List<OWLFormula> candidates1 = s.findMatchingFormulasInAntecedent(prem3);
			// System.out.println("r23repeat: prem3" + prem3);
			// System.out.println("candidates1: "+ candidates1);
			List<CopyOnWriteArrayList<OWLFormula>> chains = new ArrayList<CopyOnWriteArrayList<OWLFormula>>();
			for (int i = 0 ; i < candidates1.size(); i++){
			    List<OWLFormula> candidates2 = new ArrayList<OWLFormula>();
				// get matcher 
				try{
				List<Pair<OWLFormula,OWLFormula>> matcher = candidates1.get(i).match(prem3);
				// System.out.println("rule23 repeat: Matcher: " + matcher);
				// now build new formula using the matcher
				OWLFormula prem_1 = prem1.applyMatcher(matcher);
				// System.out.println("prem_1 " + prem_1);
				// now search for this formula in sequent
				candidates2 = s.findMatchingFormulasInAntecedent(prem_1);
				// System.out.println("candidates 2" + candidates2);
				 // Check if found formulas actually match
				for (OWLFormula candidate2: candidates2){
					// now try to build long chains from the candidate2 elements!
					// (1) Check if no chain contains this element. If so, add this as a singleton.
					boolean element_contained = false;	
					boolean still_needs_adding = true;
						// search loop
						for (List<OWLFormula> chain : chains){
							if (chain.contains(candidate2))
								element_contained = true;
						}
						chains = new CopyOnWriteArrayList<CopyOnWriteArrayList<OWLFormula>>(chains);
						// checking loop
						if (!element_contained){
							for (CopyOnWriteArrayList<OWLFormula> chain : chains){
							// (2) check if element comes before some other element in the current chain
							for (OWLFormula candidate3: chain){
								OWLFormula form1 = candidate2.getArgs().get(1).getArgs().get(1);
								// System.out.println("form1" + form1);
								OWLFormula form2 = candidate3.getArgs().get(0);
								// System.out.println("form2" + form2);
								if (form1.equals(form2) && still_needs_adding){
									// System.out.println("add in front");
									chain.add(0,candidate2); // add in front
									// System.out.println("added! : " + chains);
									still_needs_adding = false;	
								}	 // end if
								// (2b) check if element comes before some other element in the current chain
								form1 = candidate3.getArgs().get(1).getArgs().get(1);
								// System.out.println("form1" + form1);
								form2 = candidate2.getArgs().get(0);
								// System.out.println("form2" + form2);
								if (form1.equals(form2) && still_needs_adding){
									// System.out.println("add in back");
									chain.add(candidate2); // add in back
									// System.out.println("added! : " + chains);
									still_needs_adding = false;	
								}	 // end if
							} // end for
							}
							// if we are here, need to add as a new element
							if(still_needs_adding){
								CopyOnWriteArrayList<OWLFormula> newlist = new CopyOnWriteArrayList<OWLFormula>();
								newlist.add(candidate2);	
								chains.add(newlist);
								// System.out.println("added! : " + chains);
							}
						}	// end checking loop
				} // end for loop for candidates2
				// System.out.println("Chains : " + chains);
				// now need to connect chains
				for (int m = 0; m<chains.size();m++){
					for (int n = m+1; n<chains.size();n++){
						// case 1
						if (chains.get(m).size()>0 && chains.get(n).size()>0 && 
								chains.get(m).get(chains.get(m).size()-1).getArgs().get(1).getArgs().get(1).equals(chains.get(n).get(0).getArgs().get(0))
								){
							chains.get(m).addAll(chains.get(n));
							chains.get(n).removeAll(chains.get(n));
						}
						// case 2
						if (chains.get(m).size()>0 && chains.get(n).size()>0 && 
								chains.get(n).get(chains.get(n).size()-1).getArgs().get(1).getArgs().get(1).equals(chains.get(m).get(0).getArgs().get(0))
								){
							chains.get(n).addAll(chains.get(m));
							chains.get(m).removeAll(chains.get(m));
						}
					}
				}
				// System.out.println("Chains after tidying up: " + chains);
				// now add to the results all possibilities involving at least length 2
				for(List<OWLFormula> chain: chains){
					// System.out.println(chain.size());
					for(int j = 0; j< chain.size() ; j++){
						for (int k = j+2; k < chain.size();k++){
							// System.out.println("j :" + j + " k:" + k);	
							// construct binding
							// System.out.println(candidates1.get(i));
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, 
									chain.get(j).getArgs().get(0), 
									OWLFormula.createFormula(OWLSymb.EXISTS, 
											candidates1.get(i).getArgs().get(0), 
											chain.get(k).getArgs().get(1).getArgs().get(1)));								
							// System.out.println("R23repeat : conclusion: " + conclusion);
							if (!s.alreadyContainedInAntecedent(conclusion)){
								RuleBinding binding = new RuleBinding(conclusion,null);																						
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(chain.get(j)));
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(chain.get(k)));
								binding.insertPosition("A2", position2);
								SequentPosition position3 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidates1.get(i)));
								binding.insertPosition("A3", position2);
								results.add(binding);
							} // end if (not contained)
						}	// end for (k)
					} // end for (j)
				} //end for (chain)
			} catch (Exception e){
				e.printStackTrace();
			}
			} // end formula 1 loop
			// System.out.println("DEBUG R23 repeat === results " + results);			
			return results;
		}
		
		@Override
		public List<RuleApplicationResults>  computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			SequentPosition position3 = binding.get("A3");
			Sequent s = sequent; //.clone();
			// ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23repeat");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23repeat");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			if (!(position3 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23repeat");}
			SequentSinglePosition pos3 = (SequentSinglePosition) position3;
			OWLFormula formula1 = s.antecedentGetFormula(pos1.getToplevelPosition());
			OWLFormula formula2 = s.antecedentGetFormula(pos2.getToplevelPosition());
			OWLFormula formula3 = s.antecedentGetFormula(pos3.getToplevelPosition());
			// Object formula1 = antecedent.get(pos1.getToplevelPosition());
			// Object formula2 = antecedent.get(pos2.getToplevelPosition());
			// Object formula3 = antecedent.get(pos3.getToplevelPosition());
			OWLFormula conclusion = binding.getNewAntecedent();
			// System.out.println(formula1);
			// System.out.println(formula2);
			// System.out.println(formula3);
			// System.out.println(conclusion);
				if (!sequent.alreadyContainedInAntecedent(conclusion)){
					RuleApplicationResults results1 = new RuleApplicationResults();
					results1.setOriginalFormula(s);
					results1.addAddition("A1", conclusion);
					results.add(results1);	
					// System.out.println("DEBUG === " + conclusion);
					// depth bookkeeping
					results1.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
				}
			return results;
		}
		
		
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			return SequentList.makeANDSequentList(sequents);
		}
				
	}, // END RULE23
	
RULE23{ // SubCla(X, \exists r.Y) and SubCla(Y, \exists r.Z) and transistive(r) --> SubCla(X, \exists r.Z)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule23";};
		@Override
		public java.lang.String getShortName(){return "R23";};
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v2")));
		
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v2"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v3")));
		
		private final OWLFormula prem3 = OWLFormula.createFormula(OWLSymb.TRANSITIVE,
				OWLFormula.createFormulaRoleVar("r1")); 
		
		
		private final OWLFormula result = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v3")));
				
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
		    // System.out.println("Rule 23: find premises called!");
		    List<RuleBinding> results = new ArrayList<RuleBinding>();
			List<OWLFormula> candidates1 = s.findMatchingFormulasInAntecedent(prem1);
			// System.out.println("candidates1: "+ candidates1);
			for (int i = 0 ; i < candidates1.size(); i++){
				    List<OWLFormula> candidates2 = new ArrayList<OWLFormula>();
					// get matcher 
					try{
					List<Pair<OWLFormula,OWLFormula>> matcher = candidates1.get(i).match(prem1);
					// System.out.println("prem 2 " + prem2 +  " Matcher: " + matcher);
					// now build new formula using the matcher
					OWLFormula prem_2 = prem2.applyMatcher(matcher);
					// System.out.println("prem_2 " + prem_2);
					// now search for this formula in sequent
					candidates2 = s.findMatchingFormulasInAntecedent(prem_2);
					 // System.out.println("candidates 2" + candidates2);
					 // Check if found formulas actually match
					for (OWLFormula candidate2: candidates2){
						 List<OWLFormula> candidates3 = new ArrayList<OWLFormula>();
						// get matcher 
							try{
								List<Pair<OWLFormula,OWLFormula>> matcher2 = candidate2.match(prem2);
								OWLFormula prem_3 = prem3.applyMatcher(matcher2);
								// System.out.println("prem_3 " + prem_3);
								candidates3 = s.findMatchingFormulasInAntecedent(prem_3);
								for (OWLFormula candidate3: candidates3){
									List<Pair<OWLFormula,OWLFormula>> matcher_all = matcher;
									matcher_all.addAll(matcher2);
									OWLFormula conclusion = result.applyMatcher(matcher_all);	
									// System.out.println("conclusion: " + conclusion);
									if (!s.alreadyContainedInAntecedent(conclusion)){
										RuleBinding binding = new RuleBinding(conclusion,null);																						
										SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidates1.get(i)));
										binding.insertPosition("A1", position1);
										SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate2));
										binding.insertPosition("A2", position2);
										SequentPosition position3 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate2));
										binding.insertPosition("A3", position2);
										results.add(binding);
										
								} // end if
								} // end loop for third formula
							} catch(Exception e){}
					} // end formula 2 loop
					} catch (Exception e){}
			} // end formula 1 loop
					
			
			// System.out.println("DEBUG === results " + results);			
			return results;
		}
		
		@Override
		public List<RuleApplicationResults>  computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			SequentPosition position3 = binding.get("A3");
			Sequent s = sequent; //.clone();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			if (!(position3 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule22");}
			SequentSinglePosition pos3 = (SequentSinglePosition) position3;
			OWLFormula formula1 = s.antecedentGetFormula(pos1.getToplevelPosition());
			OWLFormula formula2 = s.antecedentGetFormula(pos2.getToplevelPosition());
			OWLFormula formula3 = s.antecedentGetFormula(pos3.getToplevelPosition());
			// Object formula1 = antecedent.get(pos1.getToplevelPosition());
			// Object formula2 = antecedent.get(pos2.getToplevelPosition());
			// Object formula3 = antecedent.get(pos3.getToplevelPosition());
			OWLFormula conclusion = binding.getNewAntecedent();
			// System.out.println(formula1);
			// System.out.println(formula2);
			// System.out.println(formula3);
			// System.out.println(conclusion);
				if (!sequent.alreadyContainedInAntecedent(conclusion)){
					RuleApplicationResults results1 = new RuleApplicationResults();
					results1.setOriginalFormula(s);
					results1.addAddition("A1", conclusion);
					results.add(results1);	
					// System.out.println("DEBUG === " + conclusion);
					// depth bookkeeping
					results1.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
				}
			return results;
		}
		
		
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			return SequentList.makeANDSequentList(sequents);
		}
				
	}, // END RULE23
	
	/*
RULE24{ // SubCla(X,Union(Y,Z)) and SubCla(Y,W) and SubCla(Z,W) -> SubCla(X,W)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule24";};
		public java.lang.String getShortName(){return "R24";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom // SubCla(X, Union(Y,Z))
						&&  ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectUnionOf
						){ 
					OWLSubClassOfAxiom axiom1 = ((OWLSubClassOfAxiom) formula);
					OWLObjectUnionOf union1 = (OWLObjectUnionOf) ((OWLSubClassOfAxiom) formula).getSuperClass();
					int count2= 0;
					for (Object formula2: antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom // SubCla(Y,W)
						&&  ((OWLSubClassOfAxiom) formula2).getSubClass().equals(union1.getOperandsAsList().get(0))
								){ 
							int count3=0;
							for (Object formula3: antecedent){
							if (formula3 instanceof OWLSubClassOfAxiom // SubCla(Z,W)
									&&  ((OWLSubClassOfAxiom) formula3).getSubClass().equals(union1.getOperandsAsList().get(1))
									&&  ((OWLSubClassOfAxiom) formula3).getSuperClass().equals(((OWLSubClassOfAxiom) formula2).getSuperClass())
									){ 
									RuleBinding binding = new RuleBinding();
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
									binding.insertPosition("A2", position2);
									SequentPosition position3 = new SequentSinglePosition(SequentPart.ANTECEDENT, count3);
									binding.insertPosition("A3", position3);
									results.add(binding);}	// endif A3	
							count3++;
						} // end loop A3
						} // endif A2
						count2++;
					} // end loop A2
				} // endif A1
				count1++; 
			} // end loop A1
						
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			SequentPosition position3 = binding.get("A3");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			if (!(position3 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule22");}
			SequentSinglePosition pos3 = (SequentSinglePosition) position3;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			Object formula3 = antecedent.get(pos3.getToplevelPosition());
			// System.out.println(formula1);
			// System.out.println(formula2);
			// System.out.println(formula3);
			if (formula1 instanceof OWLSubClassOfAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					&& formula3 instanceof OWLSubClassOfAxiom
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom4 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),axiom2.getSuperClass());
				if (!antecedent.contains(axiom4)){
					s.addAntecedent(axiom4);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
				
	}, // END RULE24
	*/
	
/*
RULE25{  // SubClaOf(-X,Y) --> SubClaOf(\top,Union(X,Y)) 
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule25";};
		public java.lang.String getShortName(){return "R25";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					OWLClassExpression superCl = axiom1.getSuperClass();
					OWLClassExpression subCl = axiom1.getSubClass();
					if (subCl instanceof OWLObjectComplementOf){
							RuleBinding binding = new RuleBinding();
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
							binding.insertPosition("A1", position1);
							results.add(binding);
				
					}
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				Object formulaSup = axiom1.getSuperClass();
				Object formulaSub = axiom1.getSubClass();
				if (formulaSub instanceof OWLObjectComplementOf && formulaSup instanceof OWLClassExpression){
					OWLObjectComplementOf axiomSub = (OWLObjectComplementOf) formulaSub;
					OWLClassExpression axiomSup = (OWLClassExpression) formulaSup;
					OWLClassExpression exprX = axiomSub.getOperand();
					OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
					OWLDataFactory dataFactory=manager.getOWLDataFactory();
					OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(),dataFactory.getOWLObjectUnionOf(exprX,axiomSup));
					if (!antecedent.contains(axiom3)){
						s.addAntecedent(axiom3);	
					    newsequents.add(s);
					}
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE25
	*/
	
/*
RULE26{ //SubCla(X,Union(Y,Z)) and SubCla(Y,Z) --> SubCla(X,Z)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule26";};
		public java.lang.String getShortName(){return "R26";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom //SubCla(X,Union(Y,Z))
						&& ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectUnionOf
						){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					OWLObjectUnionOf union = (OWLObjectUnionOf) ((OWLSubClassOfAxiom) formula).getSuperClass();
					List<OWLClassExpression> unionlist = union.getOperandsAsList();
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom && count1 != count2
								&& ( ((OWLSubClassOfAxiom) formula2).getSubClass().equals(unionlist.get(0))
								&& ((OWLSubClassOfAxiom) formula2).getSuperClass().equals(unionlist.get(1))
								||  ( ((OWLSubClassOfAxiom) formula2).getSubClass().equals(unionlist.get(1))
									&& ((OWLSubClassOfAxiom) formula2).getSuperClass().equals(unionlist.get(0)) ))
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}						
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule15");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule15");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					&& (((OWLSubClassOfAxiom) formula1).getSuperClass()) instanceof OWLObjectUnionOf
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),axiom2.getSuperClass());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE26
	*/
	
/*
	RULE27{ //SubClass(\exists r. X, Y) and SubClass(\forall r.\bot, Y) --> Subclass(\forall r.X, Y)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule27";};
		public java.lang.String getShortName(){return "R27";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom //SubClass(\exists r. X, Y)
						&& ((OWLSubClassOfAxiom) formula).getSubClass() instanceof OWLObjectSomeValuesFrom
						){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					// System.out.println(axiom1);
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom && count1 != count2
								&& ((OWLSubClassOfAxiom) formula2).getSubClass() instanceof OWLObjectAllValuesFrom
								&& ((OWLObjectAllValuesFrom) ((OWLSubClassOfAxiom) formula2).getSubClass()).getFiller().isOWLNothing()
								&& ((OWLObjectAllValuesFrom) ((OWLSubClassOfAxiom) formula2).getSubClass()).getProperty().equals( ((OWLObjectSomeValuesFrom) (((OWLSubClassOfAxiom) formula).getSubClass())).getProperty())
								&& ((OWLSubClassOfAxiom) formula2).getSuperClass().equals(((OWLSubClassOfAxiom) formula).getSuperClass())
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}						
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule27");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule27");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					&& (((OWLSubClassOfAxiom) formula1).getSubClass()) instanceof OWLObjectSomeValuesFrom
					&& (((OWLSubClassOfAxiom) formula2).getSubClass()) instanceof OWLObjectAllValuesFrom
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				OWLObjectPropertyExpression property = ((OWLObjectSomeValuesFrom) axiom1.getSubClass()).getProperty();
				OWLClassExpression xclass = ((OWLObjectSomeValuesFrom) axiom1.getSubClass()).getFiller();
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLObjectAllValuesFrom(property,xclass),axiom1.getSuperClass());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE27
	*/
	
/*
RULE28{ // ObjePropDomain(r,X) and SymObjProp(r) --> ObjPropRange(r,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule28";};
		public java.lang.String getShortName(){return "R28";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyDomainAxiom // ObjectPropertyDomain(r,X)
						){ 
					OWLObjectPropertyDomainAxiom axiom1 = ((OWLObjectPropertyDomainAxiom) formula);
					// System.out.println(axiom1);
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSymmetricObjectPropertyAxiom
								&& ((OWLSymmetricObjectPropertyAxiom) formula2).getProperty().equals(axiom1.getProperty()) 
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}						
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyDomainAxiom 
					&& formula2 instanceof OWLSymmetricObjectPropertyAxiom
					&& ((OWLSymmetricObjectPropertyAxiom) formula2).getProperty().equals( ((OWLObjectPropertyDomainAxiom) formula1).getProperty())
					){
				OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLObjectPropertyRangeAxiom axiom3 = dataFactory.getOWLObjectPropertyRangeAxiom(axiom1.getProperty(),axiom1.getDomain());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE28
	*/
	
/*
RULE29{ // SubCla(X, \exists r. \exists r, Y) and TransProp(r) --> SubCla(X,\exists r.Y)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule29";};
		public java.lang.String getShortName(){return "R29";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom // SubCla(X, \exists r. \exists r, Y) 
						&& ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectSomeValuesFrom
						&& ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula).getSuperClass()).getFiller() instanceof OWLObjectSomeValuesFrom 
						&& ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula).getSuperClass()).getProperty().equals( ((OWLObjectSomeValuesFrom)  ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula).getSuperClass()).getFiller()).getProperty() )
						){ 
					OWLSubClassOfAxiom axiom1 = ((OWLSubClassOfAxiom) formula);
					OWLObjectSomeValuesFrom expr1 = (OWLObjectSomeValuesFrom) axiom1.getSuperClass();
					OWLObjectSomeValuesFrom expr2 = (OWLObjectSomeValuesFrom) expr1.getFiller();
					// System.out.println(axiom1);
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLTransitiveObjectPropertyAxiom
								&& ((OWLTransitiveObjectPropertyAxiom) formula2).getProperty().equals(expr1.getProperty()) 
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}						
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule18");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom 
					&& formula2 instanceof OWLTransitiveObjectPropertyAxiom
					&& ((OWLSubClassOfAxiom) formula1).getSuperClass() instanceof OWLObjectSomeValuesFrom
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula1).getSuperClass();
				OWLObjectSomeValuesFrom some2 =  (OWLObjectSomeValuesFrom)  some.getFiller();
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLObjectSomeValuesFrom(some.getProperty(),some2.getFiller()));
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE29
	*/
	
/*
RULE30{ // Rng(r,Z) and SubCla(X,\exists r.Y)  -> SubCla(X,\exists r. Int(Y,Z))
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule30";};
		public java.lang.String getShortName(){return "R30";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyRangeAxiom // Rng(r,Z)
						){ 
					OWLObjectPropertyRangeAxiom axiom1 = ((OWLObjectPropertyRangeAxiom) formula);
					int count2= 0;
					for (Object formula2: antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom // SubCla(X,\exists r.Y)
						&&  ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLObjectSomeValuesFrom
						&&  ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getProperty().equals(axiom1.getProperty())
								){ 
									RuleBinding binding = new RuleBinding();
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
									binding.insertPosition("A2", position2);
									results.add(binding);
						}
						count2++;
						} // end loop A2
				} // endif A1
				count1++; 
			} // end loop A1
						
			return results;
			}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule23");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyRangeAxiom
					&& formula2 instanceof OWLSubClassOfAxiom
					&&  ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLObjectSomeValuesFrom
					){
				OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) axiom2.getSuperClass();
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom4 = dataFactory.getOWLSubClassOfAxiom(axiom2.getSubClass(),dataFactory.getOWLObjectSomeValuesFrom(axiom1.getProperty(),dataFactory.getOWLObjectIntersectionOf(some.getFiller(),axiom1.getRange())));
				if (!antecedent.contains(axiom4)){
					s.addAntecedent(axiom4);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
				
	}, // END RULE30
	*/


/*	
RULE31{ // SubCla(\top,Y) and disjoint(X,Y) --> SubCla(X,\bot)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule31";};
		public java.lang.String getShortName(){return "R31";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom
						&& ((OWLSubClassOfAxiom) formula).getSubClass().isOWLThing()
						){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					// we have a subclass axiom, now need to find matching disjointness axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLDisjointClassesAxiom
								&& ( ((OWLDisjointClassesAxiom) formula2).getClassExpressionsAsList().get(0).equals(axiom1.getSuperClass())
								||  ((OWLDisjointClassesAxiom) formula2).getClassExpressionsAsList().get(1).equals(axiom1.getSuperClass())
								)
										){							
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula2 instanceof OWLDisjointClassesAxiom && formula1 instanceof OWLSubClassOfAxiom){
				OWLDisjointClassesAxiom axiom2 = (OWLDisjointClassesAxiom) formula2;
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLClassExpression expr;
				if (axiom2.getClassExpressionsAsList().get(0).equals(axiom1.getSuperClass())){
					expr = axiom2.getClassExpressionsAsList().get(1);
				}
				else {
					expr = axiom2.getClassExpressionsAsList().get(0);
				}
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(expr,dataFactory.getOWLNothing());
				// System.out.println(axiom3);
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE31
	*/
	
/*
RULE32{  // SubCla(X,=n1.r.Y) --> SubCla(X,>=n2.r.Y) with n2<=n1
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule32";};
		public java.lang.String getShortName(){return "R32";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom
						&& ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectExactCardinality
						)
				{
							RuleBinding binding = new RuleBinding();
							SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
							binding.insertPosition("A1", position1);
							results.add(binding);
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom
					&& ((OWLSubClassOfAxiom) formula1).getSuperClass() instanceof OWLObjectExactCardinality
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				Object formulaSup = axiom1.getSuperClass();
				Object formulaSub = axiom1.getSubClass();
				OWLObjectExactCardinality card = (OWLObjectExactCardinality) formulaSup;
				for (int i = 0; card.getCardinality() >i; i++){
					OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
					OWLDataFactory dataFactory=manager.getOWLDataFactory();
					OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLObjectMinCardinality(i+1,card.getProperty(),card.getFiller()));
					if (!antecedent.contains(axiom3)){
						s.addAntecedent(axiom3);	
					}
				}
				newsequents.add(s);
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE32
	*/
	
/*
RULE33{ // PropDom(r,X) and SubProp(r1,r) --> PropDom(r1,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule33";};
		public java.lang.String getShortName(){return "R33";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyDomainAxiom){ // PropDom(r,X)
					OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula;
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubObjectPropertyOfAxiom // SubProp(r1,r)
								&& ((OWLSubObjectPropertyOfAxiom) formula2).getSuperProperty().equals(axiom1.getProperty())
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyDomainAxiom && formula2 instanceof OWLSubObjectPropertyOfAxiom ){
				OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
				OWLSubObjectPropertyOfAxiom axiom2 = (OWLSubObjectPropertyOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLObjectPropertyDomainAxiom axiom3 = dataFactory.getOWLObjectPropertyDomainAxiom(axiom2.getSubProperty(),axiom1.getDomain());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE33
	*/
	
RULE34{ // SubCla(X,Y) and disjoint(X,Y) --> SubCla(X,\bot)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule34";};
		@Override
		public java.lang.String getShortName(){return "R34";};
		
		// OWLFormula.createFormulaTop()
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.DISJ, 
				OWLFormula.createFormulaVar("v1"),
				OWLFormula.createFormulaVar("v2")
				);
		
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v3"),
				OWLFormula.createFormulaVar("v4")
				);
		
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// System.out.println("find rule bindings for rule 34 called");
			// look up disjointness axiom first. Reason: more specific (efficiency). Important (!): want to handle case where class is disjoint with \top without using an exiom saying that \top subclassof \top
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			Set<OWLFormula> disjCandidates = new HashSet<OWLFormula>(s.findMatchingFormulasInAntecedent(prem1));
			Set<OWLFormula> subclCandidates = new HashSet<OWLFormula>(s.findMatchingFormulasInAntecedent(prem2));
			// System.out.println(" rule 34 debug: " + candidates);
			for (OWLFormula subclCand : subclCandidates){
				if (subclCand.getArgs().get(0).equals(subclCand.getArgs().get(1))){
					continue; // <--- tautologies lead to incorrect results!
				}
				for (OWLFormula disjCand : disjCandidates){
						List<OWLFormula> disjmembers = disjCand.getArgs();
						boolean exists1 = false;
						boolean exists2 = false;
						for (OWLFormula member : disjmembers){
							if (member.equals(subclCand.getArgs().get(0)))
								exists1 = true;
							if (member.equals(subclCand.getArgs().get(1)))
								exists2 = true;
						}			
						if (exists1 && exists2){
							// build conclusion formula
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, subclCand.getArgs().get(0),OWLFormula.createFormulaBot());
							// System.out.println("building conclusion R34 " + conclusion);
								if (!s.alreadyContainedInAntecedent(conclusion)){
									RuleBinding binding = new RuleBinding(conclusion,null);
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(subclCand));
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(disjCand));
									binding.insertPosition("A2", position2);
									results.add(binding);
								}	
						}
				}
			}
			// System.out.println("Rule 34 results " + results);
			return results;
		}
			
		
		
			
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			// System.out.println("CONCL R34 : " + conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
			
		}
		
		
	}, // END RULE34

RULE34nary{ // SubCla(X,Y) and disjoint(X,Y) --> SubCla(X,\bot)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule34n";};
		@Override
		public java.lang.String getShortName(){return "R34n";};
		
		// OWLFormula.createFormulaTop()
		
		/*
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.DISJ, 
				OWLFormula.createFormulaVar("v1"),
				OWLFormula.createFormulaVar("v2")
				);
		
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v3"),
				OWLFormula.createFormulaVar("v4")
				);
				*/
		
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// System.out.println("find rule bindings for rule 34n called");
			// look up disjointness axiom first. Reason: more specific (efficiency). Important (!): want to handle case where class is disjoint with \top without using an exiom saying that \top subclassof \top
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			HashSet<OWLFormula> antecedentFormulas = s.getAllAntecedentOWLFormulas();
			for (OWLFormula ant1 : antecedentFormulas){
				if (ant1.getHead().equals(OWLSymb.DISJ)){
					List<OWLFormula> disjuncts = ant1.getArgs();
					// System.out.println("disjuncts " + disjuncts);
					// now find subclass axioms where both elements are in the disjunction
					for (OWLFormula subclCand : antecedentFormulas){
						if (!subclCand.getHead().equals(OWLSymb.SUBCL))
							continue;
						if (subclCand.getArgs().get(0).equals(subclCand.getArgs().get(1))){
							continue; // <--- tautologies lead to incorrect results!
						}
						// System.out.println("dealing with subcl axiom: " + subclCand.getArgs().get(0) + " "+ subclCand.getArgs().get(1));
						if (disjuncts.contains(subclCand.getArgs().get(0)) 
								&& disjuncts.contains(subclCand.getArgs().get(1)) 
								){
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, subclCand.getArgs().get(0),OWLFormula.createFormulaBot());
						    // System.out.println("building conclusion R34 " + conclusion);
								if (!s.alreadyContainedInAntecedent(conclusion)){
									RuleBinding binding = new RuleBinding(conclusion,null);
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(subclCand));
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(ant1));
									binding.insertPosition("A2", position2);
									results.add(binding);
								}
						}	
						
					}
				}
			}
			
			
			// System.out.println("Rule 34n results " + results);
			return results;
		}
			
		
		
			
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			// System.out.println("CONCL R34 : " + conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
			
		}
		
		
	}, // END RULE34


RULE34semiold{ // SubCla(X,Y) and disjoint(X,Y) --> SubCla(X,\bot)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule34";};
		@Override
		public java.lang.String getShortName(){return "R34";};
		
		// OWLFormula.createFormulaTop()
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.DISJ, 
				OWLFormula.createFormulaVar("v1"),
				OWLFormula.createFormulaVar("v2")
				);
		
		private final OWLFormula prem2a = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"),
				OWLFormula.createFormulaVar("v2")
				);
		
		private final OWLFormula prem2b = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v2"),
				OWLFormula.createFormulaVar("v1")
				);
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// System.out.println("find rule bindings for rule 34 called");
			// look up disjointness axiom first. Reason: more specific (efficiency). Important (!): want to handle case where class is disjoint with \top without using an exiom saying that \top subclassof \top
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			Set<OWLFormula> candidates = new HashSet<OWLFormula>(s.findMatchingFormulasInAntecedent(prem1));
			// System.out.println(" rule 34 debug: " + candidates);
			for (OWLFormula cand: candidates){
				// special case
				if (cand.getArgs().get(1).isTop()){
					OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, cand.getArgs().get(0),OWLFormula.createFormulaBot());
					if (!s.alreadyContainedInAntecedent(conclusion)){
						RuleBinding binding = new RuleBinding(conclusion,null);
						SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand));
						binding.insertPosition("A1", position1);
						results.add(binding);
					}
				}
				// end special case
				List<Pair<OWLFormula, OWLFormula>> matcher = null;
				try {
					matcher = cand.match(prem1);
				} catch (Exception e) {
					//  Auto-generated catch block
					e.printStackTrace();
				}
				OWLFormula prem_2a = prem2a.applyMatcher(matcher);
				// System.out.println("prem_2a " + prem_2a);
				// now search for this formula in sequent
				List<OWLFormula>  candidates2a = s.findMatchingFormulasInAntecedent(prem_2a);
				// 
				OWLFormula prem_2b = prem2b.applyMatcher(matcher);
				// System.out.println("prem_2b " + prem_2b);
				// now search for this formula in sequent
				List<OWLFormula> candidates2b = s.findMatchingFormulasInAntecedent(prem_2b);
				candidates2b.addAll(candidates2a);
				// System.out.println("candidates2 " + candidates2b);
				for (OWLFormula cand2: candidates2b){
					// build conclusion formula
					OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, cand.getArgs().get(0),OWLFormula.createFormulaBot());
					System.out.println("building conclusion R34 " + conclusion);
					if (!s.alreadyContainedInAntecedent(conclusion)){
						RuleBinding binding = new RuleBinding(conclusion,null);
						SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand));
						binding.insertPosition("A1", position1);
						SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(cand2));
						binding.insertPosition("A2", position2);
						results.add(binding);
					}
				}
			}
			System.out.println("Rule 34 results " + results);
			return results;
		}
			
		
		
			
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			System.out.println("CONCL R34 : " + conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
			
		}
		
		
	}, // END RULE34


RULE35{ // SubCla(X,Y) and SubCla(X,Z) and disjoint(Y,Z) --> SubCla(X,\bot)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule35";};
		@Override
		public java.lang.String getShortName(){return "R35";};
		
		// OWLFormula.createFormulaTop()
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.DISJ, 
				OWLFormula.createFormulaVar("v1"),
				OWLFormula.createFormulaVar("v2")
				);
		
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v3"),
				OWLFormula.createFormulaVar("v4")
				);
		
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// System.out.println("find rule bindings for rule 35 called");
			// look up disjointness axiom first. Reason: more specific (efficiency). Important (!): want to handle case where class is disjoint with \top without using an exiom saying that \top subclassof \top
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			Set<OWLFormula> disjCandidates = new HashSet<OWLFormula>(s.findMatchingFormulasInAntecedent(prem1));
			Set<OWLFormula> subclCandidates = new HashSet<OWLFormula>(s.findMatchingFormulasInAntecedent(prem2));
			// System.out.println(" rule 35 debug: " + candidates);
			
			// make sure we save time if there are no disjointness axioms
			if (disjCandidates.size()==0){
				return results;
			}
			
			// create buckets with the same heads...
			
			HashMap<OWLFormula,Set<OWLFormula>> buckets = new HashMap<OWLFormula,Set<OWLFormula>>();
			for (OWLFormula subclCand : subclCandidates){
				if (subclCand.getHead().isSymb() && subclCand.getHead().equals(OWLSymb.BOT))
					continue; // <-- this would lead to wrong results!
				if (buckets.get(subclCand.getArgs().get(0))!=null){
					Set<OWLFormula> set = buckets.get(subclCand.getArgs().get(0));
					set.add(subclCand);
					// buckets.put(subclCand.getArgs().get(0),subclCand);
				} else {
					Set<OWLFormula> newset = new HashSet<OWLFormula>();
					newset.add(subclCand);
					buckets.put(subclCand.getArgs().get(0),newset);
				}
			}
			
			Set<OWLFormula> keys = buckets.keySet();
			
			for (OWLFormula cand :keys){
				for (OWLFormula disjCand : disjCandidates){
					// System.out.println("rule 35 checking disjointness axiom " + disjCand.prettyPrint());
					List<OWLFormula> disjmembers = disjCand.getArgs();
					
					// check
					int i = disjmembers.size();
					List<OWLFormula> foundforms = new ArrayList<OWLFormula>();
					for (OWLFormula disjmemb : disjmembers){
						
						for (OWLFormula inspectform : buckets.get(cand)){
							// System.out.println("rule 35 checking inspectform " + inspectform.prettyPrint());
							if (inspectform.getArgs().get(1).equals(disjmemb)){
								i--;
								foundforms.add(inspectform);
							}
						}
						} // end loop for disj members
					
						if (i==0){
							// System.out.println("foundforms " + foundforms.toString());
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, foundforms.get(0).getArgs().get(0),OWLFormula.createFormulaBot());
							
							if (!s.alreadyContainedInAntecedent(conclusion)){
								RuleBinding binding = new RuleBinding(conclusion,null);
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(disjCand)); // <-- disjointness axiom
								binding.insertPosition("A1", position1);
								for (int j = 0; j < foundforms.size();j++){
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(foundforms.get(j)));
									binding.insertPosition("A" + (j + 2), position2);
								}
								results.add(binding);
							}	
							
						}
					
					}
			} // <--- keyset loop
					
					
			// System.out.println("Rule 34 results " + results);
			return results;
		}
			
		
		
			
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			// System.out.println("CONCL R34 : " + conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
			
		}
		
		
	}, // END RULE35

RULE35nary{ // SubCla(X,Y) and SubCla(X,Z) and disjoint(Y,Z) --> SubCla(X,\bot)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule35";};
		@Override
		public java.lang.String getShortName(){return "R35";};
		
		// OWLFormula.createFormulaTop()
		
		/*
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.DISJ, 
				OWLFormula.createFormulaVar("v1"),
				OWLFormula.createFormulaVar("v2")
				);
		*/
		
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v3"),
				OWLFormula.createFormulaVar("v4")
				);
		
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// System.out.println("find rule bindings for rule 35 called");
			// look up disjointness axiom first. Reason: more specific (efficiency). Important (!): want to handle case where class is disjoint with \top without using an exiom saying that \top subclassof \top
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			// Set<OWLFormula> disjCandidates = new HashSet<OWLFormula>(s.findMatchingFormulasInAntecedent(prem1));
			Set<OWLFormula> allFormulasCandidates = s.getAllAntecedentOWLFormulas();
			Set<OWLFormula> disjCandidates = new HashSet<OWLFormula>();
			for (OWLFormula dform : allFormulasCandidates){
				if (dform.getHead().equals(OWLSymb.DISJ))
					disjCandidates.add(dform);
			}
			
			// make sure we save time if there are no disjointness axioms
						if (disjCandidates.size()==0){
							return results;
						}
			
			Set<OWLFormula> subclCandidates = new HashSet<OWLFormula>(s.findMatchingFormulasInAntecedent(prem2));
			// System.out.println(" rule 35 debug: " + candidates);
			
			
			// create buckets with the same heads...
			
			HashMap<OWLFormula,Set<OWLFormula>> buckets = new HashMap<OWLFormula,Set<OWLFormula>>();
			for (OWLFormula subclCand : subclCandidates){
				if (subclCand.getHead().isSymb() && subclCand.getHead().equals(OWLSymb.BOT))
					continue; // <-- this would lead to wrong results!
				if (buckets.get(subclCand.getArgs().get(0))!=null){
					Set<OWLFormula> set = buckets.get(subclCand.getArgs().get(0));
					set.add(subclCand);
					// buckets.put(subclCand.getArgs().get(0),subclCand);
				} else {
					Set<OWLFormula> newset = new HashSet<OWLFormula>();
					newset.add(subclCand);
					buckets.put(subclCand.getArgs().get(0),newset);
				}
			}
			
			Set<OWLFormula> keys = buckets.keySet();
			
			for (OWLFormula cand :keys){
				for (OWLFormula disjCand : disjCandidates){
					// System.out.println("rule 35 checking disjointness axiom " + disjCand.prettyPrint());
					List<OWLFormula> disjmembers = disjCand.getArgs();
					
					// check
					int i = disjmembers.size();
					List<OWLFormula> foundforms = new ArrayList<OWLFormula>();
					for (OWLFormula disjmemb : disjmembers){
						
						for (OWLFormula inspectform : buckets.get(cand)){
							// System.out.println("rule 35 checking inspectform " + inspectform.prettyPrint());
							if (inspectform.getArgs().get(1).equals(disjmemb)){
								i--;
								foundforms.add(inspectform);
							}
						}
						} // end loop for disj members
					
						if (i==disjmembers.size()-2){
							// System.out.println("foundforms " + foundforms.toString());
							OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, foundforms.get(0).getArgs().get(0),OWLFormula.createFormulaBot());
							
							if (!s.alreadyContainedInAntecedent(conclusion)){
								RuleBinding binding = new RuleBinding(conclusion,null);
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(disjCand)); // <-- disjointness axiom
								binding.insertPosition("A1", position1);
								for (int j = 0; j < foundforms.size();j++){
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(foundforms.get(j)));
									binding.insertPosition("A" + (j + 2), position2);
								}
								results.add(binding);
							}	
							
						}
					
					}
			} // <--- keyset loop
					
					
			// System.out.println("Rule 34 results " + results);
			return results;
		}
			
		
		
			
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			// OWLSubClassOfAxiom axiom3;
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			// System.out.println("CONCL R34 : " + conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
			
		}
		
		
	}, // END RULE35nary




/*	
RULE35{ // SubCla(X,Y) and SubCla(X,Z) and Disj(Y,Z) -> SubCla(X,\bot)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule35";};
		public java.lang.String getShortName(){return "R35";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom // SubCla(X,Y)
						){ 
					OWLSubClassOfAxiom axiom1 = ((OWLSubClassOfAxiom) formula);
					int count2= 0;
					for (Object formula2: antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom // SubCla(X,Z)
						&&  ((OWLSubClassOfAxiom) formula2).getSubClass().equals(axiom1.getSubClass())
								){ 
							OWLSubClassOfAxiom axiom2 = ((OWLSubClassOfAxiom) formula2);
							int count3=0;
							for (Object formula3: antecedent){
							if (formula3 instanceof OWLDisjointClassesAxiom){ // Disj(Y.Z)
								OWLDisjointClassesAxiom axiom3 = (OWLDisjointClassesAxiom) formula3;
								List<OWLClassExpression> exprs = axiom3.getClassExpressionsAsList();
								if( (exprs.get(0).equals(axiom1.getSuperClass())
										&& exprs.get(1).equals(axiom2.getSuperClass()))
										||
										(exprs.get(1).equals(axiom1.getSuperClass())
												&& exprs.get(0).equals(axiom2.getSuperClass()))
										){
									RuleBinding binding = new RuleBinding();
									SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
									binding.insertPosition("A1", position1);
									SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
									binding.insertPosition("A2", position2);
									SequentPosition position3 = new SequentSinglePosition(SequentPart.ANTECEDENT, count3);
									binding.insertPosition("A3", position3);
									results.add(binding);}	// endif A3	
							}
							count3++;
						} // end loop A3
						} // endif A2
						count2++;
					} // end loop A2
				} // endif A1
				count1++; 
			} // end loop A1
						
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			SequentPosition position3 = binding.get("A3");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule35");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule35");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			if (!(position3 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule35");}
			SequentSinglePosition pos3 = (SequentSinglePosition) position3;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			Object formula3 = antecedent.get(pos3.getToplevelPosition());
			// System.out.println(formula1);
			// System.out.println(formula2);
			// System.out.println(formula3);
			if (formula1 instanceof OWLSubClassOfAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					&& formula3 instanceof OWLDisjointClassesAxiom
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom4 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLNothing());
				if (!antecedent.contains(axiom4)){
					s.addAntecedent(axiom4);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
				
	}, // END RULE35
	*/
	
/*
RULE36{ // trans(r0) and inv(r0,r1) --> trans(r1)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule36";};
		public java.lang.String getShortName(){return "R36";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLTransitiveObjectPropertyAxiom
						){
					OWLTransitiveObjectPropertyAxiom axiom1 = (OWLTransitiveObjectPropertyAxiom) formula;
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLInverseObjectPropertiesAxiom
								&&  ((OWLInverseObjectPropertiesAxiom) formula2).getFirstProperty().equals(axiom1.getProperty())
										){							
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLTransitiveObjectPropertyAxiom && formula2 instanceof OWLInverseObjectPropertiesAxiom){
				OWLInverseObjectPropertiesAxiom axiom2 = (OWLInverseObjectPropertiesAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLTransitiveObjectPropertyAxiom axiom3 = dataFactory.getOWLTransitiveObjectPropertyAxiom(axiom2.getSecondProperty());
				// System.out.println(axiom3);
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE36
	*/
	
RULE37{ // SubCla(X,\exists r0.Y)  and SubProp(r0,r1) --> SubCla(X,\exists r1.Y)
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule37";};
		@Override
		public java.lang.String getShortName(){return "R37";};
		
		private final OWLFormula formulaSubProp = OWLFormula.createFormula(OWLSymb.SUBPROPERTYOF, 
				OWLFormula.createFormulaRoleVar("r1")  , OWLFormula.createFormulaRoleVar("r2"));
		private final OWLFormula existsForm = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"),
				OWLFormula.createFormulaExists(
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v2")));
		
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(formulaSubProp);
			for (int i = 0 ; i < candidates.size(); i++){
				// List<OWLFormula> candidates2 = new ArrayList<OWLFormula>();
				try{
					// System.out.println("candidate + " + candidates.get(i));
					List<Pair<OWLFormula,OWLFormula>> matcher = candidates.get(i).match(formulaSubProp);
					OWLFormula candidate2form = existsForm.applyMatcher(matcher);
					List<OWLFormula> candidates2 = s.findMatchingFormulasInAntecedent(candidate2form);
					for (OWLFormula candidate2: candidates2){
						OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, 
								candidate2.getArgs().get(0),
								OWLFormula.createFormulaExists(
										candidates.get(i).getArgs().get(1),
										candidate2.getArgs().get(1).getArgs().get(1)));
						if (!s.alreadyContainedInAntecedent(conclusion)){
							 List<Integer> list = new ArrayList<Integer>();
							 // System.out.println(candidate);
							 //  System.out.println(s.antecedentFormulaGetID(candidate));
							 // System.out.println(s.antecedentGetFormula(s.antecedentFormulaGetID(candidate)));
							 list.add(s.antecedentFormulaGetID(candidates.get(i)));
							 SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, list);
							 List<Integer> list2 = new ArrayList<Integer>();
							 list2.add(s.antecedentFormulaGetID(candidate2));
							 SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, list2);
							 RuleBinding binding = new RuleBinding(conclusion, null);
							 binding.insertPosition("A2", position2);
							 results.add(binding);
						} // end if
					} // for loop candidates2
				} // end try
				catch(Exception e){}
			}	
			return results;
		}
		
		
		@Override
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 1 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			OWLFormula conclusion = binding.getNewAntecedent();
			if (conclusion !=null){
				if (!sequent.alreadyContainedInAntecedent(conclusion)){
					RuleApplicationResults result = new RuleApplicationResults();
					result.setOriginalFormula(sequent);
					result.addAddition("A1",conclusion);
					results.add(result);
					result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
				}
			}
			else {
				System.out.println("Something bad happened!");
			
			}
			return results;
		}
		
		
	}, // END RULE37



/*	
RULE38{ // range(r1,X)  and SubProp(r0,r1) --> Range(r0,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule38";};
		public java.lang.String getShortName(){return "R38";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyRangeAxiom
						){ //range(r1,X)
					OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula;
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubObjectPropertyOfAxiom // 
								&& ((OWLSubObjectPropertyOfAxiom) formula2).getSuperProperty().equals(axiom1.getProperty())
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyRangeAxiom 
					&& formula2 instanceof OWLSubObjectPropertyOfAxiom
					){
				OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula1;
				OWLSubObjectPropertyOfAxiom axiom2 = (OWLSubObjectPropertyOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLObjectPropertyRangeAxiom axiom3 = dataFactory.getOWLObjectPropertyRangeAxiom(axiom2.getSubProperty(),axiom1.getRange());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE38
	
RULE39{ // SubCla(X,Y) and SubCla(X,-Y) --> SubCla(X,\bot)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule39";};
		public java.lang.String getShortName(){return "R39";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom // SubCla(X,-Y) 
								&& ((OWLSubClassOfAxiom) formula2).getSubClass().equals(axiom1.getSubClass())
								&& ((OWLSubClassOfAxiom) formula2).getSuperClass().equals(axiom1.getSuperClass().getObjectComplementOf())
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule39");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule39");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom && formula2 instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLNothing());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE39
	
RULE40{ // SubCla(X,\exists r. Int(Y,Z,...)) and disjoint(Y,Z) --> SubCla(X,\bot)
		
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule40";};
		public java.lang.String getShortName(){return "R40";};
		
		
		// TODO!: Implement the "..."
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){ // SubCla(X,\exists r. Int(Y,Z))
				if (formula instanceof OWLSubClassOfAxiom
				    && ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectSomeValuesFrom
				    && ((OWLObjectSomeValuesFrom) (((OWLSubClassOfAxiom) formula).getSuperClass())).getFiller() instanceof OWLObjectIntersectionOf
						){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) axiom1.getSuperClass();
					OWLObjectIntersectionOf inter1 = (OWLObjectIntersectionOf) some1.getFiller();
					// we have a subclass axiom, now need to find matching disjointness axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLDisjointClassesAxiom){ //disjoint(Y,Z)
							OWLDisjointClassesAxiom axiom2 = (OWLDisjointClassesAxiom) formula2;
							List<OWLClassExpression> exprs = axiom2.getClassExpressionsAsList();
							if ( (exprs.get(0).equals(inter1.getOperandsAsList().get(0)) && (exprs.get(1).equals(inter1.getOperandsAsList().get(1))))
									|| (exprs.get(1).equals(inter1.getOperandsAsList().get(0)) && (exprs.get(0).equals(inter1.getOperandsAsList().get(1))))
									){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
										}
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule40");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule40");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula2 instanceof OWLDisjointClassesAxiom && formula1 instanceof OWLSubClassOfAxiom){
				OWLDisjointClassesAxiom axiom2 = (OWLDisjointClassesAxiom) formula2;
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLNothing());
				// System.out.println(axiom3);
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE40
	
	RULE41{ //SubClass(X,>=n1.r0.\top) and SubClass(X,=<n2. r0.\top) --> Subclass(X,\bot) with n1>n2>0
		
		// Every jermlaine posesses at least three things
		// every jermlaine posesses at most one thing
		// Nothing is a jermlaine
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule41";};
		public java.lang.String getShortName(){return "R41";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom //SubClass(X,>=n1.r0.\top)
						&& ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectMinCardinality
						){
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					OWLObjectMinCardinality card1 = (OWLObjectMinCardinality) axiom1.getSuperClass();
					// we have a subclass axiom, now need to find matching second subclass axiom
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLSubClassOfAxiom && count1 != count2
								&& ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLObjectMaxCardinality
								&& ((OWLObjectMaxCardinality) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getCardinality() < card1.getCardinality()
								&& ((OWLObjectMaxCardinality) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getProperty().equals(card1.getProperty())
								&& ((OWLObjectMaxCardinality) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getFiller().equals(card1.getFiller())
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);}						
						count2++;
						} //end inner loop	
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule41");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule41");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom 
					&& formula2 instanceof OWLSubClassOfAxiom
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom1.getSubClass(),dataFactory.getOWLNothing());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE41
	*/
	
RULE42{ // SubCla(X,\exists r. Y) and SubCla(Y,\bot) --> SubCla(X,\bot)
		
		private final OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v1"), 
				OWLFormula.createFormula(OWLSymb.EXISTS, 
						OWLFormula.createFormulaRoleVar("r1"), 
						OWLFormula.createFormulaVar("v2")));
		private final OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, 
				OWLFormula.createFormulaVar("v2"), 
				OWLFormula.createFormulaVar("v3"));
		
		@Override
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule42";};
		@Override
		public java.lang.String getShortName(){return "R42";};
		
		@Override
		public List<RuleBinding> findRuleBindings(Sequent s){
			// System.out.println("42 rule binding called");
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			List<OWLFormula> candidates = s.findMatchingFormulasInAntecedent(prem1);
			for (int i =0; i<candidates.size();i++){
			try{
				List<Pair<OWLFormula,OWLFormula>> matcher = candidates.get(i).match(prem1);
				// System.out.println("prem 2 " + prem2 +  " Matcher: " + matcher);
				// now build new formula using the matcher
				OWLFormula prem_2 = prem2.applyMatcher(matcher);
				// System.out.println("prem_2 " + prem_2);
				// now search for this formula in sequent
				List<OWLFormula> candidates2 = s.findMatchingFormulasInAntecedent(prem_2);
				for (OWLFormula candidate2 : candidates2){
					if (candidate2.getArgs().get(1).equals(OWLFormula.createFormulaBot())){
					OWLFormula conclusion = OWLFormula.createFormula(OWLSymb.SUBCL, candidates.get(i).getArgs().get(0),OWLFormula.createFormulaBot());
					if (!s.alreadyContainedInAntecedent(conclusion)){
						// System.out.println("Rule 42 creating conclusion " + conclusion);
						RuleBinding binding = new RuleBinding(conclusion,null);
						SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidates.get(i)));
						binding.insertPosition("A1", position1);
						SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, s.antecedentFormulaGetID(candidate2));
						binding.insertPosition("A2", position2);
						results.add(binding);
					}
					}
				}} catch(Exception e){}
			
			
			} // end outer for loop
			return results;
		}
		
		
       @Override
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			
			List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
			List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
			// System.out.println("Rule 6 neo DEBUG sequents " + sequents);
			return SequentList.makeANDSequentList(sequents);
		}
		
		@Override
		public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			 List<RuleApplicationResults> results = new ArrayList<RuleApplicationResults>();
			OWLFormula conclusionformula = null;
			// System.out.println("DEBUG -- newantecedent " + binding.getNewAntecedent());
			if (binding.getNewAntecedent()!=null){ // fast track
				// axiom3 = (OWLSubClassOfAxiom) binding.getNewAntecedent().toOWLAPI();
				conclusionformula = binding.getNewAntecedent();
			} 
			RuleApplicationResults result = new RuleApplicationResults();
			result.setOriginalFormula(sequent);
			result.addAddition("A1",conclusionformula);
			// System.out.println("CONCL R42 : " + conclusionformula);
			results.add(result);
			result.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			return results;
			
		}
		
		
	}, // END RULE42
	


/*
	
RULE43{ //FunctionalDataProp(d) and SubCla(X,DataMinCard(n,d,DR0) and n>1 --> SubCla(X,\bot)
		
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule43";};
		public java.lang.String getShortName(){return "R43";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){ //  FunctionalDataProp(d)
				if (formula instanceof OWLFunctionalDataPropertyAxiom
						){
					OWLFunctionalDataPropertyAxiom axiom1 = (OWLFunctionalDataPropertyAxiom) formula;
					// we have a functionaldataproperty axiom, now need to find matching class unsatisfiability axiom
					int count2 = 0;
					for (Object formula2 : antecedent){ // SubCla(X,DataMinCard(n,d,DR0)
						if (formula2 instanceof OWLSubClassOfAxiom
								&& ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLDataMinCardinality
								&& ((OWLDataMinCardinality) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getCardinality()>1
								&& ((OWLDataMinCardinality) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getProperty().equals(axiom1.getProperty())
								&& ((OWLDataMinCardinality) ((OWLSubClassOfAxiom) formula2).getSuperClass()).getFiller().equals(dataFactory.getIntegerOWLDatatype())
								){  
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule42");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule42");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLFunctionalDataPropertyAxiom && formula2 instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom2.getSubClass(),dataFactory.getOWLNothing());
				// System.out.println(axiom3);
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE43
	
RULE44{ // range(r0,X)  and InvObjProp(r0,r1) --> Domain(r1,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule44";};
		public java.lang.String getShortName(){return "R44";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyRangeAxiom
						){ //range(r0,X)
					OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula;
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLInverseObjectPropertiesAxiom // InvObjProp(r0,r1)
								&& ( ((OWLInverseObjectPropertiesAxiom) formula2).getFirstProperty().equals(axiom1.getProperty())
										|| ((OWLInverseObjectPropertiesAxiom) formula2).getSecondProperty().equals(axiom1.getProperty()))
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule44");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule44");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyRangeAxiom 
					&& formula2 instanceof OWLInverseObjectPropertiesAxiom
					){
				OWLObjectPropertyRangeAxiom axiom1 = (OWLObjectPropertyRangeAxiom) formula1;
				OWLInverseObjectPropertiesAxiom axiom2 = (OWLInverseObjectPropertiesAxiom) formula2;
				// construct new term
				OWLObjectPropertyExpression property;
				if (axiom2.getFirstProperty().equals(axiom1.getProperty())){
				property = axiom2.getSecondProperty();}
				else  {property = axiom2.getFirstProperty();}
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLObjectPropertyDomainAxiom axiom3 = dataFactory.getOWLObjectPropertyDomainAxiom(property,axiom1.getRange());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE44
	
RULE45{ //FunctionalDataProp(d) and SubCla(X,DataHasValue(d,val0,DR0) and SubCla(X,DataHasValue(d,val1,DR1)) and either disjoint(DR0,DR1) or val0!=val1--> SubCla(X,\bot)
		
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule45";};
		public java.lang.String getShortName(){return "R45";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){ //  FunctionalDataProp(d)
				if (formula instanceof OWLFunctionalDataPropertyAxiom
						){
					OWLFunctionalDataPropertyAxiom axiom1 = (OWLFunctionalDataPropertyAxiom) formula;
					// we have a functionaldataproperty axiom, now need to find matching class unsatisfiability axiom
					int count2 = 0;
					for (Object formula2 : antecedent){ // SubCla(X,DataHasValue(d,val0,DR0)
						if (formula2 instanceof OWLSubClassOfAxiom
								&& ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLDataHasValue
								&& ((OWLDataHasValue) (((OWLSubClassOfAxiom) formula2).getSuperClass())).getProperty().equals(axiom1.getProperty())
								){  
							OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
							OWLDataHasValue has2 = (OWLDataHasValue) axiom2.getSuperClass();
							int count3 = 0;
							for (Object formula3 : antecedent){ // SubCla(X,DataHasValue(d,val1,DR1)
								if (formula3 instanceof OWLSubClassOfAxiom
										&& ((OWLSubClassOfAxiom) formula3).getSuperClass() instanceof OWLDataHasValue
										&& ((OWLDataHasValue) (((OWLSubClassOfAxiom) formula3).getSuperClass())).getProperty().equals(axiom1.getProperty())
										&& ( !((OWLDataHasValue) (((OWLSubClassOfAxiom) formula3).getSuperClass())).getValue().getDatatype().equals(has2.getValue().getDatatype())
												|| !((OWLDataHasValue) (((OWLSubClassOfAxiom) formula3).getSuperClass())).getValue().equals(has2.getValue())
												)
										){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								SequentPosition position3 = new SequentSinglePosition(SequentPart.ANTECEDENT, count3);
								binding.insertPosition("A2", position3);
								results.add(binding);
							} //endif 	
								count3++;
							} // end loop for formula3
						} // endif
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule42");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule42");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLFunctionalDataPropertyAxiom && formula2 instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom2.getSubClass(),dataFactory.getOWLNothing());
				// System.out.println(axiom3);
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE45
	
RULE46{ //FunctionalDataProp(d) and SubCla(X,ObjectHasValue(d,val0) and SubCla(X,ObjectHasValue(d,val1)) and differentIndividuals(val0, val1)--> SubCla(X,\bot)
		
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule46";};
		public java.lang.String getShortName(){return "R46";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){ //  FunctionalObjecyProp(d)
				if (formula instanceof OWLFunctionalObjectPropertyAxiom
						){
					OWLFunctionalObjectPropertyAxiom axiom1 = (OWLFunctionalObjectPropertyAxiom) formula;
					// we have a functionalobjproperty axiom, now need to find matching class unsatisfiability axiom
					int count2 = 0;
					for (Object formula2 : antecedent){ // SubCla(X,ObjHasValue(d,val0,DR0)
						if (formula2 instanceof OWLSubClassOfAxiom
								&& ((OWLSubClassOfAxiom) formula2).getSuperClass() instanceof OWLObjectHasValue
								&& ((OWLObjectHasValue) (((OWLSubClassOfAxiom) formula2).getSuperClass())).getProperty().equals(axiom1.getProperty())
								){  
							OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
							OWLObjectHasValue has2 = (OWLObjectHasValue) axiom2.getSuperClass();
							int count3 = 0;
							for (Object formula3 : antecedent){ // SubCla(X,DataHasValue(d,val1,DR1)
								if (formula3 instanceof OWLSubClassOfAxiom
										&& ((OWLSubClassOfAxiom) formula3).getSuperClass() instanceof OWLObjectHasValue
										&& ((OWLObjectHasValue) (((OWLSubClassOfAxiom) formula3).getSuperClass())).getProperty().equals(axiom1.getProperty())
										){
									OWLSubClassOfAxiom axiom3 = (OWLSubClassOfAxiom) formula3;
									OWLObjectHasValue has3 = (OWLObjectHasValue) axiom3.getSuperClass();
									int count4 =0;
									for (Object formula4 : antecedent){
										if (formula4 instanceof OWLDifferentIndividualsAxiom
												&& ((OWLDifferentIndividualsAxiom) formula4).getIndividualsAsList().contains(has3.getValue())
												&& ((OWLDifferentIndividualsAxiom) formula4).getIndividualsAsList().contains(has2.getValue())
												){		
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								SequentPosition position3 = new SequentSinglePosition(SequentPart.ANTECEDENT, count3);
								binding.insertPosition("A2", position3);
								results.add(binding);
										}//endif
										count4++;
									} // end loop formula4
							} //endif 	
								count3++;
							} // end loop for formula3
						} // endif
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule46");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule46");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLFunctionalObjectPropertyAxiom && formula2 instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(axiom2.getSubClass(),dataFactory.getOWLNothing());
				// System.out.println(axiom3);
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE46
	
RULE47{ // domain(r1,X)  and InvObjProp(r1,r0) --> range(r0,X)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule47";};
		public java.lang.String getShortName(){return "R47";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLObjectPropertyDomainAxiom
						){ //range(r0,X)
					OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula;
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLInverseObjectPropertiesAxiom // InvObjProp(r0,r1)
								&& ( ((OWLInverseObjectPropertiesAxiom) formula2).getFirstProperty().equals(axiom1.getProperty())
										|| ((OWLInverseObjectPropertiesAxiom) formula2).getSecondProperty().equals(axiom1.getProperty()))
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLObjectPropertyDomainAxiom 
					&& formula2 instanceof OWLInverseObjectPropertiesAxiom
					){
				OWLObjectPropertyDomainAxiom axiom1 = (OWLObjectPropertyDomainAxiom) formula1;
				OWLInverseObjectPropertiesAxiom axiom2 = (OWLInverseObjectPropertiesAxiom) formula2;
				// construct new term
				OWLObjectPropertyExpression property;
				if (axiom2.getFirstProperty().equals(axiom1.getProperty())){
				property = axiom2.getSecondProperty();}
				else  {property = axiom2.getFirstProperty();}
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLObjectPropertyRangeAxiom axiom3 = dataFactory.getOWLObjectPropertyRangeAxiom(property,axiom1.getDomain());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE47
	*/
	
/*
RULE48{ // SubCla(X, \forall r0.Y)  and InvObjProp(r1,r0) --> SubCla(\exists r1.X,Y)
		
		public java.lang.String getName(){return "INLG2012NguyenEtAlRule48";};
		public java.lang.String getShortName(){return "R48";};
		
		public List<RuleBinding> findRuleBindings(Sequent s){
			ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
			if (s.isEmptyP()) return results;
			ArrayList antecedent = s.getAntecedent();
			int count1 = 0;
			for (Object formula : antecedent){
				if (formula instanceof OWLSubClassOfAxiom 
						&& ((OWLSubClassOfAxiom) formula).getSuperClass() instanceof OWLObjectAllValuesFrom
						){ //range(r0,X)
					OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula;
					OWLObjectAllValuesFrom some1 = (OWLObjectAllValuesFrom) axiom1.getSuperClass();
					int count2 = 0;
					for (Object formula2 : antecedent){
						if (formula2 instanceof OWLInverseObjectPropertiesAxiom // InvObjProp(r0,r1)
								&& ( ((OWLInverseObjectPropertiesAxiom) formula2).getFirstProperty().equals(some1.getProperty())
										|| ((OWLInverseObjectPropertiesAxiom) formula2).getSecondProperty().equals(some1.getProperty()))
								){
								RuleBinding binding = new RuleBinding();
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, count1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, count2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count2++;
					} // end inner for loop
				}
				count1++;	
			} // end outer for loop
			return results;
		}
		
		
		
		public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
			SequentPosition position1 = binding.get("A1");
			SequentPosition position2 = binding.get("A2");
			Sequent s = sequent.clone();
			ArrayList antecedent = s.getAntecedent();
			ArrayList newsequents = new ArrayList<Sequent>();
			
			if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule48");}
			SequentSinglePosition pos1 = (SequentSinglePosition) position1;
			if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule48");}
			SequentSinglePosition pos2 = (SequentSinglePosition) position2;
			Object formula1 = antecedent.get(pos1.getToplevelPosition());
			Object formula2 = antecedent.get(pos2.getToplevelPosition());
			if (formula1 instanceof OWLSubClassOfAxiom
					&& ((OWLSubClassOfAxiom) formula1).getSuperClass() instanceof OWLObjectAllValuesFrom
					&& formula2 instanceof OWLInverseObjectPropertiesAxiom
					){
				OWLSubClassOfAxiom axiom1 = (OWLSubClassOfAxiom) formula1;
				OWLObjectAllValuesFrom some1 = (OWLObjectAllValuesFrom) axiom1.getSuperClass();
				OWLInverseObjectPropertiesAxiom axiom2 = (OWLInverseObjectPropertiesAxiom) formula2;
				// construct new term
				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
				OWLDataFactory dataFactory=manager.getOWLDataFactory();
				OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLObjectSomeValuesFrom(some1.getProperty(),axiom1.getSubClass()),some1.getFiller());
				if (!antecedent.contains(axiom3)){
					s.addAntecedent(axiom3);	
				    newsequents.add(s);
				}
			}
			return SequentList.makeANDSequentList(newsequents);
		}
		
		
	}, // END RULE48
	
	// EquivalentClasses(X,\forall r.Y, ...) --> SubCla(\forall r.Y,X)
	
	RULE51{	public java.lang.String getName(){return "INLG2012NguyenEtAlRule51";};
	public java.lang.String getShortName(){return "R51";};
	
	public List<RuleBinding> findRuleBindings(Sequent s){
		ArrayList<RuleBinding> results = new ArrayList<RuleBinding>();
		if (s.isEmptyP()) return results;
		ArrayList antecedent = s.getAntecedent();
		int count1 = 0;
		for (Object formula : antecedent){
			if (formula instanceof OWLEquivalentClassesAxiom){
				OWLEquivalentClassesAxiom axiom = (OWLEquivalentClassesAxiom) formula;
				List<OWLClassExpression> expressions = axiom.getClassExpressionsAsList();
				// loop for X
				int count2 = 0;
				for (OWLClassExpression exp1 : expressions){
					if (exp1 instanceof OWLObjectAllValuesFrom){
						OWLObjectAllValuesFrom from1 = (OWLObjectAllValuesFrom) exp1;
						int count3 = 0;
						for (OWLClassExpression exp2 : expressions){
							if (count2!=count3){
								// System.out.println(exp1);
								// System.out.println(exp2);
								RuleBinding binding = new RuleBinding();
								List<Integer> list1 = new ArrayList<Integer>();
								list1.add(count1);
								list1.add(count2);
								List<Integer> list2 = new ArrayList<Integer>();
								list2.add(count1);
								list2.add(count3);
								SequentPosition position1 = new SequentSinglePosition(SequentPart.ANTECEDENT, list1);
								binding.insertPosition("A1", position1);
								SequentPosition position2 = new SequentSinglePosition(SequentPart.ANTECEDENT, list2);
								binding.insertPosition("A2", position2);
								results.add(binding);
							}
						count3++;
						}
					}
					count2++;
				}
				
			}
			count1++;	
		} // end outer for loop
		return results;
	}
	
	
	
	public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
		SequentPosition position1 = binding.get("A1");
		SequentPosition position2 = binding.get("A2");
		Sequent s = sequent.clone();
		ArrayList antecedent = s.getAntecedent();
		RuleApplicationResults results1 = null;
		ArrayList<RuleApplicationResults> results = new ArrayList();
		
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
		List<Integer> poslist1 = pos1.getPosition();
		List<Integer> poslist2 = pos2.getPosition();
		Object formula1 = antecedent.get(pos1.getToplevelPosition());
		if (formula1 instanceof OWLEquivalentClassesAxiom){
			OWLEquivalentClassesAxiom axiom = (OWLEquivalentClassesAxiom) formula1;
			List<OWLClassExpression> expressions = axiom.getClassExpressionsAsList();
			OWLClassExpression exp1 = expressions.get(poslist1.get(1));
			OWLClassExpression exp2 = expressions.get(poslist2.get(1));	
			if (exp1 instanceof OWLObjectAllValuesFrom){
				OWLObjectAllValuesFrom all1 = (OWLObjectAllValuesFrom) exp1;
			// construct new term
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLObjectAllValuesFrom(all1.getProperty(),dataFactory.getOWLNothing()),exp2);
			if (!antecedent.contains(axiom3)){
				results1 = new RuleApplicationResults();
				results1.setOriginalFormula(s);
				results1.addAddition("A1", axiom3);
				results.add(results1);	
				// depth bookkeeping
				results1.setMaxFormulaDepth(InferenceApplicationService.computeRuleBindingMaxDepth(sequent, binding));
			}
			}
		}
		return results;
	}
	
	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
		List<RuleApplicationResults> results = computeRuleApplicationResults(sequent, binding);
		List<Sequent> sequents =  ruleApplicationResultsAsSequent(results);
		return SequentList.makeANDSequentList(sequents);
	}
	
//	public SequentList computePremises(Sequent sequent, RuleBinding binding) throws Exception{
//		SequentPosition position1 = binding.get("A1");
//		SequentPosition position2 = binding.get("A2");
//		Sequent s = sequent.clone();
//		ArrayList antecedent = s.getAntecedent();
//		ArrayList newsequents = new ArrayList<Sequent>();
//		
//		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
//		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
//		if (!(position2 instanceof SequentSinglePosition)){throw new Exception("unexpected position for rule12");}
//		SequentSinglePosition pos2 = (SequentSinglePosition) position2;
//		List<Integer> poslist1 = pos1.getPosition();
//		List<Integer> poslist2 = pos2.getPosition();
//		Object formula1 = antecedent.get(pos1.getToplevelPosition());
//		if (formula1 instanceof OWLEquivalentClassesAxiom){
//			OWLEquivalentClassesAxiom axiom = (OWLEquivalentClassesAxiom) formula1;
//			List<OWLClassExpression> expressions = axiom.getClassExpressionsAsList();
//			OWLClassExpression exp1 = expressions.get(poslist1.get(1));
//			OWLClassExpression exp2 = expressions.get(poslist2.get(1));	
//			if (exp1 instanceof OWLObjectAllValuesFrom){
//				OWLObjectAllValuesFrom all1 = (OWLObjectAllValuesFrom) exp1;
//			// construct new term
//			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//			OWLDataFactory dataFactory=manager.getOWLDataFactory();
//			OWLSubClassOfAxiom axiom3 = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLObjectAllValuesFrom(all1.getProperty(),dataFactory.getOWLNothing()),exp2);
//			if (!antecedent.contains(axiom3)){
//				s.addAntecedent(axiom3);	
//			    newsequents.add(s);
//			}
//			}
//		}
//		return SequentList.makeANDSequentList(newsequents);
//	}
	
	public void expandTactic(ProofTree tree, ProofNode<Sequent,java.lang.String,AbstractSequentPositions> source, JustificationNode<java.lang.String,AbstractSequentPositions> justification) throws Exception{
		// necessary to deal with or alternatives
					HierarchNode hnode = null;
					List<HierarchNode<String,AbstractSequentPositions>> hnodes = source.getJustifications();
					for(HierarchNode h: hnodes){
						if(h.getJustifications().contains(justification)){
							hnode = h;
						}
					}
					if (hnode == null) throw new Exception("fatal error");
					//
		AbstractSequentPositions positions = justification.getPositions();
		List<Integer> original_premiseids =  justification.getPremises();
		if (positions instanceof RuleBindingForNode){
				System.out.println("positions is instance of rule binding for node");
				RuleBindingForNode rb = (RuleBindingForNode) positions;
				// we keep track of the original set of justifications, to find out which is the new one that is created.
					List<JustificationNode> original_jnodes =  new ArrayList<JustificationNode>(tree.getProofNode(rb.getNodeId()).getJustifications());
					Sequent original_seq = new Sequent(source.getContent().getAntecedent(),source.getContent().getSuccedent());
				//
				// apply Nguyen-1
				List<RuleApplicationResults> results = INLG2012NguyenEtAlRules.RULE1.computeRuleApplicationResults(original_seq, rb);
				InferenceApplicationService.INSTANCE.applySequentInferenceRule(tree, rb, INLG2012NguyenEtAlRules.RULE1); // this could be made more efficient, by using the results computed above
				// we pinpoint the newly added sequent
					JustificationNode newj = tree.getNewestJustification();
					int newnodeid = (Integer) newj.getPremises().get(0);	
					ProofNode newnode1 = tree.getProofNode(newnodeid);
					Sequent newsequent1 = (Sequent) newnode1.getContent();
				// now we apply AdditionalDLRules.FORALLWEAKENING
					List<RuleBindingForNode> forallweakeningbindings = InferenceApplicationService.INSTANCE.findRuleBindingsWhereInferenceApplicable(tree, AdditionalDLRules.FORALLWEAKENING);
					// now need to find the right binding (i.e. the binding, where the formula at the binding position is syntactically equal to the formula that has been added by the results as "A1")
					RuleBindingForNode right_binding = null;
					for (RuleBindingForNode rbfn : forallweakeningbindings){
							// get position & formula of the binding
							SequentPosition position1 = rbfn.getRuleBinding().get("A1");
							if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position!");}
							SequentSinglePosition pos1 = (SequentSinglePosition) position1;
							List<Integer> poslist1 = pos1.getPosition();
							Object formula1 = newsequent1.getAntecedent().get(pos1.getToplevelPosition());
							// get formula that has been added as "A1"
							Object formula2 = results.get(0).getAddition("A1"); // we can take the first results element, since Rule1 only produces one new sequent
							OWLObject f1 = (OWLObject) formula1;
							OWLObject f2 = (OWLObject) formula2;
							if (f1.equals(f2)){
								right_binding = rbfn;
							}
					} // end loop for weakening bindings
					// now apply forall weakening
					// InferenceApplicationService.INSTANCE.applySequentInferenceRule(tree, right_binding, AdditionalDLRules.FORALLWEAKENING); 
					 InferenceApplicationService.INSTANCE.applySequentInferenceRuleToFillGap(tree, right_binding, AdditionalDLRules.FORALLWEAKENING,original_premiseids,hnode); 
		}
		tree.print();
		// TODO: CONTINUE HERE!!!
		return;
	}
	
	
}, // END RULE51
*/	
	; // value entries separated by commas
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s){
		System.out.println("DEBUG -- entered deprecated method");
		List<SequentPosition> positions = findPositions(s);
		ArrayList<RuleBinding> bindings = new ArrayList<RuleBinding>();
		for (SequentPosition pos: positions){
			RuleBinding binding = new RuleBinding();
			binding.insertPosition("SINGLEPOS",pos);
		}
		return bindings;
	}
	
	/* PURE NONSENSE
	public SequentList computePremises(Sequent sequent){
		List<Sequent> premises = new ArrayList();
		List<SequentPosition> positions = findPositions(sequent);
		Iterator it = positions.iterator();
		while(it.hasNext()){
			try {
				premises.addAll(computePremises(sequent,(SequentSinglePosition) it.next()));
			} catch (Exception e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
		}
		return premises;
	} */
	
	@Override
	public List<RuleKind> qualifyRule(){
		RuleKind[] a = {RuleKind.FORWARD};
		return Arrays.asList(a);
	}
	
	@Override
	public boolean isApplicable(Sequent s){
		List<SequentPosition> positions = findPositions(s);
		if (positions==null || positions.size()==0){
		return false;	
		}
		else{return true;}
	}
	
	@Override
	public SequentList computePremises(Sequent s, SequentPosition p){
		return null;
	}
		
	@Override
	public List<SequentPosition> findPositions(Sequent s){
		return null;
	}
	
	public void expandTactic(ProofTree tree, ProofNode<Sequent,java.lang.String,AbstractSequentPositions> source, JustificationNode<java.lang.String,AbstractSequentPositions> justification) throws Exception{
		return;
	}
	
	@Override
	public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
			return null;
	}
	
	private static List<Sequent> ruleApplicationResultsAsSequent(List<RuleApplicationResults> results){
			// System.out.println("ruleApplicationResultsAsSequent called");
			ArrayList<Sequent> sequents = new ArrayList();
			for (RuleApplicationResults result: results){
				// System.out.println("result : " + result);
				// generate a clone of the original sequent
				Object original = result.getOriginalFormula();
				Sequent sequent;
				if (original instanceof IncrementalSequent){
					sequent = ((IncrementalSequent) result.getOriginalFormula()).clone();
				}
				else{
					sequent = ((Sequent) result.getOriginalFormula()).clone();
				}
				// deletions
				Map deletions = result.getDeletionsMap();
				for (Object sr : deletions.keySet()){
						java.lang.String str = (String) sr;
						Object formula = deletions.get(str);
						if (str.contains("A")){
								try {
									// sequent.removeAntecedent(formula);
								} catch (Exception e) {
									//  Auto-generated catch block
									e.printStackTrace();
								}} else {
								try {
									// sequent.removeSuccedent(formula);
								} catch (Exception e) {
									//  Auto-generated catch block
									e.printStackTrace();
								}
							}
						} // endif
				// additions
				Map additions = result.getAdditionsMap();
				for (Object sr : additions.keySet()){
				java.lang.String str = (String) sr;
				Object formula = additions.get(str);
				if (str.contains("A")){
						if (formula instanceof OWLFormula){
							try {
								if (sequent instanceof IncrementalSequent){
									IncrementalSequent incseq = (IncrementalSequent) sequent;
									// System.out.println("bp 1" + incseq.getAllAntecedentOWLFormulas());
									incseq.addAntecedent((OWLFormula) formula);
									// System.out.println("bp 2");
									// System.out.println("adding inc case");
								}
								else{ 
									// System.out.println("adding regular case");
								sequent.addAntecedent((OWLFormula) formula);
								}
							} catch (Exception e) {
								//  Auto-generated catch block
								e.printStackTrace();
							}
							// depth bookkeeping
							int newdepth = result.getMaxFormulaDepth()+1;
							// System.out.println("Setting depth for formula " + VerbalisationManager.prettyPrint(((OWLFormula) formula)) + " : "+ newdepth);
							sequent.setFormulaDepth(sequent.antecedentFormulaGetID((OWLFormula) formula), newdepth);
						} else{
						try {
							// sequent.addAntecedent(formula);
						} catch (Exception e) {
							//  Auto-generated catch block
							e.printStackTrace();
						}}
				}else {
						try {
							// sequent.addSuccedent(formula);
						} catch (Exception e) {
							//  Auto-generated catch block
							e.printStackTrace();
						}
					}
				} // endif
				sequents.add(sequent);
		} // end loop for one result
			// System.out.println("RESULTS " + sequents);
			return sequents;
	}
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s, boolean ... saturate) {
		return findRuleBindings(s);
	}
	
	public static boolean isCyclicBinaryExistsChain(OWLFormula form, OWLAtom role){
		// System.out.println("called with " + form + " and " + role);
		if (form==null) return false;
		if (form.getArgs()==null) return false;
		if (form.getArgs().size()==0) return false;
		if (form.getHead().equals(OWLSymb.EXISTS) && role==null){
			return isCyclicBinaryExistsChain(form.getArgs().get(1), form.getArgs().get(0).getHead());
		} 
		if (form.getHead().equals(OWLSymb.EXISTS) && role!=null){
			if (form.getArgs().get(0).getHead().equals(role)){ 
				// System.out.println("detected cycle");
				return true;
			}
			return isCyclicBinaryExistsChain(form.getArgs().get(1), role);
		} 
		return false;
	}
	
	public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
		return null;
	}
	
	public void clearCaches(){}
	
	public OWLFormula getP2(List<OWLFormula> formulalist, OWLFormula conclusion){
		return null;
	}
	
	public OWLFormula getP3(List<OWLFormula> formulalist, OWLFormula conclusion){
		return null;
	}
	
	public OWLFormula getP4(List<OWLFormula> formulalist, OWLFormula conclusion){
		return null;
	}
	
	public OWLFormula getP5(List<OWLFormula> formulalist, OWLFormula conclusion){
		return null;
	}
	
	public OWLFormula getP6(List<OWLFormula> formulalist, OWLFormula conclusion){
		return null;
	}
	
}
