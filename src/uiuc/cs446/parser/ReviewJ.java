package uiuc.cs446.parser;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.*;

public final class ReviewJ implements Comparable<ReviewJ>{

	private String text;
	private int helpfulVotes;
	private ArrayList<String> categories;

	public int funnyVotes;
	
	/*
	 * sorts based on largest to smallest.
	 */
	public int compareTo(ReviewJ other)
    {
		if (this.funnyVotes == other.funnyVotes) return 0;
        return this.funnyVotes > other.funnyVotes? -1 : 1;
    }
	
	public ReviewJ(String body, int fvotes, int hvotes, ArrayList<String> cat){
		this.setText(new String(body));
		this.funnyVotes = fvotes;
		this.helpfulVotes = hvotes;
		this.categories = cat;
	}
	
	public static void main(String[] args){
		ReviewJ temp1 = new ReviewJ("temp1", 0, 0, null);
		ReviewJ temp2 = new ReviewJ("temp2", 1, 2, null);
		ReviewJ temp3 = new ReviewJ("temp2", 3, 2, null);
		
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

	public void setText(String text) {
		this.text = text;
	}
}
