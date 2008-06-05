package org.middleheaven.util.measure.coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.middleheaven.util.measure.Measure;

/**
 * The position that a point occupies in a given n-dimensional reference frame.
 *
 * @param <F>
 */
public class Coordinate   {

	private CoordinateReferenceSystem refSystem; 
	private List<Measure> coordinates;
	private int dimention;
	
	
	
	protected Coordinate(CoordinateReferenceSystem refSystem,Measure<?,?>[] coordinates){
		
		this.dimention = refSystem.getDimention();
		if (coordinates.length != dimention){
			throw new IllegalArgumentException(coordinates.length  + " coordinates are provided but " + this.dimention + " are needed");
		}
		this.coordinates = new ArrayList<Measure>(Arrays.asList(coordinates));
	}
	
	public Measure<?,?> getOrdinate(int index){
		return coordinates.get(index);
	}

	public int getDimention(){
		return dimention;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return refSystem;
	}
}
