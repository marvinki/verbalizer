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

import java.util.HashSet;

public class TermNode {

	private OWLAtom head;
	// private HashSet<ListNode> alternatives = new HashSet<ListNode>();
	private HashSet<TermNode> alternatives = new HashSet<TermNode>();
	
	public TermNode(OWLAtom head){
		this.head = head;
	}
	
	public OWLAtom getHead(){
		return  head;
	}
	
	public HashSet<TermNode> getAlternatives(){
		return alternatives;
	}
	
	public void addAlternative(TermNode node){
		alternatives.add(node);
	}
	
	@Override
	public String toString() {
		String result = "TermNode \n";
		if (head!=null){
		result += "Head:" + head.toString();
		}
		result += "\n Alternatives: \n";
		for (TermNode alt: alternatives){
			result += "   ";
			result += alt.toString();
			result += "\n";
		}
		return result; 
	}
	
	public static String print(TermNode node, int indent){
		if (node instanceof TermNodeLeaf){
			return node.head.toString() + " " + ((TermNodeLeaf) node).formulas.toString() + "\n";
		}
		String result = "";
		if (node.head!=null){
		result += node.head.toString();
		}
		// if (node.alternatives==null || node.alternatives.size()==0){
			result += "\n";
		// }
		for (TermNode alt: node.alternatives){
			for (int i = 0; i<indent;i++){
			result += "  ";
			}
			result += print(alt,indent+1);
		}
		return result; 
	}
	
	@Override
	public TermNode clone(){
		TermNode ntn = new TermNode(head);
		if (alternatives != null){
		for (TermNode tn : alternatives){
			TermNode newtn = tn.clone();
			ntn.alternatives.add(newtn);
		}
		}
		return ntn;
	}
	
}
