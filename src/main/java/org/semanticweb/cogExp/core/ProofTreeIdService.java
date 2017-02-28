package org.semanticweb.cogExp.core;

public enum ProofTreeIdService {
	INSTANCE;
	
	int number = 0;
	
	public int getNewId(){
		number++;
		return number;
	}
	
	
}
