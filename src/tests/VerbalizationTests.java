package tests;

import java.io.IOException;



import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;

public class VerbalizationTests {

	public static void main(String[] args){
		
  String tmpdir = "";
		
   try {
      tmpdir = WordnetTmpdirManager.makeTmpdir();
       } catch (IOException e) {
      e.printStackTrace();
      }
   WordNetQuery.INSTANCE.setDict(tmpdir);
   System.out.println("TMPDIR is : " + tmpdir);
		
   testFor("man", false);
   testFor("men", true);
   testFor("male", false);
   testFor("women", true);
   testFor("female", false);
   testFor("wife", true);
  }
	
  public static void testFor(String testString, boolean isPlural){
	
	   System.out.println("\n*\n*"+ testString.toUpperCase() +"\n*");
	   int[] types = WordNetQuery.INSTANCE.getTypes(testString);
	   System.out.println("Is plural ("+ testString +"): " +
	   WordNetQuery.INSTANCE.isPlural(testString));
	   
	   System.out.print("types: (");
	   for(int type : types){
		   System.out.print(type+" ");
	   }
	   System.out.println(")");
	   System.out.println(VerbalisationManager.aOrAnIfy("body structure"));
	   
	   if(isPlural == WordNetQuery.INSTANCE.isPlural(testString))
	   {
	   		System.out.println("number test OK.");
	   }else{
		   	System.out.println("number test failed.");
	   		}
	   
	  System.out.println("isType NOUN count :" +WordNetQuery.INSTANCE.isType(testString, SynsetType.NOUN) );
	  System.out.println("isType ADJECTIVE count :" +WordNetQuery.INSTANCE.isType(testString, SynsetType.ADJECTIVE) );
	  System.out.println(" MY isType NOUN count :" +getTypeCount(testString, SynsetType.NOUN) );
	  System.out.println(" MY isType ADJECTIVE count :" +getTypeCount(testString, SynsetType.ADJECTIVE) );
  }
  
  public static int getTypeCount(String testString, SynsetType type){
	int count = 0;
	  
	Synset[] synset = WordNetQuery.INSTANCE.getDatabase().getSynsets(testString);
	for(Synset syn : synset){
		if(syn.getType().equals(SynsetType.NOUN)){
			count++;
		}
	}
	  
	return count;
	  
  }
	
}


