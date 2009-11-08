package org.middleheaven.web.processing.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.web.processing.RequestCookie;

public class RequestCookieBag implements Iterable<RequestCookie> {

	private Map<String , RequestCookie> cookies = new HashMap<String,RequestCookie>();
	
	public RequestCookieBag (){
		
	}
	
	public void add (RequestCookie cookie){
		this.cookies.put(cookie.getName(), cookie);
	}
	
	public void remove (RequestCookie cookie){
		if(this.cookies.containsValue(cookie)){
			this.cookies.remove(cookie.getName());
		}
	}
	
	public RequestCookie getCookie(String name){
		return this.cookies.get(name);
	}

	@Override
	public Iterator<RequestCookie> iterator() {
		return this.cookies.values().iterator();
	}

	public RequestCookie[] toArray() {
		RequestCookie[] array = new RequestCookie[this.cookies.size()];
		this.cookies.values().toArray(array);
		return array;
	}

}
