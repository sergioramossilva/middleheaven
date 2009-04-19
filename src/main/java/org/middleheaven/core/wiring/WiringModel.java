package org.middleheaven.core.wiring;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class WiringModel {

	private ConstructorWiringPoint point;
	private final Collection<AfterWiringPoint> afterpoints = new HashSet<AfterWiringPoint>();
	
	public ConstructorWiringPoint getConstructorPoint(){
		return point;
	}
	
	public void setConstructorPoint(ConstructorWiringPoint point){
		this.point = point;
	}
	
	public Collection<AfterWiringPoint> getAfterPoints(){
		return Collections.unmodifiableCollection(afterpoints);
	}
	
	public void addAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.add(point);
	}
	
	public void removeAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.remove(point);
	}
}
