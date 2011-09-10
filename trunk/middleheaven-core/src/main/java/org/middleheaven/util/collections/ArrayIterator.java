package org.middleheaven.util.collections;


public final class ArrayIterator<T> extends IndexBasedIterator<T> {

	private T[] array;

	public ArrayIterator(T[] array) {
		super();
		this.array = array;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected T getObject(int index) {
		return array[index];
	}

	@Override
	protected int getSize() {
		return array.length;
	}

}
