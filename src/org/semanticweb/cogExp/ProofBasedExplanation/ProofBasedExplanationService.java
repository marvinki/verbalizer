package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Set;

import javax.swing.JPanel;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.protege.editor.owl.ui.prefix.GeneratePrefixFromOntologyAction;
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
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
//import uk.ac.manchester.cs.jfact.JFactFactory;
//import tests.CustomGUIList;


public class ProofBasedExplanationService extends ExplanationService{

	@Override
	public void initialise() throws Exception {
		// Do nothing.
	}

	@Override
	public boolean hasExplanation(OWLAxiom axiom) {
		if(axiom instanceof OWLSubClassOfAxiom){
			return true;
		}else{
		return false;
		}
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
	 * @param enableDict	-- whether to use WordNet (to take into account nouns/adjectives and plural forms in class names)
	 * @param asHTML		-- whether or not get HTML output 
	 * @param obf			-- whether or not get clear/readable Text
	 * @return				-- a text string containing the explanation
	 */
	
	/*
	 * TODO here
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
				//  Auto-generated catch block
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
				//  Auto-generated catch block
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
	 * @param asHTML		TODO add explanation
	 * @param obf			TODO add explanation			
	 * @return				-- a text string containing the explanation
	 */
	
	/*
	 * TODO add some @param
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
	
	
	/** computes a GentzenTree of two classes with respect to an specified ontology
	 * 
	 * @param subclass			subclass as String	
	 * @param superclass		superclass as String	
	 * @param ontologyname		path to the ontology as string
	 * @param reasoner 			TODO
	 * @return	a GentzenTree object 
	 * @see GentzenTree
	 */
	
	/*TODO add @param reasoner
	 * 
	 */
	public static GentzenTree computeTree(String subclass, String superclass, String ontologyname,
			OWLReasonerFactory reasonerFactory, OWLReasoner reasoner){
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
//		 OWLReasonerFactory reasonerFactory2 = new JFactFactory();
//		 OWLReasonerConfiguration config = new SimpleConfiguration(30000);
//		 OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology,config);
		 
		
		 
		 tree = VerbalisationManager.computeGentzenTree(axiom, 
					reasoner, 
					reasonerFactory, 
					ontology, 
					50000,
					60000,
					"OP");	
		// } catch (OWLOntologyCreationException e) {
		} catch (Exception e) {
			//  Auto-generated catch block
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
	 * @param enableDict	TODO add description
	 * @param inferenceruleset TODO add description
	 * @return				-- a text string containing the explanation
	 */
	
	/*
	 * TODO add some @param
	 */
	public static TextElementSequence getExplanationResultAsSequence(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology, Boolean enableDict, String inferenceruleset){
		String tmpdir = "";
		if (enableDict!=null & enableDict){
			try {
				tmpdir = org.semanticweb.wordnetdicttmp.WordnetTmpdirManager.makeTmpdir();
			} catch (IOException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
			WordNetQuery.INSTANCE.setDict(tmpdir);
		}
	
		return VerbalisationManager.verbalizeAxiomAsSequence(axiom, reasoner, factory, ontology,100, 10000, inferenceruleset,true,false); // <--- 
	
	}
		
	
/* 
 * you can find the abstract ExplanationResult class as well as the 
 * abstract explanation function (in the ExplanationService class)
 * in the org.protege.editor.owl.ui.explanation package
 */
public ExplanationResult explain(OWLAxiom axiom) {
		
		System.out.println(
				"----------------------------  starting verbalization  -------------------------"
				);
	
		
		OWLModelManager modelmanager = getOWLModelManager();	
		OWLReasoner reasoner = modelmanager.getOWLReasonerManager().getCurrentReasoner();
		OWLReasonerFactory factory = modelmanager.getOWLReasonerManager().getCurrentReasonerFactory().getReasonerFactory();
		OWLOntology ontology = modelmanager.getActiveOntology();
		OWLEditorKit editorKit = getOWLEditorKit();
		     
		
		
		JPanel panel = new JPanel();
		
		TextElementSequence sequence = getExplanationResultAsSequence(axiom, reasoner, factory, 
					   													ontology, true, "OP");	
		
		
		
		
		TextExplanationResult result = new TextExplanationResult(panel,editorKit);
		
		
		result = result.getResult(sequence,editorKit);
	
		result.updateUI();
		return result;
	}
	

}
