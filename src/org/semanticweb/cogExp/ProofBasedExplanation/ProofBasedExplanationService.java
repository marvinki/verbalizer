package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.protege.editor.owl.ui.renderer.OWLModelManagerEntityRenderer;
import org.protege.editor.owl.ui.renderer.OWLObjectRenderer;
import org.semanticweb.cogExp.ProofBasedExplanation.TextExplanationResult;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.Obfuscator;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;
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
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import uk.ac.manchester.cs.jfact.JFactFactory;

public class ProofBasedExplanationService extends ExplanationService{

	@Override
	public void initialise() throws Exception {
		// Do nothing.
	}

	@Override
	public boolean hasExplanation(OWLAxiom axiom) {
		return true;
	}

	@Override
	public ExplanationResult explain(OWLAxiom axiom) {
		
		
		OWLModelManager modelmanager = getOWLModelManager();		
		OWLReasoner reasoner = modelmanager.getOWLReasonerManager().getCurrentReasoner();
		OWLReasonerFactory factory = modelmanager.getOWLReasonerManager().getCurrentReasonerFactory().getReasonerFactory();
		OWLOntology ontology = modelmanager.getActiveOntology();
		
		
		String explanation = 
	    getExplanationResult(axiom,
			   reasoner,
			   factory,
		 	   ontology,
		 	   true,
		 	   false,
		 	   false // null obfuscator
		 	   );
		 	   
		
	  
		JPanel panel = new JPanel();
		JTextArea textfield = new JTextArea(explanation);
		panel.add(textfield);
		TextExplanationResult result = new TextExplanationResult(panel);
		
		return result;
	}
	
	public void dispose() throws Exception {
		// Do nothing.
	}
	
	/** Compute textual explanations in English
	 * 
	 * @param axiom			-- the conclusion for which an explanation is desired
	 * @param reasoner		-- the reasoner to be employed for justification finding
	 * @param factory		-- the reasoner factory of the employed reasoner
	 * @param ontology		-- the current ontology
	 * @param dict			-- the path pointing to the installation of WordNet (indicate the path of the "dict" subdirectory)
	 * @return				-- a text string containing the explanation
	 */
	public static String getExplanationResult(OWLAxiom axiom, 
											  OWLReasoner reasoner, 
											  OWLReasonerFactory factory, 
											  OWLOntology ontology, 
											  Boolean enableDict, 
											  boolean asHTML, 
											  boolean obf){
		String tmpdir = "";
		if (enableDict!=null & enableDict){
			try {
				tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WordNetQuery.INSTANCE.setDict(tmpdir);
		}
		return VerbalisationManager.verbalizeAxiom(axiom, reasoner, factory, ontology, asHTML, obf);
		
	}
	
	
	
	
	public static String getExplanationResult(OWLAxiom axiom, 
											  OWLReasoner reasoner, 
											  OWLReasonerFactory factory, 
											  OWLOntology ontology, 
											  Boolean enableDict, 
											  int maxsteps, 
											  long maxtime, 
											  String ruleset, 
											  boolean asHTML,
											  boolean obf){
		String tmpdir = "";
		if (enableDict!=null & enableDict){
			try {
				tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WordNetQuery.INSTANCE.setDict(tmpdir);
		}
		return VerbalisationManager.verbalizeAxiom(axiom, reasoner, factory, ontology,maxsteps, maxtime,ruleset,asHTML,obf);
		
	}
	
	/** Compute textual explanations in English (not using WordNet)
	 * 
	 * @param axiom			-- the conclusion for which an explanation is desired
	 * @param reasoner		-- the reasoner to be employed for justification finding
	 * @param factory		-- the reasoner factory of the employed reasoner
	 * @param ontology		-- the current ontology
	 * @param rulenames     -- a flag to indicate whether rule names shall be output
	 * @return				-- a text string containing the explanation
	 */
	public static String getExplanationResult(OWLAxiom axiom, 
											  OWLReasoner reasoner,
											  OWLReasonerFactory factory, 
											  OWLOntology ontology, 
											  boolean asHTML, 
											  boolean obf){
		WordNetQuery.INSTANCE.disableDict();
		return getExplanationResult(axiom, reasoner, factory, ontology,false,asHTML, obf);
	}
	
	public static GentzenTree computeTree(String subclass, String superclass, String ontologyname){
		Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootlogger.setLevel(Level.OFF);
		
		GentzenTree tree = null;
		
		// load ontology
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		java.io.File file = new java.io.File(ontologyname);
		FileDocumentSource source = new FileDocumentSource(file);
		OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
		loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
		loaderconfig = loaderconfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.valueOf("SILENT"));
				
		OWLOntology ontology;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
			VerbalisationManager.INSTANCE.setOntology(ontology);
		
		
		
		// construct axiom
	  	OWLDataFactory dataFactory=manager.getOWLDataFactory();
		
		 
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
		 
		 if (subcl==null || supercl==null){
			 return null;
		 }
		 
		 OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(subcl, supercl);
		
		// get reasoner
		 OWLReasonerFactory reasonerFactory2 = new JFactFactory();
		 OWLReasonerConfiguration config = new SimpleConfiguration(30000);
		 OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology,config);
		 
		
		 
		 tree = VerbalisationManager.computeGentzenTree(axiom, 
					reasonerJFact, 
					reasonerFactory2, 
					ontology, 
					50000,
					60000,
					"OP");	
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return tree;
	}
	
