package org.semanticweb.cogExp.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;

public enum AlreadyTriedCache {
	INSTANCE;
	
	private HashMap<String, Integer> formulasToInteger = new HashMap<String,Integer>();
	private int formid = 0;
	private HashSet<String> triedStore = new HashSet<String>();
	
	/* 
	private String generateString(List<OWLFormula> list){
			String result = "";
			for(OWLFormula form : list){
				result += form.toString();
			}
	}
	*/
	
	private List<Integer> FormulaToInt(List<OWLFormula> forms){
		List<Integer> results = new ArrayList<Integer>();
		for (OWLFormula form:forms){
			Object found = formulasToInteger.get(form.toString());
			if (found!=null)
			{
				results.add((Integer) found);
				// System.out.println("adding " + formulasToInteger.get(form.toString()));
			} else {
				formulasToInteger.put(form.toString(), (Integer) formid);
				// System.out.println("suppose that " + formulasToInteger.get(form.toString()) + " is " + formid);
				results.add(formid);
				formid++;
			}
		}
		return results;	
	}
	
	public boolean wasTried(SequentInferenceRule rule, List<OWLFormula> formulas){
		List<Integer> ints = FormulaToInt(formulas);
		// String hashstring = rule.getName() + formulas.toString();
		 String hashstring = rule.getName() + ints.toString();
		// System.out.println(formulas.toString());
		//  System.out.println("hashstring " + hashstring);
		return triedStore.contains(hashstring);
	}
	
	public void setTried(SequentInferenceRule rule, List<OWLFormula> formulas){
		List<Integer> ints = FormulaToInt(formulas);
		// String hashstring = rule.getName() + formulas.toString();
		String hashstring = rule.getName() + ints.toString();
		// System.out.println("hashstring " + hashstring);
		triedStore.add(hashstring);
	}
	
	public void reset(){
		triedStore = new HashSet<String>();
		formulasToInteger = new HashMap<String,Integer>();
		formid = 0;
	}
	
	public String toString(){
		return "Already tried cache contains " + triedStore.size() + " entries";
	}
	
}
