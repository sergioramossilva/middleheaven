package org.middleheaven.chart;

import java.util.ArrayList;
import java.util.List;

public class TridimensionalSeries<X extends Comparable<X>, Y extends Comparable<Y>, Z extends Comparable<Z>> extends AbstractSeries {

	public TridimensionalSeries(String name) {
		super(name);
	}

	private static class Entry<X,Y,Z> {
		X x;
		Y y;
		Z z;

		public Entry(X x, Y y,Z z) {
			super();
			this.x = x;
			this.y = y;
		}
		
		public <T extends Comparable<T>> T get(int dimention){
			if (dimention==0){
				return (T)x;
			} else if (dimention==1){
				return (T)y;
			} else if (dimention==2){
				return (T)z;
			} else {
				throw new IndexOutOfBoundsException();
			}
		}
	}
	

	List<Entry<X,Y,Z>> values = new ArrayList<Entry<X,Y,Z>>(); 
	
	public void add(X x , Y y,Z z){
		values.add(new Entry<X,Y,Z>(x,y,z));
	}

	@Override
	public int getDimensions() {
		return 3;
	}



	@Override
	public <T extends Comparable<T>> T getValue(int dimention, int index) {
		return values.get(index).get(dimention);
	}

	@Override
	public int size() {
		return values.size();
	}

}
