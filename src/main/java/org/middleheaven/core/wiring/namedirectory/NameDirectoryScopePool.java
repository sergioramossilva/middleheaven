package org.middleheaven.core.wiring.namedirectory;

import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.ScopePool;
import org.middleheaven.core.wiring.WiringSpecification;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.namedirectory.NameDirectoryService;

public class NameDirectoryScopePool implements ScopePool {

	NameDirectoryService service;
	
	@Wire
	public NameDirectoryScopePool(@Service NameDirectoryService service){
		this.service = service;
	}
	
	@Override
	public <T> void add(WiringSpecification<T> spec, T object) {
		// TODO implement JNDIScopePool.add

	}

	@Override
	public void clear() {
		// not supported
	}

	@Override
	public <T> T getInScope(WiringSpecification<T> spec, Resolver<T> resolver) {
		
		String name = spec.getParam("name");
		
		if (name==null){
			throw new IllegalStateException("Name is not specified");
		}
		T obj = service.lookup(name, spec.getContract());
		if(obj==null){
			obj = resolver.resolve(spec);
			add(spec,obj);
		}
		
		return obj;
		
	}

	@Override
	public void remove(Object object) {
		// TODO implement JNDIScopePool.remove

	}

}
