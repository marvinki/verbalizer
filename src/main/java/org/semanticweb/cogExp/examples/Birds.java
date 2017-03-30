package org.semanticweb.cogExp.examples;
import java.io.File;
import java.io.IOException;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.ProofBasedExplanationService;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

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
		String root = "/home/fpaffrath/Dokumente/";
		String path = root+"ornithology_mod.owl";
		File ontologyfile = new java.io.File(path);
		OWLOntology tinyExampleOntology = 
				OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(ontologyfile);	
		OWLReasoner reasoner = reasonerFactory.createReasoner(tinyExampleOntology);
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		
		
		String a = "CoalTit";
		String b = "BirdRequiringSmallEntranceHole";
		String result = getResult(a, b, path, reasonerFactory, reasoner );
		
		
		a = "MarshTit";
		b = "BirdRequiringSmallEntranceHole";
		String result2 = getResult(a, b, path, reasonerFactory, reasoner );
		
		String nadelholzreicherMischwald = "MixedForestsRichInConifers";
		String Mischwald = "MixedForests";
		String result3 = getResult(nadelholzreicherMischwald, Mischwald, path, reasonerFactory, reasoner );
		
		a = "GreatTit";
		b = "SeedPredator";
		String result4 = getResult(a, b, path, reasonerFactory, reasoner );
		
		a = "EuropeanPiedFlycatcher";
		b = "PulpPredator";
		String result5b = getResult(a, b, path, reasonerFactory, reasoner );
		
		System.out.println("Birds : \n");
		
		System.out.println(result);
		System.out.println(result2);
		System.out.println(result3);
		System.out.println(result4);
		System.out.println(result5b);

		
//		
//		OWLOntologyManager knowledgeableManager = getImportKnowledgeableOntologyManger();
//		OWLOntology ExampleOntology = createOntology(knowledgeableManager);
//	
//		
//		
//		OWLReasoner reasoner2 = reasonerFactory.createReasoner(ExampleOntology);
//		
//		String result5 = getResult("BirchWood", "DecidiousWood", "/home/fpaffrath/Dokumente/gatheredOntologies/bosch-reworked.owl", reasonerFactory, reasoner2);
//		String result6 = getResult("DrillHoleInWood", "DrillHole", "/home/fpaffrath/Dokumente/gatheredOntologies/bosch-reworked.owl", reasonerFactory, reasoner2);
//		String result9 = getResult("CutPaper", "CutOut", "/home/fpaffrath/Dokumente/gatheredOntologies/bosch-reworked.owl", reasonerFactory, reasoner2);
//		String result7 = getResult("DrillEntryHoleWithJigsaw", "CutOut", "/home/fpaffrath/Dokumente/gatheredOntologies/bosch-reworked.owl", reasonerFactory, reasoner2);
//		String result8 = getResult("WoodTwistDrillBit", "DrillBit", "/home/fpaffrath/Dokumente/gatheredOntologies/bosch-reworked.owl", reasonerFactory, reasoner2);
//
//		
//		System.out.println("\nTools : \n");
//		System.out.println(result5);
//		System.out.println(result6);
//		System.out.println(result9);		
//		System.out.println(result7);
//		System.out.println(result8);
		
		
	}
	
	
	
	
	
	
	

	private static String getResult(String a, String b, String path, OWLReasonerFactory reasonerFactory,
			OWLReasoner reasoner) {
		// TODO Auto-generated method stub
		GentzenTree tree = ProofBasedExplanationService.computeTree(a, 
				b, 
				path, 
				reasonerFactory, 
				reasoner);
		// GentzenTree tree2 = ProofBasedExplanationService.computeTree("CoalTit", "Tit", "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Ontologien/ornithology.owl", reasonerFactory, reasoner);
		
		System.out.println("\n");
		String result = VerbaliseTreeManager.verbaliseNL(tree, false, false, null);
		return (result);
		
	}
	
	public static OWLOntologyManager getImportKnowledgeableOntologyManger(){
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		String root = "file:///home/fpaffrath/Dokumente/gatheredOntologies";
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.semanticweb.org/marvin/ontologies/2016/10/untitled-ontology-772"),
		           IRI.create(root+"bosch-reworked02032017.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.semanticweb.org/marvin/ontologies/2016/10/ontologyExport_PTGreenProducts.rdf"),
		           IRI.create(root+"ontologyExport_PTGreenProducts.rdf")));
		
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.bosch.com/MaterialsOntology"),
		           IRI.create(root+"ontologyExport_MaterialsOntology.rdf")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.bosch.com/ActivitiesOntology"),
		           IRI.create(root+"ontologyExport_ActivitiesOntology.rdf")));
		
		return manager;
	}
	
	public static OWLOntology createOntology(OWLOntologyManager manager){
		OWLOntology ontology = null;
		try {
			ontology = manager.createOntology();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ontology;
}


}