package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * represents any class
 * @author marvinki (doc by fp)
 *
 */
public class ClassElement extends TextElement{
	
	private String tooltiptext = "footext";
	
	/**
	 * @param content name
	 */
	public ClassElement(String content){
		super(content);
	}
	
	
	/**
	 * @return returns the content as plain string 
	 */
	public String toString(){		
		return content;
	}
	
	public String getToolTipText(){
		return tooltiptext;
	}
	
	/**
	 * 
	 * @param content name
	 * @param tooltiptext optional additional text
	 */
	public ClassElement(String content, String tooltiptext){
		super(content);
		this.tooltiptext = tooltiptext;
	}
	
	@Override
	public List<JLabel> toJLabel(){
		List<JLabel> labellist = new ArrayList<JLabel>();
		
		String article = "";
		String name = "";
		if (content.startsWith("a ")){
			article = "a ";
			name = content.substring(2);
		} else if (content.startsWith("an ")){
			article = "an ";
			name = content.substring(3);
		} else if (content.startsWith("the ")){
			article = "the ";
			name = content.substring(4);
		} else {
			name = content;
		}
		
		if (name.endsWith(" "))
			name = name.substring(0,name.length()-1);
		if (article.endsWith(" "))
			article = article.substring(0,article.length()-1);
		
		
		JLabel articlelabel = new JLabel(article);
		JLabel contentlabel = new JLabel(name);
		JLabel spacelabel = new JLabel(" ");
		articlelabel.setBorder(new EmptyBorder(0,0,0,0));
		contentlabel.setBorder(new EmptyBorder(0,0,0,0));
		contentlabel.setForeground(Color.MAGENTA);
		contentlabel.setToolTipText(tooltiptext);
		labellist.add(articlelabel);
		labellist.add(spacelabel);
		labellist.add(contentlabel);
		return labellist;
	}
	
	public String toHTML(){
		// do not mark the article, therefore separate it
		String article = "";
		String name = "";
		if (content.startsWith("a ")){
			article = "a ";
			name = content.substring(2);
		} else if (content.startsWith("an ")){
			article = "an ";
			name = content.substring(3);
		} else if (content.startsWith("the ")){
			article = "the ";
			name = content.substring(4);
		} else {
			name = content;
		}
		
		return article + "<font color=blue>" + name + "</font>"; 
	}
	
	@Override
	public void addToDocument(JTextPane textPane){
		// do not mark the article, therefore separate it
		String article = "";
		String name = "";
		if (content.startsWith("a ")){
			article = "a ";
			name = content.substring(2);
		} else if (content.startsWith("an ")){
			article = "an ";
			name = content.substring(3);
		} else if (content.startsWith("the ")){
			article = "the ";
			name = content.substring(4);
		} else {
			name = content;
		}
		String str = article + "<font color=blue>" + name + "</font>"; 
		
		StyledDocument document = textPane.getStyledDocument();
		Style style = textPane.addStyle("_style", null);
        StyleConstants.setForeground(style, Color.blue);
       
        JTextField text1 = new JTextField(str);
        text1.setToolTipText(tooltiptext);
        
        textPane.insertComponent(text1);
		
        /*
		try {
			document.insertString(document.getLength(), str, style);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		return; 
	}
	
}