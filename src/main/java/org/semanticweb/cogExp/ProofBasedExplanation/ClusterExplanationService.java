package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.Dimension;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// import org.apache.log4j.spi.LoggerFactory;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

// import com.github.jabbalaci.graphviz.GraphViz;

// import ch.qos.logback.classic.Level;
// import ch.qos.logback.classic.Logger;

// import ch.qos.logback.classic.Logger;

// import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.*;

import uk.ac.manchester.cs.jfact.JFactFactory;

/*
 *  nc localhost 3113
 * {"command" : "explainSubclass", "subclass": "CoalTit", "superclass": "BirdRequiringSmallEntranceHole", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Ontologien/ornithology.owl"}
 * {"command" : "list", "className": "Tit", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Ontologien/ornithology.owl"}
 * {"command" : "listAll", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Ontologien/ornithology.owl"}
 *
 * Tools
 * {"command" : "listAll", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch-intern/ontologies/simple-tools.owl"}
 * {"command" : "explainSubclass", "subclass": "DrillingInWoodWithRotationalSpeedAbove1000Umin", "superclass": "ActivityPerformedIncorrectly", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch-intern/ontologies/simple-tools.owl"}
 * {"command" : "explainSubclass", "subclass": "DrillDriver", "superclass": "DIYPowerTool", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch-intern/ontologies/simple-tools.owl"}
 * {"command" : "explainSubclass", "subclass": "DrillingInWood", "superclass": "Drilling", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch-intern/ontologies/simple-tools.owl"}
 * {"command" : "explainClassAssertion", "class": "WoodDrillingBit", "individual": "woodDrillingBit7mm", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch-intern/ontologies/simple-tools.owl"}
 * {"command" : "explainSubclass", "subclass": "Spruce", "superclass": "Material", "ontologyName" : "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch-intern/ontologies/simple-tools.owl"}
 */


public class ClusterExplanationService {
	
	public ClusterExplanationService(OWLOntology ontology, OWLReasoner reasoner, OWLReasonerFactory reasonerFactory){
		this.ontology = ontology;
		this.reasonerFactory = reasonerFactory;
		this.reasoner = reasoner;
		
		// inferredAxioms = inferAxioms(ontology);
	}
	
	private static final String TEMP_PATH = "/tmp/graph.";
	
	private OWLReasoner reasoner = null;
	private OWLReasonerFactory reasonerFactory = null;
	private Set<OWLAxiom> inferredAxioms = new HashSet<OWLAxiom>();
	private OWLOntology ontology = null;
	private static String ontologyfile = "";
	
	public static void setOntologyfile(String ontfile){
		ontologyfile = ontfile;
	}

	/*
	private static File generateGraph(String dot, String path) {

		
        GraphViz gv = new GraphViz();
        
          gv.add(dot);
          String type = "png";
          String representationType="dot";
          System.out.println(path);
          File out = new File(path);   
          System.out.println(out);

          String getsource = gv.getDotSource();
          
          byte[] graph = gv.getGraph( gv.getDotSource(), type,  representationType);
          
      
        return out;

    }
    */
	
