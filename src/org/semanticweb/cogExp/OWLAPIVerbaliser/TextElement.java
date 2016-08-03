package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
/**
 * represents any textual element of verbalizer
 * @author marvinki (doc by fp)
 *
 */
public class TextElement {
	
	protected String content;
	/**
	 * 
	 * @param content name
	 */
	public TextElement(String content){
		this.content = content;
	}
	
	public TextElement(){
		this.content = "";
	}
	
	/**
	 * @return returns the content as plain string 
	 */
	public String toText(){
		return content;
	}

	public String toHTML(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public void addToDocument(JTextPane textPane){
		StyledDocument document = textPane.getStyledDocument();
		try {
			document.insertString(document.getLength(), content, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public List<JLabel> toJLabel(){
		List<JLabel> result = new ArrayList<JLabel>();
		String name = content;
		if (content.endsWith(" "))
			 name = content.substring(0,name.length()-1);
		if (name.startsWith(" "))
			 name = name.substring(1,name.length());
		JLabel label = new JLabel(name);
		result.add(label);
		return result;
	}
	
}