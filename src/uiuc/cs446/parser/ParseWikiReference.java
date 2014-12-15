package uiuc.cs446.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseWikiReference {
	public static final String dataFile = "data/cv/hold_out_set.txt";
	public static final String pathToWikiRef = "data/funnyEntitiesOutput/";
	public static final String filePreffix = "rev_data_";
	public static final String fileSuffix = ".txt";
	public static final String nerName = ".NER.tagged";
	public static final String wikiName = ".wikification.tagged.flat.html";
	public static final String wikifull = ".wikification.tagged.full.xml";
	
	public static String[][] getWikiEntities( String idx ) throws IOException {
		
		String wikiFileName = ParseWikiReference.pathToWikiRef + ParseWikiReference.filePreffix + idx + ParseWikiReference.fileSuffix + ParseWikiReference.wikiName;
    	// wiki entities
		
    	File input = new File( wikiFileName );
    	Document docWiki = Jsoup.parse(input, "UTF-8");
    	Elements a_links = docWiki.getElementsByTag("a");
    	Elements b_text = docWiki.getElementsByTag("b");
    	
    	String [] a_ret = new String[ a_links.size()];
    	String [] b_ret = new String[ b_text.size()];
    	
    	//System.out.println("num links: "+a_links.size());
    	int i = 0; 
    	for (Element e: a_links){
    		String cats = e.attr("cat");
    		a_ret[i++] = e.text() + "\n" + cats;
    		//System.out.println("a: " + e.text() + "\n" +  cats);
    	}
    	
    	//System.out.println("b entities: " + b_text.size());
    	i = 0;
    	for (Element e: b_text){
    		b_ret[i++] = e.text();
    		//System.out.println("b: " + e.text());
    	}
    	return new String[][]{a_ret, b_ret};
	}
	
	public static String[] getNER(String idx) throws IOException{
		String filename = ParseWikiReference.pathToWikiRef + ParseWikiReference.filePreffix + idx + ParseWikiReference.fileSuffix + ParseWikiReference.nerName;
    	
    	File input = new File( filename );
    	Document doc = Jsoup.parse(input, "UTF-8");
    	
    	Elements text = doc.getElementsByTag("text");
    	//System.out.println(text.text());
    	Elements ner = doc.getElementsByTag("Form");
    	
    	//Set<String> rets = new HashSet <String> ();
    	String [] ret = new String[ner.size()];
    	int i = 0; 
    	for ( Element e : ner){
    		//for ( Element entity: e.getElementsByTag("Form")){
    		//System.out.println("ner: " + e.text());
    		ret[i++]  = e.text();
    		//}
    	}
    	
    	return ret;
	}
	
	public static TreeMap<String, Integer> sortByValue(Map unsortedMap) {
		ValueComparator vc =  new ValueComparator(unsortedMap);
		TreeMap<String,Integer> sortedMap = new TreeMap<String,Integer>(vc);
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
	
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
			//BufferedReader bb = null;
			
			String str = br.readLine();
			//int ittr = 10;
			HashMap<String, Integer> categories_of_entities = new HashMap<String, Integer>();
			while (str != null /*&& ittr > 0*/) {
				String[] temp = str.split(",");
            	System.out.println("idx: " + temp[0] );
            		
            	String[][] wiki_ents = ParseWikiReference.getWikiEntities(temp[0]);
            	String[] a_list = wiki_ents[0];
            	for (String s : a_list){
            		//System.out.println("s: " + s );
            		String[] s_cat = s.split("\n");
            		if (s_cat.length < 2 ) continue;
            		String[] a_categories = s_cat[1].split("\\s+");
            		for (String category: a_categories){
            			//System.out.println("category: " + category);
            			if ( categories_of_entities.containsKey(category)){
            				int current_count = categories_of_entities.get(category);
            				categories_of_entities.put(category, current_count+1);
            			}else{
            				categories_of_entities.put(category, 1);
            			}	
            		}
            	}
				str = br.readLine();
				//ittr--;
				//Collections.sort();
				//for ( String s : categories_of_entities.descendingKeySet()){
				//	System.out.println("string: " + s + " value: " + categories_of_entities.get(s));
					//System.out.println(categories_of_entities.values());
				//}
			}
			br.close();
			//System.out.println("after");
			TreeMap <String, Integer> sortedMap = ParseWikiReference.sortByValue(categories_of_entities);
			//System.out.println(sortedMap);
			
			int topK = 100; 
			for (String key: sortedMap.keySet()){
				if (topK-- < 0) break;
				System.out.println("key: " + key + ", value: " + categories_of_entities.get(key));
			}
			
			/*System.out.println("before");
			for (String key: categories_of_entities.keySet()){
				System.out.println("key: " + key + ", value: " + categories_of_entities.get(key));
			}*/
			
			//System.out.println(sortedMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class ValueComparator implements Comparator<String> {
	 
    Map<String, Integer> map;
 
    public ValueComparator(Map<String, Integer> base) {
        this.map = base;
    }
 
    public int compare(String a, String b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
    }
}
