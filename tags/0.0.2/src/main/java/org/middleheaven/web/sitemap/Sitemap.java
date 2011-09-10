package org.middleheaven.web.sitemap;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.middleheaven.quantity.time.CalendarDateTime;

public class Sitemap implements Iterable <SitemapEntry> {

	private String name;
	private CalendarDateTime lastModified;
	private Collection<SitemapEntry> entries = new LinkedList<SitemapEntry>();

	public Sitemap(){}
	
	public Sitemap(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public CalendarDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(CalendarDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public Sitemap addEntry(SitemapEntry entry){
		
		this.entries.add(entry);
		
		return this;
	}

	public Sitemap addEntry(String location, double prioriy) throws UnsupportedEncodingException{
		SitemapEntry entry = new SitemapEntry();
		
		entry.setLocation(location);
		entry.setPrioriy(prioriy);
		
		return this.addEntry(entry);
	}
	
	@Override
	public Iterator<SitemapEntry> iterator() {
		return entries.iterator();
	}
	
	
}
