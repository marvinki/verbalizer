package org.semanticweb.cogExp.ProofBasedExplanation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class ClusterExplanationService {

	public static void main(String[] args) throws IOException, OWLOntologyCreationException {
		
		
		// load an ontology through the OWL API
		File file = new File("/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
		
		OWLOntology clusterOntology = 
				OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(file);	
		
		
		// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
		OWLReasonerFactory reasonerFactory = new JFactFactory();
		// Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
		// OWLReasoner reasoner = reasonerFactory.createReasoner(clusterOntology);
	    
	    // indicate the IRIs of some relevant classes/roles in the ontology
	  	OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
	  	String ontologyuri = "http://www.semanticweb.org/dennis/ontologies/2013/9/complete-ontology-20#";
	  	OWLClass Upper_Body_Training_Template  = dataFactory.getOWLClass(IRI.create(ontologyuri + "Upper_Body_Training_Template"));
	 	OWLClass WT_ChestTIRETTriceps = dataFactory.getOWLClass(IRI.create(ontologyuri + "WT_Chest-Triceps"));
	  	
		OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(WT_ChestTIRETTriceps, Upper_Body_Training_Template);
			
		
		// WordNetQuery.INSTANCE.disableDict();
		String tmpdir = "";
		try {
			tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		
		// GentzenTree tree = ProofBasedExplanationService.computeTree("WT_Chest-Triceps", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
		// String result = VerbaliseTreeManager.verbaliseNL(tree, true, false,null); // <------ 3rd arg is: true - html, false - text
		// String listResult = VerbaliseTreeManager.listOutput(tree);
		// System.out.println(result);
		// System.out.println(" ");
		// System.out.println(listResult);
		
		
		// GentzenTree tree2 = ProofBasedExplanationService.computeTree("WT_UpperBody", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
		// String result2 = VerbaliseTreeManager.verbaliseNL(tree2, true, false,null);
		// System.out.println(result2);
		
		
    	boolean cont = true;
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	while (cont) {
    		// System.out.print("Input something: ");
    		String temp = in.readLine();
    		if (temp.equals("end")) {
    			cont = false;
    			System.out.println("Terminated.");
    		}
    		else{
    			String[] inputs = temp.split(" ");
    			if (inputs.length>1){
    				GentzenTree tree = ProofBasedExplanationService.computeTree(inputs[0], inputs[1], "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
    				if (tree==null){
    					System.out.println("ERROR. Subsumption could not be proven.");
    					continue;
    				}
    				String result = VerbaliseTreeManager.verbaliseNL(tree, false, true,null); // <-- 2nd arg labels, 3rd arg html
    				System.out.println(result);
    			} else {
    				System.out.println("Please enter two class expressions");
    			}
    		}
    			// System.out.println(temp);
    	}
    }
	
}
