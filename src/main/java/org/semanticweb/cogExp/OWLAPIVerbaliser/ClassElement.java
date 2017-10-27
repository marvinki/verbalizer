package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;

/** TODO - just for testing
 * just for testing, delete this 5 imports (JFrame, JPanel,...,Toolkit) when not needed anymore

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
 */
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

// import org.protege.editor.owl.OWLEditorKit;

/**
 * represents any class. Use click-ability by utilizing toJLabel().
 * @author marvinki (doc by fp)
 *
 */
/**
 * @author fpaffrath
 *
 */
public class ClassElement extends TextElement{
	
	private String tooltiptext = null;
	
	/**
	 * @param content name
	 */
	public ClassElement(String content){
		super(content);
		
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
	/**
	 * @return returns the content as plain string 
	 */
	public String toString(){		
		return content;
	}
	
	
	/**
	 * @return the tooltip as plain string. Returns null if there is no tooltip.
	 */
	public String getToolTipText(){
		return tooltiptext;
	}
		


	@Override
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
		} else if (content.startsWith("A ")){
					article = "A ";
					name = content.substring(2);
		} else if (content.startsWith("The ")){
			article = "The ";
			name = content.substring(4);
		} else if (content.startsWith("An ")){
			article = "An ";
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
		
		Style style = textPane.addStyle("_style", null);
        StyleConstants.setForeground(style, Color.blue);
       
        JTextField text1 = new JTextField(str);
        text1.setToolTipText(tooltiptext);
        
        textPane.insertComponent(text1);
				
		return; 
	}
	
}