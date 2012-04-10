package org.middleheaven.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.middleheaven.core.wiring.annotations.Component;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.ParamsMap;

/**
 * Provides an {@link ApplicationService} for loading application modules present at the application configuration path
 * Searches for a  a manifest file
 * with an {@code Application-Module} entry pointing to the {@link ModuleListener} class.
 * 
 * 
 */
@Component
public class MetaInfApplicationServiceActivator extends AbstractDynamicLoadApplicationServiceActivator   {


	public MetaInfApplicationServiceActivator(){}


	protected void loadPresentModules() {

		ManagedFile f =  getBootstrapService().getEnvironmentBootstrap().getContainer().getFileSystem().getAppConfigRepository();

		ManagedFile manifest = f.retrive("MANIFEST.MF");

		if (manifest.exists()){
			BufferedReader reader = new BufferedReader(new InputStreamReader(manifest.getContent().getInputStream()));
			String line;
			try {
				
				ParamsMap params = new ParamsMap();
				
				while (((line = reader.readLine())!=null)){
					
					String[] split = StringUtils.split(line, ':');
					
					if ( split.length == 2){
						params.setParam(split[0], split[1]);
					}

				}
				
				parseAttributes(params, this.getClass().getClassLoader());
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}
		}

	}

}
