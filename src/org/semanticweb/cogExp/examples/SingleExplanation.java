package org.semanticweb.cogExp.examples;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.IncrementalSequent;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.ProofNode;
import org.semanticweb.cogExp.core.ProofTree;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.core.SequentPosition;
import org.semanticweb.cogExp.coverageEvaluator.CoverageStoreEvaluatorCompressionDB;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;


public class SingleExplanation {
	
	public static void main(String[] args) throws OWLOntologyCreationException{
	
	
	
	String filestring = "/Users/marvin/work/workspace/justifications/originaltones-dumps/dumps-for-ecai/jfact-Ontology-tambis-patched.owl.dump";   // <----- dumpfile
	// String subclass = "<http://www.mindswap.org/ontologies/tambis-full.owl#gene-promoter>";
	// String superclass = "<http://www.mindswap.org/ontologies/tambis-full.owl#protein-name>";
	
	String subclass = "<http://www.mindswap.org/ontologies/tambis-full.owl#holoenzyme>";
	String superclass = "<http://www.mindswap.org/ontologies/tambis-full.owl#rna>";
	
	// <http://www.mindswap.org/ontologies/tambis-full.owl#holoenzyme> <http://www.mindswap.org/ontologies/tambis-full.owl#rna>
	
	List<OWLAxiom> conclusions = new ArrayList<OWLAxiom>();
	List<List<OWLAxiom>> justifications = new ArrayList<List<OWLAxiom>>();
	CoverageStoreEvaluatorCompressionDB.readJustifications(filestring, conclusions, justifications);
	
	List<OWLAxiom> justs = null;
	OWLAxiom conclusion = null;
	for (int i = 0; i< conclusions.size(); i++){
		OWLAxiom concl= conclusions.get(i);
		if (concl.toString().contains(subclass) && concl.toString().contains(superclass)){
			if (concl instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom conclAx= (OWLSubClassOfAxiom) concl;
				OWLClassExpression exp1 = conclAx.getSubClass();
				OWLClassExpression exp2 = conclAx.getSuperClass();
				if (exp1.toString().contains(subclass) && exp2.toString().contains(superclass)){
					justs = justifications.get(i);
					conclusion = concl;
				}
			}
		}
	}
	
	
	IncrementalSequent sequent = new IncrementalSequent();
	for(OWLAxiom just:justs){
		System.out.println("Prem: " + VerbalisationManager.prettyPrint(just));
		try {
			sequent.addAntecedent(ConversionManager.fromOWLAPI(just));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	try {
		System.out.println("Conclusion: " + VerbalisationManager.prettyPrint(conclusion));
		sequent.addSuccedent(ConversionManager.fromOWLAPI(conclusion));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	// System.out.println(sequent);
	
	ProofNode<Sequent<OWLFormula>,String,SequentPosition> node 
	= new ProofNode<Sequent<OWLFormula>,String,SequentPosition>(sequent,null,0);
	ProofTree<ProofNode<Sequent<OWLFormula>,String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLFormula>,String,org.semanticweb.cogExp.core.SequentPosition>>(node);
  
	List<SequentInferenceRule> rules = RuleSetManager.INSTANCE.getRuleSet("EL");
	List<SequentInferenceRule> rulesNonred = RuleSetManager.INSTANCE.getRuleSet("ELnonredundant");
	sequent.setHighestInitAxiomid(1000);
	
	long startLoop = System.currentTimeMillis();
	
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, rulesNonred, 840000, 400000);
	
	long endLoop = System.currentTimeMillis();
	System.out.println("Time taken " + ((endLoop - startLoop)/1000));
	
	System.out.println(prooftree.getOpenNodes().size());
	
	try {
		System.out.println("Eliminating irrelevant parts");
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, node.getId());
		GentzenTree gentzenTree = prooftree.toGentzen();	
		System.out.println(VerbaliseTreeManager.listOutput(gentzenTree));
		
	} catch (Exception e) {
		System.out.println("Error while eliminating irrelevant parts");
		e.printStackTrace();
 		}
	
	// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
	// OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
	// Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
	// OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
    
	/*
	
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
	
	System.out.println(prooftree.getOpenNodes());
	
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
		
		
		String result = VerbaliseTreeManager.verbaliseNL(gentzenTree, true, false,null);
		System.out.println(result);
	}catch(Exception e){}
	
	// WordNetQuery.INSTANCE.disableDict();
	// String explanation = VerbalisationManager.verbalizeAxiom(axiom, reasoner, reasonerFactory, tinyExampleOntology, false,false);
	
	
	// System.out.println("Explanation for \"" + VerbalisationManager.verbalise(axiom) + "\":\n");
	// System.out.println(explanation);
	
	// System.out.println(VerbalisationManager.treatCamelCaseAndUnderscores("Stage_IV_Enteropathy-type_T-Cell_Lymphoma"));
	
	
	*/
	
	}
	
	
}
