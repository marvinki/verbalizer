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
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
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
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

/*
[ {
	  "query" : ["getInstructionText",  "getVideo", "getImage"],
	  "task" : {
	    "name" : "Saw",
	    "text" : null,
	    "pictureURL" : null,
	    "videoURL" : null,
	    "actionParameter" : [ "saw-1", "raw", "back", "tray" ]
	  }
	},
	 {
	  "query" :  ["getInstructionText",  "getVideo", "getImage"],
	  "task" : {
	    "name" : "Drill_Screw",
	    "text" : null,
	    "pictureURL" : null,
	    "videoURL" : null,
	    "actionParameter" : [ "drill-1", "hanger-2", "a1", "back", "a4", "screw-4", "right" ]
	  }
	}
	]
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
	private OWLOntology inferredOntology = null;
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
	inferredOntology = infOnt;
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
	
	public String listAllNumbers(){
		System.out.println("list all numbers called");
		String results = "";
		Set<OWLAxiom>  axioms = ontology.getAxioms();
		Set<Long> donies = new HashSet<Long>();
		for (OWLAxiom ax : axioms){
			if (ax instanceof OWLDataPropertyAssertionAxiom){
				OWLDataPropertyAssertionAxiom datax =  (OWLDataPropertyAssertionAxiom) ax;
				String lit = datax.getObject().getLiteral();
				System.out.println("looking at " + lit);
				System.out.println("is number " + NumberUtils.isNumber(lit));
				System.out.println("contained " + donies.contains(NumberUtils.toLong(lit)));
				if (NumberUtils.isDigits(lit) && !donies.contains(NumberUtils.toLong(lit))){
					System.out.println("Dealing with number " + NumberUtils.toLong(lit));
				    String nlit = "n" + lit; 
					results = results + nlit + " ";
					donies.add(NumberUtils.toLong(lit));
				}
				
				
			}
		}
		
		System.out.println(results);
		return results;
	}
	
	public String reportConfigs(){
		JSONArray resultarray = new JSONArray();
		String results = "";
		Set<OWLAxiom>  axioms = ontology.getAxioms();
		axioms.addAll(inferredAxioms);
		boolean middle = false;
		
		Set<OWLIndividual> configs = new HashSet<OWLIndividual>();
		for (OWLAxiom ax : axioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom clax = (OWLClassAssertionAxiom) ax;
				if (clax.getClassExpression().toString().contains("Config")){
					configs.add(clax.getIndividual().asOWLNamedIndividual());
				}
			}
		}
		for (OWLIndividual configIndiv : configs){

					for (OWLAxiom innerax : axioms){
						if (innerax instanceof OWLClassAssertionAxiom){
						OWLClassAssertionAxiom clinnerax = (OWLClassAssertionAxiom) innerax;
						if (configIndiv.equals(clinnerax.getIndividual())){
							OWLClassExpression clinneraxclass = clinnerax.getClassExpression();
							if (clinneraxclass instanceof  
									OWLObjectSomeValuesFrom
									){
								OWLObjectSomeValuesFrom someclax = (OWLObjectSomeValuesFrom) clinneraxclass;
								if (someclax.getFiller().isAnonymous())
									continue;
								String fillerString = someclax.getFiller().asOWLClass().getIRI().getShortForm();
								String propString = someclax.getProperty().asOWLObjectProperty().getIRI().getShortForm();
								System.out.println("(" + propString + " " + configIndiv.asOWLNamedIndividual().getIRI().getFragment() + " " + fillerString + ")");
								JSONObject axJSON = new JSONObject();
								axJSON.put("predicate", propString);
								axJSON.put("arg1", configIndiv.asOWLNamedIndividual().getIRI().getFragment());
								axJSON.put("arg2", fillerString);
								resultarray.put(axJSON);
								// results += axJSON.toString();
							} // endif for some values from
							//System.out.println("clinnerax " + clinnerax);
						}
						} // endif for class assertions
						if (innerax instanceof OWLDataPropertyAssertionAxiom){
							OWLDataPropertyAssertionAxiom datax =  (OWLDataPropertyAssertionAxiom) innerax;
							if (datax.getSubject().equals(configIndiv)){
								OWLIndividual subject = datax.getSubject();
								String lit = datax.getObject().getLiteral();
								if (NumberUtils.isNumber(lit)){
									lit = "n" + lit; 
								}
								OWLDataPropertyExpression datprop = datax.getProperty();
								String propStr = datprop.asOWLDataProperty().getIRI().getShortForm();
								System.out.println("(" + propStr + " " + configIndiv.asOWLNamedIndividual().getIRI().getFragment() + " " + lit + ")");
								JSONObject axJSON = new JSONObject();
								axJSON.put("predicate", propStr);
								axJSON.put("arg1", configIndiv.asOWLNamedIndividual().getIRI().getFragment());
								axJSON.put("arg2", lit);
								resultarray.put(axJSON);
							}
							// System.out.println("dataprop " + innerax);
						}
					}
					// if (middle)
					// 	results += ",";
					// middle = true;
					// results += clax.getIndividual().asOWLNamedIndividual().getIRI().getShortForm();
				}
			// }
		// }
		return resultarray.toString();
		// return "[" + results + "]";
	}
	
	
	
	public String listAllMaterials(){
		String results = "";
		Set<OWLAxiom>  axioms = ontology.getAxioms();
		axioms.addAll(inferredAxioms);
		
		boolean middle = false;
		for (OWLAxiom ax : axioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom clax = (OWLClassAssertionAxiom) ax;
				if (clax.getClassExpression().toString().contains("MaterialsAndTools")){
					if (middle)
						results += ",";
					middle = true;
					results += clax.getIndividual().asOWLNamedIndividual().getIRI().getShortForm();
				}
			}
		}
		return "[" + results + "]";
	}
	
	public String getAllMaterialsAndTools(){
		String results = "";
		JSONArray resultsArray = new JSONArray();
		Set<OWLAxiom>  axioms = ontology.getAxioms();
		axioms.addAll(inferredAxioms);
		
		boolean middle = false;
		
		Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		
		// finding all individuals that represent materials and tools
		for (OWLAxiom ax : axioms){
			if (ax instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom clax = (OWLClassAssertionAxiom) ax;
				if (clax.getClassExpression().toString().contains("MaterialsAndTools")){
					individuals.add(clax.getIndividual().asOWLNamedIndividual());
					// if (middle)
					// 	results += ",";
					// middle = true;
					// results += clax.getIndividual().asOWLNamedIndividual().getIRI().getShortForm();
				}
			}
		}
		
		// finding all class assertions for each individual
		Set<OWLClassAssertionAxiom> claxioms = new HashSet<OWLClassAssertionAxiom>();
		for (OWLIndividual indiv : individuals){
			claxioms.addAll(ontology.getClassAssertionAxioms(indiv));
			claxioms.addAll(inferredOntology.getClassAssertionAxioms(indiv));
		}
		
		// now produce JSON from that
		for (OWLClassAssertionAxiom cla : claxioms){
			if (cla.getClassExpression().isOWLThing())
				continue;
			JSONObject ob = new JSONObject();
			ob.put("individual", cla.getIndividual().asOWLNamedIndividual().getIRI().getShortForm());
			if (!cla.getClassExpression().isAnonymous())
				ob.put("class", cla.getClassExpression().asOWLClass().getIRI().getShortForm());
			resultsArray.put(ob);
		}
		results = resultsArray.toString();
		return results;
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
			    				if (!subclax.getSuperClass().isAnonymous())
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
	
	public String getDataPropertyText(String query, String dataproperty){
		Set<OWLAxiom>  previousaxioms = ontology.getAxioms();
		String result = "";
		for (OWLAxiom ax : previousaxioms){
			if (ax instanceof OWLDataPropertyAssertionAxiom){
				System.out.println(ax);
				OWLDataPropertyAssertionAxiom dax = (OWLDataPropertyAssertionAxiom) ax;
				// System.out.println(dax.getProperty().asOWLDataProperty().getIRI());
				if (dax.getProperty().asOWLDataProperty().getIRI().toString().contains(dataproperty)
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
	
	/*
	public String getInstructionText(String query){
		return getDataPropertyText(query, "hasInstructionText");
	}
	*/
	
	public String getInstructionText(JSONObject obj){
		return getMostSpecificDataValue(obj, "hasInstructionText");
	}
	
	public String getVideoPath(JSONObject obj){
		return getMostSpecificDataValue(obj, "hasVideoPath");
	}
	
	public String getImagePath(JSONObject obj){
		return getMostSpecificDataValue(obj, "hasImagePath");
	}
	
	public String getMostSpecificDataValue(JSONObject obj, String property){
		List<OWLAxiom> memory = new ArrayList<OWLAxiom>();
		String actionName = obj.getString("name");
		System.out.println(actionName);
		JSONArray params = (JSONArray) obj.get("actionParameter");
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory2=manager.getOWLDataFactory();
		
		OWLIndividual new_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#action1"));
		OWLObjectProperty performsActivityProp = dataFactory2.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/powertools#instr_name"));
		OWLIndividual name_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#" + actionName));
		OWLObjectPropertyAssertionAxiom name_ax = dataFactory2.getOWLObjectPropertyAssertionAxiom(performsActivityProp,new_indiv, name_indiv);
		
		System.out.println(" assuming " + name_ax);;
		
		AddAxiom addAxiomAction= new AddAxiom(ontology,name_ax);
		manager.applyChange(addAxiomAction);
		memory.add(name_ax);
		
		int counter = 1;
		for (Object ob : params){
			String obString = (String) ob;
			// System.out.println("obString " + obString);
			OWLObjectProperty argProp = dataFactory2.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/powertools#instr_arg" + counter));
			OWLIndividual target_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#" + obString));
			OWLObjectPropertyAssertionAxiom ax = dataFactory2.getOWLObjectPropertyAssertionAxiom(argProp,new_indiv, target_indiv);
			AddAxiom addAxiomAction2= new AddAxiom(ontology,ax);
			manager.applyChange(addAxiomAction2);
			counter++;
			memory.add(ax);
			
			// System.out.println(" assuming " + ax);;
			
		}
		
		
		try {
			System.out.println("create empty ontology");
			OWLOntology infOnt = manager.createOntology();
		
		Set<OWLAxiom> previousaxioms = ontology.getAxioms();
		previousaxioms.addAll(inferredAxioms);
		System.out.println("Previous axioms " + previousaxioms.size());
		
		/*
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	    OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
	    reasoner = reasonerFactory.createReasoner(ontology, config);
	     */
	     
		SimpleConfiguration config = new SimpleConfiguration(50000);
		
		reasoner = reasonerFactory.createReasoner(ontology, config);
		
		System.out.println("create generator");
		InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
		System.out.println("fill");
		iog.fillOntology(dataFactory2, infOnt);
		System.out.println("done filling");
		// iog.fillOntology(outputOntologyManager, infOnt);
		Set<OWLAxiom> newaxioms = infOnt.getAxioms();
		
		newaxioms.removeAll(previousaxioms);
		
		System.out.println(newaxioms);
		
		System.out.println("ontology contains " + ontology.getAxioms().size() + "axioms");
		
		for (OWLAxiom delax : memory){
			RemoveAxiom removeAxiomAction= new RemoveAxiom(ontology,delax);
			manager.applyChange(removeAxiomAction);
		}
		
		/*
		for (OWLAxiom testaxiom : ontology.getAxioms()){
			System.out.println(testaxiom);
		}
		*/
		
		for (OWLAxiom axes : newaxioms){
			if (axes instanceof OWLClassAssertionAxiom){
				OWLClassAssertionAxiom cax = (OWLClassAssertionAxiom) axes;
				if (cax.getClassExpression().isOWLThing())
					continue;
				//  System.out.println(reasoner.getSubClasses(cax.getClassExpression(),true));
				if (reasoner.getSubClasses(cax.getClassExpression(),true).isBottomSingleton()){
					System.out.println("Now need to get stuff for " + cax.getClassExpression());
					
					System.out.println("ontology contains " + ontology.getAxioms().size() + "axioms");
					for (OWLAxiom axprobe : ontology.getAxioms()){
						System.out.println("examining " + axprobe);
						if (axprobe instanceof OWLSubClassOfAxiom && (((OWLSubClassOfAxiom) axprobe).getSubClass().equals(cax.getClassExpression()))){
							OWLSubClassOfAxiom subclAx = (OWLSubClassOfAxiom) axprobe;
							System.out.println(" interested in " + subclAx);
							if (subclAx.getSuperClass() instanceof OWLDataHasValue){
								OWLDataHasValue datahasvalue = (OWLDataHasValue) subclAx.getSuperClass();
								if (datahasvalue.getProperty().asOWLDataProperty().getIRI().getShortForm().equals(property)){
									return datahasvalue.getFiller().getLiteral();
								}
							}
						}
					}
					// break;
				}
			}
		}
		
		
		} catch (Exception e){
			System.out.println("ops, exception");
		}
		
		return "";
		
	}
	
	public List<String> getMostSpecificDataValue(List<JSONObject> objects, List<List<String>> properties){
		List<String> results = new ArrayList<String>();
		List<OWLAxiom> memory = new ArrayList<OWLAxiom>();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory2=manager.getOWLDataFactory();
		
		// big loop for actions
		for (int index = 0; index<objects.size(); index++){
			JSONObject obj = objects.get(index); 
			String actionName = obj.getString("name");
			JSONArray params = (JSONArray) obj.get("actionParameter");
		
			OWLIndividual new_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#action" + index));
			OWLObjectProperty performsActivityProp = dataFactory2.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/powertools#instr_name"));
			OWLIndividual name_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#" + actionName));
			OWLObjectPropertyAssertionAxiom name_ax = dataFactory2.getOWLObjectPropertyAssertionAxiom(performsActivityProp,new_indiv, name_indiv);
			System.out.println(" assuming " + name_ax);;
		
			AddAxiom addAxiomAction= new AddAxiom(ontology,name_ax);
			manager.applyChange(addAxiomAction);
			memory.add(name_ax);
		
			// loop for individual arguments
			int counter = 1;
			for (Object ob : params){
				String obString = (String) ob;
				// System.out.println("obString" + obString);
				OWLObjectProperty argProp = dataFactory2.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/powertools#instr_arg" + counter));
				OWLIndividual target_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#" + obString));
				OWLObjectPropertyAssertionAxiom ax = dataFactory2.getOWLObjectPropertyAssertionAxiom(argProp,new_indiv, target_indiv);
				AddAxiom addAxiomAction2= new AddAxiom(ontology,ax);
				manager.applyChange(addAxiomAction2);
				counter++;
				memory.add(ax);
				// System.out.println(" assuming " + ax);;
			}	
		} // assumptions done, now comes the reasoning!
		
		
		try {
			System.out.println("create empty ontology");
			OWLOntology infOnt = manager.createOntology();
		
		Set<OWLAxiom> previousaxioms = ontology.getAxioms();
		previousaxioms.addAll(inferredAxioms);
		// System.out.println("Previous axioms " + previousaxioms.size());
		
		// ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	    //  OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		SimpleConfiguration config = new SimpleConfiguration(50000);
		
	     reasoner = reasonerFactory.createReasoner(ontology, config);
		
		System.out.println("create generator");
		InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
		System.out.println("fill");
		iog.fillOntology(dataFactory2, infOnt);
		System.out.println("done filling");
		// iog.fillOntology(outputOntologyManager, infOnt);
		Set<OWLAxiom> newaxioms = infOnt.getAxioms();
		
		newaxioms.removeAll(previousaxioms);
		
		System.out.println(newaxioms);
		
		System.out.println("ontology contains " + ontology.getAxioms().size() + "axioms");
		
		for (OWLAxiom delax : memory){
			RemoveAxiom removeAxiomAction= new RemoveAxiom(ontology,delax);
			manager.applyChange(removeAxiomAction);
		}
		
		/*
		for (OWLAxiom testaxiom : ontology.getAxioms()){
			System.out.println(testaxiom);
		}
		*/
		
		// big loop for actions
		for (int index = 0; index<objects.size(); index++){
			OWLIndividual new_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#action" + index));
			for (OWLAxiom axes : newaxioms){
				if (axes instanceof OWLClassAssertionAxiom){
					OWLClassAssertionAxiom cax = (OWLClassAssertionAxiom) axes;
					if (cax.getClassExpression().isOWLThing())
						continue;
					if (!cax.getIndividual().equals(new_indiv))
						continue;
					//  System.out.println(reasoner.getSubClasses(cax.getClassExpression(),true));
					if (reasoner.getSubClasses(cax.getClassExpression(),true).isBottomSingleton()){
						// System.out.println("Now need to get stuff for " + cax.getClassExpression());
					
						// System.out.println("ontology contains " + ontology.getAxioms().size() + "axioms");
						
						//loop for properties
						for (String property : properties.get(index)){
							for (OWLAxiom axprobe : ontology.getAxioms()){
								// System.out.println("examining " + axprobe);
								if (axprobe instanceof OWLSubClassOfAxiom && (((OWLSubClassOfAxiom) axprobe).getSubClass().equals(cax.getClassExpression()))){
									OWLSubClassOfAxiom subclAx = (OWLSubClassOfAxiom) axprobe;
									   System.out.println(" interested in " + subclAx);
									if (subclAx.getSuperClass() instanceof OWLDataHasValue){
										OWLDataHasValue datahasvalue = (OWLDataHasValue) subclAx.getSuperClass();
										if (property.equals("getInstructionText")) property= "hasInstructionText";
										if (property.equals("getImage")) property= "hasImagePath";
										if (property.equals("getVideo")) property= "hasVideoPath";
										if (datahasvalue.getProperty().asOWLDataProperty().getIRI().getShortForm().equals(property)){
											results.add(datahasvalue.getFiller().getLiteral());
										}
									}
								}
							}
						}
						// break;
					}
			}
			}
		}
		
		
		
		} catch (Exception e){
			System.out.println("ops, exception");
		}
		
		System.out.println(" results " + results);
		
		return results;
		
	}
	
	
	
	
	public String getIllustrationURL(String query){
		return getDataPropertyText(query, "hasIllustrationURL");
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
	
	/*
	 * Wird von Gregor genutzt (STIMMT!)
	 */
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
			if (dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("hasImagePath")
					|| dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("hasVideoPath")
					|| dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("hasVoltage"))
			continue;
			if (dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("Instruction"))
				continue;
				// result += "(" + dAss.getProperty().asOWLDataProperty().getIRI().getFragment() 
				// + " " + dAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm() 
				// + " \"" + dAss.getObject().asLiteral().orNull().getLiteral() + "\""
		 // +       //")\n";
			else 
			if (dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("hasEnergy") && dAss.getObject().asLiteral().toString().contains("1")
				|| 	dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("unusable") && dAss.getObject().asLiteral().toString().contains("1")
				|| dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("usable") && dAss.getObject().asLiteral().toString().contains("1")
					)
				if (dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("unusable")
						|| dAss.getProperty().asOWLDataProperty().getIRI().getFragment().contains("usable")
						)
			result += "(" + dAss.getProperty().asOWLDataProperty().getIRI().getFragment() 
						+ " " + dAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm()
				+ ")\n"; 
				else
			result += "(" + dAss.getProperty().asOWLDataProperty().getIRI().getFragment() 
					+ " " + dAss.getSubject().asOWLNamedIndividual().getIRI().getShortForm()
			+ ")\n"; 
			else{
				OWLIndividual subject = dAss.getSubject();
				String lit = dAss.getObject().getLiteral();
				if (NumberUtils.isNumber(lit)){
					lit = "n" + lit; 
				}
				OWLDataPropertyExpression datprop = dAss.getProperty();
				if (datprop.asOWLDataProperty().getIRI().getShortForm().contains("hasIllustrationURL")) //<-- remove this for Gregor, slashes make his parser crash
						continue;
				if (datprop.asOWLDataProperty().getIRI().getShortForm().contains("hasInstructionText")) //<-- remove this for Gregor
				continue;
				String propStr = datprop.asOWLDataProperty().getIRI().getShortForm();
				result += "(" + propStr + " " + subject.asOWLNamedIndividual().getIRI().getShortForm() + " " + lit +")\n";
 			}
		}
		
	
		
		return result;
	}
	
	
	public String getImageIndividual(JSONObject ob){
		String individualString = ob.getString("getImageIndividual");
		return getPropertyIndividual(ob,"hasImagePath", individualString);
	}
	
	public String getVideoIndividual(JSONObject ob){
		String individualString = ob.getString("getVideoIndividual");
		return getPropertyIndividual(ob,"hasVideoPath", individualString);
	}
	
	public String getPropertyIndividual(JSONObject ob, String property, String individualString){
		System.out.println("prop: " + property + " indiv: " + individualString);
		
		Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
		
		OWLNamedIndividual targetIndividual = null;
		for (OWLNamedIndividual indiv: individuals){
			if (indiv.getIRI().getShortForm().equals(individualString)){
				targetIndividual = indiv;
			}
		}
		if (targetIndividual==null)
			return "{\"error\" : \"individual not found\"}";
		else 
			System.out.println("target individual:" +targetIndividual);
		Set<OWLClassAssertionAxiom> classAssertionAxioms = new HashSet<OWLClassAssertionAxiom>();
		Set<OWLDataPropertyAssertionAxiom> datapropertyAssertionAxioms = new HashSet<OWLDataPropertyAssertionAxiom>();
		classAssertionAxioms.addAll(ontology.getClassAssertionAxioms(targetIndividual));
		datapropertyAssertionAxioms.addAll(ontology.getDataPropertyAssertionAxioms(targetIndividual));
		
		OWLAxiom targetAxiom = null;
		
		for (OWLDataPropertyAssertionAxiom datax: datapropertyAssertionAxioms){
			System.out.println("assessing: " + datax);
			if (datax.getProperty().asOWLDataProperty().getIRI().getShortForm().equals(property)){
				JSONObject obj = new JSONObject();
				obj.put("individual", individualString);
				if (property.equals("hasVideoPath"))
					obj.put("videoURL", datax.getObject().getLiteral());
				else
					obj.put("imageURL", datax.getObject().getLiteral());
				return obj.toString();
			}
		}
		
		// if we get here, there is no image associated with an individual. Now we need to look at the class of the individual
		for (OWLClassAssertionAxiom  cla : classAssertionAxioms){
			if (cla.getClassExpression().isAnonymous())
				continue;
			OWLClass centralClass = cla.getClassExpression().asOWLClass();
			System.out.println("considering central class: " + centralClass);
			Set<OWLAxiom> axioms = ontology.getAxioms();
			for (OWLAxiom ax : axioms){
				if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSubClass().equals(centralClass)){
					OWLSubClassOfAxiom subclax = (OWLSubClassOfAxiom) ax;
					System.out.println(subclax.getSuperClass());
					if (subclax.getSuperClass() instanceof OWLDataHasValue){
						OWLDataHasValue dhv = (OWLDataHasValue) subclax.getSuperClass();
						if (dhv.getProperty().asOWLDataProperty().getIRI().getShortForm().equals(property)){
							JSONObject obj = new JSONObject();
							obj.put("individual", individualString);
							if (property.equals("hasVideoPath"))
								obj.put("videoURL", dhv.getFiller().getLiteral());
							else
								obj.put("imageURL", dhv.getFiller().getLiteral());
							return obj.toString();
						}
					}
					// 	OWLDataPropertyAssertionAxiom){
					// OWLDataPropertyAssertionAxiom dax = (OWLDataPropertyAssertionAxiom) ax;
					// if (dax.getProperty().asOWLDataProperty().getIRI().getShortForm().equals(property)){
					// 	return dax.getObject().getLiteral();
				// 	}
				}
			}
		}
		
		return "{\"error\" : \"no media\"}";
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
				result += "(hasType " + classAssertionAxiom.getIndividual().asOWLNamedIndividual().getIRI().getShortForm() 
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
		if (subclax.getSubClass().isAnonymous() || subclax.getSuperClass().isAnonymous())
			return result;
		String sub = subclax.getSubClass().asOWLClass().getIRI().getFragment().toString();
		String sup = subclax.getSuperClass().asOWLClass().getIRI().getFragment().toString();
		result.put("subclass", sub);
		result.put("superclass", sup);
		return result;
	}
	
	public String allExplanations(){
		String result = "";
		for (OWLAxiom infAx : inferredAxioms){
			if (infAx instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) infAx).getSuperClass().isOWLThing())
				continue;
			if (infAx instanceof OWLClassAssertionAxiom && ((OWLClassAssertionAxiom) infAx).getClassExpression().isOWLThing())
				continue;
			if (infAx instanceof OWLClassAssertionAxiom && ((OWLClassAssertionAxiom) infAx).getIndividual().toString().contains("round-hole"))
				continue;
			if (!((infAx instanceof OWLSubClassOfAxiom) ||  (infAx instanceof OWLClassAssertionAxiom)))
				continue;
			TextElementSequence sequence = VerbalisationManager.verbalizeAxiomAsSequence(infAx, reasoner, reasonerFactory, ontology,100, 1000, "OP",true,false);
			System.out.println(" Sequence " + sequence.toString() + "\n\n");	
			result = result + sequence.toString();
		}
		System.out.println("result " + result);
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
	
