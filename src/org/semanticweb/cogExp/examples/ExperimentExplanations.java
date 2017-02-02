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



public class ExperimentExplanations {
	
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
				System.out.println(VerbaliseTreeManager.makeUppercaseStart(VerbalisationManager.verbalise(just)) + "<br>");
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
		
		/* Difference between short and long explanation too small
	// Explanation 1
	
	String justifications1="SubClassOf(<http://www.co-ode.org/ontologies/galen#Hypogastrium> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#exactlyPaired>)) SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDSurfaceSubpart> <http://www.co-ode.org/ontologies/galen#BodyPart>) SubClassOf(<http://www.co-ode.org/ontologies/galen#Hypogastrium> <http://www.co-ode.org/ontologies/galen#NAMEDSurfaceSubpart>) SubClassOf(<http://www.co-ode.org/ontologies/galen#BodyPart> <http://www.co-ode.org/ontologies/galen#BodyStructure>) EquivalentClasses(<http://www.co-ode.org/ontologies/galen#ExactlyPairedBodyStructure> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#BodyStructure> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#exactlyPaired>)) )";
	String conclusion1 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Hypogastrium> <http://www.co-ode.org/ontologies/galen#ExactlyPairedBodyStructure>)";;
	
	
	
	
	
	ProofTree prooftree = constructProoftree(conclusion1,justifications1,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-galen1.owl");
	ProofTree prooftree_b = constructProoftree(conclusion1,justifications1,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-galen1.owl");
	
	
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, rulesNonred, 840000, 400000);
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree_b, rules, 840000, 400000);
	try {
		System.out.println("Eliminating irrelevant parts");
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, prooftree.getRoot().getId());
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree_b, prooftree_b.getRoot().getId());
				// node.getId());
		GentzenTree gentzenTree = prooftree.toGentzen();
		GentzenTree gentzenTree_b = prooftree_b.toGentzen();
		System.out.println(VerbaliseTreeManager.listOutput(gentzenTree));
		System.out.println("LONG EXPLANATION 1:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree, false, true,true,null));
		System.out.println("SHORT EXPLANATION 1:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree_b, false, true,false,null));
		
	} catch (Exception e) {
		System.out.println("Error while eliminating irrelevant parts");
		e.printStackTrace();
 		}
	
	*/

	// Explanation 2 (not used)
	
		/*
	
		String justifications2="EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#dna-binding-site> ObjectIntersectionOf(<http://www.mindswap.org/ontologies/tambis-full.owl#binding-site> ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#binds> <http://www.mindswap.org/ontologies/tambis-full.owl#dna>)) ) " + 
							   "EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#protein-part> ObjectIntersectionOf(<http://www.mindswap.org/ontologies/tambis-full.owl#macromolecule-part> ObjectIntersectionOf(ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#part-of> <http://www.mindswap.org/ontologies/tambis-full.owl#protein>) ObjectAllValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#part-of> <http://www.mindswap.org/ontologies/tambis-full.owl#protein>))) )" + 
							   "EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#binding-site> ObjectIntersectionOf(<http://www.mindswap.org/ontologies/tambis-full.owl#protein-part> ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#target-for> <http://www.mindswap.org/ontologies/tambis-full.owl#binding>)) )";
		String conclusion2 = "SubClassOf(<http://www.mindswap.org/ontologies/tambis-full.owl#dna-binding-site> <http://www.mindswap.org/ontologies/tambis-full.owl#macromolecule-part>)";;
		
		
		
		ProofTree prooftree2 = constructProoftree(conclusion2,justifications2,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
		ProofTree prooftree2_b = constructProoftree(conclusion2,justifications2,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
		
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree2, rulesNonred, 840000, 400000);
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree2_b, rules, 840000, 400000);
		try {
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree2, prooftree.getRoot().getId());
					// node.getId());
			GentzenTree gentzenTree2 = prooftree2.toGentzen();
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree2_b, prooftree2_b.getRoot().getId());
			// node.getId());
			GentzenTree gentzenTree2_b = prooftree2_b.toGentzen();
			System.out.println(VerbaliseTreeManager.listOutput(gentzenTree2));
			System.out.println();
			System.out.println("LONG EXPLANATION 2:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree2, false, true,true,null));
			System.out.println("SHORT EXPLANATION 2:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree2_b, false, true,false,null));
			
		} catch (Exception e) {
			System.out.println("Error while eliminating irrelevant parts");
			e.printStackTrace();
	 		}
		
		*/
		
		// Explanation 3
		
