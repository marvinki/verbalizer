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
