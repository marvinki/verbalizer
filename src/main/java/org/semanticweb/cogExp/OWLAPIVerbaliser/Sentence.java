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
	// private ResourceBundle LogicLabels = VerbalisationManager.LogicLabels;
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

	public Sentence(TextElementSequence subjekt, TextElementSequence praedikat, TextElementSequence objekt){
		this.subjekt = subjekt;
		this.praedikat = praedikat;
		this.objekt = objekt;
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
		setPraedikat(new LogicElement(VerbalisationManager.LogicLabels.getString("is")));
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
		sentence.add(new LogicElement(VerbalisationManager.LogicLabels.getString("AccordingToItsDefinition")));
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
		sentence.add(new LogicElement(VerbalisationManager.LogicLabels.getString("thus")));
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
	
	public void makeSideSentence(){
		//German Sentences
		if(lang == Locale.GERMAN){
			sentence.concat(subjekt); // <--- use "add" instead to achieve deep nesting
			sentence.concat(objekt);
			sentence.concat(praedikat);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			sentence.concat(subjekt);
			sentence.concat(praedikat);
			sentence.concat(objekt);
		}	
	}
	
	public void makeSinceSentence(){
		sentence.add(new LogicElement(VerbalisationManager.LogicLabels.getString("since")));
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
		System.out.println("makebyDefinitionItIsSentence (1)");
		sentence.add(new LogicElement(VerbalisationManager.LogicLabels.getString("byDefinitionItIs")));
		//German Sentences
		System.out.println("makebyDefinitionItIsSentence (2)");
		if(lang == Locale.GERMAN){
			sentence.add(new LogicElement(", "));
			sentence.add(subjekt);		
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			sentence.add(subjekt);
		}	
		System.out.println("makebyDefinitionItIsSentence (3)");
	}
	
	
	public TextElementSequence getSentence() {
		if(sentence == null){
			sentence.add(new TextElement(""));
			return sentence;
		}
		if(!isEmpty(sentence)){
			return sentence;
		}
		if(sentence.size() == 1){
			makeDefaultSentence();
		}
		return sentence;
	}

	/*
	 *  When recursion is used with sentences, this can be used to access the plain content independent of language
	 */
	public TextElementSequence toTextElementSequence(){
		if (sentence==null || isEmpty(sentence) || sentence.size()<1){
			TextElementSequence seq = new TextElementSequence();
			seq.concat(subjekt);
			seq.concat(praedikat);
			seq.concat(objekt);
			return seq;
		}
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

	public void concat(TextElementSequence clause){
		sentence.add(clause);
		return;
	}
	
	private boolean isEmpty(TextElementSequence sentence){
		
		for(TextElement el : sentence.getTextElements()){
			if(el.toString() == ""){
				return true;
			}
				
		}
		
		
		return false;
		
	}
	
	public void addToSubject(TextElementSequence seq){
		subjekt.concat(seq);
	}
	
	public void addToPredicate(TextElementSequence seq){
		praedikat.concat(seq);
	}
	
	public void addToObject(TextElementSequence seq){
		objekt.concat(seq);
	}
	
	/* 
	 * For each sentence part, the method adds the corresponding parts of the argument
	 */
	public void concat(Sentence sentence){
		subjekt.concat(sentence.subjekt);
		praedikat.concat(sentence.praedikat);
		objekt.concat(sentence.objekt);
	}
		
//	public String getSentenceType() {
//		return sentenceType;
//	}
//
//	public void setSentenceType(String sentenceTyoe) {
//		this.sentenceType = sentenceTyoe;
//	}

	public String toString(){
		String result = "";
		result = "Sentence: " + sentence.toString() + "\n";
		result = result + subjekt.toString() + " -- " + praedikat.toString() + " -- " + objekt.toString() 
		+ "\n inspect: " + sentence.inspect();
		return result;
	}	
	
	
}
