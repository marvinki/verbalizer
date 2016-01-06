package org.semanticweb.cogExp.core;

import java.util.ArrayList;
import java.util.List;

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

}
