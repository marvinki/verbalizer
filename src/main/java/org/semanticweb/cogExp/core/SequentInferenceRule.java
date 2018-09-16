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
	public OWLFormula getP3(List<OWLFormula> formulalist, OWLFormula conclusion);
	public OWLFormula getP4(List<OWLFormula> formulalist, OWLFormula conclusion);
	public OWLFormula getP5(List<OWLFormula> formulalist, OWLFormula conclusion);
	public OWLFormula getP6(List<OWLFormula> formulalist, OWLFormula conclusion);

	
}
