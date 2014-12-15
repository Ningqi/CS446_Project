package uiuc.cs446.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewStat {
	private ArrayList<ReviewJ> reviews;
	
	public ReviewStat(ArrayList<ReviewJ> reviews){
		this.reviews = reviews;
	}
	
	/**
	 * Calculate average score for all the business/User
	 * Switch ==0, get business score; else, get user score
	 * @return
	 */
	public HashMap<String, String> getAvgScoreAndStd(int switcher){
		HashMap<String, String> map = new HashMap<String, String>();
		for (ReviewJ r : reviews){
			int score = 0;
			String bid = null;
			if (switcher == 0){
				score = r.getScore();
				bid = r.getBid();
			}

			if (switcher == 1){
				score = r.getHelpfulVotes();
				bid = r.getUserId();
			}
			if (map.containsKey(bid)){
				String[] pair = map.get(bid).split(",");
				//System.out.println(pair[0]+","+ pair[1] + "," +pair[2]);
				int sum = Integer.parseInt(pair[0]) + score;
				int sqSum = Integer.parseInt(pair[1]) + score*score;
				int cnt = Integer.parseInt(pair[2]) + 1;
				//System.out.println(bid + ":" + sum + "," + sqSum + "," + cnt);
				map.put(bid, sum + "," + sqSum + ","+cnt);
			}
			else{
				map.put(bid, score + "," + score*score+",1");
			}
				
		}
		for (String key: map.keySet()){
			String[] pair = map.get(key).split(",");
			int mean = Integer.parseInt(pair[0])/Integer.parseInt(pair[2]);
			//System.out.println(pair[0]+","+ pair[1] + "," +pair[2]+ "," + mean);
			double std = Math.sqrt(Integer.parseInt(pair[1])/Integer.parseInt(pair[2]) - mean*mean);
			map.put(key, String.valueOf(mean)+"," + String.valueOf(std));
		}
		return map;
	}
	

}
