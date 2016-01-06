package org.semanticweb.cogExp.core;

import java.util.List;

public class SequentPositionInTree extends SequentPositionInNode{

	private int tree;
	
	public int getTreeId(){
		return tree;
	}
	
	public SequentPositionInTree(int treepos, int nid, SequentPart partpos, List<Integer> listpos){ 
		super(nid,partpos,listpos);
		tree = treepos;
	}
	
	public SequentPositionInTree(int treepos, int nid, SequentPart partpos, int pos){ 
		super(nid,partpos, pos);
		tree = treepos;
	}
	
	
}
