/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.util.measure.time;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author Sergio M.M. Taborda
 */
public class Year implements Serializable{

    private int ordinal;
   
    private int monthInYear;
    Chronology chronology;
    
    
    protected Year (Chronology chronology , int ordinal,int monthInYear){
        this.ordinal = ordinal;
        this.monthInYear =  monthInYear;
        this.chronology = chronology;
    }
    
    public int ordinal(){
        return this.ordinal;
    }
    
    public int monthCount(){
    	return monthInYear;
    }
    
    public Iterator<Month> monthIterator(){
        return new MonthIterator(); 
    }
    
    public Month endMonth(){
    	return Month.ofYear(ordinal, monthInYear);
    }
    
    public Month startMonth(){
    	return Month.ofYear(ordinal, 1);
    }
    
    public Month month(int month){
    	
    	return chronology.monthOf(ordinal, month);
    }
    
    private  class MonthIterator implements Iterator<Month>{

    	private int index = -1;
		@Override
		public boolean hasNext() {
			return index < monthInYear-1;
		}

		@Override
		public Month next() {
			return month(index);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
    	
    }
}
