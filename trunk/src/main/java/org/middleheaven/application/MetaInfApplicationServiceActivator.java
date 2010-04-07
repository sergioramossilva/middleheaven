package org.middleheaven.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;

/**
 * Provides an {@link ApplicationLoadingService} for loading application modules present at the application configuration path
 * Searches for a  a manifest file
 * with an {@code Application-Module} entry pointing to the {@link ApplicationModule} class.
 * 
 * 
 */
public class MetaInfApplicationServiceActivator extends AbstractDynamicLoadApplicationServiceActivator implements BootstapListener  {


	public MetaInfApplicationServiceActivator(){}

	protected void loadPresentModules() {

		ManagedFile f =  getBootstrapService().getEnvironmentBootstrap().getContainer().getFileSystem().getAppConfigRepository();

		ManagedFile manifest = f.retrive("MANIFEST.MF");

		if (manifest.exists()){
			BufferedReader reader = new BufferedReader(new InputStreamReader(manifest.getContent().getInputStream()));
			String line;
			try {
				while (((line = reader.readLine())!=null)){
					if (line.startsWith("Application-Module")){

						String className = line.substring(line.indexOf(":")+1).trim();
						if(className!=null && !className.isEmpty()){
							try{
								ApplicationModule module = Introspector.of(ApplicationModule.class)
																.load(className).newInstance();
							
								getAppContext().addModule(module);
							} catch (ClassCastException e){
								getLog().warn("{0} is not a valid application module activator",className);
							}
						}
						break;
					}
				}
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}
		}

	}

}
