package org.middleheaven.image;

import java.awt.Image;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.activation.Publish;

public class ImageServiceActivator extends ServiceActivator {

	ImageService service;
	
	@Publish
	public ImageService getImageService(){
		return service;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {
		
	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		
	}

	public static class MapImageService implements ImageService{

		Map<String, ImageSource> sources = new TreeMap<String, ImageSource>();
		
		@Override
		public Image getImage(String key) {
			Map<String, Object> properties = Collections.emptyMap();
			return getImage(key, properties);
		}

		@Override
		public Image getImage(String key, Map<String, Object> properties) {
			
			ImageSource source = sources.get(key);
			if (source==null){
				return null;
			}
			
			TreeMap<String,Object> params = new TreeMap<String,Object>(properties);
			params.put("key", key);
			return source.getImage(params);
		}

		@Override
		public void registerImage(String key, ImageSource source) {
			sources.put(key, source);
		}
		
	}


}
