package org.middleheaven.quantity.unit;

import java.util.LinkedHashSet;
import java.util.Set;

import org.middleheaven.quantity.Quantity;



public class CompositeConverter<Q extends Quantity> implements UnitConverter<Q>{

	Set<UnitConverter> converters = new LinkedHashSet<UnitConverter>();
	public CompositeConverter(){}
	
	public void addConverter (UnitConverter<Q> converter ){
		converters.add(converter);
	}
	public void removeConverter (UnitConverter<Q> converter ){
		converters.remove(converter);
	}
	
	public Q convert(Q q) {

		Q r = q;
		for (UnitConverter<Q> converter : converters ){
			r = converter.convert(r);
		}
		return r;
	}

	public UnitConverter<Q> inverse() {
		// insert converters in reversed order
		CompositeConverter inv = new CompositeConverter();
		UnitConverter<Q>[] conv = converters.toArray(new UnitConverter[converters.size()]);
		
		for (int i=conv.length - 1 ; i >=0 ; i--){
			inv.addConverter(conv[i].inverse());
		}
		
		return inv;
	}

}
