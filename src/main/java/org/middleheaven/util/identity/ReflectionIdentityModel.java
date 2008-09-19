package org.middleheaven.util.identity;

import org.middleheaven.core.reflection.ReflectionUtils;

public class ReflectionIdentityModel extends AbstractIdentityModel {

	Class<?> sequenceType;
	
	public ReflectionIdentityModel(Class<? extends IdentitySequence<?>> sequenceType){
		this.sequenceType = sequenceType;
	}
	
	protected IdentitySequence<?> newSequenceInstance(Class<?> type) {
		return (IdentitySequence<?>)ReflectionUtils.newInstance(sequenceType);
	}
}
