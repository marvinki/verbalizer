package org.semanticweb.cogExp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.inferencerules.SequentTerminationAxiom;


// import org.apache.commons.lang.StringUtils;
// http://commons.apache.org/proper/commons-lang/


/**
 * 
 * @author marvin
 *
 * @param <Type> type parameter for the nodes (e.g. Sequent)
 */

public class ProofTree<Type extends ProofNode> implements IDentifiable{

	private Type root;
	private HashMap<Integer,Type> nodesHashMap = new HashMap<Integer,Type>();
	private List<Type> open_nodes = new ArrayList<Type>();
	private List<Type> pending_nodes = new ArrayList<Type>();
	/**
	 * this is an ugly construct to make getAntecedents work
	 */
	private List<Type> somelist; // this is an ugly construct to make getAntecedents work
	int last_key = 0;
	int id;
	private JustificationNode newest_justification = null;
	private List tbox = null;
	private HashMap<Integer,List<Integer>> consequenceOrdering = new HashMap<Integer,List<Integer>>();
	private HashMap<Integer,Integer> nodeDepth = new HashMap<Integer,Integer>();

	
	// this is evil and should be made obsolete
	public ProofTree(){
	}
	
	/** produce a proof tree with one root note
	 * 
	 * @param node 		This node will become the node of the tree
	 */
	public ProofTree(Type node){
		this.addNode(node);
		this.pending_nodes = open_nodes;
		this.root = node;
	}
	
	/** this constructor produces a copy of a proof tree based on all attributes
	 * 
	 * @param hm		hashmap will be copied
	 * @param r			the node type (needed for technical reason)
	 * @param open		list of open nodes to be copied
	 * @param pending  	list of pending nodes to be copied
	 * @param some		the some element to be copied (needed for a technical reason)
	 * @param lastk		the last key that was assigned (to be copied)
	 */
	public ProofTree(HashMap<Integer,Type> hm, Type r, List<Type> open, List<Type> pending, List<Type> some, int lastk){
		HashMap<Integer,Type> newNodesHashMap = new HashMap<Integer,Type>();
		ArrayList<Type> new_open_nodes = new ArrayList<Type>();
		ArrayList<Type> new_pending_nodes = new ArrayList<Type>();
		for (Map.Entry<Integer, Type> entry : hm.entrySet()) {
			int key = entry.getKey();
			Type value = entry.getValue();
			int id = value.getId();
			// System.out.println(id);
			ProofNode newvalue = value.copy();
			// System.out.println(newvalue.getId());
			newNodesHashMap.put(key, (Type) newvalue);
			for (Type o : open){
			// if (open_nodes.contains(value)){
				if (o.getId()==id){
				new_open_nodes.add((Type) newvalue);
				}
			}
			for (Type p : pending){
				// if (open_nodes.contains(value)){
					if (p.getId()==id){
					new_pending_nodes.add((Type) newvalue);
					}
				}
		}
		nodesHashMap = newNodesHashMap;
		ProofNode newroot = r.copy();
		root = (Type) newroot;
		open_nodes = new_open_nodes;
		pending_nodes = new_pending_nodes;
		somelist = some;
		last_key = lastk;
	}
	
	/**
	 * returns id of proof tree (int)
	 */
	@Override
	public int getId(){
		return id;
	}
	
	/**
	 * sets id of proof tree
	 * @param ident  	identifier as int value
	 */
	@Override
	public void setId(int ident){
		id = ident;
		return;
	}
	
	/**
	 * 
	 * @param proofnode		proofnode to be added as an open node
	 * @return int id of proofnode that identifies it within the tree
	 */
	public int addNode(Type proofnode){
		proofnode.setId(last_key);
		proofnode.setTreeId(id);
		nodesHashMap.put(last_key,proofnode);
		last_key++;
		open_nodes.add(proofnode); // new proofnodes are considered unjustified by default
		return proofnode.getId();
	}
	
	/**
	 * 
	 * @param proofnode		proofnode to be removed
	 */
	public void removeNode(Type proofnode){
		nodesHashMap.remove(proofnode.getId());
		if (pending_nodes.contains(proofnode)){
			pending_nodes.remove(proofnode);
		}
		if (open_nodes.contains(proofnode)){
			open_nodes.remove(proofnode);
		}
		return;
	}
	
