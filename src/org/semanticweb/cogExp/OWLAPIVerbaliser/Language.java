/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

/**
 * @author fpaffrath
 *
 */
public enum Language {
	ENGLISH("english"),
	GERMAN("deutsch");
	
	private String lang;
	
	private Language(String s){
		lang = s;
	}
	
	public String toString(){
		return lang;
	}
}
