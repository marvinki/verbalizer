package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

/**
 * contains zero or more TextElementSequences. All you can do is create a list, add and get/set 
 * to elements in this List. 
 * @author fpaffrath
 *
 */
public class TextSequenceList extends TextElementSequence{
	
	private List<TextElementSequence> list = new ArrayList<TextElementSequence>();

	
	/**
	 * @param list List of TextElements
	 */
	public TextSequenceList(List<TextElementSequence> list) {
		this.list = list;
	}

	
	public TextSequenceList(){}
	
	
	/**
	 * @param index the index of the desired element
	 * @return the element at index
	 */
	public TextElementSequence getElement(int index){
		
		TextElementSequence element = new TextElementSequence();
		
		try{
			element = list.get(index);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return element;
	}
	
	
	/**
	 * @return number of elements in the list
	 */
	public int getSize(){
		return list.size();
	}
	
	
	/**
	 * @param index where the element needs to be set
	 * @param element  to set
	 */
	public void setElement( int index, TextElementSequence element){
		
		try {
			list.set(index, element);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	/**
	 * @param element to add at the end
	 */
	public void add(TextElementSequence element){
		list.add(element);
	}
	
	
	/**
	 * @return all elements of the list as Strings, separated by line breaks
	 */
	@Override
	public String toString(){
		String listSequence = "";
		for(int i=0; i<list.size(); i++){
				 listSequence = listSequence + "\n" + " - " + list.get(i).toString() ;
		}
		return listSequence;
	}
	
	
	/**
	 * 
	 * @return all elements of the list concatinated as one single TextElement
	 */
	public TextElement toTextElement(){
		TextElement te = new TextElement(toString());
		return te;
	}
}
