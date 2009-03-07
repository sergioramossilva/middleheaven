package org.middleheaven.global.location;

import org.middleheaven.quantity.measure.AngularMeasure;


public abstract class LandmarkRepository {


	public LandmarkRepository(){}
	
	public abstract void addLandmark(Landmark landmark, String category);
	public abstract void removeLandmark(Landmark landmark,String category);
	public abstract void deleteLandmark(Landmark landmark);
	public abstract void deleteCategory(String category);
	public abstract Iterable<String> getCategories();
	public abstract Iterable<Landmark> getLandmarks();
	public abstract Iterable<Landmark> getLandmarks(String category, AngularMeasure minLongitude,AngularMeasure minLatitude, AngularMeasure maxLongitude,AngularMeasure maxLatitude);
	public abstract Iterable<Landmark> getLandmarks(String category,String name);
}
