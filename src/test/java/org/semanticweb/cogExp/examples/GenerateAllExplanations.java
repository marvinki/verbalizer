package org.semanticweb.cogExp.examples;
import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// import org.apache.log4j.Level;
// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;

import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.slf4j.LoggerFactory;

// import org.apache.log4j.Level;
// import org.apache.log4j.LogManager;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class GenerateAllExplanations {

	public static void main(String[] args) throws OWLOntologyCreationException{

		// Set output language (alternative: Locale.GERMAN)
		VerbaliseTreeManager.INSTANCE.init(Locale.ENGLISH);		

		/* 
		 * Load an ontology through the OWL API
		 */
		
		// Indicate path to ontology file    
		File ontologyfile = new java.io.File("U:\\ .... PATH .... \\<FILENAME>.owl"); // <======= INSERT FILE NAME.
		
		// Alternative code for loading file contained within this project (relative to project path)
		/* File file = new File(".");
			File ontologyfile = new java.io.File(Paths.get(file.getAbsoluteFile().getParentFile().getAbsolutePath(),
				"resource", 
				"<INSERT FILE NAME HERE>").toString());
		*/
		OWLOntology ontology = 
				OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(ontologyfile);	
		// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
		OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootlogger.setLevel(Level.OFF); 

		// Classify the ontology.
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		// To generate an inferred ontology we use implementations of
		// inferred axiom generators
		List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
		gens.add(new InferredSubClassAxiomGenerator());
		gens.add(new InferredEquivalentClassAxiomGenerator());

		/*
		Compute the inferrable axioms
		 */
		InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner,gens);
		OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
		Set<OWLAxiom> newaxioms = new HashSet<OWLAxiom>();
		try {
			OWLOntology newontology = outputOntologyManager.createOntology();
			iog.fillOntology(outputOntologyManager.getOWLDataFactory(), newontology);
			newaxioms = newontology.getAxioms();
		} catch(Exception e)
		{e.printStackTrace();
		}
		
		// For all inferred axioms, compute the proof tree and output the verbalization
		for (OWLAxiom ax : newaxioms){
			// do not consider trivial axioms
			if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing()){
				continue;
			}
			GentzenTree tree = VerbalisationManager.computeGentzenTree(ax, reasoner, reasonerFactory,
					ontology, 
					100,      // <-- search depth 
					1000000,    // <-- time in ms
					"OP");  
			if (tree==null){
				continue;
			}
			try{
				String explanation = VerbalisationManager.computeVerbalization(tree, false, false, null);
				System.out.println("Explanation for \"" + VerbalisationManager.textualise(ax).toString() + "\":\n");
				System.out.println(explanation);
			} catch (Exception e){
				continue;
			}	
		}
	}
}
