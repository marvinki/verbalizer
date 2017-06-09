package tests;

import java.io.IOException;


import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;


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
		
   testFor("a man", false, true, false);
   testFor("men", true, true, false);
   testFor("male", false, true, true);
   testFor("women", true, true, false);
   testFor("female", false, true, true);
   testFor("wife", false, true, false);
  }
	
  public static void testFor(String testString, boolean isPlural, boolean isNoun, boolean isAdj){
	
	   System.out.println("\n*\n*"+ testString.toUpperCase() +"\n*");
	   int[] types = WordNetQuery.INSTANCE.getTypes(testString);
	   System.out.println("Is plural ("+ testString +"): " +
	   WordNetQuery.INSTANCE.isPlural(testString));
	   
	   System.out.print("types: (");
	   for(int type : types){
		   System.out.print(type+" ");
	   }
	   System.out.println(")");
	   
	   //Number Test
	   if(isPlural == WordNetQuery.INSTANCE.isPlural(testString))
	   {
	   		System.out.println("number test \t\t OK.");
	   }else{
		   	System.out.println("number test \t\t failed.");
	   		}
	   

	   //Noun Test
	   if(isNoun == (0<WordNetQuery.INSTANCE.isType(testString, SynsetType.NOUN)))
	   {
	   		System.out.println("NOUN test \t\t OK.");
	   }else{
		   	System.out.println("NOUN test \t\t failed.");
	   		}
	   
	 //Adjective Test
	   if(isAdj == (0<WordNetQuery.INSTANCE.isType(testString, SynsetType.ADJECTIVE)))
	   {
	   		System.out.println("ADJECTIVE test \t\t OK.");
	   }else{
		   	System.out.println("ADJECTIVE test \t\t failed.");
	   		}
	   
	   
//	  System.out.println("isType NOUN count :" +WordNetQuery.INSTANCE.isType(testString, SynsetType.NOUN) );
//	  System.out.println("isType ADJECTIVE count :" +WordNetQuery.INSTANCE.isType(testString, SynsetType.ADJECTIVE) );
 
	  System.out.println(VerbalisationManager.aOrAnIfy(testString));
  }
  
  

	
}



