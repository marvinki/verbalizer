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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;


public enum InferenceApplicationService {
INSTANCE;

private int last_sequentid = 0;

public int generateSequentID(){
	last_sequentid = last_sequentid + 1;
	return last_sequentid;
}

public class UselessInferenceException extends Exception{}

/* Each rule has its generic method findRuleBindings to check if it can be applied to a sequent. This is applied to all open nodes, 
   and the resulting bindings are returned. The method passes on a flag whether it suffices to return one rule binding
*/ 
public List<RuleBindingForNode> findRuleBindingsWhereInferenceApplicable (ProofTree tree, SequentInferenceRule rule, boolean... saturation){
	ArrayList<SequentPositionInNode> sequentPositionsInNode = new ArrayList<SequentPositionInNode>();
	List<ProofNode> open_nodes = tree.getOpenNodes();
	List<RuleBindingForNode> rule_bindings = new ArrayList<RuleBindingForNode>();
	// for each open node find bindings
	for(ProofNode open_node : open_nodes){
		Sequent sequent = ((ProofNode<Sequent,java.lang.String,SequentPosition>) open_node).getContent();
		List<RuleBinding> bindings = new ArrayList<RuleBinding>();
		// System.out.println("DEBUG (Inference appl.): before getting bindings in findRuleBindingsWhereInferenceApplicable");
		if (saturation.length>0){ 
			bindings =  rule.findRuleBindings(sequent,true);
			}
		else { 
			bindings =  rule.findRuleBindings(sequent);
			}
		// System.out.println("DEBUG (Inference appl.): after getting bindings in findRuleBindingsWhereInferenceApplicable " + bindings);
		for (RuleBinding binding : bindings){
			// System.out.println("DEBUG (Inference appl.): where appli: new antecedent " + binding.getNewAntecedent());
			// turn binding into binding with node id.
			RuleBindingForNode binding_fornode = new RuleBindingForNode(open_node.getId(),binding,binding.getNewAntecedent(),binding.getNewSuccedent());
			rule_bindings.add(binding_fornode);
			// System.out.println("DEBUG (Inference appl.):  where appli: new antecedent " + binding_fornode.getNewAntecedent());
		}
	}
	return rule_bindings;
}

public List<RuleBindingForNode> findRuleBindingsWhereInferenceApplicableDepthLimited (ProofTree tree, SequentInferenceRule rule, int bfslevel, boolean... saturation){
	ArrayList<SequentPositionInNode> sequentPositionsInNode = new ArrayList<SequentPositionInNode>();
	List<ProofNode> open_nodes = tree.getOpenNodes();
	List<RuleBindingForNode> rule_bindings = new ArrayList<RuleBindingForNode>();
	// for each open node find bindings
	for(ProofNode open_node : open_nodes){
		Sequent sequent = ((ProofNode<Sequent,java.lang.String,SequentPosition>) open_node).getContent();
		// Optimization! This is one difference to above!
		Sequent amputatedSequent = sequent.amputateDepth(bfslevel);
		// Sequent amputatedSequent = sequent;
//		System.out.println("amputated sequent, depth " + bfslevel + " "+ sequent);
		List<RuleBinding> bindings = new ArrayList<RuleBinding>();
		// System.out.println("DEBUG (Inference appl.): before getting bindings in findRuleBindingsWhereInferenceApplicable");
		if (saturation.length>0){ 
			bindings =  rule.findRuleBindings(amputatedSequent,true);
			}
		else { 
			bindings =  rule.findRuleBindings(amputatedSequent);
			}
		// System.out.println("DEBUG (Inference appl.): after getting bindings in findRuleBindingsWhereInferenceApplicable " + bindings);
		// check which bindings are obviously redundant
		List<RuleBinding> tmpBindings = new ArrayList(bindings);
		bindings= new ArrayList<RuleBinding>();
		for (RuleBinding tmpBin : tmpBindings){
			if (tmpBin.getNewAntecedent()!=null && sequent.alreadyContainedInAntecedent(tmpBin.getNewAntecedent()))
				continue;
			 	bindings.add(tmpBin);
		}
		// Now that we know there are bindings, we redo everything with the real sequent.
		if (bindings.size()>0){
			if (saturation.length>0){ 
				bindings =  rule.findRuleBindings(sequent,true);
				}
			else { 
				bindings =  rule.findRuleBindings(sequent);
				}	
		}
		
		for (RuleBinding binding : bindings){
				
			// System.out.println("DEBUG (Inference appl.): where appli: new antecedent " + binding.getNewAntecedent());
			// turn binding into binding with node id.
			RuleBindingForNode binding_fornode = new RuleBindingForNode(open_node.getId(),binding,binding.getNewAntecedent(),binding.getNewSuccedent());
			
			rule_bindings.add(binding_fornode);
			// System.out.println("DEBUG (Inference appl.):  where appli: new antecedent " + binding_fornode.getNewAntecedent());
		}
	}
	return rule_bindings;
}

public List<RuleBindingForNode> findRuleBindingsWhereInferenceApplicableDepthLimited (ProofNode open_node, Sequent sequent, SequentInferenceRule rule, int bfslevel, boolean... saturation){
	ArrayList<SequentPositionInNode> sequentPositionsInNode = new ArrayList<SequentPositionInNode>();
	// List<ProofNode> open_nodes = tree.getOpenNodes();
	List<RuleBindingForNode> rule_bindings = new ArrayList<RuleBindingForNode>();
	// for each open node find bindings
	// for(ProofNode open_node : open_nodes){
		// Optimization! This is one difference to above!
		Sequent amputatedSequent = sequent;
		// Sequent amputatedSequent = sequent;
		// System.out.println("amputated sequent, depth__ " + bfslevel + " "+ sequent);
		List<RuleBinding> bindings = new ArrayList<RuleBinding>();
		// System.out.println("DEBUG (Inference appl.): before getting bindings in findRuleBindingsWhereInferenceApplicable");
		if (saturation.length>0){ 
			bindings =  rule.findRuleBindings(amputatedSequent,true);
			}
		else { 
			bindings =  rule.findRuleBindings(amputatedSequent);
			}
		if (bindings.size()==0){
			return rule_bindings;
		}
		// System.out.println("DEBUG (Inference appl.): after getting bindings in findRuleBindingsWhereInferenceApplicable " + bindings);
		// check which bindings are obviously redundant
		List<RuleBinding> tmpBindings = new ArrayList(bindings);
		bindings= new ArrayList<RuleBinding>();
		for (RuleBinding tmpBin : tmpBindings){
			if (tmpBin.getNewAntecedent()!=null && sequent.alreadyContainedInAntecedent(tmpBin.getNewAntecedent()))
				continue;
			 	bindings.add(tmpBin);
		}
		
		// Now that we know there are bindings, we redo everything with the real sequent.
		sequent = (Sequent) open_node.getContent(); // this is evil, we are really using a different sequent here!
		
		// System.out.println("Rule " + rule.getName());
		
		boolean ok = true;
		List<RuleBindingForNode> rule_bindings_alt = new ArrayList<RuleBindingForNode>();
		for (RuleBinding rb2: bindings){
			// System.out.println("rule " + rule.getName() + " old " + rb2);
			RuleBinding newBinding = rb2.convert(amputatedSequent, sequent);
			// System.out.println("new " + newBinding + " with conclusion  " + newBinding.getNewAntecedent());
			if (newBinding!=null){
				RuleBindingForNode binding_fornode = new RuleBindingForNode(open_node.getId(),
						newBinding,newBinding.getNewAntecedent(),newBinding.getNewSuccedent());
				rule_bindings_alt.add(binding_fornode);
			} else{ 
				ok = false;
			}
		}
		if (        rule.getName().equals("SimpleTermination")
			    ||	rule.getName().equals("TerminationAxiom")
			    ||	rule.getName().equals("Topintro")
			    ||	rule.getName().equals("Botintro")
			    ||	rule.getName().equals("R0")
				|| rule.getName().equals("Subclass-chain")
				|| rule.getName().equals("INLG2012NguyenEtAlRule23Repeat")
				|| rule.getName().equals("R5M")
				|| rule.getName().equals("INLG2012NguyenEtAlRule12")
				|| rule.getName().equals("INLG2012NguyenEtAlRule2")
				|| rule.getName().equals("INLG2012NguyenEtAlRule5")
				|| rule.getName().equals("INLG2012NguyenEtAlRule15")
				|| rule.getName().equals("ONLYSOME")
				|| rule.getName().equals("EQUIVEXTRACT")
				|| rule.getName().equals("INLG2012NguyenEtAlRule23")
				|| rule.getName().equals("INLG2012NguyenEtAlRule37")
				|| rule.getName().equals("INLG2012NguyenEtAlRule42")
				|| rule.getName().equals("INLG2012NguyenEtAlRule34")
				|| rule.getName().equals("obj-wit")
				|| rule.getName().equals("UNIONINTRO")
				|| rule.getName().equals("AdditionalDLRules-DefinitionOfDomain")
				|| rule.getName().equals("ELEXISTSMINUS")
				|| rule.getName().equals("AdditionalDLRules-SubclassAndEquiv")
				// Individuals stuff
				|| rule.getName().equals("Individual")
				|| rule.getName().equals("Indiv Topintro")
				){
			 return rule_bindings_alt;
		}
		
		if (ok && (rule.getName().equals("INLG2012NguyenEtAlRule12")
				|| rule.getName().equals("INLG2012NguyenEtAlRule15")
				|| rule.getName().equals("Botintro")
				|| rule.getName().equals("Topintro")
				|| rule.getName().equals("R0")
				|| rule.getName().equals("AdditionalDLRules-Propchain")
				|| rule.getName().equals("Additional-Forall-Union")
				|| rule.getName().equals("INLG2012NguyenEtAlRule5")
				)
				){
			 return rule_bindings_alt;
		}
		
	   
		if (bindings.size()>0){
			if (saturation.length>0){ 
				// System.out.println("Actually redoing, rule " + rule.getName());
				bindings =  rule.findRuleBindings(sequent,true);
				}
			else { 
				// System.out.println("Actually redoing, rule " + rule.getName());
				bindings =  rule.findRuleBindings(sequent);
				}	
		}
		
		for (RuleBinding binding : bindings){
			RuleBindingForNode binding_fornode = new RuleBindingForNode(open_node.getId(),binding,binding.getNewAntecedent(),binding.getNewSuccedent());
			// need to check if this corresponds to what we computed before!
			boolean isOk = false;
			for (RuleBinding rb2: tmpBindings){
				// TODO! Need to test real equality here!
				// System.out.println(rb2.getNewAntecedent());
				if (rb2.getNewAntecedent()!=null && rb2.getNewAntecedent().equals(binding_fornode.getNewAntecedent())
						|| rb2.getNewAntecedent()==null
						)
					isOk = true;
			}
			if (isOk)
				rule_bindings.add(binding_fornode);
			// System.out.println("DEBUG (Inference appl.):  where appli: new antecedent " + binding_fornode.getNewAntecedent());
		}
	// }
		// System.out.println("Rule bindings " + rule_bindings);
		// System.out.println("Rule bindings alt: " + rule_bindings_alt);
		return rule_bindings;
}


public List<JustificationNode> getAllJustificationsOfAKind(ProofTree tree, java.lang.String name){
	List<JustificationNode> results = new ArrayList<JustificationNode>();
	Collection<ProofNode> collection = tree.getAllProofNodes(); 
	for(ProofNode node:collection){
		List<HierarchNode> hierarchnodes = node.getJustifications();
		for(HierarchNode hnode: hierarchnodes){
			List<JustificationNode> justlist = hnode.getJustifications();
			for(JustificationNode just: justlist){
				if(just.getJustification().equals(name)){
					results.add(just);
				}
			}
		} 
	}
	return results;
}

public void applySequentInferenceRule(ProofTree tree, RuleBindingForNode binding, SequentInferenceRule rule, HierarchNode... hnode) 
		throws UselessInferenceException{
	// System.out.println("apply seq inf called");
	// generate new sequents & proof nodes
	// System.out.println("applyInference rule : " + rule.getName() + " newant: " + binding.getNewAntecedent());
	// + binding.getNewAntecedent());
	// DEBUG OUTPUT
	// if (!(binding.getNewAntecedent()==null)){
	// 	System.out.println(binding.getNewAntecedent().toOWLAPI().accept(new PrettyPrintOWLObjectVisitor()));
	// }
	// .accept(new PrettyPrintOWLObjectVisitor())
	ProofNode node = tree.getProofNode(binding.getNodeId());
	Sequent sequent = (Sequent) node.getContent();
	// System.out.println("apply inf rule with antecedent " + sequent.getAllAntecedentOWLFormulas());
	// List<Sequent> premiseSequents = new ArrayList<Sequent>();
	SequentList premiseSequents = null;
	try {
		// System.out.println(binding.getRuleBinding());
		// System.out.println("debug breakpoint 1");
		premiseSequents = rule.computePremises(sequent, binding.getRuleBinding());
		// System.out.println("debug breakpoint 1 (a)");
		// System.out.println("premise sequents " + premiseSequents);
		// System.out.println("premise sequents " + premiseSequents.getSequents().get(0).getAllAntecedentOWLFormulas());
		assert(premiseSequents!=null);
		// if (premiseSequents.size()==0) throw new UselessInferenceException();
	} catch (Exception e) {
		//  Auto-generated catch block
		e.printStackTrace();
	}
	// System.out.println(premiseSequents.getSequents());
	if (premiseSequents == null || premiseSequents.getSequents().size()==0){
//		System.out.println("STH WICKED HAPPENED");
//		System.out.println("-- rule: " + rule.getName());
		throw new UselessInferenceException();
	}
	if (premiseSequents == null || premiseSequents.getSequents().size()==0) throw new UselessInferenceException();
//	if (premiseSequents.getSequents().get(0).equals(sequent)){System.out.println("FOO " + premiseSequents.getSequents().get(0) + " AND " + sequent);}
	if (premiseSequents.getSequents().get(0).equals(sequent)){throw new UselessInferenceException();}
	// this is the case where a termination axiom has applied
		if (premiseSequents.getSequents().size()==1 && 
				premiseSequents.getSequents().get(0).getAllAntecedentOWLFormulas().size()==0
				&& premiseSequents.getSequents().get(0).getAllSuccedentOWLFormulas().size()==0
				// premiseSequents.getSequents().get(0).getAntecedent().size()==0
				// && premiseSequents.getSequents().get(0).getSuccedent().size()==0
				){
			// close a node with an empty justification
			JustificationNode emptyjustification = new JustificationNode();
			emptyjustification.setJustification(rule.getName());
			HierarchNode hier = new HierarchNode();
			hier.getJustifications().add(emptyjustification);
			node.addHierarchNode(hier);
			tree.getOpenNodes().remove(node);
			tree.setNewestJustification(emptyjustification);
			return;
		}
		// otherwise
	Iterator<Sequent> it = premiseSequents.getSequents().iterator();
	ArrayList<ProofNode> premisenodes = new ArrayList<ProofNode>();
	ProofNode<Sequent,java.lang.String,SequentPosition> newnode;
	while(it.hasNext()){
		newnode = new ProofNode<Sequent,java.lang.String,SequentPosition>(it.next(),null,tree.getId());
		tree.addNode(newnode);
		premisenodes.add(newnode);
	}
	// now handle the special case (dealing with quantifiers) where something like a t-rule applies 
	/* if (rule.getClass()==DiamondLeftSequentInferenceRule.class || 
			rule.getClass()==BoxRightSequentInferenceRule.class || 
			rule.getClass()==NegDiamondRightSequentInferenceRule.class || 
			rule.getClass()==NegBoxLeftSequentInferenceRule.class ||
			rule.getClass()==QuantifierMetaRule.class){
		// now we add the tbox to the antecedent of every premise!
		for(ProofNode proofnode: premisenodes){
			Sequent seq =  (Sequent) proofnode.getContent();
			seq.getAntecedent().addAll(tree.getTBox());
		}
	}
	*/
	// determine if we need an OR branch
		if (premiseSequents.isTypeAND()){
			// AND case
		JustificationNode justificationnode = new JustificationNode();
		List<Integer> premisenodeids = new ArrayList<Integer>();
		for(ProofNode nod : premisenodes){
			premisenodeids.add(nod.getId());
		}
		justificationnode.setPremises(premisenodeids);
		justificationnode.setJustification(rule.getName());
		//
		// System.out.println("setting positions " + binding);
		justificationnode.setPositions(binding);
		justificationnode.setJustifiedNode(node.getId());
		justificationnode.setInferenceRule(rule);
		// if we are dealing with a tactic expansion, need to add to the same HierarchNode
		HierarchNode new_h;
		if (hnode.length>0){
			new_h = hnode[0];
		} else {
		new_h = new HierarchNode();}
		new_h.getJustifications().add(justificationnode);
		// if the hnode exists already, we do not need to link it
		if (hnode.length==0){tree.linkPremises(node, new_h);}
		tree.setNewestJustification(justificationnode);
		} else {
			// OR case
			// List<HierarchNode> hnodelist = new ArrayList<HierarchNode>();
			
			int count = 0;
			for(ProofNode nod : premisenodes){
				List<Integer> premisenodeids = new ArrayList<Integer>();
				premisenodeids.add(nod.getId());
				JustificationNode justificationnode = new JustificationNode();
				java.lang.Class inferenceclass = premiseSequents.getRulelist().get(count);
				Object[] enums = inferenceclass.getEnumConstants();
				SequentInferenceRule r = (SequentInferenceRule) enums[0];
			    justificationnode.setJustification(r.getName());		
				justificationnode.setPremises(premisenodeids);
				justificationnode.setPositions(binding);
				justificationnode.setJustifiedNode(node.getId());
				justificationnode.setInferenceRule(rule);
				HierarchNode h_node = new HierarchNode();
				h_node.getJustifications().add(justificationnode);
				tree.linkPremises(node, h_node);
				tree.setNewestJustification(justificationnode);
				count++;
			}
		}
}

public void applySequentInferenceRuleToSaturate(ProofTree tree, RuleBindingForNode binding, SequentInferenceRule rule, HierarchNode... hnode) throws UselessInferenceException{
	// System.out.println("apply seq inf called");
	// generate new sequents & proof nodes
	ProofNode node = tree.getProofNode(binding.getNodeId());
	Sequent sequent = (Sequent) node.getContent();
	// List<Sequent> premiseSequents = new ArrayList<Sequent>();
	SequentList premiseSequents = null;
	try {
		// System.out.println(binding.getRuleBinding());
		premiseSequents = rule.computePremises(sequent, binding.getRuleBinding());
		// System.out.println(premiseSequents);
		assert(premiseSequents!=null);
		// if (premiseSequents.size()==0) throw new UselessInferenceException();
	} catch (Exception e) {
		//  Auto-generated catch block
		e.printStackTrace();
	}
	sequent = premiseSequents.getSequents().get(0);
}


public void applySequentInferenceRuleToFillGap(ProofTree tree, RuleBindingForNode binding, SequentInferenceRule rule,List<Integer> targetids, HierarchNode hnode) throws UselessInferenceException{
	// generate new sequents & proof nodes
	ProofNode node = tree.getProofNode(binding.getNodeId());
	Sequent sequent = (Sequent) node.getContent();
	// List<Sequent> premiseSequents = new ArrayList<Sequent>();
	SequentList premiseSequents = null;
	try {
		premiseSequents = rule.computePremises(sequent, binding.getRuleBinding());
		// if (premiseSequents.size()==0) throw new UselessInferenceException();
	} catch (Exception e) {
		//  Auto-generated catch block
		e.printStackTrace();
	}
	// the following should never happen, but we keep it nevertheless.
	if (premiseSequents == null||premiseSequents.getSequents().size()==0) throw new UselessInferenceException();
	if (premiseSequents.getSequents().get(0).equals(sequent)){throw new UselessInferenceException();}
	
	// one could now check whether the target nodes subsume the generated premises. This would be useful for debugging/making sure things are correct, but we omit this for the moment.
	
	JustificationNode justificationnode = new JustificationNode();
	List<Integer> premisenodeids = new ArrayList<Integer>();
	for(Integer i: targetids){
		premisenodeids.add(i);
	}
	
	justificationnode.setPremises(premisenodeids);
	justificationnode.setJustification(rule.getName());
	justificationnode.setPositions(binding);
	justificationnode.setJustifiedNode(node.getId());
	justificationnode.setInferenceRule(rule);
	hnode.getJustifications().add(justificationnode);
	tree.linkPremises(node, hnode);
	tree.setNewestJustification(justificationnode);
}

