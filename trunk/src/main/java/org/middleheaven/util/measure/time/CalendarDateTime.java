package org.middleheaven.util.measure.time;


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


}
