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

public class RuleBindingForNode extends RuleBinding{
	private int nid;
	// private OWLFormula newantecedent;
	// private OWLFormula newsuccedent;
	
	RuleBindingForNode (int n){
		super();
		nid = n;
	}
	
	public RuleBindingForNode (int n,RuleBinding binding){
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
