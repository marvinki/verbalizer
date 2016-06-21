package org.semanticweb.cogExp.examples;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;


public class UsageExample {
	
	public static void main(String[] args) throws OWLOntologyCreationException{
	
	// load an ontology through the OWL API
	File file = new File(".");
	File ontologyfile = new java.io.File(Paths.get(file.getAbsoluteFile().getParentFile().getAbsolutePath(),
									  "resource", 
									  "TinyExampleOntology.owl").toString());
	OWLOntology tinyExampleOntology = 
			OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(ontologyfile);	
	
	
	// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
	OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
	Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
	OWLReasoner reasoner = reasonerFactory.createReasoner(tinyExampleOntology);
    
    // indicate the IRIs of some relevant classes/roles in the ontology
  	OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
  	String ontologyuri = "http://www.semanticweb.org/marvin/ontologies/2016/0/untitled-ontology-490#";
  	OWLClass animalLover = dataFactory.getOWLClass(IRI.create(ontologyuri + "AnimalLover"));
 	OWLClass person = dataFactory.getOWLClass(IRI.create(ontologyuri + "Person"));
  	OWLClass organ = dataFactory.getOWLClass(IRI.create(ontologyuri + "Organ"));
  	OWLObjectProperty hasBodyPart = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "hasBodyPart"));
  	OWLObjectProperty has = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "has"));
  	OWLObjectProperty likes = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "likes"));
	
  	// Now specify which axiom is to be explained;
  	// For example: 
  	// "AnimalLover subClassOf likes some (has some Organ)"
	OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(animalLover, 
			dataFactory.getOWLObjectSomeValuesFrom(likes,
					dataFactory.getOWLObjectSomeValuesFrom(hasBodyPart,organ)
					)
			);
	
	WordNetQuery.INSTANCE.disableDict();
	String explanation = VerbalisationManager.verbalizeAxiom(axiom, reasoner, reasonerFactory, tinyExampleOntology, false,false);
	
	
	System.out.println("Explanation for \"" + VerbalisationManager.verbalise(axiom) + "\":\n");
	System.out.println(explanation);
	
	
	
	}
}