	// Queue is needed to weed out all steps that we might already have in the queue
	public List<ProofTree> computeAllNextSteps(ProofTree tree, List<SequentInferenceRule> rules, Queue<ProofTree> queue){
		// System.out.println("enter");
		List<ProofTree> resulttrees = new ArrayList();
		// loop for rules
		for (SequentInferenceRule rule : rules){
//			if (rule.getName()=="SubClassLeftSequent") {System.out.println("subclass left sequent is being tried");}
			 // System.out.println(rule.getName()); //
			 // List<SequentPositionInNode> positions = findPositionsWhereInferenceApplicable(tree, rule);
			 List<RuleBindingForNode> bindings = findRuleBindingsWhereInferenceApplicable(tree, rule);
			 // if (rule.getName()=="SubClassLeftSequent") {System.out.println(positions);}
			 // System.out.println(positions.size()); //
			 // System.out.println(bindings.size());
			 for (RuleBindingForNode rb : bindings){
				 ProofTree newtree = tree.deepCopy();
				 newtree.setId(ProofTreeIdService.INSTANCE.getNewId());
				 try{
					 applySequentInferenceRule(newtree,rb,rule);
					 if (newtree.getOpenNodes().size()<tree.getOpenNodes().size())
					 {
						resulttrees = new ArrayList();
						 resulttrees.add(newtree);
						 return resulttrees;
					 }
					 for (ProofTree othertree : queue){
						 // System.out.println("Comparison");
						 // othertree.print();
						 // newtree.print();
						 // System.out.println();
						 if (othertree.problemEquivalent(newtree)){
							 throw new UselessInferenceException();
						 }
					 }
					 resulttrees.add(newtree);
				 } catch (UselessInferenceException e){
//				     System.out.println("caught");
				 }
			 } // end loop for positions
		}	// end loop for rules
		// System.out.println("exit");
		return resulttrees;		 				 
	} 
	
