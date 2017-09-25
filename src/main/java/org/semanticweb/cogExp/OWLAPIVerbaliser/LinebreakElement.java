package org.semanticweb.cogExp.OWLAPIVerbaliser;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * represents a line break (its content is nothing but "\n").
 * This is needed for the @see CustomJPanel, so mostly for GUI in the protege plugin.
 * @author fpaffrath
 *
 */
public class LinebreakElement extends TextElement{

		public LinebreakElement(){
			super("\n");
		}
		
		/* (non-Javadoc)
		 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toHTML()
		 */
		public String toHTML(){
			return "<br>"; 
		}
		
		/* (non-Javadoc)
		 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#addToDocument(javax.swing.JTextPane)
		 */
		@Override
		public void addToDocument(JTextPane textPane){
			StyledDocument document = textPane.getStyledDocument();
			try {
				document.insertString(document.getLength(), content, null);
			} catch (BadLocationException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		/* (non-Javadoc)
		 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#isLinebreak()
		 */
		@Override
		public boolean isLinebreak(){
			return true;
		}
	
}
