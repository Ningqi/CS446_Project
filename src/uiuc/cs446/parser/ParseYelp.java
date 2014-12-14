package uiuc.cs446.parser;
//import org.json.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParseYelp {
	public static final int MINFUNNYVOTES=5;
	public static void main(String[] args){
		ArrayList<ReviewJ> data = new ArrayList<ReviewJ>();
		Map<String, MutableInt> freq = new HashMap<String, MutableInt>();
		Map<String, String> ids = new HashMap<String, String>();
		try{ 
			InputStreamReader reader = new InputStreamReader( new FileInputStream ( "data/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_review.json"), "UTF8");
			BufferedReader bf = new BufferedReader( reader);
			//TODO: need to hash the business values before so we can check the Ids to add to categories.
			InputStreamReader business = new InputStreamReader( new FileInputStream("data/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_business.json"), "UTF8");
			BufferedReader bb = new BufferedReader(business);
			BufferedWriter br = new BufferedWriter(new FileWriter("data/funny_reviews_5.txt"));
			BufferedWriter br2 = new BufferedWriter(new FileWriter("data/not_funny_reviews_5.txt"));
			
			String line;
			int ittrs = 11;
			
			while( ittrs > 0 && (line = bb.readLine()) != null){
				JsonElement jelement = new JsonParser().parse(line);
				JsonObject jobject = jelement.getAsJsonObject();
				JsonArray categories = jobject.getAsJsonArray("categories");
			    String business_id = jobject.get("business_id").toString();
				String name = jobject.get("name").toString(); 
				if ( categories.contains( new JsonParser().parse("Restaurants")) ){
			    	//System.out.println("This buisiness: " + name + " is a restaurant");
			    	ids.put(business_id, name);
			    }
				//ittrs--;
			}
			
			while (ittrs > 0 && (line = bf.readLine()) != null){
				JsonElement jelement = new JsonParser().parse(line);
			    JsonObject  jobject = jelement.getAsJsonObject();
			    JsonObject votes = jobject.getAsJsonObject("votes");
			    String funnyVotes = votes.get("funny").toString();
			    String helpfulVotes = votes.get("useful").toString();
			    String reviewText = jobject.get("text").toString();
			    String business_id = jobject.get("business_id").toString();
			    
			    //if(ids.containsKey(business_id)){
			    	// Restaurant Review
			    	ReviewJ temp = new ReviewJ(reviewText, Integer.valueOf(funnyVotes), Integer.valueOf(helpfulVotes),null, "unknown");
				    data.add(temp);
				    // counting distribuion of funny text
				    MutableInt count = freq.get(funnyVotes);
				    if (count == null) {
				        freq.put(funnyVotes, new MutableInt());
				    }
				    else {
				        count.increment();
				    }   
			    //}	    
			    //ittrs--;
			}
			
			int total_reviews = 0;
			for (String key : freq.keySet()){
				System.out.println( key + ", there are:"+ freq.get(key).value );
				total_reviews += freq.get(key).value;
			}
			
			System.out.println("total number of reviews: " + total_reviews);
			Collections.sort(data);
			for(ReviewJ r : data){
				if ( r.funnyVotes > ParseYelp.MINFUNNYVOTES){
					br.write( r.getText() + "\n");
				}
				else if ( r.funnyVotes == 0){
					br2.write( r.getText() + "\n");
				}
			}
	    
			bb.close();
			bf.close();
			br.close();
			br2.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}

class MutableInt {
	int value = 1; // note that we start at 1 since we're counting
	public void increment () { ++value;      }
	public int  get ()       { return value; }
}