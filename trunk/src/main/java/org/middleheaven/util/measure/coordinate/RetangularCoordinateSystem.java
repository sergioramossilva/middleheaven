package org.middleheaven.util.measure.coordinate;

public class RetangularCoordinateSystem implements CoordinateReferenceSystem<RetangularCoordinate>{

	
	@Override
	public int getDimention() {
		return 3;
	}

	@Override
	public RetangularCoordinate toRetangular(RetangularCoordinate c) {
		return c;
	}

	@Override
	public RetangularCoordinate fromRetangular(RetangularCoordinate c) {
		return (RetangularCoordinate)c;
	}

}
