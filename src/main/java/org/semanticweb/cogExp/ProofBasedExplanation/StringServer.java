package org.semanticweb.cogExp.ProofBasedExplanation;

import java.io.BufferedReader;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class StringServer {
	private final ServerSocket server;
	// private final Socket socket;
	private ClusterExplanationService service = null;

	public StringServer(int port, ClusterExplanationService service) throws IOException {
		String tmpdir = "";
		try {
			tmpdir = WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		server = new ServerSocket(port);
		this.service = service;
		while (true) {
			final Socket socket = server.accept();
			/*Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						socket.close();
						System.out.println("The server is shut down!");
					} catch (IOException e) {
						/* failed  }
				}
			});*/
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// Socket socket = null;
					try {
						handle(socket);
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
		int idlecount = 0;
		while (socket != null && socket.isConnected()) {
			if (inputReader.ready()) {
				s = "";
				
				char fc = (char)inputReader.read();
				s += fc;
				int oc = 1;
				while (oc != 0){
					char nc = (char)inputReader.read();
					if (nc == fc) oc++;
					else if (fc == '[' && nc == ']') oc--;
					else if (fc == '{' && nc == '}') oc--;
					s +=nc;
				}
				
				if (s == null) {
					System.out.println("We've lost connection");
					break;
				}

				System.out.println("[Reading input: " + s + "]");
				String output = "";
				try {
					output = service.handleBoschRequest(s, outputStream);
				} catch (OWLOntologyCreationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// outputStream.println(output);
				// System.out.println("input reader ready? " +
				// inputReader.ready() + " socket: " + socket + " is closed "+
				// socket.isClosed());
				System.out.println("[Waiting]");
			} else {
				// System.out.println("idlecount " + idlecount);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
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
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
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
			// inputStr.close();
			OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration();
			loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
			loaderconfig = loaderconfig
					.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));

			String result = "";
			try {
				ontology = manager.loadOntologyFromOntologyDocument(source, loaderconfig);
				// manager.loadOntologyFromOntologyDocument(file);
				System.out.println("Done loading ontology " + ontology.getOntologyID() + ". Now loading reasoner.");
				reasonerFactory = new JFactFactory();
				SimpleConfiguration configuration = new SimpleConfiguration(50000);
				reasoner = reasonerFactory.createReasoner(ontology, configuration);
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