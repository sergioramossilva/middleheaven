
package org.middleheaven.sequence.service;

import java.io.IOException;
import java.util.Properties;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.StateChangedEvent;
import org.middleheaven.sequence.StatePersistentSequence;
import org.middleheaven.util.conversion.TypeConvertions;

/**
 * Activates  <code>SequenceStorageService</code> for sequence storing. 
 * This implementation stores all sequences in a local properties file named <code>sequences.properties</code>
 * 
 */
@Service
public class FileSequenceStorageActivator extends Activator  {

	private Properties properties = new Properties();
	private ManagedFile file;
	private BootstrapService bootstrapService;
	private SequenceStorageService sequenceStorageService;

	
	@Wire
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}

	@Publish({"type=file", "remote=no"})
	public SequenceStorageService getSequenceStorageService() {
		return sequenceStorageService;
	}
	
	@Override
	public void activate(ActivationContext context) {

		BootstrapContainer container = bootstrapService.getEnvironmentBootstrap().getContainer();

		if (!container.getAppDataRepository().isWriteable()){
			throw new IllegalArgumentException("Data repository must be writable");
		}
		ManagedFile file = container.getAppDataRepository().resolveFile("sequences.properties");
		if (file.exists()){
			try {
				properties.load(file.getContent().getInputStream());
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}
		}

		sequenceStorageService = new FileSequenceStorageService();
	}



	@Override
	public void inactivate(ActivationContext context) {
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
			Object value = TypeConvertions.convert(
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





