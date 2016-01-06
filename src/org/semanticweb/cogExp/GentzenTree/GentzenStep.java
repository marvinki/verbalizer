package org.semanticweb.cogExp.GentzenTree;

import java.util.List;

import org.semanticweb.cogExp.core.SequentInferenceRule;

public class GentzenStep {
	private List<Integer> premises;
	private List<Integer> axiompremises;
	private int conclusion;
	private SequentInferenceRule infrule;
	
	public GentzenStep(List<Integer> premises, int conclusion, SequentInferenceRule infrule){
		this.premises = premises;
		this.conclusion = conclusion;
		this.infrule = infrule;
	}
	
	public GentzenStep(List<Integer> premises, List<Integer> axiompremises, int conclusion, SequentInferenceRule infrule){
		this.premises = premises;
		this.axiompremises = axiompremises;
		this.conclusion = conclusion;
		this.infrule = infrule;
	}
	
	public List<Integer> getPremises(){
		return premises;
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
	
	
}
