package org.middleheaven.process.web;

import java.net.MalformedURLException;
import java.net.URL;

import org.middleheaven.util.UrlStringUtils;

/**
 * Encapsulates an url an context information.
 */
public class HttpUrl {

	protected URL url;
	private CharSequence context;

	/**
	 * 
	 * Constructor.
	 * @param url the url to handle.
	 * @param context the current server context.
	 */
	public HttpUrl (CharSequence url, CharSequence context){
		this.context = context;
		if (url == null){
			throw new IllegalArgumentException("Url is required");
		}
		try { 
			this.url = new URL(url.toString());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}

	}

	/**
	 * The file name. If any. 
	 * The filename includes any extension, if present.
	 * 
	 * @return the filename the url refers to or a blank string if no file is present in the name.
	 */
	public String getFilename(){
		return UrlStringUtils.filename(url.toString(), false);
	}

	/**
	 * The file name without the extension.
	 * @param excludeExtention if <code>true</code>, the extention is removed and mantained if <code>false</code>.
	 * @return
	 */
	public String getFilename(boolean excludeExtention){
		return UrlStringUtils.filename(url.toString(), excludeExtention);
	}

	/**
	 * 
	 * @return the path with the context removed.
	 */
	public String getContexlessPath(){
		return UrlStringUtils.path(url.toString(), context);
	}
	
	

	public String getAuthority(){
		return url.getAuthority();
	}

	public String getHost(){
		return url.getHost();
	}

	public String getProtocol(){
		return url.getProtocol();
	} 

	public int getPort(){
		return url.getPort();
	}

	public String toString(){
		return this.url.toString();
	}

	public String getPath() {
		return this.url.getPath();
	}
	
	public String getQueryString(){
		return null; // TODO
	}

	/**
	 * @return
	 */
	public CharSequence getContext() {
		return context;
	}
	
}
