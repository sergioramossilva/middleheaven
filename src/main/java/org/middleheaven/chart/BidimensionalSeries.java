package org.middleheaven.chart;

import java.util.ArrayList;
import java.util.List;

public class BidimensionalSeries<X extends Comparable<X>, Y extends Comparable<Y>> extends AbstractSeries {

	int dimentions = 2;
	
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
		
		public <T extends Comparable<T>> T get(int dimention){
			if (dimention==0){
				return (T)x;
			} else if (dimention==1){
				return (T)y;
			} else if (dimention==2){
				return (T)open;
			} else if (dimention==3){
				return (T)close;
			} else if (dimention==4){
				return (T)low;
			} else if (dimention==5){
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
		dimentions = 6;
		values.add(new Entry<X,Y>(x,open,close,y,height , low));
		this.fireChangeEvent();
	}
	
	
	@Override
	public int getDimensions() {
		return dimentions;
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
