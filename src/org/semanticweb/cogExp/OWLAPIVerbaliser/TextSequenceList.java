package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

/**
 * contains zero or more TextElementSequences. All you can do is create a list,
 * add and get/set to elements in this List.
 * 
 * @author fpaffrath
 *
 */
public class TextSequenceList extends TextElement {

	private List<TextElementSequence> list = new ArrayList<TextElementSequence>();

	/**
	 * @param list
	 *            List of TextElements
	 */
	public TextSequenceList(List<TextElementSequence> list) {
		this.list = list;
	}

	public TextSequenceList() {
	}

	/**
	 * @param index
	 *            the index of the desired element
	 * @return returns the element at a certain index
	 */
	public TextElement getElement(int index) {

		TextElement element = new TextElement();

		try {
			element = list.get(index);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return element;
	}

	/**
	 * @return returns the number of elements in the list
	 */
	public int getSize() {
		return list.size();
	}

	/**
	 * @param index
	 *            where the element needs to be set
	 * @param element
	 *            to set
	 */
	public void setElement(int index, TextElementSequence element) {

		try {
			list.set(index, element);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @param sequence
	 *            to add at the end
	 */
	public void add(TextElementSequence sequence) {
		this.list.add(sequence);
	}

	/**
	 * 
	 * @param element
	 *            to add at the end
	 */
	public void add(TextElement element) {
		list.add(new TextElementSequence(element));
	}

	/**
	 * @return returns all elements of the list, that is longer than one element
	 *         as Strings, separated by line breaks: "- element1 \n - element2
	 *         \n - ... - elementN \n " If the list has exactly one element,
	 *         then this function returns only this element ("element1"). If the
	 *         list is empty it returns an empty string ("").
	 */
	@Override
	public String toString() {
		String listSequence = "";

		if (list.size() > 1) {
			for (int i = 0; i < list.size(); i++) {
				listSequence = listSequence + "\n" + " - " + list.get(i).toString();
			}
		}

		if (list.size() == 1) {
			listSequence = list.get(0).toString();
		}

		return listSequence;
	}

	/**
	 * @return returns all elements of the list as HTML list:
	 *         <ul>
	 *         <li>element1</li>
	 *         <li>element2</li> 
	 *         <li> ... </li> 
	 *         <li>elementN</li>
	 *         </ul>
	 * 
	 *         If the list has exactly one element, then this function returns
	 *         only this element. If the list is empty it returns an empty
	 *         string ("").
	 */
	@Override
	public String toHTML() {
		String listSequence = "";
		if (list.size() > 1) {
			listSequence += "<ul>";
			for (int i = 0; i < list.size(); i++) {
				listSequence += "<li>" + list.get(i).toHTML() + "</li>";
			}
			listSequence += "</ul>";
		}

		if (list.size() == 1) {
			listSequence = list.get(0).toHTML();
		}

		return listSequence;
	}
}
