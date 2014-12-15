package uiuc.cs446.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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
	
	public static String[] getWikiEntities( String idx ) throws IOException {
		String wikiFileName = ParseWikiReference.pathToWikiRef + ParseWikiReference.filePreffix + idx + ParseWikiReference.fileSuffix + ParseWikiReference.wikiName;
    	// wiki entities
    	File input = new File( wikiFileName );
    	Document docWiki = Jsoup.parse(input, "UTF-8");
    	Elements a_links = docWiki.getElementsByTag("a");
    	Elements b_text = docWiki.getElementsByTag("b");
    	
    	System.out.println("num links: "+a_links.size());
    	System.out.println("b entities: " + b_text.size());
    	
    	for (Element e: b_text){
    		System.out.println("b: " + e.text());
    	}
    	return new String[1];
	}
	
	public static String[] getNER(String idx) throws IOException{
		String filename = ParseWikiReference.pathToWikiRef + ParseWikiReference.filePreffix + idx + ParseWikiReference.fileSuffix + ParseWikiReference.nerName;
    	
    	File input = new File( filename );
    	Document doc = Jsoup.parse(input, "UTF-8");
    	
    	Elements text = doc.getElementsByTag("text");
    	System.out.println(text.text());
    	Elements ner = doc.getElementsByTag("Form");
    	
    	String[] ret = new String[ner.size()];
    	int i = 0; 
    	for ( Element e : ner){
    		//for ( Element entity: e.getElementsByTag("Form")){
    		System.out.println("ner: " + e.text());
    		ret[i++]  = e.text();
    		//}
    	}
    	
    	return ret;
	}
	
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
			//BufferedReader bb = null;
			
			String str = br.readLine();
			int ittr = 10;
			while (str != null && ittr > 0) {
				String[] temp = str.split(",");
            	System.out.println("idx: " + temp[0] );
            	
            	//bb = new BufferedReader(new InputStreamReader(
            	//		new FileInputStream(pathToWikiRef + filePreffix + temp[0] + fileSuffix + nerName)));
    			
            	
            	
				str = br.readLine();
				ittr--;
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
