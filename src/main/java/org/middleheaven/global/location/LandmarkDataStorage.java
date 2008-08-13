package org.middleheaven.global.location;

import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.criteria.CriteriaBuilder;

public class LandmarkDataStorage extends LandmarkStorage {

	
	private DataStorage storage;


	private class CategorizedLandmark {
		private Landmark landmark;
		private LandmarkCategory category;
		
		public CategorizedLandmark() {}
		
		public CategorizedLandmark(Landmark landmark, String category) {
			super();
			this.landmark = landmark;
			this.category = new LandmarkCategory(category);
		}
		
	}
	@Override
	public void addLandmark(Landmark landmark, String category) {
		CategorizedLandmark cl = new CategorizedLandmark(landmark, category);
		storage.store(cl);
	}

	@Override
	public void removeLandmark(Landmark landmark, String category) {
		CategorizedLandmark cl = new CategorizedLandmark(landmark, category);
		storage.store(cl);
	}
	
	@Override
	public void deleteCategory(String category) {
		storage.remove(new LandmarkCategory(category));
	}

	@Override
	public void deleteLandmark(Landmark landmark) {
		storage.remove(landmark);
	}

	@Override
	public Iterable<LandmarkCategory> getCategories() {
		return storage.createQuery(CriteriaBuilder.createCriteria(LandmarkCategory.class)).list();
	}

	@Override
	public Iterable<Landmark> getLandmarks() {
		return storage.createQuery(CriteriaBuilder.createCriteria(Landmark.class)).list();
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, Coordinates min,Coordinates max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, String name) {
		return storage.createQuery(CriteriaBuilder.createCriteria(Landmark.class)
				.where("category").eq(category).or("name").eq(name)
		).list();
	}



	
}
