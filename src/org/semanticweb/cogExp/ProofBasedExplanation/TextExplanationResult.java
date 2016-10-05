package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.naming.spi.DirectoryManager;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;

import tests.CustomJPanel;

public class TextExplanationResult extends ExplanationResult{ // implements OWLModelManagerListener{

	/*
	 * TODO Comment
	 * 
	 */
	private static final long serialVersionUID = 1944836708820525384L;

	private JPanel content = new JPanel();
	//private GridLayout contentLayout = new GridLayout(0,1,0,0);
	private GridBagLayout contentLayout  = new GridBagLayout();
	private GridBagConstraints constraint = new GridBagConstraints();
	private Dimension contentSize;
	
	
	public TextExplanationResult(JPanel panel){
		//content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.anchor = GridBagConstraints.FIRST_LINE_START;
		constraint.gridx = 0;
		constraint.gridy = 0;	
		constraint.weightx = 0;
		constraint.weighty = 0;
		
		content.setLayout(contentLayout);
		contentSize = new Dimension((int)getToolkit().getScreenSize().getWidth()/4,
											  (int)getToolkit().getScreenSize().getHeight()/2);
		content.setSize(contentSize);
	}
	
	
	public TextExplanationResult getResult(TextElementSequence sequence){
		CustomJPanel innerpanel = new CustomJPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		
		
		innerpanel.setLayout(flowLayout);
		innerpanel.setBackground(Color.WHITE);
		
		content.add(innerpanel,constraint);
		
		innerpanel.add(new JLabel(""));
		
		List<JLabel> labels = sequence.generateLabels();
		
		for (int i=0; i<labels.size(); i++){
			
			// check if colons are set correctly and fix it if necessary
			if(!(i>=labels.size()-1) && labels.get(i).getText().equals(".")&&
					 					labels.get(i+1).getText().equals(" ")){
				innerpanel.add(labels.get(i));
				labels.set(i+1, new JLabel(""));
				continue;
			}
			
			// check if there are double spaces and correct if necessary
			if(labels.get(i).getText().equals(" ") ||
			   labels.get(i).getText().equals("") ||
			   labels.get(i).getText().equals(", ") ){
				
				if(i<=0 && labels.get(i-1).getText().equals(" ")){
					labels.set(i-1, new JLabel(""));
				}
				if(!(i>=labels.size()-1) && labels.get(i+1).getText().equals(" ")){
					labels.set(i+1, new JLabel(""));
				}
			}
				
			// concatenate labels and put them into a panel if line is broken
			
			innerpanel.add(labels.get(i));
			
			
			if(labels.get(i).getText().equals("\n")){
				
				
				//Dimension d = innerpanel.getPreferredSize();
				Dimension d = innerpanel.computeBestSize(content);
				constraint.gridy++;
				
				
				System.out.println("\nPanel\n "+i+
								   ": computedSize: "+ d.getWidth() +
								   ", "+ d.getHeight());
				
				System.out.println("\nPanel\n "+i+
						   ": preferredSize: "+ innerpanel.getPreferredSize().getWidth() +
						   ", "+ innerpanel.getPreferredSize().getHeight());
				
				innerpanel.setPreferredSize(d);
				//innerpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				content.add(innerpanel, constraint);
				
				System.out.println("ActualSize: "+ innerpanel.getSize().getWidth() +
									", "+ innerpanel.getSize().getHeight());	
				
				innerpanel = new CustomJPanel(flowLayout); // removeAll
				innerpanel.setBackground(Color.WHITE);
			}
			
			
		}
		
		
		
//		System.out.println("\nSize of ConentPanel: "+ content.getWidth() +", "+ content.getHeight());
//		TextExplanationResult scrollable = this.makeScrollable(); 
//		scrollable.setSize(contentSize);
//		System.out.println("\nSize of ScrollPanel: "+ scrollable.getWidth() +", "+ scrollable.getHeight());
		content.setPreferredSize(contentSize);
		this.add(content);
		this.makeScrollable(); 
		
		return this;
	}
	
	
	private TextExplanationResult makeScrollable(){
		//this.setLayout(contentLayout);
		this.add(getScrollPane(content));
		
		return this;
	}
		
		
	private JScrollPane getScrollPane(JPanel panel){		
			JScrollPane scrollPane = new JScrollPane (panel, 
		            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
		            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			return scrollPane;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub	
	}


}
