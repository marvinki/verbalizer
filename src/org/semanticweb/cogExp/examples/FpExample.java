package org.semanticweb.cogExp.examples;
import java.io.IOException;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.ProofBasedExplanationService;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;

public class FpExample {

	public static void main(String[] args) {
		/**
		 * this is the very smallest example one can think of
		 * 
		 */

		String tmpdir = "";
		try {
			tmpdir = WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		
		System.out.println("FpExample: start searchin'");
		
		/**
		 * generate a proof based explanation in form of a gentzen tree by passing 
		 * 		1. a subclass(WT_UpperBody) 
		 * 		2. a superclass(Upper_Body_training_Template) and 
		 * 		3. a file containing the respective ontology !!!!!!!!!!!change this file to your ne!!!!!!!!!!!!
		 */
		GentzenTree tree2 = ProofBasedExplanationService.computeTree("WT_UpperBody", "Upper_Body_Training_Template", "/home/fpaffrath/Dokumente/cluster6ontology_complete.rdf");
		/**
		 * verbalising the the freshly generated tree by passing 
		 * 		1. the tree
		 * 		2. a boolean describing whether or not the specific rule names are contained
		 * 		3. a boolean describing whether or not the output is supposed to be HTML
		 * 		4. if the output is supposed to contain meaningful concepts or not (null means yes, some obfuscator means no)
		 */
		String result2 = VerbaliseTreeManager.verbaliseNL(tree2, false, false, null);
		System.out.println(result2);
	}

}
