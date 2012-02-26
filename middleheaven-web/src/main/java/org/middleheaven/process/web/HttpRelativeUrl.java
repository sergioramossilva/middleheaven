package org.middleheaven.process.web;

import org.middleheaven.util.UrlStringUtils;

/**
 * Encapsulates an url an context information.
 */
public class HttpRelativeUrl {

	private CharSequence url;
	private CharSequence context;

	/**
	 * 
	 * Constructor.
	 * @param url the url to handle.
	 * @param context the current server context.
	 */
	public HttpRelativeUrl (CharSequence url, CharSequence context){
		this.context = context;
		if (url == null){
			throw new IllegalArgumentException("Url is required");
		}
		this.url = url;

	}

	/**
	 * The file name. If any.
	 * @return the filename the url refers to or an empty string if no file is present in the name.
	 */
	public String getFilename(){
		return UrlStringUtils.filename(url, false);
	}

	/**
	 * The file name without the extension.
	 * @param excludeExtention if <code>true</code>, the extention is removed and mantained if <code>false</code>.
	 * @return
	 */
	public String getFilename(boolean excludeExtention){
		return UrlStringUtils.filename(url, excludeExtention);
	}

	/**
	 * 
	 * @return the path with the context removed.
	 */
	public String getContexlessPath(){
		return UrlStringUtils.path(url, context);
	}
	
	
	public String toString(){
		return this.url.toString();
	}

	public String getPath() {
		return this.url.toString();
	}
	

	public HttpUrl specify(HttpUrl root) {
		return new HttpUrl(root.toString() + this.url, root.getContext());
	}
	
	/**
	 * @return
	 */
	public CharSequence getContext() {
		return context;
	}
	
}
