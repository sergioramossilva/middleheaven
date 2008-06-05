package org.middleheaven.util.measure.coordinate;

import org.middleheaven.util.measure.Measure;
import org.middleheaven.util.measure.structure.Field;

public class GeographicReferenceSystem implements CoordinateReferenceSystem<LatLong> {

	@Override
	public LatLong coordinateAt(Measure ... ordinates) {
		if (ordinates.length > 2){
			throw new IllegalArgumentException();
		}
		return new LatLong(this,ordinates);
	}

	@Override
	public int getDimention() {
		return 2;
	}

}
