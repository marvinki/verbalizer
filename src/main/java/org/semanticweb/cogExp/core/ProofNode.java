package org.semanticweb.cogExp.core;

import java.util.List;
import java.util.ArrayList;

public class ProofNode<ContentType,JustificationType,PositionType> implements IDentifiable{
	private int id;
	private ContentType content;
	private List<HierarchNode<JustificationType,PositionType>> justifications; // this list represents OR alternatives
	// private List<List<JustificationNode<JustificationType,PositionType>>> justifications; // we use integers in the role of pointers, managed by the proof tree class
																						  // the top level List represents OR alternatives, the inner list hierarchical alternatives
	private int treeid;
	
	
	public ProofNode(ContentType cont, List<HierarchNode<JustificationType,PositionType>> justs, int tid){
		content = cont;
		treeid = tid;
		if (justs == null)
		{justifications = new ArrayList<HierarchNode<JustificationType,PositionType>>();} else{
			justifications = justs;}
	}
	
	
	public List<Integer> getTopPremiseIds(){ // this always automatically only considers the first OR alternative
		return justifications.get(0).getJustifications().get(0).getPremises();
	}
	
	public List<Integer> getBottomPremiseIds(){ // this always automatically only considers the first OR alternative
		return justifications.get(0).getJustifications().get(justifications.size()-1).getPremises();
	}
	
	public JustificationNode<JustificationType,PositionType> getBottomJustification(){ // this always automatically only considers the first OR alternative
		return justifications.get(0).getJustifications().get(justifications.size()-1);
	}
	
	public JustificationNode<JustificationType,PositionType> getTopJustification(){ // this always automatically only considers the first OR alternative
		return justifications.get(0).getJustifications().get(0);
	}
	
	
	public List<HierarchNode<JustificationType,PositionType>> getJustifications(){
		return justifications;
	}
	
	public void setJustifications (List<HierarchNode<JustificationType,PositionType>> justs){
		justifications = justs;
	}
	
	@Override
	public void setId(int ident){
		id = ident;
	}
	
	
	
	public ContentType getContent(){
		return content;
	}
	
	public void setContent(ContentType c){
		content = c;
	}
	
	@Override
	public int getId(){
		return id;
	}
	
	public int getTreeId(){
		return treeid;
	}
	
	public void setTreeId(int tid){
		treeid = tid;
		return;	
	}
	
	public void addHierarchNode(HierarchNode<JustificationType,PositionType> just){
		justifications.add(just);
		return;
	}
	
	/*
	public void addJustification(JustificationNode<JustificationType,PositionType> just){
		justifications.add(just);
		return;
	}
	*/
	
	public ProofNode<ContentType,JustificationType,PositionType> copy(){
		// copy justifications
		ArrayList<HierarchNode<JustificationType,PositionType>> newjustifications = new ArrayList<HierarchNode<JustificationType,PositionType>>();
		for (HierarchNode<JustificationType,PositionType> justnode : justifications){
			HierarchNode<JustificationType,PositionType> newjustnode = justnode.copy();
			newjustifications.add(newjustnode);
		}
		
		ProofNode<ContentType,JustificationType,PositionType> newnode = new ProofNode<ContentType,JustificationType,PositionType>(content,newjustifications,treeid);
		newnode.setId(id);
		return newnode;
	}
	
	@Override
	public java.lang.String toString(){
		java.lang.String result = this.getClass().getName() + '@' + Integer.toHexString(hashCode()) + " id: " + id + " justifications: " + justifications + " content: " + content;
	return result;
	}
	
	
	
	
}
