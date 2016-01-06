package org.semanticweb.cogExp.core;

import java.util.HashMap;
import java.util.List;

import org.semanticweb.cogExp.OWLFormulas.OWLFormula;

public class RuleBinding extends AbstractSequentPositions {
	protected HashMap<java.lang.String,SequentPosition> hashMap;
	protected OWLFormula newantecedent = null;
	protected OWLFormula newsuccedent = null;
	
	public void insertPosition(java.lang.String str, SequentPosition pos){
		hashMap.put(str,pos);
	}
	
	public SequentPosition get(java.lang.String str){
		return hashMap.get(str);
	}
	
	public HashMap<java.lang.String,SequentPosition> getBindings(){
		return hashMap;
	}
	
	public RuleBinding(HashMap<java.lang.String,SequentPosition> map){
		hashMap = map;
	}
	
	public RuleBinding(){
		hashMap = new HashMap<java.lang.String,SequentPosition>();
	}
	
	public RuleBinding(OWLFormula newantecedent, OWLFormula newsuccedent){
		hashMap = new HashMap<java.lang.String,SequentPosition>();
		this.newantecedent = newantecedent;
		this.newsuccedent = newsuccedent;
	}
	
	public RuleBinding(HashMap<java.lang.String,SequentPosition> map, OWLFormula newantecedent, OWLFormula newsuccedent){
		hashMap = map;
		this.newantecedent = newantecedent;
		this.newsuccedent = newsuccedent;
	}
	
	public List<Integer> getSinglePosition(java.lang.String str) throws Exception{
		SequentPosition position1 = hashMap.get(str);
		if (!(position1 instanceof SequentSinglePosition)){throw new Exception("unexpected position type!");}
		SequentSinglePosition pos1 = (SequentSinglePosition) position1;
		List<Integer> poslist1 = pos1.getPosition();
		return poslist1;
	}
	
	public OWLFormula getNewAntecedent(){
		return newantecedent;
}

	public OWLFormula getNewSuccedent(){
	return newsuccedent;
}
	
}
