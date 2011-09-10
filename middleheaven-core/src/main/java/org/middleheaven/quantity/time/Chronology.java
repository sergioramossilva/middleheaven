package org.middleheaven.quantity.time;

import org.middleheaven.core.exception.UnimplementedMethodException;
import org.middleheaven.quantity.time.clocks.Clock;
import org.middleheaven.quantity.time.clocks.MachineClock;



public abstract class Chronology {

	protected Clock referenceClock = new MachineClock();
	public void setClock(Clock referenceClock){
		this.referenceClock = referenceClock;
	}
	
	public abstract long milisecondsFor(boolean lenient,int year, int month, int day );
	
	public <T extends TimePoint> T add(T point , ElapsedTime elapsed){
		if (elapsed instanceof Duration){
			return add(point , (Duration)elapsed);
		} else {
			return add(point , (Period)elapsed);
		}	
    }

	public abstract <T extends TimePoint> T add(T point , Duration eplased);
	
	public abstract <T extends TimePoint> T add(T point , Period eplased);
	
    public abstract DayOfMonth monthDay(TimePoint point);
    
    public abstract DayOfWeek weekDay(TimePoint point);
    
    public abstract Month monthOf(TimePoint point);
    
    public abstract Year yearOf(TimePoint point);

    public abstract int yearDay(TimePoint point);
    
	public abstract Month monthOf(int ordinal, int month);

	public abstract TimeHolder timeOf(TimePoint point);

	public CalendarDate convertTo(CalendarDate date, Chronology chronology){
		
		// TODO 
		throw new UnimplementedMethodException("Conversion between chonologies is not yet supported");
	}

	

    
}
