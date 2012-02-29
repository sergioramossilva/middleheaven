package org.middleheaven.work.scheduled;

import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;


public class AlarmClockScheduleWorkExecutionServiceActivator extends Activator {

	
	AlarmClockScheduleWorkExecutionService service;
	
	public AlarmClockScheduleWorkExecutionServiceActivator(){}
	
	@Publish
	public ScheduleWorkExecutorService getScheduleWorkExecutorService(){
		return service;
	}
	
	
	@Override
	public void activate() {
		service = new AlarmClockScheduleWorkExecutionService();
		
	}

	@Override
	public void inactivate() {
		// no-op
	}
}