/* 	
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
*/
	
	public String handleBoschBatchRequest(String input, PrintStream printstream) throws IOException, OWLOntologyCreationException{
		JSONArray jsonArray = new JSONArray(input); 
		String result = "";
		boolean middle = false;
		printstream.print("[");
		
		List<JSONObject> inputs = new ArrayList<JSONObject>();
		List<List<String>> properties = new ArrayList<List<String>>();
		
		for (int i = 0; i < jsonArray.length();i++){
			JSONObject part = (JSONObject) jsonArray.get(i);
			inputs.add(part.getJSONObject("task"));
			JSONArray queryArray = part.getJSONArray("query");
			List<String> queryItems = new ArrayList<String>();
		  	for (int j = 0; j<queryArray.length();j++){
		  		queryItems.add(queryArray.getString(j));
		  	}
		  	properties.add(queryItems);
		}
		
		List<String> results = getMostSpecificDataValue(inputs,properties);
		
		int resind = 0;
		for (int y=0; y<inputs.size();y++){
			String name = inputs.get(y).getString("name");
			
			JSONObject newobject = new JSONObject();
			newobject.put("action",name);
			
			
			for (String prop : properties.get(y)){
				
				System.out.println("obtaining result for property " + prop);
				
				String propertylabel = prop;
				if (propertylabel.equals("getVideo"))
					propertylabel = "videoURL";
				if (propertylabel.equals("getImage"))
					propertylabel = "imageURL";
				if (propertylabel.equals("getInstructionText"))
					propertylabel = "text";
				// obtaining value
				String text = results.get(resind);
				resind++;
				newobject.put(propertylabel,text);
				
			}
			String res = newobject.toString();
			if (middle){
				result += ",";
				printstream.print(",");
				}
			printstream.print(res);
			middle = true;
			result += res;
			
		}
		
		
		
		printstream.print("]");
		return "[" + result + "]";
	}
	

