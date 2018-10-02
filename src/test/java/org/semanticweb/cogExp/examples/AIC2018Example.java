package org.semanticweb.cogExp.examples;
import java.io.File;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
// import org.apache.log4j.Level;
// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPICompatibility;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.ProofBasedExplanation.ClusterExplanationService;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;
import org.semanticweb.cogExp.inferencerules.INLG2012NguyenEtAlRules;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDisjointClassesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredIndividualAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;
import org.slf4j.LoggerFactory;

// import org.apache.log4j.Level;
// import org.apache.log4j.LogManager;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import uk.ac.manchester.cs.jfact.JFactFactory;

public class AIC2018Example {
	
	public static void main(String[] args) throws OWLOntologyCreationException{
	
	// load an ontology through the OWL API
	File file = new File(".");
	// File ontologyfile = new java.io.File(Paths.get(file.getAbsoluteFile().getParentFile().getAbsolutePath(),
	 // 								  "resource", 
	 // 								  "tinyExampleOntology2.owl").toString());
	
	
	String ontologyname = "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain-AIC.owl";
	// String ontologyname = "/Users/marvin/work/ki-ulm-repository/publications/conferences/2017/ICCT/modelling-full/modelling-excerpt.owl";
	File ontologyfile = new java.io.File(ontologyname);
	// File ontologyfile = new java.io.File("/Users/marvin/work/workspace/justifications/originaltones-modified/Ontology-galen1.owl");
	
	
	
	OWLOntology ontology = 
			OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(ontologyfile);	
	
	System.out.println("loaded");
	
	// Language setting
	
	VerbaliseTreeManager.setLocale(Locale.ENGLISH);
	
	// OWLReasonerFactory reasonerFactory = new JFactFactory();
	// SimpleConfiguration configuration = new SimpleConfiguration(50000);
	// OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, configuration);
	
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	OWLDataFactory dataFactory = manager.getOWLDataFactory();
	
	OWLReasonerFactory reasonerFactory =new Reasoner.ReasonerFactory();
	
	SimpleConfiguration configuration = new SimpleConfiguration(50000);
	OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, configuration);
	
	InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
	
	Set<OWLAxiom> infAxs = null;
	
	Set<OWLAxiom> newaxioms = new HashSet<OWLAxiom>();
	try {
		OWLOntology newontology = manager.createOntology();
	
	OWLDataFactory dataFactory2=manager.getOWLDataFactory();
	
	// iog.fillOntology(dataFactory2, newontology);
	// iog.fillOntology(outputOntologyManager, infOnt);
		OWLAPICompatibility.fill(iog, dataFactory2, newontology);
	
		newaxioms = newontology.getAxioms(true);}catch(Exception e){}
	
	infAxs = newaxioms;
	
	String ontologyuri = "http://www.semanticweb.org/diy-domain#";
	OWLIndividual drill1 = dataFactory.getOWLNamedIndividual(IRI.create(ontologyuri + "drill-1"));
	
	/* 		
	reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY,
            InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_HIERARCHY,
            InferenceType.DATA_PROPERTY_HIERARCHY, InferenceType.OBJECT_PROPERTY_ASSERTIONS);
	*/

	// InferredObjectPropertyAxiomGenerator generator = new InferredObjectPropertyAxiomGenerator<? extends OWLAxiom>();
	// generator.createAxioms(dataFactory, reasoner);
	
        // List<InferredAxiomGenerator<? extends OWLAxiom>> generators = new ArrayList<>();
        // generators.add(new InferredSubClassAxiomGenerator());
        // generators.add(new InferredClassAssertionAxiomGenerator());
        // generators.add(new InferredDataPropertyCharacteristicAxiomGenerator());
        // generators.add(new InferredEquivalentClassAxiomGenerator());
        //generators.add(new InferredEquivalentDataPropertiesAxiomGenerator());
        // generators.add(new InferredEquivalentObjectPropertyAxiomGenerator());
        // generators.add(new InferredInverseObjectPropertiesAxiomGenerator());
        // generators.add(new InferredObjectPropertyCharacteristicAxiomGenerator());

        // NOTE: InferredPropertyAssertionGenerator significantly slows down
        // inference computation
        // generators.add(new org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator());

        // generators.add(new InferredSubClassAxiomGenerator());
        // generators.add(new InferredSubDataPropertyAxiomGenerator());
        // generators.add(new InferredSubObjectPropertyAxiomGenerator());
        // List<InferredIndividualAxiomGenerator<? extends OWLIndividualAxiom>> individualAxioms =
        //     new ArrayList<>();
       //  generators.addAll(individualAxioms);

        // generators.add(new InferredDisjointClassesAxiomGenerator());
        //InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, generators);
       //  OWLOntology inferredAxiomsOntology = manager.createOntology();
       // iog.fillOntology(dataFactory, inferredAxiomsOntology);
       //  Set<OWLAxiom> axioms = inferredAxiomsOntology.getAxioms(true);
        //for (OWLAxiom ax : axioms){
        //	System.out.println(ax);
        // }
        
	
	// ClusterExplanationService clusterExplanationService = new ClusterExplanationService(ontology, reasoner,
	// 		reasonerFactory);
	// clusterExplanationService.precomputeAxioms();
	// clusterExplanationService.listInferredAssertions(ontologyname);
	
	// InferredAxiomGenerator generator = new InferredClassAssertionAxiomGenerator();
	// InferredAxiomGenerator generator = new InferredDataPropertyCharacteristicAxiomGenerator();
	// Set<OWLAxiom> infAxs = generator.createAxioms(dataFactory, reasoner);
	// Set<OWLAxiom> infAxs = generator.createAxioms(manager, reasoner);
	
	
	// Set<OWLAxiom> infAxs = clusterExplanationService.inferAxioms(ontology);
	
		
		OWLClass activity = dataFactory.getOWLClass(IRI.create(ontologyuri + "Screwing3MMScrewInSpruce"));
		OWLClass predrilling = dataFactory.getOWLClass(IRI.create(ontologyuri + "Predrilling"));
		OWLClass spruce = dataFactory.getOWLClass(IRI.create(ontologyuri + "Spruce"));
		OWLClass screw3mm = dataFactory.getOWLClass(IRI.create(ontologyuri + "Screw3MM"));
	  	OWLObjectProperty doesNotRequire = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "doesNotRequire"));
	  	OWLObjectProperty screwingConfig_materialType = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "ScrewingConfig_materialType"));
		OWLObjectProperty screwingConfig_screwType = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "ScrewingConfig_screwType"));
		OWLObjectSomeValuesFrom svf = dataFactory.getOWLObjectSomeValuesFrom(doesNotRequire, predrilling);
	  	OWLSubClassOfAxiom ax = dataFactory.getOWLSubClassOfAxiom(activity, svf);
	  	
	  	OWLObjectSomeValuesFrom matSpruce = dataFactory.getOWLObjectSomeValuesFrom(screwingConfig_materialType, spruce);
	 	OWLObjectSomeValuesFrom screwSmall = dataFactory.getOWLObjectSomeValuesFrom(screwingConfig_screwType, screw3mm);
	 	OWLObjectIntersectionOf inter = dataFactory.getOWLObjectIntersectionOf(matSpruce,screwSmall);
	  	OWLSubClassOfAxiom ax2 = dataFactory.getOWLSubClassOfAxiom(inter, svf);
		// System.out.println(ax2);
		
	    TextElementSequence sequence = VerbalisationManager.verbalizeAxiomAsSequence(ax, reasoner, reasonerFactory, ontology,100, 30000, "OP",true,false);
		System.out.println(sequence.toString());
	
	
	}
	
}
