package org.middleheaven.core.wiring.activation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.middleheaven.core.wiring.WiringModel;

public class UnitActivatorDepedencyModel extends WiringModel{

	
	private Collection<PublishPoint> publishPoints = new LinkedList<PublishPoint>();
	private Class<? extends Activator> activatorType;

	public UnitActivatorDepedencyModel(Class<? extends Activator> activatorType){
		this.activatorType = activatorType;
	}
	
	public Collection<PublishPoint> getPublishPoints() {
		return Collections.unmodifiableCollection(publishPoints);
	}

	public void addPublishPoint(PublishPoint element) {
		this.publishPoints.add(element);
	}

	public void removePublishPoint(PublishPoint element) {
		this.publishPoints.remove(element);
	}
	
	public String toString(){
		return publishPoints.toString();
	}
	
	public int hashCode(){
		return activatorType.getName().hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof UnitActivatorDepedencyModel && 
			((UnitActivatorDepedencyModel)other).activatorType.equals(this.activatorType);
	}
}
