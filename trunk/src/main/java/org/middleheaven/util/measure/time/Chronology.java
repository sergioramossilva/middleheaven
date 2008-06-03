package org.middleheaven.util.measure.time;



public abstract class Chronology {


	protected Clock referenceClock;
	public void setClock(Clock referenceClock){
		this.referenceClock = referenceClock;
	}
	
	public abstract long milisecondsFor(boolean lenient,int year, int month, int day );
	
	public <T extends TimePoint> T add(T point , ElapsedTime elapsed){
		if (elapsed instanceof Duration){
			return add(point , (Duration)elapsed);
		} else {
			return add(point , (Duration)elapsed);
		}	
    }

	public abstract <T extends TimePoint> T add(T point , Duration eplased);
	
	public abstract <T extends TimePoint> T add(T point , Period eplased);
	
    public abstract DayOfMonth monthDay(TimePoint point);
    
    public abstract DayOfWeek weekDay(TimePoint point);
    
    public abstract Month monthOf(TimePoint point);
    
    public abstract Year yearOf(TimePoint point);

    
}
