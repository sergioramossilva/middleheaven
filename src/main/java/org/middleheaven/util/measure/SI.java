package org.middleheaven.util.measure;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.measure.measures.Dimensionless;
import org.middleheaven.util.measure.measures.Distance;

/**
 * The International Standard Unit System
 * 
 * @author Sergio M.M. Taborda
 *
 * {@link http://physics.nist.gov/cuu/Units/}
 */
public final class SI implements UnitSystem{

	public static final Unit DIMENTIONLESS = Unit.unit(Dimension.DIMENTIONLESS,"");
	public static final Unit METER = Unit.unit(Dimension.LENGTH,"m");
	public static final Unit SECOND = Unit.unit(Dimension.TIME,"s");
	public static final Unit KILOGRAM = Unit.unit(Dimension.MASS,"kg");
	
	
	public static final Unit HOUR = Unit.unit(Dimension.TIME,"h");
	public static final Unit MILISECOND = Unit.unit(Dimension.TIME,"ms");
	
	public static final Unit NEWTON = Unit.unit(Dimension.FORCE,"N");
	public static final Unit AMPERE = Unit.unit(Dimension.CURRENT_INTENSITY,"A");
	

	private static final Map<String , Unit> allUnits = new HashMap<String , Unit> ();
	
	static {
		
		allUnits.put(Distance.class.getName(), METER);
		allUnits.put(Dimensionless.class.getName(), DIMENTIONLESS);
		// TODO complete
	}
	
	private static SI me = new SI();
	public static SI getInstance(){
		return me;
	}
	
	private SI(){}
	
	@SuppressWarnings("unchecked")
	@Override
	public Unit getMeasuableUnit(Class measurable) {
		return (Unit)allUnits.get(measurable);
	}

	@Override
	public  Collection<Unit> units() {
		return Collections.unmodifiableCollection(allUnits.values());
	}


}