	/**
	 * 
	 * @param conclusion			proofnode is awarded status of a conclusion (not open any more)
	 * @param justificationnode		hierarchnode that links the conclusion to its premises
	 */
	public void linkPremises(ProofNode conclusion, HierarchNode justificationnode){
		open_nodes.remove(conclusion);
		conclusion.addHierarchNode(justificationnode);
	}
	
	/*
	public void linkPremises(ProofNode conclusion, JustificationNode justificationnode){
		open_nodes.remove(conclusion);
		conclusion.addJustification(justificationnode);
	}
	*/
	
	/**
	 * 
	 * @param id		id (int) of node to be retrieved
	 * @return			retrieved proof node
	 */
	public Type getProofNode(int id){
		return nodesHashMap.get(id);
	}
	
	/**
	 * 
	 * @return			all proof nodes
	 */
	public Collection<Type> getAllProofNodes(){
		Collection<Type> values = nodesHashMap.values();
		return values;
	}
	
	public void setNewestJustification(JustificationNode justificationnode){
		newest_justification = justificationnode;
		return;
	}
	
	public JustificationNode getNewestJustification(){
		return newest_justification;
	}
	
	/*
	public List<Type> getAntecedents(Type node){
		List<Integer> premiseIds =  node.getPremiseIds();
		somelist.clear();
		List<Type> newlist = somelist.subList(0,0);
		Iterator<Integer> it = premiseIds.iterator();
		while (it.hasNext())
		{
			newlist.add(getProofNode(it.next()));
		}
		return newlist;
	}
	*/
	
	public List<Type> getOpenNodes(){
		return open_nodes;
	}
	
	public List<Type> getPendingNodes(){
		return pending_nodes;
	}
	
	public ProofTree deepCopy(){
		ProofTree newtree = new ProofTree(nodesHashMap,root,open_nodes,pending_nodes,somelist,last_key);
		return newtree;
	}
	
	
	
	public void  print(){
		System.out.println("PROOFTREE No. " + id);
		System.out.println("Opennodes " + open_nodes);
		Set<Integer> keyset = nodesHashMap.keySet();
		List<Integer> keylist = new ArrayList<Integer>(keyset);
		Collections.sort(keylist);
		Iterator<Integer> it = keylist.iterator();
		ProofNode node;
		while(it.hasNext()){
			int i = it.next();
			System.out.print(i + ": ");
			node  = nodesHashMap.get(i);
			System.out.print(node.getContent());
			// if (node.getJustifications().size() !=0){
			//	System.out.print(" by " + node.getTopJustification().getJustification());
			// 	System.out.print(" from " + node.getTopJustification().getPremises());
			// }
			boolean first_flag = true;
			List<HierarchNode> hnodes = node.getJustifications(); // by default only look at first option
			if (hnodes==null||hnodes.size()==0){}
			else{
			List<JustificationNode> jnodes = hnodes.get(0).getJustifications();
			for (JustificationNode jnode : jnodes){
				if (!first_flag){System.out.print(", or");}
				first_flag = false;
				System.out.print(" by " + jnode.getJustification());
				System.out.print(" from " + jnode.getPremises());	
			}
			}
			System.out.println("");
		}
		return;
	}
	
	
	