	private static File createGraph(File source, File out){
		Runtime rt = Runtime.getRuntime();

		// patch by Mike Chenault
		// representation type with -K argument by Olivier Duplouy
		String[] args = {"dot", "-Tpng", source.getAbsolutePath() ,"-o", out.getAbsolutePath()};
		// System.out.println(Arrays.toString(args));
		Process p;
		try {
			p = rt.exec(args);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	
	
	public Set<OWLAxiom> inferAxioms(OWLOntology ontology){
			System.out.println("Axiom count " + ontology.getAxiomCount());
			
		    
			// List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
			// gens.add(new InferredSubClassAxiomGenerator());
			
		    // Put the inferred axioms into a fresh empty ontology.
			Set<OWLAxiom> previousaxioms = ontology.getAxioms();
			// System.out.println("Previous axioms " + previousaxioms.size());
			InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			Set<OWLAxiom> newaxioms = new HashSet<OWLAxiom>();
			try {
				OWLOntology newontology = manager.createOntology();
			
			OWLDataFactory dataFactory2=manager.getOWLDataFactory();
			iog.fillOntology(dataFactory2, newontology);
			// iog.fillOntology(outputOntologyManager, infOnt);
			newaxioms = newontology.getAxioms();
			System.out.println("Newly inferred axioms: " + (newaxioms.size() - previousaxioms.size()));
			
			newaxioms.removeAll(previousaxioms);
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
			
			return newaxioms;
	}
	
	public void precomputeAxioms(){
		Set<OWLAxiom> newaxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> previousaxioms = new HashSet<OWLAxiom>();
		if (inferredAxioms.size()>0){
			System.out.println("[Using cached axioms]");
			newaxioms = inferredAxioms;
		} else{
		
   // OWLReasonerFactory reasonerFactory2 = new JFactFactory();
    // SimpleConfiguration configuration = new SimpleConfiguration(50000);
    // System.out.println("Ontology " + ontology);;
	// OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology);
	// List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
	// gens.add(new InferredSubClassAxiomGenerator());
	 
    // Put the inferred axioms into a fresh empty ontology.
    OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
	
    
    try {
		OWLOntology infOnt = outputOntologyManager.createOntology();
	
	previousaxioms = ontology.getAxioms();
	// System.out.println("Previous axioms " + previousaxioms.size());
	InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
	
	
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	OWLDataFactory dataFactory2=manager.getOWLDataFactory();
	iog.fillOntology(dataFactory2, infOnt);
	// iog.fillOntology(outputOntologyManager, infOnt);
	newaxioms = infOnt.getAxioms();
	} catch (OWLOntologyCreationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	newaxioms.removeAll(previousaxioms);
	}
	
	System.out.println("[Newly inferred axioms: " + newaxioms.size() + "]");
	
	inferredAxioms = newaxioms;
}
	
	public Set<OWLAxiom> getInferredAxioms(String ontologynameinput){
		System.out.println("[Inferred Axioms cached: " + inferredAxioms.size() + "]");
		System.out.println("[Retrieving list of superclasses]");
			// find classname
			String ontologyname =ontologynameinput.replaceAll("\"", "");
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			String result = "";
			try {
				
				if (ontology==null){
								
					java.io.File file = new java.io.File(ontologyname);
					FileDocumentSource source = new FileDocumentSource(file);
					OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
					loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
					loaderconfig = loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));
								
				
				ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
				System.out.println("[Done loading ontology]");
				} else{ 
					System.out.println("[Using preloaded ontology]");
					}
				
			Set<OWLAxiom> newaxioms;
			Set<OWLAxiom> previousaxioms = new HashSet<OWLAxiom>();
				
				if (inferredAxioms.size()>0){
					System.out.println("[Using cached axioms]");
					newaxioms = inferredAxioms;
				} else{
			 
		    // Put the inferred axioms into a fresh empty ontology.
		    OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
			OWLOntology infOnt = outputOntologyManager.createOntology();
			previousaxioms = ontology.getAxioms();
			// System.out.println("Previous axioms " + previousaxioms.size());
			InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
			
			OWLDataFactory dataFactory2=manager.getOWLDataFactory();
			iog.fillOntology(dataFactory2, ontology);
			// iog.fillOntology(outputOntologyManager, infOnt);
			newaxioms = ontology.getAxioms();
			newaxioms.removeAll(previousaxioms);
			}
			
			System.out.println("[Newly inferred axioms: " + newaxioms.size() + "]");
			
			inferredAxioms = newaxioms;
			System.out.println("[Inferred axioms cached: " + inferredAxioms.size()+ "]");
				
			HashSet<OWLClassExpression> superclasses = new HashSet<OWLClassExpression>();
				} catch (Exception e){
					e.printStackTrace();
				}
			System.out.println("Returning list: " + result);
		return inferredAxioms;
		
	}
	
	public String listClasses(){
		String result = "";
		Set<OWLClass> classes = ontology.getClassesInSignature();
		for (OWLClass cl : classes){
			result += cl.asOWLClass().getIRI().getFragment() + "\n";
		}
		return result;
	} 
	
	
	
	public String listClassesPanda(){
		String result = "";
		Set<OWLClass> classes = ontology.getClassesInSignature();
		for (OWLClass cl : classes){
			result += cl.asOWLClass().getIRI().getFragment() + " - Type\n";
		}
		return result;
	} 
	
	public String listIndividuals(){
		String result = "";
		Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
		for (OWLNamedIndividual ind : individuals){
			result += ind.getIRI().getShortForm() + "\n";
		}
		return result;
	} 
	
	
	public String listAllActions(){
		String results = "";
		Set<OWLAxiom>  axioms = ontology.getAxioms();
		boolean middle = false;
		for (OWLAxiom ax : axioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom clax = (OWLClassAssertionAxiom) ax;
				if (clax.getClassExpression().toString().contains("Activity")){
					if (middle)
						results += ",";
					middle = true;
					results += clax.getIndividual().asOWLNamedIndividual().getIRI().getShortForm();
				}
			}
		}
		return "[" + results + "]";
	}
	
	
	public String listAllAxioms(String ontologyname){
		System.out.println("[Inferred Axioms cached: " + inferredAxioms.size() + "]");
		System.out.println("[Retrieving list of superclasses]");
		String result = "";
			// find classname
			// String classnameString = input.get(0);
			// String ontologyname = input.get(1).replaceAll("\"", "");
			
			Set<OWLAxiom>  axioms = ontology.getAxioms();
			
			axioms.addAll(getInferredAxioms(ontologyname));
			
			for (OWLAxiom ax : axioms){
				System.out.println(VerbalisationManager.prettyPrint(ax));
			}
			
			return "foo";
			}
			
			
			
	
	public String listInferredAxioms(List<String> input){
		System.out.println("[Inferred Axioms cached: " + inferredAxioms.size() + "]");
		System.out.println("[Retrieving list of superclasses]");
		String result = "";
			// find classname
			String classnameString = input.get(0);
			String ontologyname = input.get(1).replaceAll("\"", "");
			
			Set<OWLAxiom>  previousaxioms = ontology.getAxioms();
			
			Set<OWLAxiom> inferredAxioms = getInferredAxioms(ontologyname);
			System.out.println("[Inferred axioms cached: " + inferredAxioms.size()+ "]");
				
			HashSet<OWLClassExpression> superclasses = new HashSet<OWLClassExpression>();
			
	        for (OWLAxiom ax: inferredAxioms){
			    	if (!previousaxioms.contains(ax)){
			    		if (ax instanceof OWLSubClassOfAxiom){
			    			// System.out.println("subcl " + ax);
			    			OWLSubClassOfAxiom subclax = (OWLSubClassOfAxiom) ax;
			    			if (subclax.getSubClass().toString().contains(classnameString) && !superclasses.contains(subclax.getSuperClass())){
			    				//  System.out.println(subclax.getSuperClass());
			    				result += subclax.getSuperClass().asOWLClass().getIRI().getFragment().toString() + " ";
			    				superclasses.add(subclax.getSuperClass());
			    			}
			    		}
			    		else{
			    			// System.out.println("sth else " + ax);
			    		}
			    	}
			    	else{
			    		// System.out.println("something else " + ax);
			    	}
			    }
				 
				
			System.out.println("Returning list: " + result);
		return result;
		
	}
	
	public JSONArray listInferredAxiomsJSON(String ontologyname){
		JSONArray result = new JSONArray();
		Set<OWLAxiom> inferredAxioms = getInferredAxioms(ontologyname);
		Set<OWLAxiom> filteredInferredAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredClassAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredObjectPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredDataPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		inferredAxioms.addAll(ontology.getAxioms());
		
		// filter out trivial axioms: (top)(x), x subclassof top
		for (OWLAxiom ax: inferredAxioms){
			if (ax instanceof OWLClassAssertionAxiom && ((OWLClassAssertionAxiom) ax).getClassExpression().isOWLThing())
				continue;
			if (ax instanceof OWLDeclarationAxiom)
				continue;
			if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing())
				continue;
			if (ax instanceof OWLAnnotationAssertionAxiom)
				continue;
			if (ax instanceof OWLSubObjectPropertyOfAxiom && ((OWLSubObjectPropertyOfAxiom) ax).getSuperProperty().isOWLTopObjectProperty())
				continue;
			if (ax instanceof OWLClassAssertionAxiom)
				filteredClassAssertionAxioms.add(ax);
			if (ax instanceof OWLObjectPropertyAssertionAxiom)
				filteredObjectPropertyAssertionAxioms.add(ax);
			if (ax instanceof OWLDataPropertyAssertionAxiom)
				filteredDataPropertyAssertionAxioms.add(ax);
			filteredInferredAxioms.add(ax);
		}
		
		
		System.out.println("[Sending " + filteredInferredAxioms.size() + " results]");
		
		for (OWLAxiom ax: filteredClassAssertionAxioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) ax;
				if  (classAssertionAxiom.getClassExpression().isAnonymous())
					continue;
				result.put(toJSON(classAssertionAxiom));
			}
		}
		
		for (OWLAxiom ax: filteredObjectPropertyAssertionAxioms){
			if (ax instanceof OWLObjectPropertyAssertionAxiom){
				OWLObjectPropertyAssertionAxiom propAss = (OWLObjectPropertyAssertionAxiom) ax;
				result.put(toJSON(propAss));
			}
		}
		
		
		
		for (OWLAxiom ax: filteredDataPropertyAssertionAxioms){
			OWLDataPropertyAssertionAxiom dAss = (OWLDataPropertyAssertionAxiom) ax;
			if (dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("Instruction"))
				result.put(toJSON(dAss));
			else 
			if (dAss.getObject().asLiteral().toString().contains("1"))
				result.put(toJSON(dAss));
		}
		return result;
	}
	
	public JSONArray listActivitiesJSON(String ontologyname){
		JSONArray result = new JSONArray();
		Set<OWLAxiom> inferredAxioms = getInferredAxioms(ontologyname);
		Set<OWLAxiom> filteredInferredAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredClassAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredObjectPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredDataPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		inferredAxioms.addAll(ontology.getAxioms());
		
		// filter out trivial axioms: (top)(x), x subclassof top
		for (OWLAxiom ax: inferredAxioms){
			if (ax instanceof OWLClassAssertionAxiom && ((OWLClassAssertionAxiom) ax).getClassExpression().isOWLThing())
				continue;
			if (ax instanceof OWLDeclarationAxiom)
				continue;
			if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing())
				continue;
			if (ax instanceof OWLAnnotationAssertionAxiom)
				continue;
			if (ax instanceof OWLSubObjectPropertyOfAxiom && ((OWLSubObjectPropertyOfAxiom) ax).getSuperProperty().isOWLTopObjectProperty())
				continue;
			if (ax instanceof OWLClassAssertionAxiom)
				filteredClassAssertionAxioms.add(ax);
			if (ax instanceof OWLObjectPropertyAssertionAxiom)
				filteredObjectPropertyAssertionAxioms.add(ax);
			if (ax instanceof OWLDataPropertyAssertionAxiom)
				filteredDataPropertyAssertionAxioms.add(ax);
			filteredInferredAxioms.add(ax);
		}
		
		
		System.out.println("[Sending " + filteredInferredAxioms.size() + " results]");
		
		for (OWLAxiom ax: filteredClassAssertionAxioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) ax;
				NodeSet<OWLClass> nodeset = reasoner.getSuperClasses(classAssertionAxiom.getClassExpression(), true);
				Set<OWLClass> flattened = nodeset.getFlattened();
				for (OWLClass c: flattened){
					if (c.getIRI().getFragment().contains("Activity"))
						result.put(toJSON(classAssertionAxiom));
				}
			}
		}
		
		/*
		for (OWLAxiom ax: filteredObjectPropertyAssertionAxioms){
			if (ax instanceof OWLObjectPropertyAssertionAxiom){
				OWLObjectPropertyAssertionAxiom propAss = (OWLObjectPropertyAssertionAxiom) ax;
				result.put(toJSON(propAss));
			}
		}
		*/
		
		for (OWLAxiom ax: filteredDataPropertyAssertionAxioms){
			OWLDataPropertyAssertionAxiom dAss = (OWLDataPropertyAssertionAxiom) ax;
			OWLIndividual indiv = dAss.getSubject();
			NodeSet<OWLClass> classes =  reasoner.getTypes(indiv.asOWLNamedIndividual(), true);
			for (OWLClass cl : classes.getFlattened()){
				System.out.println("class " +cl);
				if (cl.getIRI().getFragment().contains("Activity")){
					// System.out.println("yes!");
					result.put(toJSON(dAss));
				}
			NodeSet<OWLClass> nodeset = reasoner.getSuperClasses(cl, true);
			Set<OWLClass> flattened = nodeset.getFlattened();
			for (OWLClass c: flattened){
				if (c.getIRI().getFragment().contains("Activity")){
					// System.out.println("yes!");
					result.put(toJSON(dAss));
				}
			}
			}
		}
		return result;
	}
	
	public String getInstructionText(String query){
		Set<OWLAxiom>  previousaxioms = ontology.getAxioms();
		String result = "";
		for (OWLAxiom ax : previousaxioms){
			if (ax instanceof OWLDataPropertyAssertionAxiom){
				System.out.println(ax);
				OWLDataPropertyAssertionAxiom dax = (OWLDataPropertyAssertionAxiom) ax;
				// System.out.println(dax.getProperty().asOWLDataProperty().getIRI());
				if (dax.getProperty().asOWLDataProperty().getIRI().toString().contains("hasInstructionText")
						&& dax.getSubject().asOWLNamedIndividual().getIRI().getShortForm().equals(query)
						){
					result = dax.getObject().getLiteral().toString();
					break;
				}
			}
			
			// System.out.println(ax);
		}
		return result ;
	}
	
	public String listInferredAxioms(String ontologyname){
		String result = "";
		Set<OWLAxiom> inferredAxioms = getInferredAxioms(ontologyname);
		Set<OWLAxiom> filteredInferredAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredClassAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredObjectPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredDataPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		inferredAxioms.addAll(ontology.getAxioms());
		
		// filter out trivial axioms: (top)(x), x subclassof top
		for (OWLAxiom ax: inferredAxioms){
			if (ax instanceof OWLClassAssertionAxiom && ((OWLClassAssertionAxiom) ax).getClassExpression().isOWLThing())
				continue;
			if (ax instanceof OWLDeclarationAxiom)
				continue;
			if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing())
				continue;
			if (ax instanceof OWLAnnotationAssertionAxiom)
				continue;
			if (ax instanceof OWLSubObjectPropertyOfAxiom && ((OWLSubObjectPropertyOfAxiom) ax).getSuperProperty().isOWLTopObjectProperty())
				continue;
			if (ax instanceof OWLClassAssertionAxiom)
				filteredClassAssertionAxioms.add(ax);
			if (ax instanceof OWLObjectPropertyAssertionAxiom)
				filteredObjectPropertyAssertionAxioms.add(ax);
			if (ax instanceof OWLDataPropertyAssertionAxiom)
				filteredDataPropertyAssertionAxioms.add(ax);
			filteredInferredAxioms.add(ax);
		}
		
		
		System.out.println("[Sending " + filteredInferredAxioms.size() + " results]");
		
		for (OWLAxiom ax: filteredClassAssertionAxioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) ax;
				if  (classAssertionAxiom.getClassExpression().isAnonymous())
					continue;
				result += classAssertionAxiom.getIndividual().asOWLNamedIndividual().getIRI().getShortForm() 
						+ " - "
						+ classAssertionAxiom.getClassExpression().asOWLClass().getIRI().getShortForm() + "\n";
			}
		}
		
		for (OWLAxiom ax: filteredObjectPropertyAssertionAxioms){
			if (ax instanceof OWLObjectPropertyAssertionAxiom){
				OWLObjectPropertyAssertionAxiom propAss = (OWLObjectPropertyAssertionAxiom) ax;
				result += "(" + propAss.getProperty().asOWLObjectProperty().getIRI().getFragment()
						+ " " + propAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm()
						+ " " + propAss.getObject().asOWLNamedIndividual().getIRI().getShortForm() + ")" + "\n";
			}
		}
		
		/*
		for (OWLAxiom ax: filteredInferredAxioms){
			// Class Assertions
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) ax;
				if  (classAssertionAxiom.getClassExpression().isAnonymous())
					continue;
				result += classAssertionAxiom.getIndividual().asOWLNamedIndividual().getIRI().getShortForm() 
						+ " - "
						+ classAssertionAxiom.getClassExpression().asOWLClass().getIRI().getShortForm() + "\n";
			}
			if (ax instanceof OWLObjectPropertyAssertionAxiom){
				OWLObjectPropertyAssertionAxiom propAss = (OWLObjectPropertyAssertionAxiom) ax;
				result += "(" + propAss.getProperty().asOWLObjectProperty().getIRI().getFragment()
						+ " " + propAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm()
						+ " " + propAss.getObject().asOWLNamedIndividual().getIRI().getShortForm() + ")" + "\n";
			}
			
			
			//else 
			// 	result += "UNACCOUNTED : " + ax + "\n";
			
			
		} */
		
		for (OWLAxiom ax: filteredDataPropertyAssertionAxioms){
			OWLDataPropertyAssertionAxiom dAss = (OWLDataPropertyAssertionAxiom) ax;
			if (dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("Instruction"))
				result += "(" + dAss.getProperty().asOWLDataProperty().getIRI().getFragment() 
				+ " " + dAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm() 
				+ " " + dAss.getObject().asLiteral().orNull().getLiteral()
		+ ")\n";
			else 
			if (dAss.getObject().asLiteral().toString().contains("1"))
			result += "(" + dAss.getProperty().asOWLDataProperty().getIRI().getFragment() 
						+ " " + dAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm()
				+ ")\n";
		}
		
	
		
		return result;
	}
	
	public String listInferredAssertions(String ontologyname){
		String result = "";
		Set<OWLAxiom> inferredAxioms = getInferredAxioms(ontologyname);
		Set<OWLAxiom> filteredInferredAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredClassAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredObjectPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredDataPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		inferredAxioms.addAll(ontology.getAxioms());
		
		// filter out trivial axioms: (top)(x), x subclassof top
		for (OWLAxiom ax: inferredAxioms){
			if (ax instanceof OWLClassAssertionAxiom && ((OWLClassAssertionAxiom) ax).getClassExpression().isOWLThing())
				continue;
			if (ax instanceof OWLDeclarationAxiom)
				continue;
			if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing())
				continue;
			if (ax instanceof OWLAnnotationAssertionAxiom)
				continue;
			if (ax instanceof OWLSubObjectPropertyOfAxiom && ((OWLSubObjectPropertyOfAxiom) ax).getSuperProperty().isOWLTopObjectProperty())
				continue;
			if (ax instanceof OWLClassAssertionAxiom)
				filteredClassAssertionAxioms.add(ax);
			if (ax instanceof OWLObjectPropertyAssertionAxiom)
				filteredObjectPropertyAssertionAxioms.add(ax);
			if (ax instanceof OWLDataPropertyAssertionAxiom)
				filteredDataPropertyAssertionAxioms.add(ax);
			filteredInferredAxioms.add(ax);
		}
		
		
		
		
		for (OWLAxiom ax: filteredObjectPropertyAssertionAxioms){
			if (ax instanceof OWLObjectPropertyAssertionAxiom){
				OWLObjectPropertyAssertionAxiom propAss = (OWLObjectPropertyAssertionAxiom) ax;
				result += "(" + propAss.getProperty().asOWLObjectProperty().getIRI().getFragment()
						+ " " + propAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm()
						+ " " + propAss.getObject().asOWLNamedIndividual().getIRI().getShortForm() + ")" + "\n";
			}
		}
		
		
		
		for (OWLAxiom ax: filteredDataPropertyAssertionAxioms){
			OWLDataPropertyAssertionAxiom dAss = (OWLDataPropertyAssertionAxiom) ax;
			if (dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("Instruction"))
				continue;
				// result += "(" + dAss.getProperty().asOWLDataProperty().getIRI().getFragment() 
				// + " " + dAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm() 
				// + " \"" + dAss.getObject().asLiteral().orNull().getLiteral() + "\""
		 // +       //")\n";
			else 
			if (dAss.getObject().asLiteral().toString().contains("1"))
			result += "(" + dAss.getProperty().asOWLDataProperty().getIRI().getFragment() 
						+ " " + dAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm()
				+ ")\n";
		}
		
	
		
		return result;
	}
	
	
	public String listInferredClassAssertions(String ontologyname){
		String result = "";
		Set<OWLAxiom> inferredAxioms = getInferredAxioms(ontologyname);
		Set<OWLAxiom> filteredInferredAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredClassAssertionAxioms = new HashSet<OWLAxiom>();
		// Set<OWLAxiom> filteredObjectPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		// Set<OWLAxiom> filteredDataPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		inferredAxioms.addAll(ontology.getAxioms());
		
		// filter out trivial axioms: (top)(x), x subclassof top
		for (OWLAxiom ax: inferredAxioms){
			if (ax instanceof OWLClassAssertionAxiom)
				filteredClassAssertionAxioms.add(ax);
		}
		
		
		System.out.println("[Sending " + filteredInferredAxioms.size() + " results]");
		
		for (OWLAxiom ax: filteredClassAssertionAxioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) ax;
				if  (classAssertionAxiom.getClassExpression().isAnonymous())
					continue;
				if (classAssertionAxiom.getClassExpression().isOWLThing())
					continue;
				result += classAssertionAxiom.getIndividual().asOWLNamedIndividual().getIRI().getShortForm() 
						+ " - "
						+ classAssertionAxiom.getClassExpression().asOWLClass().getIRI().getShortForm() + "\n";
			}
		}
		
		return result;
	}
	
	public String listInferredClassAssertions2(String ontologyname){
		String result = "";
		Set<OWLAxiom> inferredAxioms = getInferredAxioms(ontologyname);
		Set<OWLAxiom> filteredInferredAxioms = new HashSet<OWLAxiom>();
		Set<OWLAxiom> filteredClassAssertionAxioms = new HashSet<OWLAxiom>();
		// Set<OWLAxiom> filteredObjectPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		// Set<OWLAxiom> filteredDataPropertyAssertionAxioms = new HashSet<OWLAxiom>();
		inferredAxioms.addAll(ontology.getAxioms());
		
		// filter out trivial axioms: (top)(x), x subclassof top
		for (OWLAxiom ax: inferredAxioms){
			if (ax instanceof OWLClassAssertionAxiom)
				filteredClassAssertionAxioms.add(ax);
		}
		
		
		System.out.println("[Sending " + filteredInferredAxioms.size() + " results]");
		
		for (OWLAxiom ax: filteredClassAssertionAxioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) ax;
				if  (classAssertionAxiom.getClassExpression().isAnonymous())
					continue;
				if (classAssertionAxiom.getClassExpression().isOWLThing())
					continue;
				result += "(TypeOf " + classAssertionAxiom.getIndividual().asOWLNamedIndividual().getIRI().getShortForm() 
						+ " "
						+ classAssertionAxiom.getClassExpression().asOWLClass().getIRI().getShortForm() + ")\n";
			}
		}
		
		return result;
	}
	
	public static JSONObject toJSON(OWLClassAssertionAxiom classAssertionAxiom){
	 	JSONObject result = new JSONObject();
	 	String individual = classAssertionAxiom.getIndividual().asOWLNamedIndividual().getIRI().getShortForm(); 
	 	String classexpression = classAssertionAxiom.getClassExpression().asOWLClass().getIRI().getShortForm();
	 	result.put("class", classexpression);
	 	result.put("individual", individual);
	 	return result;
	} 
	
	public static JSONObject toJSON(OWLDataPropertyAssertionAxiom propAss){
		JSONObject result = new JSONObject();
		String property = propAss.getProperty().asOWLDataProperty().getIRI().getFragment();
		String subject =  propAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm();
		String object = propAss.getObject().getLiteral();
		result.put("individual", subject);
		result.put("property", property);
		result.put("value", object);
		return result;
	}
	
	public static JSONObject toJSON(OWLObjectPropertyAssertionAxiom propAss){
		JSONObject result = new JSONObject();
		String property = propAss.getProperty().asOWLObjectProperty().getIRI().getFragment();
		String subject =  propAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm();
		String object = propAss.getObject().asOWLNamedIndividual().getIRI().getShortForm();
		result.put("subject", subject);
		result.put("property", property);
		result.put("object", object);
		return result;
	}
	
	public static JSONObject toJSON(OWLSubClassOfAxiom subclax){
		JSONObject result = new JSONObject();
		String sub = subclax.getSubClass().asOWLClass().getIRI().getFragment().toString();
		String sup = subclax.getSuperClass().asOWLClass().getIRI().getFragment().toString();
		result.put("subclass", sub);
		result.put("superclass", sup);
		return result;
	}
	
	
	public static void handleDotRequest(String dotTree){
		try{
		// Temporary file for graphics
			String property = "java.io.tmpdir";
		    String tempDir = System.getProperty(property);
		    File newTempfile = new File(tempDir + File.separator + "graph.png");
		    
		    Path dotpath = Paths.get(tempDir + File.separator + "graph.dot");
		    File dotfile = new File(dotpath.toString());
		    System.out.println("Dotfile generated in: " + dotfile);
		    if(!dotfile.exists()){
				dotfile.createNewFile();
			}
		    Writer dotWriter = new PrintWriter(new OutputStreamWriter(
		    	    new FileOutputStream(dotfile.toString()), "UTF-8"));
		    
		    // PrintWriter dotWriter = new PrintWriter(dotfile.toString());
		    dotWriter.write(dotTree);
		    dotWriter.close();
		    
		    // Temporary file for png
		    Path pngpath = Paths.get(tempDir + File.separator + "graph.png");
		    File pngfile = new File(pngpath.toString());
		    if(!pngfile.exists()){
				pngfile.createNewFile();
			}
		    
		    // make the external call
		    createGraph(dotfile,pngfile);
		      
		   // System.out.println("pngfile: " + pngpath.toString());
		    
		    // Image_Panel panel = new Image_Panel(pngfile.toString());
		    System.out.println("Trying to access png image at path: " + pngfile.getPath());
		    Image_Panel panel = new Image_Panel(pngfile.getPath());
	        ImageZoom zoom = new ImageZoom(panel,panel.getScale());
	        JFrame f = new JFrame();
	        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        f.getContentPane().add(zoom.getUIPanel(), "North");
	        f.getContentPane().add(new JScrollPane(panel));
	        int initHeight = (int) (panel.getPreferredSize().getHeight() * 1.2);
	        int initWidth = (int) (panel.getPreferredSize().getWidth() * 1.2);
	        // System.out.println(panel.getPreferredSize().getHeight());
	        f.setSize(initWidth,initHeight);
	        f.setLocation(200,200);
	        f.setVisible(true);
		} catch (Exception e){
			e.printStackTrace();
		}
		return;
		
	}
	
	
