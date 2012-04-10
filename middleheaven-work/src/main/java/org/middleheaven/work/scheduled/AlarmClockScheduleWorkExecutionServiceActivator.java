package org.middleheaven.work.scheduled;

import java.util.Collection;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;


public class AlarmClockScheduleWorkExecutionServiceActivator extends ServiceActivator {

	

	public AlarmClockScheduleWorkExecutionServiceActivator(){}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//no-dependencies
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(AlarmClockScheduleWorkExecutionService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		serviceContext.register(AlarmClockScheduleWorkExecutionService.class, new AlarmClockScheduleWorkExecutionService());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(AlarmClockScheduleWorkExecutionService.class);
	}
}
