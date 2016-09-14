package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

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
				result += "<font>&nbsp;</font>";  // <--- spaces
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
			    if (!WordNetQuery.INSTANCE.isDisabled() && WordNetQuery.INSTANCE.isPlural(lastword)){
			    	// System.out.println("yes, plural.");
			    	// System.out.println("Current element " + current_element);
			    	if (current_element instanceof LogicElement){
			    		LogicElement currLogic = (LogicElement) current_element;
			    		String currString = current_element.toString();
			    		// System.out.println("current element: " + currString);
			    		if (currString.equals("is")){
			    			sequence.remove(i);
			    			sequence.add(i,new LogicElement("are"));
			    		}
			    	} 
			    	if (current_element instanceof RoleElement){
			    		RoleElement currRole = (RoleElement) current_element;
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
