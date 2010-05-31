package org.middleheaven.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class IteratorsIterator<T> implements Iterator<T>{


	List<Iterator<T>> iterators;
	int index=0;
	public < E extends Collection<T>> IteratorsIterator(Collection<E> collections) {
		super();
		iterators = new ArrayList<Iterator<T>>(collections.size());

		for (E c : collections){
			iterators.add(c.iterator());
		}

	}

	@Override
	public boolean hasNext() {
		if (iterators.isEmpty()){
			return false;
		}
		
		while (index < iterators.size()){
			
			if(iterators.get(index).hasNext()){
				return true;
			}
			index++;
		};
		
		index=0;
		return false;
	}

	@Override
	public T next() {
		return iterators.get(index).next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}


}
