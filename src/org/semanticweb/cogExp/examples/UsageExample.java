package org.semanticweb.cogExp.examples;
import java.util.Arrays;
import java.util.List;

import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;


public class UsageExample {
	
	
	public static void main(String[] args){
	
	// Small example where proof problem is defined without using an ontology
	OWLFormula class1 = OWLFormula.createFormulaClass("AnimalLover","example#AnimalLover");
	OWLFormula class2 = OWLFormula.createFormulaClass("Heart","example#Heart");
	OWLFormula class3 = OWLFormula.createFormulaClass("Animal","example#Animal");
	OWLFormula class4 = OWLFormula.createFormulaClass("Organ","example#Organ");
	
	OWLFormula role1 = OWLFormula.createFormulaRole("likes", "example#likes");
	OWLFormula role2 = OWLFormula.createFormulaRole("has", "example#has");
	
	OWLFormula axiom1 = OWLFormula.createFormulaSubclassOf(
			class1, 
			OWLFormula.createFormulaExists(role1, class3));
	
	OWLFormula axiom2 = OWLFormula.createFormulaSubclassOf(
			class3, 
			OWLFormula.createFormulaExists(role2, class2));
	
	OWLFormula axiom3 = OWLFormula.createFormulaSubclassOf(class2,class4);
	
	OWLFormula axiom4 = OWLFormula.createFormulaSubclassOf(class1,
			OWLFormula.createFormulaExists(role1,
					OWLFormula.createFormulaExists(role2,class4
					)));
	
	OWLFormula[] axioms = new OWLFormula[]{axiom1, axiom2, axiom3};
	
	// Obtaining the proof
	try {
		GentzenTree tree = InferenceApplicationService.computeProofTree(axiom4, Arrays.asList(axioms), 1000, "EL");
		// Getting the verbalisation
		// System.out.println(tree.verbaliseNL());
		
		// An example for how to access the individual steps of the proof tree
		
		List<GentzenStep> steps = tree.getStepsInOrder();
		for (GentzenStep step : steps){
			System.out.println("PROOFSTEP: ");
			System.out.println("Inference rule: " + tree.stepGetRuleName(step));
			System.out.println("Premises: " + tree.stepGetPremiseFormulas(step));
			System.out.println("Conclusion: " + tree.stepGetConclusionFormula(step));
			// System.out.println("Verbalised conclusion: " + tree.stepGetConclusionFormula(step).verbalise());
		}
		
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return;
	}
	
	
	
}
