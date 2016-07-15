package org.semanticweb.cogExp.OWLAPIVerbaliser;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class RoleElement extends TextElement{

	public RoleElement(String content){
		super(content);
	}
	
	public String toHTML(){
		return "<font color=Maroon>" + content + "</font>"; 
	}
	
	public String toString(){
		return content;
	}
	
	@Override
	public void addToDocument(JTextPane textPane){
		StyledDocument document = textPane.getStyledDocument();
		String str = "<font color=Maroon>" + content + "</font>";
		try {
			document.insertString(document.getLength(), str, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
