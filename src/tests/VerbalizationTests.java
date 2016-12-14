package tests;

import java.io.IOException;

import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;

public class VerbalizationTests {

	public static void main(String[] args){
		
		String tmpdir = "";
		
		try {
			tmpdir = WordnetTmpdirManager.makeTmpdir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WordNetQuery.INSTANCE.setDict(tmpdir);
		
	
		System.out.println(VerbalisationManager.aOrAnIfy("body structure"));
		
	}
	
	
}
