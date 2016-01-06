package org.semanticweb.cogExp.core;

import java.util.ArrayList;
import java.util.List;

public class JustificationNode<JustificationType,PositionType> {
private List<Integer> premises = new ArrayList<Integer>();// we use integers in the role of pointers, managed by the proof tree class
private JustificationType justification;
private PositionType positions;// invariant: positions are given in the same order as the premises
private int justifiesNode; // this is a backlink to the node that is being justified by this justification
private SequentInferenceRule infrule; // stores the inference rule instance that was used to do the justification

	
public JustificationType getJustification(){
	return justification;
}

public void setJustification(JustificationType just){
	justification = just;
	return;
}

public void setPositions(PositionType pos){
	positions = pos;
	return;
}

public void setPremises(List<Integer> prems){
	premises = prems;
}

public void setJustifiedNode(int i){
	justifiesNode = i;
}

public List<Integer> getPremises(){
	return premises;
}

public PositionType getPositions(){
	return positions;
}

public int getJustifiedNode(){
	return justifiesNode;
}

public SequentInferenceRule getInferenceRule(){
	return infrule;
}

public void setInferenceRule(SequentInferenceRule ir){
	infrule = ir;
}

public JustificationNode<JustificationType,PositionType> copy(){
	JustificationNode<JustificationType,PositionType> newjustificationnode = new JustificationNode<JustificationType,PositionType>();
	newjustificationnode.setPremises(premises);
	newjustificationnode.setJustification(justification);
	newjustificationnode.setPositions(positions);
	return newjustificationnode;
}

}
