package org.middleheaven.quantity.convertion;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measure.Scalable;
import org.middleheaven.quantity.unit.NonSI;
import org.middleheaven.quantity.unit.SI;
import org.middleheaven.quantity.unit.Unit;

@SuppressWarnings("unchecked")
public class UnitConversion {


	
	private static Map<MapKey , UnitConverter> converters = new HashMap<MapKey , UnitConverter>();
	

	static {
	
		addConverter(new AngleConverter());
		addConverter( AditiveConverter.convert(SI.KELVIN, NonSI.CELSIUS,Real.valueOf("273.15")));
		addConverter(new FahrenheitCelciusConverter());
	}
	

	private static  void addConverter(UnitConverter converter){
		
		converters.put(new MapKey(converter.originalUnit(),converter.resultUnit()), converter);
	}
	
	public static <E extends Measurable,T extends Scalable<E,T>> T convert(T value,Unit<E> to){
		Unit<E> from = value.unit();
		if (from.equals(to)){
			return value;
		}
		
		MapKey key = new MapKey(from ,to);
		UnitConverter<E,T> converter = converters.get(key);
		if (converter.resultUnit().equals(from)){
			converter =  converter.inverse();
		}
		
		return converter.convertFoward(value);
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
