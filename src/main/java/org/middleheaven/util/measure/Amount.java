package org.middleheaven.util.measure;

import org.middleheaven.util.measure.measures.Measurable;
import org.middleheaven.util.measure.structure.GroupAdditive;

public interface Amount<T, E extends Measurable> extends Quantity<E>, GroupAdditive<T> {

	
}
