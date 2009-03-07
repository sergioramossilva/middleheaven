package org.middleheaven.quantity.unit;

import org.middleheaven.quantity.Quantity;


public interface UnitConverter<Q extends Quantity> {

	public Q convert(Q q);
	
	public UnitConverter<Q> inverse();
}
