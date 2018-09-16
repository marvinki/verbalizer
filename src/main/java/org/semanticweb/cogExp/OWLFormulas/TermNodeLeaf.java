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

package org.semanticweb.cogExp.OWLFormulas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class TermNodeLeaf extends TermNode{
	// ArrayList<OWLFormula> formulas = new ArrayList<OWLFormula>();
	HashMap<Integer, OWLFormula> formulas = new HashMap<Integer, OWLFormula>();
	
	public TermNodeLeaf(OWLAtom atom, HashMap<Integer, OWLFormula> formulas){
		super(atom);
		this.formulas = formulas;
	}
	
	public void addFormula(int i, OWLFormula formula){
		formulas.put(i, formula);
		// formulas.add(i,formula);
	}
	
	public Collection<OWLFormula> getFormulas(){
		return formulas.values();
		// return new ArrayList<OWLFormula>(formulas.values());
	}
	
	public Set<Integer> getFormulaIDs(){
		return formulas.keySet();
	}
	
	@Override
	public String toString() {
		return formulas.toString(); 
	}
	
	
	public static String print(TermNodeLeaf tnl, int indent) {
		return tnl.formulas.toString(); 
	}
	
	@Override
	public TermNodeLeaf clone(){
		HashMap<Integer,OWLFormula> newformulas = new HashMap<Integer,OWLFormula>();
		TermNodeLeaf ntn = new TermNodeLeaf(this.getHead(),newformulas);
		for (int i : formulas.keySet()){
			OWLFormula newf = formulas.get(i).clone();
			newformulas.put(i,newf);
		}
		return ntn;
	}
	
}
