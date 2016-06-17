package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class TextElementSequence {

	private List<TextElement> sequence = new ArrayList<TextElement>();
	
	public TextElementSequence(List<TextElement> sequence){
		this.sequence = sequence;
	}
	
	public TextElementSequence(){
	}
	
	public String toString(){
		String result = "";
		boolean needsep = false;
		for(TextElement elem : sequence){
			if (elem.content.startsWith(","))
				needsep = false;
			if (needsep)
				result += " ";
			result += elem.toText();
			needsep = true;
		}
		return result;
	}
	
	public String toHTML(){
		String result = "";
		boolean needsep = false;
		for(TextElement elem : sequence){
			if (elem.content.startsWith(","))
				needsep = false;
			if (needsep)
				result += " ";
			result += elem.toHTML();
			needsep = true;
		}
		return result;
	}
	
	public List<JLabel> generateLabels(){
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		boolean needsep = false;
		for (TextElement elem : sequence){
			if (elem.content.startsWith(","))
				needsep = false;
			if (needsep)
				labels.add(new JLabel(" ")); 
			labels.addAll(elem.toJLabel());
			needsep = true;
		}
		return labels;
	}
	
	/*
	public void addToDocument(JTextPane textPane){
		boolean needsep = false;
		for(TextElement elem : sequence){
			if (elem.content.startsWith(","))
				needsep = false;
			if (needsep){
				document.insertString(document.getLength(), " ", null);
			}
			elem.addToDocument();
			result += elem.toHTML();
			needsep = true;
		}
		return result;
	}
	*/
	
	public void add(TextElement el){
		sequence.add(el);	
	}
	
	public void concat(TextElementSequence seq2){
		sequence.addAll(seq2.sequence);	
	}
	
	public void makeUppercaseStart(){
		TextElement start;
		if (sequence.get(0)==null)
			return;
		start = sequence.get(0);
		String str = start.content;
		str= VerbaliseTreeManager.makeUppercaseStart(str);
		start.content = str;
	}
	
	public List<TextElement> getTextElements(){
		return sequence;
	}
	
}
