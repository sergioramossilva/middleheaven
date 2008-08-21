package org.middleheaven.util.measure.convertion;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.measure.NonSI;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Scalable;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Measurable;

public class UnitConversion {


	private static Map<MapKey , UnitConverter> converters = new HashMap<MapKey , UnitConverter>();
	
	
	static {
		
		addConverter(new AngleConverter());
		addConverter(AditiveConverter.convert(SI.KELVIN, NonSI.CELSIUS,Real.valueOf("273.15")));
		addConverter(new FahrenheitCelciusConverter());
	}
	
	private static void addConverter(UnitConverter<?> converter){
		
		converters.put(new MapKey(converter.originalUnit(),converter.resultUnit()), converter);
	}
	
	public static <E extends Measurable, S extends Scalable<E,S>> S convert(S value, Unit<E> to){
		UnitConverter<E> converter =  getConverter(value.unit(), to);
		return converter.convertFoward(value);
	}
	
	private static <E extends Measurable> UnitConverter<E> getConverter(Unit<E> from , Unit<E> to){
		
		if (from.equals(to)){
			return new IdentityConverter<E>(from);
		}
		
		MapKey key = new MapKey(from ,to);
		UnitConverter<E> converter = converters.get(key);
		if (converter.resultUnit().equals(from)){
			converter =  converter.inverse();
		}
		return converter;
	}
	
	public static class MapKey {

		private  Object A; 
		private  Object B;
		
		public MapKey(Object a, Object b) {
			A = a;
			B = b;
		}
		
		public int hashCode(){
			return A.hashCode() ^ B.hashCode();
		}
		
		public boolean equals(Object other) {
			return other instanceof MapKey && equals((MapKey) other);
		}

		public boolean equals(MapKey other) {
			 return  (A.equals(other.A) &&  B.equals(other.B)) || (A.equals(other.B) &&  B.equals(other.A));
		}
	}
}
