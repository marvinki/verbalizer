package org.semanticweb.cogExp.ProofBasedExplanation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class StringServer {
	private final ServerSocket server;
	// private final Socket socket;
	private ClusterExplanationService service = null;

	public StringServer(int port, ClusterExplanationService service) throws IOException {
		
		/*
		
		String tmpdir = "";
		try {
			tmpdir = WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		
		*/
		
		////// WE ARE NOT USING WORDNET!
		WordNetQuery.INSTANCE.disableDict();
		
		server = new ServerSocket(port);
		this.service = service;
		while (true) {
			System.out.println(".");
			
			final Socket socket = server.accept();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// Socket socket = null;
					try {
						handle(socket);
						socket.close();
					}

					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
		}

	}

	/*private void connect() {

		while (true) {
			// Socket socket = null;
			try {
				handle(socket);
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	private void handle(Socket socket) throws IOException {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream outputStream = new PrintStream(socket.getOutputStream());
		String s;

		// while(inputReader.ready()) {
		while (socket != null && socket.isConnected()) {
			int inp = inputReader.read();
			// if end of stream is reached, 'read' returns -1
			if (inp>0) {
				s = "";
				
				char fc = (char) inp;
				while (fc != '[' && fc != '{')
					fc = (char)inputReader.read();
				s += fc;
				int oc = 1;
				while (oc != 0){
					char nc = (char)inputReader.read();
					if (nc == fc) oc++;
					else if (fc == '[' && nc == ']') oc--;
					else if (fc == '{' && nc == '}') oc--;
					s +=nc;
				}
				

				System.out.println(">>Reading input: " + s + "<<");
				
				try {
					service.handleBoschRequest(s, outputStream);
				} catch (OWLOntologyCreationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(">>Waiting<<");
			} else {
				// System.out.println("idlecount " + idlecount);
				try {
<<<<<<< HEAD
					Thread.sleep(100);
				} catch (InterruptedException e) {
=======
					Thread.sleep(200);
					// System.out.println("sleep");
				} catch (Exception e) {
>>>>>>> cluster1
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * idlecount++; if (idlecount>1000){
				 * outputStream.println("PROBING THE CONNECTION!!!"); if
				 * (outputStream.checkError()){ idlecount = 0; break; } }
				 */
			}

		}
		System.out.println("Socket connection lost.");
		inputReader.close();
		outputStream.close();
		socket.close();
	}

	public static void main(String[] args) throws IOException {
		int port = 3111;
		System.out.println("[Server preparation. Please wait.]");
		if (args.length > 2 && args[2] != null) {
			String lang = args[2];
			if (lang.equals("de")){
				VerbaliseTreeManager.setLocale(Locale.GERMAN);
			}
		}
		if (args.length > 0 && args[0] != null) {
			port = Integer.parseInt(args[0]);
		}
		OWLOntology ontology = null;
		OWLReasonerFactory reasonerFactory = null;
		OWLReasoner reasoner = null;
		if (args.length > 1 && args[1] != null) {
			String ontologyfile = args[1];
			ClusterExplanationService.setOntologyfile(ontologyfile);
			File inputfile = new File(ontologyfile);
			System.out.println("inputfile " + inputfile.getAbsoluteFile());
			System.out.println("inputfile parent " + inputfile.getAbsoluteFile().getParentFile());
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			AutoIRIMapper autoIRIMapper = new AutoIRIMapper(inputfile.getAbsoluteFile().getParentFile(),true);
			// OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(inputfile.getAbsoluteFile().getParentFile(),true);
	        // We can now use this mapper in the usual way, i.e.
			manager.addIRIMapper(autoIRIMapper);
			System.out.println(autoIRIMapper.getOntologyIRIs());
			java.io.File file = new java.io.File(ontologyfile);
			// System.out.println("Ontologyfile exists?: "+ file.exists());
			// System.out.println("Ontologyfile can read?: "+ file.canRead());
			FileDocumentSource source = new FileDocumentSource(file);
			// System.out.println("Reader available?: "+
			// source.isReaderAvailable());
			/// sReader reader = source.getReader();
			// System.out.println("Absolute path "+ file.getAbsolutePath());
			// System.out.println("Is file: "+ file.isFile());
			// FileInputStream inputStr = new
			// FileInputStream(file.getAbsolutePath());
			// inputStr.close()
			// OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration();
			// loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.THROW_EXCEPTION);
			// loaderconfig = loaderconfig
			// 		.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("THROW_EXCEPTION"));

			String result = "";
			try {
				ontology =  manager.loadOntologyFromOntologyDocument(source);
				Set<OWLOntology> imports = ontology.getImports();
				for (OWLOntology imp : imports){
					Set<OWLAxiom> ax = imp.getAxioms();		
					for (OWLAxiom axio : ax)
						System.out.println(axio);
				}
				
				
				// ontology = manager.loadOntologyFromOntologyDocument(source, loaderconfig);
				
				VerbalisationManager.INSTANCE.setOntology(ontology);
				
				// manager.loadOntologyFromOntologyDocument(file);
				System.out.println("Done loading ontology " + ontology.getOntologyID() + ". Now loading reasoner.");
				reasonerFactory = new JFactFactory();
				SimpleConfiguration configuration = new SimpleConfiguration(50000);
				reasoner = reasonerFactory.createReasoner(ontology, configuration);
				
				
				
				//reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
				// reasoner.precomputeInferences();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed to load ontology at " + ontologyfile);
			}
		}

		ClusterExplanationService clusterExplanationService = new ClusterExplanationService(ontology, reasoner,
				reasonerFactory);
		// if (args.length>1 && args[1]!=null){
		// clusterExplanationService.precomputeAxioms();
		// }

		try {
			clusterExplanationService.handleBoschRequest("{\"command\" : \"precompute\"}", null);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[Explanation generator listening on port " + port + ".]");
		StringServer server = new StringServer(port, clusterExplanationService);
		// server.connect();
	}

	// WT_UpperBody Upper_Body_Training_Template
	// "/Users/marvin/work/ki-ulm-repository/miscellaneous/cluster-1-and-6/ontology/in
	// rdf-xml format/cluster6ontology_demo.rdf" /Users/marvin

}