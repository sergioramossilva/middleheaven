package org.middleheaven.application;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.core.wiring.annotations.Component;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.watch.FileChangeStrategy;
import org.middleheaven.io.repository.watch.FileWatchChannelProcessor;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.ParamsMap;
import org.middleheaven.util.collections.Walker;

/**
 * Provides an {@link ApplicationService} for loading application modules present at the application configuration path
 * Application Modules are jar files with extension .apm that contain a manifest file
 * with an {@code Application-Module} entry pointing to the {@link ModuleListener} class.
 * 
 * 
 */
@Component
public class DynamicLoadApplicationServiceActivator extends AbstractDynamicLoadApplicationServiceActivator  {



	public static void setAppModulesFilter(BooleanClassifier<ManagedFile> appModulesFilter) {
		DynamicLoadApplicationServiceActivator.appModulesFilter = appModulesFilter;
	}

	private static BooleanClassifier<ManagedFile> appModulesFilter =  new BooleanClassifier<ManagedFile>(){

		@Override
		public Boolean classify(ManagedFile file) {
			if (file.getPath().getFileName().endsWith(".jar")){
				JarInputStream jis;
				try {
					jis = new JarInputStream(file.getContent().getInputStream());
				} catch (ManagedIOException e) {
					return false;
				} catch (IOException e) {
					return false;
				}
				Manifest manifest = jis.getManifest();

				return manifest != null ;
			}
			return false;

		}
	};

	private FileWatchChannelProcessor fileWatchChannelProcessor;

	public DynamicLoadApplicationServiceActivator(){

	}


	public void inactivate(){
		if (fileWatchChannelProcessor != null){
			fileWatchChannelProcessor.close();
		}
	}

	protected void loadPresentModules(){

		final Collection<ManagedFile> applicationModuleFiles = new HashSet<ManagedFile>();

		ManagedFile f =   this.getFileContextService().getFileContext().getAppLibraryRepository();

		if (f.isWatchable()){
			WatchService ws = f.getRepository().getWatchService();

			WatchEventChannel channel = f.register(ws , StandardWatchEvent.ENTRY_CREATED, StandardWatchEvent.ENTRY_DELETED, StandardWatchEvent.ENTRY_MODIFIED);

			this.fileWatchChannelProcessor = new FileWatchChannelProcessor(new FileChangeStrategy() {

				@Override
				public void onChange(WatchEvent event) {
					if (StandardWatchEvent.ENTRY_CREATED.equals(event.kind())){
						ManagedFile file = event.getFile();
						if (appModulesFilter.classify(file)){
							setState(ApplicationCycleState.PAUSED);
							loadModuleFromFile(file);
							setState(ApplicationCycleState.READY);
						}
					}
				}
			});

			fileWatchChannelProcessor.add(channel);	

			fileWatchChannelProcessor.start();
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
			URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURI().toURL()}, Thread.currentThread().getContextClassLoader());
			
			JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
			Manifest manifest = jis.getManifest();

			if (manifest!=null){
				Attributes at = manifest.getMainAttributes();

				if (at.getValue("Application") != null){
					ParamsMap map = new ParamsMap()
					.setParam("Application", at.getValue("Application"))
					.setParam("Application-Listeners", at.getValue("Application-Listeners"))
					.setParam("Application-Modules", at.getValue("Application-Modules"))
					.setParam("Application-Module-Listeners", at.getValue("Application-Module-Listeners"))
					;

					parseAttributes(map, cloader);
				}
			}

		}catch (IOException e) {
			ManagedIOException.manage(e);
		} 

	}



}
