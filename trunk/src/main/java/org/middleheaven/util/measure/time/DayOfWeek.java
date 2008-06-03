/*
 * Created on 10/Mar/2007
 */
package org.middleheaven.util.measure.time;

import java.util.Calendar;

/**
 * @author Sergio M.M. Taborda
 */
public final class DayOfWeek  {

    public static final DayOfWeek SUNDAY = new DayOfWeek(Calendar.SUNDAY);
    public static final DayOfWeek MONDAY = new DayOfWeek(Calendar.MONDAY);
    public static final DayOfWeek TUESDAY = new DayOfWeek(Calendar.TUESDAY);
    public static final DayOfWeek WEDNESDAY = new DayOfWeek(Calendar.WEDNESDAY);
    public static final DayOfWeek THURSDAY = new DayOfWeek(Calendar.THURSDAY);
    public static final DayOfWeek FRIDAY = new DayOfWeek(Calendar.FRIDAY);
    public static final DayOfWeek SATURDAY = new DayOfWeek(Calendar.SATURDAY);
    
    
    private static DayOfWeek[] week = new DayOfWeek[]{ SUNDAY , MONDAY , TUESDAY, WEDNESDAY , THURSDAY, FRIDAY, SATURDAY};

    private final int ordinal;
    
    static DayOfWeek valueOf (final int ordinal){
        DayOfWeek[] week = values();
        for (int i=0;i < week.length; i++){
            if (week[i].ordinal == ordinal){
                return week[i];
            }
        }
        // is must have found the day
        throw new IllegalArgumentException ( ordinal + " is not a valid day of the week");
    }
    
    public static DayOfWeek[] values (){
        return week;
    }
    
    
    /**
     * @param sunday2
     */
    private DayOfWeek(int ordinal) {
         this.ordinal = ordinal;
    }

    public boolean equals(DayOfWeek other){
        return this == other; // Enum pattern garantees the use of ==
    }
    
    public boolean equals(Object other){
        return other instanceof DayOfWeek && equals((DayOfWeek)other);
    }
    
    public int ordinal(){
        return ordinal;
    }
    
}
