package org.middleheaven.application;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.FileChangeListener;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.WatchableContainer;
import org.middleheaven.util.classification.BooleanClassifier;

/**
 * Provides an {@link ApplicationLoadingService} for loading application modules present at the application configuration path
 * Application Modules are jar files with extension .apm that contain a manifest file
 * with an {@code Application-Module} entry pointing to the {@link ApplicationModule} class.
 * 
 * 
 */
public class DynamicLoadApplicationServiceActivator extends AbstractDynamicLoadApplicationServiceActivator implements FileChangeListener  {

	public static void setAppModulesFilter(BooleanClassifier<ManagedFile> appModulesFilter) {
		DynamicLoadApplicationServiceActivator.appModulesFilter = appModulesFilter;
	}

	private static BooleanClassifier<ManagedFile> appModulesFilter =  new BooleanClassifier<ManagedFile>(){

		@Override
		public Boolean classify(ManagedFile file) {
			return file.getPath().getBaseName().endsWith(".apm");
		}
	};
	
	public DynamicLoadApplicationServiceActivator(){}
	
	@Override
	public void onChange(FileChangeEvent event) {
		ManagedFile file = event.getFile();
		if (appModulesFilter.classify(file)){
			getCycle().setState(ApplicationCycleState.PAUSED);
			loadModuleFromFile(file);
			getCycle().setState(ApplicationCycleState.READY);
		}
	}
	
	protected void loadPresentModules(){

		// look for all files ending in .apm (Application Module)
		// these must be jar files
		Collection<ManagedFile> applicationModuleFiles = new HashSet<ManagedFile>();

		ManagedFile f =  getBootstrapService().getEnvironmentBootstrap().getContainer().getFileSystem().getAppConfigRepository();

		if (f.isWatchable()){
			WatchableContainer wr = (WatchableContainer)f;
			wr.addFileChangelistener(this, f);
		}

		// filter apm only (apm are jar files)
		applicationModuleFiles.addAll( f.children().findAll(appModulesFilter));

		for (ManagedFile jar : applicationModuleFiles){
			loadModuleFromFile(jar);
		}

	}

	private void loadModuleFromFile(ManagedFile jar) {

		try{
			URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURL()});

			JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
			Manifest manifest = jis.getManifest();
			String className=null;
			
			if (manifest!=null){
				Attributes at = manifest.getMainAttributes();
				className = at.getValue("Application-Module");
			}

			if(className!=null && !className.isEmpty()){
				TransientApplicationContext appContext = getAppContext();
				try{
					ApplicationModule module = Introspector.of(ApplicationModule.class)
													.load(className,cloader).newInstance();

					appContext.addModule(module);
				} catch (ClassCastException e){
					getLog().warn("{0} is not a valid application module activator",className);
				}
			}else {
				getLog().warn("{0} does not present an application module.",jar.getPath().getBaseName());
			}

		}catch (IOException e) {
			ManagedIOException.manage(e);
		} 

	}


}