	public GentzenTree toGentzen() throws Exception{
		GentzenTree tree = new GentzenTree();
		Set<Integer> keyset = nodesHashMap.keySet();
		try {
			this.computeConsequenceOrdering();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	this.computeNodeDepth();
    	ProofNode node;
    	List<Integer> keylist = computePresentationOrder();
    	// Collections.reverse(keylist);
		Iterator<Integer> it = keylist.iterator();
		while(it.hasNext()){
			int i = it.next();
			// System.out.println("DEBUG! toGentzen working on node nr " + i);
			node  = nodesHashMap.get(i);
			Sequent current_sequent = (Sequent) node.getContent();
			if (node.getJustifications().size() !=0){
				List<Integer> topPremises = node.getTopJustification().getPremises();
				// System.out.println("DEBUG top premises " + topPremises);
				Object positions = node.getTopJustification().getPositions();		
				SequentInferenceRule rule = node.getTopJustification().getInferenceRule();	
				// Determining premise formulas
				List premiseformulas = null;
				if (positions instanceof SequentSinglePosition){
					premiseformulas = current_sequent.retrieveOWLFormulas((SequentSinglePosition) positions);
				}
				else if (positions instanceof SequentMultiPosition){
						premiseformulas = current_sequent.retrieveOWLFormulas((SequentMultiPosition) positions);
					}
				else if (positions instanceof SequentPositionInNode){
						premiseformulas = current_sequent.retrieveOWLFormulas((SequentPositionInNode) positions);
						}
				else if (positions instanceof RuleBinding){
						premiseformulas = current_sequent.retrieveOWLFormulas((RuleBinding) positions);
						}		
				if (!(premiseformulas==null)){
					int formulas_size = premiseformulas.size();
				     // System.out.println("toGentzen DEBUG : " + premiseformulas);
				// Removing duplicates
					if (formulas_size>1){
						for(int ii =0; ii<formulas_size;ii++){
							for (int iii=0;iii<formulas_size;iii++){
								if (ii!=iii && premiseformulas.get(ii).equals(premiseformulas.get(iii))){
									premiseformulas.remove(iii);
									formulas_size--;
								}
							}
						}
					}
				}
				List<Object> additions_to_antecedent = new ArrayList<Object>();
				List<Object> additions_to_succedent = new ArrayList<Object>();
				// Determining conclusions (the hard way)
				if (topPremises.size()>0){
					for(int p : topPremises){		
						ProofNode premNode = getProofNode(p);
						// System.out.println("DEBUG! premNode:  " + premNode + " rule name " + rule.getName());
						if (premNode==null){ // this can be the case if we are at a 'leaf'
							break;
						}
						Sequent prem_sequent = (Sequent) premNode.getContent();
						Set current_ant = current_sequent.getAllAntecedentOWLFormulas();
						Set current_succ = current_sequent.getAllSuccedentOWLFormulas();
						Set prem_ant = prem_sequent.getAllAntecedentOWLFormulas();
						Set prem_succ = prem_sequent.getAllSuccedentOWLFormulas();
						if (!current_ant.containsAll(prem_ant)){ // something has been added to the antecedent
							Set<Sequent> difference = new HashSet<Sequent>(prem_ant);
							difference.removeAll(current_ant);
							for (Object f: difference){
								additions_to_antecedent.add(f);
								}
							}
						if (!current_succ.containsAll(prem_succ)){ // something has been added to the antecedent
						Set<Sequent> difference = new HashSet<Sequent>(prem_succ);
						difference.removeAll(current_succ);
						for (Object f: difference){
							additions_to_succedent.add(f);
							}
						}
					}
				}
				if (rule==null){
					continue;
				}
			
				// Actual conversion
				// we can only treat the case of single, forward conclusion formulas
				if (additions_to_antecedent.size()==1){
					/* OWLObject owlObject = (OWLObject)  additions_to_antecedent.get(0);
					OWLFormula conclusion = OWLFormula.createFormulaBot();
					if (owlObject instanceof OWLSubClassOfAxiom){
						conclusion = OWLFormula.fromOWLAPI((OWLSubClassOfAxiom) owlObject);
					}
					if (owlObject instanceof OWLEquivalentClassesAxiom){
						conclusion = OWLFormula.fromOWLAPI((OWLEquivalentClassesAxiom) owlObject);
					}
					*/
					OWLFormula conclusion = (OWLFormula) additions_to_antecedent.iterator().next();
					List<OWLFormula> premises = new ArrayList<OWLFormula>();
					for (Object o: premiseformulas){
						/*
						OWLObject owlO = (OWLObject) o;
						OWLFormula oformula = OWLFormula.createFormulaBot();
						if (owlO instanceof OWLSubClassOfAxiom){
							oformula = OWLFormula.fromOWLAPI((OWLSubClassOfAxiom) owlO);
						}
						if (owlO instanceof OWLEquivalentClassesAxiom){
							oformula = OWLFormula.fromOWLAPI((OWLEquivalentClassesAxiom) owlO);
						}
						*/
						OWLFormula oformula = (OWLFormula) o;
						premises.add(oformula);
					}
					// System.out.println("INSERT STEP");
					// System.out.println("PREMISES " + premises);
					// System.out.println("CONCLUSION " + conclusion);
					// System.out.println("RULENAME " + rule.getName());
					tree.insertStep(premises, conclusion, rule);
				}		
			}
		}
		return tree;
	}
	
	
	public static String makeUppercaseStart(String str){
		String firstletter = str.substring(0, 1);
		String rest = str.substring(1);
		return firstletter.toUpperCase() + rest;
	}
	
	
		
		
		
	
	public boolean problemEquivalent(ProofTree<ProofNode<Sequent,java.lang.String,AbstractSequentPositions>> tree2){
		// System.out.println("problemEquivalent called");
		List<ProofNode<Sequent,java.lang.String,AbstractSequentPositions>> open_nodes2 = tree2.getOpenNodes();
		if (!(open_nodes.size()==open_nodes2.size())){return false;}
		// System.out.println("Same number of open nodes");
		boolean equivP = true;
		for (ProofNode<Sequent,java.lang.String,AbstractSequentPositions> node2 : open_nodes2){
			boolean foundP = false;
			for (Type node1 : open_nodes){
				Sequent seq2 = (node2.getContent());
				Sequent seq1 = ((Sequent) node1.getContent());
					if (seq1.equal(seq2)){
						foundP = true;
						// System.out.println("found match!");
					}
			}
			if (!foundP){equivP=false;
			return false;
			}
			// if (!foundP){System.out.println("argh!");}
		}
		for (Type node1 : open_nodes){
			boolean foundP = false;
			// for every node1 there needs to be some matching node2
			for (ProofNode<Sequent,java.lang.String,AbstractSequentPositions> node2 : open_nodes2){
				// System.out.println(node1.getContent());
				// System.out.println(node2.getContent());
				Sequent seq1 = (node2.getContent());
				Sequent seq2 = ((Sequent) node1.getContent());
					if (seq1.equal(seq2)){
						// System.out.println("found match!");
						// System.out.println(node1.getContent());
						// System.out.println(node2.getContent());
						foundP = true;
					}
			}
			if (!foundP){equivP=false;
						 return false;
			}
		}
		if (equivP){
			// System.out.println("Trees equivalent!");
			}
		return equivP;
	}

	
	public void setTBox(List t){
		tbox = t;
	}
	
	public List getTBox(){
		return tbox;
	}
	
	public Type getRoot(){
		return root;
	}
	
	public void setRoot(Type r){
		root = r;
	}
	
	
	public List<Pair<Integer,List<OWLFormula>>> computeConsequenceOrdering() throws Exception{
		consequenceOrdering = new HashMap<Integer,List<Integer>>(); // initialise (remove old stuff that might already be there)
		return computeConsequenceOrdering(root);
	}
	
	// propagates uses of formulas as premises upwards
	
	public List<Pair<Integer,List<OWLFormula>>> computeConsequenceOrdering(ProofNode node) throws Exception{
		 List<Pair<Integer,List<OWLFormula>>> results = new ArrayList<Pair<Integer,List<OWLFormula>>>();
		if (node==null){
			return results;
		} else{ // node is not null
			// System.out.println("working on node with id " + node.getId());
			List<HierarchNode> hnodes = node.getJustifications();
			for (HierarchNode hnode : hnodes){ // each hnode encapsulates an alternative option (with its own hierarchy)
					List<JustificationNode> justs = hnode.getJustifications();
					for(JustificationNode just : justs){
						// re-computing results of inference application
						RuleBindingForNode rb_where_inference_was_applied  = (RuleBindingForNode) just.getPositions();
						SequentInferenceRule infrule = just.getInferenceRule();
						Sequent inf_sequent = (Sequent) node.getContent();
						// System.out.println("insequent: " + inf_sequent);
						// System.out.println("rb_where_inference_was_applied: " + rb_where_inference_was_applied);
						// In case we terminate, we still need to take note of the premises!
						Object positions; 
						List<RuleApplicationResults> infresults = null;
						// THIS PART IS ONLY IN CASE THE TERMINATION AXIOM HAS APPLIED
						if (rb_where_inference_was_applied==null){
							// System.out.println("DEBUG --  THE NULL CASE ");
							// positions = SequentTerminationAxiom.INSTANCE.findPositions(inf_sequent).get(0);
							List<SequentPosition> positionslist = SequentTerminationAxiom.INSTANCE.findPositions(inf_sequent);
							for (SequentPosition pos  : positionslist){
							// SequentPosition pos = positionslist.get(0); 
							// System.out.println("DEBUG : " + inf_sequent.retrieveOWLFormulas(pos));
							List formulas = new ArrayList<OWLFormula>();
							if (pos instanceof SequentSinglePosition){
								SequentSinglePosition ssp = (SequentSinglePosition) pos;
								// System.out.println("DEBUG single position case " + pos + " " + pos.getSequentPart() + " " + ssp.getToplevelPosition());
								formulas.addAll(inf_sequent.retrieveOWLFormulas(pos));
							}
							else 
							if (pos instanceof SequentMultiPosition){
									// System.out.println("multi position case ");
									formulas.addAll(inf_sequent.retrieveOWLFormulas((SequentMultiPosition) pos));}
								else{}
							results.add(new Pair(node.getId(),formulas));
							}
							return results;
						} else{
							// ALL OTHER CASES
						infresults = infrule.computeRuleApplicationResults(inf_sequent, rb_where_inference_was_applied);
						// In case we terminate, we still need to take note of the premises!
						// if (infresults==null){
						// 	return results;
						// 	}
						ProofNode justifiednode = getProofNode(just.getJustifiedNode());
						
						// "Formulas" now holds the premise formulas ("from") of this step.
						positions = just.getPositions();
						}
						// System.out.println("DEBUG: positions " + positions);
						List formulas = null;
						if (positions instanceof SequentSinglePosition){
							// System.out.println("single position case ");
							formulas.addAll(inf_sequent.retrieveOWLFormulas((SequentSinglePosition) positions));
						}
						else 
						if (positions instanceof SequentMultiPosition){
								// System.out.println("multi position case ");
								formulas.addAll(inf_sequent.retrieveOWLFormulas((SequentMultiPosition) positions));}
						else if (positions instanceof SequentPositionInNode){
									// System.out.println("sequent position in node case ");
									formulas.addAll(inf_sequent.retrieveOWLFormulas((SequentPositionInNode) positions));}
						else if (positions instanceof RuleBinding){
									// System.out.println("rule binding case ");
									formulas = inf_sequent.retrieveOWLFormulas((RuleBinding) positions);
									// System.out.println("done rule binding case " + formulas);
						}
							else{}
						// System.out.println("DEBUG Formulas: " + formulas);
						results.add(new Pair(node.getId(),formulas));
						
						// now identify whether what we have added in the current step is being used in a child step	
					   //recursive call
						List<Integer> premises = just.getPremises();
						List<Pair<Integer,List<OWLFormula>>> recursiveResults = new ArrayList<Pair<Integer,List<OWLFormula>>>();
						for(Integer prem : premises){		
								recursiveResults.addAll(computeConsequenceOrdering(getProofNode(prem)));
						}
						results.addAll(recursiveResults);
						// System.out.println("recursive Results : " + recursiveResults);
						if (infresults!=null){
						for(RuleApplicationResults result : infresults){
							Set<java.lang.String> additionkeys = result.getAllAdditionKeys();
							for (java.lang.String str : additionkeys){
								OWLFormula form = (OWLFormula) result.getAddition(str);
								// System.out.println("DEBUG -- ADDITION: " + form);
								for (Pair<Integer,List<OWLFormula>> recursiveResult :recursiveResults){
									// System.out.println("DEBUG --- u : " + recursiveResult.u);
									if (recursiveResult.u.contains(form)){
										// System.out.println("DEBUG! -- GOT A HIT");
										/*
										if (consequenceOrdering.get(node.getId())==null){
											ArrayList<Integer> newlist =  new ArrayList<Integer>();
											newlist.add(recursiveResult.t);
											consequenceOrdering.put(node.getId(), newlist);
										} else
										{
											List<Integer> ids = consequenceOrdering.get(node.getId());
											ids.add(recursiveResult.t);
											consequenceOrdering.put(node.getId(),ids); // this is unnecessary, isn't it?
										}
										*/
										if (consequenceOrdering.get(recursiveResult.t)==null){
											ArrayList<Integer> newlist =  new ArrayList<Integer>();
											newlist.add(node.getId());
											consequenceOrdering.put(recursiveResult.t, newlist);
										} else
										{
											List<Integer> ids = consequenceOrdering.get(recursiveResult.t);
											ids.add(node.getId());
											consequenceOrdering.put(recursiveResult.t,ids); // this is unnecessary, isn't it?
										}
									}
								}
							}
						} // end of the fors of the recursive call
					} // end for justs
					}
			} // end for hnodes
					
		} // end else
		return results;
		
	} 
	
	public java.lang.String reportConsequenceOrdering(){
		System.out.println("report");
		System.out.println(consequenceOrdering.keySet());
		String result = "";
		Set<Integer> keys = consequenceOrdering.keySet();
		for (Integer i : keys){
			result = result + "i: " + i + consequenceOrdering.get(i).toString() + "  ";	
		}
		return result;
	}
	
	public java.lang.String reportNodeDepths(){
		System.out.println("report");
		System.out.println(nodeDepth.keySet());
		String result = "";
		Set<Integer> keys = nodeDepth.keySet();
		for (Integer i : keys){
			result = result + "i: " + i + " - " + nodeDepth.get(i).toString() + "  ";	
		}
		return result;
	}
	
	public int computeNodeDepth(ProofNode node){
		if (node==null ){
			return 0;
		}
		if (consequenceOrdering.get(node.getId())==null){
			// System.out.println("case 1; id: " + node.getId() + " depth: 0");
			nodeDepth.put(node.getId(), 0);
			return 0;
		} else{
			List<Integer> premiseids = consequenceOrdering.get(node.getId());
			int depth = 0;
			for (int i : premiseids){
				int recdepth = computeNodeDepth(getProofNode(i));
				if (recdepth+1>depth){
					depth = recdepth +1;
				}
			}
			// System.out.println("case 2; id: " + node.getId() + " depth: " + depth);
			nodeDepth.put(node.getId(), depth);
			return depth;
		}
		
	}
	
	public void computeNodeDepth(){
		// tabula rasa:
		nodeDepth = new HashMap<Integer,Integer>();
		Collection<ProofNode> nodes = (Collection<ProofNode>) this.getAllProofNodes();
		for (ProofNode node : nodes){
			computeNodeDepth(node);
		}
	}
	
	public List<Integer> computePresentationOrder(){
		// preprocessing -- compute node depth
		computeNodeDepth();
		// find the deepest node (n)
		List<Integer> results = new ArrayList<Integer>();
		int depth = 0;
		ProofNode n = root;
		Collection<ProofNode> nodes = (Collection<ProofNode>) this.getAllProofNodes();
		for (ProofNode node : nodes){
			int founddepth = nodeDepth.get(node.getId());
			// System.out.println("node " + node.getId() + " depth " + nodeDepth.get(node.getId()) + " pred: " + node.getTopPremiseIds());
			if (founddepth>depth){
				depth = founddepth;
				n = node;
			}		
		}	
		// System.out.println("DEBUG COMPUTE PRESENTATION ORDER -- deepest node: " + n.getId());
		List<Integer> nodequeue = new ArrayList<Integer>();
		nodequeue.add(n.getId());
		while (nodequeue.size()>0){
			int currentnodeid = nodequeue.remove(0);
			// if we are required to talk about this earlier in the proof, skip
			if(nodequeue.contains(currentnodeid)){
				continue;
			}
			// System.out.println("DEBUG -- currentnodeid : " + currentnodeid);
			results.add(0,currentnodeid);
			List<Integer> premiseids = new ArrayList<Integer>();
			if (consequenceOrdering.get(currentnodeid)!=null){
				premiseids.addAll(consequenceOrdering.get(currentnodeid));
			}
			if (premiseids==null){
				continue;
			}
			// System.out.println("DEBUG -- premiseids : " + premiseids);
			while(premiseids.size()>0){
					int mindepth = 100000;
					int minnode = 0;
					for (int pi : premiseids){
						if (nodeDepth.get(pi)<mindepth){
							mindepth = nodeDepth.get(pi);
							minnode = pi;
						}
					}
					ArrayList foo = new ArrayList<Integer>();
					foo.add(minnode);
					if (!nodequeue.contains(minnode)){
					nodequeue.add(0,minnode);
					}
					premiseids.removeAll(foo);
			}
		}
		return results;
	}
			

	
	
	
}
