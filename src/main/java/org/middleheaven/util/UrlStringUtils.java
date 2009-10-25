package org.middleheaven.util;

public class UrlStringUtils extends StringUtils {
	
	/**
	 * Remove the context string and all content before it.
	 * If the context is empty, remove all content before the first {@code /} after
	 * the protocol separator ({@code ://})
	 * @param url
	 * @param context
	 * @return the url with the context removed
	 */
	public static String removeContext(CharSequence url, CharSequence context){
		if (context.length() == 0){
			String u = ensureEndsWith(url.toString(),"/");
			return u.substring(u.indexOf("/", u.indexOf("://")+4)+1);
			
		}
		return removeStart(url, context);
	}
	
	/**
	 * 
	 * @param url
	 * @return the name of the file in the url 
	 */
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
	/**
	 * 
	 * @param url
	 * @param excludeExtention
	 * @return the name of the file in the url  minus the extention id {@code excludeExtention} is {@code true}
	 */
	public static String filename(CharSequence url, boolean excludeExtention){
		String filename = filename(url);
		final int pos = filename.indexOf('.');
		if (excludeExtention && pos> 0){
			filename = filename.substring(0,pos);
		}
		return filename;
	}
	
	public static String path(CharSequence url,CharSequence context){
		return removeEnd(removeContext(url,context), filename(url));
	}
}
