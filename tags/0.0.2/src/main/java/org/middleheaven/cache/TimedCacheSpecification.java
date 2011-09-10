package org.middleheaven.cache;

import org.middleheaven.quantity.time.Period;

public class TimedCacheSpecification extends CacheSpecification {

	
	public static TimedCacheSpecification purgeEvery(Period period){
		TimedCacheSpecification spec = new TimedCacheSpecification();
		spec.setPurgeIntervalSeconds((int)period.seconds());
		return spec;
	}
	
	protected TimedCacheSpecification(){}
}
