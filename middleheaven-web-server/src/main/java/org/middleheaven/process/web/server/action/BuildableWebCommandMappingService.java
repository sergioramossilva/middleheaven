package org.middleheaven.process.web.server.action;

import java.util.List;
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
		for (WebCommandMapping m : mappings){
			if (m.matches(url)){
				return m;
			}
		}
		return null;
	}

}
