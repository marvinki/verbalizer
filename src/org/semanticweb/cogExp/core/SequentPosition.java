package org.semanticweb.cogExp.core;

import java.util.List;

public abstract class SequentPosition extends AbstractSequentPositions{
	
	SequentPart sequentpart;

	public abstract SequentPart getSequentPart();
	
	public SequentPosition(SequentPart part, List<Integer> positions){};
	
	public SequentPosition(SequentPart part, int position){};
	
	public SequentPosition(){};
	
	public boolean isNotNull(){return true;};
	

}