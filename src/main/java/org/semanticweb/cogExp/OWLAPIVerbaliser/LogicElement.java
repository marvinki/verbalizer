package org.semanticweb.cogExp.OWLAPIVerbaliser;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

// TODO documentation pending
public class LogicElement extends TextElement{

	public String toString(){
		return content;
	}
		
		public LogicElement(String content){
			super(content);
		}
		
		public String toHTML(){
			return content; 
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
	
	
}
