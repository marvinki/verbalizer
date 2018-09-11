package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.functional.parser.OWLFunctionalSyntaxOWLParser;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public enum OWLAPIManagerManager {

	INSTANCE;
	
	private final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
	private final OWLDataFactory dataFactory=ontologyManager.getOWLDataFactory();
	private final OWLFunctionalSyntaxOWLParser functionalSyntaxParser = new OWLFunctionalSyntaxOWLParser();

	
	public OWLOntologyManager getOntologyManager(){
		return ontologyManager;
	} 
	
	public OWLDataFactory getDataFactory(){
		return dataFactory;
	}
	
	public OWLFunctionalSyntaxOWLParser getFunctionalSyntaxParser(){
		return functionalSyntaxParser;
	}
	

}
