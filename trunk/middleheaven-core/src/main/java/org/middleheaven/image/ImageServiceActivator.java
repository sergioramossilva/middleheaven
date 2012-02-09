package org.middleheaven.image;

import java.awt.Image;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.service.Service;

public class ImageServiceActivator extends Activator {

	ImageService service;
	
	@Publish
	public ImageService getImageService(){
		return service;
	}

	@Service
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


	@Override
	public void activate(ActivationContext context) {
		this.service  = new MapImageService();
	}


	@Override
	public void inactivate(ActivationContext context) {
		this.service = null;
	}


}
