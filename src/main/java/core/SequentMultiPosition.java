package org.semanticweb.cogExp.core;

public class SequentMultiPosition extends SequentPosition {
	
	
	private SequentPart sequentpart;
	private Integer[][] positions;
	
	public SequentMultiPosition(SequentPart part, int pos){
		sequentpart = part;
		Integer[] positions1 = new Integer[]{};
		positions1[0] = pos;
		Integer[][] positions2 = new Integer[][]{};
		positions2[0] = positions1;
		positions = positions2;
	}
	
	public SequentMultiPosition(SequentPart part, Integer[][] pos){
		sequentpart = part;
		positions = pos;
	}
	
	
	@Override
	public SequentPart getSequentPart(){
		return sequentpart;
	}
	
	public Integer[][] getPositions(){
		return positions;
	}
	
	@Override
	public boolean isNotNull(){
		if (positions==null){return false;} else {return true;}
	}
	
}