public String handleBoschInstructionRequest(String jsonstring, PrintStream printstream){
	JSONObject jobject =  new JSONObject(jsonstring);
    JSONArray queryArray = jobject.getJSONArray("query");
  	List<String> queryItems = new ArrayList<String>();
  	for (int i = 0; i<queryArray.length();i++){
  		queryItems.add(queryArray.getString(i));
  	}
  	System.out.println(queryItems.toString());
  	
	return "foo";
}

public String describeVisual(JSONObject input){
	String toBeDescribed = input.getString("describeVisual");
	String result = "";
	JSONArray resultArray = new JSONArray();
	
	Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
	
	OWLNamedIndividual targetIndividual = null;
	for (OWLNamedIndividual indiv: individuals){
		if (indiv.getIRI().getShortForm().equals(toBeDescribed)){
			targetIndividual = indiv;
		}
	}
	
	Set<OWLClass> classes  = ontology.getClassesInSignature();
	OWLClass targetClass = null;
	for (OWLClass cls: classes){
		if (cls.getIRI().getShortForm().equals(toBeDescribed)){
			targetClass = cls;
		}
	}
	
	if (targetIndividual !=null || targetClass !=null){
		
		Set<OWLClassAxiom> axioms = new HashSet<OWLClassAxiom>();
		
		if (targetIndividual!=null){
		Set<OWLClassAssertionAxiom> classAssertionAxioms = ontology.getClassAssertionAxioms(targetIndividual);
		classAssertionAxioms.addAll(inferredOntology.getClassAssertionAxioms(targetIndividual));
		for (OWLClassAssertionAxiom cas : classAssertionAxioms){
			System.out.println("class assertion examined: " + cas);
			if (cas.getClassExpression().isAnonymous())
				continue;
			OWLClass classToBeDescribed = cas.getClassExpression().asOWLClass();
			axioms.addAll(ontology.getAxioms(classToBeDescribed));
			axioms.addAll(inferredOntology.getAxioms(classToBeDescribed));
		}
		} else { // we are talking about a class
			axioms.addAll(ontology.getAxioms(targetClass));
			axioms.addAll(inferredOntology.getAxioms(targetClass));
		}
	
		// System.out.println("REACHED CODE");
		for (OWLClassAxiom ax : axioms){
			System.out.println("got : " + ax);
		}
		
		// Set<OWLAxiom> potentiallyDescriptiveAxioms = new HashSet<OWLAxiom>();
		// for (OWLClassAxiom ax : axioms){
		// 	potentiallyDescriptiveAxioms.addAll(ontology.getAxioms(ax.get));
		// }
		
	
	for (OWLClassAxiom ax : axioms){
		if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSuperClass() instanceof OWLObjectSomeValuesFrom){
			OWLSubClassOfAxiom subclax = (OWLSubClassOfAxiom) ax;
			OWLObjectSomeValuesFrom someax = (OWLObjectSomeValuesFrom) subclax.getSuperClass();
			OWLPropertyExpression property = someax.getProperty();
			System.out.println("testing: " + VerbalisationManager.textualise(someax).toJSON().toString());
			if (VerbalisationManager.textualise(someax).toJSON().toString().contains("hat")){
				TextElementSequence seq = VerbalisationManager.textualise(ax);
				seq.makeUppercaseStart();
				
				seq.makeUppercaseStart();
				JSONArray arr1 = seq.toJSON();
				resultArray = concatenate(resultArray,arr1);
				resultArray.put(makeFullstop());
				
			}
			}
		}
	}
	result = resultArray.toString();
	
	System.out.println("describeVisual returns: " + result);
	return result;
}
	
	
	
	
public String describe(JSONObject input){
	String toBeDescribed = input.getString("describe");
	String result = "";
	JSONArray resultArray = new JSONArray();
	
	Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
	
	OWLNamedIndividual targetIndividual = null;
	for (OWLNamedIndividual indiv: individuals){
		if (indiv.getIRI().getShortForm().equals(toBeDescribed)){
			targetIndividual = indiv;
		}
	}
	
	Set<OWLClass> classes  = ontology.getClassesInSignature();
	OWLClass targetClass = null;
	for (OWLClass cls: classes){
		if (cls.getIRI().getShortForm().equals(toBeDescribed)){
			targetClass = cls;
		}
	}
	
	if (targetIndividual !=null || targetClass !=null){
		
		Set<OWLClassAxiom> axioms = null;
		
		if (targetIndividual!=null){
		Set<OWLClassAssertionAxiom> classAssertionAxioms = ontology.getClassAssertionAxioms(targetIndividual);
		// classAssertionAxioms.addAll(inferredOntology.getClassAssertionAxioms(targetIndividual));
		for (OWLClassAssertionAxiom cas : classAssertionAxioms){
			if (cas.getClassExpression().isAnonymous())
				continue;
			OWLClass classToBeDescribed = cas.getClassExpression().asOWLClass();
			axioms = ontology.getAxioms(classToBeDescribed);
			axioms.addAll(inferredOntology.getAxioms(classToBeDescribed));
		}
		} else {
			axioms = ontology.getAxioms(targetClass);
			axioms.addAll(inferredOntology.getAxioms(targetClass));
		}
			
		Set<OWLClassAxiom> simpleSubsumptions = new HashSet<OWLClassAxiom>();
		Set<OWLClassAxiom> usageSubsumptions = new HashSet<OWLClassAxiom>();
		Set<OWLClassAxiom> otherSubsumptions = new HashSet<OWLClassAxiom>();
		
		
			
			for (OWLClassAxiom ax : axioms){
				if (ax instanceof OWLSubClassOfAxiom && ((OWLSubClassOfAxiom) ax).getSuperClass().isClassExpressionLiteral())
				{
					// System.out.println("Simple: " + ax);
					simpleSubsumptions.add(ax);
				}
				else {
					if (ax instanceof OWLSubClassOfAxiom 
							// && ((OWLSubClassOfAxiom) ax).getSuperClass() instanceof OWLObjectSomeValuesFrom 
							&& VerbalisationManager.textualise(ax).toJSON().toString().contains("eignet")
							// &&  ((OWLObjectSomeValuesFrom) ((OWLSubClassOfAxiom) ax).getSuperClass()).getProperty().toString().contains("isSuitedFor")
							){
						// System.out.println("Useage: " + ax);
						usageSubsumptions.add(ax);
					}
					else {
						// System.out.println("Other: " + ax);
						// System.out.println(VerbalisationManager.textualise(ax).toJSON().toString());
						otherSubsumptions.add(ax);
					}
				}
			}
			
			for (OWLClassAxiom ax : simpleSubsumptions){
				TextElementSequence seq1 = VerbalisationManager.textualise(ax);
				seq1.makeUppercaseStart();
				JSONArray arr1 = seq1.toJSON();
				resultArray = concatenate(resultArray,arr1);
				resultArray.put(makeFullstop());
				
				// result += VerbalisationManager.germanGrammarify(seq1.toJSON().toString())
				// 		+ ". ";
			}
			
			for (OWLClassAxiom ax : usageSubsumptions){
				TextElementSequence seq1 = VerbalisationManager.textualise(ax);
				seq1.makeUppercaseStart();
				JSONArray arr1 = seq1.toJSON();
				resultArray = concatenate(resultArray,arr1);
				resultArray.put(makeFullstop());
				
				// result += VerbalisationManager.germanGrammarify(seq1.toJSON().toString())
				// 		+ ". ";
			}
			
			for (OWLClassAxiom ax : otherSubsumptions){
				TextElementSequence seq1 = VerbalisationManager.textualise(ax);
				seq1.makeUppercaseStart();
				JSONArray arr1 = seq1.toJSON();
				resultArray = concatenate(resultArray,arr1);
				resultArray.put(makeFullstop());
				
				// result += VerbalisationManager.germanGrammarify(seq1.toJSON().toString())
				// 		+ ". ";
			}
			
			
		}
	
	result = resultArray.toString();
	
	
	return result;
}

