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
	
	// SynsetType.ADJECTIVE                ---- 3!
	// SynsetType.ADJECTIVE_SATELLITE
	// SynsetType.ADVERB
	// SynsetType.NOUN                     ---- 1!
	
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
		String key = str + type;
		// System.out.println("Type: " + type);
		// System.out.println("Key: " +  key);
		if (isTypeCache.containsKey(key)){
			return isTypeCache.get(key);
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
			if (synsets[i].getType().equals(type)){
				// System.out.println(synsets[i].getType());
				countType++;
			}
		}
		isTypeCache.put(key,countType);
		
		return countType;
	}
	
	
	public int[] getTypes(String str){
		if (cache.containsKey(str)){
			// System.out.println("DEBUG -- retrieved cached element for " + str + " ! " + Arrays.toString(cache.get(str)));
			return cache.get(str);
		}
		// errorWriter.write("get Types called with " + str + ", data base " + database.toString() + "\n");
		// errorWriter.write("file instance " + database.getFileInstance() + "\n");
		String[] arr = str.split(_space);  
		
	    int[] types = new int[5]; 
	    String lastword = arr[arr.length-1];
	    
	    // System.out.println("Lastword " +  lastword);
		Synset[] synsets = database.getSynsets(lastword); // getting synsets for last word
		// System.out.println("Synsets length " + synsets.length);
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
		// System.out.println("Caching element " + str + " " + Arrays.toString(types));
		cache.put(str, types);
		return types;
	}
	
	public boolean isPlural(String str){
		// known exceptions
		if (str.equals("workout")){
			return false;
		}
		Synset[] synsets = database.getSynsets(str,SynsetType.NOUN);
		// System.out.println("Length " + synsets.length);
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
	
	public boolean detectIsNounPlusPreposition(String str){
		String[] words = str.split(" ");
		if (words.length==3 && words[0].equals("is")){
			Synset[] synsets = database.getSynsets(words[1],SynsetType.NOUN);
			// System.out.println(synsets);
			if (synsets.length<1)
				return false;
			return true;
		}
		if (words.length==2){
			Synset[] synsets = database.getSynsets(words[0],SynsetType.NOUN);
			// System.out.println(synsets);
			if (synsets.length<1)
				return false;
		} 
		if (words[1].equals("of"))
			return true;
		return false;
	}
	
	public boolean detectNounPlusPreposition(String str){
		String[] words = str.split(" ");
		
		if (words.length==2){
			Synset[] synsets = database.getSynsets(words[0],SynsetType.NOUN);
			// System.out.println(synsets);
			if (synsets.length<1)
				return false;
		} 
		if (words[1].equals("of"))
			return true;
		return false;
	}
	
	public void wordnetTest(){
		database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets("person");
		// System.out.println("Found synsets for 'person': " + synsets.length);
		for (int i = 0; i < synsets.length; i++) {
			Synset synSet = synsets[i];
			String[] usageExample = synSet.getUsageExamples();
			System.out.println(Arrays.toString(usageExample));
		}
		int[] types = getTypes("person");
		System.out.println(Arrays.toString(types));
	}
	
	public boolean isKnown(String str){
		// known exceptions
	
		Synset[] synsets = database.getSynsets(str,SynsetType.NOUN);
		// System.out.println("Length " + synsets.length);
		if (synsets.length<1)
			return false;
		else return true;
	}
	
}
