/*
 *     Copyright 2012-2018 Ulm University, AI Institute
 *     Main author: Marvin Schiller, contributors: Felix Paffrath, Chunhui Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

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
