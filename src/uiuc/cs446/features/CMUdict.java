package uiuc.cs446.features;

/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische UniversitÃ¤t Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 * An interface to the <a
 * href="http://www.speech.cs.cmu.edu/cgi-bin/cmudict">CMU Pronouncing
 * Dictionary</a>.
 *
 * @author Tristan Miller <miller@ukp.informatik.tu-darmstadt.de>
 *
 */
public class CMUdict
{
	private MultiMap<String, String> dict;
	private final static String DICT_PATH = "/home/squirtle/Downloads/cmudict/cmudict.0.7a";
	
	public CMUdict() throws IOException{
		InputStreamReader ir = new InputStreamReader(new FileInputStream(DICT_PATH), "UTF-8");
		String line;
		int lineNumber = 0;
		final Pattern cmuPattern = Pattern.compile("(.+?)(\\(\\d+\\))?  (.*)");
		final BufferedReader bufRead = new BufferedReader(ir);
		dict = new MultiValueMap<String, String>();

		while ((line = bufRead.readLine()) != null) {

			lineNumber++;

			if (line.length() < 3) {
				throw new IOException("syntax error on line " + lineNumber);
			}

			// Skip comment lines
			if (line.substring(0, 3).equals(";;;")) {
				continue;
			}

			Matcher m = cmuPattern.matcher(line);

			if (!m.find()) {
				throw new IOException("syntax error on line " + lineNumber);
			}
			//System.out.println("key: " + m.group(1) + " value: " + m.group(3));
			dict.put(m.group(1), m.group(3));
		}
		ir.close();
		bufRead.close();
	}

	public CMUdict(Reader reader)
		throws IOException
	{
		String line;
		int lineNumber = 0;
		final Pattern cmuPattern = Pattern.compile("(.+?)(\\(\\d+\\))?  (.*)");
		final BufferedReader bufRead = new BufferedReader(reader);
		dict = new MultiValueMap<String, String>();

		while ((line = bufRead.readLine()) != null) {

			lineNumber++;

			if (line.length() < 3) {
				throw new IOException("syntax error on line " + lineNumber);
			}

			// Skip comment lines
			if (line.substring(0, 3).equals(";;;")) {
				continue;
			}

			Matcher m = cmuPattern.matcher(line);

			if (!m.find()) {
				throw new IOException("syntax error on line " + lineNumber);
			}
			
			dict.put(m.group(1), m.group(3));
		}

		bufRead.close();
	}

	public String getDictionaryId()
	{
		return "CMUdict 0.7a";
	}

	public List<String> getPronunciations(String word){
		//return  (List<String>) dict.get(word.toUpperCase());
		@SuppressWarnings("unchecked")
		String line = ((List<String>) dict.get(word.toUpperCase())).get(0);
		List<String> wordArrayList = new ArrayList<String>();
		for(String w : line.split("\\s+")) {
		    wordArrayList.add(w);
		}
		return wordArrayList;
	}

	public String getAlphabetId()
	{
		return "x-arpabet";
	}

	public String getLanguage()
	{
		return "en-US";
	}

	public static void main(String[] args){
		try {
			CMUdict dict = new CMUdict();
			
			System.out.println("pronunciation for engineering: " + dict.getPronunciations("Engineering"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}