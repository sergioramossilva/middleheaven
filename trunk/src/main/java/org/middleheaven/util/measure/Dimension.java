package org.middleheaven.util.measure;

import java.io.Serializable;

import org.middleheaven.util.measure.measures.Angle;
import org.middleheaven.util.measure.measures.Area;
import org.middleheaven.util.measure.measures.Currency;
import org.middleheaven.util.measure.measures.Dimensionless;
import org.middleheaven.util.measure.measures.Distance;
import org.middleheaven.util.measure.measures.Force;
import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.measures.Temperature;
import org.middleheaven.util.measure.measures.Time;
import org.middleheaven.util.measure.measures.Volume;
import org.middleheaven.util.measure.measures.Mass;

public abstract class Dimension<E extends Measurable> implements Serializable {

	// special 
	public static final Dimension<Dimensionless> DIMENTIONLESS = BaseDimention.base('1',0);
	public static final Dimension<Currency> CURRENCY = BaseDimention.base('$');
	// fundamental
	public static final Dimension<Angle> ANGLE = BaseDimention.base('\u0398');
	
	public static final Dimension<Distance> LENGTH = BaseDimention.base('L');
	public static final Dimension<Time> TIME = BaseDimention.base('T');
	public static final Dimension<Mass> MASS = BaseDimention.base('M');
	public static final Dimension<Temperature> TEMPERATURE = BaseDimention.base('K');
	public static final Dimension ELECTRIC_CARGE = BaseDimention.base('C');
	
	// derived
	public static final Dimension CURRENT_INTENSITY = ELECTRIC_CARGE.over(TIME);
	public static final Dimension<Area> AREA = BaseDimention.base('L',2);
	public static final Dimension<Volume> VOLUME = BaseDimention.base('L',3);
	public static final Dimension DENSITY = MASS.over(VOLUME);
	public static final Dimension VELOCITY = LENGTH.over(TIME);
	public static final Dimension ACELERATION = VELOCITY.over(TIME);
	public static final Dimension<Force> FORCE = ACELERATION.times(MASS);
	public static final Dimension PRESSURE = FORCE.over(AREA);
	public static final Dimension ENERGY = FORCE.times(LENGTH);
	public static final Dimension ACTION = ENERGY.times(TIME);
	public static final Dimension POWER = ENERGY.over(TIME);
	public static final Dimension ENTROPY = ENERGY.over(TEMPERATURE);
	
	public Dimension plus(Dimension dimention) throws IncompatibleDimentionException{
		if (dimention.equals(this)){
			return this;
		}
		throw new IncompatibleDimentionException();
	}
	public Dimension minus(Dimension dimention) throws IncompatibleDimentionException{
		if (dimention.equals(this)){
			return this;
		}
		throw new IncompatibleDimentionException();
	}
	
	public abstract Dimension times(Dimension dimention) ;
	
	public abstract Dimension over(Dimension dimention);

	
    protected abstract Dimension simplify();
}
