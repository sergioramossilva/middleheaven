package org.middleheaven.process.web.server.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.process.web.HttpRelativeUrl;

public class BuildableWebCommandMappingService implements WebCommandMappingService{

	private final List<WebCommandMapping> mappings = new CopyOnWriteArrayList<WebCommandMapping>();

	public BuildableWebCommandMappingService (){}
	
	/**
	 * Maps all requests directed to the specified internal URL to be handle by the specified presenter class
	 * @param url
	 * @param presenter
	 * @return
	 */
	public PresenterCommandMappingBuilder map(Class<?> presenter){

		PresenterCommandMappingBuilder builder = PresenterCommandMappingBuilder.map(presenter, this);

		addWebMapping(builder.getMapping());
		return builder;
	}
	
	
	protected void addWebMapping(WebCommandMapping webCommandMapping){
		this.mappings.add(webCommandMapping);
	}
	
	@Override
	public WebCommandMapping resolve(HttpRelativeUrl url) {
		
		
		WebCommandMapping mapping = null;
		double max = 0;
		
		for (WebCommandMapping m : mappings){
			double match = m.matches(url);
			if (  match > max ){
				mapping = m;
				max = match;
				if (max == 1d){
					break;
				}
			}
		}
		
		return mapping;
	}

}
