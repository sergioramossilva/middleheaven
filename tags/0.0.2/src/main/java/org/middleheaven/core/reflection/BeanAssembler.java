package org.middleheaven.core.reflection;


public interface BeanAssembler {

	
	public <B> B assemble (Class<B> type);
}
