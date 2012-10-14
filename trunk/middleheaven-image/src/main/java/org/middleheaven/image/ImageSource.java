package org.middleheaven.image;

import java.awt.Image;
import java.util.Map;

public interface ImageSource {

	
	public Image getImage(Map<String,Object> params);
}
