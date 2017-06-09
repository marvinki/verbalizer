package org.semanticweb.cogExp.examples;
import java.io.IOException;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.ProofBasedExplanationService;
import org.semanticweb.cogExp.ProofBasedExplanation.TextExplanationResult;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class UpperBodyExample {

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
		
		// System.out.println("FpExample: start searchin'");
		
		/**
		 * generate a proof based explanation in form of a gentzen tree by passing 
		 * 		1. a subclass(WT_UpperBody) 
		 * 		2. a superclass(Upper_Body_training_Template) and 
		 * 		3. a file containing the respective ontology !!!!!!!!!!!change this file to your ne!!!!!!!!!!!!
		 */
		// GentzenTree tree2 = ProofBasedExplanationService.computeTree("WT_UpperBody", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
		/**
		 * verbalising the the freshly generated tree by passing 
		 * 		1. the tree
		 * 		2. a boolean describing whether or not the specific rule names are contained
		 * 		3. a boolean describing whether or not the output is supposed to be HTML
		 * 		4. if the output is supposed to contain meaningful concepts or not (null means yes, some obfuscator means no)
		 */
		// String result2 = VerbaliseTreeManager.verbaliseNL(tree2, false, false, null);
		// System.out.println(result2);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		java.io.File file = new java.io.File("/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
		FileDocumentSource source = new FileDocumentSource(file);
		OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
		loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
		loaderconfig = loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));
				
		OWLOntology ontology;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
			VerbalisationManager.INSTANCE.setOntology(ontology);
		
		
		
		// construct axiom
	  	OWLDataFactory dataFactory=manager.getOWLDataFactory();
		
		 
		 OWLClass subcl = null;
		 OWLClass supercl = null;
		 
		 Set<OWLClass> classes = ontology.getClassesInSignature();
		 for (OWLClass cl : classes){
			 // System.out.println(cl.toString());
			 if (cl.getIRI().getFragment().equals("WT_UpperBody")){
				 subcl = cl;
			 }
			 if (cl.getIRI().getFragment().equals("Upper_Body_Training_Template")){
				 supercl = cl;
			 }
		 }
	
		 
		 OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
			Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
			OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
			 OWLReasonerFactory reasonerFactory2 = new JFactFactory();
			 OWLReasonerConfiguration config = new SimpleConfiguration(30000);
			 OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology,config);
			
			
		 OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(subcl, supercl);
		
		TextElementSequence sequence = ProofBasedExplanationService.getExplanationResultAsSequence(axiom, reasonerJFact, reasonerFactory2, 
					ontology, true, "OP");	
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		// TextExplanationResult result = new TextExplanationResult(panel,null);
		// result = result.getResult(sequence);
		// frame.add(result);
		frame.setSize(1200, 900);
		
			        frame.setLocationRelativeTo(null);
		
			        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		System.out.println(sequence.toString());
		
		}catch(Exception e){
			e.printStackTrace();
		}
		}

}
