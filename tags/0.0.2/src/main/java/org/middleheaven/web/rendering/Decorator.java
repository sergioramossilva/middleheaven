package org.middleheaven.web.rendering;

public class Decorator {

	private String page;
	private String uriPath;

	public Decorator(String page, String uriPath) {
		super();
		this.page = page;
		this.uriPath = uriPath;
	}
	
	public String getPage(){
		return page;
	}

	public String getURIPath(){
		return uriPath;
	}

	
	

}
