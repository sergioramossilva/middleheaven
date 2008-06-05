package org.middleheaven.util.measure.coordinate;

import org.middleheaven.util.measure.Measure;
import org.middleheaven.util.measure.structure.Field;

public interface CoordinateReferenceSystem<C extends Coordinate<?>> {

	
	public int getDimention();
	
	public C coordinateAt(Measure ... ordinates);
}
