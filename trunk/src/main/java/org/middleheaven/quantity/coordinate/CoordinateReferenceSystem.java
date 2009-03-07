package org.middleheaven.quantity.coordinate;


public interface CoordinateReferenceSystem<C extends Coordinate> {

	public int getDimention();
	
	public RetangularCoordinate toRetangular(C c);
	
	public C fromRetangular(RetangularCoordinate c);
	
}