	public static String computeVerbalization(GentzenTree tree, boolean asHTML, Obfuscator obfuscator){
		WordNetQuery.INSTANCE.disableDict();
		String result = VerbaliseTreeManager.verbaliseNL(tree, false,asHTML,obfuscator); 
		return result;
	}
	
	

	/** Compute textual explanations in English
	 * 
	 * @param axiom			-- the conclusion for which an explanation is desired
	 * @param reasoner		-- the reasoner to be employed for justification finding
	 * @param factory		-- the reasoner factory of the employed reasoner
	 * @param ontology		-- the current ontology
	 * @param dict			-- the path pointing to the installation of WordNet (indicate the path of the "dict" subdirectory)
	 * @return				-- a text string containing the explanation
	 */
	public static TextElementSequence getExplanationResultAsSequence(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology, Boolean enableDict, String inferenceruleset){
		String tmpdir = "";
		if (enableDict!=null & enableDict){
			try {
				tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WordNetQuery.INSTANCE.setDict(tmpdir);
		}
		return VerbalisationManager.verbalizeAxiomAsSequence(axiom, reasoner, factory, ontology,100, 10000, inferenceruleset,true,false); // <--- 
	}
		
	
public ExplanationResult explainInPlugin(OWLAxiom axiom) {
		
		
		OWLModelManager modelmanager = getOWLModelManager();		
		OWLReasoner reasoner = modelmanager.getOWLReasonerManager().getCurrentReasoner();
		OWLReasonerFactory factory = modelmanager.getOWLReasonerManager().getCurrentReasonerFactory().getReasonerFactory();
		OWLOntology ontology = modelmanager.getActiveOntology();
		
		
		/* Outer panel, with vertical layout */
		
		JPanel panel = new JPanel();
		
		TextElementSequence sequence = 
			    getExplanationResultAsSequence(axiom,
					   reasoner,
					   factory,
				 	   ontology,
				 	   true,
				 	   "EL"
				 	   );
				 	   
		
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setHgap(0);
		BoxLayout verticalLayout = new BoxLayout(panel,BoxLayout.Y_AXIS);
		panel.setLayout(verticalLayout);
		JPanel innerpanel = new JPanel();
		boolean justskipped = false;
		innerpanel.setLayout(flowLayout);
		innerpanel.setBackground(Color.WHITE);
		panel.add(innerpanel);
		innerpanel.add(new JLabel(" "));
		String previoustext = "placeholder";
		
		List<JLabel> labels = sequence.generateLabels();
		for (JLabel label : labels){
			// if (!label.getText().equals(" ")){
				// label.setBorder(BorderFactory.createLineBorder(Color.black));
				if (label.getText().equals(" ") && justskipped){
					justskipped = false;
					continue;
				}
				if ((label.getText().equals(" ") || label.getText().equals("")) && previoustext.equals(" ")){
					continue;
				}
				if (label.getText().equals("\n")){
					innerpanel = new JPanel();
					innerpanel.setLayout(flowLayout);
					innerpanel.setBackground(Color.WHITE);
					if (!label.equals(labels.get(labels.size()-1)))
						panel.add(innerpanel);
					justskipped = true;
				}
				innerpanel.add(label);
				previoustext = label.getText();
				System.out.println("inserting label |" + label.getText() + "|");
			// }
		}
		TextExplanationResult result = new TextExplanationResult(panel);
		
		// trying out stuff
		OWLObjectRenderer objectrenderer = modelmanager.getOWLObjectRenderer();
		
		
		OWLModelManagerEntityRenderer renderer = modelmanager.getOWLEntityRenderer();
		// OWLObjectListPanel olp = new OWLObjectListPanel();
		
		panel.setBackground(Color.WHITE);
		System.out.println(panel.getHeight());
		
		
		return result;
	}
	

}
