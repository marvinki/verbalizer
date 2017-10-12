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

import org.protege.editor.owl.OWLEditorKit;

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
		
	/**
	 * @return a list of (click-able) JLabels.
	 */
	@Override
	public List<JLabel> toJLabel(OWLEditorKit ek){
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
		LinkedJLabel contentlabel = new LinkedJLabel(ek);
		contentlabel.setText(name);
		JLabel spacelabel = new JLabel(" ");
		articlelabel.setBorder(new EmptyBorder(0,0,0,0));
		contentlabel.setBorder(new EmptyBorder(0,0,0,0));
		contentlabel.setForeground(Color.BLUE);
		contentlabel.setToolTipText(tooltiptext);
		labellist.add(articlelabel);
		labellist.add(spacelabel);
		labellist.add(contentlabel);
		
	/*	This code could be utilized if a class element is clicked
	 * 
		contentlabel.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		       //run this code when mouse is clicked
		        
		    	System.out.println("mouse clicked on class label\n");
		    		       
		       JFrame frame = new JFrame("Class Frame");
		       JPanel panel = new JPanel();
		       
		       panel.add(articlelabel);		      
		       panel.add(spacelabel);
		       panel.add(contentlabel);
		       
		       frame.add(panel);
		       
		       frame.setSize(500, 50);
			       
		       Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		       int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		       int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		       frame.setLocation(x, y);
		       
		       frame.setVisible(true);
		       
		    }  
		    
		}); 
		*/
		return labellist;
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