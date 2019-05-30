package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.semanticweb.cogExp.core.SequentInferenceRule;
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

public enum OWLAPIManagerManager {

	INSTANCE;
	
	private final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
	private final OWLDataFactory dataFactory=ontologyManager.getOWLDataFactory();
	private final OWLFunctionalSyntaxOWLParser functionalSyntaxParser = new OWLFunctionalSyntaxOWLParser();
	static private OWLOntology tmpOntology = createOntology();
	
	public OWLOntologyManager getOntologyManager(){
		return ontologyManager;
	} 
	
	public OWLDataFactory getDataFactory(){
		return dataFactory;
	}
	
	public OWLFunctionalSyntaxOWLParser getFunctionalSyntaxParser(){
		return functionalSyntaxParser;
}
	
	
	
	public static OWLOntology createOntology(){
		OWLOntology ontology = null;
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			ontology = manager.createOntology();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ontology;
}
	
	public static OWLOntologyManager getImportKnowledgeableOntologyManger(){
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.loa-cnr.it/ontologies/DUL.owl"),
		           IRI.create("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Hydrology/v2.0/Hydrology.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-Hydrology.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Rabbit/v1.0/Rabbit.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/rabbit.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Topography/v0.1/Topography.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/topography.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/SpatialRelations/v0.2/SpatialRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/spatialrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/MereologicalRelations/v0.2/MereologicalRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/mereologicalrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/NetworkRelations/v0.2/NetworkRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/networkrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Hydrology/v2.0/Hydrology.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-Hydrology.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Rabbit/v1.0/Rabbit.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/rabbit.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Topography/v0.1/Topography.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/topography.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/SpatialRelations/v0.2/SpatialRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/spatialrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/MereologicalRelations/v0.2/MereologicalRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/mereologicalrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/NetworkRelations/v0.2/NetworkRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/networkrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/nulo"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-nulo.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/bfo"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-bfo.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/bro"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-bro.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/annotation"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-annotation.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/bro-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-bro-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/biochemistry-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-biochemistry-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/chemistry-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-chemistry-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-compound-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-compound-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-compound-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-compound-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/chemistry-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-chemistry-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-functional-group-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-functional-group-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/atom-common"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-atom-common.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-functional-group-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-functional-group-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/element-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-element-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/periodic-table-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-periodic-table-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/periodic-table-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-periodic-table-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/molecule-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-molecule-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/molecule-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-molecule-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/atom-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-atom-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/property-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-property-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/property-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-property-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/unit-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-unit-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/unit-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-unit-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/yowl-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-yowl-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/goslim"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-goslim.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://sweet.jpl.nasa.gov/1.1/property.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-property.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://sweet.jpl.nasa.gov/1.1/units.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-units.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl"),
		           IRI.create("file:///Users/marvin/Downloads/6537658-811aec9d3c507976efd6aba1599ea3c12f8ed61c/list.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://translationalmedicineontology.googlecode.com/svn/trunk/ontology/tmo-external.owl"),
		           IRI.create("https://raw.githubusercontent.com/micheldumontier/translationalmedicineontology/master/ontology/tmo-external.owl")));
		return manager;
}
	
	public static OWLAxiom parseAxiomFunctional(String str, OWLOntology ont){
		
		// System.out.println("Trying to parse: " + str);
		
		// OWLFunctionalSyntaxOWLParserFactory parserFactory = new OWLFunctionalSyntaxOWLParserFactory();
		OWLFunctionalSyntaxOWLParser parser = OWLAPIManagerManager.INSTANCE.getFunctionalSyntaxParser();
		
		
		OWLOntologyLoaderConfiguration loaderConfiguration = new OWLOntologyLoaderConfiguration();
		
		str = "Ontology(" + str + ")";
		
		
		
		// System.out.println("Trying to parse: " + str);
		
		InputStream in = new ByteArrayInputStream(str.getBytes());
		StreamDocumentSource streamSource = new StreamDocumentSource(in);
		
		// OWLOntologyDocumentSource toBeParsed = new OWLOntologyDocumentSource();
		
		OWLOntologyManager manager = OWLAPIManagerManager.INSTANCE.getOntologyManager();
		// PrefixOWLOntologyFormat pm = (PrefixOWLOntologyFormat) manager.getOntologyFormat(tmpOntology);
		// pm.setPrefix("obo", "http://purl.obolibrary.org/obo");
		
		OWLAxiom a = null;
		
		try {
			
		manager.removeAxioms(tmpOntology, tmpOntology.getAxioms());
		
		OWLDocumentFormat format = parser.parse(streamSource, tmpOntology, loaderConfiguration);
		
		
		Set<OWLAxiom> axioms = tmpOntology.getAxioms();
		for (OWLAxiom ax : axioms){
			a = ax;
			in.close();
			streamSource = null;
			axioms = null;
			break;
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// str = str.replace("ObjectIntersectionOf(","");
			// str = str.substring(9,str.length()-2);
			
			// parseAxiomFunctional(str,newontology);
			
			 if (true)
		    	throw new RuntimeException();
		}
		
		/*
		OWLFunctionalSyntaxParser parser = new OWLFunctionalSyntaxParser(new StringReader(str));
	    parser.setUp(ont, new OWLOntologyLoaderConfiguration());
		 OWLAxiom a = null;
		try {
        a = parser.Axiom();
       } catch (ParseException e) {
         System.err.println(e.getMessage());
         System.err.println("Error parsing axiom: " + str);
        }
        */
		// System.out.println("Parsed: " + a);
		// if (true)
		// throw new RuntimeException();
	
		
    return a;
}
	
	public static Set<OWLAxiom> parseAxiomsFunctional(String str, OWLOntology ont){
		
		//System.out.println("Trying to parse: " + str);
		
		// OWLFunctionalSyntaxOWLParserFactory parserFactory = new OWLFunctionalSyntaxOWLParserFactory();
		OWLFunctionalSyntaxOWLParser parser = OWLAPIManagerManager.INSTANCE.getFunctionalSyntaxParser();
		
		
		OWLOntologyLoaderConfiguration loaderConfiguration = new OWLOntologyLoaderConfiguration();
		
		str = "Ontology(" + str + ")";
		
		
		// System.out.println("Trying to parse: " + str);
		
		InputStream in = new ByteArrayInputStream(str.getBytes());
		StreamDocumentSource streamSource = new StreamDocumentSource(in);
		
		// OWLOntologyDocumentSource toBeParsed = new OWLOntologyDocumentSource();
		
		OWLOntologyManager manager = OWLAPIManagerManager.INSTANCE.getOntologyManager();
		
		
		Set<OWLAxiom> axioms = null;
		
		try {
			
		manager.removeAxioms(tmpOntology, tmpOntology.getAxioms());
		
		OWLDocumentFormat format = parser.parse(streamSource, tmpOntology, loaderConfiguration);
		
		
			axioms = tmpOntology.getAxioms();
		} catch(Exception e){}
		
    return axioms;
	}
	
	public static int countRules(List<SequentInferenceRule> rules, String rulename){
		int i = 0;
		for (SequentInferenceRule rule : rules){
			// System.out.println(rule.getName());
			if (rule.getName().equals(rulename)){
				// System.out.println("hit");
				i++;
			}
		}
		return i;
}
	
}
