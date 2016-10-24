package org.semanticweb.cogExp.OWLAPIVerbaliser;

import javax.swing.JTextPane;

public class EmptyTreeElement extends TextElement{


	/**
	 * Special TextElement that indicates that the Gentzen tree is empty. 
	 * It comes in action if e.g. the proof can't be found within the limit.
	 * It is supposed to facilitate coloring and such like.
	 */
	
	/*TODO maybe an extra "Failure!" Element should be implemented
	 * 
	 */

	/**
	 * this constructor loads the TextElement with a special string
	 * to indicates that the Gentzen tree is empty. 
	 */
	public EmptyTreeElement() {	
		super("Failure! Empty tree!");
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toString()
	 */
	@Override
	public String toString() {
		return super.toString();
	}
	
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toHTML()
	 */
	@Override
	public String toHTML() {
				return super.toHTML();
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#addToDocument(javax.swing.JTextPane)
	 */
	@Override
	public void addToDocument(JTextPane textPane) {
		super.addToDocument(textPane);
	}


}
