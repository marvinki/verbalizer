package org.semanticweb.cogExp.core;

import java.util.HashSet;
import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;

public enum AlreadyTriedCache {
	INSTANCE;
	
	private HashSet<String> triedStore = new HashSet<String>();
	
	/* 
	private String generateString(List<OWLFormula> list){
			String result = "";
			for(OWLFormula form : list){
				result += form.toString();
			}
	}
	*/
	
	public boolean wasTried(SequentInferenceRule rule, List<OWLFormula> formulas){
		String hashstring = rule.getName() + formulas.toString();
		// System.out.println(formulas.toString());
		return triedStore.contains(hashstring);
	}
	
	public void setTried(SequentInferenceRule rule, List<OWLFormula> formulas){
		String hashstring = rule.getName() + formulas.toString();
		triedStore.add(hashstring);
	}
	
	public void reset(){
		triedStore = new HashSet<String>();
	}
	
}
