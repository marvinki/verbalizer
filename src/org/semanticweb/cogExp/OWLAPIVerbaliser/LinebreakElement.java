package org.semanticweb.cogExp.OWLAPIVerbaliser;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
}