	public ProofTree simpleBFS(ProofTree initialtree,List<SequentInferenceRule> rules, int limit){
		long starttime = System.currentTimeMillis();
		Queue<ProofTree> queue = new LinkedList<ProofTree>();
		queue.offer(initialtree);
		int nodecounter = 0;
		while(queue.size()>0){
			// System.out.println("Queue size " + queue.size());
			ProofTree currenttree = queue.poll();
			nodecounter++;
			List<ProofTree> next = computeAllNextSteps(currenttree,rules,queue);
			if (next.size()==0 || next.get(0)==null){
//			    System.out.println("Dead end");
			}
			else{ 
			ProofTree elem = next.get(0);
			// next = new ArrayList<ProofTree>();
			// next.add(elem);
			// System.out.println("Next size " + next.size());
			for (ProofTree tree: next){
				if (tree.getOpenNodes().size() == 0){
//					System.out.println();
//					System.out.println("Heureka, after stepping through " + nodecounter + " search nodes, finally found a proof!");
//					System.out.println();
					long endtime = System.currentTimeMillis();
//					System.out.println("Time taken was " + (endtime - starttime) + "ms.");
					return tree;
				} // endif
				// UNCOMMENT FOR DEBUG
				//  tree.print();
//				 System.out.println();
				if (nodecounter > limit){
					long endtime = System.currentTimeMillis();
//					System.out.println("Unsuccessful. Time taken was " + (endtime - starttime) + "ms.");
					return null;
					}
				queue.offer(tree);
			} // end for
			} // end if
		}
		return null;
		
	}
	
	// is used by the Horridge Movie example
	public ProofTree simpleNextTree(ProofTree tree,List<SequentInferenceRule> rules){
		ProofTree oneresult = null;
		List<ProofTree> results = computeAllNextSteps(tree, rules, new LinkedList<ProofTree>());
		if (!(results==null) || results.size()==0){
//			System.out.println("got a next step");
			oneresult = results.get(0);
			oneresult.print();
		}
		return oneresult;
	}
	
	
	private List<RuleBindingForNode> findRuleBindingWithMaxDepth(ProofTree tree, SequentInferenceRule rule, int bfsLevel, boolean...saturate){
		// System.out.println("find binding max depth " + rule.getName());
		/*
		List<RuleBindingForNode> all_bindings = findRuleBindingsWhereInferenceApplicableDepthLimited(tree, rule, bfsLevel, saturate);
		List<RuleBindingForNode> limited_bindings = new ArrayList<RuleBindingForNode>(); 
		for (RuleBindingForNode bin : all_bindings){
			ProofNode node = tree.getProofNode(bin.getNodeId());
			Sequent sequent = (Sequent) node.getContent();
			int depth = computeRuleBindingMaxDepth(sequent,bin);
			// System.out.println("depth " + depth);
			if (depth<=bfsLevel)
				limited_bindings.add(bin);
			// Satisficing!
			if (limited_bindings.size()>0)
				break;
		}
		// System.out.println("lim bindings size " + limited_bindings.size());
		 */
		List<RuleBindingForNode> limited_bindings = findRuleBindingsWhereInferenceApplicableDepthLimited(tree, rule, bfsLevel, saturate);
		return limited_bindings;
	}
	
