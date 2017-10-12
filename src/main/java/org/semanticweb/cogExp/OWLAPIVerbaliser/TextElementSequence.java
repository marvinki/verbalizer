package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.protege.editor.owl.OWLEditorKit;

/**
 * All textual generated Explanations are passed as Sequences of text elements
 * @author fpaffrath
 *
 */
public class TextElementSequence extends TextElement{

	private List<TextElement> sequence = new ArrayList<TextElement>();
	
	
	/**
	 * @param sequence s
	 */
	public TextElementSequence(List<TextElement> sequence){
		this.sequence = sequence;
	}
	
	/**
	 * @param element e
	 */
	public TextElementSequence(TextElement element){
		this.sequence.add(element);
	}
	
	public TextElementSequence(){
	}
	
		
	public String inspect(){
		String result = ">";
		for(TextElement elem : sequence){
			result += elem.toString();
			result +="|";
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toString()
	 */
	public String toString(){
		String result = "";
		boolean needsep = false;
		for(TextElement elem : sequence){
			if (elem.content.startsWith(","))
				needsep = false;
			if (elem.content.startsWith("."))
				needsep = false;
			if (elem.toString().equals(""))
				needsep = false; /// <--- avoid creating spaces with "sentences" that are not fully instantiated
			if (needsep)
				result += " ";
			result += elem.toString();
			needsep = true;
		}
		result.trim();
		return result;
	}
	
	public String inspect2(){
		String result = "";
		for(TextElement elem : sequence){
			result += "Element " + elem.getClass() + " : " + elem.content + "\n";
		}
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toHTML()
	 */
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
	
		
	/**
	 * @param el self explanatory
	 */
	public void add(TextElement el){
		sequence.add(el);	
	}
	
	/**
	 * @param textList self explanatory
	 */
	public void addAll(List<TextElement> textList) {
		// TODO Auto-generated method stub
		sequence.addAll(textList);
	}
	
	/**
	 * @param seq2 self explanatory
	 */
	public void concat(TextElementSequence seq2){
	if(seq2!=null) 
			sequence.addAll(seq2.sequence);	
	}
	
	/**
	 * Makes the first letter of the contained String capital.
	 * If the string is empty, it does nothing.
	 */
	public void makeUppercaseStart(){
		TextElement start;
		if (sequence.get(0)==null)
			return;
		start = sequence.get(0);
		String str = start.content;
		str= VerbaliseTreeManager.makeUppercaseStart(str);
		start.content = str;
	}
	
	/**
	 * @return self explanatory
	 */
	public List<TextElement> getTextElements(){
		return sequence;
	}
	
	public int size(){
		int count = 0;
		for(TextElement el : this.getTextElements()){
			
			if(el.toString() != ""){
				count++;
			}
		}
		
		return count;
		
	}
	
	
	/**
	 * TODO add description 
	 */
	public void pluralise(){
		TextElement previous = null;
		boolean somethingthat = false;
		for (int i=0; i<sequence.size();i++){
			TextElement current_element = sequence.get(i);
			if (current_element.toString().contains("Something that")){
				somethingthat = true;
			}
			if (previous instanceof ClassElement && !somethingthat){
				ClassElement previousClass = (ClassElement) previous;
				String previousString = previousClass.toString();
				String[] arr = previousString.split(" ");  	
			    String lastword = arr[arr.length-1];
			    if (!WordNetQuery.INSTANCE.isDisabled() && WordNetQuery.INSTANCE.isPlural(lastword) && WordNetQuery.INSTANCE.isKnown("lastword")){
			    	if (current_element instanceof LogicElement){
			    		String currString = current_element.toString();
			    		if (currString.equals("is")){
			    			sequence.remove(i);
			    			sequence.add(i,new LogicElement("are"));
			    		}
			    	} 
			    	if (current_element instanceof RoleElement){
			    		String currString = current_element.toString();
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

	public JSONArray toJSON(){
		JSONArray result = new JSONArray();
		// JSONObject result = new JSONObject();
		int itemcounter = 0;
		for (TextElement element : sequence){
			
			System.out.println("dealing with " + element + " " + element.getClass());
			
			if (element instanceof ClassElement){
				ClassElement classElement = (ClassElement) element;
				String classDescription = classElement.toString();
				String tooltip = classElement.getToolTipText();
				JSONObject innerobject = new JSONObject();
				innerobject.put("text", classDescription);
				innerobject.put("classTooltip", tooltip);
				innerobject.put("type", "class-description");
				result.put(innerobject);
				itemcounter++;
			}
			if (element instanceof LogicElement){
				LogicElement logicElement = (LogicElement) element;
				String logicDescription = logicElement.toString();
				JSONObject innerobject = new JSONObject();
				innerobject.put("text", logicDescription);
				innerobject.put("type", "text");
				result.put(innerobject);
				itemcounter++;
			}
			if (element instanceof RoleElement){
				RoleElement roleElement = (RoleElement) element;
				String roleDescription = roleElement.toString();
				JSONObject innerobject = new JSONObject();
				innerobject.put("text", roleDescription);
				innerobject.put("type", "roleDescription");
				result.put(innerobject);
				itemcounter++;
			}
			if (element instanceof ConclusionMarkerElement){
				ConclusionMarkerElement cme = (ConclusionMarkerElement) element;
				JSONArray recursiveResult = cme.toJSON();
				for (Object io : recursiveResult){
					result.put(io);
				}
				itemcounter++;
			}
			
			if (element instanceof TextElementSequence){
				TextElementSequence innersequence = (TextElementSequence) element;
				JSONArray innerarray = innersequence.toJSON();
				for (Object ob : innerarray){
					JSONObject oj = (JSONObject) ob;
					result.put(oj);
				}
			}
			
			if (element instanceof TextElement){
				TextElement textElement = (TextElement) element;
				String textDescription = textElement.toString();
				JSONObject innerobject = new JSONObject();
				innerobject.put("text", textDescription);
				// innerobject.put("classTooltip", tooltip);
				innerobject.put("type", "text");
				result.put(innerobject);
				itemcounter++;
			}
		
		}
		return result;
}
	
}
