package org.middleheaven.util.measure;

import java.util.Collection;

import org.middleheaven.util.measure.measures.Measurable;

/**
 * Represents a set of units 
 * 
 * @author Sergio M.M. Taborda
 *
 */
public interface UnitSystem {

	
	/**
	 * Obtains the correct unit, in this system, for a given measurable.
	 * @param measurable 
	 * @return the correct unit, in this system,for a given measurable.
	 */
	public  Unit getMeasuableUnit(Class<Measurable> measurable);

	/**
	 * 
	 * @return A set of all units endorsed by this units system.
	 */
	public  Collection<Unit> units();
}
