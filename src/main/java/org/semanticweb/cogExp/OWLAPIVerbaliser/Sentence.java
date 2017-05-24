/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author fpaffrath
 *
 */
public class Sentence{

	private TextElementSequence sentence = new TextElementSequence();
			
	private TextElementSequence subjekt = new TextElementSequence();
	private TextElementSequence objekt = new TextElementSequence();
	private TextElementSequence praedikat = new TextElementSequence();
	
	
//	private String sentenceType = "default";
	
	
	private Locale lang = VerbaliseTreeManager.locale;
	private ResourceBundle LogicLabels = VerbaliseTreeManager.LogicLabels;
	/**
	 * 
	 */
	
	public Sentence() {
	}
	
	public Sentence(TextElement subjekt, TextElement objekt, TextElement praedikat) {
		
		this.subjekt = (TextElementSequence) subjekt;
		this.setObjekt(objekt);
		this.setPraedikat(praedikat);
		
//		setSentenceType("default");
	}

	public void makeDefaultSentence(){
		//German Sentences
		if(lang == Locale.GERMAN){	
		
				sentence.add(subjekt);
				sentence.add(praedikat);
				sentence.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
	
				sentence.add(subjekt);
				sentence.add(praedikat);
				sentence.add(objekt);
			}
			
	}
	
	public void makeAisBSentence(){
		setPraedikat(new LogicElement(LogicLabels.getString("is")));
		//German Sentences
		if(lang == Locale.GERMAN){				
		 	sentence.add(subjekt);
			sentence.add(praedikat);
			sentence.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			sentence.add(subjekt);
			sentence.add(praedikat);
			sentence.add(objekt);
		}	
	}
	
	public void makeAccordingToItsDefSentence(){
		sentence.add(new LogicElement(LogicLabels.getString("AccordingToItsDefinition")));
		//German Sentences
		if(lang == Locale.GERMAN){	
		 	sentence.add(praedikat);
			sentence.add(subjekt);
			sentence.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			sentence.add(subjekt);
			sentence.add(praedikat);
			sentence.add(objekt);
		}	
	}
	
	public void makeThusSentence(){
		sentence.add(new LogicElement(LogicLabels.getString("thus")));
		//German Sentences
		if(lang == Locale.GERMAN){	
			sentence.add(praedikat);
			sentence.add(subjekt);
			sentence.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			sentence.add(subjekt);
			sentence.add(praedikat);
			sentence.add(objekt);
		}	
	}
	
	public void makeSinceSentence(){
		sentence.add(new LogicElement(LogicLabels.getString("since")));
		//German Sentences
		if(lang == Locale.GERMAN){	
			sentence.add(praedikat);
			sentence.add(subjekt);
			sentence.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			sentence.add(subjekt);
			sentence.add(praedikat);
			sentence.add(objekt);
		}	
	}
	
	public void makebyDefinitionItIsSentence(){
		sentence.add(new LogicElement(LogicLabels.getString("byDefinitionItIs")));
		//German Sentences
		if(lang == Locale.GERMAN){
			sentence.add(new LogicElement(", "));
			sentence.add(subjekt);		
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			sentence.add(subjekt);
		}	
	}
	
	
	public TextElementSequence getSentence() {
		return sentence;
	}


	public void setSentence(TextElementSequence sentence) {
		this.sentence = sentence;
	}


	public TextElement getSubjekt() {
		return subjekt;
	}


	public void setSubjekt(TextElement subject) {
		 this.subjekt.add(subject);
	}

	public TextElement getObjekt() {
		return objekt;
	}

	public void setObjekt(TextElement objekt) {
		
		this.objekt.add(objekt);;
	}

	public TextElement getPraedikat() {
		return praedikat;
	}

	public void setPraedikat(TextElement praedikat) {
		this.praedikat.add(praedikat);;
	}

	public TextElementSequence concat(TextElementSequence clause){
		sentence.add(clause);
		return clause;
	}
	
//	public String getSentenceType() {
//		return sentenceType;
//	}
//
//	public void setSentenceType(String sentenceTyoe) {
//		this.sentenceType = sentenceTyoe;
//	}

	
}
