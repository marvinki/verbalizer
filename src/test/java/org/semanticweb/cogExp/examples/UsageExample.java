package org.semanticweb.cogExp.examples;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.inferencerules.INLG2012NguyenEtAlRules;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
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
	
	// Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
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
	
	// WordNetQuery.INSTANCE.disableDict();
	// String explanation = VerbalisationManager.verbalizeAxiom(axiom, reasoner, reasonerFactory, tinyExampleOntology, false,false);
	
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	OWLOntology ont = manager.loadOntologyFromOntologyDocument(ontologyfile);
	
	// The proof tree is constructed 
	GentzenTree tree = VerbalisationManager.computeGentzenTree(axiom,reasoner, reasonerFactory, ont, 100, 1000, "OP");
	
	// Output the proof tree (each line is a rule application: rule name, conclusion, premises)
	System.out.println("Prooftree \n" + VerbaliseTreeManager.listOutput(tree) + "\n");
	
	// Generate the explanation from the proof tree
	String explanation = VerbalisationManager.computeVerbalization(tree, false, false, null);
	
	System.out.println("Explanation for \"" + VerbalisationManager.textualise(axiom).toString() + "\":\n");
	System.out.println(explanation);
	

	System.out.println(VerbalisationManager.treatCamelCaseAndUnderscores("Stage_IV_Enteropathy-type_T-Cell_Lymphoma"));
	
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
	
	}
}
