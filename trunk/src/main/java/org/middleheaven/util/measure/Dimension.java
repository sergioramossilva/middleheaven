package org.middleheaven.util.measure;

import java.io.Serializable;

import org.middleheaven.util.measure.measures.Aceleration;
import org.middleheaven.util.measure.measures.Action;
import org.middleheaven.util.measure.measures.Angle;
import org.middleheaven.util.measure.measures.Area;
import org.middleheaven.util.measure.measures.Currency;
import org.middleheaven.util.measure.measures.Density;
import org.middleheaven.util.measure.measures.Dimensionless;
import org.middleheaven.util.measure.measures.Distance;
import org.middleheaven.util.measure.measures.ElectricCarge;
import org.middleheaven.util.measure.measures.ElectricCurrent;
import org.middleheaven.util.measure.measures.Energy;
import org.middleheaven.util.measure.measures.Entropy;
import org.middleheaven.util.measure.measures.Force;
import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.measures.Power;
import org.middleheaven.util.measure.measures.Pressure;
import org.middleheaven.util.measure.measures.Temperature;
import org.middleheaven.util.measure.measures.Time;
import org.middleheaven.util.measure.measures.Velocity;
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
	public static final Dimension<ElectricCarge> ELECTRIC_CARGE = BaseDimention.base('C');
	
	
	// derived
	public static final Dimension<ElectricCurrent> CURRENT_INTENSITY = ELECTRIC_CARGE.over(TIME);
	public static final Dimension<Area> AREA = BaseDimention.base('L',2);
	public static final Dimension<Volume> VOLUME = BaseDimention.base('L',3);
	public static final Dimension<Density> DENSITY = MASS.over(VOLUME);
	public static final Dimension<Velocity> VELOCITY = LENGTH.over(TIME);
	public static final Dimension<Aceleration> ACELERATION = VELOCITY.over(TIME);
	public static final Dimension<Force> FORCE = ACELERATION.times(MASS);
	public static final Dimension<Pressure> PRESSURE = FORCE.over(AREA);
	public static final Dimension<Energy> ENERGY = FORCE.times(LENGTH);
	public static final Dimension<Action> ACTION = ENERGY.times(TIME);
	public static final Dimension<Power> POWER = ENERGY.over(TIME);
	public static final Dimension<Entropy> ENTROPY = ENERGY.over(TEMPERATURE);
	
	public Dimension<E> plus(Dimension<E> dimention) throws IncompatibleDimentionException{
		if (dimention.equals(this)){
			return this;
		}
		throw new IncompatibleDimentionException();
	}
	public Dimension<E> minus(Dimension<E> dimention) throws IncompatibleDimentionException{
		if (dimention.equals(this)){
			return this;
		}
		throw new IncompatibleDimentionException();
	}
	
	public abstract <T extends Measurable> Dimension<T> times(Dimension<?> dimention) ;
	
	public abstract <T extends Measurable> Dimension<T> over(Dimension<?> dimention);

	
    protected abstract <T extends Measurable> Dimension<T> simplify();
    

}
