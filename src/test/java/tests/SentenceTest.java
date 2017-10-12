package tests;



import org.semanticweb.cogExp.OWLAPIVerbaliser.Sentence;
import org.semanticweb.cogExp.OWLAPIVerbaliser.SentenceOrder;

import javax.xml.soap.Text;

import org.semanticweb.cogExp.OWLAPIVerbaliser.ClassElement;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;

public class SentenceTest {

	
	
	public static void main(String[] args) {
		
		
		ClassElement subject = new ClassElement("ein Apfel");
		ClassElement object = new ClassElement("eine Obstsorte");
		TextElement predicate = new TextElement("ist");
		
		Sentence sentence = new Sentence();
		
		sentence.setSubjekt(subject);
		sentence.setObjekt(object);
		sentence.setPraedikat(predicate);
		
		sentence.setOrder(SentenceOrder.A_is_B);
		
		TextElementSequence sequence = sentence.getSentence();

		System.out.println(sequence.toString());
		
		
		Sentence sentence2 = new Sentence(subject, object, predicate);
        sentence2.makeAisBSentence();
		
		TextElementSequence sequence2 = sentence2.getSentence();

		System.out.println(sequence2.toString());
		

		Sentence accordingToItsDefSentence = new Sentence(subject, object, predicate);
		accordingToItsDefSentence.makeAccordingToItsDefSentence();
		
		Sentence byDefItIsSentence = new Sentence(subject, object, predicate);
		byDefItIsSentence.makebyDefinitionItIsSentence();
		
		Sentence thusSentence = new Sentence(subject, object, predicate);
		thusSentence.makeThusSentence();
		
		Sentence sinceSentence = new Sentence(subject, object, predicate);
		sinceSentence.makeSinceSentence();
		
		
		System.out.println(accordingToItsDefSentence.getSentence().toString());
		System.out.println(byDefItIsSentence.getSentence().toString());
		System.out.println(thusSentence.getSentence().toString());
		System.out.println(sinceSentence.getSentence().toString());
		
		
		
		TextElementSequence longSentence = accordingToItsDefSentence.getSentence();
		
		TextElement pred = new TextElement("fällt");
		TextElement ob = new TextElement("nicht weit vom Stamm");
		
		
		Sentence shortSentence = new Sentence();
		shortSentence.setPraedikat(pred);
		shortSentence.setObjekt(ob);
		shortSentence.setSubjekt(subject);
		shortSentence.makeThusSentence();
		
		longSentence.add(new TextElement("."));
		longSentence.add(shortSentence.getSentence());
		
		System.out.println(longSentence.toString());
//		ClassElement s = new ClassElement("ein Apfel");
//		ClassElement o = new ClassElement("eine Obstsorte");
//		TextElement p = new TextElement("ist");
//		
//		Sentence sen = new Sentence();
//		
//		sen.setSubjekt(s);
//		sen.setObjekt(o);
//		sen.setPraedikat(p);
//		
//		sen.makeAccordingToItsDefSentence();
//		
//		TextElementSequence array = sen.getSentence();
//		
//		System.out.println("1.");
//		System.out.println(array.toString());
//		
//		
//		
//		
//		s = new ClassElement("eine Ameise");
//		o = new ClassElement("den Zucker");
//		p = new TextElement("frisst");
//		
//		sen = new Sentence(s, o, p);
//		sen.makeDefaultSentence();
//		
//		array = sen.getSentence();
//		
//		System.out.println("2.");
//		System.out.print(array.toString());
//		
		
		
		
	}

}
