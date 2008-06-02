package org.middleheaven.utils.time;

public class MonthOfYear {

	
	private int month;
	private int year;
	public int getMonth() {
		return month;
	}
	public int getYear() {
		return year;
	}
	public MonthOfYear( int year,int month) {
		super();
		this.month = month;
		this.year = year;
	}
	
	
	public DateHolder firstDay(){
		return new XCalendarDate(year,month,1);
	}
}
