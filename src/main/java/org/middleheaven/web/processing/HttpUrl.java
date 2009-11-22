package org.middleheaven.web.processing;

import java.net.MalformedURLException;
import java.net.URL;

import org.middleheaven.util.UrlStringUtils;

public class HttpUrl {
	
	private URL url;
	private CharSequence context;

	public HttpUrl (CharSequence url, CharSequence context){
		this.context = context;
		if (url != null){
			try { //TODO url is null
				this.url = new URL(url.toString());
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	public String getFilename(){
		return UrlStringUtils.filename(url.toString(), false);
	}
	
	public String getFilename(boolean excludeExtention){
		return UrlStringUtils.filename(url.toString(), excludeExtention);
	}
	
	public String getContexlessPath(){
		return UrlStringUtils.removeContext(url.toString(), context);
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
	
	public URL toUrl(){
		return url;
	}
	
	public String toString(){
		return this.url.toString();
	}

	public String getPath() {
		return this.url.getPath();
	}
}
