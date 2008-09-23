package org.middleheaven.storage.criteria;

import java.io.Serializable;

public interface Criterion extends Serializable{

	public boolean isEmpty();

	public Criterion simplify();

}
