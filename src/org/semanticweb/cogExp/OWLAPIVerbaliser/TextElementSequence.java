package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.protege.editor.owl.OWLEditorKit;

public class TextElementSequence extends TextElement{

	private List<TextElement> sequence = new ArrayList<TextElement>();
	
	
	public TextElementSequence(List<TextElement> sequence){
		this.sequence = sequence;
	}
	
	public TextElementSequence(TextElement element){
		this.sequence.add(element);
	}
	
	public TextElementSequence(){
	}
	
	public String toString(){
		String result = "";
		boolean needsep = false;
		for(TextElement elem : sequence){
			if (elem.content.startsWith(","))
				needsep = false;
			if (elem.content.startsWith("."))
				needsep = false;
			if (needsep)
				result += " ";
			result += elem.toString();
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
			if (elem.content.startsWith("."))
				needsep = false;
			if (needsep)
				result += " ";
			result += elem.toHTML();
			needsep = true;
		}
		return result;
	}
	
	/**
	 * @param ek OWLEditorKit needed for LinkedJLabel
	 * @return returns a list of JLabels of the TextElementSequence (without empty JLabels or spaces)
	 */
	public List<JLabel> generateLabels(OWLEditorKit ek){
		// add each element in the sequence as JLabel to the List
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
				
		for (TextElement elem : sequence){
			labels.addAll(elem.toJLabel(ek));
		}
		
		//adding commas and periods to the leading label
		for(int i = 1; i<labels.size(); i++){
			if(labels.get(i).getText().equals(" , ")
					||labels.get(i).getText().equals(" ,")
					||labels.get(i).getText().equals(", ")
					||labels.get(i).getText().equals(",")
					||labels.get(i).getText().equals(".")){
				labels.get(i-1).setText(labels.get(i-1).getText()+labels.get(i).getText());
			}
		}
		
		// clean Label List from surplus punctuation marks and spaces
		labels.removeIf(l->l.getText().equals(" "));
		labels.removeIf(l->l.getText().equals(""));
		labels.removeIf(l->l.getText().equals(","));
		labels.removeIf(l->l.getText().equals(" ,"));
		labels.removeIf(l->l.getText().equals(", "));
		labels.removeIf(l->l.getText().equals(" , "));
		labels.removeIf(l->l.getText().equals("."));
		
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
	
	public void pluralise(){
		TextElement previous = null;
		boolean somethingthat = false;
		for (int i=0; i<sequence.size();i++){
			TextElement current_element = sequence.get(i);
			if (current_element.toString().contains("Something that")){
				somethingthat = true;
			}
			// System.out.println("pluralise looking at :" + current_element.toString());
			if (previous instanceof ClassElement && !somethingthat){
				ClassElement previousClass = (ClassElement) previous;
				String previousString = previousClass.toString();
				String[] arr = previousString.split(" ");  	
			    String lastword = arr[arr.length-1];
			    // System.out.println("pluralise checking plurality of :" + lastword);
			    if (WordNetQuery.INSTANCE.isPlural(lastword)){
			    	// System.out.println("yes, plural.");
			    	// System.out.println("Current element " + current_element);
			    	if (current_element instanceof LogicElement){
			    		String currString = current_element.toString();
			    		// System.out.println("current element: " + currString);
			    		if (currString.equals("is")){
			    			sequence.remove(i);
			    			sequence.add(i,new LogicElement("are"));
			    		}
			    	} 
			    	if (current_element instanceof RoleElement){
			    		String currString = current_element.toString();
			    		// System.out.println("current element: " + currString);
			    		// System.out.println("checking " + currString.substring(0,2) + "<");
			    		if (currString.substring(0,2).equals("is")){
			    			sequence.remove(i);
			    			sequence.add(i,new RoleElement("are" + currString.substring(2) ));
			    		}
			    	} 
			    }
			}
			previous = current_element;
		}
	}
	
}
