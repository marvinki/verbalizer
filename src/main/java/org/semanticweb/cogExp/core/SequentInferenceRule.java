package org.semanticweb.cogExp.core;

import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;

public interface SequentInferenceRule {

	public boolean isApplicable(Sequent s);
	
	public List<SequentPosition> findPositions(Sequent s);
	
	public List<RuleBinding> findRuleBindings(Sequent s);
	public List<RuleBinding> findRuleBindings(Sequent s,boolean ... saturate);
	
	// public SequentList computePremises(Sequent s); // there is no use in computing several inference applications possible on some sequent at once
	
	public SequentList computePremises (Sequent sequent,SequentPosition position) throws Exception;
	
	
	public SequentList computePremises (Sequent sequent,RuleBinding binding) throws Exception;
	
	public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception;
	
	public java.lang.String getName();
	
	public java.lang.String getShortName();
	
	public List<RuleKind> qualifyRule();
	
	public void clearCaches();
	
	public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion);
	public OWLFormula getP2(List<OWLFormula> formulalist, OWLFormula conclusion);

	
}
