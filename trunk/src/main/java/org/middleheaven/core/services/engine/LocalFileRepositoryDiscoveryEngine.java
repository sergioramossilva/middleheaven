package org.middleheaven.core.services.engine;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceDiscoveryEngine;
import org.middleheaven.core.services.ServiceDiscoveryException;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.service.FileRepositoryService;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.LoggingService;

public class LocalFileRepositoryDiscoveryEngine implements ServiceDiscoveryEngine {

	private List<ServiceActivator> activators = new LinkedList<ServiceActivator>();
	private LogBook log;
	
	@Override
	public void init(ServiceContext context) {
	    log = context.getService(LoggingService.class, null).getLogBook(this.getClass().getName());
		
		loadActivators(context);
		
		boolean error = false;
		for (ServiceActivator activator : activators){
			try {
				activator.activate(context);
			} catch (Exception e){
				error = true;
				log.logFatal("Impossible to activate " + activator.getClass().getName(), e);
			}
		}
		if (error==true){
			throw new ServiceDiscoveryException("Impossible to iniciate all activators. Discovery fails");
		}
	}
	
	@Override
	public void stop(ServiceContext context) {
		for (ServiceActivator activator : activators){
			try {
				activator.inactivate(context);
			} catch (Exception e){
				log.logFatal("Impossible to  inactivate " + activator.getClass().getName(), e);
			}
		}
	}

	
	protected void loadActivators(ServiceContext context){
		FileRepositoryService frs = context.getService(FileRepositoryService.class, null);

		
		ManagedFile f = frs.getRepository("ENV_CONFIGURATION");
		
		Collection<ManagedFile> serviceJars = new HashSet<ManagedFile>();
		serviceJars.addAll( f.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith(".srv");
			}

		}));
		f = frs.getRepository("APP_CONFIGURATION");

		serviceJars.addAll( f.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith(".srv");
			}

		}));

		
		URLClassLoader cloader ;
		for (ManagedFile jar : serviceJars){
			try {
				try{
				    cloader = URLClassLoader.newInstance(new URL[]{jar.getURL()});
					
					JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
					Manifest manifest = jis.getManifest();
					Attributes at = manifest.getMainAttributes();
					
					String className = at.getValue("MiddleHeaven-Activator");
					
					if(className!=null && !className.isEmpty()){
						addActivator((ServiceActivator) cloader.loadClass(className).newInstance());
					}

				}catch (IOException e) {
					ManagedIOException.manage(e);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ManagedIOException e) {
				// TODO log and continue
				e.printStackTrace();
			} 
		}
	}
	

	public void addActivator(ServiceActivator activator){
		activators.add(activator);
	}
	

}
