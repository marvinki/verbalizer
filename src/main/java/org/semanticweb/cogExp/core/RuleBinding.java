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

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
	
	public String toString(){
		return hashMap.toString();
	}
	
	public RuleBinding convert(Sequent source, Sequent target){
		Set<String> keys = hashMap.keySet();
		HashMap<java.lang.String,SequentPosition> newHashMap = new HashMap<java.lang.String,SequentPosition>();
		for (String key : keys){
			SequentPosition pos = hashMap.get(key);
			if (pos instanceof SequentSinglePosition){
				SequentSinglePosition singlePos = (SequentSinglePosition) pos;
				int i = singlePos.getToplevelPosition();
				OWLFormula form = source.antecedentGetFormula(i);
				int targetId = target.antecedentFormulaGetID(form);
				// System.out.println(key + " Orig: " + form + " found: " +  target.antecedentGetFormula(targetId));
				SequentSinglePosition newPos = new SequentSinglePosition(SequentPart.ANTECEDENT,targetId);
				newHashMap.put(key, newPos);
			} else
				return null;
		}
		RuleBinding newBinding = new RuleBinding(newHashMap,newantecedent, newsuccedent);
		// System.out.println("New antecedent " + newBinding.getNewAntecedent());
		if (newantecedent !=null && target.alreadyContainedInAntecedent(newantecedent))
		return null;
		
		return newBinding;
	}
}
