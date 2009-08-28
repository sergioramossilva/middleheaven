package org.middleheaven.util;

public class UrlStringUtils extends StringUtils {
	
	public static String removeContext(CharSequence url, CharSequence context){
		return removeStart(url, context);
	}
	
	public static String filename(CharSequence url){
		final String urlString = url.toString();
		String fileName = urlString.substring(urlString.lastIndexOf("/")+1);
		int pos = fileName.lastIndexOf(".");
		if (pos>=0){
			return fileName;
		} else {
			return "";
		}
		
	}
	
	public static String path(CharSequence url,CharSequence context){
		return removeEnd(removeContext(url,context), filename(url));
	}
}
