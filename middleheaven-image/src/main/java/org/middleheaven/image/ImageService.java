package org.middleheaven.image;

import java.awt.Image;
import java.util.Map;

public interface ImageService {
	
	
	public Image getImage(String key);
	public Image getImage(String key, Map<String,Object> properties);
	
	public void registerImage(String key, ImageSource source);

}
