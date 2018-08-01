package org.semanticweb.cogExp.examples;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.functional.parser.OWLFunctionalSyntaxOWLParser;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPIManagerManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;
import org.semanticweb.cogExp.core.IncrementalSequent;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.ProofNode;
import org.semanticweb.cogExp.core.ProofTree;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.core.SequentPosition;	

public class StudyExplanationSelection {
		
		// helper method for constructing proof trees (to be applied for generating all explanations)
		public static ProofTree constructProoftree(String conclusion, String justifications, String ontologyfilestring){
			OWLOntologyManager manager = OWLAPIManagerManager.getImportKnowledgeableOntologyManger();
			// OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory=manager.getOWLDataFactory();
			File ontologyfile = new java.io.File(ontologyfilestring);
			// OWLOntology ontology = CoverageStoreEvaluatorCompressionDB.createOntology();
			OWLOntology ontology = null;
			try {
				ontology = manager.loadOntologyFromOntologyDocument(ontologyfile);
			} catch (OWLOntologyCreationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			VerbalisationManager.INSTANCE.setOntology(ontology);
			
			// List<OWLAxiom> justifications1axioms = new ArrayList<OWLAxiom>();

			OWLAxiom conclusion1axiom = OWLAPIManagerManager.parseAxiomFunctional(conclusion,ontology);
			Set<OWLAxiom> justifications1axioms = OWLAPIManagerManager.parseAxiomsFunctional(justifications,ontology);
			
			System.out.println("Conclusion " + conclusion1axiom.toString());
			System.out.println("Justifications " + justifications1axioms.toString());
			
			// conversion
			IncrementalSequent sequent = new IncrementalSequent();
			System.out.println("VERBALIZED AXIOMS:");
			for(OWLAxiom just:justifications1axioms){
				try {
					sequent.addAntecedent(ConversionManager.fromOWLAPI(just));
					// get sequence
					TextElementSequence justSeq = VerbalisationManager.textualise(just);
					String justHTML = justSeq.toHTML();
					String justHTML2 = justHTML.replace("<font>&nbsp;</font>"," ");
					String justHTML3 = justHTML2.replace("<font color=blue>","<span style=\"color: blue\">");
					String justHTML4 = justHTML3.replace("</font>","</span>");
					String justHTML5 = justHTML4.replace("<font color=Maroon>","<span style=\"color: black\">");
					System.out.println(justHTML5);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				sequent.addSuccedent(ConversionManager.fromOWLAPI(conclusion1axiom));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			ProofNode<Sequent<OWLFormula>,String,SequentPosition> node 
			= new ProofNode<Sequent<OWLFormula>,String,SequentPosition>(sequent,null,0);
			ProofTree<ProofNode<Sequent<OWLFormula>,String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLFormula>,String,org.semanticweb.cogExp.core.SequentPosition>>(node);
		  
			
			sequent.setHighestInitAxiomid(1000);
			
			
			
			return prooftree;
		}

		
		public static void generateExplanation(String justifications, String conclusion, String ontology){
			
			List<SequentInferenceRule> rules = RuleSetManager.INSTANCE.getRuleSet("EL");
			
			ProofTree prooftree3_b = constructProoftree(conclusion,justifications,ontology);			
			InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree3_b, rules, 840000, 400000);
			try {
						
					GentzenTree gentzenTree3_b= prooftree3_b.toGentzen();	
				
				
				System.out.println("EXPLANATION:");
				VerbalisationManager.INSTANCE.featuresOFF=false;
				System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree3_b, false, true,null).replace("<font>&nbsp;</font>"
						," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
				
				System.out.println("no. steps: " + gentzenTree3_b.computePresentationOrder().size());
				
			} catch (Exception e) {
				System.out.println("Error while eliminating irrelevant parts");
				e.printStackTrace();
			}
		}
		
		public static void main(String[] args) throws OWLOntologyCreationException{
		
			List<SequentInferenceRule> rules = RuleSetManager.INSTANCE.getRuleSet("EL");
			List<SequentInferenceRule> rulesNonred = RuleSetManager.INSTANCE.getRuleSet("ELnonredundant");
			
			String tmpdir = "";
			
			try {
				tmpdir = WordnetTmpdirManager.makeTmpdir();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WordNetQuery.INSTANCE.setDict(tmpdir);
			
			
			
		// Explanation 
			
		String justifications1="EquivalentClasses(<http://purl.obolibrary.org/obo/GO_0010551> <http://purl.obolibrary.org/obo/GO_1900406>)" + 
				"EquivalentClasses(<http://purl.obolibrary.org/obo/GO_1901718> <http://purl.obolibrary.org/obo/GO_0010551>)";
		String conclusion1 = "SubClassOf(<http://purl.obolibrary.org/obo/GO_1900406> <http://purl.obolibrary.org/obo/GO_1901718>)";
		
		String ontology1 = "/Users/marvin/work/ki-ulm-repository/students/hiwis/Tanja-Perleth/resources/ontologies/bioportal/OMIT.owl";			
		generateExplanation(justifications1,conclusion1, ontology1);
		
	}

	
}
