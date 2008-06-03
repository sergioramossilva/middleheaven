package org.middleheaven.util.measure;

import java.util.HashMap;
import java.util.Map;


public class Units {


	private static Map<String,Unit> units= new HashMap<String,Unit>(); 
	
	static {
		addUnit(new BaseUnit(Dimension.TEMPERATURE , "ºC")); // Celcius
		addUnit(new BaseUnit(Dimension.TEMPERATURE , "K")); // Kelvin
		addUnit(new BaseUnit(Dimension.TEMPERATURE , "R")); // Rankine
		addUnit(new BaseUnit(Dimension.TEMPERATURE , "ºF")); // Fareihight
	}

	private static void addUnit(Unit unit){
		units.put(unit.symbol(), unit);
	}
	
	public static  Unit getUnit(String unitSymbol){
		return units.get(unitSymbol.trim());
	}
}