public static JSONArray concatenate(JSONArray arr1, JSONArray arr2){
	JSONArray arr3 = new JSONArray();
	for (Object ob1 : arr1){
		arr3.put(ob1);
	}
	for (Object ob1 : arr2){
		arr3.put(ob1);
	}
	return arr3;
}

public static JSONObject makeFullstop(){
	JSONObject fullstop = new JSONObject();	
	fullstop.put("text", ". ");	
	fullstop.put("type", "text");	
	return fullstop;
}

public String elaborate(JSONObject input){
	// String activity = input.getString("elaborate");
	Set<String> keys = input.keySet();
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	OWLDataFactory dataFactory2=manager.getOWLDataFactory();
	for (String key : keys){
		if (key.equals("elaborate")){
			OWLObjectProperty performsActivityProp = dataFactory2.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/powertools#performsActivity"));
			OWLIndividual new_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#event"));
			OWLIndividual target_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#" + input.get(key)));
			OWLObjectPropertyAssertionAxiom ax = dataFactory2.getOWLObjectPropertyAssertionAxiom(performsActivityProp,new_indiv, target_indiv);
			AddAxiom addAxiomAction= new AddAxiom(ontology,ax);
			manager.applyChange(addAxiomAction);
			System.out.println("adding + " + ax);
			continue;
		}
		// establish whether we are talking about an object or a data property
		Set<OWLDataProperty> dataproperties = ontology.getDataPropertiesInSignature();
		OWLDataProperty targetDataProperty = null;
		for (OWLDataProperty datprop : dataproperties){
			if (datprop.getIRI().getShortForm().equals(key)){
				targetDataProperty = datprop;
				break;
			}
		}
		if (targetDataProperty!=null){
			
			OWLIndividual new_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("IRI-foo#foo"));
			OWLIndividual target_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("IRI-foo#" + input.get(key)));
			// OWLDataPropertyAssertionAxiom ax = dataFactory2.getOWLObjectPropertyAssertionAxiom(arg0, arg1, arg2)  (new_indiv,targetDataProperty, target_indiv); 
		} else {// Object property
			Set<OWLObjectProperty> objectproperties = ontology.getObjectPropertiesInSignature();
			OWLObjectProperty targetObjectProperty = null;
			for (OWLObjectProperty obprop : objectproperties){
				if (obprop.getIRI().getShortForm().equals(key)){
					targetObjectProperty = obprop;
					break;
				}
			}
			
			OWLIndividual new_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#event"));
			OWLIndividual target_indiv = dataFactory2.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/powertools#" + input.get(key)));
			OWLObjectPropertyAssertionAxiom ax = dataFactory2.getOWLObjectPropertyAssertionAxiom(targetObjectProperty,new_indiv, target_indiv);
			AddAxiom addAxiomAction= new AddAxiom(ontology,ax);
			manager.applyChange(addAxiomAction);
			System.out.println("adding + " + ax);
		}
		
	}
	
	try {
		OWLOntology infOnt = manager.createOntology();
	
	Set<OWLAxiom> previousaxioms = ontology.getAxioms();
	previousaxioms.addAll(inferredAxioms);
	// System.out.println("Previous axioms " + previousaxioms.size());
	
	SimpleConfiguration configuration = new SimpleConfiguration(50000);
	reasoner = reasonerFactory.createReasoner(ontology, configuration);
	
	InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
	
	iog.fillOntology(dataFactory2, infOnt);
	// iog.fillOntology(outputOntologyManager, infOnt);
	Set<OWLAxiom> newaxioms = infOnt.getAxioms();
	
	newaxioms.removeAll(previousaxioms);
	
	System.out.println(newaxioms);
	
	JSONArray na = new JSONArray();
	
	for (OWLAxiom ax: newaxioms){
		if (ax instanceof OWLObjectPropertyAssertionAxiom){
			OWLObjectPropertyAssertionAxiom axio = (OWLObjectPropertyAssertionAxiom) ax;
			JSONObject jo = new JSONObject();
			jo.put("subject", axio.getSubject().asOWLNamedIndividual().getIRI().getShortForm());
			jo.put("relation", axio.getProperty().asOWLObjectProperty().getIRI().getShortForm());
			jo.put("object", axio.getObject().asOWLNamedIndividual().getIRI().getShortForm());
			na.put(jo);
		}
		if (ax instanceof OWLClassAssertionAxiom){
			OWLClassAssertionAxiom axio = (OWLClassAssertionAxiom) ax;
			if (axio.getClassExpression().isOWLThing())
				continue;
			if (axio.getClassExpression().isAnonymous())
				continue;
			JSONObject jo = new JSONObject();
			jo.put("individual", axio.getIndividual().asOWLNamedIndividual().getIRI().getShortForm());
			jo.put("relation", "has-type");
			jo.put("class", axio.getClassExpression().asOWLClass().getIRI().getShortForm());
			na.put(jo);
		}
		inferredAxioms.add(ax);
	}
	return na.toString();
	
	} catch (OWLOntologyCreationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return "";
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
	  	 	
	  	 
	  
	  	 
	  	if (inputObject.has("elaborate")){
	  		 String result = elaborate(inputObject);
	  		printstream.println(result);
	  		return result;
	  	}
	  	
	  	if (inputObject.has("describeVisual")){
	  		 String result = describeVisual(inputObject);
	  		printstream.println(result);
	  		return result;
	  	}
	  	
	  	if (inputObject.has("getVideoIndividual")){
	  		 String result = getVideoIndividual(inputObject);
	  		printstream.println(result);
	  		return result;
	  	}
	  	
	  	if (inputObject.has("getImageIndividual")){
	  		 String result = getImageIndividual(inputObject);
	  		printstream.println(result);
	  		return result;
	  	}
	  	
	  	if (inputObject.has("describe")){
	  		 String result = describe(inputObject);
	  		printstream.println(result);
	  		return result;
	  	}
	  	
	  	if (inputObject.has("allExplanations")){
	  		 String result = allExplanations();
	  		printstream.println(result);
	  		return result;
	  	}
	  	
	  	 
	  	 if (inputObject.has("query")){
	  		 if (inputObject.getString("query").equals("getInstructionText")){
	  			 String result = getInstructionText((JSONObject) inputObject.get("task"));
	  			 JSONObject resultJSON = new JSONObject();
	  			 resultJSON.put("action", ((JSONObject) inputObject.get("task")).getString("name"));
	  			 resultJSON.put("text", result);
	  			 System.out.println("resultJSON " + resultJSON);;
	  			printstream.println(resultJSON.toString());
	  			 return resultJSON.toString();
	  		 }
	  		if (inputObject.getString("query").equals("getVideo")){
	  			 String result = getVideoPath((JSONObject) inputObject.get("task"));
	  			 JSONObject resultJSON = new JSONObject();
	  			 resultJSON.put("action", ((JSONObject) inputObject.get("task")).getString("name"));
	  			 resultJSON.put("videoURL", result);
	  			 System.out.println("resultJSON " + resultJSON);;
	  			printstream.println(resultJSON.toString());
	  			 return resultJSON.toString();
	  		 }
	  		if (inputObject.getString("query").equals("getImage")){
	  			 String result = getImagePath((JSONObject) inputObject.get("task"));
	  			 JSONObject resultJSON = new JSONObject();
	  			 resultJSON.put("action", ((JSONObject) inputObject.get("task")).getString("name"));
	  			 resultJSON.put("imageURL", result);
	  			 System.out.println("resultJSON " + resultJSON);;
	  			printstream.println(resultJSON.toString());
	  			 return resultJSON.toString();
	  		 }
	  		if (inputObject.getString("query").equals("getIllustration")){
	  			 String result = getIllustrationURL(inputObject.getString("task"));
	  			 JSONObject resultJSON = new JSONObject();
	  			 resultJSON.put("action", inputObject.getString("task"));
	  			 resultJSON.put("illustration", result);
	  			printstream.println(resultJSON.toString());
	  			 return resultJSON.toString();
	  		 }
	  		
	  	 }
	  	 
	  	 String command = inputObject.getString("command");
	
	 	if (command.contains("reportConfigs")){
	  		 String result = reportConfigs();
	  		printstream.println(result);
	  		return result;
	  	}
	  	 
   		if (command.contains("precompute")){
				precomputeAxioms();
				return output;
		}
   		if (command.contains("listAllNumbers")){
   			String out = listAllNumbers();
   			printstream.println("{" + out + "}");
			return "{" + out + "}";
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
   		if (command.contains("listAllMaterials")){
			output = listAllMaterials();	
			printstream.println(output);
			return output;
	}
   		if (command.contains("getAllMaterialsAndTools")){
			output = getAllMaterialsAndTools();	
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
   		if (command.contains("listClassesBraced")){
			output = listClasses();	
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
   	   			 
   	   		/* SHORTCUT FOR DEMO */
    				if (command.contains("explainClassAssertion")){
    					Set<OWLAxiom>  axioms = ontology.getAxioms();
    					axioms.addAll(inferredAxioms);
    					
    				
    					
    					// classExp : to be explained class 
    					
    					for (OWLAxiom infax : axioms){
    						if (infax instanceof OWLClassAssertionAxiom){
    							OWLClassAssertionAxiom infaxCl = (OWLClassAssertionAxiom) infax;
    							if (infaxCl.getClassExpression().isTopEntity())
    								continue;
    							for (OWLAxiom testax : axioms){
    								if (testax instanceof OWLSubClassOfAxiom){
    									OWLSubClassOfAxiom testSub = (OWLSubClassOfAxiom) testax;
    									// OWLClassAssertionAxiom axiomCl = (OWLClassAssertionAxiom) axiom;
    									if (infaxCl.toString().contains("DrillingInSoftwood"))
    									System.out.println("checking " + testSub + " for : " + infaxCl);
    									if (testSub.getSubClass().equals(infaxCl.getClassExpression())
    											&& testSub.getSuperClass().equals(classExp)
    											){
    												axiom = testSub;
    												System.out.println("Explaining instead " + axiom);
    												break;
    									}
    								}
    							}
    						}	
    					}
    				}
    				
   	   			 
   	   			 
   	   				}
   				
   			 
   			 /*
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
   				*/
   				
   				
   				
   				
   				TextElementSequence sequence = VerbalisationManager.verbalizeAxiomAsSequence(axiom, reasoner, reasonerFactory, ontology,100, 10000, "OP",true,false);
   				
   				System.out.println("Sending explanation");
   				System.out.println(sequence.toString());
   				
   				JSONArray jsonObject = sequence.toJSON();
   				System.out.println("jsonObject: " + jsonObject);
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
