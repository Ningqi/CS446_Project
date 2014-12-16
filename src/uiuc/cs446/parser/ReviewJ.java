package uiuc.cs446.parser;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import uiuc.cs446.features.StructureFunnyFeatures;

import com.google.gson.*;

public final class ReviewJ implements Comparable<ReviewJ>{
	private static final StructureFunnyFeatures sff = new StructureFunnyFeatures();
	private String text;
	private int helpfulVotes;
	private ArrayList<String> sentences; // split on sentences, stopped words removed.
	private String label; 
	private String userId;
	private String businessId;
	private int score;
	private String time;
	private String userStat;
	private String businessStat;
	public int funnyVotes;
	
	/*
	 * sorts based on largest to smallest.
	 */
	public int compareTo(ReviewJ other)
    {
		if (this.funnyVotes == other.funnyVotes) return 0;
        return this.funnyVotes > other.funnyVotes? -1 : 1;
    }
	
	public ReviewJ(String body, String l){
		this.setText(new String(body));
		this.sentences = ParseWikiReference.getSentences(body);
		this.label = l;
		this.funnyVotes = -1;
		this.helpfulVotes = -1;
		this.sentences = null;
		this.userId = null;
	}
	
	public ReviewJ(String body, int fvotes, int hvotes, ArrayList<String> cat, String l, String uid, String bid, int score, String time){
		this.setText(new String(body));
		this.funnyVotes = fvotes;
		this.helpfulVotes = hvotes;
		this.sentences = ParseWikiReference.getSentences(body);
		this.label = l;
		this.userId = uid;
		this.businessId = bid;
		this.score = score;
		this.time = time;
	}
	
	public ReviewJ(String body, String l, int helpfulVotes, String userId, String uStat, String bid, String bStat, String time){
		this.setText(new String(body));
		this.sentences = ParseWikiReference.getSentences(body);
		this.label = l;
		this.helpfulVotes = helpfulVotes;
		this.userId = userId;
		this.userStat = uStat;
		this.businessId = bid;
		this.businessStat = bStat;
		this.time = time;
	}
	
	public static void main(String[] args){
		ReviewJ temp1 = new ReviewJ("temp1, I dont like this.", 0, 0, null, "positive", "abc", "zxc", 4, "2014-07-16");
		ReviewJ temp2 = new ReviewJ("temp2m haha, haha, ha", 0, 2, null, "positive", "abc", "zxc", 2, "2014-05-17");
		ReviewJ temp3 = new ReviewJ("temp2this is crazy!!!!, I can believe I bought this!", 6, 2, null, "positive", "abd", "jks", 2, "2014-06-14");
		
		ArrayList<ReviewJ> data = new ArrayList<ReviewJ>();
		data.add(temp1);
		data.add(temp2);

		data.add(temp3);
		/*ReviewStat st = new ReviewStat(data);
		HashMap<String, String> map = st.getAvgScoreAndStd(1);
		for (String k : map.keySet()){
			System.out.println(k + ":" + map.get(k));
		}*/
		ParseYelp.writeToCSV(data);
		ReviewDataReader rd = new ReviewDataReader("data/funny_exp.csv", "data/not_funny_exp.csv");
		List<ReviewJ> reviews = rd.getReviews();
		System.out.println("here:" + reviews.size());
		for (ReviewJ r : reviews){
			System.out.println(r.getText() + "," + r.getLabel()+ "," + r.getHelpfulVotes() + "," + r.getUserId() + ","
					+ r.getUserStat() + "," + r.getBid() + "," + r.getBusinessStat() + "," + r.getTime() );
		}
		/*for(ReviewJ rj : data){
			System.out.println(rj.funnyVotes);
		}
		Collections.sort(data);
		
		System.out.println("sorted:");
		for(ReviewJ rj : data){
			System.out.println(rj.funnyVotes);
		}*/
		
	}
	
	
	
	public double lexicalAmbiguity(){
		double lexical_score = 0;
		for (String sentence : this.sentences){
			lexical_score += sff.ambiguity(sentence);
		}
		return lexical_score/(1.0*this.sentences.size());
	}
	
	public int alliterationChains(){
		int chains = 0;
		for (String sentence : this.sentences){
			chains += sff.alliteration(sentence);
		}
		return chains;
	}

	public String getText() {
		return text;
	}
	
	public String getBid(){
		return businessId;
	}
	
	public int getScore(){
		return this.score;
	}
	public String getTime(){
		return this.time;
	}
	public String getUserId(){
		return this.userId;
	}
	
	public int getFvotes(){
		return this.funnyVotes;
	}
	
	public int getHelpfulVotes(){
		return this.helpfulVotes;
	}
	
	public String getUserStat(){
		return this.userStat;
	}
	public String getBusinessStat(){
		return this.businessStat;
	}
	public List<String> getWords(){
		List<String> words = new ArrayList<String>();
		for (String word : text.split("\\s+"))
			words.add(word.trim());
		return words;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getLabel(){
		return this.label;
	}
}
