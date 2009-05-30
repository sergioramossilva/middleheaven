package org.middleheaven.work.scheduled;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;


public class AlarmClockScheduleWorkExecutionServiceActivator extends Activator {

	
	AlarmClockScheduleWorkExecutionService service;
	
	@Publish
	public ScheduleWorkExecutorService getScheduleWorkExecutorService(){
		return service;
	}
	
	
	@Override
	public void activate(ActivationContext context) {
		service = new AlarmClockScheduleWorkExecutionService();
		
	}

	@Override
	public void inactivate(ActivationContext context) {
		// no-op
	}
}
