package tests;

import java.util.ArrayList;

import org.semanticweb.cogExp.OWLAPIVerbaliser.ClassElement;
import org.semanticweb.cogExp.OWLAPIVerbaliser.LogicElement;
import org.semanticweb.cogExp.OWLAPIVerbaliser.Sentence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.CompoundSentence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;

public class SentenceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassElement s = new ClassElement("ein Apfel");
		ClassElement o = new ClassElement("eine Obstsorte");
		TextElement p;
		
		Sentence sen = new Sentence(s, o, new LogicElement(" "));
		
		sen.makeAccordingToItsDefSentence();
		
		TextElementSequence array = sen.getSentence();
		
		
		System.out.println(array.toString());
		s = new ClassElement("eine Ameise");
		o = new ClassElement("den Zucker");
		p = new TextElement("frisst");
		
		sen = new Sentence(s, o, p);
		sen.makeDefaultSentence();
		
		array = sen.getSentence();
		
	
			System.out.print(array.toString());
		
		
	}

}
