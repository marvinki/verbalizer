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
		result += head.toString();
		}
		result += "\n";
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
