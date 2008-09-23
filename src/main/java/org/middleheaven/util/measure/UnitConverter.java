package org.middleheaven.util.measure;


public interface UnitConverter<Q extends Quantity> {

	public Q convert(Q q);
	
	public UnitConverter<Q> inverse();
}
