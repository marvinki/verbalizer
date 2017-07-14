package org.semanticweb.cogExp.examples;
import java.io.File;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// import org.apache.log4j.Level;
// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.inferencerules.INLG2012NguyenEtAlRules;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.slf4j.LoggerFactory;

// import org.apache.log4j.Level;
// import org.apache.log4j.LogManager;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class RunTroughExample {
	
	public static void main(String[] args) throws OWLOntologyCreationException{
	
	// load an ontology through the OWL API
	File file = new File(".");
	File ontologyfile = new java.io.File(Paths.get(file.getAbsoluteFile().getParentFile().getAbsolutePath(),
	 								  "resource", 
	 								  "tinyExampleOntology2.owl").toString());
	
	// File ontologyfile = new java.io.File("/Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-galen.owl");
	
	OWLOntology ontology = 
			OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(ontologyfile);	
	
	
	// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
	OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
	OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
	
	// Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
	
	 Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
     rootlogger.setLevel(Level.OFF); 
    // Logger iogLogger = (Logger) LoggerFactory.getLogger(InferredOntologyGenerator.class.getName());
    
    
     // LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
	
	
	// Classify the ontology.
	reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

	// To generate an inferred ontology we use implementations of
	// inferred axiom generators
	List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
	gens.add(new InferredSubClassAxiomGenerator());
	// gens.add(new InferredEquivalentClassAxiomGenerator());
    
	/*
	Compute the inferrable axioms
	*/
	InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner,gens);
	// OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
	Set<OWLAxiom> newaxioms = new HashSet<OWLAxiom>();
	try {
		
		OWLOntology newontology = outputOntologyManager.createOntology();
		
		iog.fillOntology(outputOntologyManager.getOWLDataFactory(), newontology);
		
	
		newaxioms = newontology.getAxioms();
		System.out.println("No. of new axioms: " + (newaxioms.size()));
	} catch(Exception e)
	{e.printStackTrace();
	}
	
	for (OWLAxiom ax : newaxioms){
		GentzenTree tree = VerbalisationManager.computeGentzenTree(ax, reasoner, reasonerFactory,
				ontology, 100, 10000, "OP");
		
		if (tree==null)
			continue;
		
		try{
		WordNetQuery.INSTANCE.disableDict();
		String explanation = VerbalisationManager.computeVerbalization(tree, false, null);
		System.out.println("Explanation for \"" + VerbalisationManager.verbalise(ax) + "\":\n");
		System.out.println(explanation);
		} catch (Exception e){
			continue;
		}
		
		List<Integer> proofsteps =  tree.computePresentationOrder();
		System.out.println(proofsteps);
		System.out.println("\n == INFORMATION CONTAINED IN THE PROOFTREE == \n");
		for (int id: proofsteps){
			GentzenStep step = tree.getStepByID(id);
			System.out.println("Inference rule: " + step.getInfrule());
			SequentInferenceRule infrule = step.getInfrule();
			
			// Get premises and conclusions of the inference rule (as OWLFormula)
			List<OWLFormula> premises = step.getPremiseFormulas();
			OWLFormula conclusion = step.getConclusionFormula();	
			OWLFormula prem1 = infrule.getP1(premises,conclusion);
			OWLFormula prem2 = infrule.getP2(premises,conclusion);
			
			// Convert everything to OWLAPI format
			List<OWLObject> premisesOWLAPI = ConversionManager.toOWLAPI(premises);
			OWLObject conclusionOWLAPI = ConversionManager.toOWLAPI(conclusion);
			OWLObject prem1OWLAPI = null;
			if (prem1 !=null)
				prem1OWLAPI = ConversionManager.toOWLAPI(prem1);
			OWLObject prem2OWLAPI = null;
			if (prem2 !=null)
				prem2OWLAPI = ConversionManager.toOWLAPI(prem2);
			
			// Output	
			if (prem1OWLAPI!=null)
				System.out.println("Premise 1 " + VerbalisationManager.prettyPrint(prem1OWLAPI));
			if (prem2OWLAPI!=null)
				System.out.println("Premise 2 " + VerbalisationManager.prettyPrint(prem2OWLAPI));
			System.out.println("Conclusion: " + VerbalisationManager.prettyPrint(conclusionOWLAPI) + "\n");
		}
	}
   
	/*
	
	
	// String explanation = VerbalisationManager.verbalizeAxiom(axiom, reasoner, reasonerFactory, tinyExampleOntology, false,false);
	
	
	
	// The proof tree is constructed 
	GentzenTree tree = VerbalisationManager.computeGentzenTree(axiom,reasoner, reasonerFactory, ont, 100, 1000, "OP");
	
	// Output the proof tree (each line is a rule application: rule name, conclusion, premises)
	System.out.println("Prooftree \n" + VerbaliseTreeManager.listOutput(tree) + "\n");
	
	// Generate the explanation from the proof tree
	String explanation = VerbalisationManager.computeVerbalization(tree, false, null);
	
	System.out.println("Explanation for \"" + VerbalisationManager.verbalise(axiom) + "\":\n");
	System.out.println(explanation);
	
	List<Integer> proofsteps =  tree.computePresentationOrder();
	System.out.println(proofsteps);
	System.out.println("\n == INFORMATION CONTAINED IN THE PROOFTREE == \n");
	for (int id: proofsteps){
		GentzenStep step = tree.getStepByID(id);
		System.out.println("Inference rule: " + step.getInfrule());
		SequentInferenceRule infrule = step.getInfrule();
		
		// Get premises and conclusions of the inference rule (as OWLFormula)
		List<OWLFormula> premises = step.getPremiseFormulas();
		OWLFormula conclusion = step.getConclusionFormula();	
		OWLFormula prem1 = infrule.getP1(premises,conclusion);
		OWLFormula prem2 = infrule.getP2(premises,conclusion);
		
		// Convert everything to OWLAPI format
		List<OWLObject> premisesOWLAPI = ConversionManager.toOWLAPI(premises);
		OWLObject conclusionOWLAPI = ConversionManager.toOWLAPI(conclusion);
		OWLObject prem1OWLAPI = ConversionManager.toOWLAPI(prem1);
		OWLObject prem2OWLAPI = ConversionManager.toOWLAPI(prem2);
		
		// Output	
		System.out.println("Premise 1 " + VerbalisationManager.prettyPrint(prem1OWLAPI));
		System.out.println("Premise 2 " + VerbalisationManager.prettyPrint(prem2OWLAPI));
		System.out.println("Conclusion: " + VerbalisationManager.prettyPrint(conclusionOWLAPI) + "\n");
	}
	*/
	
	}
	
	
}
