/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.util.measure.time;

import java.util.Iterator;

/**
 * @author Sergio M.M. Taborda
 */
public class Year {


    private int ordinal;
   
    private int monthInYear;
    
    protected Year (int ordinal,int monthInYear){
        this.ordinal = ordinal;
        this.monthInYear =  monthInYear;
    }
    
    public int ordinal(){
        return this.ordinal;
    }
    
    public int monthCount(){
    	return monthInYear;
    }
    
    public Iterator<DateHolder> dayIterator(){
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public Iterator<Month> monthIterator(){
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public Month endMonth(){
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public Month startMonth(){
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public Month month(int month){
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
