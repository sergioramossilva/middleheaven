package org.middleheaven.quantity.time;




public abstract class AbstractChronology extends Chronology {

	@Override
	public <T extends TimePoint> T add(T point, Period period) {
		return reduce(timePointFor(point.milliseconds() + period.milliseconds()), point.getClass());
	}

	@SuppressWarnings("unchecked")
	protected <T extends TimePoint> T reduce(CalendarDateTime d , Class resultClass){
		if (resultClass.equals(CalendarDate.class)){
			return (T)d.toDate();
		} 
		return (T)d;
		
	}
	
	protected TimePoint now(){
		return this.referenceClock.getTime();
	}
	
	protected CalendarDateTime timePointFor(long milliseconds){
		return new CalendarDateTime(TimeContext.getTimeContext(), milliseconds);
	}
	
	protected Month month(int year, int month , int maxDaysInMonth){
		 return new Month(year,month,maxDaysInMonth);
	}
	
	protected Year year(int year,  int maxMonths){
		 return new Year(this,year,maxMonths);
	}
	
	protected DayOfWeek weekDay(int ordinal){
		 return DayOfWeek.valueOf(ordinal);
	}
}
