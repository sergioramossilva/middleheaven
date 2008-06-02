package org.middleheaven.utils.time;

public interface DateHolder {

	public int day();
	public int month();
	public int year();
	public DateHolder subtract(int i);
	public DateHolder add(int i);
	public DateHolder reduceTo(int wednesday);
	public boolean isBefore(DateHolder end);
	public DateHolder next();
	public DateHolder previous();
	public boolean isWeekDay(int sunday);
}
