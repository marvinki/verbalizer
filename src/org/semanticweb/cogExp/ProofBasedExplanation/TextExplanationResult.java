package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.explanation.ExplanationResult;

public class TextExplanationResult extends ExplanationResult{ // implements OWLModelManagerListener{

	private JPanel jpanel = new JPanel();
	
	public TextExplanationResult(JPanel panel){
		jpanel = panel;
		setLayout(new BorderLayout());
		add(panel);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub	
	}

	public void handleChange(OWLModelManagerChangeEvent event) {
		// TODO Auto-generated method stub
	}


}
