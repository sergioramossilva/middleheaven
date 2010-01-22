package org.middleheaven.chart;

import java.util.ArrayList;
import java.util.List;

public class BidimensionalSeries<X extends Comparable<X>, Y extends Comparable<Y>> extends AbstractSeries {

	int dimensions = 2;
	
	public BidimensionalSeries(String name) {
		super(name);
	}

	private static class Entry<X,Y> {
		X x;
		X open;
		X close;
		Y y;
		Y low;
		Y height;
		
		public X getX() {
			return x;
		}
		public Y getY() {
			return y;
		}
		public Entry(X x, X open, X close, Y y, Y low, Y height) {
			super();
			this.x = x;
			this.y = y;
		}
		
		@SuppressWarnings("unchecked")
		public <T extends Comparable<T>> T get(int dimension){
			if (dimension==0){
				return (T)x;
			} else if (dimension==1){
				return (T)y;
			} else if (dimension==2){
				return (T)open;
			} else if (dimension==3){
				return (T)close;
			} else if (dimension==4){
				return (T)low;
			} else if (dimension==5){
				return (T)height;
			} else {
				throw new IndexOutOfBoundsException();
			}
		}
	}
	

	List<Entry<X,Y>> values = new ArrayList<Entry<X,Y>>(); 
	
	public void add(X x , Y y){
		values.add(new Entry<X,Y>(x,null,null,y,null,null));
		this.fireChangeEvent();
	}
	
	public void add(X x , X open , X close , Y y , Y low, Y height){
		dimensions = 6;
		values.add(new Entry<X,Y>(x,open,close,y,height , low));
		this.fireChangeEvent();
	}
	
	
	@Override
	public int getDimensions() {
		return dimensions;
	}



	@Override
	public <T extends Comparable<T>> T getValue(int dimension, int index) {
		return values.get(index).get(dimension);
	}

	@Override
	public int size() {
		return values.size();
	}

}
