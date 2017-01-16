package org.semanticweb.cogExp.examples;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.ProofBasedExplanationService;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class Birds {

	public static void main(String[] args) throws OWLOntologyCreationException {
		/**
		 * this is the very smallest example one can think of
		 * 
		 */

		String tmpdir = "";
		try {
			tmpdir = WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		
		OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		
		File file = new File(".");
		File ontologyfile = new java.io.File("/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Ontologien/ornithology.owl");
		OWLOntology tinyExampleOntology = 
				OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(ontologyfile);	
		OWLReasoner reasoner = reasonerFactory.createReasoner(tinyExampleOntology);
		
		GentzenTree tree2 = ProofBasedExplanationService.computeTree("CoalTit", 
				"BirdRequiringSmallEntranceHole", 
				"/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Ontologien/ornithology.owl", 
				reasonerFactory, 
				reasoner);
		// GentzenTree tree2 = ProofBasedExplanationService.computeTree("CoalTit", "Tit", "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Ontologien/ornithology.owl", reasonerFactory, reasoner);
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		String result2 = VerbaliseTreeManager.verbaliseNL(tree2, false, false, false, null);
		System.out.println(result2);
	}

}
