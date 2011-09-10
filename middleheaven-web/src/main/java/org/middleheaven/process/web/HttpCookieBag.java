package org.middleheaven.process.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HttpCookieBag implements Iterable<HttpCookie> {

	private Map<String , HttpCookie> cookies = new HashMap<String,HttpCookie>();
	
	public HttpCookieBag (){
		
	}
	
	public void add (HttpCookie cookie){
		this.cookies.put(cookie.getName(), cookie);
	}
	
	public void remove (HttpCookie cookie){
		if(this.cookies.containsValue(cookie)){
			this.cookies.remove(cookie.getName());
		}
	}
	
	public HttpCookie getCookie(String name){
		return this.cookies.get(name);
	}

	@Override
	public Iterator<HttpCookie> iterator() {
		return this.cookies.values().iterator();
	}

	public HttpCookie[] toArray() {
		HttpCookie[] array = new HttpCookie[this.cookies.size()];
		this.cookies.values().toArray(array);
		return array;
	}

}
