package org.middleheaven.util.measure;

import org.middleheaven.math.structure.GroupAdditive;
import org.middleheaven.util.measure.measures.Measurable;

public interface Amount<T, E extends Measurable> extends Quantity<E>, GroupAdditive<T> {

	
}
