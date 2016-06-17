package org.semanticweb.cogExp.OWLAPIVerbaliser;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public enum OWLAPIManagerManager {

	INSTANCE;
	
	private final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
	private final OWLDataFactory dataFactory=ontologyManager.getOWLDataFactory();
	
	public OWLOntologyManager getOntologyManager(){
		return ontologyManager;
	} 
	
	public OWLDataFactory getDataFactory(){
		return dataFactory;
	}
	
}
