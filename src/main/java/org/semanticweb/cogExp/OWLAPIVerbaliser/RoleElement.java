package org.semanticweb.cogExp.OWLAPIVerbaliser;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
/**
 * Represents any role.
 *
 * @author marvinki (doc by fp)
 *
 */

public class RoleElement extends TextElement{

	/**
	 * @param content of the JLabel 
	 */
	public RoleElement(String content){
		
		super(content);
	}

	@Override
	public String toHTML(){
		return "<font color=Maroon>" + content + "</font>";  // <-- there was a space there on purpose 
	}
	
	@Override
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
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}