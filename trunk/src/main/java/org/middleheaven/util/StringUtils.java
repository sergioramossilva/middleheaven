
package org.middleheaven.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringUtils {

	public static String join(String[] original, String separator){
		return join(original, separator,0, original.length);
	}

	public static String join(String[] original, String separator, int start){
		return join(original, separator, start, original.length);
	}

	public static String join(String[] original, String separator, int start, int end){
		StringBuilder builder = new StringBuilder();
		for (int i=start; i < end;i++){
			builder.append(original[i].toString()).append(separator);
		}
		if(builder.length()>0){
			builder.delete(builder.length()-separator.length(), builder.length());
		}
		return builder.toString();
	}
	
	public static String join(Iterable<?> original, String separator){
		StringBuilder builder = new StringBuilder();
		for (Iterator<?> it = original.iterator();it.hasNext();){
			builder.append(it.next().toString()).append(separator);
		}
		if(builder.length()>0){
			builder.delete(builder.length()-separator.length(), builder.length());
		}
		return builder.toString();
	}

	public static boolean isInArray(String candidate, String[] set){
		if (candidate==null) return false;

		for (String s: set){
			if (candidate.equals(s)){
				return true;
			}
		}
		return false;
	}

	public static Boolean booleanValueOf (String value){
		if (value==null || value.trim().length()==0) {
			return null;
		}
		return new Boolean(logicValueOf(value,false));
	}

	public static Boolean booleanValueOf (String value, boolean defaultValue){
		return new Boolean(logicValueOf(value,defaultValue));
	}

	public static String objectToString(Object obj){
		return objectToString(obj,"");
	}

	public static String objectToString(Object obj, String defaultValue){
		return obj == null ? defaultValue : obj.toString(); 
	}

	public static boolean logicValueOf (final String value, boolean defaultValue){
		if (value==null) return defaultValue;
		String v = value.trim();
		return v.indexOf("y")==0 || v.indexOf("t")==0 || v.indexOf("on")==0;
	}

	public static long longValueOf (final String value, long defaultValue){
		if (value==null || value.trim().length()==0) return defaultValue;
		try {
			return Long.parseLong(value.trim());
		} catch (NumberFormatException e){
			return defaultValue;
		}
	}

	public static String getStackStrace(Throwable t){
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}

	/**
	 * Splits a CharSequence in the format paramname1=value1;paramname2=value2;...  
	 * @param charSequence
	 * @return
	 */
	public static Map<String,String> splitParams(CharSequence charSequence){
		String[] paramPairs = split(charSequence, ";");

		if (paramPairs.length==0){
			return Collections.emptyMap();
		}
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		for (String s : paramPairs){
			String[] p = split(s,"=");
			paramsMap.put(p[0], p[1]);
		}
		return paramsMap;
	}

	public static String[] split(CharSequence charSequence, char delimiter){
		return split(charSequence , Character.toString(delimiter));
	}

	public static String[] split(CharSequence charSequence, String delimiter){
		if (delimiter==null){
			throw new IllegalArgumentException("parameter `delimiter` is required");
		}
		if (charSequence==null){
			return new String[0];
		}

		String source = charSequence.toString();

		if (source.indexOf(delimiter)<0){
			// return the original sequence
			return new String[]{source};
		}

		if (delimiter.equals(".")){
			delimiter = "\\.";
		}else  if (delimiter.equals(" ")){
			delimiter = "\\s";
		}

		return source.split(delimiter);

	}

	public static String capitalize(String text) {
		StringBuilder builder  = new StringBuilder(text);
		builder.replace(0, 1, builder.substring(0,1).toUpperCase());
		return builder.toString();
	}
}
