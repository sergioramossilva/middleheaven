package org.middleheaven.web.processing.action;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BuildableWebCommandMappingService implements WebCommandMappingService{

	private final List<WebCommandMapping> mappings = new CopyOnWriteArrayList<WebCommandMapping>();

	
	/**
	 * Maps all requests directed to the specified internal URL to be handle by the specified presenter class
	 * @param url
	 * @param presenter
	 * @return
	 */
	public PresenterCommandMappingBuilder map(Class<?> presenter){

		PresenterCommandMappingBuilder builder = PresenterCommandMappingBuilder.map(presenter, this);

		this.mappings.add(builder.getMapping());
		return builder;
	}
	
	@Override
	public WebCommandMapping resolve(CharSequence url) {
		for (WebCommandMapping m : mappings){
			if (m.matches(url)){
				return m;
			}
		}
		return null;
	}

}
