package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import org.protege.editor.owl.OWLEditorKit;

/**
 * Special TextElement that indicates that there's nothing to explain. 
 * It comes in action if e.g. there is no SequentInferenceRule for 
 * the axiom.
 * It is supposed to facilitate coloring and such like.
 * @author fpaffrath
 *
 */
public class NoRulesElement extends TextElement{
		
	
	/*TODO maybe an extra "Failure!" Element should be implemented
	 * 
	 */

	/**
	 * This constructor loads the TextElement with the string
	 * "That's already stated in the ontology. " and adds an explanation at the end. 
	 */
	/**
	 * @param explanation is added at the end
	 */
	public NoRulesElement(String explanation) {
		ResourceBundle LogicLabels = ResourceBundle.getBundle("LogicLabels", VerbaliseTreeManager.locale);
		this.content = LogicLabels.getString("thatsAlreadyStated") + explanation;
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toHTML()
	 */
	@Override
	public String toHTML() {
		// TODO Auto-generated method stub
		return super.toHTML();
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#setContent(java.lang.String)
	 */
	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		super.setContent(content);
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#addToDocument(javax.swing.JTextPane)
	 */
	@Override
	public void addToDocument(JTextPane textPane) {
		// TODO Auto-generated method stub
		super.addToDocument(textPane);
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toJLabel()
	 */
	@Override
	public List<JLabel> toJLabel(OWLEditorKit ek) {
		// TODO Auto-generated method stub
		List<JLabel> labelList = new ArrayList<JLabel>();
		labelList.addAll(super.toJLabel(ek));
		labelList.add(new JLabel("\n"));
		
		return labelList;
	}

}
