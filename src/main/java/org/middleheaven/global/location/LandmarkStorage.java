package org.middleheaven.global.location;

import org.middleheaven.util.measure.AngularPosition;


public abstract class LandmarkStorage {


	public LandmarkStorage(){}
	
	public abstract void addLandmark(Landmark landmark, String category);
	public abstract void removeLandmark(Landmark landmark,String category);
	public abstract void deleteLandmark(Landmark landmark);
	public abstract void deleteCategory(String category);
	public abstract Iterable<String> getCategories();
	public abstract Iterable<Landmark> getLandmarks();
	public abstract Iterable<Landmark> getLandmarks(String category, AngularPosition minLongitude,AngularPosition minLatitude, AngularPosition maxLongitude,AngularPosition maxLatitude);
	public abstract Iterable<Landmark> getLandmarks(String category,String name);
}
