package org.semanticweb.cogExp.examples;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
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
import org.semanticweb.cogExp.coverageEvaluator.CoverageStoreEvaluatorCompressionDB;

/*
 * Corpus generated from database. 
 * Explanations were selected at random from a subset of explanations that fulfilled the following criteria:
 * - It consists of at least three verbalized steps, at most eight. 
 * - The explanation does not rely on the unsatisfiability of a class that, to the contrary, commonly has instances in real life (e.g. <TODO!>)
 * At current, the collection is found in the file marvin_work_ulm/notes/howto-mysql
 * Dumps are in /Users/marvin/work/workspace/justifications/originaltones-dumps/dumps-for-ecai
 */



public class BioExperimentExplanations {
	
	public static ProofTree constructProoftree(String conclusion, String justifications, String ontologyfilestring){
		OWLOntologyManager manager = CoverageStoreEvaluatorCompressionDB.getImportKnowledgeableOntologyManger();
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

		OWLAxiom conclusion1axiom = CoverageStoreEvaluatorCompressionDB.parseAxiomFunctional(conclusion,ontology);
		Set<OWLAxiom> justifications1axioms = CoverageStoreEvaluatorCompressionDB.parseAxiomsFunctional(justifications,ontology);
		
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
		
	
		// Explanation 31
		/*
		String justifications1=
				"DisjointClasses(<http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Ictal_Relationship> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Phase> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Seizure_Wave_Property> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Waveform>)" +
				"SubClassOf(<http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Transient> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Seizure_Wave_Property>)"+
				"SubClassOf(<http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Normal_Wave_Forms> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Waveform>)"+
				"SubClassOf(<http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Normal_Wave_Forms> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Transient>)"+
				"SubClassOf(<http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Clipping> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Normal_Wave_Forms>)";
		String conclusion1 = "SubClassOf(<http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Clipping> <http://www.semanticweb.org/rjyy/ontologies/2015/5/ESSO#Asymmetrical>)";;



		ProofTree prooftree1 = constructProoftree(conclusion1,justifications1,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/ESSO.owl");
		ProofTree prooftree1_b = constructProoftree(conclusion1,justifications1,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/ESSO.owl");

		
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree1, rulesNonred, 840000, 400000);
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree1_b, rules, 840000, 400000);
		try {
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree1, prooftree1.getRoot().getId());
		// node.getId());
			GentzenTree gentzenTree1= prooftree1.toGentzen();
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree1_b, prooftree1_b.getRoot().getId());
			// node.getId());
				GentzenTree gentzenTree1_b= prooftree1_b.toGentzen();	
			System.out.println("LISTING FOR EXPLANATION 1  :");
			System.out.println(VerbaliseTreeManager.listOutput(gentzenTree1));
			System.out.println("LONG EXPLANATION 1:");
			
			VerbalisationManager.INSTANCE.featuresOFF=true;
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree1, false, true,true,null).replace("<font>&nbsp;</font>"
					," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
			System.out.println("SHORT EXPLANATION 1:");
			VerbalisationManager.INSTANCE.featuresOFF=false;
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree1_b, false, true,false,null).replace("<font>&nbsp;</font>"
					," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
			System.out.println("no. long steps: " + gentzenTree1.computePresentationOrder().size());
			System.out.println("no. short steps: " + gentzenTree1_b.computePresentationOrder().size());
			
		} catch (Exception e) {
			System.out.println("Error while eliminating irrelevant parts");
			e.printStackTrace();
		}
		
	*/

		// Explanation xxx
			
				/*
				String justifications2=
						"DisjointClasses(<http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000181> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000312> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000314> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000317> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000331> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000332> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000335> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000336> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_555642> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_726447> <http://purl.obolibrary.org/obo/IAO_0000013>)"  + 
						"SubClassOf(<http://purl.obolibrary.org/obo/OBI_0600013> ObjectSomeValuesFrom(<http://www.obofoundry.org/ro/ro.owl#part_of> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000323>)) " + 
						"SubClassOf(<http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000328> ObjectSomeValuesFrom(<http://www.obofoundry.org/ro/ro.owl#has_part> <http://purl.obolibrary.org/obo/OBI_0600013>)) " + 
						"SubClassOf(<http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000323> ObjectSomeValuesFrom(<http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000215> ObjectIntersectionOf(<http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000312> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000314> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000317> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000331>)))";
		
				String conclusion2 = "SubClassOf(<http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_000328> <http://kt.ijs.si/panovp/OntoDM#OntoDM-KDD_537902>)";



				ProofTree prooftree2 = constructProoftree(conclusion2,justifications2,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/ONTODM-KDD.owl");
				ProofTree prooftree2_b = constructProoftree(conclusion2,justifications2,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/ONTODM-KDD.owl");

				
				InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree2, rulesNonred, 8400000, 4000000);
				InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree2_b, rules, 8400000, 4000000);
				System.out.println(" open nodes? " + prooftree2.getOpenNodes().size());
				System.out.println(" open nodes? " + prooftree2_b.getOpenNodes().size());
				try {
					org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree2, prooftree2.getRoot().getId());
				// node.getId());
					GentzenTree gentzenTree2= prooftree2.toGentzen();
					org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree2_b, prooftree2_b.getRoot().getId());
					// node.getId());
						GentzenTree gentzenTree2_b= prooftree2_b.toGentzen();	
					System.out.println("LISTING FOR EXPLANATION 2  :");
					System.out.println(VerbaliseTreeManager.listOutput(gentzenTree2));
					System.out.println("LONG EXPLANATION 2:");
					
					VerbalisationManager.INSTANCE.featuresOFF=true;
					System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree2, false, true,true,null).replace("<font>&nbsp;</font>"
							," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
					System.out.println("SHORT EXPLANATION 2:");
					VerbalisationManager.INSTANCE.featuresOFF=false;
					System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree2_b, false, true,false,null).replace("<font>&nbsp;</font>"
							," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
					System.out.println("no. long steps: " + gentzenTree2.computePresentationOrder().size());
					System.out.println("no. short steps: " + gentzenTree2_b.computePresentationOrder().size());
					
				} catch (Exception e) {
					System.out.println("Error while eliminating irrelevant parts");
					e.printStackTrace();
				}
		*/
				
				// DEBUG --- to be proven Spasme⊑SpasmeLaryngeSigne
				// DEBUG --- Trying to add EquivalentClasses(<http://www.limics.fr/ontologies/ontolurgences#SpasmeLaryngeSigne> ObjectIntersectionOf(<http://www.limics.fr/ontologies/ontolurgences#SigneFonctionnelORL> <http://www.limics.fr/ontologies/ontolurgences#Spasme>) )
				// DEBUG --- Trying to add SubClassOf(<http://www.limics.fr/ontologies/ontolurgences#Spasme> <http://www.limics.fr/ontologies/ontolurgences#SigneFonctionnelORL>)
				
			/*
				String justifications3=
						 "EquivalentClasses(<http://www.limics.fr/ontologies/ontolurgences#SpasmeLaryngeSigne> ObjectIntersectionOf(<http://www.limics.fr/ontologies/ontolurgences#SigneFonctionnelORL> <http://www.limics.fr/ontologies/ontolurgences#Spasme>) )" + 
						  "SubClassOf(<http://www.limics.fr/ontologies/ontolurgences#Spasme> <http://www.limics.fr/ontologies/ontolurgences#SigneFonctionnelORL>)";
	
			String conclusion3 = "SubClassOf( <http://www.limics.fr/ontologies/ontolurgences#Spasme> <http://www.limics.fr/ontologies/ontolurgences#SpasmeLaryngeSigne>)";



			ProofTree prooftree3 = constructProoftree(conclusion3,justifications3,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/ONTOLURGENCES.owl");
			ProofTree prooftree3_b = constructProoftree(conclusion3,justifications3,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/ONTOLURGENCES.owl");

			
			InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree3, rulesNonred, 8400000, 4000000);
			InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree3_b, rules, 8400000, 4000000);
			System.out.println(" open nodes? " + prooftree3.getOpenNodes().size());
			System.out.println(" open nodes? " + prooftree3_b.getOpenNodes().size());
			try {
				org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree3, prooftree3.getRoot().getId());
			// node.getId());
				GentzenTree gentzenTree3= prooftree3.toGentzen();
				org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree3_b, prooftree3_b.getRoot().getId());
				// node.getId());
					GentzenTree gentzenTree3_b= prooftree3_b.toGentzen();	
				System.out.println("LISTING FOR EXPLANATION 3  :");
				System.out.println(VerbaliseTreeManager.listOutput(gentzenTree3));
				
				
				System.out.println("LONG EXPLANATION 3: ");
				
				VerbalisationManager.INSTANCE.featuresOFF=true;
				System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree3, false, true,true,null).replace("<font>&nbsp;</font>"
						," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
				System.out.println("SHORT EXPLANATION 2:");
				VerbalisationManager.INSTANCE.featuresOFF=false;
				System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree3_b, false, true,false,null).replace("<font>&nbsp;</font>"
						," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
				System.out.println("no. long steps: " + gentzenTree3.computePresentationOrder().size());
				System.out.println("no. short steps: " + gentzenTree3_b.computePresentationOrder().size());
				
				
			} catch (Exception e) {
				System.out.println("Error while eliminating irrelevant parts");
				e.printStackTrace();
			}
				*/	
				
			// DEBUG --- to be proven DB05812⊑FTC_A0019825
			// DEBUG --- Trying to add SubClassOf(<http://www.drugbank.ca/drugs/DB05812> <http://schema.org/Drug>)
			// DEBUG --- Trying to add EquivalentClasses(<http://www.ebi.ac.uk/chembl/ftc/FTC_A0019825> ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> ObjectIntersectionOf(<http://purl.uniprot.org/core/Protein> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/GO_0003674> <http://purl.obolibrary.org/obo/GO_0019825>))))) )
			// DEBUG --- Trying to add SubClassOf(<http://purl.obolibrary.org/obo/GO_0005488> <http://purl.obolibrary.org/obo/GO_0003674>)
			// DEBUG --- Trying to add SubClassOf(<http://purl.uniprot.org/uniprot/P05093> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> <http://purl.obolibrary.org/obo/GO_0019825>))
			// DEBUG --- Trying to add SubClassOf(<http://purl.uniprot.org/uniprot/P05093> <http://purl.uniprot.org/core/Protein>)
			// DEBUG --- Trying to add SubClassOf(<http://www.drugbank.ca/drugs/DB05812> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> <http://purl.uniprot.org/uniprot/P05093>))
			// DEBUG --- Trying to add SubClassOf(<http://purl.obolibrary.org/obo/GO_0019825> <http://purl.obolibrary.org/obo/GO_0005488>)

			
		// String justifications4= "SubClassOf(<http://www.drugbank.ca/drugs/DB05812> <http://schema.org/Drug>)" + 
		// "EquivalentClasses(<http://www.ebi.ac.uk/chembl/ftc/FTC_A0019825> ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> ObjectIntersectionOf(<http://purl.uniprot.org/core/Protein> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/GO_0003674> <http://purl.obolibrary.org/obo/GO_0019825>))))) )" + 
		// "SubClassOf(<http://purl.obolibrary.org/obo/GO_0005488> <http://purl.obolibrary.org/obo/GO_0003674>)" +
		// "SubClassOf(<http://purl.uniprot.org/uniprot/P05093> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> <http://purl.obolibrary.org/obo/GO_0019825>))" +
		// "SubClassOf(<http://purl.uniprot.org/uniprot/P05093> <http://purl.uniprot.org/core/Protein>)	" +
		// "SubClassOf(<http://www.drugbank.ca/drugs/DB05812> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> <http://purl.uniprot.org/uniprot/P05093>))	" +
		// "SubClassOf(<http://purl.obolibrary.org/obo/GO_0019825> <http://purl.obolibrary.org/obo/GO_0005488>)";
		// 
		
		
		String justifications4= "SubClassOf(<http://www.drugbank.ca/drugs/DB05812> <http://schema.org/Drug>)" + 
		"SubClassOf(ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> ObjectIntersectionOf(<http://purl.uniprot.org/core/Protein> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/GO_0003674> <http://purl.obolibrary.org/obo/GO_0019825>))))) <http://www.ebi.ac.uk/chembl/ftc/FTC_A0019825>)" +
		"SubClassOf(<http://purl.uniprot.org/uniprot/P05093> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> <http://purl.obolibrary.org/obo/GO_0019825>))" +
		"SubClassOf(<http://purl.uniprot.org/uniprot/P05093> <http://purl.uniprot.org/core/Protein>)	" +
		"SubClassOf(<http://www.drugbank.ca/drugs/DB05812> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> <http://purl.uniprot.org/uniprot/P05093>))	" +
		"SubClassOf(<http://purl.obolibrary.org/obo/GO_0019825> <http://purl.obolibrary.org/obo/GO_0003674>)";
		
		
		// final
		// String conclusion4 = "SubClassOf(<http://www.drugbank.ca/drugs/DB05812>  <http://www.ebi.ac.uk/chembl/ftc/FTC_A0019825>)";
		
		// step 1: ok
		// String conclusion4 = "SubClassOf(<http://www.drugbank.ca/drugs/DB05812>  ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> <http://purl.uniprot.org/uniprot/P05093>)))";
		
		// step 2: ok
		// String conclusion4 = "SubClassOf(<http://www.drugbank.ca/drugs/DB05812>  ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> <http://purl.uniprot.org/core/Protein>)))";
				
		// step 3: ok
		// String conclusion4 = "SubClassOf(<http://www.drugbank.ca/drugs/DB05812>  ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> ObjectIntersectionOf(<http://purl.uniprot.org/core/Protein> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> <http://purl.obolibrary.org/obo/GO_0019825>)))))";
		
		// step 4: ok
		// String conclusion4 = "SubClassOf(<http://www.drugbank.ca/drugs/DB05812>  ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> ObjectIntersectionOf(<http://purl.uniprot.org/core/Protein> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> <http://purl.obolibrary.org/obo/GO_0005488>)))))";
		
		// step 5: ok
		// String conclusion4 = "SubClassOf(<http://www.drugbank.ca/drugs/DB05812>  ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> ObjectIntersectionOf(<http://purl.uniprot.org/core/Protein> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> <http://purl.obolibrary.org/obo/GO_0003674>)))))";
		
		// step 6: fails 
		// String conclusion4 = "SubClassOf(<http://www.drugbank.ca/drugs/DB05812>  ObjectIntersectionOf(<http://schema.org/Drug> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R4> ObjectIntersectionOf(<http://purl.uniprot.org/core/Protein> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/GO_0003674> <http://purl.obolibrary.org/obo/GO_0019825>))))))";
		
		// helper 1: ok
		// String conclusion4 = "SubClassOf(<http://purl.obolibrary.org/obo/GO_0019825> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/GO_0003674> <http://purl.obolibrary.org/obo/GO_0019825>))";
		
		// helper 2:
		String conclusion4 = "SubClassOf(<http://purl.uniprot.org/uniprot/P05093> ObjectSomeValuesFrom(<http://www.ebi.ac.uk/chembl/ftc/FTC_R2> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/GO_0003674> <http://purl.obolibrary.org/obo/GO_0019825>)))";
				
		
		 ProofTree prooftree4 = constructProoftree(conclusion4,justifications4,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/FTC.owl");
		// ProofTree prooftree4_b = constructProoftree(conclusion4,justifications4,"/Users/marvin/marvin_work_ulm/resources/ontologies/bio/FTC.owl");

		
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree4, rulesNonred, 8400000, 4000000);
		// InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree4_b, rules, 8400000, 4000000);
		System.out.println(" open nodes? " + prooftree4.getOpenNodes().size());
		// System.out.println(" open nodes? " + prooftree4_b.getOpenNodes().size());
		try {
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree4, prooftree4.getRoot().getId());
		// node.getId());
			GentzenTree gentzenTree4= prooftree4.toGentzen();
			// org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree4_b, prooftree4_b.getRoot().getId());
			// node.getId());
				// GentzenTree gentzenTree4_b= prooftree4_b.toGentzen();	
			System.out.println("LISTING FOR EXPLANATION 4  :");
			System.out.println(VerbaliseTreeManager.listOutput(gentzenTree4));
			
			/*
			System.out.println("LONG EXPLANATION 3: ");
			
			VerbalisationManager.INSTANCE.featuresOFF=true;
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree3, false, true,true,null).replace("<font>&nbsp;</font>"
					," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
			System.out.println("SHORT EXPLANATION 2:");
			VerbalisationManager.INSTANCE.featuresOFF=false;
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree3_b, false, true,false,null).replace("<font>&nbsp;</font>"
					," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>"));
			System.out.println("no. long steps: " + gentzenTree3.computePresentationOrder().size());
			System.out.println("no. short steps: " + gentzenTree3_b.computePresentationOrder().size());
			*/
			
		} catch (Exception e) {
			System.out.println("Error while eliminating irrelevant parts");
			e.printStackTrace();
		}
			
			
			} // end main

	
}
