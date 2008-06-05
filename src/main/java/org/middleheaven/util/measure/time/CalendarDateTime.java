package org.middleheaven.util.measure.time;

import java.util.Date;


public class CalendarDateTime extends AbstractTimePoint implements  DateHolder  {

	public static CalendarDateTime now(){
		return new CalendarDateTime(TimeContext.getTimeContext(), TimeContext.getTimeContext().now().milliseconds());
	}
	
	static CalendarDateTime origin(){
		return new CalendarDateTime(TimeContext.getTimeContext(), 0);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8177265376553572402L;

	protected CalendarDateTime (TimeContext context, long timeFromEpoc){
		super(context,timeFromEpoc);
	}
	

    public DayOfMonth day() {
       return context.getChronology().monthDay(this);
    }
    
    public DayOfMonth dayOfMonth() {
        return context.getChronology().monthDay(this);
     }
    
    public DayOfWeek dayOfWeek(){
        return context.getChronology().weekDay(this);
    }
    
    public Month month(){
        return context.getChronology().monthOf(this);
    }
    
    public Year year(){
        return context.getChronology().yearOf(this);
    }
    
    
    public boolean isWeekend(){
        return context.getReferenceWorkCalendar().isWeekend(this);
    }
    
    public boolean isHoliday(){
        return context.getReferenceWorkCalendar().isHoliday(this);
    }
    
    public boolean isWorkingDay(){
        return context.getReferenceWorkCalendar().isWorkingDay(this);
    }
    
    public CalendarDateTime nextDate(){
    	return context.getChronology().add(this, Duration.days(1));
    }
    
    public CalendarDateTime previousDate(){
    	return context.getChronology().add(this, Duration.days(-1));
    }
    
    public CalendarDate toDate(){
    	return new CalendarDate(this.context, this.milliseconds);
    }
    
    public CalendarDateTime plus (ElapsedTime elapsed){
    	return this.context.getChronology().add(this, elapsed);
    }
    
    public CalendarDateTime minus (ElapsedTime elapsed){
    	return this.context.getChronology().add(this, (ElapsedTime)elapsed.negate());
    }

	@Override
	public boolean isBefore(DateHolder other) {
		return this.compareTo((DateHolder)CalendarDate.date(other.year().ordinal(),other.month().ordinal(),other.dayOfMonth().getDay()))<0;
	}

	@Override
	public boolean isAfter(DateHolder other) {
		return this.compareTo((DateHolder)CalendarDate.date(other.year().ordinal(),other.month().ordinal(),other.dayOfMonth().getDay()))>0;
	}

	@Override
	public DateHolder nearestBefore(DayOfWeek weekDay) {
		DateHolder day = this;
		
		while (!day.dayOfWeek().equals(weekDay)){
			day = day.previousDate();
		}
		
		return day;
	}
	
	@Override
	public DateHolder nearestAfter(DayOfWeek weekDay) {
		DateHolder day = this;
		
		while (!day.dayOfWeek().equals(weekDay)){
			day = day.nextDate();
		}
		
		return day;
	}
	
	@Override
	public DateHolder nearest(DayOfWeek weekDay) {
		
		int diff= weekDay.ordinal() -  this.dayOfWeek().ordinal();
		
		return diff==0? this : this.plus(Duration.days(diff));
	}
	
	@Override
	public int compareTo(DateHolder other) {
		return this.compareTo((TimePoint)CalendarDate.date(other.year().ordinal(),other.month().ordinal(),other.dayOfMonth().getDay()));
	}

	public boolean equals(Object other) {
		return other instanceof TimePoint
				&& equals((TimePoint) other);
	}

	public boolean equals(TimePoint other) {
		return other.milliseconds() == this.milliseconds;
	}

	public int hashCode() {
		return (int)this.milliseconds();
	}
	
	public String toString(){
		return new Date(this.milliseconds).toString();
	}
}