public String handleBoschBatchRequest(String input, PrintStream printstream) throws IOException, OWLOntologyCreationException{
	String result = "";
	boolean middle = false;
	JSONArray jsonArray = new JSONArray(input); 
	printstream.print("[");
	for (int i = 0; i < jsonArray.length();i++){
		JSONObject part = (JSONObject) jsonArray.get(i);
		if (middle){
			result += ",";
			printstream.print(",");
			}
		String res = handleBoschRequest(part.toString(), printstream);
		middle = true;
		result += res;
	}
	printstream.print("]");
	return "[" + result + "]";
}	
	
	
public String handleBoschRequest(String input, PrintStream printstream) throws IOException, OWLOntologyCreationException {
			
		
	
		String output = "";
		// Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	    // rootlogger.setLevel(Level.OFF);
	  	OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
	  	
	  	Object json = new JSONTokener(input).nextValue();
	  	if (json instanceof JSONArray){
	  		return handleBoschBatchRequest(input, printstream);
	  	}
	  	
	  	 JSONObject inputObject = new JSONObject(input);
	  	 
	  	 if (inputObject.has("query")){
	  		 if (inputObject.getString("query").equals("getInstructionText")){
	  			 String result = getInstructionText(inputObject.getString("task"));
	  			 JSONObject resultJSON = new JSONObject();
	  			 resultJSON.put(inputObject.getString("task"), result);
	  			printstream.println(resultJSON.toString());
	  			 return resultJSON.toString();
	  		 }
	  		
	  	 }
	  	 
	  	 String command = inputObject.getString("command");
	
   		if (command.contains("precompute")){
				precomputeAxioms();
				return output;
		}
   		if (command.contains("listInferredAssertions")){
			String ontname;
			if (inputObject.has("ontologyName"))
				ontname = 	inputObject.getString("ontologyName");
			else
				ontname = ontologyfile;
			output = listInferredAssertions( ontname);	
			printstream.println("{" + output + "}") ;
			return output;
	}
   		if (command.contains("listActivitiesJSON")){
			String ontname;
			if (inputObject.has("ontologyName"))
				ontname = 	inputObject.getString("ontologyName");
			else
				ontname = ontologyfile;
			output = listActivitiesJSON(ontname).toString();	
			printstream.println(output);
			return output;
	}
   		
   		if (command.contains("listAllAxioms")){
			String ontname;
			if (inputObject.has("ontologyName"))
				ontname = 	inputObject.getString("ontologyName");
			else
				ontname = ontologyfile;
			output = listAllAxioms(ontname);	
			printstream.println(output);
			return output;
	}
   		
   		if (command.contains("listAllJSON")){
			String ontname;
			if (inputObject.has("ontologyName"))
				ontname = 	inputObject.getString("ontologyName");
			else
				ontname = ontologyfile;
			output = listInferredAxiomsJSON(ontname).toString();	
			printstream.println(output);
			return output;
	}
   		if (command.contains("listAllActions")){
			output = listAllActions();	
			printstream.println(output);
			return output;
	}
   		if (command.contains("listAll")){
				String ontname;
				if (inputObject.has("ontologyName"))
					ontname = 	inputObject.getString("ontologyName");
				else
					ontname = ontologyfile;
				output = listInferredAxioms(ontname);	
				printstream.println(output);
				return output;
		}
   		if (command.contains("listClassAssertions2")){
			String ontname;
			if (inputObject.has("ontologyName"))
				ontname = 	inputObject.getString("ontologyName");
			else
				ontname = ontologyfile;
			output = listInferredClassAssertions2(ontname);	
			printstream.println("{" + output + "}");
			return output;
	}
   		if (command.contains("listClassAssertions")){
			String ontname;
			if (inputObject.has("ontologyName"))
				ontname = 	inputObject.getString("ontologyName");
			else
				ontname = ontologyfile;
			output = listInferredClassAssertions(ontname);	
			printstream.println("{" + output + "}");
			return output;
	}
   		if (command.contains("listClassesPanda")){
			output = listClassesPanda();	
			printstream.println("{" + output + "}");
			return output;
	}
   		if (command.contains("listClasses")){
			output = listClasses();	
			printstream.println(output);
			return output;
	}
   		
   		if (command.contains("listIndividuals")){
			output = listIndividuals();	
			printstream.println(output);
			return output;
	}
   		if (command.contains("list")){
   				List strlist = new ArrayList<String>();
   				strlist.add(inputObject.getString("className"));
   				strlist.add(inputObject.getString("ontologyName"));
   				output = listInferredAxioms(strlist);	
   				printstream.println(output);
   				return output;
   		}
   		
   	 OWLOntology ont = ontology;
   		if (inputObject.has("ontologyName")){
   			String ontologyname = inputObject.getString("ontologyName");
   		    // String ontologyname = list.get(2).replaceAll("\"", "");
   		    if (ontology!=null)
   		    	ont = ontology;
   		    else{
   		    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
   				java.io.File file = new java.io.File(ontologyname);
   				FileDocumentSource source = new FileDocumentSource(file);
   				OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
   				loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
   				loaderconfig = loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));
   						
   				try {
   					ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
   				} catch (Exception e){
   					System.out.println("error loading ontology");
   				}
   				ont = ontology;
   		    }
   		}
   		 System.out.println("[Start proof search.]");
   			if (command.contains("explainSubclass") || command.contains("explainClassAssertion")){
   				
   				OWLAxiom axiom = null;
   				
   				if (command.contains("explainSubclass")){
   				
   				String subclass = inputObject.getString("subclass");
   				String superclass = inputObject.getString("superclass");
   				
   			 OWLClass subcl = null;
   			 OWLClass supercl = null;	 
   			 Set<OWLClass> classes = ontology.getClassesInSignature();
   			 for (OWLClass cl : classes){
   				 // System.out.println(cl.toString());
   				 if (cl.getIRI().getFragment().equals(subclass)){
   					 subcl = cl;
   				 }
   				 if (cl.getIRI().getFragment().equals(superclass)){
   					 supercl = cl;
   				 }
   			 }
   			 
   			 if (subcl==null)
   				 System.out.println("Class not found in ontology: " + subclass);
   			 if (supercl==null)
   				 System.out.println("Class not found in ontology: " + superclass);
   			 if (subcl==null || supercl==null){
   				 return null;
   			 }  			 		 
   			
   			 axiom = dataFactory.getOWLSubClassOfAxiom(subcl, supercl);
   				}
   				
   				if (command.contains("explainClassAssertion")){
   	   				
   	   				String classExpression = inputObject.getString("class");
   	   				String individual = inputObject.getString("individual");
   	   				
   	   			 OWLClass classExp = null; 
   	   			 Set<OWLClass> classes = ontology.getClassesInSignature();
   	   			 for (OWLClass cl : classes){
   	   				 // System.out.println(cl.toString());
   	   				 if (cl.getIRI().getFragment().equals(classExpression)){
   	   					 classExp = cl;
   	   				 }
   	   				 
   	   			 }
   	   			 
   	   			 Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
   	   			 
   	   			 OWLNamedIndividual indivExp = null; 
	   			
	   			 for (OWLNamedIndividual cl : individuals){
	   				 // System.out.println(cl.toString());
	   				 if (cl.getIRI().getFragment().equals(individual)){
	   					 indivExp = cl;
	   				 }
	   				 
	   			 }
   	   			 
   	   			 if (classExp==null)
   	   				 System.out.println("Class assertion explanation: Class not found in ontology: " + classExp);
   	   			 if (indivExp==null)
   	   				 System.out.println("Class assertion explanation: Individual not found in ontology: " + indivExp);
   	   			 if (classExpression==null || indivExp==null){
   	   				 return null;
   	   			 }  			 		 
   	   			
   	   			 axiom = dataFactory.getOWLClassAssertionAxiom(classExp, indivExp);
   	   				}
   				
   			 
   			 
   				GentzenTree tree = VerbalisationManager.computeGentzenTree(axiom, 
   						reasoner, 
   						reasonerFactory, 
   						ont, 
   						500000,
   						600000,
   						"OP");	
   				
   				
   				if (tree==null){
   					System.out.println("ERROR. Axiom could not be proven.");
   					printstream.println("ERROR. Axiom could not be proven.");
   					return "ERROR";
   				}
   				
   				TextElementSequence sequence = VerbalisationManager.verbalizeAxiomAsSequence(axiom, reasoner, reasonerFactory, ontology,100, 10000, "OP",true,false);
   				
   				System.out.println("Sending explanation");
   				System.out.println(sequence.toString());
   				
   				JSONArray jsonObject = sequence.toJSON();
   				output = jsonObject.toString();
   				printstream.println(output);
   				// System.out.println(result);
   			} else {
   				System.out.println("Please enter two class expressions, the path to the ontology, and a path for the output");
   			}	
   	return output;
   	
   }
	
	
	public String handleCluster1Request(String input, PrintStream printstream) throws IOException, OWLOntologyCreationException {
		
		// System.out.println("[Handle Request called.]");
		
		String output = "";
		
		 // Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	     // rootlogger.setLevel(Level.OFF);
		//  OWLReasonerFactory reasonerFactory = new JFactFactory();
	  	 OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
	  	
	  	 JSONObject inputObject = new JSONObject(input);
	  	 String command = inputObject.getString("command");
	
   			// String[] inputs = temp.split(" ");
   		if (command.contains("precompute")){
				precomputeAxioms();
				return output;
		}
   		
   		List<String> list = new ArrayList<String>();
   		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
   		while (m.find())
   			   list.add(m.group(1));
   		if (list.size()==1 && list.get(0).contains("precompute")){
				precomputeAxioms();
				return output;
		}
   		
   		if (list.size()==3 && list.get(0).contains("list")){
   				output = listInferredAxioms(list);	
   				printstream.println(output);
   				return output;
   		}
   		    String ontologyname = list.get(2).replaceAll("\"", "");
   		    OWLOntology ont;
   		    if (ontology!=null)
   		    	ont = ontology;
   		    else{
   		    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
   				java.io.File file = new java.io.File(ontologyname);
   				FileDocumentSource source = new FileDocumentSource(file);
   				OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
   				loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
   				loaderconfig = loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));
   						
   				try {
   					ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
   				} catch (Exception e){
   					System.out.println("error loading ontology");
   				}
   				ont = ontology;
   		    }
   		
   		 System.out.println("[Start proof search.]");
   			if (list.size()>3){
   				/*
   				GentzenTree tree = ProofBasedExplanationService.computeTree(list.get(0), 
   																			list.get(1),
   																			ont);
   				
   																			*
   																			*/
   				
   				String subclass = list.get(0);
   				String superclass = list.get(1);
   				
   			 OWLClass subcl = null;
   			 OWLClass supercl = null;
   			 
   			 Set<OWLClass> classes = ontology.getClassesInSignature();
   			 for (OWLClass cl : classes){
   				 // System.out.println(cl.toString());
   				 if (cl.getIRI().getFragment().equals(subclass)){
   					 subcl = cl;
   				 }
   				 if (cl.getIRI().getFragment().equals(superclass)){
   					 supercl = cl;
   				 }
   			 }
   			 
   			 if (subcl==null)
   				 System.out.println("Class not found in ontology: " + subclass);
   			 
   			 if (supercl==null)
   				 System.out.println("Class not found in ontology: " + superclass);
 
   			 if (subcl==null || supercl==null){
   				 return null;
   			 }
   			 
   			 
   			 
   			 OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(subcl, supercl);
   				GentzenTree tree = VerbalisationManager.computeGentzenTree(axiom, 
   						reasoner, 
   						reasonerFactory, 
   						ont, 
   						50000,
   						60000,
   						"OP");	
   				
   				
   				if (tree==null){
   					System.out.println("ERROR. Subsumption could not be proven.");
   					printstream.println("ERROR. Subsumption could not be proven.");
   					return "ERROR";
   				}
   				// System.out.println(tree);
   				// System.out.println(VerbaliseTreeManager.listOutput(tree));
   				// System.out.println(tree.getStepsInOrder());
   				// System.out.println(tree.computePresentationOrder());
   				long startVerbalising = System.currentTimeMillis();
   				String result = VerbaliseTreeManager.verbaliseNL(tree, false, true,false, null); // <-- 2nd arg labels, 3rd arg html
   				printstream.println(result);
   				String resultPlain = VerbaliseTreeManager.verbaliseNL(tree, false, false,false, null); // <-- 2nd arg labels, 3rd arg html
   				long endVerbalising = System.currentTimeMillis();
   				System.out.println("Verbalisation took: " + (endVerbalising - startVerbalising) + "ms");
   				String dotTree = tree.toDOT();
   				
   				handleDotRequest(dotTree);
   					
   				PrintWriter htmlwriter = new PrintWriter(list.get(3) + "/text.html", "UTF-8");
   				htmlwriter.write(result);
   				htmlwriter.close();
   				
   				PrintWriter plainwriter = new PrintWriter(list.get(3) + "/plaintext.txt", "UTF-8");
   				plainwriter.write(resultPlain);
   				plainwriter.close();
   				output = result;
   				
   				System.out.println(result);
   			} else {
   				System.out.println("Please enter two class expressions, the path to the ontology, and a path for the output");
   			}	
   	return output;
   	
   }
	
	
	
}