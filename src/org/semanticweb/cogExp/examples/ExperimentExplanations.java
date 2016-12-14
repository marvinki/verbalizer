package org.semanticweb.cogExp.examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;
import org.semanticweb.cogExp.core.IncrementalSequent;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.ProofNode;
import org.semanticweb.cogExp.core.ProofTree;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.core.SequentPosition;
import org.semanticweb.cogExp.coverageEvaluator.CoverageStoreEvaluatorCompressionDB;

public class ExperimentExplanations {
	
	public static ProofTree constructProoftree(String conclusion, String justifications){
		OWLOntologyManager manager = CoverageStoreEvaluatorCompressionDB.getImportKnowledgeableOntologyManger();
		// OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory=manager.getOWLDataFactory();
		File ontologyfile = new java.io.File("/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-galen1.owl");
		// OWLOntology ontology = CoverageStoreEvaluatorCompressionDB.createOntology();
		OWLOntology ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(ontologyfile);
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		VerbalisationManager.INSTANCE.setOntology(ontology);
		
		// List<OWLAxiom> justifications1axioms = new ArrayList<OWLAxiom>();

		OWLAxiom conclusion1axiom = CoverageStoreEvaluatorCompressionDB.parseAxiomFunctional(conclusion,ontology);
		Set<OWLAxiom> justifications1axioms = CoverageStoreEvaluatorCompressionDB.parseAxiomsFunctional(justifications,ontology);
		
		System.out.println("Conclusion " + conclusion1axiom.toString());
		System.out.println("Justifications " + justifications1axioms.toString());
		
		// conversion
		IncrementalSequent sequent = new IncrementalSequent();
		for(OWLAxiom just:justifications1axioms){
			try {
				sequent.addAntecedent(ConversionManager.fromOWLAPI(just));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			sequent.addSuccedent(ConversionManager.fromOWLAPI(conclusion1axiom));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ProofNode<Sequent<OWLFormula>,String,SequentPosition> node 
		= new ProofNode<Sequent<OWLFormula>,String,SequentPosition>(sequent,null,0);
		ProofTree<ProofNode<Sequent<OWLFormula>,String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLFormula>,String,org.semanticweb.cogExp.core.SequentPosition>>(node);
	  
		
		sequent.setHighestInitAxiomid(1000);
		
		
		
		return prooftree;
	}

	public static void main(String[] args) throws OWLOntologyCreationException{
	
	// Explanation 1
	
	String justifications1="SubClassOf(<http://www.co-ode.org/ontologies/galen#Hypogastrium> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#exactlyPaired>)) SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDSurfaceSubpart> <http://www.co-ode.org/ontologies/galen#BodyPart>) SubClassOf(<http://www.co-ode.org/ontologies/galen#Hypogastrium> <http://www.co-ode.org/ontologies/galen#NAMEDSurfaceSubpart>) SubClassOf(<http://www.co-ode.org/ontologies/galen#BodyPart> <http://www.co-ode.org/ontologies/galen#BodyStructure>) EquivalentClasses(<http://www.co-ode.org/ontologies/galen#ExactlyPairedBodyStructure> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#BodyStructure> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#exactlyPaired>)) )";
	String conclusion1 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Hypogastrium> <http://www.co-ode.org/ontologies/galen#ExactlyPairedBodyStructure>)";;
	
	String tmpdir = "";
	
	try {
		tmpdir = WordnetTmpdirManager.makeTmpdir();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	WordNetQuery.INSTANCE.setDict(tmpdir);
	
	List<SequentInferenceRule> rules = RuleSetManager.INSTANCE.getRuleSet("EL");
	List<SequentInferenceRule> rulesNonred = RuleSetManager.INSTANCE.getRuleSet("ELnonredundant");
	
	ProofTree prooftree = constructProoftree(conclusion1,justifications1);
	
	
	
	
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, rulesNonred, 840000, 400000);
	try {
		System.out.println("Eliminating irrelevant parts");
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, prooftree.getRoot().getId());
				// node.getId());
		GentzenTree gentzenTree = prooftree.toGentzen();	
		System.out.println(VerbaliseTreeManager.listOutput(gentzenTree));
		System.out.println();
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree, true, false,null));
		
	} catch (Exception e) {
		System.out.println("Error while eliminating irrelevant parts");
		e.printStackTrace();
 		}
	
	
	}
	
}
