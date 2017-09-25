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
	
	/**
	 * Generates a default sentence depending on the set language. (actually in both German and English , it is "Subject predicate object")
	 */
	public void makeDefaultSentence(){
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
			
	}
	
	
	/**
	 * Generates a "A is B" sentence depending on the set language, different words for "is" is used.
	 * The SentencesOrder is set respectively
	 */
	public void makeAisBSentence(){
		
		setOrder(SentenceOrder.A_is_B);
//		setPraedikat(new LogicElement(LogicLabels.getString("is")));
	 	
		this.add(subjekt);
		this.add(praedikat);
		this.add(objekt);
		
	
	}
	
	/**
	 * see makeAisBSentence()
	 */
	public void makeABisSentence(){
		
		setOrder(SentenceOrder.A_B_is);
//		setPraedikat(new LogicElement(LogicLabels.getString("is")));
			
	 	this.add(subjekt);
		this.add(objekt);
		this.add(praedikat);
	
	}
	
	/**
	 * see makeAisBSentence()
	 */
	public void makeisABSentence() {
		
		
		// TODO Auto-generated method stub
		setOrder(SentenceOrder.is_A_B);
//		setPraedikat(new LogicElement(LogicLabels.getString("is")));			
		 	
		this.add(praedikat);
		this.add(subjekt);
		this.add(objekt);
			
			
	}
	
	
	/**
	 * see makeAisBSentence()
	 * "According to it's definition..." (and German translation) is used
	 */
	public void makeAccordingToItsDefSentence(){
		setOrder(SentenceOrder.noOrder);
		
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
	
	/**
	 * see makeAccordingToItsDefSentence()
	 */
	public void makeThusSentence(){
		setOrder(SentenceOrder.noOrder);
		
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
	
	/**
	 * see makeAisBSentence()
	 */
	public void makeSideSentence(){
		
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
	}
	
	/**
	 * see makeAccordingToItsDefSentence()
	 */
	public void makeSinceSentence(){
		setOrder(SentenceOrder.noOrder);
		
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
	
	/**
	 * see makeAccordingToItsDefSentence()
	 */
	public void makebyDefinitionItIsSentence(){
		setOrder(SentenceOrder.noOrder);	
		this.add(new LogicElement(LogicLabels.getString("byDefinitionItIs")));
		
		//German Sentences
		if(lang == Locale.GERMAN){
			// this.add(new LogicElement(", "));
			this.add(new LogicElement(","));  // <-- spaces are added after all sentences automatically, no matter what language
			this.add(subjekt);		
		}

		//English Sentence
		if(lang == Locale.ENGLISH){
			this.add(subjekt);
		}	
	}
	
	/**
	 * generates Sentences depending on the set SentenceOrder.
	 * if there's no Order set, makeDefaultSentence() is called.
	 */
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
	}

	
	/**
	 * @return the generated sentence as TextElementSequence. This is supposed to be the best way
	 * to get the Sentence
	 */
	public Sentence getSentence() {
		
		
		if(order !=null && order != SentenceOrder.noOrder){
			makeOrderedSentence();
		}else if(order == null){
			makeDefaultSentence();
		}else{
			
		}
		
//		cleanSentence();
				
		return this;
	}
	
	/**
	 * cleans up the {@link Sentence} to get rid of unnecessary white spaces.
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
	 * This method can be used to set a Sentence. (This should not be necessary)
	 * @param s  TODO
	 */
	public void setSentence(TextElementSequence s) {
		this.add(s);
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
		result = result + subjekt.toString() + " -- " + praedikat.toString() + " -- " + objekt.toString();
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
