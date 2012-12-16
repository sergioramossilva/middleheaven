/**
 * 
 */
package org.middleheaven.util;

import java.util.regex.Pattern;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.Mapper;

/**
 * 
 */
public final class Splitter {

	
	public static Splitter on(String delimiter) {
		return new Splitter(delimiter);
	}
	
	public static Splitter on(Pattern pattern) {
		return on(pattern.pattern());
	}
	
	public static Splitter on(char delimiter) {
		return new Splitter(new String(new char[]{delimiter}));
	}

	private String delimiter;
	private Mapper<String, String> transform;
	
	
	private Splitter (String delimiter){
		this.delimiter = delimiter;
	}
	
	public Splitter trim(){
		Splitter s = new Splitter(this.delimiter);
		s.transform = new Mapper<String,String>(){

			@Override
			public String apply(String object) {
			 return object.trim();
			}
			
		};
		return s;
	}
	public Enumerable<String> split(CharSequence charSequence){
		if(charSequence == null){
			throw new IllegalArgumentException("Argument is null");
		}
		String source = charSequence.toString();

		Enumerable<String> res;
		if (source.indexOf(delimiter)<0){
			// return the original sequence
			res = CollectionUtils.asEnumerable(source);
		}

		if (delimiter.equals(".")){
			res = CollectionUtils.asEnumerable(source.split("\\."));
		}else  if (delimiter.equals(" ")){
			res = CollectionUtils.asEnumerable(source.split("\\s"));
		} else {
			res = CollectionUtils.asEnumerable(source.split(delimiter));
		}
		
		if (transform != null){
			res = res.map(transform);
		}
		
		return res;
	}
}
