package org.semanticweb.cogExp.OWLAPIVerbaliser;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * represents a line break .
 * This is needed for the @see CustomJPanel 
 * @author fpaffrath
 *
 */
public class LinebreakElement extends TextElement{

		public LinebreakElement(){
			super("\n");
		}
		
		public String toHTML(){
			return "<br>"; 
		}
		
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
	
		@Override
		public boolean isLinebreak(){
			return true;
		}
	
}
