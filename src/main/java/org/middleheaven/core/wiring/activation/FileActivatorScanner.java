package org.middleheaven.core.wiring.activation;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.FileChangeListener;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.WatchableContainer;
import org.middleheaven.logging.Log;
import org.middleheaven.util.classification.BooleanClassifier;

public class FileActivatorScanner extends AbstractActivatorScanner {

	private final ManagedFile root;
	private final Set<Pattern> filePatterns = new HashSet<Pattern>();
	private final FileChangeListener fileChangeListener;
	private final Collection<ManagedFile> allFiles = new HashSet<ManagedFile>();
	private final Map<String, Activator> activators = new HashMap<String,Activator>();
	private WiringService wiringService;
	
	public FileActivatorScanner(ManagedFile file, String ... filePatterns){
		
		this.root = file;
		for (String pattern : filePatterns){
			this.filePatterns.add(Pattern.compile(pattern));
		}

		fileChangeListener= new FileChangeListener(){

			@Override
			public void onChange(FileChangeEvent event) {

				if(wiringService!=null){
					loadModuleFromFile(wiringService,event.getFile(), activators);
				}
			}

		};
	}

	@Override
	public void scan(WiringService wiringService) {

		this.wiringService = wiringService;
		
		if(root.getType().isFolder()){

			allFiles.addAll(root.children().findAll(new BooleanClassifier<ManagedFile>(){

				@Override
				public Boolean classify(ManagedFile file) {
					if (file.isReadable() && file.getType().isFile()){
						for (Pattern pattern : filePatterns){
							return pattern.matcher(file.getName()).find();
						}
					} 
					return Boolean.FALSE;
				}

			}));
		} else if(root.exists()){
			allFiles.add(root);
		}

		WatchableContainer wr = null;
		if (root.isWatchable()){
			wr = (WatchableContainer)root;
		}

		for (ManagedFile file : allFiles){
			loadModuleFromFile(wiringService,file , activators);
			if(wr != null){
				wr.addFileChangelistener(fileChangeListener, root);
			}
		}

	}

	private void loadModuleFromFile(WiringService wiringService,ManagedFile jar , Map<String,Activator> activators) {

		try{
			URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURL()});

			JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
			Manifest manifest = jis.getManifest();
			String className=null;
			if (manifest!=null){
				Attributes at = manifest.getMainAttributes();
				className = at.getValue("unit-activator");
			}

			if(className!=null && !className.isEmpty()){
				try{
					// TODO must instantiate ? 
					Class<? extends Activator> type = Introspector.of(Activator.class).load(className,cloader).getIntrospected();
					Activator activator = wiringService.getObjectPool().getInstance(type);

					Activator older = activators.get(activator.getClass().getName());
					if (older!=null){
						fireDeployableLost(older.getClass());
						activators.remove(activator.getClass().getName());
					}
					activators.put(activator.getClass().getName(),activator);
					fireDeployableFound(older.getClass());
				} catch (ClassCastException e){
					Log.onBookFor(this.getClass()).warn("{0} is not a valid application module activator",className);
				}
			}else {
				Log.onBookFor(this.getClass()).warn( "{0} does not present an application module.",jar.getName());
			}

		}catch (IOException e) {
			ManagedIOException.manage(e);
		}
	}


}