		String justifications3="EquivalentClasses(<http://www.opengalen.org/owl/opengalen.owl#DiameterChanging> ObjectIntersectionOf(<http://www.opengalen.org/owl/opengalen.owl#DimensionChangingProcess> ObjectSomeValuesFrom(<http://www.opengalen.org/owl/opengalen.owl#actsSpecificallyOn> <http://www.opengalen.org/owl/opengalen.owl#Diameter>)) )" + 
								"SubClassOf(<http://www.opengalen.org/owl/opengalen.owl#DimensionIncreasingProcess> <http://www.opengalen.org/owl/opengalen.owl#DimensionChangingProcess>)" + 
								"EquivalentClasses(<http://www.opengalen.org/owl/opengalen.owl#DilatatingProcess> ObjectIntersectionOf(<http://www.opengalen.org/owl/opengalen.owl#DimensionIncreasingProcess> ObjectSomeValuesFrom(<http://www.opengalen.org/owl/opengalen.owl#actsSpecificallyOn> <http://www.opengalen.org/owl/opengalen.owl#Diameter>) ObjectSomeValuesFrom(<http://www.opengalen.org/owl/opengalen.owl#hasPathologicalStatus> <http://www.opengalen.org/owl/opengalen.owl#pathological>)) )";
		String conclusion3 = "SubClassOf(<http://www.opengalen.org/owl/opengalen.owl#DilatatingProcess>  <http://www.opengalen.org/owl/opengalen.owl#DiameterChanging>)";;



