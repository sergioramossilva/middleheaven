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

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.wiring.WiringContext;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileChangeEvent;
import org.middleheaven.io.repository.FileChangeListener;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.WatchableContainer;
import org.middleheaven.logging.Logging;
import org.middleheaven.util.ArrayMapKey;

public class FileDeployableScanner extends AbstractDeployableScanner {

	private final ManagedFile root;
	private final Set<Pattern> filePatterns = new HashSet<Pattern>();
	private final FileChangeListener fileChangeListener;
	private final Collection<ManagedFile> allFiles = new HashSet<ManagedFile>();
	private final Map<String, UnitActivator> activators = new HashMap<String,UnitActivator>();
	private WiringContext context;
	
	public FileDeployableScanner(ManagedFile file, String ... filePatterns){
		if(file.getType().isFolder() && !file.isWatchable()){
			throw new IllegalArgumentException("Can only scan watchable folders or single files");
		}
		this.root = file;
		for (String pattern : filePatterns){
			this.filePatterns.add(Pattern.compile(pattern));
		}

		fileChangeListener= new FileChangeListener(){

			@Override
			public void onChange(FileChangeEvent event) {

				if(context!=null){
					loadModuleFromFile(context,event.getFile(), activators);
				}
			}

		};
	}

	@Override
	public void scan(WiringContext context) {

		this.context = context;
		
		if(root.getType().isFolder()){
			allFiles.addAll( root.listFiles(new ManagedFileFilter(){

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
		} else {
			allFiles.add(root);
		}

		WatchableContainer wr = null;
		if (root.isWatchable()){
			wr = (WatchableContainer)root;
		}

		for (ManagedFile file : allFiles){
			loadModuleFromFile(context,file , activators);
			if(wr!=null){
				wr.addFileChangelistener(fileChangeListener, root);
			}
		}

	}

	private void loadModuleFromFile(WiringContext context,ManagedFile jar , Map<String,UnitActivator> activators) {

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
					// TODO must instanciate ? 
					Class<? extends UnitActivator> type = ReflectionUtils.loadClass(className , UnitActivator.class,cloader);
					UnitActivator activator = context.getInstance(type);

					UnitActivator older = activators.get(new ArrayMapKey(jar.getName() ,activator.getClass().getName()));
					if (older!=null){
						fireDeployableLost(older.getClass());
						activators.remove(activator.getClass().getName());
					}
					activators.put(activator.getClass().getName(),activator);
					fireDeployableFound(older.getClass());
				} catch (ClassCastException e){
					Logging.getBook(this.getClass()).warn(className + " is not a valid application module activator");
				}
			}else {
				Logging.getBook(this.getClass()).warn(jar.getName() + " does not present an application module.");
			}

		}catch (IOException e) {
			ManagedIOException.manage(e);
		}
	}


}
