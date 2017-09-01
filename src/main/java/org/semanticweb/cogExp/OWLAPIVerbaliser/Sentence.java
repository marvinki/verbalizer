/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
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

public class Sentence extends TextElementSequence{

				
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
	
	public Sentence(TextElementSequence seq){
		this.add(seq);
	}
	
	public Sentence(TextElement el){
		this.add(el);
	}
	
	public Sentence(TextElement subjekt, TextElement objekt, TextElement praedikat) {
		
		this.setSubjekt(subjekt);
		this.setObjekt(objekt);
		this.setPraedikat(praedikat);
		
		makeDefaultSentence();
	}
	
public Sentence(TextElement subjekt, TextElement objekt, TextElement praedikat, SentenceOrder order) {	
		this.setSubjekt(subjekt);
		this.setObjekt(objekt);
		this.setPraedikat(praedikat);	
		this.setOrder(order);
		
		makeOrderedSentence();

//		setSentenceType("default");
	}



public Sentence(TextElementSequence subjekt, TextElementSequence praedikat, TextElementSequence objekt, SentenceOrder order){
	this.subjekt = subjekt;
	this.praedikat = praedikat;
	this.objekt = objekt;
	this.setOrder(order);
	
	makeOrderedSentence();

}
	

	public Sentence(TextElementSequence subjekt, TextElementSequence praedikat, TextElementSequence objekt){
		this.subjekt = subjekt;
		this.praedikat = praedikat;
		this.objekt = objekt;
		
		makeDefaultSentence();

	}
	
	public void makeDefaultSentence(){
		//German Sentences
		if(lang == Locale.GERMAN){	
		
				this.add(subjekt);
				this.add(praedikat);
				this.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
	
				this.add(subjekt);
				this.add(praedikat);
				this.add(objekt);
			}
			
	}
	
	public void makeAisBSentence(){
		setPraedikat(new LogicElement(LogicLabels.getString("is")));
	 	
		this.add(subjekt);
		this.add(praedikat);
		this.add(objekt);
		
	
	}
	
	public void makeABisSentence(){
		setPraedikat(new LogicElement(LogicLabels.getString("is")));
			
	 	this.add(subjekt);
		this.add(objekt);
		this.add(praedikat);
	
	}
	
	public void makeisABSentence() {
		// TODO Auto-generated method stub
		setPraedikat(new LogicElement(LogicLabels.getString("is")));			
		 	
		this.add(praedikat);
		this.add(subjekt);
		this.add(objekt);
			
			
	}
	
	
	
	public void makeAccordingToItsDefSentence(){
		this.add(new LogicElement(LogicLabels.getString("AccordingToItsDefinition")));
		//German Sentences
		if(lang == Locale.GERMAN){	
		 	this.add(praedikat);
			this.add(subjekt);
			this.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			this.add(subjekt);
			this.add(praedikat);
			this.add(objekt);
		}	
	}
	
	public void makeThusSentence(){
		this.add(new LogicElement(LogicLabels.getString("thus")));
		//German Sentences
		if(lang == Locale.GERMAN){	
			this.add(praedikat);
			this.add(subjekt);
			this.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			this.add(subjekt);
			this.add(praedikat);
			this.add(objekt);
		}	
	}
	
	public void makeSideSentence(){
		//German Sentences
		if(lang == Locale.GERMAN){
			this.add(subjekt);
			this.add(objekt);
			this.add(praedikat);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			this.add(subjekt);
			this.add(praedikat);
			this.add(objekt);
		}	
	}
	
	public void makeSinceSentence(){
		this.add(new LogicElement(LogicLabels.getString("since")));
		//German Sentences
		if(lang == Locale.GERMAN){	
			this.add(praedikat);
			this.add(subjekt);
			this.add(objekt);
			
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			this.add(subjekt);
			this.add(praedikat);
			this.add(objekt);
		}	
	}
	
	public void makebyDefinitionItIsSentence(){
		this.add(new LogicElement(LogicLabels.getString("byDefinitionItIs")));
		//German Sentences
		if(lang == Locale.GERMAN){
			this.add(new LogicElement(", "));
			this.add(subjekt);		
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			this.add(subjekt);
		}	
	}
	
	protected void makeOrderedSentence() {
		// TODO Auto-generated method stub
		if(order!=null){
			switch(getOrder()){
			case A_B_is:
				this.add(subjekt);
				this.add(objekt);	
				this.add(praedikat);
				break;
				
			case A_is_B:
				this.add(subjekt);
				this.add(praedikat);
				this.add(objekt);	
				break;
				
			case is_A_B:
				this.add(praedikat);
				this.add(subjekt);
				this.add(objekt);	
				break;
				
			default:
				this.add(subjekt);
				this.add(praedikat);
				this.add(objekt);		
			}	
		}
	}

	
	/**
	 * @return the generated sentence as TextElementSequence
	 */
	public Sentence getSentence() {
//		if(this == null){
//			this.add(new TextElement(""));
//			return this;
//		}
//		if(!isEmpty(this)){
//			return this;
//		}
//		if(this.size() == 1){
//			makeDefaultSentence();
//		}
		if(order !=null){
			makeOrderedSentence();
		}else{
			makeDefaultSentence();
		}
		
		return this;
	}

	
	/**
	 * @return When recursion is used with sentences, this can be used to access the plain content independent of language
	 */
	public TextElementSequence toTextElementSequence(){		
		if(order!=null){
			switch(getOrder()){
			case A_B_is:
				this.add(subjekt);
				this.add(objekt);	
				this.add(praedikat);
				break;
				
			case A_is_B:
				this.add(subjekt);
				this.add(praedikat);
				this.add(objekt);	
				break;
				
			case is_A_B:
				this.add(praedikat);
				this.add(subjekt);
				this.add(objekt);	
				break;
				
			default:
				this.add(subjekt);
				this.add(praedikat);
				this.add(objekt);		
			}	
		}
		
		if (this==null || isEmpty(this) || this.size()<1){
			return null;
		}
		
		return this;
	}
	
	/**
	 * This method can be used to set a this. (This should not be necessary)
	 * @param this  
	 */
	public void setSentence(TextElementSequence s) {
		this.add(s);
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
		this.add(clause);
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
		this.add(sentence);
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
//	public String toString(){
////		result = "Sentence: " + this.toString() + "\n";
////		result = result + subjekt.toString() + " -- " + praedikat.toString() + " -- " + objekt.toString();
//		return this.toString();
//	}	

	public List<TextElement> toList(){
		List<TextElement> list = new ArrayList<TextElement>();
//		list.add(new TextElement("example text"));
		list.add(this);		
		return list;
		
	}

	public SentenceOrder getOrder() {
		return order;
	}

	public void setOrder(SentenceOrder order) {
		this.order = order;
	}
}
