package org.middleheaven.quantity.unit;

import java.io.Serializable;

import org.middleheaven.quantity.measurables.Aceleration;
import org.middleheaven.quantity.measurables.Action;
import org.middleheaven.quantity.measurables.Angle;
import org.middleheaven.quantity.measurables.Area;
import org.middleheaven.quantity.measurables.Currency;
import org.middleheaven.quantity.measurables.Density;
import org.middleheaven.quantity.measurables.Dimensionless;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measurables.ElectricCarge;
import org.middleheaven.quantity.measurables.ElectricCurrent;
import org.middleheaven.quantity.measurables.Energy;
import org.middleheaven.quantity.measurables.Entropy;
import org.middleheaven.quantity.measurables.Force;
import org.middleheaven.quantity.measurables.Mass;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.measurables.Power;
import org.middleheaven.quantity.measurables.Pressure;
import org.middleheaven.quantity.measurables.Temperature;
import org.middleheaven.quantity.measurables.Time;
import org.middleheaven.quantity.measurables.Velocity;
import org.middleheaven.quantity.measurables.Volume;

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
