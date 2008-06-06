package org.middleheaven.chart;

import java.util.ArrayList;
import java.util.List;

public class HistogramSeries <C extends Comparable<C>, V extends Comparable<V>>  extends AbstractSeries {

	
	List<C> categories = new ArrayList<C>(); 
	List<V> values = new ArrayList<V>(); 
	
	public HistogramSeries(String name) {
		super(name);
	}
	
	public void add(C category , V value){
		categories.add(category);
		values.add(value);
	}

	@Override
	public int getDimensions() {
		return 2;
	}



	@Override
	public <T extends Comparable<T>> T getValue(int dimention, int index) {
		if (dimention==0){
			return (T)categories.get(index);
		} else if (dimention==1){
			return (T)values.get(index);
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int size() {
		return values.size();
	}

}
