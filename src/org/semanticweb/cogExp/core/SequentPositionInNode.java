package org.semanticweb.cogExp.core;

import java.util.List;

public class SequentPositionInNode {

	private int nodeid;
	SequentPosition position;
	
	
	public int getNodeId(){
		return nodeid;
	}
	
	public SequentPositionInNode(int nid, SequentPart partpos, List<Integer> listpos){ 
		// super(partpos,listpos);
		position = new SequentSinglePosition(partpos,listpos);
		nodeid = nid;
	}
	
	public SequentPositionInNode(int nid, SequentPart partpos, int pos){ 
		position = new SequentSinglePosition(partpos,pos);
		// super(partpos, pos);
		nodeid = nid;
	}
	
	public SequentPositionInNode(int nid, SequentPart partpos, Integer[][] pos){ 
		position = new SequentMultiPosition(partpos,pos);
		// super(partpos, pos);
		nodeid = nid;
	}
	
	public SequentPositionInNode(int nid, SequentPosition seqpos){ 
		position = seqpos;
		// super(partpos, pos);
		nodeid = nid;
	}
	public SequentPart getSequentPart(){
		return getSequentPart();
	}
	
	public SequentPosition getSequentPosition(){
		return position;
	}
	
	@Override
	public java.lang.String toString(){
		return "node " + nodeid + "-" + position.toString();
	}
}
