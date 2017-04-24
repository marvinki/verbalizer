package org.semanticweb.cogExp.GentzenTree;

import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.SequentInferenceRule;

import uk.ac.manchester.cs.bhig.util.Tree;

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
	
}
