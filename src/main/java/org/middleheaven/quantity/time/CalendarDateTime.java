package org.middleheaven.quantity.time;

import java.util.Date;


public class CalendarDateTime extends AbstractTimePoint implements  DateHolder , TimeHolder  {

	public static CalendarDateTime now(){
		return new CalendarDateTime(TimeContext.getTimeContext(), TimeContext.getTimeContext().now().getMilliseconds());
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
    
	@Override
	public int dayOfYear() {
		 return context.getChronology().yearDay(this);
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
    
	@Override
	public int hour() {
		return context.getChronology().timeOf(this).hour();
	}

	@Override
	public int minute() {
		return context.getChronology().timeOf(this).minute();
	}

	@Override
	public int second() {
		return context.getChronology().timeOf(this).second();
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
    	return context.getChronology().add(this, Duration.of().days(1));
    }
    
    public CalendarDateTime previousDate(){
    	return context.getChronology().add(this, Duration.of().days(-1));
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
		
		int diff= weekDay.calendarOrdinal() -  this.dayOfWeek().calendarOrdinal();
		
		return diff==0? this : this.plus(Duration.of().days(diff));
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
		return other.getMilliseconds() == this.milliseconds;
	}

	public int hashCode() {
		return (int)this.getMilliseconds();
	}
	
	public String toString(){
		return new Date(this.milliseconds).toString();
	}

	public CalendarTime toTime() {
		return new CalendarTime(this.context, this.milliseconds);
	}

    public CalendarDate toDate(){
    	return new CalendarDate(this.context, this.milliseconds);
    }


	

}
