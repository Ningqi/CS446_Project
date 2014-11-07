package uiuc.cs446.parser;
//import org.json.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class ParseYelp {
	public static void main(String[] args){
		try{ 
		FileReader reader = new FileReader("data/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_review.json");
		BufferedReader bf = new BufferedReader( reader);
	    JsonElement jelement = new JsonParser().parse( bf.readLine());
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonObject votes = jobject.getAsJsonObject("votes");
	    String funnyVotes = votes.get("funny").toString();
	    System.out.println("funny votes: " + funnyVotes);
	    
	    String reviewText = jobject.get("text").toString();
	    System.out.println("review: " + reviewText);
	    //JsonArray jarray = jobject.getAsJsonArray("translations");
	    //jobject = jarray.get(0).getAsJsonObject();
	    //String result = jobject.get("translatedText").toString();
	    //return result;
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