		ProofTree prooftree3 = constructProoftree(conclusion3,justifications3,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-opengalen-no-propchains.owl");
		ProofTree prooftree3_b = constructProoftree(conclusion3,justifications3,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-opengalen-no-propchains.owl");

		
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree3, rulesNonred, 840000, 400000);
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree3_b, rules, 840000, 400000);
		try {
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree3, prooftree3.getRoot().getId());
		// node.getId());
			GentzenTree gentzenTree3= prooftree3.toGentzen();
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree3_b, prooftree3_b.getRoot().getId());
			// node.getId());
				GentzenTree gentzenTree3_b= prooftree3_b.toGentzen();	
			System.out.println(VerbaliseTreeManager.listOutput(gentzenTree3));
			System.out.println("LONG EXPLANATION 1:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree3, false, true,true,null));
			System.out.println("SHORT EXPLANATION 1:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree3_b, false, true,false,null));
			System.out.println("no. long steps: " + gentzenTree3.computePresentationOrder().size());
			System.out.println("no. short steps: " + gentzenTree3_b.computePresentationOrder().size());
			
		} catch (Exception e) {
			System.out.println("Error while eliminating irrelevant parts");
			e.printStackTrace();
		}
		
	
		
	
				// Explanation 4
				/* 
				String justifications4="SubClassOf(<http://ontology.dumontierlab.com/Ligase_activity> <http://ontology.dumontierlab.com/Molecular_function>)" +
					"EquivalentClasses(<http://ontology.dumontierlab.com/Ligase_activity> <http://purl.org/obo/owl/GO#GO_0016874> )" +
					"EquivalentClasses(<http://ontology.dumontierlab.com/Molecular_function> <http://purl.org/obo/owl/GO#GO_0003674> ) ";
				
				
				String conclusion4 = "SubClassOf(<http://purl.org/obo/owl/GO#GO_0016874> <http://purl.org/obo/owl/GO#GO_0003674>)";



				ProofTree prooftree4 = constructProoftree(conclusion4,justifications4,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-goslim.owl");
				ProofTree prooftree4_b = constructProoftree(conclusion4,justifications4,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-goslim.owl");

				
				
				InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree4, rulesNonred, 840000, 400000);
				InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree4_b, rulesNonred, 840000, 400000);
				try {
					org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree4, prooftree4.getRoot().getId());
				// node.getId());
					GentzenTree gentzenTree4= prooftree4.toGentzen();	
					org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree4_b, prooftree4_b.getRoot().getId());
					// node.getId());
						GentzenTree gentzenTree4_b= prooftree4_b.toGentzen();	
					System.out.println(VerbaliseTreeManager.listOutput(gentzenTree4));
					System.out.println("UNUSED LONG EXPLANATION 3:");
					System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree4, false, true,true,null));
					System.out.println("UNUSED SHORT EXPLANATION 3:");
					System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree4_b, false, true,false,null));
					
				} catch (Exception e) {
					System.out.println("Error while eliminating irrelevant parts");
					e.printStackTrace();
				}
				*/
					
					// Explanation 5 -- TODO: Komische Reihenfolge, der Statements, Baum prüfen!
					
					String justifications5= 
											"EquivalentClasses(<http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#EarthRealm> <http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#PlanetaryStructure> )" + 
											"EquivalentClasses(<http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#BodyOfWater> <http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#WaterBody> )" + 
											"SubClassOf(<http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#BodyOfWater> <http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#EarthRealm>)" ;
					String conclusion5 = "SubClassOf(<http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#WaterBody> <http://sweet.jpl.nasa.gov/1.1/earthrealm.owl#PlanetaryStructure>)";
				    		
					
					
					ProofTree prooftree5 = constructProoftree(conclusion5,justifications5,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-earthrealm.owl");
					ProofTree prooftree5_b = constructProoftree(conclusion5,justifications5,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-earthrealm.owl");
					
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree5, rulesNonred, 840000, 400000);
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree5_b, rules, 840000, 400000);
					try {
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree5, prooftree5.getRoot().getId());
								// node.getId());
						GentzenTree gentzenTree5 = prooftree5.toGentzen();	
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree5_b, prooftree5_b.getRoot().getId());
						// node.getId());
						GentzenTree gentzenTree5_b = prooftree5_b.toGentzen();
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree5));
						System.out.println("LONG EXPLANATION 2:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree5, false, true,true,null));
						System.out.println("SHORT EXPLANATION 2:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree5_b, false, true,false,null));
						System.out.println("no. long steps: " + gentzenTree5.computePresentationOrder().size());
						System.out.println("no. short steps: " + gentzenTree5_b.computePresentationOrder().size());
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}
					
					
// Explanation 6 
					
					String justifications6= "SubClassOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#FourCheesesTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseTopping>)" +
						"SubClassOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#QuattroFormaggi> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/pizza/pizza.owl#hasTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#FourCheesesTopping>))" +
							"EquivalentClasses(<http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseyPizza> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#Pizza> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/pizza/pizza.owl#hasTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseTopping>)) )" +
							"ObjectPropertyDomain(<http://www.co-ode.org/ontologies/pizza/pizza.owl#hasTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#Pizza>)";
												
					String conclusion6 = "SubClassOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#QuattroFormaggi> <http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseyPizza>)";
				    		
					
					
					ProofTree prooftree6 = constructProoftree(conclusion6,justifications6,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-pizza.owl");
					ProofTree prooftree6_b = constructProoftree(conclusion6,justifications6,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-pizza.owl");
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree6, rulesNonred, 840000, 400000);
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree6_b, rules, 840000, 400000);
					try {
						InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree6, prooftree6.getRoot().getId());
								// node.getId());
						GentzenTree gentzenTree6 = prooftree6.toGentzen();	
						InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree6_b, prooftree6_b.getRoot().getId());
						// node.getId());
						GentzenTree gentzenTree6_b = prooftree6_b.toGentzen();
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree6));
						System.out.println("LONG EXPLANATION 3:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree6, false, true,true,null));
						System.out.println("SHORT EXPLANATION 3:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree6_b, false, true,false,null));
						System.out.println("no. long steps: " + gentzenTree6.computePresentationOrder().size());
						System.out.println("no. short steps: " + gentzenTree6_b.computePresentationOrder().size());
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}
			
					
// Explanation 7 
					/*
					String justifications7= "SubClassOf(<http://www.co-ode.org/ontologies/galen#Penis> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#unpaired>))" +
						    "SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDGenitalSurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#NAMEDTrunkBodyPart>)" + 
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDSurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#BodyPart>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#Penis> <http://www.co-ode.org/ontologies/galen#NAMEDMaleGenitalSurfaceBodyPart>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDMaleGenitalSurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#NAMEDGenitalSurfaceBodyPart>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#BodyPart> <http://www.co-ode.org/ontologies/galen#BodyStructure>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDTrunkBodyPart> <http://www.co-ode.org/ontologies/galen#NAMEDSurfaceBodyPart>) " +
							"EquivalentClasses(<http://www.co-ode.org/ontologies/galen#UnpairedBodyStructure> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#BodyStructure> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#unpaired>)) )";
					*/
					
					/*
					
					String justifications7= "SubClassOf(<http://www.co-ode.org/ontologies/galen#Scrotum> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#unpaired>))" +
						    "SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDGenitalSurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#NAMEDTrunkBodyPart>)" + 
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDSurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#BodyPart>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#Scrotum> <http://www.co-ode.org/ontologies/galen#NAMEDMaleGenitalSurfaceBodyPart>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDMaleGenitalSurfaceBodyPart> <http://www.co-ode.org/ontologies/galen#NAMEDGenitalSurfaceBodyPart>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#BodyPart> <http://www.co-ode.org/ontologies/galen#BodyStructure>)" +
							"SubClassOf(<http://www.co-ode.org/ontologies/galen#NAMEDTrunkBodyPart> <http://www.co-ode.org/ontologies/galen#NAMEDSurfaceBodyPart>) " +
							"EquivalentClasses(<http://www.co-ode.org/ontologies/galen#UnpairedBodyStructure> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/galen#BodyStructure> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/galen#isPairedOrUnpaired> <http://www.co-ode.org/ontologies/galen#unpaired>)) )";
							
					
					
					String conclusion7 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Scrotum> <http://www.co-ode.org/ontologies/galen#UnpairedBodyStructure>)";
					// String conclusion7 = "SubClassOf(<http://www.co-ode.org/ontologies/galen#Penis> <http://www.co-ode.org/ontologies/galen#UnpairedBodyStructure>)";
					
					
					ProofTree prooftree7 = constructProoftree(conclusion7,justifications7,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-galen1.owl");
					ProofTree prooftree7_b = constructProoftree(conclusion7,justifications7,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-galen1.owl");
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree7, rulesNonred, 840000, 400000);
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree7_b, rules, 840000, 400000);
					try {
						InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree7, prooftree7.getRoot().getId());
								// node.getId());
						GentzenTree gentzenTree7 = prooftree7.toGentzen();	
						InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree7_b, prooftree7_b.getRoot().getId());
						// node.getId());
						GentzenTree gentzenTree7_b = prooftree7_b.toGentzen();
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree7));
						System.out.println("LONG EXPLANATION 5:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree7, false, true,true,null));
						System.out.println("SHORT EXPLANATION 5:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree7_b, false, true,false,null));
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}
					
					*/
					
										// Explanation 8 
					
							
					String justifications8= "SubClassOf(<http://www.geneontology.org/go#BSPO_0000006> <http://www.geneontology.org/go#BSPO_0000070>)" + 
											"EquivalentClasses(<http://www.geneontology.org/go#BSPO_0000682> ObjectIntersectionOf(<http://www.geneontology.org/go#BSPO_0000006> ObjectSomeValuesFrom(<http://www.geneontology.org/go#has_position> <http://www.geneontology.org/go#BSPO_0000066>)) )" + 
											"EquivalentClasses(<http://www.geneontology.org/go#BSPO_0000082> ObjectIntersectionOf(<http://www.geneontology.org/go#BSPO_0000070> ObjectSomeValuesFrom(<http://www.geneontology.org/go#has_position> <http://www.geneontology.org/go#BSPO_0000066>)) )";
					
					String conclusion8 = "SubClassOf(<http://www.geneontology.org/go#BSPO_0000682> <http://www.geneontology.org/go#BSPO_0000082>)";
					
					ProofTree prooftree8 = constructProoftree(conclusion8,justifications8,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-spatial.obo.owl");
					ProofTree prooftree8_b = constructProoftree(conclusion8,justifications8,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-spatial.obo.owl");
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree8, rulesNonred, 840000, 400000);
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree8_b, rules, 840000, 400000);
					try {
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree8, prooftree8.getRoot().getId());
								// node.getId());
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree8_b, prooftree8_b.getRoot().getId());
						
						GentzenTree gentzenTree8 = prooftree8.toGentzen();	
						GentzenTree gentzenTree8_b = prooftree8_b.toGentzen();
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree8));
						System.out.println();
						System.out.println("LONG EXPLANATION 4:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree8, false, true,true,null));
						System.out.println("SHORT EXPLANATION 4:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree8_b, false, true,false,null));
						System.out.println("no. long steps: " + gentzenTree8.computePresentationOrder().size());
						System.out.println("no. short steps: " + gentzenTree8_b.computePresentationOrder().size());
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}
					
// Explanation 9 (similar to 6) 
					/*
					String justifications9= "SubClassOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#MozzarellaTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseTopping>)" +
						"SubClassOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#Giardiniera> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/pizza/pizza.owl#hasTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#MozzarellaTopping>))" +
						"EquivalentClasses(<http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseyPizza> ObjectIntersectionOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#Pizza> ObjectSomeValuesFrom(<http://www.co-ode.org/ontologies/pizza/pizza.owl#hasTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseTopping>)) )" +
						"ObjectPropertyDomain(<http://www.co-ode.org/ontologies/pizza/pizza.owl#hasTopping> <http://www.co-ode.org/ontologies/pizza/pizza.owl#Pizza>)";
					
										
					String conclusion9 = "SubClassOf(<http://www.co-ode.org/ontologies/pizza/pizza.owl#Giardiniera> <http://www.co-ode.org/ontologies/pizza/pizza.owl#CheeseyPizza>)";
				    		
					
					
					ProofTree prooftree9 = constructProoftree(conclusion9,justifications9,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-pizza.owl");
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree9, rulesNonred, 840000, 400000);
					try {
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree9, prooftree9.getRoot().getId());
						// org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree9_b, prooftree9_b.getRoot().getId());
						// node.getId());
						GentzenTree gentzenTree9 = prooftree9.toGentzen();	
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree9));
						System.out.println();
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree9, true, false,false,null));
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}
				 		*/
					
// Explanation 10 (strange modelling, replaced by below)
			/*		
					String justifications10= "EquivalentClasses(<http://ontology.dumontierlab.com/OrganicSulfurGroup> ObjectIntersectionOf(<http://ontology.dumontierlab.com/OrganicGroup> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/hasBondWith> <http://ontology.dumontierlab.com/SulfurAtom>)) )" +
											 "SubClassOf(<http://ontology.dumontierlab.com/SulfinicAcidGeneralGroup> <http://ontology.dumontierlab.com/OrganicGroup>)" +
											 "SubObjectPropertyOf(<http://ontology.dumontierlab.com/hasSingleBondWith> <http://ontology.dumontierlab.com/hasBondWith>)" +
											 "EquivalentClasses(<http://ontology.dumontierlab.com/SulfinicAcidGeneralGroup> ObjectIntersectionOf(<http://ontology.dumontierlab.com/CarbonGroup> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/hasSingleBondWith> ObjectIntersectionOf(<http://ontology.dumontierlab.com/SulfurAtom> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/hasDoubleBondWith> <http://ontology.dumontierlab.com/OxygenAtom>) ObjectExactCardinality(1 <http://ontology.dumontierlab.com/hasDoubleBondWith> <http://ontology.dumontierlab.com/OxygenAtom>)))) )";
										
					String conclusion10 = "SubClassOf(<http://ontology.dumontierlab.com/SulfinicAcidGeneralGroup> <http://ontology.dumontierlab.com/OrganicSulfurGroup>)";
					
					
					ProofTree prooftree10 = constructProoftree(conclusion10,justifications10,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-organic-functional-group-complex.owl");
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree10, rulesNonred, 840000, 400000);
					try {
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree10, prooftree.getRoot().getId());
								// node.getId());
						GentzenTree gentzenTree10 = prooftree10.toGentzen();	
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree10));
						System.out.println();
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree10, true, false,null));
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}	
				*/	
					
// Explanation 10
					
					String justifications10= "SubClassOf(<http://ontology.dumontierlab.com/PressureUnit> <http://ontology.dumontierlab.com/Unit>)" +
											 "EquivalentClasses(<http://ontology.dumontierlab.com/DerivedUnit> ObjectIntersectionOf(<http://ontology.dumontierlab.com/Unit> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/derivesFrom> <http://ontology.dumontierlab.com/Unit>)) )" +
											 "SubClassOf(<http://ontology.dumontierlab.com/ForceUnit> <http://ontology.dumontierlab.com/Unit>)" +
											 "SubClassOf(<http://ontology.dumontierlab.com/PressureUnit> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/derivesFrom> <http://ontology.dumontierlab.com/ForceUnit>))";
					String conclusion10 = "SubClassOf(<http://ontology.dumontierlab.com/PressureUnit> <http://ontology.dumontierlab.com/DerivedUnit>)";
					
					
					ProofTree prooftree10 = constructProoftree(conclusion10,justifications10,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-property-complex.owl");
					ProofTree prooftree10_b = constructProoftree(conclusion10,justifications10,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-property-complex.owl");
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree10, rulesNonred, 840000, 400000);
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree10_b, rules, 840000, 400000);
					try {
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree10, prooftree10.getRoot().getId());
								// node.getId());
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree10_b, prooftree10_b.getRoot().getId());
						
						GentzenTree gentzenTree10 = prooftree10.toGentzen();	
						GentzenTree gentzenTree10_b = prooftree10_b.toGentzen();	
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree10));
						System.out.println();
						System.out.println("LONG EXPLANATION 5:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree10, false, true,true,null));
						System.out.println("SHORT EXPLANATION 5:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree10_b, false, true,false,null));
						System.out.println("no. long steps: " + gentzenTree10.computePresentationOrder().size());
						System.out.println("no. short steps: " + gentzenTree10_b.computePresentationOrder().size());
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}	
					
// Explanation 11 (opengalen-no-propchains)
					/*
					String justifications11= 
							"EquivalentClasses(<http://www.opengalen.org/owl/opengalen.owl#changedActivityLevel> ObjectIntersectionOf(<http://www.opengalen.org/owl/opengalen.owl#ProcessActivity> ObjectSomeValuesFrom(<http://www.opengalen.org/owl/opengalen.owl#hasQuantity> ObjectIntersectionOf(<http://www.opengalen.org/owl/opengalen.owl#Level> ObjectSomeValuesFrom(<http://www.opengalen.org/owl/opengalen.owl#hasChangeInState> <http://www.opengalen.org/owl/opengalen.owl#changed>)))) )" +
							"SubClassOf(<http://www.opengalen.org/owl/opengalen.owl#increased> <http://www.opengalen.org/owl/opengalen.owl#changed>)" +
							"EquivalentClasses(<http://www.opengalen.org/owl/opengalen.owl#increasedActivityLevel> ObjectIntersectionOf(<http://www.opengalen.org/owl/opengalen.owl#ProcessActivity> ObjectSomeValuesFrom(<http://www.opengalen.org/owl/opengalen.owl#hasQuantity> ObjectIntersectionOf(<http://www.opengalen.org/owl/opengalen.owl#Level> ObjectSomeValuesFrom(<http://www.opengalen.org/owl/opengalen.owl#hasChangeInState> <http://www.opengalen.org/owl/opengalen.owl#increased>)))) )";
							
							
				    String conclusion11 = "SubClassOf(<http://www.opengalen.org/owl/opengalen.owl#increasedActivityLevel> <http://www.opengalen.org/owl/opengalen.owl#changedActivityLevel>)"; 
					
					
					ProofTree prooftree11 = constructProoftree(conclusion11,justifications11,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-opengalen-no-propchains.owl");
					ProofTree prooftree11_b = constructProoftree(conclusion11,justifications11,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-opengalen-no-propchains.owl");
					
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree11, rulesNonred, 840000, 400000);
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree11, rules, 840000, 400000);
					try {
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree11, prooftree11.getRoot().getId());
								// node.getId());
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree11_b, prooftree11_b.getRoot().getId());
						// node.getId());
						GentzenTree gentzenTree11 = prooftree11.toGentzen();	
						GentzenTree gentzenTree11_b = prooftree11.toGentzen();	
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree11));
						System.out.println();
						System.out.println("LONG EXPLANATION 8:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree11, false, true,true,null));
						System.out.println("SHORT EXPLANATION 8:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree11_b, false, true,false,null));
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}	
					*/
					
// Explanation 12 (tambis-patched)
					
					String justifications12= "EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#hydrolase> ObjectIntersectionOf(<http://www.mindswap.org/ontologies/tambis-full.owl#macromolecular-compound> ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#catalyses> <http://www.mindswap.org/ontologies/tambis-full.owl#hydrolysis>) ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#has-ec-number> <http://www.mindswap.org/ontologies/tambis-full.owl#ec-number>) ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>) ObjectAllValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>)) )" +
											 "EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#gene-product> <http://www.mindswap.org/ontologies/tambis-full.owl#protein> )" +
											 "EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#protein> ObjectIntersectionOf(<http://www.mindswap.org/ontologies/tambis-full.owl#macromolecular-compound> ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>) ObjectAllValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>)) )";
							
							
				    String conclusion12 = "SubClassOf(<http://www.mindswap.org/ontologies/tambis-full.owl#hydrolase> <http://www.mindswap.org/ontologies/tambis-full.owl#gene-product>)";
				    		
				    	
					
					ProofTree prooftree12 = constructProoftree(conclusion12,justifications12,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
					ProofTree prooftree12_b = constructProoftree(conclusion12,justifications12,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
					
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree12, rulesNonred, 840000, 400000);
					InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree12_b, rules, 840000, 400000);
					try {
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree12, prooftree12.getRoot().getId());
						org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree12_b, prooftree12_b.getRoot().getId());
						
						// node.getId());
						GentzenTree gentzenTree12 = prooftree12.toGentzen();	
						GentzenTree gentzenTree12_b = prooftree12_b.toGentzen();	
						System.out.println(VerbaliseTreeManager.listOutput(gentzenTree12));
						System.out.println();
						System.out.println("LONG EXPLANATION 6:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree12, false, true,true,null));
						System.out.println("SHORT EXPLANATION 6:");
						System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree12_b, false, true,false,null));
						System.out.println("no. long steps: " + gentzenTree12.computePresentationOrder().size());
						System.out.println("no. short steps: " + gentzenTree12_b.computePresentationOrder().size());
						
					} catch (Exception e) {
						System.out.println("Error while eliminating irrelevant parts");
						e.printStackTrace();
				 		}
					
					// Explanation 13 (mygrid)
					
					
					String justifications13= 
							"SubClassOf(<http://www.mygrid.org.uk/ontology#structure> <http://www.mygrid.org.uk/ontology#domain_concept>)" +
							"EquivalentClasses(<http://www.mygrid.org.uk/ontology#informatics_abstract_structure> ObjectIntersectionOf(<http://www.mygrid.org.uk/ontology#abstract_structure> ObjectSomeValuesFrom(<http://www.mygrid.org.uk/ontology#in_domain> <http://www.mygrid.org.uk/ontology#informatics_domain>)) )"+
							"EquivalentClasses(<http://www.mygrid.org.uk/ontology#informatics_domain_concept> ObjectIntersectionOf(<http://www.mygrid.org.uk/ontology#domain_concept> ObjectSomeValuesFrom(<http://www.mygrid.org.uk/ontology#in_domain> <http://www.mygrid.org.uk/ontology#informatics_domain>)) )" +
							"SubClassOf(<http://www.mygrid.org.uk/ontology#abstract_structure> <http://www.mygrid.org.uk/ontology#structure>)";
														
			
   String conclusion13 = "SubClassOf(<http://www.mygrid.org.uk/ontology#informatics_abstract_structure>  <http://www.mygrid.org.uk/ontology#informatics_domain_concept>)";
   		
   	
	
	ProofTree prooftree13 = constructProoftree(conclusion13,justifications13,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-mygrid-unclassified.owl");
	ProofTree prooftree13_b = constructProoftree(conclusion13,justifications13,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-mygrid-unclassified.owl");
	
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree13, rulesNonred, 840000, 400000);
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree13_b, rules, 840000, 400000);
	try {
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree13, prooftree13.getRoot().getId());
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree13_b, prooftree13_b.getRoot().getId());
		
		// node.getId());
		GentzenTree gentzenTree13 = prooftree13.toGentzen();	
		GentzenTree gentzenTree13_b = prooftree13_b.toGentzen();	
		System.out.println(VerbaliseTreeManager.listOutput(gentzenTree13));
		System.out.println();
		System.out.println("LONG EXPLANATION 7:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree13, false, true,true,null));
		System.out.println("SHORT EXPLANATION 7:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree13_b, false, true,false,null));
		System.out.println("no. long steps: " + gentzenTree13.computePresentationOrder().size());
		System.out.println("no. short steps: " + gentzenTree13_b.computePresentationOrder().size());
		
	} catch (Exception e) {
		System.out.println("Error while eliminating irrelevant parts");
		e.printStackTrace();
		}
					
	// Explanation 14
	/*
	String justifications14= "EquivalentClasses(<http://ontology.dumontierlab.com/DerivedUnit> ObjectIntersectionOf(<http://ontology.dumontierlab.com/Unit> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/derivesFrom> <http://ontology.dumontierlab.com/Unit>)) )" +
		"SubClassOf(<http://ontology.dumontierlab.com/AreaUnit> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/derivesFrom> ObjectIntersectionOf(<http://ontology.dumontierlab.com/UnitRaisedToPowerPositiveTwo> ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/derivesFrom> <http://ontology.dumontierlab.com/LengthUnit>))))" +
			"SubClassOf(<http://ontology.dumontierlab.com/AreaUnit> <http://ontology.dumontierlab.com/Unit>)" +
			"EquivalentClasses(<http://ontology.dumontierlab.com/UnitRaisedToPowerPositiveTwo> ObjectIntersectionOf(ObjectSomeValuesFrom(<http://ontology.dumontierlab.com/derivesFrom> <http://ontology.dumontierlab.com/Unit>) DataHasValue(<http://ontology.dumontierlab.com/isRaisedToPowerOf> \"2.0\"^^xsd:double)) )" +
			"TransitiveObjectProperty(<http://ontology.dumontierlab.com/derivesFrom>)";
	String conclusion14 = "SubClassOf(<http://ontology.dumontierlab.com/AreaUnit> <http://ontology.dumontierlab.com/DerivedUnit>)";
	
	
	ProofTree prooftree14 = constructProoftree(conclusion14,justifications14,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-property-complex.owl");
	ProofTree prooftree14_b = constructProoftree(conclusion14,justifications14,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-property-complex.owl");
	
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree14, rulesNonred, 840000, 400000);
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree14_b, rules, 840000, 400000);
	try {
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree14, prooftree14.getRoot().getId());
				// node.getId());
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree14_b, prooftree14_b.getRoot().getId());
		
		GentzenTree gentzenTree14 = prooftree14.toGentzen();	
		GentzenTree gentzenTree14_b = prooftree14_b.toGentzen();	
		System.out.println(VerbaliseTreeManager.listOutput(gentzenTree14));
		System.out.println();
		System.out.println("LONG EXPLANATION 8:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree14, false, true,true,null));
		System.out.println("SHORT EXPLANATION 8:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree14_b, false, true,false,null));
		
	} catch (Exception e) {
		System.out.println("Error while eliminating irrelevant parts");
		e.printStackTrace();
 		}			
			*/	
	// Explanation 15 (opengalen-no-propchains)
	
	String justifications15= "EquivalentClasses(<http://www.opengalen.org/owl/opengalen.owl#InwardOutwardRotation> <http://www.opengalen.org/owl/opengalen.owl#MedialLateralRotation> )" +
							 "SubClassOf(<http://www.opengalen.org/owl/opengalen.owl#OutwardRotation> <http://www.opengalen.org/owl/opengalen.owl#InwardOutwardRotation>)" +
							 "EquivalentClasses(<http://www.opengalen.org/owl/opengalen.owl#ExternalRotation> <http://www.opengalen.org/owl/opengalen.owl#OutwardRotation> )";
			
    String conclusion15 = "SubClassOf(<http://www.opengalen.org/owl/opengalen.owl#ExternalRotation> <http://www.opengalen.org/owl/opengalen.owl#MedialLateralRotation>)";
    		
    	
	
	ProofTree prooftree15 = constructProoftree(conclusion15,justifications15,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
	ProofTree prooftree15_b = constructProoftree(conclusion15,justifications15,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
	
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree15, rulesNonred, 840000, 400000);
	InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree15_b, rules, 840000, 400000);
	try {
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree15, prooftree15.getRoot().getId());
		org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree15_b, prooftree15_b.getRoot().getId());
		
		// node.getId());
		GentzenTree gentzenTree15 = prooftree15.toGentzen();	
		GentzenTree gentzenTree15_b = prooftree15_b.toGentzen();	
		System.out.println(VerbaliseTreeManager.listOutput(gentzenTree15));
		System.out.println();
		System.out.println("LONG EXPLANATION 8:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree15, false, true,true,null));
		System.out.println("SHORT EXPLANATION 8:");
		System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree15_b, false, true,false,null));
		
	} catch (Exception e) {
		System.out.println("Error while eliminating irrelevant parts");
		e.printStackTrace();
 		}
	
	// Explanation 16 (tambis) -- unused
	
		String justifications16= 
				"EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#nad-requiring-oxidoreductase> ObjectIntersectionOf(<http://www.mindswap.org/ontologies/tambis-full.owl#macromolecular-compound> ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#catalyses> <http://www.mindswap.org/ontologies/tambis-full.owl#oxidation-and-reduction>) ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#has-bound> <http://www.mindswap.org/ontologies/tambis-full.owl#nad>) ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#has-ec-number> <http://www.mindswap.org/ontologies/tambis-full.owl#ec-number>) ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>) ObjectAllValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>) ObjectMinCardinality(1 <http://www.mindswap.org/ontologies/tambis-full.owl#has-bound> owl:Thing)) )" + 
				"EquivalentClasses(<http://www.mindswap.org/ontologies/tambis-full.owl#oxidoreductase> ObjectIntersectionOf(<http://www.mindswap.org/ontologies/tambis-full.owl#macromolecular-compound> ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#catalyses> <http://www.mindswap.org/ontologies/tambis-full.owl#oxidation-and-reduction>) ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#has-ec-number> <http://www.mindswap.org/ontologies/tambis-full.owl#ec-number>) ObjectSomeValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>) ObjectAllValuesFrom(<http://www.mindswap.org/ontologies/tambis-full.owl#polymer-of> <http://www.mindswap.org/ontologies/tambis-full.owl#amino-acid>)) )";
				
	    String conclusion16 = "SubClassOf(<http://www.mindswap.org/ontologies/tambis-full.owl#nad-requiring-oxidoreductase> <http://www.mindswap.org/ontologies/tambis-full.owl#oxidoreductase>)";
	    		
	    	
		
		ProofTree prooftree16 = constructProoftree(conclusion16,justifications16,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
		ProofTree prooftree16_b = constructProoftree(conclusion16,justifications16,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
		
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree16, rulesNonred, 840000, 400000);
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree16_b, rules, 840000, 400000);
		try {
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree16, prooftree16.getRoot().getId());
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree16_b, prooftree16_b.getRoot().getId());
			
			// node.getId());
			GentzenTree gentzenTree16 = prooftree16.toGentzen();	
			GentzenTree gentzenTree16_b = prooftree16_b.toGentzen();	
			System.out.println(VerbaliseTreeManager.listOutput(gentzenTree16));
			System.out.println();
			System.out.println("LONG EXPLANATION 8:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree16, false, true,true,null));
			System.out.println("SHORT EXPLANATION 8:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree16_b, false, true,false,null));
			
		} catch (Exception e) {
			System.out.println("Error while eliminating irrelevant parts");
			e.printStackTrace();
	 		}
		
		// Explanation (tambis-patched)
		
		String justifications17= "EquivalentClasses(<http://sweet.jpl.nasa.gov/1.1/substance.owl#ChemicalCompound> <http://sweet.jpl.nasa.gov/1.1/substance.owl#Compound> )" +
								 "EquivalentClasses(<http://sweet.jpl.nasa.gov/1.1/substance.owl#Fe2O3> <http://sweet.jpl.nasa.gov/1.1/substance.owl#Hematite> )" +
								"SubClassOf(<http://sweet.jpl.nasa.gov/1.1/substance.owl#Fe2O3> <http://sweet.jpl.nasa.gov/1.1/substance.owl#Compound>)";
				
	    String conclusion17 = "SubClassOf(<http://sweet.jpl.nasa.gov/1.1/substance.owl#Hematite> <http://sweet.jpl.nasa.gov/1.1/substance.owl#ChemicalCompound>)";
	    		
	    	
		
		ProofTree prooftree17 = constructProoftree(conclusion17,justifications17,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
		ProofTree prooftree17_b = constructProoftree(conclusion17,justifications17,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-tambis-patched.owl");
		
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree17, rulesNonred, 840000, 400000);
		InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree17_b, rules, 840000, 400000);
		try {
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree17, prooftree17.getRoot().getId());
			org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree17_b, prooftree17_b.getRoot().getId());
			
			// node.getId());
			GentzenTree gentzenTree17 = prooftree17.toGentzen();	
			GentzenTree gentzenTree17_b = prooftree17_b.toGentzen();	
			System.out.println(VerbaliseTreeManager.listOutput(gentzenTree17));
			System.out.println();
			System.out.println("LONG EXPLANATION 8:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree17, false, true,true,null));
			System.out.println("SHORT EXPLANATION 8:");
			System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree17_b, false, true,false,null));
			
		} catch (Exception e) {
			System.out.println("Error while eliminating irrelevant parts");
			e.printStackTrace();
	 		}
		
		// Explanation ()
		
		String justifications18= 
				"EquivalentClasses(<http://www.geneontology.org/go#SO_0000872> ObjectIntersectionOf(<http://www.geneontology.org/go#SO_0000234> ObjectSomeValuesFrom(<http://www.geneontology.org/go#adjacent_to> <http://www.geneontology.org/go#SO_0000636>) ObjectSomeValuesFrom(<http://www.geneontology.org/go#has_quality> <http://www.geneontology.org/go#SO_0000870>)) )" +
			    "SubClassOf(<http://www.geneontology.org/go#SO_0000234> <http://www.geneontology.org/go#SO_0000233>)" +
				"EquivalentClasses(<http://www.geneontology.org/go#SO_0000479> ObjectIntersectionOf(<http://www.geneontology.org/go#SO_0000673> ObjectSomeValuesFrom(<http://www.geneontology.org/go#has_quality> <http://www.geneontology.org/go#SO_0000870>)) )" +
				"SubClassOf(<http://www.geneontology.org/go#SO_0000233> <http://www.geneontology.org/go#SO_0000673>)";
				String conclusion18 = "SubClassOf(<http://www.geneontology.org/go#SO_0000872> <http://www.geneontology.org/go#SO_0000479>)";



ProofTree prooftree18 = constructProoftree(conclusion18,justifications18,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-so-xp.obo.owl");
ProofTree prooftree18_b = constructProoftree(conclusion18,justifications18,"/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-so-xp.obo.owl");

InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree18, rulesNonred, 840000, 400000);
InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree18_b, rules, 840000, 400000);
try {
org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree18, prooftree18.getRoot().getId());
org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree18_b, prooftree18_b.getRoot().getId());

// node.getId());
GentzenTree gentzenTree18 = prooftree18.toGentzen();	
GentzenTree gentzenTree18_b = prooftree18_b.toGentzen();	
System.out.println(VerbaliseTreeManager.listOutput(gentzenTree18));
System.out.println();
System.out.println("LONG EXPLANATION 10:");
System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree18, false, true,true,null));
System.out.println("SHORT EXPLANATION 10:");
System.out.println(VerbaliseTreeManager.verbaliseNL(gentzenTree18_b, false, true,false,null));

} catch (Exception e) {
System.out.println("Error while eliminating irrelevant parts");
e.printStackTrace();
}
		
		
			
			} // end main

	
}
