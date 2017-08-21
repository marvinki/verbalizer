/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author fpaffrath
 *
 * This class should be used to handle sentences when verbalizing an explanation.
 * Depending on the set language, German or English is generated.
 * 
 */

public class Sentence{

	private TextElementSequence sentence = new TextElementSequence();
			
	private TextElementSequence subjekt = new TextElementSequence();
	private TextElementSequence objekt = new TextElementSequence();
	private TextElementSequence praedikat = new TextElementSequence();
	
	private SentenceOrder order = null;
	
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
	
public Sentence(TextElement subjekt, TextElement objekt, TextElement praedikat, SentenceOrder order) {	
		this.subjekt = (TextElementSequence) subjekt;
		this.setObjekt(objekt);
		this.setPraedikat(praedikat);	
		this.setOrder(order);
//		setSentenceType("default");
	}


public Sentence(TextElementSequence subjekt, TextElementSequence praedikat, TextElementSequence objekt, SentenceOrder order){
	this.subjekt = subjekt;
	this.praedikat = praedikat;
	this.objekt = objekt;
	this.setOrder(order);
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
	
	public void makeABisSentence(){
		setPraedikat(new LogicElement(LogicLabels.getString("is")));
		//German Sentences
		if(lang == Locale.GERMAN){				
		 	sentence.add(subjekt);
			sentence.add(objekt);
			sentence.add(praedikat);
			sentence.add(new TextElement("TEST"));
			
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
	
	public void makeSideSentence(){
		//German Sentences
		if(lang == Locale.GERMAN){
			sentence.add(subjekt);
			sentence.add(objekt);
			sentence.add(praedikat);
			
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
	
	
	/**
	 * @return the generated sentence as TextElementSequence
	 */
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

	
	/**
	 * @return When recursion is used with sentences, this can be used to access the plain content independent of language
	 */
	public TextElementSequence toTextElementSequence(){		
		if(order!=null){
			switch(getOrder()){
			case A_B_is:
				sentence.add(subjekt);
				sentence.add(objekt);	
				sentence.add(praedikat);
				break;
				
			case A_is_B:
				sentence.add(subjekt);
				sentence.add(praedikat);
				sentence.add(objekt);	
				break;
				
			case is_A_B:
				sentence.add(praedikat);
				sentence.add(subjekt);
				sentence.add(objekt);	
				break;
				
			default:
				sentence.add(subjekt);
				sentence.add(praedikat);
				sentence.add(objekt);		
			}	
		}
		
		if (sentence==null || isEmpty(sentence) || sentence.size()<1){
			return null;
		}
		
		return sentence;
	}
	
	/**
	 * This method can be used to set a sentence. (This should not be necessary)
	 * @param sentence  
	 */
	public void setSentence(TextElementSequence sentence) {
		this.sentence = sentence;
	}


	public TextElement getSubjekt() {
		return subjekt;
	}


	public void setSubjekt(TextElement subject) {
		 this.subjekt.add(subject);
	}

	public void setSubjekt(List <TextElement> subjectList) {
		
		 this.subjekt.addAll(subjectList);
	}
	
	public TextElement getObjekt() {
		return objekt;
	}

	public void setObjekt(TextElement objekt) {
		
		this.objekt.add(objekt);;
	}
	
   public void setObjekt(List <TextElement> objekt) {
		
		this.objekt.addAll(objekt);;
	}

	public TextElement getPraedikat() {
		return praedikat;
	}

	public void setPraedikat(TextElement praedikat) {
		this.praedikat.add(praedikat);;
	}
	
	public void setPraedikat(List <TextElement> praedikat) {
		this.praedikat.addAll(praedikat);;
	}

	/**
	 * Concatenates the clause to the existing sentence.
	 * @param clause
	 */
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
	
	
	/**
	 * For each sentence part, the method adds the corresponding parts of the
	 * argument
	 * @param sentence
	 */
	public void concat(Sentence sentence) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String result = "";
		result = "Sentence: " + sentence.toString() + "\n";
		result = result + subjekt.toString() + " -- " + praedikat.toString() + " -- " + objekt.toString();
		return result;
	}	


	public SentenceOrder getOrder() {
		return order;
	}

	public void setOrder(SentenceOrder order) {
		this.order = order;
	}
	
}
