package org.semanticweb.cogExp.core;

import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;

public class RuleBindingForNode extends RuleBinding{
	private int nid;
	// private OWLFormula newantecedent;
	// private OWLFormula newsuccedent;
	
	RuleBindingForNode (int n){
		super();
		nid = n;
	}
	
	RuleBindingForNode (int n,RuleBinding binding){
		super();
		hashMap = binding.getBindings();
		nid = n;
		this.newantecedent = binding.getNewAntecedent();
		this.newsuccedent = binding.getNewSuccedent();
	}
	
	RuleBindingForNode (int n,RuleBinding binding, OWLFormula newantecedent, OWLFormula newsuccedent){
		super();
		hashMap = binding.getBindings();
		nid = n;
		this.newantecedent = newantecedent;
		this.newsuccedent = newsuccedent;
	}
	
	public int getNodeId(){
		return nid;
	}
	
	public void setNodeId(int id){
		nid = id;
	}
	
	public RuleBinding getRuleBinding(){
		RuleBinding binding = new RuleBinding(hashMap,newantecedent, newsuccedent);
		return binding;
	}
	
	@Override
	public List<Integer> getSinglePosition(java.lang.String str) throws Exception{
		return super.getSinglePosition(str);
	}
	
	
	
}
