package org.middleheaven.global.calendar;

import org.middleheaven.util.measure.time.DateHolder;

public class DefaultEphemeris implements Ephemeris {

	private String name;
	private boolean isWorkingDay;
	private DateHolder date;
	
	public DefaultEphemeris(String name,boolean isWorkingDay, DateHolder date) {
		this.name = name;
		this.isWorkingDay = isWorkingDay;
		this.date = date;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isHoliday() {
		return isWorkingDay;
	}


	@Override
	public DateHolder getDate() {
		return date;
	}
	
	public boolean equals(Object other) {
		return other instanceof Ephemeris
				&& equals((Ephemeris) other);
	}

	public boolean equals(Ephemeris other) {
		return this.name.equals(other.getName()) && this.date.equals(other.getDate());
	}

	public int hashCode() {
		return name.hashCode() ^ date.hashCode();
	}

}
