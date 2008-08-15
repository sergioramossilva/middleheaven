package org.middleheaven.global.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.util.measure.AngularPosition;

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
	public Iterable<String> getCategories() {
		Collection<LandmarkCategory> all = storage.createQuery(CriteriaBuilder.createCriteria(LandmarkCategory.class)).list();
		List<String> names = new ArrayList<String>(all.size());
		for (LandmarkCategory category : all){
			names.add(category.getName());
		}
		return names;
	}

	@Override
	public Iterable<Landmark> getLandmarks() {
		return storage.createQuery(CriteriaBuilder.createCriteria(Landmark.class)).list();
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, 
				AngularPosition minLongitude,AngularPosition minLatitude, 
				AngularPosition maxLongitude,AngularPosition maxLatitude) {
		
		return storage.createQuery(CriteriaBuilder.createCriteria(Landmark.class)
				.and("coordinates.latitude").inInterval(minLatitude,maxLatitude)
				.and("coordinates.longitude").inInterval(minLongitude, maxLongitude)
				.and("category").eq(category)
		).list();
	}

	@Override
	public Iterable<Landmark> getLandmarks(String category, String name) {
		return storage.createQuery(CriteriaBuilder.createCriteria(Landmark.class)
				.and("category").eq(category).or("name").eq(name)
		).list();
	}



	
}
