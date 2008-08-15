package org.middleheaven.util.measure;

import java.sql.Time;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.measure.measures.Dimensionless;
import org.middleheaven.util.measure.measures.Distance;
import org.middleheaven.util.measure.measures.Mass;

/**
 * The International Standard Unit System
 * 
 * @author Sergio M.M. Taborda
 *
 * {@link http://physics.nist.gov/cuu/Units/}
 */
public final class SI implements UnitSystem{

	public static final Unit<Dimensionless> DIMENTIONLESS = Unit.unit(Dimension.DIMENTIONLESS,"");
	public static final Unit<Distance> METER = Unit.unit(Dimension.LENGTH,"m");
	public static final Unit<Time> SECOND = Unit.unit(Dimension.TIME,"s");
	public static final Unit<Mass> KILOGRAM = Unit.unit(Dimension.MASS,"kg");
	
	
	public static final Unit<Time> HOUR = Unit.unit(Dimension.TIME,"h");
	public static final Unit<Time> MILISECOND = Unit.unit(Dimension.TIME,"ms");
	
	public static final Unit NEWTON = Unit.unit(Dimension.FORCE,"N");
	public static final Unit AMPERE = Unit.unit(Dimension.CURRENT_INTENSITY,"A");
	
	public static final Unit RADIANS = Unit.unit(Dimension.ANGLE,"rad");
	public static final Unit DEGREE = Unit.unit(Dimension.ANGLE,"�");
	
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
