package org.semanticweb.cogExp.GentzenTree;

import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.SequentInferenceRule;

public class GentzenStep {
	private List<Integer> premises;
	private List<Integer> axiompremises;
	private int conclusion;
	private SequentInferenceRule infrule;
	private GentzenTree tree;
	
	public GentzenStep(List<Integer> premises, int conclusion, SequentInferenceRule infrule){
		this.premises = premises;
		this.conclusion = conclusion;
		this.infrule = infrule;
	}
	
	public GentzenStep(List<Integer> premises, List<Integer> axiompremises, int conclusion, SequentInferenceRule infrule, GentzenTree tree){
		this.premises = premises;
		this.axiompremises = axiompremises;
		this.conclusion = conclusion;
		this.infrule = infrule;
		this.tree = tree;
	}
	
	public List<Integer> getPremises(){
		return premises;
	}
	
	public List<OWLFormula> getPremiseFormulas(){
		return tree.idsToFormulas(premises);
	}
	
	public List<Integer> getAxiomPremises(){
		return axiompremises;
	}
	
	public SequentInferenceRule getInfrule(){
		return infrule;
	}
	
	public int getConclusion(){
		return conclusion;
	}
	public OWLFormula getConclusionFormula(){
		return tree.idToFormula(conclusion);
	}
	
	public OWLFormula getP1(){
		return infrule.getP1(getPremiseFormulas(),getConclusionFormula());
	}
	
	public OWLFormula getP2(){
		return infrule.getP2(getPremiseFormulas(),getConclusionFormula());
	}
	
	public OWLFormula getP3(){
		return infrule.getP3(getPremiseFormulas(),getConclusionFormula());
	}
	
	public OWLFormula getP4(){
		return infrule.getP4(getPremiseFormulas(),getConclusionFormula());
	}
	
}
