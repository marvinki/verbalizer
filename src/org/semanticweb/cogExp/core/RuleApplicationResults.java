package org.semanticweb.cogExp.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/* 
 * This works as follows:
 * Invariant: keys are strings which distinguish between antecedent/succedent; 
 * call antecedents "A1", "A2", etc. 
 */

public class RuleApplicationResults<FormulaType,FormulaType2> {
	private FormulaType originalformula;
	private HashMap<java.lang.String,FormulaType2> add    = new HashMap();
	private HashMap<java.lang.String,FormulaType2> delete = new HashMap();
	private int maxFormulaDepth = 10000;
	
	
	public FormulaType getOriginalFormula(){
		return originalformula;
	}
	
	public void setOriginalFormula(FormulaType formula){
		originalformula = formula;
		return ;
}
	
	public FormulaType2 getAddition(java.lang.String sr){
		return add.get(sr);
	}	
	
	public void addAddition(java.lang.String sr, FormulaType2 formula){
		 add.put(sr, formula);
		 return;
}	
	
	public Collection<FormulaType2> getAdditions(){
		return add.values();
}		
	
	public HashMap<java.lang.String,FormulaType2> getAdditionsMap(){
		return add;
}	
	
	
	public FormulaType2 getDeletion(java.lang.String sr){
		return delete.get(sr);
}		
	
	public void addDeletion(java.lang.String sr, FormulaType2 formula){
		delete.put(sr,formula);
		return;
}		

	public Collection<FormulaType2> getDeletions(){
		return delete.values();
	}		

	public HashMap<java.lang.String,FormulaType2> getDeletionsMap(){
		return delete;
	}	
	
	public Set<java.lang.String> getAllAdditionKeys(){
		return add.keySet();
	}
	
	public Set<java.lang.String> getAllDeletionsKeys(){
		return delete.keySet();
	}
	
	@Override
	public java.lang.String toString(){
			return "Original formula: " +  originalformula + " Add: " + add + " Delete: " + delete;
	}
	
	public int getMaxFormulaDepth(){
		return maxFormulaDepth;
	}
	
	public void setMaxFormulaDepth(int depth){
		maxFormulaDepth = depth;
	}
	
}
