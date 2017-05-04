/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import org.protege.editor.core.ProtegeManager;
import org.protege.editor.owl.OWLEditorKit;

/**
 * @author fpaffrath
 *
 */
public class ContentJPanel extends JPanel{
/**
	 * 
	 */
	private static final long serialVersionUID = 8461307731989185377L;

	private GridBagLayout contentLayout  = new GridBagLayout();
	private GridBagConstraints constraint = new GridBagConstraints();
	private Dimension contentSize;

	private List<CustomJPanel> innerpanelArray;
	private List<JLabel> labels ;
	private int height;
	
	/**
	 * @param ek OWLEditorKit
	 * @param sequence TextElementSequence
	 */
	public ContentJPanel(OWLEditorKit ek, TextElementSequence sequence) {
		super();
		
		labels = sequence.generateLabels(ek);
		
		constraint.fill = GridBagConstraints.BOTH;
		constraint.anchor = GridBagConstraints.LINE_START;
		constraint.gridx = 0;
		constraint.gridy = 0;	
		constraint.weightx = 0;
		constraint.weighty = 0;
		
		this.setLayout(contentLayout);
				
		contentSize = new Dimension(ProtegeManager
					.getInstance()
					.getFrame(ek.getWorkspace())
					.getSize());
		
		contentSize.setSize(contentSize.getWidth()*0.8, contentSize.getHeight());

		this.setSize(contentSize);
		
		initializeArray();
		fillInnerPanels();
		setProperSize();
		addInnerpanels();
	}
	
	public ContentJPanel(OWLEditorKit ek, double width, double height) {
		super();
		
		labels = new ArrayList<JLabel>();
		
		constraint.fill = GridBagConstraints.BOTH;
		constraint.anchor = GridBagConstraints.LINE_START;
		constraint.gridx = 0;
		constraint.gridy = 0;	
		constraint.weightx = 0;
		constraint.weighty = 0;
		
		this.setLayout(contentLayout);
				
		contentSize = new Dimension(ProtegeManager
					.getInstance()
					.getFrame(ek.getWorkspace())
					.getSize());
		
		// contentSize.setSize(contentSize.getWidth()*0.8, contentSize.getHeight());
		contentSize.setSize(width,height);

		this.setSize(contentSize);
		
		initializeArray();
		// fillInnerPanels();
		// setProperSize();
		// addInnerpanels();
	}
	

	/* (non-Javadoc)
	 * @see java.awt.Component#getSize()
	 */
	public Dimension getSize(){
		return contentSize;
	}
	
	
	private void initializeArray(){
		innerpanelArray = new ArrayList<>();
	}
	
	private void fillInnerPanels(){
		CustomJPanel innerpanel = new CustomJPanel();
		height = 0;
		// put concatenated Labels in Panels 
		for(JLabel label : labels){
			// concatenate labels and put them into a panel if line is broken
			Dimension d;
			innerpanel.add(label);
				if(label.getText().equals("\n")){								
					d = innerpanel.computeBestSize(this);
					innerpanel.setBackground(Color.white);			
				
					innerpanel.setSize(d);
					innerpanel.setMaximumSize(d);
					innerpanel.setMinimumSize(d);
					innerpanel.setPreferredSize(d);

					innerpanelArray.add(innerpanel);
					
					height += Math.ceil(d.getHeight());
					innerpanel = new CustomJPanel(); // removeAll
					innerpanel.setBackground(Color.white);
						
					innerpanel.removeAll();
					innerpanel = new CustomJPanel();
				}
				
				
		}
	}
	
	private void setProperSize(){
		this.setPreferredSize(contentSize);
		contentSize.setSize(contentSize.getWidth(), height/*+fac*getFontMetrics(getFont()).getHeight()*/);
		
	}
	
	private void addInnerpanels(){
		for(CustomJPanel c : innerpanelArray){
			this.add(c, constraint);
			constraint.gridy++;
		}
	}

	
	}
