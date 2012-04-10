/**
 * 
 */
package org.middleheaven.process.web.server.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class PathMatcher {



	public static PathMatcher newInstance(String pattern){

		if(pattern.contains("?")){
			throw new IllegalArgumentException("'?' is not a valid pattern character ");
		}
		
		if (pattern.contains("{")){
			StringBuilder builder = new StringBuilder(pattern);
			int posStart = builder.indexOf("{");
		    int posStartAll = builder.indexOf("*");
		    
			if ( posStartAll >= 0 && posStartAll < posStart){
				builder.replace(posStartAll, posStartAll+1, "{??}");
			}
			
			posStart = builder.indexOf("{");
			int posEnd;

			List<String> names = new ArrayList<String>();
			while (posStart > 0){

				posEnd = builder.indexOf("}", posStart+1);

				String name = builder.substring(posStart + 1, posEnd);

				builder.replace(posStart, posEnd + 1, "(.*)");

				
				names.add(name);
				
				posStart = builder.indexOf("{", posStart +1);
			}

			return new PathMatcher(names.toArray(new String[names.size()]), Pattern.compile(builder.toString()));
		} else {
			return new PathMatcher(new String[0],Pattern.compile(pattern.replace("*", "(.*)") + "$"));
		}

	}


	private Pattern pattern;
	private String[] names;


	/**
	 * Constructor.
	 * @param names 
	 * @param compile
	 */
	private PathMatcher(String[] names, Pattern pattern) {
		this.pattern = pattern;
		this.names = names;
	}


	public Map<String, String> parse(String url){
		if (names.length > 0){
			return getPathVariables(this.pattern, names, url);
		} else {
			return Collections.emptyMap();
		}
	}

	private static final Map<String, String> getPathVariables(Pattern p,
			String[] names, String path) {

		Map<String, String> tokenMap = new HashMap<String, String>();

		Matcher matcher = p.matcher(path);

		if (matcher.find()) {
			// Get all groups for this match
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				if (i != 0 && !names[i - 1].equals("??")) {
					tokenMap.put(names[i - 1], groupStr);
				}
			}
		}
		return tokenMap;
	}


	/**
	 * @param string
	 * @return
	 */
	public double match(String url) {
		if(this.pattern.matcher(url).find()){
			if(names.length==0){
				return 1d;
			} else {
				return 0.5d;
			}
		}
		return 0d;
	}
	
	public String toString(){
		return this.pattern.toString() + "=>" + Arrays.toString(this.names);
	}
}
