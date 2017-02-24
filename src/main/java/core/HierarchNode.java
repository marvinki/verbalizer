package org.semanticweb.cogExp.core;

import java.util.ArrayList;
import java.util.List;

public class HierarchNode<JustificationType,PositionType> {
	private List<JustificationNode<JustificationType,PositionType>> justifications = new ArrayList<JustificationNode<JustificationType,PositionType>>(); 
	
	public List<JustificationNode<JustificationType,PositionType>> getJustifications(){
		return justifications;
	}
	
	public HierarchNode<JustificationType,PositionType> copy(){
		HierarchNode<JustificationType,PositionType> new_hierarch_node = new HierarchNode<JustificationType,PositionType>();
		List<JustificationNode<JustificationType,PositionType>> new_justifications = new_hierarch_node.getJustifications();
		for (JustificationNode<JustificationType,PositionType> just: justifications){
				new_justifications.add(just.copy());
		}
		return new_hierarch_node;
		
	}
	
	
}
