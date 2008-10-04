
package org.middleheaven.util.sequence.service;

import java.io.IOException;
import java.util.Properties;

import org.middleheaven.core.Container;
import org.middleheaven.core.services.ContainerService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.engine.ServiceActivator;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.util.ParamsMap;
import org.middleheaven.util.conversion.TypeConvertions;
import org.middleheaven.util.sequence.SequenceState;
import org.middleheaven.util.sequence.StateChangedEvent;
import org.middleheaven.util.sequence.StatePersistentSequence;

/**
 * Activates  <code>SequenceStorageService</code> for sequence storing. 
 * This implementation stores all sequences in a local properties file named <code>sequences.properties</code>
 * 
 * @author  Sergio M. M. Taborda
 */
public class FileSequenceStorageActivator extends ServiceActivator  {

	private Properties properties = new Properties();
	private ManagedFile file;

	@Override
	public void activate(ServiceContext context) {

		Container container = context.getService(ContainerService.class, null).getContainer();

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

		context.register(
				SequenceStorageService.class, 
				new FileSequenceStorageService(), 
				new ParamsMap()
				.setParam("type", "file")
				.setParam("remote", "no")
		);
	}

	@Override
	public void inactivate(ServiceContext context) {
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





