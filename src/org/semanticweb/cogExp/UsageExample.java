package org.semanticweb.cogExp;

import java.util.Arrays;
import java.util.List;

import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseOWLObjectVisitor;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLObjectVisitor;

public class UsageExample {
	
	
	public static void main(String[] args){
		
	PrettyPrintOWLObjectVisitor ppVisitor = new PrettyPrintOWLObjectVisitor();
	
	// Small example where proof problem is defined without using an ontology
	OWLFormula animalLover = OWLFormula.createFormulaClass("AnimalLover","example#AnimalLover");
	OWLFormula heart = OWLFormula.createFormulaClass("Heart","example#Heart");
	OWLFormula animal = OWLFormula.createFormulaClass("Animal","example#Animal");
	OWLFormula organ = OWLFormula.createFormulaClass("Organ","example#Organ");
	
	OWLFormula likes = OWLFormula.createFormulaRole("likes", "example#likes");
	OWLFormula has = OWLFormula.createFormulaRole("has", "example#has");
	
	
	// Formulate the axioms
	OWLFormula axiom1 = OWLFormula.createFormulaSubclassOf(
			animalLover, 
			OWLFormula.createFormulaExists(likes, animal));
	
	System.out.println("Axiom 1 (nicht schoen!): " +axiom1);
	System.out.println("Axiom 1: " + VerbalisationManager.prettyPrint(axiom1));
	
	OWLFormula axiom2 = OWLFormula.createFormulaSubclassOf(
			animal, 
			OWLFormula.createFormulaExists(has, heart));
	
	System.out.println("Axiom 2: " + VerbalisationManager.prettyPrint(axiom2));
	
	OWLFormula axiom3 = OWLFormula.createFormulaSubclassOf(heart,organ);
	
	System.out.println("Axiom 3: " + VerbalisationManager.prettyPrint(axiom3));
	
	// Axiom4 is the conclusion
	OWLFormula axiom4 = OWLFormula.createFormulaSubclassOf(animalLover,
			OWLFormula.createFormulaExists(likes,
					OWLFormula.createFormulaExists(has,organ
					)));
	
	System.out.println("Axiom 4: " + VerbalisationManager.prettyPrint(axiom4));
	
	OWLFormula[] axioms = new OWLFormula[]{axiom1, axiom2, axiom3};
	
	// Obtaining the proof
	try {
		GentzenTree tree = InferenceApplicationService.computeProofTree(axiom4, Arrays.asList(axioms), 1000, "EL");
		// Choose whether to use wordnet (enable one of the two lines below)
		WordNetQuery.INSTANCE.disableDict(); // switch off wordnet
		// WordNetQuery.INSTANCE.setDict("/Users/marvin/software/wordnet/WordNet-3.0/dict"); // put in the correct path to wordnet
		System.out.println("Generated text:");
		// Getting the verbalisation
		System.out.println(VerbaliseTreeManager.verbaliseNL(tree,true));
		// System.out.println(tree.verbaliseNL());
		
		// An example for how to access the individual steps of the proof tree
		List<GentzenStep> steps = tree.getStepsInOrder();
		for (GentzenStep step : steps){
			System.out.println("PROOFSTEP: ");
			System.out.println("Inference rule: " + tree.stepGetRuleName(step));
			System.out.println("Premises: " + tree.stepGetPremiseFormulas(step));
			System.out.println("Conclusion: " + tree.stepGetConclusionFormula(step));
			System.out.println("Verbalised conclusion: " 
			+  VerbalisationManager.verbalise(ConversionManager.toOWLAPI(tree.stepGetConclusionFormula(step))));
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
  	OWLDataFactory dataFactory=manager.getOWLDataFactory();
	OWLClass localAligning  = 
    		 dataFactory.getOWLClass(IRI.create("http://sth#LocalAligning"));
     OWLClass pairwiseLocalAligning  = 
    		 dataFactory.getOWLClass(IRI.create("http://sth#PairwiseLocalAligning"));
     OWLClass winterGardenBuilding  = 
    		 dataFactory.getOWLClass(IRI.create("http://www.ordnancesurvey.co.uk/ontology/BuildingsAndPlaces/v1.1/BuildingsAndPlaces.owl#WinterGarden_Building"));
     OWLClass entertainment  = dataFactory.getOWLClass(IRI.create(""));
    
    OWLClass dog = dataFactory.getOWLClass(IRI.create("http://sth#Dog"));
    OWLClass cat = dataFactory.getOWLClass(IRI.create("http://sth#Cat"));
    OWLClass grownup = dataFactory.getOWLClass(IRI.create("http://sth#Grownup"));
    OWLClass youth = dataFactory.getOWLClass(IRI.create("http://sth#Youth"));
    OWLClass vehicle = dataFactory.getOWLClass(IRI.create("http://sth#Vehicle"));
    OWLClass secretedSubstance = dataFactory.getOWLClass(IRI.create("http://sth#SecretedSubstance"));
    OWLClass substance = dataFactory.getOWLClass(IRI.create("http://sth#Substance"));
    OWLClass secretion = dataFactory.getOWLClass(IRI.create("http://sth#Secretion"));
    OWLClass person = dataFactory.getOWLClass(IRI.create("http://sth#Person"));
    OWLClass driver = dataFactory.getOWLClass(IRI.create("http://sth#Driver"));
     
    
       
	 OWLObjectProperty hasFeature = dataFactory.getOWLObjectProperty(IRI.create("http://purl.org/obo/owl/obo#hasFeature"));
	 OWLObjectProperty isActedOnBy = dataFactory.getOWLObjectProperty(IRI.create("http://purl.org/obo/owl/obo#isActedOnBy"));
	 OWLObjectProperty drives = dataFactory.getOWLObjectProperty(IRI.create("http://purl.org/obo/owl/obo#drives"));
	 // OWLObjectProperty eng = dataFactory.getOWLObjectProperty(IRI.create("http://purl.org/obo/owl/obo#engages"));
	
	// OWLClassExpression test1 = dataFactory.getOWLObjectSomeValuesFrom(incl,Squat);
	// OWLClassExpression test2 = test1.getObjectComplementOf();
	// OWLObjectComplementOf compl = (OWLObjectComplementOf) test2;
	// System.out.println(compl.getOperand());
	 
	 
	OWLDisjointClassesAxiom disj1 = dataFactory.getOWLDisjointClassesAxiom(dog,cat);
    VerbaliseOWLObjectVisitor visitor = new VerbaliseOWLObjectVisitor();
    System.out.println(disj1.accept(visitor));
    OWLDisjointClassesAxiom disj2 = dataFactory.getOWLDisjointClassesAxiom(
    		dataFactory.getOWLObjectIntersectionOf(grownup,
		  dataFactory.getOWLObjectSomeValuesFrom(drives,vehicle)),
    		dataFactory.getOWLObjectIntersectionOf(youth,
		  dataFactory.getOWLObjectSomeValuesFrom(drives,vehicle)));
   System.out.println(disj2.accept(visitor));
   
   OWLEquivalentClassesAxiom eqv1 = dataFactory.getOWLEquivalentClassesAxiom(secretedSubstance, 
		  dataFactory.getOWLObjectIntersectionOf(substance,
		  dataFactory.getOWLObjectSomeValuesFrom(isActedOnBy, secretion)));
  System.out.println(eqv1.accept(visitor));
  
 OWLEquivalentClassesAxiom eqv2 = dataFactory.getOWLEquivalentClassesAxiom(
		  dataFactory.getOWLObjectIntersectionOf(person,
		  dataFactory.getOWLObjectSomeValuesFrom(drives, vehicle)),
		 dataFactory.getOWLObjectIntersectionOf(grownup,driver));
  System.out.println(eqv2.accept(visitor));

	 
	OWLEquivalentClassesAxiom ax1 = dataFactory.getOWLEquivalentClassesAxiom(
			localAligning,
			dataFactory.getOWLObjectSomeValuesFrom(hasFeature, localAligning)
			);
	
	OWLSubClassOfAxiom ax2 =  dataFactory.getOWLSubClassOfAxiom(pairwiseLocalAligning,
			localAligning
			);
	
	return;
	}
	
	
}
