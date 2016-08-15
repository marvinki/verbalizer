package org.semanticweb.cogExp.examples;
import java.io.File;
import java.io.IOException;
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
import org.semanticweb.cogExp.ProofBasedExplanation.ProofBasedExplanationService;
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


public class Begehung {
	
	public static void main(String[] args) throws OWLOntologyCreationException{
	
	// load an ontology through the OWL API
	/// ---------------> ENTER HERE THE PATH TO THE ONTOLOGY!	
	// File file = new File("/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
	// File file = new File("/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_demo.rdf");
	
	
	// OWLOntology clusterOntology = 
	//		OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(file);	
	
	// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
	OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
	Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
	// OWLReasoner reasoner = reasonerFactory.createReasoner(clusterOntology);
    
    // indicate the IRIs of some relevant classes/roles in the ontology
  	OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
  	String ontologyuri = "http://www.semanticweb.org/dennis/ontologies/2013/9/complete-ontology-20#";
  	OWLClass Upper_Body_Training_Template  = dataFactory.getOWLClass(IRI.create(ontologyuri + "Upper_Body_Training_Template"));
 	OWLClass WT_ChestTIRETTriceps = dataFactory.getOWLClass(IRI.create(ontologyuri + "WT_Chest-Triceps"));
  	
	
  	// Now specify which axiom is to be explained;
	// OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(WT_ChestTIRETTriceps, Upper_Body_Training_Template);
		
	// WordNetQuery.INSTANCE.disableDict();
	String tmpdir = "";
	try {
		tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	WordNetQuery.INSTANCE.setDict(tmpdir);
	
	// GentzenTree tree = ProofBasedExplanationService.computeTree("WT_Chest-Triceps", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
	// String result = VerbaliseTreeManager.verbaliseNL(tree, true, false,null); // <------ 3rd arg is: true - html, false - text
	// String listResult = VerbaliseTreeManager.listOutput(tree);
	// System.out.println(result);
	// System.out.println(" ");
	// System.out.println(listResult);
	
	System.out.println("start searchin'");
	// GentzenTree tree2 = ProofBasedExplanationService.computeTree("WT_Chest-Triceps", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_demo.rdf");
	
	// WT_UpperBody
	// WT_Chest-Triceps
	
	GentzenTree tree2 = ProofBasedExplanationService.computeTree("WT_Chest-Triceps", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_demo.rdf");
	String result2 = VerbaliseTreeManager.verbaliseNL(tree2, false, true,null);
	System.out.println(result2);
	
	// String explanation = VerbalisationManager.verbalizeAxiom(axiom, reasoner, reasonerFactory, clusterOntology, false,false);
	// System.out.println("Explanation for \"" + VerbalisationManager.verbalise(axiom) + "\":\n");
	// System.out.println(explanation);
	}
}
