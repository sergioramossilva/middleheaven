package org.middleheaven.core.dependency;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.wiring.WiringContext;

public class DependencyResolver {

	public DependencyResolver(){}

	public <T> void resolve (WiringContext wiringContext, List<T> dependables, Starter<T> starter){
		
		List<T> stack = starter.sort(dependables);
		LinkedList<T> newStack  = new  LinkedList<T>(stack);
		LinkedList<T> oldStack  = new  LinkedList<T>();
		
		boolean error = false;
		while (newStack.size() != oldStack.size()) {
			oldStack = newStack; 
			newStack = new  LinkedList<T>();
			for (Iterator<T> it = oldStack.iterator();  it.hasNext(); ){
				T dependable = it.next();
				try{
					starter.inicialize(dependable,wiringContext);
					it.remove();
				} catch (InicializationNotResolvedException e){
					// dependencies not available yet 
					it.remove();
					newStack.addLast(dependable);
				}  catch (InicializationNotPossibleException e){
					// dependencies will never be available
					error = true;
				}  
			}
		}

		if (error){
			throw new IllegalStateException("Not all dependencies could be resolve");
		}
		
		// proxy phase
		if (!newStack.isEmpty()){

		}
	}
}
