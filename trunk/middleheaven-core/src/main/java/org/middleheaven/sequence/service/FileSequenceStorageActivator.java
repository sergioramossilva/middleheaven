
package org.middleheaven.sequence.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileRepositoryService;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.StateChangedEvent;
import org.middleheaven.sequence.StatePersistentSequence;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.collections.ParamsMap;

/**
 * Activates  <code>SequenceStorageService</code> for sequence storing. 
 * This implementation stores all sequences in a local properties file named <code>sequences.properties</code>
 * 
 */
@Service
public class FileSequenceStorageActivator extends ServiceActivator  {

	private Properties properties = new Properties();
	private ManagedFile file;

	private SequenceStorageService sequenceStorageService;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(BootstrapContainer.class));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(SequenceStorageService.class));
	}
	
	@Override
	public void activate(ServiceContext serviceContext) {

		BootstrapContainer container = serviceContext.getService(BootstrapService.class).getEnvironmentBootstrap().getContainer();

		final ManagedFile appDataRepository = container.getFileSystem().getAppDataRepository();
		if (!appDataRepository.isWriteable()){
			throw new IllegalArgumentException("Data repository must be writable");
		}
		
		file = appDataRepository.retrive("sequences.properties");
		if (file.exists()){
			try {
				properties.load(file.getContent().getInputStream());
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}
		}

		sequenceStorageService = new FileSequenceStorageService();
		
		final ParamsMap params = new ParamsMap().setParam("type", "file").setParam("remote", "no");
		
		serviceContext.register(SequenceStorageService.class, sequenceStorageService , params);
	}
	
	



	@Override
	public void inactivate(ServiceContext serviceContext) {
		// no-op
	}

	private class FileSequenceStorageService implements SequenceStorageService {


		public synchronized void store(SequenceState state) {
		
			properties.setProperty(state.getName(), state.getLastUsedValue().toString());
			try {
				properties.store(file.getContent().getOutputStream(), "");
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}
		}

		@Override
		public void restore(StatePersistentSequence<?> sequence) {
			Class<?> valueType = sequence.getSequenceState().getLastUsedValue().getClass();
			Object value = TypeCoercing.coerce(
					properties.getProperty(sequence.getName()), 
					valueType
			);
			SequenceState state = new SequenceState(sequence.getName(),value);

			sequence.setSequenceState(state);
		}

		@Override
		public void onStateChanged(StateChangedEvent event) {
			 this.store(event.getSequenceState());
		}

	}



}





