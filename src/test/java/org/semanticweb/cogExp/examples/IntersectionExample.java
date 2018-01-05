package org.semanticweb.cogExp.examples;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.IncrementalSequent;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.ProofNode;
import org.semanticweb.cogExp.core.ProofTree;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.core.SequentPosition;
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


public class IntersectionExample {
	
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
  	String ontologyuri = "#";
  	OWLClass b1 = dataFactory.getOWLClass(IRI.create(ontologyuri + "B1"));
  	OWLClass b2 = dataFactory.getOWLClass(IRI.create(ontologyuri + "B2"));
 	OWLClass a1 = dataFactory.getOWLClass(IRI.create(ontologyuri + "A1"));
 	OWLClass a2 = dataFactory.getOWLClass(IRI.create(ontologyuri + "A2"));
 	OWLClass a3 = dataFactory.getOWLClass(IRI.create(ontologyuri + "A3"));
 	OWLClass a4 = dataFactory.getOWLClass(IRI.create(ontologyuri + "A4"));
 	OWLClass a5 = dataFactory.getOWLClass(IRI.create(ontologyuri + "A5"));
 	OWLClass a6 = dataFactory.getOWLClass(IRI.create(ontologyuri + "A6"));
 	OWLClass a7 = dataFactory.getOWLClass(IRI.create(ontologyuri + "A7"));
  	
	
  	// Now specify which axiom is to be explained;
  	// For example: 
  	// "AnimalLover subClassOf likes some (has some Organ)"
	OWLSubClassOfAxiom axiomPrem1 = dataFactory.getOWLSubClassOfAxiom(b1, 
			dataFactory.getOWLObjectIntersectionOf(a1,a2,a3,a4,a5,a6,a7)
			);
	
	OWLSubClassOfAxiom axiomPrem2 = dataFactory.getOWLSubClassOfAxiom( 
			dataFactory.getOWLObjectIntersectionOf(a5,a4,a3,a2,a1),b2);
	
	OWLSubClassOfAxiom axiomConcl = dataFactory.getOWLSubClassOfAxiom( 
			b1,b2);
	
	IncrementalSequent sequent = new IncrementalSequent();
	try {
		sequent.addAntecedent(ConversionManager.fromOWLAPI(axiomPrem1));
		sequent.addAntecedent(ConversionManager.fromOWLAPI(axiomPrem2));
		sequent.addSuccedent(ConversionManager.fromOWLAPI(axiomConcl));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	ProofNode<Sequent<OWLFormula>,java.lang.String,SequentPosition> node 
	= new ProofNode<Sequent<OWLFormula>,java.lang.String,SequentPosition>(sequent,null,0);
	ProofTree<ProofNode<Sequent<OWLFormula>,String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition>>(node);
  
	 List<SequentInferenceRule> nonredundantInferenceRules = RuleSetManager.INSTANCE.getRuleSet("ELnonredundant");
	
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, nonredundantInferenceRules, 840000, 100000);
	
	// System.out.println(prooftree.getOpenNodes());
	
	System.out.println(prooftree.toString());
	
	try {
		System.out.println("Eliminating irrelevant parts 1");
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, node.getId());
	} catch (Exception e) {
		System.out.println("Error while eliminating irrelevant parts 1");
		e.printStackTrace();
 		}
	
	org.semanticweb.cogExp.GentzenTree.GentzenTree gentzenTree;
	
	try {
		
		gentzenTree = prooftree.toGentzen();
		
		System.out.println(VerbaliseTreeManager.listOutput(gentzenTree));
		
		
		String result = VerbaliseTreeManager.verbaliseNL(gentzenTree, true, false,false, null);
		System.out.println(result);
	}catch(Exception e){}
	
	// WordNetQuery.INSTANCE.disableDict();
	// String explanation = VerbalisationManager.verbalizeAxiom(axiom, reasoner, reasonerFactory, tinyExampleOntology, false,false);
	
	
	// System.out.println("Explanation for \"" + VerbalisationManager.verbalise(axiom) + "\":\n");
	// System.out.println(explanation);
	
	// System.out.println(VerbalisationManager.treatCamelCaseAndUnderscores("Stage_IV_Enteropathy-type_T-Cell_Lymphoma"));
	
	
	}
}
