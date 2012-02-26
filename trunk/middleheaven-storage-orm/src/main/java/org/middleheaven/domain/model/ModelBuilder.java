package org.middleheaven.domain.model;

import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.model.ModelSet;


public interface ModelBuilder <M , MODELSET extends ModelSet<M> >{

	public  abstract MODELSET build(ClassSet classes);
	
}
