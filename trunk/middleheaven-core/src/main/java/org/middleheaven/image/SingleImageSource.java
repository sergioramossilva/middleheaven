package org.middleheaven.image;

import java.awt.Image;
import java.util.Map;

public class SingleImageSource implements ImageSource{

	public static SingleImageSource store(Image image){
		return new SingleImageSource(image);
	}

	private Image image;
	
	public SingleImageSource(Image image){
		this.image = image;
	}
	
	@Override
	public Image getImage(Map<String, Object> params) {
		return image;
	}

}
