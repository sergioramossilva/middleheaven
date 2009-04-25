package org.middleheaven.core.wiring.activation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.middleheaven.core.wiring.WiringModel;

public class UnitActivatorDepedencyModel extends WiringModel{

	
	private Collection<PublishPoint> PublishPoints = new LinkedList<PublishPoint>();

	public Collection<PublishPoint> getPublishPoints() {
		return Collections.unmodifiableCollection(PublishPoints);
	}

	public void addPublishPoint(PublishPoint element) {
		this.PublishPoints.add(element);
	}

	public void removePublishPoint(PublishPoint element) {
		this.PublishPoints.remove(element);
	}
}
