package org.middleheaven.util.measure.convertion;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.structure.Field;

public class UnitConversion {


	private static Map<MapKey , UnitConverter> converters = new HashMap<MapKey , UnitConverter>();
	
	
	static {
		
		addConverter(AngularPosition.class, new AngleConverter(SI.RADIANS, SI.DEGREE));
		addConverter(DecimalMeasure.class, new AngleConverter(SI.RADIANS, SI.DEGREE));
		
	}
	
	private static void addConverter(Class<?> type, UnitConverter<?> converter){
		
		converters.put(new MapKey(type,converter.originalUnit(),converter.resultUnit()), converter);
	}
	
	private static  UnitConverter<?> converter(Class<?> type, Unit from, Unit to){
		
		MapKey key = new MapKey(type,from ,to);
		UnitConverter converter =converters.get(key);
		if (converter.resultUnit().equals(from)){
			converter =  converter.inverse();
		}
		return converter;
	}
	
	public static <T, F extends Field<F>, C extends Convertable<F, T> >  T convert (C original, Unit unit){
		UnitConverter<F> converter =   (UnitConverter<F>) converter(original.getClass(), original.unit(), unit);
		return original.convert(converter, unit);
	}
	
	public static class MapKey {

		
		private  Object A; 
		private  Object B;
		private String type;
		
		public MapKey(Class<?> type, Object a, Object b) {
			A = a;
			B = b;
			this.type = type.getName();
		}
		
		public int hashCode(){
			return type.hashCode() ^ A.hashCode() ^ B.hashCode();
		}
		
		public boolean equals(Object other) {
			return other instanceof MapKey && equals((MapKey) other);
		}

		public boolean equals(MapKey other) {
			 return  this.type.equals(other.type) && (  (A.equals(other.A) &&  B.equals(other.B)) || (A.equals(other.B) &&  B.equals(other.A)));
		}
	}
}