	public Pair<List<RuleBindingForNode>,List<SequentInferenceRule>> findBindings(ProofNode opennode, Sequent seq, List<SequentInferenceRule> runningRules, int bfsLevel, boolean... saturate){
		SequentInferenceRule current_rule = null;
		List<RuleBinding> bindings = new ArrayList<RuleBinding>();
		List<SequentInferenceRule> rules = new ArrayList<SequentInferenceRule>();
		List<RuleBindingForNode> rule_bindings = new ArrayList<RuleBindingForNode>();
		while(runningRules.size()>0 && bindings.size()==0){
			current_rule = runningRules.get(0);				
			runningRules.remove(0);
			//  System.out.println("trying rule " + current_rule.getName());
			// positions = findPositionsWhereInferenceApplicable(initialtree, runningRules.get(0));
			// System.out.println("before getting bindings");
			// System.out.println("DEBUG runSimpleLoop trying: " + runningRules.get(0).getName());
			// List<RuleBindingForNode> bindings_pre = new ArrayList(); 
			List<RuleBindingForNode> foundBindings = new ArrayList<RuleBindingForNode>();
			if (saturate.length>0){	
					 // System.out.println("case1");
					//bindings.addAll(current_rule.findRuleBindings(seq));
					foundBindings = findRuleBindingsWhereInferenceApplicableDepthLimited(opennode, seq,current_rule, bfsLevel, true);
				} else 
					{	
					// bindings.addAll(current_rule.findRuleBindings(seq));
						//  System.out.println("case2");
				     foundBindings = findRuleBindingsWhereInferenceApplicableDepthLimited(opennode, seq,current_rule,bfsLevel);
							// System.out.println("case2 after");
						} 
			for (RuleBindingForNode binding : foundBindings){
				rule_bindings.add(binding);
				rules.add(current_rule);
			}
		}
			// System.out.println("returning result with " + current_rule.getName());
		return new Pair(rule_bindings,rules);
	}
	
	/* Original, strange
	public Pair<List<RuleBindingForNode>,List<SequentInferenceRule>> findBindings(ProofNode opennode, Sequent seq, List<SequentInferenceRule> runningRules, int bfsLevel, boolean... saturate){
		SequentInferenceRule current_rule = null;
		List<RuleBinding> bindings = new ArrayList<RuleBinding>();
		List<SequentInferenceRule> rules = new ArrayList<SequentInferenceRule>();
		List<RuleBindingForNode> rule_bindings = new ArrayList<RuleBindingForNode>();
		while(runningRules.size()>0 && bindings.size()==0){
			current_rule = runningRules.get(0);				
			runningRules.remove(0);
			  // System.out.println("trying rule " + current_rule.getName());
			// positions = findPositionsWhereInferenceApplicable(initialtree, runningRules.get(0));
			// System.out.println("before getting bindings");
			// System.out.println("DEBUG runSimpleLoop trying: " + runningRules.get(0).getName());
			// List<RuleBindingForNode> bindings_pre = new ArrayList(); 
			if (saturate.length>0){	
					// System.out.println("case1");
					//bindings.addAll(current_rule.findRuleBindings(seq));
					bindings.addAll(findRuleBindingsWhereInferenceApplicableDepthLimited(opennode, seq,current_rule, bfsLevel, true));
				} else 
					{	
					// bindings.addAll(current_rule.findRuleBindings(seq));
						// System.out.println("case2");
				     bindings.addAll(findRuleBindingsWhereInferenceApplicableDepthLimited(opennode, seq,current_rule,bfsLevel));
							// System.out.println("case2 after");
						} 
			// if (bindings.size()>0 ) return new Pair(bindings,current_rule);
			// System.out.println("now the bindings!");
			// System.out.println(bindings);
		}	 // end while
		for (RuleBinding binding : bindings){
			RuleBindingForNode binding_fornode = new RuleBindingForNode(opennode.getId(),binding,binding.getNewAntecedent(),binding.getNewSuccedent());
			// now put in
			List<RuleBinding> tmpBindings = new ArrayList(bindings);
			bindings= new ArrayList<RuleBinding>();
			rule_bindings.add(binding_fornode);
			rules.add(current_rule);
		}
		// System.out.println("returning result with " + current_rule.getName());
		return new Pair(rule_bindings,rules);
	}
	*/
	
	
	/*
	public ProofTree runSimpleLoop(ProofTree initialtree,
								List<SequentInferenceRule> rules, 
								int limit, 
								long timelimit, 
								boolean...saturate){
	long starttime = System.currentTimeMillis();
	int bfsLIMIT = 100;
	
	
	
	
	while (initialtree.getOpenNodes().size() > 0){
		  List<ProofNode> open_nodes = initialtree.getOpenNodes();
		  for (ProofNode opennode : open_nodes){
			  int bfsLevel = 0;
			  while (open_nodes.contains(opennode) && bfsLevel<bfsLIMIT){
		    	Sequent seq = (Sequent) opennode.getContent();
		    	seq = seq.amputateDepth(bfsLevel);
		    	List<SequentInferenceRule> runningRules = new ArrayList<SequentInferenceRule>(rules);
		    	Pair<List<RuleBindingForNode>,List<SequentInferenceRule>> bindings = 
		    			findBindings(opennode, seq, runningRules, bfsLevel, saturate);
		}
	}
	*/	
	
