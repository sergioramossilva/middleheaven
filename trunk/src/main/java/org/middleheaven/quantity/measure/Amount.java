package org.middleheaven.quantity.measure;

import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.math.structure.GroupAdditive;
import org.middleheaven.quantity.measurables.Measurable;

public interface Amount<T, E extends Measurable> extends Quantity<E>, GroupAdditive<T> {

	
}
