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
            while (str != null) {
            	lines.add(new ReviewJ(str, "funny" ));
            	str = br.readLine();
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(notFunnyRevs)));
            str = br.readLine();
            while (str != null) {
            	lines.add(new ReviewJ(str, "notFunny" ));
            	str = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            
        }
    }
    
    public void close() {
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
