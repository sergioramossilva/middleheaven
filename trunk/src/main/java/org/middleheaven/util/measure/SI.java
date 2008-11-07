package org.middleheaven.util.measure;

import org.middleheaven.util.measure.measures.Angle;
import org.middleheaven.util.measure.measures.Dimensionless;
import org.middleheaven.util.measure.measures.Distance;
import org.middleheaven.util.measure.measures.ElectricCurrent;
import org.middleheaven.util.measure.measures.Force;
import org.middleheaven.util.measure.measures.Mass;
import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.measures.Temperature;
import org.middleheaven.util.measure.measures.Time;

/**
 * The International Standard Unit System
 * 
 * @author Sergio M.M. Taborda
 *
 * {@link http://physics.nist.gov/cuu/Units/}
 */
public final class SI extends UnitSystem{

	public static final Unit<Dimensionless> DIMENTIONLESS = Unit.unit(Dimension.DIMENTIONLESS,"");
	public static final Unit<Distance> METER = Unit.unit(Dimension.LENGTH,"m");
	public static final Unit<Time> SECOND = Unit.unit(Dimension.TIME,"s");
	public static final Unit<Mass> KILOGRAM = Unit.unit(Dimension.MASS,"kg");
	
	
	public static final Unit<Time> HOUR = Unit.unit(Dimension.TIME,"h");
	public static final Unit<Time> MILISECOND = Unit.unit(Dimension.TIME,"ms");
	
	public static final Unit<Force> NEWTON = Unit.unit(Dimension.FORCE,"N");
	public static final Unit<ElectricCurrent> AMPERE = Unit.unit(Dimension.CURRENT_INTENSITY,"A");
	
	public static final Unit<Angle> RADIANS = Unit.unit(Dimension.ANGLE,"rad");
	public static final Unit<Angle> DEGREE = Unit.unit(Dimension.ANGLE,"º");
	
	public static final Unit<Temperature> KELVIN = Unit.unit(Dimension.TEMPERATURE,"K");
	
	
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
	




}
