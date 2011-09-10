package org.middleheaven.core.wiring;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * A model for wiring.
 * The wiring model consists of construct-point where the object is created/retrieved and multiple after-points where the object is injected.
 * 
 */
public class WiringModel {

	private ProducingWiringPoint point;
	private final Collection<AfterWiringPoint> afterpoints = new HashSet<AfterWiringPoint>();
	
	/**
	 * 
	 * Constructor.
	 */
	public WiringModel(){}
	
	/**
	 * Obtains the {@link ProducingWiringPoint} that will obtain the object.
	 * @return the {@link ProducingWiringPoint}
	 */
	public ProducingWiringPoint getProducingWiringPoint(){
		return point;
	}
	
	/**
	 * Attributes the {@link ProducingWiringPoint} to use.
	 * @param point the {@link ProducingWiringPoint} to use.
	 */
	public void setProducingWiringPoint(ProducingWiringPoint point){
		if (this.point == null){
			this.point = point;
		} else {
			this.point = this.point.merge(point);
		}
	}
	
	/**
	 * 
	 * @return all the {@link AfterWiringPoint}s in this model.
	 */
	public Collection<AfterWiringPoint> getAfterPoints(){
		return Collections.unmodifiableCollection(afterpoints);
	}
	
	/**
	 * Add an {@link AfterWiringPoint}. A {@link AfterWiringPoint} is used to inject other objects after creation.
	 * @param point the {@link AfterWiringPoint} to add to this model.
	 */
	public void addAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.add(point);
	}
	
	/**
	 * Remove an {@link AfterWiringPoint}. A {@link AfterWiringPoint} is used to inject other objects after creation.
	 * @param point the {@link AfterWiringPoint} to remove to this model.
	 */
	public void removeAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.remove(point);
	}
	
	/**
	 * 
	 * @return <code>true</code> if any of the after-points is required, <code>false</code> if all after-points are optional.
	 */
	public boolean isRequired() {
		for (AfterWiringPoint ap : afterpoints){
			if(ap.isRequired()){
				return true;
			}
		}
		return false;
	}
}
