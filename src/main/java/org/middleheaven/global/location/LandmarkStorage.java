package org.middleheaven.global.location;


public abstract class LandmarkStorage {


	public LandmarkStorage(){}
	
	public abstract void addLandmark(Landmark landmark, String category);
	public abstract void removeLandmark(Landmark landmark,String category);
	public abstract void deleteLandmark(Landmark landmark);
	public abstract void deleteCategory(String category);
	public abstract Iterable<LandmarkCategory> getCategories();
	public abstract Iterable<Landmark> getLandmarks();
	public abstract Iterable<Landmark> getLandmarks(String category,Coordinates min , Coordinates max);
	public abstract Iterable<Landmark> getLandmarks(String category,String name);
}
