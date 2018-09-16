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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class TermTree {
	
	private TermNode root;
	private int last_term_id = 0;
	private HashMap<Integer,OWLFormula> termIds = new HashMap<Integer,OWLFormula>();
	private HashMap<OWLFormula,Integer> formulaToIDMapping = new HashMap<OWLFormula, Integer>();
	
	
	public String getStatistics(){
		return ("Items: " + termIds.keySet().size());
	}
	
	public TermTree(OWLFormula formula) throws Exception{
		insert(formula);
	} 
	
	public TermTree(){
	} 
	
	public TermTree(TermNode root){
		this.root = root;
	} 
	
	public TermTree(TermNode root, HashMap<Integer,OWLFormula> termIds, HashMap<OWLFormula,Integer> formulaToIDMapping, int lastid){
		this.root = root;
		this.termIds = termIds;
		this.formulaToIDMapping = formulaToIDMapping;
		this.last_term_id = lastid;
	} 
	
	public Integer getHighestContainedTermID(){
		int i = 0;
		Set<Integer> ids = termIds.keySet();
		for(int j : ids){
			if(j>i){
				i =j;
			}
		}
		return i;
	}
	
	
	
	public void insert(OWLFormula formula) throws Exception{
		// do not insert if formula is already present!
		// System.out.println("Checking containment with tree " + this);
		if (contains(formula)){
			System.out.println("Formula already contained! " + formula);
			return;
		}
		// System.out.println("TT Insert is inserting formula: " + formula);
		LinkedList<OWLAtom> list = formula.getBFLinearisation();
	    // System.out.println("insert uses linearisation: " + list);
		if (root == null){ // make sure there is something in the root
			// System.out.println("in the null case");
			root = new TermNode(null);
		}
		// System.out.println("after root vodoo");
		insert(list,root,formula);
		// show the id
		// System.out.println("Inserted formula " + formula + " with id " + formulaGetID(formula) + " into tree " + this);
		return;
	}
	
	
	public void insert(LinkedList<OWLAtom> list, TermNode parent, OWLFormula originalFormula) throws Exception{
		// System.out.println("insert iterator called with current item " + list);
		// System.out.println("and parent " + parent);
		// System.out.println(" is leaf parent " + (parent instanceof TermNodeLeaf));
		if (list.size()==0){
				return;
		}
		// get the child ("right child") where the head matches the first item in the atom list
		HashSet<TermNode> children = parent.getAlternatives();
		TermNode right_child = null;
		for (TermNode tn : children){
			if (tn.getHead().equals(list.get(0))){
				right_child = tn;
				break;
			}
		}
		if (right_child == null){ // need to add alternative
			TermNode newnode;
			if (list.size()==1){ // if we need to create leaf
				    // System.out.println("creating leaf");
					HashMap<Integer,OWLFormula> newformulalist = new HashMap<Integer,OWLFormula>();
					this.last_term_id += 1;
					newformulalist.put(this.last_term_id, originalFormula);
					termIds.put(this.last_term_id,originalFormula);
					formulaToIDMapping.put(originalFormula, this.last_term_id);
					if (list.get(0)==null)
						throw new Exception();
					TermNodeLeaf tnl = new TermNodeLeaf(list.get(0),newformulalist);
					newnode = tnl;
			}	else { // if intermediate node (recurse)
					TermNode tn = new TermNode(list.get(0));
					newnode = tn;
					list.remove(0);
					insert(list,tn,originalFormula);
			}
			// in any case, link the new node with the parent
			parent.addAlternative(newnode);
			// System.out.println("leafy parent " + parent);
		} // end the case of adding an alternative
		else{ // if there already exists the right node
			if (list.size()==1){ // if leaf
				if (right_child instanceof TermNodeLeaf){ // we are at leaf, everything allright
					this.last_term_id += 1;
					((TermNodeLeaf) right_child).addFormula(this.last_term_id,originalFormula);
					termIds.put(this.last_term_id,originalFormula);
					formulaToIDMapping.put(originalFormula, this.last_term_id);
				}	else { // this case should not happen... in principle
					// --> in fact, this happens when "list" is a sublist of a longer term
					// now create a leaf as an alternative to the right child
					// System.out.println("TERM TREE: SOMETHING IS REALLY GOING WRONG!" + originalFormula);
					// System.out.println("right_child: " + right_child);
					HashMap<Integer,OWLFormula> newformulalist = new HashMap<Integer,OWLFormula>();
					this.last_term_id += 1;
					newformulalist.put(this.last_term_id, originalFormula);
					termIds.put(this.last_term_id,originalFormula);
					formulaToIDMapping.put(originalFormula, this.last_term_id);
					TermNodeLeaf tnl = new TermNodeLeaf(list.get(0),newformulalist);
					parent.addAlternative(tnl);
				}
			} else { // if not leaf; recurse
				if (right_child instanceof TermNodeLeaf
						&& list.size()>0
						){ // <-- need to be careful, are we inserting a super-string of what is already there? 
					// System.out.println("attention!");
					// we create an alternative node with the same head as the "right child"
					TermNode tn = new TermNode(list.get(0));
					list.remove(0);
					parent.addAlternative(tn);
					// tn.addAlternative(right_child);
					// System.out.println("generated " + tn);
					insert(list,tn,originalFormula);
				} else 
				list.remove(0);
				insert(list,right_child,originalFormula);
			}
		} // done in all cases
		return;
	}
	
	
	// simple check if a certain formula is contained.
	public boolean contains (OWLFormula formula){
		LinkedList<OWLAtom> list = formula.getBFLinearisation();
		// System.out.println("linearisation " + list);
		return contains(formula, list, root);
	}
	
	public boolean contains (OWLFormula formula, LinkedList<OWLAtom> list, TermNode node){
		// System.out.println("Contains called with formula " + formula + " atomlist " + list + " TermNode " + TermNode.print(node,0));
		if (list.size()==0 || node==null){
			return false; 
		}
		if (list.size()==1){ // base case
			HashSet<TermNode> children = node.getAlternatives();
			TermNode right_child = null;
			for (TermNode tn : children){
				if (tn instanceof TermNodeLeaf 
						&& tn.getHead().equals(list.get(0)) 
						&& ((TermNodeLeaf) tn).getFormulas().contains(formula)){
					right_child = tn;
					break;
				}
			}
			if (right_child == null){
				return false;
			} else {
				return true;
			}
		} else{ // recursive case
			// find correct child
			HashSet<TermNode> children = node.getAlternatives();
			TermNode right_child = null;
			for (TermNode tn : children){
				if (tn.getHead().equals(list.get(0))){
					right_child = tn;
					break;
				}
			}
				if (right_child == null){ // not found
					 // System.out.println("child not found");
					return false;
				} else{ // was found
					 // System.out.println("child was found");
					list.remove(0);
					return contains(formula,list,right_child);
			}
		} // end recursive case
		
	}
	
	
	
	// uses the simple check to see if a formula is contained somewhere within the tree
	// this can't be used properly: it disrupts the subtree structure. Need reworking.
		public boolean containsOrDeeplyContains (OWLFormula formula){
			LinkedList<OWLAtom> list = formula.getBFLinearisation();
			// System.out.println("linearisation " + list);
			return containsOrDeeplyContains(formula, list, root);
		}
		
		public boolean containsOrDeeplyContains (OWLFormula formula, LinkedList<OWLAtom> list, TermNode node){
			// System.out.println("contains called with " + formula + " list " + " node " + node);
			if (list==null || list.size()==0){
				return true;
			}
			if (node instanceof TermNodeLeaf){
				TermNodeLeaf tnl = (TermNodeLeaf) node;
				// now need to loop through formulas
				Collection<OWLFormula> allformulas = tnl.getFormulas();
				for (OWLFormula form : allformulas){
					if (form.containsSubformula(formula)){
						return true;
						}
				}
				return false; // if we get here, this means what we were looking for is not in the leaf.
			}
			if (node.getHead()!=null && node.getHead().equals(list.get(0))){
				// recurse
				list.remove(0);
				for (TermNode child : node.getAlternatives()){
					if (containsOrDeeplyContains (formula, list, child)){
						return true;
					}
				}
				return false; // this will be reached only if recursion was not successful
			}
			
			if (contains(formula, list, node)){
				return true;
			} else{ // we may recurse
				if (node.getAlternatives()!=null && node.getAlternatives().size()>0){
					for (TermNode n: node.getAlternatives()){
						if (containsOrDeeplyContains(formula, list, n)){
							return true;
						}
					}
				}
			}
			return false;
		}
			
	
		
		// uses the simple check to see if a formula is contained somewhere within the tree
		      
				public List<OWLFormula> matchDeeply (OWLFormula formula){
					List<OWLFormula> results = new ArrayList<OWLFormula>();
					LinkedList<OWLAtom> list = formula.getBFLinearisation();
					// System.out.println("linearisation " + list);
					return matchDeeply(formula, list, root);
				}
				
				public List<OWLFormula> matchDeeply (OWLFormula formula, LinkedList<OWLAtom> list, TermNode node){
					List<OWLFormula> results = new ArrayList<OWLFormula>();
					// System.out.println("matchDeeply called with formula " + formula + " atomlist " + list + " TermNode " + node);
					// System.out.println("linearisation " + list);
					results.addAll(matchCandidates(formula, list, node));
					if (root.getAlternatives()!=null && root.getAlternatives().size()>0){
						for (TermNode n: node.getAlternatives()){
							results.addAll(matchDeeply(formula, list, n));
						}
					}
					return results;
				}
					
					
		
		
	public static List<TermNode> findAllSubChildrenWithParticularHead(OWLAtom atom, TermNode node){
		List<TermNode> result = new ArrayList<TermNode>();
		if (node.getHead().equals(atom)){
			result.add(node);
			return result;
		}
		HashSet<TermNode> children = node.getAlternatives();
		if (!(children==null) && children.size()==0){
			for (TermNode child:children){
				result.addAll(findAllSubChildrenWithParticularHead(atom,child));
			}
		}
		return result;
		
	}
				
		
	// return formulas that are potential candidates for matching (correct structure)
		public List<OWLFormula> matchCandidates (OWLFormula formula){
			LinkedList<OWLAtom> list = formula.getBFLinearisation();
			// this is just for testing!
			List<OWLFormula> candidates = matchCandidates(formula,list, root);
			/////// below was used for debugging only!
			// for (int i = 0; i < candidates.size();i++){
			// 	for (int j = 0; j < candidates.size();j++){
			//		if (i!=j && candidates.get(i).equals(candidates.get(j))){
			//			System.out.println(this.toString());
			//			System.out.println("ALERT! " + candidates.get(i));
			//			throw new RuntimeException();
			//		}
			//	}
			//}
			return candidates;
			// HashSet<TermNode> rootalternativeset = root.getAlternatives();
			// for (TermNode tn: rootalternativeset){
			// 	return matchCandidates(formula,list, tn);
			// }
			// return new ArrayList<OWLFormula>();
		}
		
		public List<OWLFormula> matchCandidates (OWLFormula formula, LinkedList<OWLAtom> list, TermNode node){
			// System.out.println("matchCandidates called with formula " + formula + " atomlist " + list + " TermNode with head " + node.getHead());
			if (list.size()<=0 || node==null){
				// System.out.println(" list is of null size");
				return new ArrayList<OWLFormula>(); 
			}
			if (list.size()==1){ // base case (last atom to be matched)
				// System.out.println("base case");
				HashSet<TermNode> children = node.getAlternatives();
				// base case with var
				if(list.get(0) instanceof OWLVar || list.get(0) instanceof OWLRoleVar){
					ArrayList<OWLFormula> newlist = new ArrayList<OWLFormula>();
					for (TermNode tn : children){
						HashSet<OWLFormula> accumulator = new HashSet<OWLFormula>(); 
						getAllFormulas(tn, accumulator);
						newlist.addAll(accumulator);
					}
					// System.out.println("returned list base case " + newlist.toString());
					return newlist;
					}
				// base case without var
				TermNode right_child = null;
				for (TermNode tn : children){
					if (tn instanceof TermNodeLeaf 
							&& tn.getHead().equals(list.get(0)) 
							&& ((TermNodeLeaf) tn).getFormulas().contains(formula)){
						right_child = tn;
						break;
					}
				}
				if (right_child == null){
					return new ArrayList<OWLFormula>();
				} else {
					ArrayList<OWLFormula> newlist = new ArrayList<OWLFormula>();
					newlist.addAll(((TermNodeLeaf) right_child).getFormulas());
					return newlist;
				}
			} else{ // recursive case
				// System.out.println("List " + list);
				// find correct child
				HashSet<TermNode> children = node.getAlternatives();
				
				// var case
				if(list.get(0) instanceof OWLVar || list.get(0) instanceof OWLRoleVar){
					// System.out.println("List " + list);
					ArrayList<OWLFormula> newlist = new ArrayList<OWLFormula>();
					if (!(children==null || children.size()==0)){
						list.remove(0);
						for (TermNode tn : children){
							// System.out.println(" looping for " + tn);
							// System.out.println("before removal " + list);
							// System.out.println("after removal " + list);
							
							/* HERE!!! */
							
							List<OWLFormula> reclist = matchCandidates(formula,list,tn);
							
							///// ---> the below was removed because it produced duplicates!
							HashSet allFormulas = new HashSet<OWLFormula>();
							// System.out.println(" tn " + tn);
							getAllFormulas(tn,allFormulas);   // <--- this is a lot; is this really necessary?? apparently yes, as soon as a var appears, not much can be done
							
							
							if (!(reclist==null)){
								allFormulas.addAll(reclist);
								// System.out.println("reclist " + reclist);
							}
							
							
							// System.out.println(allFormulas.toString());
							newlist.addAll(allFormulas);
							
						}
					}
					// System.out.println("var case, returning: " + newlist);
					return newlist;		
				}
				
				// non-var case
				TermNode right_child = null;
				for (TermNode tn : children){
					// System.out.println("comparing " + tn.getHead() + " and " + list.get(0) + " result : " + tn.getHead().equals(list.get(0)));
					if (tn.getHead().equals(list.get(0))){
						right_child = tn;
						break;
					}
				}
					if (right_child == null){ // not found
						// System.out.println("child not found");
						return new ArrayList<OWLFormula>(); 
					} else{ // was found
						// System.out.println("child was found: " + right_child);
						list.remove(0);
						List<OWLFormula> results = matchCandidates(formula,list,right_child);
						// System.out.println("recursive result" + results);
						return results;
				}
			} // end recursive case
			
		}
		
		public List<OWLFormula> findMatchingFormulas (OWLFormula formula){ 
			// System.out.println("Called findMatchingFormulas for " + formula);
				List<OWLFormula> candidates = matchCandidates(formula);
				
				// System.out.println("candidates for matching: "+ candidates);
				ArrayList<OWLFormula> results = new ArrayList<OWLFormula>();
				for (OWLFormula candidate : candidates){
					try {
						 candidate.match(formula);
						// System.out.println("candidate did match");
						results.add(candidate);
					} catch (Exception e) {
						  // System.out.println("candidate " + candidate.prettyPrint() + " did not match " + formula);
						// do nothing if does not match
					}
				}
				// System.out.println("tried to match formula " + formula);
				// System.out.println("findMatchingFormulas returning results " + results);
				return results;
		}
		
	
	// removing a formula
		public boolean remove (OWLFormula formula){
			LinkedList<OWLAtom> list = formula.getBFLinearisation();
			return remove(formula, list, root);
		}
		
		public boolean remove (OWLFormula formula, LinkedList<OWLAtom> list, TermNode node){
		// System.out.println("called REMOVE with formula " + formula + " atomlist " + list + " TermNode " + node);
			if (list.size()==0 || node==null){
				return false; 
			}
			if (list.size()==1){ // base case (one element in atom list)
				HashSet<TermNode> children = node.getAlternatives();
				TermNode right_child = null;
				for (TermNode tn : children){
					// System.out.println("comparing " + tn.getHead().toString() + " and " +  list.get(0));
					if (tn instanceof TermNodeLeaf 
							&& tn.getHead().equals(list.get(0)) 
							&& ((TermNodeLeaf) tn).getFormulas().contains(formula)){
						right_child = tn;
						break;
					}
				}
				// System.out.println("correct child " + right_child);
				// System.out.println("correct child " + ((TermNodeLeaf) right_child).getFormulas());
				// System.out.println("term IDS " + termIds);
				if (right_child == null){
					return false;
				} else {
					// now do the actual work!!!
					if (((TermNodeLeaf) right_child).getFormulas().size()>1){
						// removing entry from id maps
						termIds.remove(formulaToIDMapping.get(formula));
						formulaToIDMapping.remove(formula);
						// remove the node
						((TermNodeLeaf) right_child).getFormulas().remove(formula);
					} else{
						node.getAlternatives().remove(right_child);
					}
					// System.out.println("term IDS " + termIds);
					return true;
				}
			} else{ // recursive case 
				// find correct child
				HashSet<TermNode> children = node.getAlternatives();
				TermNode right_child = null;
				for (TermNode tn : children){
					// System.out.println("comparing " + tn.getHead().toString() + " and " +  list.get(0));
					if (tn.getHead().equals(list.get(0))){
						right_child = tn;
						break;
					}
				}
					if (right_child == null){ // not found
						// System.out.println("child not found");
						return false;
					} else{ // was found
						// System.out.println("child was found");
						list.remove(0);
						boolean result = remove(formula,list,right_child);
						// tidy up in case we have created an orphan
						if (result && (right_child.getAlternatives() == null || right_child.getAlternatives().size()==0)){
							node.getAlternatives().remove(right_child);
						}
						return result;
				}
			} // end recursive case
			
		}
		
	
	
	public HashSet<OWLFormula> getAllFormulas(){
		HashSet<OWLFormula> result = new HashSet<OWLFormula>();
		getAllFormulas(root,result);
		return result;
	}
	
	public void getAllFormulas(TermNode node, HashSet<OWLFormula> accumulator){
		if (node == null){return; }
		if (node instanceof TermNodeLeaf){
			if (((TermNodeLeaf) node).getFormulas()!=null){
				// System.out.println("TT DBG FETCHING FORMULAS " + ((TermNodeLeaf) node).getFormulas());
			accumulator.addAll(((TermNodeLeaf) node).getFormulas());
			}
		} else{
			if (node.getAlternatives() != null){
			for (TermNode child : node.getAlternatives()){
				getAllFormulas(child,accumulator);
			}
			}
		}
	}
	
	
	/* OLD IDEA THAT WAS FLAWED
	public boolean contains (OWLFormula formula, TermNode node){
		OWLAtom formulahead = formula.getHead();
		ArrayList<OWLFormula> formulatail = formula.getArgs();
		OWLAtom nodehead = node.getHead();
		HashSet<ListNode> alternatives = node.getAlternatives();
		// System.out.println("formula head " + formulahead);
		// System.out.println("node head " + nodehead);
		if(formulahead instanceof OWLVar){
			return true;
		}
		if (!formulahead.equals(nodehead)){
			// System.out.println("not identical");
			return false;
		}
		boolean contained = false;
		// System.out.println(alternatives.size());
		if (alternatives.size()==0 && formulatail.size()==0){ return true; }
		for (ListNode ln : alternatives){
			if (formulatail.size() == ln.getList().size()){
				// System.out.println(" ln.getList().size() " + ln.getList().size());
				boolean identical = true;
				for (int i = 0; i < formulatail.size(); i++){
					if (!contains(formulatail.get(i), ln.getList().get(i))){
						identical = false;
						// System.out.println(contains(formulatail.get(i), ln.getList().get(i)));
					}
					}
				if (identical){
					contained = true;
				}	
				}		
		}
		return contained;	
		}
		*/
	
	@Override
	public String toString() {
		if (root==null)
			return "Empty tree";
		return TermNode.print(root,0);
	}
	
	@Override
	public TermTree clone(){
		if (root == null){
			return new TermTree();
		}
		TermNode newroot = root.clone();
		HashMap<Integer,OWLFormula> cl1 = (HashMap<Integer,OWLFormula>) termIds.clone();
		HashMap<OWLFormula,Integer> cl2 = (HashMap<OWLFormula, Integer>) formulaToIDMapping.clone();
		return new TermTree(newroot, cl1, cl2, last_term_id);
	}
	
	
	
	public int formulaGetID(OWLFormula formula){
		/* 
		for (OWLFormula form : formulaToIDMapping.keySet()){
			System.out.println("Candidate form" + form);
			System.out.println(form.equals(formula));
			System.out.println(form.hashCode());
			System.out.println(formula.hashCode());
		} */
		// System.out.println("formulaGetID called with formula " + formula);
		// System.out.println("Mapping " + formulaToIDMapping);
		// System.out.println(formulaToIDMapping.get(formula));
		if (formula==null)
		{System.out.println("formula null");}
		if (formulaToIDMapping.get(formula)==null){
			System.out.println("Mapping " + formulaToIDMapping);
			System.out.println("formulaGetID called with formula " + formula);
		}
		int id = formulaToIDMapping.get(formula);
		return id;
	}
	
	public OWLFormula getFormula(int i){
		// System.out.println("found: " + termIds.get(i));
		return termIds.get(i);
	}
	
	public Comparator<OWLFormula> formulaDescComparator() {
	    return new Comparator<OWLFormula>() {
	      // @Override
	      @Override
		public int compare(OWLFormula first, OWLFormula second) {
	        return Integer.valueOf(formulaGetID(second)).compareTo(Integer.valueOf(formulaGetID(first)));
	      }
	    };
	  }
	
	// recursive walk through OWLFormula, to collect all class names
	public static Set<OWLClassName> decomposeClassNames(OWLFormula form){
		Set<OWLClassName> classnames = new HashSet<OWLClassName>();
		if (form.isClassFormula()){
			if (form.getHead() instanceof OWLClassName)
				classnames.add((OWLClassName) form.getHead());
		}
		else{
			List<OWLFormula> subforms = form.getArgs();
			if (subforms!=null){
				for (OWLFormula subform : subforms)
					classnames.addAll(decomposeClassNames(subform));
			}
		}
		return classnames;
	}
		
	public Set<OWLClassName> getAllClassNames(){
		Set<OWLFormula> all_formulas = getAllFormulas();
		Set<OWLClassName> all_classes = new HashSet<OWLClassName>();
		for (OWLFormula formula : all_formulas){
			all_classes.addAll(decomposeClassNames(formula));
		}
		return all_classes;
	}
	
	public String printIDMapping(){
		return formulaToIDMapping.toString();
	}

}
