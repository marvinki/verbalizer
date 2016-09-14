package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
	
	public TextExplanationResult(JPanel panel, boolean scrollable){
		JScrollPane scrollPane = getScrollPane(panel);	
		scrollPane.setViewportView(jpanel);
		setLayout(new BorderLayout());
		add(scrollPane);
	}
	
	
	private JScrollPane getScrollPane(JPanel panel){		
			JScrollPane scrollPane = new JScrollPane (panel, 
		            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			return scrollPane;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub	
	}

	public void handleChange(OWLModelManagerChangeEvent event) {
		// TODO Auto-generated method stub
	}


}
