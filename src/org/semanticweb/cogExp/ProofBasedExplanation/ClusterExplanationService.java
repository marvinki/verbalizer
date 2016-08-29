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
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import org.apache.log4j.spi.LoggerFactory;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
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
	
	private static final String TEMP_PATH = "/tmp/graph.";

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
	
	
	public static String listInferredAxioms(List<String> input){
		System.out.println("[Retrieving list of superclasses]");
			// find classname
			String classnameString = input.get(1);
			String ontologyname = input.get(2).replaceAll("\"", "");
				
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			java.io.File file = new java.io.File(ontologyname);
			FileDocumentSource source = new FileDocumentSource(file);
			OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
			loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
			loaderconfig = loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));
						
			OWLOntology ontology;
			String result = "";
			try {
				ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
				System.out.println("[done loading ontology]");
	   				
				OWLClass classname = null;
			
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
				
		    OWLReasonerFactory reasonerFactory2 = new JFactFactory();
		    SimpleConfiguration configuration = new SimpleConfiguration(50000);
			OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology,configuration);
			// List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
			// gens.add(new InferredSubClassAxiomGenerator());
			 
		    // Put the inferred axioms into a fresh empty ontology.
		    OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
			OWLOntology infOnt = outputOntologyManager.createOntology();
			Set<OWLAxiom> previousaxioms = ontology.getAxioms();
			// System.out.println("Previous axioms " + previousaxioms.size());
			InferredOntologyGenerator iog = new InferredOntologyGenerator(reasonerJFact);
			    
			OWLDataFactory dataFactory2=manager.getOWLDataFactory();
			iog.fillOntology(dataFactory2, ontology);
			// iog.fillOntology(outputOntologyManager, infOnt);
			Set<OWLAxiom> newaxioms = ontology.getAxioms();
			System.out.println("Newly inferred axioms: " + (newaxioms.size() - previousaxioms.size()));
			
			newaxioms.removeAll(previousaxioms);
			
	        for (OWLAxiom ax: newaxioms){
			    	if (!previousaxioms.contains(ax)){
			    		if (ax instanceof OWLSubClassOfAxiom){
			    			System.out.println("subcl " + ax);
			    			OWLSubClassOfAxiom subclax = (OWLSubClassOfAxiom) ax;
			    			if (subclax.getSubClass().toString().contains(classnameString)){
			    				System.out.println(subclax.getSuperClass());
			    				result += subclax.getSuperClass().toString() + " ";
			    			}
			    		}
			    		else{
			    			System.out.println("sth else " + ax);
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
		    System.out.println("dotfile generated in: " + dotfile);
		    if(!dotfile.exists()){
				dotfile.createNewFile();
			}
		    Writer dotWriter = new BufferedWriter(new OutputStreamWriter(
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
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	
	
	
	public static String handleCluster1Request(String input) throws IOException, OWLOntologyCreationException {
		
		String output = "";
		
		 Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	     rootlogger.setLevel(Level.OFF);
		 OWLReasonerFactory reasonerFactory = new JFactFactory();
	  	 OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
	  	
		String tmpdir = "";
		try {
			tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		List<String> list = new ArrayList<String>();
   		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
   		while (m.find())
   			   list.add(m.group(1));
   			// String[] inputs = temp.split(" ");
   		if (list.size()==3 && list.get(0).contains("list")){
   				output = listInferredAxioms(list);			
   		}
   			if (list.size()>3){
   				GentzenTree tree = ProofBasedExplanationService.computeTree(list.get(0), 
   																			list.get(1),
   																			list.get(2).replaceAll("\"", ""));
   				if (tree==null){
   					System.out.println("ERROR. Subsumption could not be proven.");
   					output = "ERROR. Subsumption could not be proven.";
   				}
   				String result = VerbaliseTreeManager.verbaliseNL(tree, false, true,null); // <-- 2nd arg labels, 3rd arg html
   				String resultPlain = VerbaliseTreeManager.verbaliseNL(tree, false, false,null); // <-- 2nd arg labels, 3rd arg html
   				
   				String dotTree = tree.toDOT();
   				
   				handleDotRequest(dotTree);
   					
   				PrintWriter htmlwriter = new PrintWriter(list.get(3) + "/text.html", "UTF-8");
   				htmlwriter.write(result);
   				htmlwriter.close();
   				
   				PrintWriter plainwriter = new PrintWriter(list.get(3) + "/plaintext.txt", "UTF-8");
   				plainwriter.write(resultPlain);
   				plainwriter.close();
   				output = resultPlain;
   				
   				System.out.println(result);
   			} else {
   				System.out.println("Please enter two class expressions, the path to the ontology, and a path for the output");
   			}	
   	return output;
   	
   }
	
	
	public static void main(String[] args) throws IOException, OWLOntologyCreationException {
		
		
		 Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		 
		//  System.out.println(rootlogger.isInfoEnabled());
	     rootlogger.setLevel(Level.OFF);
		
		
		// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
		OWLReasonerFactory reasonerFactory = new JFactFactory();
		// Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
		// OWLReasoner reasoner = reasonerFactory.createReasoner(clusterOntology);
	    
	    // indicate the IRIs of some relevant classes/roles in the ontology
	  	OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
	  	
			
		
		// WordNetQuery.INSTANCE.disableDict();
		String tmpdir = "";
		try {
			tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		
		// GentzenTree tree = ProofBasedExplanationService.computeTree("WT_Chest-Triceps", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
		// String result = VerbaliseTreeManager.verbaliseNL(tree, true, false,null); // <------ 3rd arg is: true - html, false - text
		// String listResult = VerbaliseTreeManager.listOutput(tree);
		// System.out.println(result);
		// System.out.println(" ");
		// System.out.println(listResult);
		
		
		// GentzenTree tree2 = ProofBasedExplanationService.computeTree("WT_UpperBody", "Upper_Body_Training_Template", "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf");
		// String result2 = VerbaliseTreeManager.verbaliseNL(tree2, true, false,null);
		// System.out.println(result2);
		
		
		// Eingabe: (i) Unterklasse (ii) Oberklasse (iii) Pfad zur Ontologie (iv) Pfad zu Ausgabedateien
		
		// WT_Chest-Triceps Upper_Body_Training_Template "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_demo.rdf" /Users/marvin
		// WT_Chest-Triceps Upper_Body_Training_Template "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_demo.rdf" /Users/marvin
		// WT_UpperBody Upper_Body_Training_Template "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_demo.rdf" /Users/marvin
		
		
		// list WT_Chest-Triceps "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in rdf-xml format/cluster6ontology_complete.rdf"
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
    	boolean cont = true;
    	while (cont) {
    		// System.out.print("Input something: ");
    		String temp = in.readLine();
    		if (temp.equals("end")) {
    			cont = false;
    			System.out.println("Terminated.");
    		}
    		else{
    			List<String> list = new ArrayList<String>();
    			Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(temp);
    			while (m.find())
    			    list.add(m.group(1));
    			// String[] inputs = temp.split(" ");
    			if (list.size()==3 && list.get(0).contains("list")){
    				System.out.println("[Retrieving list of superclasses]");
    				// find classname
    				String classnameString = list.get(1);
    				String ontologyname = list.get(2).replaceAll("\"", "");
    				
    				OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    				java.io.File file = new java.io.File(ontologyname);
    				FileDocumentSource source = new FileDocumentSource(file);
    				OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
    				loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
    				loaderconfig = loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));
    						
    				OWLOntology ontology;
    				try {
    					ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
    					System.out.println("[done loading ontology]");
    	   				
    				 OWLClass classname = null;
    				 
    				 Set<OWLClass> classes = ontology.getClassesInSignature();
    				 for (OWLClass cl : classes){
    					 // System.out.println(cl.toString());
    					 if (cl.getIRI().getFragment().equals(classnameString)){
    						 classname = cl;
    					 }
    				 }
    				 
    				 System.out.println("[identified class]: " + classname);
    				
    				 OWLReasonerFactory reasonerFactory2 = new JFactFactory();
    				 // OWLReasonerConfiguration config = new SimpleConfiguration(30000);
    				 // OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology,config);
    				 OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology);
    
    				 List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
    			     gens.add(new InferredSubClassAxiomGenerator());
    			     // gens.add(new InferredEquivalentClassAxiomGenerator());

    			    // Put the inferred axioms into a fresh empty ontology.
    			    OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
    			    OWLOntology infOnt = outputOntologyManager.createOntology();
    			    Set<OWLAxiom> previousaxioms = ontology.getAxioms();
    			    InferredOntologyGenerator iog = new InferredOntologyGenerator(reasonerJFact,
    			                        gens);
    			    
    			    OWLDataFactory dataFactory2=manager.getOWLDataFactory();
    			    iog.fillOntology(dataFactory2, infOnt);
    			    // iog.fillOntology(outputOntologyManager, infOnt);
    			    Set<OWLAxiom> newaxioms = infOnt.getAxioms();
    			    // System.out.println(newaxioms);
    			    
    			    for (OWLAxiom ax: newaxioms){
    			    	if (ax instanceof OWLSubClassOfAxiom){
    			    		OWLSubClassOfAxiom subclax = (OWLSubClassOfAxiom) ax;
    			    		if (subclax.getSubClass().equals(classname)){
    			    			System.out.println(subclax.getSuperClass());
    			    		}
    			    	}
    			    }
    				 
    				} catch (Exception e){
    					e.printStackTrace();
    				}
    				
    				continue;
    			}
    			if (list.size()>3){
    				GentzenTree tree = ProofBasedExplanationService.computeTree(list.get(0), 
    																			list.get(1),
    																			list.get(2).replaceAll("\"", ""));
    				if (tree==null){
    					System.out.println("ERROR. Subsumption could not be proven.");
    					continue;
    				}
    				String result = VerbaliseTreeManager.verbaliseNL(tree, false, true,null); // <-- 2nd arg labels, 3rd arg html
    				String resultPlain = VerbaliseTreeManager.verbaliseNL(tree, false, false,null); // <-- 2nd arg labels, 3rd arg html
    				
    				String dotTree = tree.toDOT();
    				
    				// System.out.println("dot string generated: " + dotTree);
    				
    				// Temporary file for graphics
    				String property = "java.io.tmpdir";
    			    String tempDir = System.getProperty(property);
    			    // File newTempfile = new File(tempDir + File.separator + "graph.png");
    			    
    			    Path dotpath = Paths.get(tempDir + File.separator + "graph.dot");
    			    File dotfile = new File(dotpath.toString());
    			    System.out.println("dotfile generated in: " + dotfile);
    			    if(!dotfile.exists()){
    					dotfile.createNewFile();
    				}
    			    Writer dotWriter = new BufferedWriter(new OutputStreamWriter(
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
    		        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		        f.getContentPane().add(zoom.getUIPanel(), "North");
    		        f.getContentPane().add(new JScrollPane(panel));
    		        int initHeight = (int) (panel.getPreferredSize().getHeight() * 1.2);
    		        int initWidth = (int) (panel.getPreferredSize().getWidth() * 1.2);
    		        // System.out.println(panel.getPreferredSize().getHeight());
    		        f.setSize(initWidth,initHeight);
    		        f.setLocation(200,200);
    		        f.setVisible(true);
    			    
    		        /*
    			    ImagePanel panel = new ImagePanel(pngfile.toString());
    			    JFrame frame = new JFrame();
    			    JScrollPane pane = new JScrollPane(panel);
    			    frame.setTitle("Proof Graph");
    			    frame.add(pane,"Center");
    			    // frame.getContentPane().add(pane);
    			    // frame.add(panel);
    			    // frame.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
    			    // System.out.println(panel.getHeight());
    			    frame.setSize(new Dimension(panel.getWidth(), panel.getHeight()));
    			    frame.setSize(new Dimension(1600, 1000));
    			    // System.out.println(frame.getHeight());
    			    // frame.pack();
    			    frame.setVisible(true);
    				*/
    				
    				PrintWriter htmlwriter = new PrintWriter(list.get(3) + "/text.html", "UTF-8");
    				htmlwriter.write(result);
    				htmlwriter.close();
    				
    				PrintWriter plainwriter = new PrintWriter(list.get(3) + "/plaintext.txt", "UTF-8");
    				plainwriter.write(resultPlain);
    				plainwriter.close();
    				
    				System.out.println(result);
    			} else {
    				System.out.println("Please enter two class expressions, the path to the ontology, and a path for the output");
    			}
    		}
    			// System.out.println(temp);
    	}
    	
    	return;
    	
    }
	
}
