package org.middleheaven.util.measure.coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Unit;

/**
 * The position that a point occupies in a given n-dimensional reference frame.
 *
 * @param <F>
 */
public class Coordinate   {

	private CoordinateReferenceSystem refSystem; 
	private List<DecimalMeasure<?>> coordinates;
	
	protected Coordinate(CoordinateReferenceSystem refSystem,DecimalMeasure<?> ... coordinates){
		
		if (coordinates.length !=  refSystem.getDimention()){
			throw new IllegalArgumentException(coordinates.length  + " coordinate values where provided but " +  refSystem.getDimention() + " are needed");
		}
		this.coordinates = new ArrayList<DecimalMeasure<?>>(Arrays.asList(coordinates));
	}
	
	public DecimalMeasure<?> getOrdinate(int index){
		return coordinates.get(index);
	}

	public Unit<?> getOrdinateUnit(int index){
		return coordinates.get(index).unit();
	}
	
	public int getDimention(){
		return refSystem.getDimention();
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return refSystem;
	}
}
