package org.middleheaven.util.measure;

import java.io.Serializable;

public abstract class Dimension implements Serializable {

	// special 
	public static final Dimension DIMENTIONLESS = new BaseDimention('1',0);
	public static final Dimension CURRENCY = new BaseDimention('$');
	// fundamental
	public static final Dimension ANGLE = new BaseDimention('\u0398');
	
	public static final Dimension LENGTH = new BaseDimention('L');
	public static final Dimension TIME = new BaseDimention('T');
	public static final Dimension MASS = new BaseDimention('M');
	public static final Dimension TEMPERATURE = new BaseDimention('K');
	public static final Dimension ELECTRIC_CARGE = new BaseDimention('C');
	
	// derived
	public static final Dimension CURRENT_INTENSITY = ELECTRIC_CARGE.over(TIME);
	public static final Dimension AREA = new BaseDimention('L',2);
	public static final Dimension VOLUME = new BaseDimention('L',3);
	public static final Dimension DENSITY = MASS.over(VOLUME);
	public static final Dimension VELOCITY = LENGTH.over(TIME);
	public static final Dimension ACELERATION = VELOCITY.over(TIME);
	public static final Dimension FORCE = ACELERATION.times(MASS);
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
