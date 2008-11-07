package org.middleheaven.core.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.wiring.WiringContext;

public class DependencyLoader {

	public DependencyLoader(){}

	public <T,D extends DependableProperties> void load (WiringContext wiringContext, List<T> dependables, Starter<T, D> starter){
		
		List<D> stack = new ArrayList<D> ();
		
		for (T t : dependables){
			stack.add(starter.wrap(t));
		}

		Collections.sort (stack, new Comparator<DependableProperties>(){

			@Override
			public int compare(DependableProperties a, DependableProperties b) {
				return a.dependencyCount()- b.dependencyCount();
			}

		});
		
		LinkedList<DependableProperties> newStack  = new  LinkedList<DependableProperties>(stack);
		LinkedList<DependableProperties> oldStack  = new  LinkedList<DependableProperties>();
		
		while (newStack.size() != oldStack.size()) {
			oldStack = newStack; 
			newStack = new  LinkedList<DependableProperties>();
			for (Iterator<D> it = stack.iterator();  it.hasNext(); ){
				D properties = it.next();
				try{
					starter.inicialize(properties,wiringContext);
					it.remove();
				} catch (InicializationNotResolvedException e){
					// dependencies not available yet 
					it.remove();
					newStack.addLast(properties);
				}  catch (InicializationNotPossibleException e){
					// dependencies will never be available
				}  
			}
		}

		// proxy phase
		if (!newStack.isEmpty()){

		}
	}
}
