/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

/**
 * @author fpaffrath
 *
 */
public enum Language {
	ENGLISH("english", "en"),
	GERMAN("deutsch", "de");
	
	private String lang;
	private String abbr;
	
	private Language(String s, String a){
		lang = s;
		abbr = a;
	}
	
	public String toString(){
		return lang;
	}
	
	public String toAbbr(){
		return abbr;
		
	}
}
