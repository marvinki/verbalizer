package org.semanticweb.cogExp.core;

import java.util.List;
import java.util.ArrayList;

public class SequentSinglePosition extends SequentPosition {
	
	
	private SequentPart sequentpart;
	private List<Integer> position;
	
	public SequentSinglePosition(SequentPart part, int pos){
		sequentpart = part;
		position = new ArrayList<Integer>();
		position.add(pos);
	}
	
	public SequentSinglePosition(SequentPart part, List<Integer> pos){
		sequentpart = part;
		position = new ArrayList<Integer>(pos);
	}
	
	
	/* (non-Javadoc)
	 * @see org.semanticweb.tOWL.SequentPosition#getSequentPart()
	 */
	@Override
	public SequentPart getSequentPart(){
		return sequentpart;
	}
	
	public List<Integer> getPosition(){
		return position;
	}
	
	public boolean isToplevelPosition(){
		return position.size() == 1;
	}
	
	public int getToplevelPosition(){
		return position.get(0);
	}
	
	@Override
	public boolean isNotNull(){
		if (position==null){return false;} else {return true;}
	}
	
	@Override
	public java.lang.String toString(){
	return position.toString();
	}
}
