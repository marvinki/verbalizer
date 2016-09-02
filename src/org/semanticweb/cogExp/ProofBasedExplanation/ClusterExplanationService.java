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
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
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

import com.github.jabbalaci.graphviz.GraphViz;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

// import ch.qos.logback.classic.Logger;

// import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import uk.ac.manchester.cs.jfact.JFactFactory;

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
	
	public String listInferredAxioms(List<String> input){
		System.out.println("[Inferred Axioms cached: " + inferredAxioms.size() + "]");
		System.out.println("[Retrieving list of superclasses]");
			// find classname
			String classnameString = input.get(1);
			String ontologyname = input.get(2).replaceAll("\"", "");
			
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
	   				
				// OWLClass classname = null;
			
				/* 
			 Set<OWLClass> classes = ontology.getClassesInSignature();
				for (OWLClass cl : classes){
				 // System.out.println(cl.toString());
				 if (cl.getIRI().getFragment().equals(classnameString)){
					 classname = cl;
					}
			 }
				 
			System.out.println("[identified class]: " + classname);
				*/
				
			Set<OWLAxiom> newaxioms;
			Set<OWLAxiom> previousaxioms = new HashSet<OWLAxiom>();
				
				if (inferredAxioms.size()>0){
					System.out.println("[Using cached axioms]");
					newaxioms = inferredAxioms;
				} else{
				
		    // OWLReasonerFactory reasonerFactory2 = new JFactFactory();
		    // SimpleConfiguration configuration = new SimpleConfiguration(50000);
			// OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology,configuration);
			// List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
			// gens.add(new InferredSubClassAxiomGenerator());
			 
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
				
	        for (OWLAxiom ax: newaxioms){
			    	if (!previousaxioms.contains(ax)){
			    		if (ax instanceof OWLSubClassOfAxiom){
			    			// System.out.println("subcl " + ax);
			    			OWLSubClassOfAxiom subclax = (OWLSubClassOfAxiom) ax;
			    			if (subclax.getSubClass().toString().contains(classnameString)){
			    				//  System.out.println(subclax.getSuperClass());
			    				result += subclax.getSuperClass().asOWLClass().getIRI().getFragment().toString() + " ";
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
				 
				} catch (Exception e){
					e.printStackTrace();
				}
			System.out.println("Returning list: " + result);
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
	
	
	
	
	
	public String handleCluster1Request(String input, PrintStream printstream) throws IOException, OWLOntologyCreationException {
		
		// System.out.println("[Handle Request called.]");
		
		String output = "";
		
		 Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	     rootlogger.setLevel(Level.OFF);
		//  OWLReasonerFactory reasonerFactory = new JFactFactory();
	  	 OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
	  	
		
		List<String> list = new ArrayList<String>();
   		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
   		while (m.find())
   			   list.add(m.group(1));
   			// String[] inputs = temp.split(" ");
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
   				String result = VerbaliseTreeManager.verbaliseNL(tree, false, true,null); // <-- 2nd arg labels, 3rd arg html
   				printstream.println(result);
   				String resultPlain = VerbaliseTreeManager.verbaliseNL(tree, false, false,null); // <-- 2nd arg labels, 3rd arg html
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
