package org.middleheaven.quantity.measure;

import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.measurables.Measurable;
import org.middleheaven.quantity.structure.GroupAdditive;

public interface Amount<T, E extends Measurable> extends Quantity<E>, GroupAdditive<T> {

	
}
