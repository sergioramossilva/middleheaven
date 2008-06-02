package org.middleheaven.global.calendar;

public class DefaultEphemeris implements Ephemeris {

	private String name;
	private String description;
	private boolean isWorkingDay;
	
	public DefaultEphemeris(String name,boolean isWorkingDay) {
		this(name,isWorkingDay,"");
	}
	
	public DefaultEphemeris(String name,boolean isWorkingDay, String description) {
		this.name = name;
		this.description = description;
		this.isWorkingDay = isWorkingDay;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isWorkingDay() {
		return isWorkingDay;
	}

}
