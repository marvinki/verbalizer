/*
 *     Copyright 2012-2018 Ulm University, AI Institute
 *     Main author: Marvin Schiller, contributors: Felix Paffrath, Chunhui Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

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
	// private ResourceBundle LogicLabels = VerbalisationManager.LogicLabels;
	/**
	 * 
	 */
	private boolean sentenceMade = false;
	
	public Sentence() {
	}
	
	public Sentence(TextElementSequence seq){	
		this.add(seq);
		sentenceMade = true;
	}
	
	public Sentence(TextElement el){
		this.add(el);
		sentenceMade = true;
	}
	
	public Sentence(TextElement subjekt, TextElement objekt, TextElement praedikat) {
		
		this.setSubjekt(subjekt);
		this.setObjekt(objekt);
		this.setPraedikat(praedikat);
				
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
	
	/**
	 * Generates a default sentence depending on the set language. (actually in both German and English , it is "Subject predicate object")
	 */
	public void makeDefaultSentence(){
		if(sentenceMade) return;
		//German Sentences
		setOrder(SentenceOrder.noOrder);
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
		
		sentenceMade = true;
			
	}
	
	
	/**
	 * Generates a "A is B" sentence depending on the set language, different words for "is" is used.
	 * The SentencesOrder is set respectively
	 */
	public void makeAisBSentence(){
		if(sentenceMade) return;
		
		setOrder(SentenceOrder.A_is_B);
//		setPraedikat(new LogicElement(LogicLabels.getString("is")));
	 	
		this.add(subjekt);
		this.add(praedikat);
		this.add(objekt);
		
		sentenceMade = true;

	}
	
	/**
	 * see makeAisBSentence()
	 */
	public void makeABisSentence(){
		if(sentenceMade) return;
		
		setOrder(SentenceOrder.A_B_is);
//		setPraedikat(new LogicElement(LogicLabels.getString("is")));
			
	 	this.add(subjekt);
		this.add(objekt);
		this.add(praedikat);
	
		sentenceMade = true;

	}
	
	/**
	 * see makeAisBSentence()
	 */
	public void makeisABSentence() {
		if(sentenceMade) return;
		
		// TODO Auto-generated method stub
		setOrder(SentenceOrder.is_A_B);
//		setPraedikat(new LogicElement(LogicLabels.getString("is")));			
		 	
		this.add(praedikat);
		this.add(subjekt);
		this.add(objekt);
			
		sentenceMade = true;

			
	}
	
	
	/**
	 * see makeAisBSentence()
	 * "According to it's definition..." (and German translation) is used
	 */
	public void makeAccordingToItsDefSentence(){
		if(sentenceMade) return;
		
		setOrder(SentenceOrder.noOrder);
		
		this.add(new LogicElement(VerbalisationManager.LogicLabels.getString("AccordingToItsDefinition")));
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
		
		sentenceMade = true;

	}
	
	/**
	 * see makeAccordingToItsDefSentence()
	 */
	public void makeThusSentence(){
		if(sentenceMade) return;
		
		setOrder(SentenceOrder.noOrder);
		
		this.add(new LogicElement(VerbalisationManager.LogicLabels.getString("thus")));
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
		
		sentenceMade = true;

	}
	
	/**
	 * see makeAisBSentence()
	 */
	public void makeSideSentence(){
		if(sentenceMade) return;
		
		setOrder(SentenceOrder.noOrder);
		
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
		
		sentenceMade = true;

	}
	
	/**
	 * see makeAccordingToItsDefSentence()
	 */
	public void makeSinceSentence(){
		if(sentenceMade) return;
		
		setOrder(SentenceOrder.noOrder);
		
		this.add(new LogicElement(VerbalisationManager.LogicLabels.getString("since")));
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
		
		sentenceMade = true;

	}
	
	/**
	 * see makeAccordingToItsDefSentence()
	 */
	public void makebyDefinitionItIsSentence(){
		if(sentenceMade) return;
		
		setOrder(SentenceOrder.noOrder);	
		this.add(new LogicElement(VerbalisationManager.LogicLabels.getString("byDefinitionItIs")));
		
		//German Sentences
		System.out.println("makebyDefinitionItIsSentence (2)");
		if(lang == Locale.GERMAN){
			// this.add(new LogicElement(", "));
			this.add(new LogicElement(","));  // <-- spaces are added after all sentences automatically, no matter what language
			this.add(subjekt);		
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			this.add(subjekt);
		}	
		
		sentenceMade = true;

	}
	
	/**
	 * generates Sentences depending on the set SentenceOrder.
	 * if there's no Order set, makeDefaultSentence() is called.
	 */
	public void makeOrderedSentence() {
		if(sentenceMade) return;
		
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
				
			case noOrder:
				break;
				
			default:
				this.add(subjekt);
				this.add(praedikat);
				this.add(objekt);		
			}	
		}else{
			makeDefaultSentence();
		}
		
		sentenceMade = true;

	}

	
	/**
	 * @return the generated sentence as TextElementSequence. This is supposed to be the best way
	 * to get the Sentence
	 */
	public Sentence getSentence() {
		
		if(!sentenceMade){
			if(order !=null && order != SentenceOrder.noOrder){
				makeOrderedSentence();
			}else if(order == null){
				makeDefaultSentence();
			}
		}
		
//		cleanSentence();
				
		return this;
	}
	
	/**
	 * cleans up the {@link Sentence} to get rid of unnecessary white spaces. -Obsolete-
	 */
	private void cleanSentence(){
		String newSen = "";
		String oldSen = this.toString();
		
		for(int i = 0; i<=oldSen.length(); i++){
			
			if(i<oldSen.length()-1 && (oldSen.charAt(i) == ' ')){
				if(oldSen.charAt(i+1)==' '){
					i+=1;
				}
			}
			
			if(i<oldSen.length()){
					newSen += oldSen.charAt(i);
			}else{
				break;
			}
			
			
		}
		
		
		newSen.trim();
//		this.deleteTextElement();
		this.setSentence(new TextElementSequence(new TextElement(newSen)));
		
		
	}

	
	/**
	 * @return When recursion is used with sentences, this can be used to access the plain content independent of language
	 */
	public TextElementSequence toTextElementSequence(){	
		if (sentenceMade)
			return this;
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
		
		// if (sentence==null || isEmpty(sentence) || sentence.size()<1){
		if (this==null || this.size()<1){
			System.out.println("null case");
			return null;
		}
		
		return this;
	}
	
	/**
	 * This method can be used to set a Sentence. (This should not be necessary)
	 * @param s  TODO
	 */
	public void setSentence(TextElementSequence s) {
		this.add(s);
		sentenceMade=true;
	}


	public TextElement getSubjekt() {
		return subjekt;
	}


	public void setSubjekt(TextElement subject) {
		this.subjekt = new TextElementSequence(new TextElement());  
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
	 * @param clause add description
	 */
	public void concat(TextElementSequence clause){
		
		clause.content.trim();
		
		this.getSentence().add(clause.content);
		return;
	}
	
	public void add(String s){
		s.trim();
		this.add(new TextElement(s));
	}
	
	public void concat(TextElement textElement){
		this.add(textElement);
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
	 * @param sentence that is to be concatenated
	 */
	public void concat(Sentence sentence) {
		this.add(sentence);
	}
	
	/**
	 * TODO add description
	 * @return result
	 */
	public String inspect(){
		String result = "";
		result = "Sentence: " + this.toString() + "\n";
		result = result + subjekt.toString() + " -- " + praedikat.toString() + " -- " + objekt.toString() + "\n";;
		result = result + this.sequence + "\n";
	return result;
}	
	


	/**
	 * @return the sentence as List of TextElements
	 */
	public List<TextElement> toList(){
		List<TextElement> list = new ArrayList<TextElement>();
//		list.add(new TextElement("example text"));
		String s = this.toString();
		String[] sArray = s.split(" ");
		
		for(String str : sArray){
			str.trim();
			list.add(new TextElement(str));
		}
		//		list.add(this);		
		return list;
		
	}

	public SentenceOrder getOrder() {
		return order;
	}

	public void setOrder(SentenceOrder order) {
		this.order = order;
	}

	public List<TextElement> trim() {
		// TODO Auto-generated method stub
		return null;
	}
}
