package org.semanticweb.cogExp.examples;

import java.util.List;
import java.util.Locale;

import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;

public class RunThroughOntologyAxioms {
	
	public static void main(String[] args){
	
		VerbaliseTreeManager.setLocale(Locale.ENGLISH);	
		List<String> sts = VerbalisationManager.verbalizeAllOntologyAxiomsAsStrings("U:\\project\\HI-013\\sparql_output.ttl");
		for (String st:sts){
			 System.out.println(st);
		}
	
	}
	
	
}
