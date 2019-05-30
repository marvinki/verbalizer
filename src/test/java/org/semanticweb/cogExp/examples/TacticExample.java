package org.semanticweb.cogExp.examples;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.OWLFormulas.OWLSymb;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.JustificationNode;
import org.semanticweb.cogExp.core.ProofNode;
import org.semanticweb.cogExp.core.ProofNotFoundException;
import org.semanticweb.cogExp.core.ProofTree;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;
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

public class TacticExample {
	
	public static void main(String[] args){
	
		OWLFormula formA = OWLFormula.createFormulaClass("A-thing", "ant");
		OWLFormula formB = OWLFormula.createFormulaClass("B-thing", "banana");
		OWLFormula formC = OWLFormula.createFormulaClass("C-thing", "clown");
		OWLFormula formD = OWLFormula.createFormulaClass("D-thing", "dress");
		
		OWLFormula axiomFormula = OWLFormula.createFormula(OWLSymb.SUBCL, formA, formD);
		
		OWLFormula prem1 = OWLFormula.createFormula(OWLSymb.SUBCL, formA, formB);
		OWLFormula prem2 = OWLFormula.createFormula(OWLSymb.SUBCL, formB, formC);
		OWLFormula prem3 = OWLFormula.createFormula(OWLSymb.SUBCL, formC, formD);
		
		List<OWLFormula> premises = new ArrayList<OWLFormula>();
		premises.add(prem1);
		premises.add(prem2);
		premises.add(prem3);
		
		ProofTree prooftree;
		
		GentzenTree tree1;
		GentzenTree tree2;
		try {
			prooftree = InferenceApplicationService.computeSequentTree(axiomFormula, premises, 200, 2000,
					"OP"
					+ "");
			
			try {
				 InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, prooftree.getRoot().getId());
			 	 } catch (Exception e) {
			 e.printStackTrace();
				 		}
			
			// tree1 represents prooftree where tactic is applied
			tree1 = prooftree.toGentzen();
			
			// find all justifications where the rule SUBCLCHAIN is used
			List<JustificationNode> justnodes = 
					InferenceApplicationService.INSTANCE.getExpandableJustifications(prooftree,
							AdditionalDLRules.SUBCLCHAIN);
			
			
			// now expland the justification (there should be exactly one)
			JustificationNode jnode = justnodes.get(0);		
			AdditionalDLRules.SUBCLCHAIN.expandTactic(prooftree,
					prooftree.getDirectlyJustifiedNode(jnode),
					jnode);
			
			tree2 = prooftree.toGentzen();
			
			// Output the unexpanded proof tree (each line is a rule application: rule name, conclusion, premises)
			System.out.println("Prooftree \n" + VerbaliseTreeManager.listOutput(tree1) + "\n");
			
			// Output the expanded proof tree (each line is a rule application: rule name, conclusion, premises)
			System.out.println("Prooftree \n" + VerbaliseTreeManager.listOutput(tree2) + "\n");
			
			String seq1 = VerbalisationManager.computeVerbalization(tree1, false, null);
			System.out.println(seq1);
			String seq2 = VerbalisationManager.computeVerbalization(tree2, false, null);
			System.out.println(seq2);
			
		} catch (ProofNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	

	
	}
}
