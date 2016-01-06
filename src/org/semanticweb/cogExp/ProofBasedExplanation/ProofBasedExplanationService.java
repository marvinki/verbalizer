package org.semanticweb.cogExp.ProofBasedExplanation;

import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

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
		
		String tmpdirstring= "";
		try {
			tmpdirstring = WordnetTmpdirManager.INSTANCE.makeTmpdir();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				}
		
		String explanation = 
	    getExplanationResult(axiom,
			   reasoner,
			   factory,
		 	   ontology,
		 	   tmpdirstring
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
	public static String getExplanationResult(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology, String dict){
		if (dict!=null & dict.length()>0)
			WordNetQuery.INSTANCE.setDict(dict);
		return VerbalisationManager.verbalizeAxiom(axiom, reasoner, factory, ontology);
	}
	
	/** Compute textual explanations in English (not using WordNet)
	 * 
	 * @param axiom			-- the conclusion for which an explanation is desired
	 * @param reasoner		-- the reasoner to be employed for justification finding
	 * @param factory		-- the reasoner factory of the employed reasoner
	 * @param ontology		-- the current ontology
	 * @return				-- a text string containing the explanation
	 */
	public static String getExplanationResult(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology){
		return getExplanationResult(axiom, reasoner, factory, ontology);
	}

}
