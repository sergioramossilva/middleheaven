package org.middleheaven.quantity.time;

import java.io.Serializable;

class AbstractTimePoint implements TimePoint, Serializable {

	private static final long serialVersionUID = -5640403398323384453L;
	

	protected final long milliseconds;

	protected final TimeContext context;
    
    protected AbstractTimePoint (TimeContext context, long timeFromEpoc){
        this.milliseconds = timeFromEpoc;
        this.context = context;
    }
    
    
	@Override
	public long getMilliseconds() {
		return milliseconds;
	}

	@Override
	public TimeInterval until(TimePoint other) {
		 return new TimeInterval(this,other);
	}

	public int compareTo(TimePoint other) {
		if (other==null){
			throw new ClassCastException("Null is not an instance of " + this.getClass());
		}
		
		if (this.milliseconds > other.getMilliseconds()){
			return 1;
		} else if (this.milliseconds < other.getMilliseconds()) {
			return -1;
		} else {
			return 0;
		}

	}

}
