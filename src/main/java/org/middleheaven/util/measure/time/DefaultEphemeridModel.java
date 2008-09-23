package org.middleheaven.util.measure.time;

public class DefaultEphemeridModel extends EphemeridModel {

	@Override
	public boolean isHoliday(DateHolder dateHolder) {
		return false;
	}

	@Override
	public boolean isWeekend(DateHolder dateHolder) {
	    DayOfWeek dw =  dateHolder.dayOfWeek();
	    return dw.equals(DayOfWeek.SATURDAY) || dw.equals(DayOfWeek.SUNDAY);
	}

	@Override
	public boolean isWorkingDay(DateHolder dateHolder) {
		  return !isWeekend(dateHolder) && !isHoliday(dateHolder);
	}


}
