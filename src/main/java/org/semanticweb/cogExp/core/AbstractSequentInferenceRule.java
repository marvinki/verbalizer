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

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;

public class AbstractSequentInferenceRule implements SequentInferenceRule {

@Override
public boolean isApplicable(Sequent s) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public List<SequentPosition> findPositions(Sequent s) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List<RuleBinding> findRuleBindings(Sequent s, boolean ... saturate) {
	return findRuleBindings(s);
}

@Override
public List<RuleBinding> findRuleBindings(Sequent s) {
	List<SequentPosition> positions = findPositions(s);
	ArrayList<RuleBinding> bindings = new ArrayList<RuleBinding>();
	for (SequentPosition pos: positions){
		RuleBinding binding = new RuleBinding();
		binding.insertPosition("SINGLEPOS",pos);
	}
	return bindings;
}

public SequentList computePremises(Sequent s) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public SequentList computePremises(Sequent sequent, SequentPosition position)
		throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public SequentList computePremises(Sequent sequent, RuleBinding binding)
		throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getName() {
	// TODO Auto-generated method stub
	return null;
};

@Override
public String getShortName() {
	// TODO Auto-generated method stub
	return null;
};

@Override
public List<RuleKind> qualifyRule(){
	return null;
}

@Override
public List<RuleApplicationResults> computeRuleApplicationResults(Sequent sequent, RuleBinding binding) throws Exception{
	return null;
}

public void clearCaches(){}

public OWLFormula getP1(List<OWLFormula> formulalist, OWLFormula conclusion){
	return null;
}

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
