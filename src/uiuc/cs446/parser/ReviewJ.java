package uiuc.cs446.parser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.*;

public final class ReviewJ implements Comparable<ReviewJ>{

	private String text;
	private int helpfulVotes;
	private ArrayList<String> categories;
	private String label; 
	private String userId;
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
		this.label = l;
		this.funnyVotes = -1;
		this.helpfulVotes = -1;
		this.categories = null;
		this.userId = null;
	}
	
	public ReviewJ(String body, int fvotes, int hvotes, ArrayList<String> cat, String l, String id){
		this.setText(new String(body));
		this.funnyVotes = fvotes;
		this.helpfulVotes = hvotes;
		this.categories = cat;
		this.label = l;
		this.userId = id;
	}
	
	public static void main(String[] args){
		ReviewJ temp1 = new ReviewJ("temp1", 0, 0, null, "positive", "abc");
		ReviewJ temp2 = new ReviewJ("temp2", 1, 2, null, "positive", "abcd");
		ReviewJ temp3 = new ReviewJ("temp2", 3, 2, null, "positive", "abd");
		
		ArrayList<ReviewJ> data = new ArrayList<ReviewJ>();
		data.add(temp1);
		data.add(temp2);

		data.add(temp3);
		for(ReviewJ rj : data){
			System.out.println(rj.funnyVotes);
		}
		Collections.sort(data);
		
		System.out.println("sorted:");
		for(ReviewJ rj : data){
			System.out.println(rj.funnyVotes);
		}
		
	}

	public String getText() {
		return text;
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
