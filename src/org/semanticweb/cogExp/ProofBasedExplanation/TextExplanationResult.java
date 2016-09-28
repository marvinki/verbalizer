package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;

public class TextExplanationResult extends ExplanationResult{ // implements OWLModelManagerListener{

	/*
	 * TODO Comment
	 * 
	 */
	private static final long serialVersionUID = 1944836708820525384L;

	private JPanel content = new JPanel();
	
	public TextExplanationResult(JPanel panel){
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
	}
	
	
	public TextExplanationResult getResult(TextElementSequence sequence){
		JPanel innerpanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		
		innerpanel.setLayout(flowLayout);
		innerpanel.setBackground(Color.WHITE);
		
		content.add(innerpanel);
		
		innerpanel.add(new JLabel(" "));
		
		List<JLabel> labels = sequence.generateLabels();
		
		for (int i=1; i<labels.size()-1; i++){
			
			// check if colons are set correctly and fix it if necessary
			if(labels.get(i).getText().equals(".")&
					 labels.get(i+1).getText().equals(" ")){
				innerpanel.add(labels.get(i));
				labels.set(i+1, new JLabel(""));
				continue;
			}
			
			// check if there are double spaces and correct if necessary
			if(labels.get(i).getText().equals(" ") ||
				labels.get(i).getText().equals("") ||
				labels.get(i).getText().equals(", ") ){
				
				if(labels.get(i-1).getText().equals(" ")){
				 	labels.set(i-1, new JLabel(""));
				}
				if(labels.get(i+1).getText().equals(" ")){
					labels.set(i+1, new JLabel(""));
				}
				continue;
			}
				
			// concatenate labels and put them into a panel if line is broken
			innerpanel.add(labels.get(i));
			if(labels.get(i).getText().equals("\n")){
				content.add(innerpanel);
				
				innerpanel = new JPanel(flowLayout); // removeAll
				innerpanel.setBackground(Color.WHITE);
			}
		}
				
		return this.formatResult();
	}
	
	
	private TextExplanationResult formatResult(){
		removeAll();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(getScrollPane(content));
		
		return this;
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


}
