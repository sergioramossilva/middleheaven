package org.middleheaven.application;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.watch.FileChangeStrategy;
import org.middleheaven.io.repository.watch.FileWatchChannelProcessor;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.Walker;

/**
 * Provides an {@link ApplicationLoadingService} for loading application modules present at the application configuration path
 * Application Modules are jar files with extension .apm that contain a manifest file
 * with an {@code Application-Module} entry pointing to the {@link ApplicationModule} class.
 * 
 * 
 */
public class DynamicLoadApplicationServiceActivator extends AbstractDynamicLoadApplicationServiceActivator  {

	
	
	public static void setAppModulesFilter(BooleanClassifier<ManagedFile> appModulesFilter) {
		DynamicLoadApplicationServiceActivator.appModulesFilter = appModulesFilter;
	}

	private static BooleanClassifier<ManagedFile> appModulesFilter =  new BooleanClassifier<ManagedFile>(){

		@Override
		public Boolean classify(ManagedFile file) {
			return file.getPath().getFileNameWithoutExtension().endsWith(".apm");
		}
	};
	
	private FileWatchChannelProcessor fileWatchChannelProcessor;
	
	public DynamicLoadApplicationServiceActivator(){
		
		this.fileWatchChannelProcessor = new FileWatchChannelProcessor(new FileChangeStrategy() {
			
			@Override
			public void onChange(WatchEvent event) {
				if (StandardWatchEvent.ENTRY_CREATED.equals(event.kind())){
					ManagedFile file = event.getFile();
					if (appModulesFilter.classify(file)){
						getCycle().setState(ApplicationCycleState.PAUSED);
						loadModuleFromFile(file);
						getCycle().setState(ApplicationCycleState.READY);
					}
				}
			}
		});
	}
	
	protected void loadPresentModules(){

		// look for all files ending in .apm (Application Module)
		// these can be jar files
		final Collection<ManagedFile> applicationModuleFiles = new HashSet<ManagedFile>();

		ManagedFile f =  getBootstrapService().getEnvironmentBootstrap().getContainer().getFileSystem().getAppConfigRepository();

		if (f.isWatchable()){
			WatchService ws = f.getRepository().getWatchService();
			
			WatchEventChannel channel = f.register(ws , StandardWatchEvent.ENTRY_CREATED, StandardWatchEvent.ENTRY_DELETED, StandardWatchEvent.ENTRY_MODIFIED);
			fileWatchChannelProcessor.add(channel);		
		}

		f.each(new Walker<ManagedFile>(){

			@Override
			public void doWith(ManagedFile file) {
				if (appModulesFilter.classify(file)){
					applicationModuleFiles.add(file);
				}
			}
			
		});
		
		for (ManagedFile jar : applicationModuleFiles){
			loadModuleFromFile(jar);
		}

	}

	private void loadModuleFromFile(ManagedFile jar) {

		try{
			URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURI().toURL()});

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
				getLog().warn("{0} does not present an application module.",jar.getPath().getFileNameWithoutExtension());
			}

		}catch (IOException e) {
			ManagedIOException.manage(e);
		} 

	}


}
