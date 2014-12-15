package uiuc.cs446.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import LBJ2.parse.Parser;

public class ReviewDataReader implements Parser {
	 private List<ReviewJ> lines;

    private int currentline;
    private BufferedReader br;
    
    /**
     * 
     */
    public ReviewDataReader(String funnyRevs, String notFunnyRevs) {
    	
        lines = new ArrayList<ReviewJ>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(funnyRevs)));
            String str = br.readLine();
            //str = br.readLine();
    		while (str != null) {
            	//System.out.println("funny:" + str);
            	String[] temp = str.split(",");
            	int l = temp.length; 
    			String time = temp[l-1];
    			String bStat = temp[l-3] + "," + temp[l-2];
    			String bid = temp[l-4];
    			String uStat = temp[l-6] + "," + temp[l-5];
    			String uid = temp[l-7];
    			int hvotes = Integer.parseInt(temp[l-8]);

    			StringBuffer s = new StringBuffer();
    			for (int i = 0; i <= l-9; i ++){
    				s.append(temp[i]);
    			}
    			String review = s.toString();
    			ReviewJ r = new ReviewJ(review, "funny", hvotes, uid, uStat, bid, bStat, time);
    			lines.add(r);
    			//System.out.println(r.getText() + "," + r.getLabel()+ "," + r.getHelpfulVotes() + "," + r.getUserId() + ","
    			//		+ r.getUserStat() + "," + r.getBid() + "," + r.getBusinessStat() + "," + r.getTime() );
            	/*lines.add(new ReviewJ(review, "funny", hvotes, uid, uStat, bid, bStat, time));*/
            	str = br.readLine();
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(notFunnyRevs)));
            str = br.readLine();
            //str = br.readLine();
            while (str != null) {
            	//System.out.println("not funny:" + str);
            	String[] temp = str.split(",");
            	int l = temp.length; 
    			String time = temp[l-1];
    			String bStat = temp[l-3] + "," + temp[l-2];
    			String bid = temp[l-4];
    			String uStat = temp[l-6] + "," + temp[l-5];
    			String uid = temp[l-7];
    			int hvotes = Integer.parseInt(temp[l-8]);
    			StringBuffer s = new StringBuffer();
    			for (int i = 0; i <= l-9; i ++){
    				s.append(temp[i]);
    			}
    			String review = s.toString();
            	lines.add(new ReviewJ(review, "notFunny", hvotes, uid, uStat, bid, bStat, time ));
            	str = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            
        }
    }
    
    public void close() {
    }
    
    public List<ReviewJ> getReviews(){
    	return this.lines;
    }
    public Object next() {
        
        if (currentline == lines.size()) {
            return null;
        } else {
            currentline++;
            return lines.get(currentline - 1);
        }
    }
    
    public void reset() {
        currentline = 0;
    }
}