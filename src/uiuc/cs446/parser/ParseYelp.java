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
		//Map<String, String> ids = new HashMap<String, String>();
		try{ 
			InputStreamReader reader = new InputStreamReader( new FileInputStream ( "data/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_review.json"), "UTF8");
			BufferedReader bf = new BufferedReader( reader);
			//TODO: need to hash the business values before so we can check the Ids to add to categories.
			//InputStreamReader business = new InputStreamReader( new FileInputStream("data/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_business.json"), "UTF8");
			//BufferedReader bb = new BufferedReader(business);
			//BufferedWriter br = new BufferedWriter(new FileWriter("data/funny_reviews_5.txt"));
			//BufferedWriter br2 = new BufferedWriter(new FileWriter("data/not_funny_reviews_5.txt"));
			
			String line;
			int ittrs = 11;
			
			/*while( ittrs > 0 && (line = bb.readLine()) != null){
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
			}*/
			int cnt = 0;
			while (ittrs > 0 && (line = bf.readLine()) != null && cnt < 100000){
				JsonElement jelement = new JsonParser().parse(line);
			    JsonObject  jobject = jelement.getAsJsonObject();
			    JsonObject votes = jobject.getAsJsonObject("votes");
			    String funnyVotes = votes.get("funny").toString();
			    String helpfulVotes = votes.get("useful").toString();
			    String reviewText = jobject.get("text").toString();
			    String business_id = jobject.get("business_id").toString();
			    int score = Integer.parseInt(jobject.get("stars").toString());
			    String time = jobject.get("date").toString();
			    String user_id = jobject.get("user_id").toString();
			    //if(ids.containsKey(business_id)){
			    	// Restaurant Review
			    	ReviewJ temp = new ReviewJ(reviewText, Integer.valueOf(funnyVotes), Integer.valueOf(helpfulVotes),null, "unknown", user_id,
			    			business_id, score, time);
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
				    cnt ++;
				    if (cnt%10000 == 0)
				    	System.out.println(cnt);
			}
			
			int total_reviews = 0;
			for (String key : freq.keySet()){
				System.out.println( key + ", there are:"+ freq.get(key).value );
				total_reviews += freq.get(key).value;
			}
			
			System.out.println("total number of reviews: " + total_reviews);
			Collections.sort(data);

			//TODO: write everything into CSV format. Review, funny/not, helpful/not, user_id
			writeToCSV(data);
			/*for(ReviewJ r : data){
				if ( r.funnyVotes > ParseYelp.MINFUNNYVOTES){
					br.write( r.getText() + "\n");
				}
				else if ( r.funnyVotes == 0){
					br2.write( r.getText() + "\n");
				}
			}
	    	*/
			//bb.close();
			bf.close();
			//br.close();
			//br2.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeToCSV(ArrayList<ReviewJ> data){
		try
		{
		    FileWriter writer1 = new FileWriter("data/funny_exp.csv");
		    FileWriter writer2 = new FileWriter("data/not_funny_exp.csv");
		    putHeader(writer1);
		    putHeader(writer2);
		    
			ReviewStat st = new ReviewStat(data);
			HashMap<String, String> userScoreMap = st.getAvgScoreAndStd(1);
			HashMap<String, String> businessScoreMap = st.getAvgScoreAndStd(0);
			
		    for (ReviewJ r : data){
		    	String userStat = userScoreMap.get(r.getUserId());
		    	String businessStat = businessScoreMap.get(r.getBid());
		    	if (r.funnyVotes > ParseYelp.MINFUNNYVOTES){
		    		appendContents(writer1, r, userStat, businessStat);
		    	}
		    	else if (r.funnyVotes == 0){
		    		appendContents(writer2, r, userStat, businessStat);
		    	}
		    }
	 
		    writer1.flush();
		    writer1.close();
		    
		    writer2.flush();
		    writer2.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	private static void putHeader(FileWriter w){
		try {
		    w.append("Review");
		    w.append(',');
		    w.append("HelpfulVotes");
		    w.append(',');
		    w.append("userId");
		    w.append(',');
		    w.append("userScoreMean");
		    w.append(',');
		    w.append("userScoreStd");
		    w.append(',');
		    w.append("businessId");
		    w.append(',');
		    w.append("businessScoreMean");
		    w.append(',');
		    w.append("businessScoreStd");
		    w.append(',');
		    w.append("time");
		    w.append('\n');
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	private static void appendContents(FileWriter w, ReviewJ r, String userStat, String businessStat){
		try {
    		w.append(r.getText());
    		w.append(',');
    		
    		w.append(String.valueOf(r.getHelpfulVotes()));
    		w.append(',');
    		
    		w.append(r.getUserId());
    		w.append(',');
    		
    		if (userStat == null)
    			w.append("NA, NA");
    		else
    			w.append(userStat);
    		w.append(',');
    		
    		w.append(r.getBid());
    		w.append(',');
    		
    		if (businessStat == null){
    			w.append("NA, NA");
    		}
    		else
    			w.append(businessStat);
    		w.append(',');
    		
    		w.append(r.getTime());
    		w.append('\n');
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}

class MutableInt {
	int value = 1; // note that we start at 1 since we're counting
	public void increment () { ++value;      }
	public int  get ()       { return value; }
}