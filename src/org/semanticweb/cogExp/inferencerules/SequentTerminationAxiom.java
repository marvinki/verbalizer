package org.semanticweb.cogExp.inferencerules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.semanticweb.cogExp.core.RuleApplicationResults;
import org.semanticweb.cogExp.core.RuleBinding;
import org.semanticweb.cogExp.core.RuleKind;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.core.SequentList;
import org.semanticweb.cogExp.core.SequentMultiPosition;
import org.semanticweb.cogExp.core.SequentPart;
import org.semanticweb.cogExp.core.SequentPosition;
import org.semanticweb.cogExp.core.SequentSinglePosition;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.OWLFormulas.OWLSymb;



public enum SequentTerminationAxiom implements SequentInferenceRule {
	INSTANCE;
	

		
	@Override
	public  boolean isApplicable(Sequent s){
		return findPositions(s).size()>0;
	}
	
	
	@Override
	public List<SequentPosition> findPositions(Sequent s){
		ArrayList sequentPositions = new ArrayList<SequentPosition>();
		OWLFormula formulaBot = OWLFormula.createFormulaBot();
		// 5th rule in Fig 3, Explaining ALC Subsumption
		if (s.alreadyContainedInAntecedent(formulaBot)){
			SequentPosition sequentPosition = new SequentSinglePosition(SequentPart.ANTECEDENT,
					s.antecedentFormulaGetID(formulaBot));
			sequentPositions.add(sequentPosition);
		}
		OWLFormula formulaNegTop = OWLFormula.createFormula(OWLSymb.NEG, OWLFormula.createFormulaTop());
		if (s.alreadyContainedInAntecedent(formulaNegTop)){
			SequentPosition sequentPosition = new SequentSinglePosition(SequentPart.ANTECEDENT,
					s.antecedentFormulaGetID(formulaNegTop));
			sequentPositions.add(sequentPosition);
		}
		// 6th rule in Fig 3, Explaining ALC Subsumption
				// System.out.println("debug 2");
		OWLFormula formulaTop = OWLFormula.createFormulaTop();
		if (s.alreadyContainedInSuccedent(formulaNegTop)){
			SequentPosition sequentPosition = new SequentSinglePosition(SequentPart.SUCCEDENT,
					s.succedentFormulaGetID(formulaTop));
			sequentPositions.add(sequentPosition);
		}
		
		// 1st and 2nd rule
		Set<OWLFormula> antecedentFormulas = s.getAllAntecedentOWLFormulas();
		for (OWLFormula antFormula : antecedentFormulas){
			if (s.alreadyContainedInSuccedent(antFormula)){
				int id1 = s.antecedentFormulaGetID(antFormula);
				// int id2 = s.succedentFormulaGetID(antFormula);
				SequentPosition sequentPosition = new SequentSinglePosition(SequentPart.ANTECEDENT,id1);
				sequentPositions.add(sequentPosition);
				// System.out.println("TerminationAxiom DEBUG: " + s.retrieveOWLFormulas(sequentPosition));
				return sequentPositions;
			}
		}
		// 3rd rule
		// System.out.println("debug 4");
		for (OWLFormula antFormula : antecedentFormulas){
			OWLFormula formulaCompl = OWLFormula.createFormula(OWLSymb.NEG,antFormula);
			if (s.alreadyContainedInAntecedent(formulaCompl)){
				int id1 = s.antecedentFormulaGetID(antFormula);
				int id2 = s.antecedentFormulaGetID(formulaCompl);
				Integer[][] positions = new Integer[][]{};
				positions[0][0]=id1;
				positions[1][0]=id2;
				SequentPosition sequentPosition = new SequentMultiPosition(SequentPart.ANTECEDENT,positions);
				sequentPositions.add(sequentPosition);
			}
		}
		Set<OWLFormula> succedentFormulas = s.getAllSuccedentOWLFormulas();
		for (OWLFormula sucFormula : succedentFormulas){
			OWLFormula formulaCompl = OWLFormula.createFormula(OWLSymb.NEG,sucFormula);
				if (s.alreadyContainedInSuccedent(formulaCompl)){
						int id1 = s.succedentFormulaGetID(sucFormula);
						int id2 = s.succedentFormulaGetID(formulaCompl);
						Integer[][] positions = new Integer[][]{};
						positions[0][0]=id1;
						positions[1][0]=id2;
						SequentPosition sequentPosition = new SequentMultiPosition(SequentPart.SUCCEDENT,positions);
						sequentPositions.add(sequentPosition);
					}
				}
		return sequentPositions;
	}
	
	/* OLD AND UNUSED
	public List<SequentPosition> findPositions(Sequent s){
		ArrayList sequentPositions = new ArrayList();
		if (isApplicable(s)){
			// System.out.println("termination is applicable");
			sequentPositions = new ArrayList();
			SequentPosition sequentPosition = new SequentSinglePosition(SequentPart.ANY,0);
			sequentPositions.add(sequentPosition);
			return sequentPositions;
		}
		return sequentPositions;
	}
	*/
	
	public SequentList computePremises(Sequent sequent){
		// System.out.println("TERMINATION");
		if (isApplicable(sequent)){
			ArrayList sequentlist = new ArrayList();
			Sequent newsequent = new Sequent();
			sequentlist.add(newsequent);
			return SequentList.makeANDSequentList(sequentlist);
		}
		return null;
	}
	
	@Override
	public SequentList computePremises (Sequent sequent,SequentPosition position) throws Exception{
		return computePremises(sequent);
	}
	
	@Override
	public java.lang.String getName(){return "TerminationAxiom";};
	@Override
	public java.lang.String getShortName(){return "t";};
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s){
		List<SequentPosition> positions = findPositions(s);
		ArrayList<RuleBinding> bindings = new ArrayList<RuleBinding>();
		for (SequentPosition pos: positions){
			RuleBinding binding = new RuleBinding();
			binding.insertPosition("SINGLEPOS",pos);
			bindings.add(binding);
		}
		return bindings;
	}
	
	@Override
	public SequentList computePremises(Sequent sequent, RuleBinding binding)
			throws Exception {
		SequentPosition pos = binding.get("SINGLEPOS");
		return computePremises(sequent,pos);
	}
	
	@Override
	public List<RuleKind> qualifyRule(){
		RuleKind[] a = {RuleKind.TERMINATING};
		return Arrays.asList(a);
	}
	
	@Override
	public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
		throw new Exception("not implemented yet!");
	}
	
	@Override
	public List<RuleBinding> findRuleBindings(Sequent s, boolean ... saturate) {
		return findRuleBindings(s);
	}
	
}
