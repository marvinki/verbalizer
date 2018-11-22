package org.semanticweb.cogExp.examples;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class StudyExplanationSelection {
	
	static String ontologypath = "C:/Users/Tanja/Desktop/HIWI-KI/svn/Tanja-Perleth/resources/ontologies/bioportal/";
	// static String ontologypath = "/Users/marvin/work/ki-ulm-repository/students/hiwis/Tanja-Perleth/resources/ontologies/bioportal-beautified/";
	
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
				
				System.out.println("Conclusion " + conclusion1axiom.toString().replaceAll("  ", " "));
				System.out.println("Justifications " + justifications1axioms.toString().replaceAll("  ", " "));
				
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
						String result = justHTML5.replaceAll("  ", " ");
						// System.out.println(justHTML5);
						System.out.println(result);
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
					VerbalisationManager.INSTANCE.featuresOFF=false; // second arg below is rule names
					String result = VerbaliseTreeManager.verbaliseNL(gentzenTree3_b, false, true,null).replace("<font>&nbsp;</font>"
							," ").replace("<font color=blue>","<span style=\"color: blue\">").replace("</font>","</span>");
					
					System.out.println(result.replaceAll("  ", " "));
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
		
		// for explanation with two steps
		
		/* OMIT 2-STEP EXPLANATION: Rejected for too long concept names
		String justifications1="EquivalentClasses(<http://purl.obolibrary.org/obo/GO_0010551> <http://purl.obolibrary.org/obo/GO_1900406>)" + 
		"EquivalentClasses(<http://purl.obolibrary.org/obo/GO_1901718> <http://purl.obolibrary.org/obo/GO_0010551>)";
		String conclusion1 = "SubClassOf(<http://purl.obolibrary.org/obo/GO_1900406> <http://purl.obolibrary.org/obo/GO_1901718>)";

		String ontology1 = ontologypath + "OMIT.owl";

		generateExplanation(justifications1,conclusion1, ontology1);	
		*/	
		
		
		// OMIT 2-STEP EXPLANATION: Rejected for erroneous modeling
		/*
		String justifications2="EquivalentClasses(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Arm_dystonia_disability_scale> ObjectSomeValuesFrom(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#is_a> <http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Clinical_rating_scale>))" + 
		"EquivalentClasses(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Hamilton_depression_scale> ObjectSomeValuesFrom(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#is_a> <http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Clinical_rating_scale>))";
		String conclusion2 = "SubClassOf(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Hamilton_depression_scale> <http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Arm_dystonia_disability_scale>)";

		String ontology2 = ontologypath + "PDON.owl";

		generateExplanation(justifications2,conclusion2, ontology2);	
		*/
		
		// PDON 2-STEP EXPLANATION:  Rejected for erroneous modeling
		/* 
		String justifications2_1= "EquivalentClasses(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Polysomnography> ObjectSomeValuesFrom(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#part_of> <http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Evaluation_of_Parkinson_s_disease>) )"
			+ "EquivalentClasses(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Multidimensional_fatigue_inventory> ObjectSomeValuesFrom(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#part_of> <http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Evaluation_of_Parkinson_s_disease>) )";
		String conclusion2_1 = "SubClassOf(<http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Multidimensional_fatigue_inventory> <http://www.semanticweb.org/ontologies/2011/1/Ontology1296772722296.owl#Polysomnography>)";
		String ontology2_1 = ontologypath + "PDON.owl";
		generateExplanation(justifications2_1,conclusion2_1, ontology2_1);
		*/
		
		
		
			
		// DRON
		/* Is good, but takes very long
		String justifications2_1= "EquivalentClasses(<http://purl.obolibrary.org/obo/DRON_00000911> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/DRON_00000022> ObjectSomeValuesFrom(<http://www.obofoundry.org/ro/ro.owl#has_proper_part> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/OBI_0000576> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000053> <http://purl.obolibrary.org/obo/DRON_00000028>) ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000071> <http://purl.obolibrary.org/obo/CHEBI_52010>)))) )"
			+ "SubClassOf(<http://purl.obolibrary.org/obo/DRON_00024755> ObjectSomeValuesFrom(<http://www.obofoundry.org/ro/ro.owl#has_proper_part> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/OBI_0000576> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000053> <http://purl.obolibrary.org/obo/DRON_00000028>) ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000071> <http://purl.obolibrary.org/obo/CHEBI_52010>))))"
			+ "SubClassOf(<http://purl.obolibrary.org/obo/DRON_00024755> <http://purl.obolibrary.org/obo/DRON_00000022>)";
		String ontology2_1 = ontologypath + "DRON.owl";
		String conclusion2_1 = "SubClassOf(<http://purl.obolibrary.org/obo/DRON_00024755> <http://purl.obolibrary.org/obo/DRON_00000911>)";
		generateExplanation(justifications2_1,conclusion2_1, ontology2_1);
		*/
		
		String justifications2_1= "EquivalentClasses(<http://sweet.jpl.nasa.gov/2.3/phenSystem.owl#Decrease> <http://sweet.jpl.nasa.gov/2.3/phenSystem.owl#Drop> )"
					+ "EquivalentClasses(<http://sweet.jpl.nasa.gov/2.3/phenSystem.owl#Decline> <http://sweet.jpl.nasa.gov/2.3/phenSystem.owl#Decrease> )";
			String ontology2_1 = ontologypath + "MINERAL.owl";
			String conclusion2_1 = "SubClassOf(<http://sweet.jpl.nasa.gov/2.3/phenSystem.owl#Decline> <http://sweet.jpl.nasa.gov/2.3/phenSystem.owl#Drop> )";
			generateExplanation(justifications2_1,conclusion2_1, ontology2_1);
		
			
			String justifications2_2= "SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0005876> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0002202> <http://purl.obolibrary.org/obo/UBERON_0004874>))"
									+ "ObjectPropertyDomain(<http://purl.obolibrary.org/obo/RO_0002202> <http://purl.obolibrary.org/obo/BFO_0000004>)";
			String ontology2_2 = ontologypath + "CL.owl";
			String conclusion2_2 = "SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0005876> <http://purl.obolibrary.org/obo/BFO_0000004>)";
			generateExplanation(justifications2_2,conclusion2_2, ontology2_2);
			
			String justifications2_3= "EquivalentClasses(<http://www.flyglycodb.org/ontologies/2015/enzyme> ObjectSomeValuesFrom(<http://www.flyglycodb.org/ontologies/2015/FGDBOWL_0008> <http://www.flyglycodb.org/ontologies/2015/Substrate>) )"
									+ "SubClassOf(<http://www.flyglycodb.org/ontologies/2015/glycosidase> <http://www.flyglycodb.org/ontologies/2015/enzyme>)"
									+ "EquivalentClasses(<http://www.flyglycodb.org/ontologies/2015/Protein> ObjectSomeValuesFrom(<http://www.flyglycodb.org/ontologies/2015/FGDBOWL_0008> <http://www.flyglycodb.org/ontologies/2015/Substrate>) )";
			String ontology2_3 = ontologypath + "FLYGLYCODB.owl";
			String conclusion2_3 = "SubClassOf(<http://www.flyglycodb.org/ontologies/2015/glycosidase> <http://www.flyglycodb.org/ontologies/2015/Protein>)";
			generateExplanation(justifications2_3,conclusion2_3, ontology2_3);
			
			String justifications2_4 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Vomer> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#Nose>))"
										+ "EquivalentClasses(<http://www.co-ode.org/ontologies/galen#BoneOfNose> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#Bone> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#Nose>)) )"
										+ "SubClassOf(<http://www.co-ode.org/ontologies/galen#Vomer> <http://www.co-ode.org/ontologies/galen#Bone>)"; // corrected
										// + "SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDBone> <http://www.co-ode.org/ontologies/galen#Bone>)";
			String ontology2_4 = ontologypath + "GALEN.owl";
			String conclusion2_4 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Vomer> <http://www.co-ode.org/ontologies/galen#BoneOfNose> )";
			generateExplanation(justifications2_4,conclusion2_4, ontology2_4);
			
		// for explanation with three steps
		// OMIT 3-STEP EXPLANATION: Rejected for too long concept names
		/*
		String justifications3="EquivalentClasses(<http://purl.obolibrary.org/obo/GO_0035949> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/negatively_regulates> <http://purl.obolibrary.org/obo/GO_0006366>))" + 
		"EquivalentClasses(<http://purl.obolibrary.org/obo/GO_0072365> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/negatively_regulates> <http://purl.obolibrary.org/obo/GO_0006366>))" +
		"SubClassOf(<http://purl.obolibrary.org/obo/GO_1902353> <http://purl.obolibrary.org/obo/GO_1900465>)"+
		"SubClassOf(<http://purl.obolibrary.org/obo/GO_1900465> <http://purl.obolibrary.org/obo/GO_0072365>)";
		String conclusion3 = "SubClassOf(<http://purl.obolibrary.org/obo/GO_1902353> <http://purl.obolibrary.org/obo/GO_0035949>)";
		String ontology3 = ontologypath + "OMIT.owl";	
	
		generateExplanation(justifications3,conclusion3, ontology3);	
		*/
		
		// FOODON_03411380: vegetable corn
		// FOODON_03411454: popcorn
		// FOODON_03411151: white popcorn
		// FOODON_03411232: corn
		// NCBITaxon_4577: maize
		
		// Original variant
		/*
		String justifications3_2= "EquivalentClasses(<http://purl.obolibrary.org/obo/FOODON_03411380> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/FOODON_00001303> <http://purl.obolibrary.org/obo/NCBITaxon_4577>) )"
				+ "EquivalentClasses(<http://purl.obolibrary.org/obo/FOODON_03411232> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/FOODON_00001303> <http://purl.obolibrary.org/obo/NCBITaxon_4577>) )"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411454> <http://purl.obolibrary.org/obo/FOODON_03411232>)"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411151> <http://purl.obolibrary.org/obo/FOODON_03411454>)";
		String conclusion3_2 = "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411151> <http://purl.obolibrary.org/obo/FOODON_03411380>)";
		String ontology3_2 = ontologypath + "FOODON.owl";
		
		generateExplanation(justifications3_2,conclusion3_2, ontology3_2);
		*/
		
		
		String justifications3_2= 
				  "EquivalentClasses(<http://purl.obolibrary.org/obo/FOODON_03411380> ObjectIntersectionOf(ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/NCBITaxon_4577>) ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/R0_0009004> <http://purl.obolibrary.org/obo/NCBITaxon_9606>)))"
				+ "EquivalentClasses(<http://purl.obolibrary.org/obo/FOODON_03411232> ObjectIntersectionOf(ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/NCBITaxon_4577>) ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/R0_0009004> <http://purl.obolibrary.org/obo/NCBITaxon_9606>)))"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411454> <http://purl.obolibrary.org/obo/FOODON_03411232>)"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411151> <http://purl.obolibrary.org/obo/FOODON_03411454>)";
		String conclusion3_2 = "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411151> <http://purl.obolibrary.org/obo/FOODON_03411380>)";
		String ontology3_2 = ontologypath + "FOODON.owl";
		
		generateExplanation(justifications3_2,conclusion3_2, ontology3_2);
		
		String justifications3_3= 
				  "SubClassOf(<http://purl.obolibrary.org/obo/VO_0003082> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0000086> <http://purl.obolibrary.org/obo/VO_0000173>))"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0000566> <http://purl.obolibrary.org/obo/VO_0000165>)"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0000763> <http://purl.obolibrary.org/obo/VO_0000566>)"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0000165> <http://purl.obolibrary.org/obo/VO_0000001>)"
				+ "EquivalentClasses(<http://purl.obolibrary.org/obo/VO_0000367> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/VO_0000001> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0000086> <http://purl.obolibrary.org/obo/VO_0000173>)) )"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0003082> <http://purl.obolibrary.org/obo/VO_0000763>)";
		String conclusion3_3 = "SubClassOf(<http://purl.obolibrary.org/obo/VO_0003082> <http://purl.obolibrary.org/obo/VO_0000367>)";
		String ontology3_3 = ontologypath + "VO.owl";
		
		generateExplanation(justifications3_3,conclusion3_3, ontology3_3);
		
		// for explanation with four steps
		// NCRO 4-STEP EXPLANATION: Rejected for too long concept names
		/*
		String justifications4_1="EquivalentClasses(<http://purl.obolibrary.org/obo/NCRO_0000009> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/NCRO_0004019> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0002160> <http://purl.obolibrary.org/obo/NCBITaxon_40674>)))" + 
		"SubClassOf(<http://purl.obolibrary.org/NCRO_MI0027985> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0002160> <http://purl.obolibrary.org/obo/NCBITaxon_9615>))" +
		"SubClassOf(<http://purl.obolibrary.org/NCRO_MIPF0000018> <http://purl.obolibrary.org/obo/NCRO_0004019>)"+
		"SubClassOf(<http://purl.obolibrary.org/NCRO_MI0027985> <http://purl.obolibrary.org/NCRO_MIPF0000018>)"+
		"SubClassOf(<http://purl.obolibrary.org/obo/NCBITaxon_9615> <http://purl.obolibrary.org/obo/NCBITaxon_40674>)";
		String conclusion4_1 = "SubClassOf(<http://purl.obolibrary.org/NCRO_MI0027985> <http://purl.obolibrary.org/obo/NCRO_0000009>)";
		String ontology4_1 = ontologypath +  "NCRO.owl"; 
	    
		generateExplanation(justifications4_1,conclusion4_1, ontology4_1);	
		*/
		
		// for explanation with four steps
		// NCRO 4-STEP EXPLANATION: Rejected for too long concept names
		/*
		String justifications5="EquivalentClasses(<http://purl.obolibrary.org/obo/NCRO_0000009> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/NCRO_0004019> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0002160> <http://purl.obolibrary.org/obo/NCBITaxon_40674>)) )" + 
				"SubClassOf(<http://purl.obolibrary.org/NCRO_MIMAT0017880> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0002160> <http://purl.obolibrary.org/obo/NCBITaxon_10116>))" +
				"SubClassOf(<http://purl.obolibrary.org/NCRO_MIMAT0017880> <http://purl.obolibrary.org/obo/NCRO_0004001>)"+
				"SubClassOf(<http://purl.obolibrary.org/obo/NCRO_0004001> <http://purl.obolibrary.org/obo/NCRO_0004019>)"+
				"SubClassOf(<http://purl.obolibrary.org/obo/NCBITaxon_10116> <http://purl.obolibrary.org/obo/NCBITaxon_40674>)";
		String conclusion5 = "SubClassOf(<http://purl.obolibrary.org/NCRO_MIMAT0017880> <http://purl.obolibrary.org/obo/NCRO_0000009>)";
		String ontology5 = ontologypath + "NCRO.owl";
		
		generateExplanation(justifications5,conclusion5, ontology5);
		*/
		
		
		
	}
	
	

}
