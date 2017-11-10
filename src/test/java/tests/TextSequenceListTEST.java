package tests;

import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextSequenceList;

public class TextSequenceListTEST {

	/**
	 * the only purpose of this class is to test the verbaliser.TextSequenceList
	 * @param args no args needed
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/**
		 * create a list using the constructor, add, set and get elements of this list
		 */
		
		/*
		 * pretests
		 */
		
		TextElement text1 = new TextElement("foo1");
		TextElement text2 = new TextElement("foo2");
		TextElement isText = new TextElement("is");
		
		System.out.println("\n...testing TextElement toString():\n");
		System.out.println(text2.toString());
		
		System.out.println("\n...testing TextElement toHTML():\n");
		System.out.println(text2.toHTML());
		
		
		TextElementSequence testSequence = new TextElementSequence();
		testSequence.add(text1);
		testSequence.add(isText);
		testSequence.add(text2);
		
		TextElementSequence testSequence2 = new TextElementSequence();
		testSequence2.add(text2);
		testSequence2.add(isText);
		testSequence2.add(text1);
		
		System.out.println("\n...testing TextElementSequence toString():\n");
		System.out.println(testSequence.toString());
		
		
		/*
		 * TextSequenceList tests
		 */
		
		TextSequenceList testList = new TextSequenceList();
		try{
		TextElement testElement = testList.getElement(1);
		System.out.println("\n...testing empty TextSequenceList element.toString():\n");
		System.out.println(testElement.toString());
		}catch (Exception e) {
			System.out.println("\n # Error when referencing an empty TextSequence List \n");
		}
		
		
		testList.add(testSequence);
		testList.add(testSequence2);
		testList.add(testSequence2);
		testList.add(testSequence2);
		
		System.out.println("\n...size of my test list:\n");
		System.out.println(testList.getSize());
		
		
		
		TextElement testElement2 = testList.getElement(1);
		System.out.println("\n...testing element2 TextSequenceList element.toString():\n");
		System.out.println(testElement2.toString());
		
		
		System.out.println("\n...testing TextSequenceList toString():\n");
		System.out.println(testList.toString());
		
		System.out.println("\\\"");
		System.out.println("\\\"".replaceAll("\\\\\"", "\""));
		
	}

}