	public ProofTree runSimpleLoop(ProofTree initialtree,List<SequentInferenceRule> rules, int limit, long timelimit, boolean...saturate){
		
		AlreadyTriedCache.INSTANCE.reset();
		// System.out.println(AlreadyTriedCache.INSTANCE.toString());
		
		Pair<List<RuleBindingForNode>,List<SequentInferenceRule>> bindings_dash_save = null;
		long starttime = System.currentTimeMillis();
		boolean allTried = false;
		int bfsLevel = 0;
		while(limit>0 && System.currentTimeMillis()-starttime <timelimit && initialtree.getOpenNodes().size() > 0 && allTried==false){
			// find some rule that is applicable
			// List<SequentPositionInNode> positions = new ArrayList();
			// List<RuleBindingForNode> bindings = new ArrayList();
			SequentInferenceRule current_rule = null;
			allTried = true;
			List<SequentInferenceRule> runningRules = new ArrayList<SequentInferenceRule>(rules);
			// while(runningRules.size()>0 && positions.size()==0 && bindings.size()==0){
			    List<RuleBindingForNode> bindings = new ArrayList();
			    List<ProofNode> open_nodes = initialtree.getOpenNodes();
			    List<SequentInferenceRule> detectedRules = new ArrayList<SequentInferenceRule>();
			    for (ProofNode opennode : open_nodes){
			    	
			    	Sequent seq = (Sequent) opennode.getContent();
			    	// System.out.println(seq.getStatistics());
			    	if (seq instanceof IncrementalSequent){
			    		seq = ((IncrementalSequent) seq).amputateDepth(bfsLevel);
			    	} else{
			            seq = (Sequent) opennode.getContent();
			            seq = seq.amputateDepth(bfsLevel);
			    	}
			    	
			    	
			    	 // Sequent seq = (Sequent) opennode.getContent();
			    	 // seq = seq.amputateDepth(bfsLevel);
			    	
			    	// System.out.println("BFS LEVEL " + bfsLevel);
			    	// Debugging only
			    	// System.out.println(seq);
			    	 //Set<OWLFormula> anteceds=  seq.getAllAntecedentOWLFormulas();
			    	 //for (OWLFormula form: anteceds){
			    	//	System.out.print(VerbalisationManager.prettyPrint(form) + " ");
			    	 //   System.out.println(seq.getFormulaDepth(seq.antecedentFormulaGetID(form)));
			    	// }
			    	// Debugging end
			    	boolean empty =false;
			    	Pair<List<RuleBindingForNode>,List<SequentInferenceRule>> bindings_dash = null;
			    	/*
			    	if (bindings_dash_save!=null){
			    		List<RuleBindingForNode> saved = bindings_dash_save.t;
			    		if (saved.size()>0){
			    		bindings_dash = bindings_dash_save;}
			    		else empty = true;
			    	}
			    	*/
			    	if (bindings_dash==null || empty)
			    	bindings_dash = findBindings(opennode, seq, runningRules, bfsLevel, saturate);
			    	bindings = bindings_dash.t;
			    	detectedRules = bindings_dash.u;
			    } // end for 
			// System.out.println("after looping, binding size: " + bindings.size());    
			if (bindings.size()==0){ 
				bfsLevel=bfsLevel + 1;
				// System.out.println("BFS LEVEL NOW: " + bfsLevel);
				/// HACKY!
				if (bfsLevel > 30){
//					System.out.println("simpleLoop: Reached BFS bounds.");
					return initialtree;
				}
				// 
				allTried = false;
				runningRules = new ArrayList<SequentInferenceRule>(rules);
				continue;
			}
			if (bindings.size()>0){ 
				allTried = false;
				try {
					current_rule = 	detectedRules.get(0);
				// for (RuleBinding rb: bindings){
					// System.out.println(" in simple loop " + rb.getNewAntecedent());
				// }
					
					/* ENABLE THIS FOR DEBUG OUTPUT */
				   // System.out.println("ONE APPLICATION is carried out by runSimpleLoop");
				   // System.out.println("rulename " + current_rule.getName() + " at bfs level " + bfsLevel + " limit " + limit + " timelimit " + (timelimit - (System.currentTimeMillis()-starttime)));
				 
				   
				   // System.out.println(" in simple loop " + bindings.get(0).getNewAntecedent());
				// System.out.println(current_rule.getName() + "-->" + bindings.get(0).getNewAntecedent());
				   
				// for (RuleBindingForNode rb: bindings)
				//  	 applySequentInferenceRule(initialtree,rb,current_rule);
					
				// if (current_rule.getName().equals("INLG2012NguyenEtAlRule15")){
					
					/*
					Set<OWLFormula> conclusions = new HashSet<OWLFormula>();
					
					for (int h = 0; h<bindings.size();h++){
						RuleBindingForNode bind = bindings.get(h);
						current_rule = detectedRules.get(h);
						// if (!conclusions.contains(bind.getNewAntecedent()))
							applySequentInferenceRule(initialtree,bind,current_rule);
						// conclusions.add(bind.getNewAntecedent());
					}
					*/
					
				// }
				// else
				// 	applySequentInferenceRule(initialtree,bindings.get(0),detectedRules.get(0));
				
				//	applySequentInferenceRule(initialtree,bindings.get(0),current_rule);
				
				// if (bindings.size()>1)
				// 	applySequentInferenceRule(initialtree,bindings.get(1),detectedRules.get(1));
				// else 
				
				// System.out.println(detectedRules.get(0));
				// System.out.println(bindings.get(0).getNewAntecedent());
				// System.out.println(detectedRules.get(1));
				// System.out.println(bindings.get(1).getNewAntecedent());
				
				
				if (detectedRules.get(0).getName().equals("INLG2012NguyenEtAlRule1"))	
					{
					applySequentInferenceRule(initialtree,bindings.get(0),detectedRules.get(0));
					// System.out.println(current_rule.getName() + "-->" + bindings.get(0).getNewAntecedent());
					}
				else{
					/*
				if (bindings.get(0).getNewAntecedent()!=null 
						&& bindings.get(0).getNewAntecedent().toString().contains("Spatial")
						&& bindings.get(0).getNewAntecedent().toString().contains("419")
						&& bindings.get(0).getNewAntecedent().toString().contains("131")
						&& bindings.get(0).getNewAntecedent().toString().contains("771")
						&& bindings.get(0).getNewAntecedent().toString().contains("443")
						)
				 System.out.println(current_rule.getName() + "-->" + bindings.get(0).getNewAntecedent());
				*/
				
				for(int h = 0; h<bindings.size();h++){
					if (initialtree.getOpenNodes().size()==0)
						break;
					ProofNode newopennode = (ProofNode) initialtree.getOpenNodes().get(0);
					RuleBindingForNode rb = bindings.get(h);
					// System.out.println("checkpoint1 " + rb.getNewAntecedent());
					Sequent seqnew = (Sequent) newopennode.getContent();
					Sequent oldseq = (Sequent) initialtree.getProofNode(rb.getNodeId()).getContent();
					
					// System.out.println(seqnew.getStatistics());
					
					RuleBinding rbnew = rb.convert(oldseq, seqnew);
					// System.out.println("checkpoint2 " + rbnew.getNewAntecedent());
					if ( rbnew!=null && (rbnew.getNewAntecedent()==null 
							|| !seqnew.alreadyContainedInAntecedent(rbnew.getNewAntecedent()))
							// && !detectedRules.get(h).getName().equals("ELEXISTSMINUS")
							// && !detectedRules.get(h).getName().equals("INLG2012NguyenEtAlRule2")
							&& !detectedRules.get(h).getName().equals("INLG2012NguyenEtAlRule1")
							){
					RuleBindingForNode rbnewfn = new RuleBindingForNode(newopennode.getId(),rbnew);
					applySequentInferenceRule(initialtree,rbnewfn,detectedRules.get(h));
				    // System.out.println(detectedRules.get(h).getName() + "-->" + rbnewfn.getNewAntecedent().prettyPrint());
					/*
					if (rbnewfn.getNewAntecedent()!=null && rbnewfn.getNewAntecedent().toString().contains("Spatial")
							// && rbnewfn.getNewAntecedent().toString().contains("419")
							// && rbnewfn.getNewAntecedent().toString().contains("131")
							// && rbnewfn.getNewAntecedent().toString().contains("771")
							// && rbnewfn.getNewAntecedent().toString().contains("443")
							)
					   System.out.println(detectedRules.get(h).getName() + "-->" + rbnewfn.getNewAntecedent());
					*/
					
					// Sanity checking
					/*
					if (initialtree.getOpenNodes().size()>0){
						ProofNode node2 = (ProofNode) initialtree.getOpenNodes().get(0);
						Sequent seqnew_check = (Sequent) node2.getContent();
						// System.out.println("obtained new antecedent " + rbnewfn.getNewAntecedent().prettyPrint());
						// System.out.println("check report");
						seqnew_check.reportAntecedent();
						// System.out.println(((IncrementalSequent) seqnew_check).getAntecedentTree());
						try {
							((IncrementalSequent) seqnew_check).getAntecedentTree().insert(rbnewfn.getNewAntecedent());
						} catch (Exception e) {
							//  Auto-generated catch block
							e.printStackTrace();
						}
						if (!seqnew_check.alreadyContainedInAntecedent(rbnewfn.getNewAntecedent())){
//							System.out.println("reported not to be contained");
							throw new RuntimeException();
						}
						boolean notin = true;
						Set<OWLFormula> listantecedent = seqnew_check.getAllAntecedentOWLFormulas();
						for (OWLFormula form : listantecedent){
						if (form.equals(rbnewfn.getNewAntecedent())){
							notin = false;
						}
						}
						if (notin){
//							System.out.println("not in list");
							throw new RuntimeException();
						}
					} // end sanity checks
					*/
					
					}
				}
				
				}  //end else
				
				ProofNode newopennode = null;
				if(initialtree.getOpenNodes().size()>0)
					newopennode = (ProofNode) initialtree.getOpenNodes().get(0);
				if (newopennode!=null){
					Sequent obtainedsequent = (Sequent) newopennode.getContent(); 
					List<RuleBinding> terminationlist = AdditionalDLRules.SIMPLETERMINATION.findRuleBindings((Sequent) newopennode.getContent());
					// obtainedsequent.reportAntecedent();
					if (terminationlist.size()>0){
					// System.out.println("terminationlist " + terminationlist);
					RuleBindingForNode rbnewfn = new RuleBindingForNode(newopennode.getId(),terminationlist.get(0));
					applySequentInferenceRule(initialtree,rbnewfn,AdditionalDLRules.SIMPLETERMINATION);
					}
					
				}
				
				
				/*
				applySequentInferenceRule(initialtree,bindings.get(0),current_rule);
				if (bindings.size()>1 && initialtree.getOpenNodes().size()>0){
					ProofNode newopennode = (ProofNode) initialtree.getOpenNodes().get(0);
					RuleBindingForNode rb = bindings.get(1);
					rb.setNodeId(newopennode.getId());
					applySequentInferenceRule(initialtree,bindings.get(1),detectedRules.get(1));
				}
				*/
				// 	applySequentInferenceRule(initialtree,bindings.get(1),detectedRules.get(1));
				
				List<RuleBindingForNode> saved_rbs = bindings;
				 List<SequentInferenceRule> saved_rs = detectedRules;
				 saved_rbs.remove(0);
				 saved_rs.remove(0);
				 bindings_dash_save = new Pair(saved_rbs,saved_rs);
				
				if(initialtree.getOpenNodes().size()>0){
				int count = ((Sequent) ((ProofNode) initialtree.getOpenNodes().get(0)).getContent()).getAllAntecedentOWLFormulas().size();
	 			// System.out.println("COUNT " + count);
				}
			} catch (UselessInferenceException e) {
				//  Auto-generated catch block
				e.printStackTrace();
				return initialtree;
			}
			}
			limit--;
		}
		
		long endtime = System.currentTimeMillis();
		if (System.currentTimeMillis()-starttime >timelimit){
//			System.out.println("simpleLoop: Timelimit reached");
		} 
		// System.out.println("Time taken was " + (endtime - starttime) + "ms.");
					return null;
	}
	
	
	public ProofTree saturate (ProofTree initialtree,List<SequentInferenceRule> rules, int limit, boolean...saturate){
		long starttime = System.currentTimeMillis();
		// int roundrobin = 0;
		List<SequentInferenceRule> allRules = new ArrayList<SequentInferenceRule>(rules);
		HashMap<SequentInferenceRule,Long> timings = new HashMap<SequentInferenceRule,Long>();
		for(SequentInferenceRule rule : allRules){
			if (!timings.containsKey(rule)){
				timings.put(rule,new Long(0));
				// System.out.println(timings.get(rule));
			}
			// System.out.println(rule + " " + timings.get(rule));
		}
		while(limit>0 && initialtree.getOpenNodes().size() > 0){
			List<RuleBindingForNode> bindings = new ArrayList();
			SequentInferenceRule current_rule = null;
			SequentInferenceRule best_rule = null;
			List<SequentInferenceRule> runningRules = new ArrayList<SequentInferenceRule>(allRules);
			// for(SequentInferenceRule rule : allRules){
			// 	//  System.out.println(rule + " " + timings.get(rule));
			// }
			
			for (int i = 0; i< allRules.size();i++){
				for (int j = i; j< runningRules.size();j++){
					if (timings.get(runningRules.get(j))<timings.get(runningRules.get(i))){
						SequentInferenceRule tmp = runningRules.get(j);
						runningRules.set(j, runningRules.get(i));
						runningRules.set(i, tmp);
					}
			}
			}
			// System.out.println(runningRules);
			long starttimeOneIteration = System.currentTimeMillis();
			// System.out.println("best rule " + best_rule);
			    while(runningRules.size()>0 && bindings.size()==0){
			    	long starttimerule = System.currentTimeMillis();
				if (saturate.length>0){
					// System.out.println("Trying rule " + runningRules.get(0));
					bindings = findRuleBindingsWhereInferenceApplicable(initialtree, runningRules.get(0), true);
					// System.out.println("Bindings " + bindings.size());
				} else 
					{	
						bindings = findRuleBindingsWhereInferenceApplicable(initialtree, runningRules.get(0));
					} 
				long endtimerule = System.currentTimeMillis();
				if (bindings.size()>0){
					Long prevtimings = timings.get(runningRules.get(0));
					timings.put(runningRules.get(0), prevtimings + new Long((long) ((0.1) * ((endtimerule - starttimerule) - prevtimings))));
				} else{
					Long prevtimings = timings.get(runningRules.get(0));
					timings.put(runningRules.get(0), prevtimings + new Long((long) ((0.1) * ((new Long(100) + endtimerule - starttimerule) - prevtimings))));
				}
				// System.out.println("Evaluation ");
				// System.out.println(runningRules.get(0) + " " + timings.get(runningRules.get(0)));
				current_rule = runningRules.get(0);
				if(bindings.size()>0){
					break;
				}
				runningRules.remove(0);
			}
			long stoptimeOneIteration = System.currentTimeMillis();
			// System.out.println("Time for one iteration: " + (stoptimeOneIteration - starttimeOneIteration));
			if (bindings.size()==0){
				// System.out.println("Remaining open nodes: "+ initialtree.getOpenNodes().size());
				long endtime = System.currentTimeMillis();
				// System.out.println("Time taken was " + (endtime - starttime) + "ms.");
						return null;}; 
			if (bindings.size()>0){ try {
				 // System.out.println("Bindings found " + bindings.size());
				 // System.out.println("ONE APPLICATION is carried out by saturate");
				 // System.out.println("rulename " + current_rule.getName());
				 ProofNode node = initialtree.getProofNode(bindings.get(0).getNodeId());
			     Sequent sequent = (Sequent) node.getContent();
				 List<RuleApplicationResults> results = current_rule.computeRuleApplicationResults(sequent, bindings.get(0));
				 // do everything that is suggested
				 if (results==null || results.size()==0){
					 System.out.println("Rule application results are NULL");
				 }
				 for(RuleApplicationResults result : results){
					 Collection<java.lang.String> additionKeys = result.getAllAdditionKeys();
					 Collection<java.lang.String> deletionKeys = result.getAllDeletionsKeys();
					 // additions
					 for(java.lang.String str : additionKeys){
						 if (str.substring(0,1).equals("A")){
							 // now we have addition to antecedent
							 // System.out.println("DBG INFSERV ADDING " + result.getAddition(str));
							 sequent.addAntecedent((OWLFormula) result.getAddition(str));
						 } else { // now we have addition to succedent
							 sequent.addSuccedent((OWLFormula) result.getAddition(str));
						 }
					 }
					 // now the same for removals
					 for(java.lang.String str : deletionKeys){
						 if (str.substring(0,1).equals("A")){
							 // now we have addition to antecedent
							 sequent.removeOWLFormulaFromAntecedent((OWLFormula) result.getAddition(str));
						 } else { // now we have addition to succedent
							 sequent.removeOWLFormulaFromSuccedent((OWLFormula) result.getAddition(str));
						 }
					 }
				 }
				 int count = ((Sequent) ((ProofNode) initialtree.getOpenNodes().get(0)).getContent()).getAllAntecedentOWLFormulas().size();
		 			// System.out.println("COUNT " + count);
				 // applySequentInferenceRuleToSaturate(initialtree,bindings.get(0),current_rule);
			} catch (Exception e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
			}
			// initialtree.sequentDeltaPrint();
			limit--;
		}
//		System.out.println("Remaining open nodes: "+ initialtree.getOpenNodes().size());
		long endtime = System.currentTimeMillis();
//		System.out.println("Time taken was " + (endtime - starttime) + "ms.");
					return null;
	}
	
	
	public void determinePendingIterative(ProofTree tree, ProofNode node){
		if (tree.getOpenNodes().contains(node)){ // if a node is open, we are in some kind of leaf case
			tree.getPendingNodes().add(node);
		}
		else{
			boolean pending = true;
			// we recursively determine the status of the subtree(s)
			List<HierarchNode> alternative_hierarchnodes =  node.getJustifications(); // these are alternatives, only one needs to be non-pending
			for (HierarchNode h:alternative_hierarchnodes){
						List<JustificationNode> justificationnodes = h.getJustifications(); // each justification has a list of premises, i.e. pointers to proof nodes
						// these are hierarchical alternatives now. To assume a node is non-pending, only one alternative needs to be non-pending
						for (JustificationNode justnode : justificationnodes){
							List<Integer> premises = justnode.getPremises();
							// check if this justification has at least a pending node, in which case the justification should be considered pending
							boolean child_pending = false;
							for (int premise : premises){
								ProofNode childnode = tree.getProofNode(premise);
								// here is the recursive call
								determinePendingIterative(tree, childnode);
								if (tree.getPendingNodes().contains(childnode)){child_pending = true;}
							}
							if (!child_pending){pending = false;}  // if there is a non-pending justification, one alternative is fulfilled, thus the current node is not pending
						}
			}
			if (pending){
				tree.getPendingNodes().add(node);
			}
		}
	}
	
	public boolean proofIsFinished(ProofTree tree){
		determinePendingIterative(tree, tree.getRoot());
		return !(tree.getPendingNodes().contains(tree.getRoot()));
	}
	
	
	// returns relevant formulas from the subbranch (from (1) antecedent and (2) succedent)
	public Pair<List<OWLFormula>,List<OWLFormula>> eliminateIrrelevantParts (ProofTree tree, int nodeid) throws Exception{
		List<OWLFormula> list1 = new ArrayList<OWLFormula>();
		List<OWLFormula> list2 = new ArrayList<OWLFormula>();
		Pair<List<OWLFormula>,List<OWLFormula>> relevantFormulas = new Pair(list1,list2);
		ProofNode node = tree.getProofNode(nodeid);
		// === PENDING NODE ===
		if (tree.getPendingNodes().contains(node)){
			// We are at a pending node. If the proof is solved, then we are in an unsuccessful branch of some OR-alternative. 
			// Therefore we remove this node from the tree (but we do not yet bend the justification pointer to it, that's for the outer call to do)
			//	and return an empty relevance list, to signal back that this branch is irrelevant 
			tree.removeNode(node);
			relevantFormulas = new Pair(new ArrayList<OWLFormula>(),new ArrayList<OWLFormula>());
			return relevantFormulas;
		}
		List<HierarchNode> hnodes = node.getJustifications();
		List<HierarchNode> hnodes_to_be_removed = new ArrayList<HierarchNode>();
		for(HierarchNode hnode:hnodes){
			List<JustificationNode> justnodes = hnode.getJustifications(); //these are hierarchical alternatives, therefore choice should not matter
			// === LEAF NODE ===
			if (justnodes.size()>0 && justnodes.get(0).getPremises().size()==0){
				// need to identify the matching parts between antecedent and succedent
					// System.out.println("we are in the leafy part");
					Sequent sequent = (Sequent) node.getContent();
					Set<OWLFormula> antecedent = sequent.getAllAntecedentOWLFormulas();
					Set<OWLFormula> succedent = sequent.getAllSuccedentOWLFormulas();
					List<OWLFormula> matching = new ArrayList<OWLFormula>();
					for(OWLFormula form: antecedent){
						for(OWLFormula form2: succedent){
						// if (succedent.contains(form)){
						// 	matching.add(form);
						// }
							if (form.equals(form2)){
								matching.add(form);
							}
						}
					}
					list1.addAll(matching);
					list2.addAll(matching);
					// System.out.println("In the leafy part, we determined the following to be the relevant formulas: " + relevantFormulas);
					return relevantFormulas;
			}
			// === INNER NODE ===
			// System.out.println("we are dealing with an inner node!");
			JustificationNode justnode = justnodes.get(0); // here the assumption is that the node is not unjustified, and that the first element is the highest in the hierarchy, and that we do not need to consider other alternatives
			List<Integer> premises = justnode.getPremises();
			// now do the recursive calls
			List<OWLFormula> antecedent_relevant = new ArrayList<OWLFormula>();
			List<OWLFormula> succedent_relevant = new ArrayList<OWLFormula>();
			Integer one_relevant_child = null;
			Pair<List<OWLFormula>,List<OWLFormula>>  one_relevant_pair = null;
			for (Integer i: premises){
				Pair<List<OWLFormula>,List<OWLFormula>> relevantFormulas_i = eliminateIrrelevantParts(tree,i);
				// System.out.println(relevantFormulas_i);
				antecedent_relevant.addAll(relevantFormulas_i.t);
				succedent_relevant.addAll(relevantFormulas_i.u);
				if (relevantFormulas_i.t.size()>0 || relevantFormulas_i.u.size()>0){one_relevant_child = i;
				one_relevant_pair = relevantFormulas_i;
				}
			}
			// System.out.println(one_relevant_pair);
			// need to deal with the case that everything is irrelevant
			if (antecedent_relevant.size()==0 && succedent_relevant.size()==0){
				// now we need to signal that the OR alternative needs to be removed
				hnodes_to_be_removed.add(hnode);
			} else {
				// System.out.println("else case");
				// need to determine whether the results of rule application intersect with relevant formulas
				RuleBindingForNode rb_where_inference_was_applied  = (RuleBindingForNode) justnode.getPositions();
				SequentInferenceRule infrule = justnode.getInferenceRule();
				Sequent inf_sequent = (Sequent) node.getContent();
				// System.out.println(infrule);
				// System.out.println(inf_sequent);
				// System.out.println(rb_where_inference_was_applied.getRuleBinding().getBindings());
				List<RuleApplicationResults> infresults = infrule.computeRuleApplicationResults(inf_sequent, rb_where_inference_was_applied);
				// System.out.println(infresults);
				List<OWLFormula> additions1 = new ArrayList<OWLFormula>();
				List<OWLFormula> additions2 = new ArrayList<OWLFormula>();
				List<OWLFormula> additions3 = new ArrayList<OWLFormula>();
				for (RuleApplicationResults res: infresults){
					// System.out.println("Additions: " + res.getAdditions());
					additions1.addAll(res.getAdditions());
					additions2.addAll(res.getAdditions());
					additions3.addAll(res.getAdditions());
				}
				// now determine intersection
				additions1.retainAll(antecedent_relevant);
				additions2.retainAll(antecedent_relevant);
				if (additions1.size()==0 && additions2.size()==0){ // inference application is irrelevant!
					// bend justification of "node" to a child with relevance (as was indicated in recursive call)
					// System.out.println("BENDING! at node" + nodeid);
					node.setJustifications(tree.getProofNode(one_relevant_child).getJustifications());
					// need to remove all additions from children's antecedents and succedents
					removeFromSequentsRecursive(tree,one_relevant_child,additions3,new ArrayList<OWLFormula>()); // todo: need to split into antecedent and succedent
					removeFromSequentsRecursive(tree,one_relevant_child,new ArrayList<OWLFormula>(),additions3); // todo: need to split into antecedent and succedent
					// node.setContent(tree.getProofNode(one_relevant_child).getContent());
					// remove premises
					// System.out.print("premises to be removed: ");
					// System.out.println(premises);
					for(Integer i : premises){
						tree.removeNode(tree.getProofNode(i));
					}
					// return previously relevant formulae
					return one_relevant_pair;
				} else { // inference application is relevant!
					// need to add positions in rule binding to relevant pairs
					RuleBinding rb = rb_where_inference_was_applied.getRuleBinding();
					List<OWLFormula> formulas_ant = new ArrayList<OWLFormula>();
					List<OWLFormula> formulas_succ = new ArrayList<OWLFormula>();
					for (java.lang.String positions_str : rb.getBindings().keySet()){
						if (positions_str.contains("A")){
							SequentPosition pos1 = rb.get(positions_str);
							SequentPart part1 = pos1.getSequentPart();
							// single position case
							if (pos1 instanceof SequentSinglePosition){
								SequentSinglePosition pos1s = (SequentSinglePosition) pos1;
								if (part1.equals(SequentPart.ANTECEDENT)){
								// formulas_ant.add((OWLFormula) inf_sequent.getAntecedent().get(pos1s.getToplevelPosition()));
								formulas_ant.add(inf_sequent.antecedentGetFormula(pos1s.getToplevelPosition()));
								}
								if (part1.equals(SequentPart.SUCCEDENT)){
									// formulas_succ.add((OWLFormula) inf_sequent.getSuccedent().get(pos1s.getToplevelPosition()));
									formulas_succ.add(inf_sequent.succedentGetFormula(pos1s.getToplevelPosition()));
									}
							}
							if (pos1 instanceof SequentMultiPosition){
								SequentMultiPosition pos1m = (SequentMultiPosition) pos1;
								Integer[][] pos_ints = pos1m.getPositions();
								for (int i =0; i<pos_ints.length;i++){
										if (part1.equals(SequentPart.ANTECEDENT)){
											// formulas_ant.add((OWLFormula) inf_sequent.getAntecedent().get(pos_ints[i][0]));
											formulas_ant.add(inf_sequent.antecedentGetFormula(pos_ints[i][0]));
										}
										if (part1.equals(SequentPart.SUCCEDENT)){
											formulas_succ.add(inf_sequent.succedentGetFormula(pos_ints[i][0]));
										}
								}
							}
						}	
					} // for positions	
					antecedent_relevant.addAll(formulas_ant);
					succedent_relevant.addAll(formulas_succ);
				} // inf appl is relevant
			}
			relevantFormulas.t.addAll(antecedent_relevant);
			relevantFormulas.u.addAll(succedent_relevant);
		}
		
		return relevantFormulas;
	}
	
	
	
	
	public void removeFromSequentsRecursive (ProofTree tree,int nodeid, List<OWLFormula> remove_from_ants, List<OWLFormula> remove_from_succs){
		ProofNode node = tree.getProofNode(nodeid);
		Sequent sequent = (Sequent) node.getContent();
		for(OWLFormula ob1: remove_from_ants){
			// System.out.print("trying to remove ");
			// System.out.print(ob1);
			// System.out.print(" from ");
			// System.out.print(sequent.getAntecedent());
			// TODO: use this for removing: removeOWLFormulaFromAntecedent
			// int index = sequent.getAntecedent().indexOf(ob1);
			// sequent.getAntecedent().remove(ob1); // should remove one object
			sequent.removeOWLFormulaFromAntecedent(ob1);
			// now mend any positions stored in justnodes
			/* I suspect that after using term trees, all the following is not necessary any more.
			List<HierarchNode> hnodes = node.getJustifications();
			for (HierarchNode hnode:hnodes){
				List<JustificationNode> justnodes = hnode.getJustifications();
				for (JustificationNode just: justnodes){
					if (just.getPositions()!=null){	
					RuleBindingForNode binding = (RuleBindingForNode) just.getPositions();
						RuleBinding bnd = binding.getRuleBinding();
							Collection values = bnd.getBindings().values();
							for (Object pos_obj:values){
									SequentPosition pos = (SequentPosition) pos_obj;
									if (pos instanceof SequentSinglePosition){
										SequentSinglePosition pos_sin = (SequentSinglePosition) pos;
										if (pos_sin.getSequentPart().equals(SequentPart.ANTECEDENT)){
											int prev = pos_sin.getPosition().get(0); 
											if (index<prev){
												pos_sin.getPosition().set(0,prev-1);
											}
										}
									}
									if (pos instanceof SequentMultiPosition){
										SequentMultiPosition pos_sin = (SequentMultiPosition) pos;
										if (pos_sin.getSequentPart().equals(SequentPart.ANTECEDENT)){
											Integer[][] positions = pos_sin.getPositions();
											for (int ip=0; ip < positions.length; ip++){
												if (index<ip){
													positions[ip][0] = positions[ip][0] -1;
												}
											}
											
										}
									}
							}
							}
					}
			} */ 
		} // end the outer loop for the antecedents to be removed
		for(OWLFormula ob2: remove_from_succs){
			sequent.removeOWLFormulaFromAntecedent(ob2);
			// int index = sequent.getAntecedent().indexOf(ob2);
		    // sequent.getSuccedent().remove(ob2);
		// now mend any positions stored in justnodes
		 /* As above!
					List<HierarchNode> hnodes = node.getJustifications();
					for (HierarchNode hnode:hnodes){
						List<JustificationNode> justnodes = hnode.getJustifications();
						for (JustificationNode just: justnodes){
							if (just.getPositions()!=null){	
							RuleBindingForNode binding = (RuleBindingForNode) just.getPositions();
								RuleBinding bnd = binding.getRuleBinding();
									Collection values = bnd.getBindings().values();
									for (Object pos_obj:values){
											SequentPosition pos = (SequentPosition) pos_obj;
											if (pos instanceof SequentSinglePosition){
												SequentSinglePosition pos_sin = (SequentSinglePosition) pos;
												if (pos_sin.getSequentPart().equals(SequentPart.SUCCEDENT)){
													int prev = pos_sin.getPosition().get(0); 
													if (index<prev){
														pos_sin.getPosition().set(0,prev-1);
													}
												}
											}
											if (pos instanceof SequentMultiPosition){
												SequentMultiPosition pos_sin = (SequentMultiPosition) pos;
												if (pos_sin.getSequentPart().equals(SequentPart.SUCCEDENT)){
													Integer[][] positions = pos_sin.getPositions();
													for (int ip=0; ip < positions.length; ip++){
														if (index<ip){
															positions[ip][0] = positions[ip][0] -1;
														}
													}
													
												}
											}
									}
									}
							}
					}
		}	 */
		} // end the outer loop for succedents to be removed.
		// now do this recursively
		// System.out.println("remove -- before recursion");
		List<HierarchNode> hnodes = node.getJustifications();
		for (HierarchNode hnode:hnodes){
			List<JustificationNode> justnodes = hnode.getJustifications();
			for (JustificationNode just: justnodes){
				List<Integer> prems = just.getPremises();
				for(Integer i:prems){
					removeFromSequentsRecursive(tree,i,remove_from_ants,remove_from_succs);
				}
			}
		}
		return;
	}
	
	/*
	 public ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String, SequentPosition>> initialiseOWLProoftree(){
		 ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition> node = 
		   		 new ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>(new Sequent<OWLObject>(),null,0);
			 ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String, SequentPosition>> prooftree = 
					 new ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>>(node);
		 return prooftree;
	 }
	 */
	 
	 public static int computeRuleBindingMaxDepth(Sequent s, RuleBinding rb){
		 HashMap bindings = rb.getBindings();
		 int maxdepth = 0;
		 // System.out.println("BINDINGS SIZE " + bindings.values().size());
		 for (Object pos : bindings.values()){
			 SequentSinglePosition position = (SequentSinglePosition) pos;
			 OWLFormula formula = s.antecedentGetFormula(position.getToplevelPosition());
			// Bookkeeping of formula depths
			int depth1 = s.getFormulaDepth(s.antecedentFormulaGetID(formula));
			// System.out.println("DEBUG found depth for  " + formula + " : " + depth1);
			if (depth1> maxdepth)
					maxdepth = depth1;
		 }
		 return maxdepth;
	 }
	 
	 /** Takes a conclusion, the required axioms, and returns a proof for the conclusion (as a Gentzen style tree)
	  * 
	  * @param conclusion 				-- the OWLFormula to be shown
	  * @param axioms 					-- the set of axioms from which the conclusion is to be derived
	  * @param loopLimit 				-- cutoff maximum number of inference applications during proof search
	  * @param timeLimit				TODO add description
	  * @param ruleset 					-- the set of inference rules to be used
	  * 
	  * @return 						a Gentzen style proof tree showing the conclusion
	  * @throws ProofNotFoundException 	TODO add descirpition
	  * @throws Exception 				TODO add description
	  */
	 
	 /*
	  * TODO add description
	  */
	 public static GentzenTree computeProofTree(OWLFormula conclusion, List<OWLFormula> axioms, int loopLimit, long timeLimit, String ruleset) throws ProofNotFoundException,Exception{
		 	
		 ProofTree<ProofNode<Sequent<OWLFormula>,java.lang.String, SequentPosition>> prooftree = 
				 computeSequentTree(conclusion, axioms, loopLimit, timeLimit, ruleset);
		 	
		    GentzenTree gentzenTree = prooftree.toGentzen();
			return gentzenTree;
	 }
	 
	 public static ProofTree computeSequentTree(OWLFormula conclusion, List<OWLFormula> axioms, int loopLimit, long timeLimit, String ruleset) throws ProofNotFoundException,Exception{
		 	
		  	// Sequent<OWLFormula> sequent = new Sequent<OWLFormula>();
		    IncrementalSequent sequent = new IncrementalSequent();
	 	  	sequent.addSuccedent(conclusion);
	 	  	for (OWLFormula axiom: axioms){
	 	  		// System.out.println("DBG INFSERV COMPUTE PT ADDING " + axiom);
	 	  		sequent.addAntecedent(axiom,0);
	 	  	}
		    ProofNode<Sequent<OWLFormula>,java.lang.String,SequentPosition> node = new ProofNode<Sequent<OWLFormula>,java.lang.String,SequentPosition>(sequent,null,0);
	        ProofTree<ProofNode<Sequent<OWLFormula>,java.lang.String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLFormula>,java.lang.String,SequentPosition>>(node);
		    List<SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet(ruleset);
	    		 
	    	sequent.setHighestInitAxiomid(1000);
		    InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, allInferenceRules, loopLimit, timeLimit);
		    	 
		    if(prooftree.getOpenNodes().size()>0){
		    		 throw new ProofNotFoundException("proof not found within step limit " + loopLimit);
		    };
		    	 
		    try {
				 InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, node.getId());
			 	 } catch (Exception e) {
			 e.printStackTrace();
				 		}
		    	
		    
			return prooftree;
	 }
	 
	 public List<JustificationNode> getExpandableJustifications(ProofTree prooftree, SequentInferenceRule seqrule){
		 Collection<ProofNode> proofnodes = prooftree.getAllProofNodes();
		 List<JustificationNode> toBeExpanded = new ArrayList<JustificationNode>();
		 for (ProofNode  node : proofnodes){
			 if (node.getBottomJustification()==null)
				 continue;
			 List<HierarchNode> hnodes = node.getJustifications();
			 for (HierarchNode hnode : hnodes){
				 	List<JustificationNode> justnodes = hnode.getJustifications();
				 	for (JustificationNode just : justnodes){
					just = node.getBottomJustification();
					// System.out.println(node);
					// System.out.println(just.getInferenceRule());
				 		if (just.getInferenceRule()==null)
				 			continue;
				 		if (just.getInferenceRule().equals(seqrule)){
				 			// System.out.println("action!");
				 			toBeExpanded.add(just);
				 			// AdditionalDLRules.SUBCLCHAIN.expandTactic(prooftree,node,just);
				 		}
				 	}
			 }	
		 }	
			 return toBeExpanded;
	 }

	
	 
} // end class defn
