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
	
	// static String ontologypath = "C:/Users/Tanja/Desktop/HIWI-KI/svn/Tanja-Perleth/resources/ontologies/bioportal/";
	static String ontologypath = "/Users/marvin/work/ki-ulm-repository/students/hiwis/Tanja-Perleth/resources/ontologies/bioportal-beautified/";
	
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
					String result = VerbaliseTreeManager.verbaliseNL(gentzenTree3_b, true, true,null).replace("<font>&nbsp;</font>"
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
			
			/*
			String justifications2_3= "EquivalentClasses(<http://www.flyglycodb.org/ontologies/2015/enzyme> ObjectSomeValuesFrom(<http://www.flyglycodb.org/ontologies/2015/FGDBOWL_0008> <http://www.flyglycodb.org/ontologies/2015/Substrate>) )"
									+ "SubClassOf(<http://www.flyglycodb.org/ontologies/2015/glycosidase> <http://www.flyglycodb.org/ontologies/2015/enzyme>)"
									+ "EquivalentClasses(<http://www.flyglycodb.org/ontologies/2015/Protein> ObjectSomeValuesFrom(<http://www.flyglycodb.org/ontologies/2015/FGDBOWL_0008> <http://www.flyglycodb.org/ontologies/2015/Substrate>) )";
			String ontology2_3 = ontologypath + "FLYGLYCODB.owl";
			String conclusion2_3 = "SubClassOf(<http://www.flyglycodb.org/ontologies/2015/glycosidase> <http://www.flyglycodb.org/ontologies/2015/Protein>)";
			generateExplanation(justifications2_3,conclusion2_3, ontology2_3);
			*/
			
			String justifications2_3 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Vomer> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#Nose>))"
										+ "EquivalentClasses(<http://www.co-ode.org/ontologies/galen#BoneOfNose> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#Bone> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#Nose>)) )"
										+ "SubClassOf(<http://www.co-ode.org/ontologies/galen#Vomer> <http://www.co-ode.org/ontologies/galen#Bone>)"; // corrected
										// + "SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDBone> <http://www.co-ode.org/ontologies/galen#Bone>)";
			String ontology2_3 = ontologypath + "GALEN.owl";
			String conclusion2_3 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Vomer> <http://www.co-ode.org/ontologies/galen#BoneOfNose> )";
			generateExplanation(justifications2_3,conclusion2_3, ontology2_3);
			
		// for explanation with three steps
		
		String justifications3_1= 
				  "EquivalentClasses(<http://purl.obolibrary.org/obo/FOODON_03411380> ObjectIntersectionOf(ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/NCBITaxon_4577>) ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/R0_0009004> <http://purl.obolibrary.org/obo/NCBITaxon_9606>)))"
				+ "EquivalentClasses(<http://purl.obolibrary.org/obo/FOODON_03411232> ObjectIntersectionOf(ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/NCBITaxon_4577>) ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/R0_0009004> <http://purl.obolibrary.org/obo/NCBITaxon_9606>)))"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411454> <http://purl.obolibrary.org/obo/FOODON_03411232>)"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411151> <http://purl.obolibrary.org/obo/FOODON_03411454>)";
		String conclusion3_1 = "SubClassOf(<http://purl.obolibrary.org/obo/FOODON_03411151> <http://purl.obolibrary.org/obo/FOODON_03411380>)";
		String ontology3_1 = ontologypath + "FOODON.owl";
		
		generateExplanation(justifications3_1,conclusion3_1, ontology3_1);
		
		String justifications3_1a= "SubClassOf(<http://purl.obolibrary.org/obo/VO_0003082> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0000086> <http://purl.obolibrary.org/obo/VO_0000173>))"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0000566> <http://purl.obolibrary.org/obo/VO_0000165>)"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0000763> <http://purl.obolibrary.org/obo/VO_0000566>)"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0000165> <http://purl.obolibrary.org/obo/VO_0000001>)"
				+ "EquivalentClasses(<http://purl.obolibrary.org/obo/VO_0000367> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/VO_0000001> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0000086> <http://purl.obolibrary.org/obo/VO_0000173>)) )"
				+ "SubClassOf(<http://purl.obolibrary.org/obo/VO_0003082> <http://purl.obolibrary.org/obo/VO_0000763>)";
				 
		String conclusion3_1a = "SubClassOf(<http://purl.obolibrary.org/obo/VO_0003082> <http://purl.obolibrary.org/obo/VO_0000367>)";
		String ontology3_1a = ontologypath + "VO.owl";
		
		generateExplanation(justifications3_1a,conclusion3_1a, ontology3_1a);
	
		
		String justifications3_2= "ObjectPropertyDomain(<http://purl.obolibrary.org/obo/RO_0002202> <http://purl.obolibrary.org/obo/BFO_0000004>)"
								+ "SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0001485> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0002202> <http://purl.obolibrary.org/obo/UBERON_0006256>))"
								+ "SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0011088> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/UBERON_0001485>))"
								+ "SubObjectPropertyOf(ObjectPropertyChain( <http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/RO_0002202> ) <http://purl.obolibrary.org/obo/RO_0002202>)";
		String conclusion3_2 = "SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0011088> <http://purl.obolibrary.org/obo/BFO_0000004>  )";
		String ontology3_2 = ontologypath + "CL.owl";
		generateExplanation(justifications3_2,conclusion3_2, ontology3_2);


		// Uses sub object property of
		String justifications3_3= "EquivalentClasses(<http://www.co-ode.org/ontologies/galen#CranialFontanelle> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#Fontanelle> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#IsDivisionOf> <http://www.co-ode.org/ontologies/galen#Cranium>)) )"
							+ "SubClassOf(<http://www.co-ode.org/ontologies/galen#PosteriorFontanelle> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#Cranium>))"
							+ "SubClassOf(<http://www.co-ode.org/ontologies/galen#PosteriorFontanelle> <http://www.co-ode.org/ontologies/galen#Fontanelle>)"
							+ "SubObjectPropertyOf(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#IsDivisionOf>)";
		String conclusion3_3 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#PosteriorFontanelle> <http://www.co-ode.org/ontologies/galen#CranialFontanelle>)";
		String ontology3_3 = ontologypath + "GALEN.owl";
		generateExplanation(justifications3_3,conclusion3_3, ontology3_3);


		String justifications3_4= "SubClassOf(<http://scai.fraunhofer.de/AlzheimerOntology#drug_used_in_treatment> <http://scai.fraunhofer.de/NDDUO#drug>)"
	    + "SubClassOf(<http://scai.fraunhofer.de/AlzheimerOntology#nortriptyline> <http://scai.fraunhofer.de/AlzheimerOntology#drug_used_in_treatment>)"
		+ "EquivalentClasses(<http://scai.fraunhofer.de/AlzheimerOntology#drug_used_against_depression> ObjectIntersectionOf(<http://scai.fraunhofer.de/NDDUO#Molecular_entities> ObjectSomeValuesFrom(<http://scai.fraunhofer.de/NDDUO#has_role> <http://scai.fraunhofer.de/NDDUO#antidepressant>)) )"
		+ "SubClassOf(<http://scai.fraunhofer.de/AlzheimerOntology#nortriptyline> ObjectSomeValuesFrom(<http://scai.fraunhofer.de/NDDUO#has_role> <http://scai.fraunhofer.de/NDDUO#antidepressant>))"
		+ "SubClassOf(<http://scai.fraunhofer.de/NDDUO#drug> <http://scai.fraunhofer.de/NDDUO#Molecular_entities>)";
		String conclusion3_4 = "SubClassOf(<http://scai.fraunhofer.de/AlzheimerOntology#nortriptyline> <http://scai.fraunhofer.de/AlzheimerOntology#drug_used_against_depression> )";
		String ontology3_4 = ontologypath + "ADO.owl";
		generateExplanation(justifications3_4,conclusion3_4, ontology3_4);


		
		// for explanation with four steps
		String justifications4_1= "SubClassOf(<http://sweet.jpl.nasa.gov/2.3/stateTime.owl#Period> ObjectSomeValuesFrom(<http://sweet.jpl.nasa.gov/2.3/relaTime.owl#temporalPartOf> <http://sweet.jpl.nasa.gov/2.3/stateTime.owl#Era>))"
		+ "SubObjectPropertyOf(<http://sweet.jpl.nasa.gov/2.3/relaMath.owl#subsetOf> <http://sweet.jpl.nasa.gov/2.3/relaMath.owl#setRelation>)"
		+ "SubObjectPropertyOf(<http://sweet.jpl.nasa.gov/2.3/relaTime.owl#temporalPartOf> <http://sweet.jpl.nasa.gov/2.3/relaMath.owl#subsetOf>)"
		+ "ObjectPropertyDomain(<http://sweet.jpl.nasa.gov/2.3/relaMath.owl#setRelation> <http://sweet.jpl.nasa.gov/2.3/reprMath.owl#Set>)";
String conclusion4_1 = "SubClassOf(<http://sweet.jpl.nasa.gov/2.3/stateTime.owl#Period> <http://sweet.jpl.nasa.gov/2.3/reprMath.owl#Set>)";
String ontology4_1 = ontologypath + "MATR.owl";
generateExplanation(justifications4_1,conclusion4_1, ontology4_1);


String justifications4_2= "EquivalentClasses(<http://www.co-ode.org/ontologies/galen#BursaOfKnee> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#Bursa> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#IsDivisionOf> <http://www.co-ode.org/ontologies/galen#Knee>)) )"
		+ "SubClassOf(<http://www.co-ode.org/ontologies/galen#PoplitealBursa> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#Knee>))"
		+ "SubClassOf(<http://www.co-ode.org/ontologies/galen#PoplitealBursa> <http://www.co-ode.org/ontologies/galen#Bursa>)"
		+ "SubObjectPropertyOf(<http://www.co-ode.org/ontologies/galen#isStructuralComponentOf> <http://www.co-ode.org/ontologies/galen#IsDivisionOf>)";
String conclusion4_2 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#PoplitealBursa> <http://www.co-ode.org/ontologies/galen#BursaOfKnee>)";
String ontology4_2 = ontologypath + "GALEN.owl";
generateExplanation(justifications4_2,conclusion4_2, ontology4_2);

String justifications4_3= "EquivalentClasses(<http://www.co-ode.org/ontologies/galen#TendonPathology> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#PathologicalPhenomenon> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#LocativeAttribute> <http://www.co-ode.org/ontologies/galen#Tendon>)) )"
						+ "EquivalentClasses(<http://www.co-ode.org/ontologies/galen#TendonInjury> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#PathologicalPhenomenon> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#LocativeAttribute> <http://www.co-ode.org/ontologies/galen#Tendon>) ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#hasUniqueAssociatedProcess> <http://www.co-ode.org/ontologies/galen#TraumaticProcess>)) )";
	String conclusion4_3 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#TendonInjury> <http://www.co-ode.org/ontologies/galen#TendonPathology> )";
	String ontology4_3 = ontologypath + "GALEN.owl";
	generateExplanation(justifications4_3,conclusion4_3, ontology4_3);

	String justifications4_4= "ObjectPropertyDomain(<http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000932> <http://purl.org/obo/owl/SBO#SBO_0000064>)"
		    +"SubClassOf(<http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000113> ObjectSomeValuesFrom(<http://purl.obofoundry.org/obo/IAO_0000222> <http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000879>))"
			+"EquivalentClasses(<http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000089> ObjectIntersectionOf(<http://purl.org/obo/owl/SBO#SBO_0000064> ObjectSomeValuesFrom(<http://purl.obofoundry.org/obo/IAO_0000222> <http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000879>)) )"
			+"SubClassOf(<http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000113> ObjectSomeValuesFrom(<http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000932> ObjectIntersectionOf(<http://www.sarala.bioeng.auckland.ac.nz/CellMLOntology.owl#Variable> ObjectSomeValuesFrom(<http://purl.obofoundry.org/obo/IAO_0000136> ObjectIntersectionOf(<http://purl.org/obo/owl/PATO#PATO_0001035> ObjectSomeValuesFrom(<http://purl.org/obo/owl/OBO_REL#quality_of> <http://onto.eva.mpg.de/ontologies/gfo-bio.owl#Anatomical_part>))))))";
String conclusion4_4 = "SubClassOf(<http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000113> <http://scai.fraunhofer.de/HuPSON#SCAIVPH_00000089> )";
String ontology4_4 = ontologypath + "HUPSON.owl";
generateExplanation(justifications4_4,conclusion4_4, ontology4_4);

//for explanation with five steps
String justifications5_1= "EquivalentClasses(<http://www.co-ode.org/ontologies/galen#SurgicalBinding> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#Binding> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#playsClinicalRole> <http://www.co-ode.org/ontologies/galen#SurgicalRole>)) )"
	+ "SubClassOf(<http://www.co-ode.org/ontologies/galen#Binding> <http://www.co-ode.org/ontologies/galen#SolidFasteningProcess>)"
	+	"EquivalentClasses(<http://www.co-ode.org/ontologies/galen#SurgicalFasteningProcess> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#SolidFasteningProcess> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#playsClinicalRole> <http://www.co-ode.org/ontologies/galen#SurgicalRole>)) )";
String conclusion5_1 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#SurgicalBinding> <http://www.co-ode.org/ontologies/galen#SurgicalFasteningProcess> )";
String ontology5_1 = ontologypath + "GALEN.owl";
generateExplanation(justifications5_1,conclusion5_1, ontology5_1);


String justifications5_2= "SubClassOf(<http://www.co-ode.org/ontologies/galen#TragusOfPinna> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isSolidDivisionOf> <http://www.co-ode.org/ontologies/galen#Pinna>))"
		+"SubClassOf(<http://www.co-ode.org/ontologies/galen#HeadSurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#SurfaceBodyPart>)"
		+	"EquivalentClasses(<http://www.co-ode.org/ontologies/galen#ComponentOfAuricle> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#BodyStructure> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#IsDivisionOf> <http://www.co-ode.org/ontologies/galen#Pinna>)) )"	
		+	"SubObjectPropertyOf(<http://www.co-ode.org/ontologies/galen#isSolidDivisionOf> <http://www.co-ode.org/ontologies/galen#isSolidRegionOf>)"
		+	"SubClassOf(<http://www.co-ode.org/ontologies/galen#BodyPart> <http://www.co-ode.org/ontologies/galen#BodyStructure>)"
		+	"SubClassOf(<http://www.co-ode.org/ontologies/galen#TragusOfPinna> <http://www.co-ode.org/ontologies/galen#HeadSurfaceBodyPart>)"
		+	"SubClassOf(<http://www.co-ode.org/ontologies/galen#SurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#BodyPart>)";	
		String conclusion5_2 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#TragusOfPinna> <http://www.co-ode.org/ontologies/galen#ComponentOfAuricle>)";
		String ontology5_2 = ontologypath + "GALEN.owl";
		generateExplanation(justifications5_2,conclusion5_2, ontology5_2);	
		
	String justifications5_3= "SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0004908> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/RO_0002202> <http://purl.obolibrary.org/obo/UBERON_0001041>))"
		+"SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0001043> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/UBERON_0004908>))"
		+	"ObjectPropertyDomain(<http://purl.obolibrary.org/obo/RO_0002202> <http://purl.obolibrary.org/obo/BFO_0000004>)"
		+	"EquivalentClasses(<http://purl.obolibrary.org/obo/UBERON_0001096> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/UBERON_0000060> ObjectSomeValuesFrom(<http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/UBERON_0001043>)) )"
		+	"SubObjectPropertyOf(ObjectPropertyChain( <http://purl.obolibrary.org/obo/BFO_0000050> <http://purl.obolibrary.org/obo/RO_0002202> ) <http://purl.obolibrary.org/obo/RO_0002202>)";
			String conclusion5_3 = "SubClassOf(<http://purl.obolibrary.org/obo/UBERON_0001096> <http://purl.obolibrary.org/obo/BFO_0000004>)";
			String ontology5_3 = ontologypath + "CL.owl";
			generateExplanation(justifications5_3,conclusion5_3, ontology5_3);	
			
	String justifications5_4= "SubClassOf(<http://www.co-ode.org/ontologies/galen#RenalInfectionLesion> <http://www.co-ode.org/ontologies/galen#InfectionLesion>)"
		+"EquivalentClasses(<http://www.co-ode.org/ontologies/galen#TuberculousLesion> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#InfectionLesion> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isSpecificImmediateConsequenceOf> <http://www.co-ode.org/ontologies/galen#MycobacteriumTuberculosisHominis>)) )"
			+"EquivalentClasses(<http://www.co-ode.org/ontologies/galen#RenalTuberculosis> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#RenalInfectionLesion> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isSpecificImmediateConsequenceOf> <http://www.co-ode.org/ontologies/galen#MycobacteriumTuberculosisHominis>)) )";
	String conclusion5_4 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#RenalTuberculosis> <http://www.co-ode.org/ontologies/galen#TuberculousLesion>)";
	String ontology5_4 = ontologypath + "GALEN.owl";
	generateExplanation(justifications5_4,conclusion5_4, ontology5_4);		
	
	String justifications5_5= "EquivalentClasses(<http://www.drugtargetontology.org/dto/DTO_00000017> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/CHEBI_16670> <http://www.drugtargetontology.org/dto/DTO_00000001>) )"
		+"SubClassOf(<http://purl.obolibrary.org/obo/CHEBI_7542> <http://purl.obolibrary.org/obo/CHEBI_16670>)"
			+"EquivalentClasses(<http://www.drugtargetontology.org/dto/DTO_00000001> ObjectIntersectionOf(<http://purl.obolibrary.org/obo/CHEBI_23367> ObjectSomeValuesFrom(<http://www.drugtargetontology.org/dto/DTO_90100125> <http://www.drugtargetontology.org/dto/DTO_02300001>)) )"
			+"SubClassOf(<http://purl.obolibrary.org/obo/CHEBI_16670> <http://purl.obolibrary.org/obo/CHEBI_23367>)"
			+"SubClassOf(<http://purl.obolibrary.org/obo/CHEBI_7542> ObjectSomeValuesFrom(<http://www.drugtargetontology.org/dto/DTO_90100125> <http://www.drugtargetontology.org/dto/DTO_02300001>))";
		String conclusion5_5 = "SubClassOf(<http://purl.obolibrary.org/obo/CHEBI_7542> <http://www.drugtargetontology.org/dto/DTO_00000017>)";
		String ontology5_5 = ontologypath + "DTO.owl";
		generateExplanation(justifications5_5,conclusion5_5, ontology5_5);	

		
	}
	
	

}
