package uiuc.cs446.features;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import uiuc.cs446.parser.ParseWikiReference;
import edu.smu.tspell.wordnet.*;

public class StructureFunnyFeatures {
	private WordNetDatabase database;
	private CMUdict dict;
	private static List<String> stop_words;
	
	public static void main(String[] args){
		StructureFunnyFeatures sff = new StructureFunnyFeatures();
		
		//System.out.println(sff.wordFormTypes("moon"));
		//System.out.println(sff.wordFormTypes("sun"));
		//System.out.println(sff.alliteration("great green greenery scenery"));
		//for( String word : StructureFunnyFeatures.stop_words ){
		//	System.out.println(word);
		//}
		String text = " This is a sentence. This is another sentence 2.";
		for (String sentence : ParseWikiReference.getSentences(text)){
			System.out.println(sentence);
		}
		
	}
	
	public static List<String> stopWords(){
		List<String> stop_list = null;
		try {
			stop_list = FileUtils.readLines(new File("data/stop_words.txt"), "utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		return stop_list;
	}
	
	public StructureFunnyFeatures(){
		System.setProperty("wordnet.database.dir", TestJAWS.WORDNETDIR );
		database = WordNetDatabase.getFileInstance();
		dict = null;
		try{
			dict = new CMUdict();
		}catch (Exception e){
			e.printStackTrace();
		}
		stop_words = StructureFunnyFeatures.stopWords();
	}
	
	//This extracts the alliteration chains in the sentence for example
	// greenery secenery. has only one alliteration chain of length 2 while
	// Vini vidi vinci, also has one alliteration chian of length 3
	// see Learning to laugh (automatically): Computational models for humor recognition.
	public double alliteration(String sentence){
		Map<String, Integer> alliterations = new HashMap<String, Integer>();
		Map<String, Integer> rhymes = new HashMap<String, Integer>();
		List<String> wordPron = null;
		//int new_count = 0;
		
		String[] words = sentence.split("\\s+");
		for (String word : words){
			//System.out.println(word);
			wordPron = dict.getPronunciations(word);
			if (wordPron != null){
				//System.out.println(wordPron);
				String first_p = wordPron.get(0);
				String last_p = wordPron.get(wordPron.size()-1);
				if ( alliterations.containsKey(first_p) ) {
					// update alliterations counter for the word
					//new_count = ;
					alliterations.put(first_p, alliterations.get(first_p) + 1);
				}else {
					alliterations.put(first_p, 0); 
				}
				if (rhymes.containsKey(last_p)){
					// update rhyme counter for the word
					//new_count = rhymes.get(last_p)+1; 
					rhymes.put(last_p, rhymes.get(last_p)+1);
				} else{
					rhymes.put(last_p, 0);   
				}  
			}
		}
		
		int chains = 0;
		for (int item: alliterations.values()){
			chains += (item > 0) ? 1 : 0;
		}
		
		for (int item: rhymes.values()){
			chains += (item > 0) ? 1 : 0;
		}
		
		return chains;
	}
	
	
	// returns the avereage ambiguity score for the words in the sentences
	public double ambiguity(String sentence){
		int score = 0;
		String[] words = sentence.split("\\s");
		for (String word : words){
			score += wordFormTypes(word);
		}
		return score/(1.0* words.length);
	}
	
	public int wordFormTypes(String word){
		Synset[] synsets = database.getSynsets(word, SynsetType.NOUN);
		return synsets.length;
	}
	
	
	public int wordFormsFeatures(String wordForm){
		System.out.println("word Form:" + wordForm);
		//  Get the synsets containing the wrod form
		Synset[] synsets = database.getSynsets(wordForm, SynsetType.NOUN);
		//  Display the word forms and definitions for synsets retrieved
		if (synsets.length > 0)
		{
			System.out.println("The following synsets contain '" +
					wordForm + "' or a possible base form " +
					"of that text:");
			for (int i = 0; i < synsets.length; i++)
			{
				System.out.println("");
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++)
				{
					System.out.print((j > 0 ? ", " : "") +
							wordForms[j]);
				}
				System.out.println(": " + synsets[i].getDefinition());
			}
		}
		else
		{
			System.err.println("No synsets exist that contain " +
					"the word form '" + wordForm + "'");
		}
		return 0;
	}
}
