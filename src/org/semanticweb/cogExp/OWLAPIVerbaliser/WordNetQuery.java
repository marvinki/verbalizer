package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

// import org.semanticweb.cogExp.ErrorWriter;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public enum WordNetQuery {
	INSTANCE;
	
	private WordNetDatabase database; // = WordNetDatabase.getFileInstance();
	private static final String _space = VerbalisationManager.INSTANCE._space;
	private boolean disabled = false;
	private HashMap<String,int[]> cache = new HashMap<String,int[]>();
	private HashMap<String,Integer> isTypeCache = new HashMap<String,Integer>();
	// private static ErrorWriter errorWriter = new ErrorWriter("/var/folders/v0/3ytrm6vj49l0h238wgg66x7w0000gn/T/error.tmp");
	
	WordNetQuery(){
		// Do nothing.
		// System.setProperty("wordnet.database.dir", "/gdisk/ki/home/mschiller/software/remote/wordnet/WordNet-3.0/dict");
		// System.setProperty("wordnet.database.dir", "/Users/marvin/software/wordnet/WordNet-3.0/dict");
	}
	
	// SynsetType.ADJECTIVE
	// SynsetType.ADJECTIVE_SATELLITE
	// SynsetType.ADVERB
	// SynsetType.NOUN
	
	public void setDict(String dict){
		System.setProperty("wordnet.database.dir",dict);
		disabled = false;
		database = WordNetDatabase.getFileInstance();
		// Synset[] synsets = database.getSynsets("person");
	}
	
	public void disableDict(){
		disabled = true;
	}
	
	public boolean isDisabled(){
		return disabled;
	}
	
	public WordNetDatabase getDatabase(){
		return database;
	}
	
	public float isType(String str, SynsetType type){
		if (isTypeCache.containsKey(str)){
			return isTypeCache.get(str);
		}
		// errorWriter.write("is Type called with " + str + ", data base " + database.toString() + "\n");
		String[] arr = str.split(_space);   
		if (arr.length <1) return 0;
		Synset[] synsets = database.getSynsets(arr[arr.length-1]); // getting synsets for last word
		if (synsets.length==0){
			// System.out.println("no synsets ");
			return 0;
		}
	    int countType = 0;
		for (int i = 0; i < synsets.length; i++) {
			if (synsets[i].getType().equals(type))
				countType++;
		}
		isTypeCache.put(str,countType);
		
		return countType;
	}
	
	
	public int[] getTypes(String str){
		if (cache.containsKey(str)){
			// System.out.println("DEBUG -- retrieved cached element for " + str + " ! " + cache.get(str).toString());
			return cache.get(str);
		}
		// errorWriter.write("get Types called with " + str + ", data base " + database.toString() + "\n");
		// errorWriter.write("file instance " + database.getFileInstance() + "\n");
		String[] arr = str.split(_space);  
		
	    int[] types = new int[5]; 
	    String lastword = arr[arr.length-1];
	    
		Synset[] synsets = database.getSynsets(lastword); // getting synsets for last word
		// errorWriter.write("synsets " + synsets + "\n");
		for (int i = 0; i < synsets.length; i++) {
			int count = 0;
			try{
				count = synsets[i].getTagCount(lastword);
			}
			catch(Exception e){}
			types[synsets[i].getType().getCode()-1] = types[synsets[i].getType().getCode()-1] + count;
			}	
		// errorWriter.write("types " + Arrays.toString(types) + "\n");
		// errorWriter.close();
		// System.out.println("Caching element " + str + " " + types.toString());
		cache.put(str, types);
		return types;
	}
	
	public boolean isPlural(String str){
		Synset[] synsets = database.getSynsets(str,SynsetType.NOUN);
		// System.out.println(synsets);
		if (synsets.length<1)
			return false;
		boolean exactfound = false;
		for (int i = 0; i < synsets.length; i++) {
		    NounSynset nounSynset = (NounSynset)(synsets[i]); 
		    String[] wordforms = nounSynset.getWordForms();
		    
		    if (wordforms[0].equals(str))
		    	exactfound = true;
		    // System.out.println(Arrays.toString(wordforms));
		}
		if (exactfound)
			return false;
		else return true;
	}
	
}
