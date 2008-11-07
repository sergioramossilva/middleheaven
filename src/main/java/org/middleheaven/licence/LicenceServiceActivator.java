package org.middleheaven.licence;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

import javassist.util.proxy.MethodHandler;

import org.middleheaven.core.reflection.ProxyUtils;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceEvent;
import org.middleheaven.core.services.ServiceListener;
import org.middleheaven.core.services.ServiceEvent.ServiceEventType;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.crypto.CryptoUtils;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.service.FileRepositoryService;


public class LicenceServiceActivator extends ServiceActivator {

	private LicenceService implementation;
	private LicenceProvider provider = new VoidLicenceProvider();
	
	@Override
	public void inactivate(ServiceContext context) {
		implementation = null;
	}
	
	@Override
	public void activate(ServiceContext context) {
	
		FileRepositoryService frs = context.getService(FileRepositoryService.class, null);


		ManagedFile f = frs.getRepository("ENV_CONFIGURATION");
		Collection<ManagedFile> licences = new HashSet<ManagedFile>();
		licences.addAll( f.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith(".lic");
			}

		}));
		f = frs.getRepository("APP_CONFIGURATION");
		licences.addAll( f.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith(".lic");
			}

		}));

		Collection<ManagedFile> certifcates = new HashSet<ManagedFile>();
		certifcates.addAll( f.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith(".cert");
			}

		}));

		if (licences.isEmpty() || certifcates.isEmpty()){
			provider = new VoidLicenceProvider();
		} else {
			provider = load( new ComposedProvider() , licences ,certifcates.iterator().next() );
		}

		implementation = new LicenceServiceImpl();

		context.register(LicenceService.class, implementation, null);
		context.addServiceListener(new ServiceLock());
	}

	private class ServiceLock implements ServiceListener{

		@Override
		public void onEvent(ServiceEvent event) {
			if (event.getType().equals(ServiceEventType.ADDED) && event.getServiceClass().getName().equals(LicenceService.class.getName())){
				throw new SecurityException("LicenceService is locked");
			}
		}
		
	}
	
	private ComposedProvider load(ComposedProvider providers ,Collection<ManagedFile> licences , ManagedFile certificate ){
		AddocClassLoader cloader = new AddocClassLoader();
		
		for (ManagedFile f : licences){
			// open file and deencript it with key

			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(f.getContent().getInputStream()));
				StringBuilder buffer = new StringBuilder();
				String line;
				while ((line = reader.readLine())!=null){
					buffer.append(line);
				}
				
				String classDef = buffer.substring(buffer.indexOf("<class>")+ "<class>".length(), buffer.indexOf("</class>"));
				
				ByteArrayInputStream in = new ByteArrayInputStream(CryptoUtils.decodeBase64(classDef.getBytes()));
				
				ByteArrayOutputStream classDefStream = new ByteArrayOutputStream();

				ClassDefinition cd  = new ClassDefinition();
				String className = cd.read(in, classDefStream);
	
				cloader.addClassData(className, classDefStream.toByteArray());
				
				Object obj = cloader.loadClass(className).newInstance();
				
				SerializableLicenceProvider p = ProxyUtils.proxy(SerializableLicenceProvider.class, LicenceProviderHandler.wrapp(obj));
				
				String data = buffer.substring(buffer.indexOf("<data>")+ "<data>".length(), buffer.indexOf("</data>"));
				
				p.setAttributes(data);
				
				providers.addProvider(p);
				
			} catch (ManagedIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// read provider class

			// read properties data

			// add reader provider to providers

		}
		return providers;
	}

	private static class LicenceProviderHandler implements MethodHandler{

		Object provider;
		Method getLicenceMethod;
		Method setAttributesMethod;

		public static LicenceProviderHandler wrapp(Object provider) throws SecurityException, NoSuchMethodException{
			LicenceProviderHandler me =  new LicenceProviderHandler();
			
			me.provider =provider;
			me.getLicenceMethod = provider.getClass().getMethod("getLicence", String.class,String.class);
			me.setAttributesMethod = provider.getClass().getMethod("setAttributes", String.class);

			return me;
		}
		
		private LicenceProviderHandler(){
		}
		
		@Override
		public Object invoke(Object obj, Method proxy, Method original,Object[] args) throws Throwable {
			if (proxy.getName().equals(getLicenceMethod.getName())){
				try{
				Object objx=  getLicenceMethod.invoke(provider, args);
				
				return objx;
				} catch (Throwable t){
					return null;
				}
			} else if (proxy.getName().equals(setAttributesMethod.getName())){
				return setAttributesMethod.invoke(provider, args);
			} else {
				return null;
			}
		}
		
	}
	

	private class LicenceServiceImpl implements LicenceService{


		public LicenceServiceImpl(){


		}

		@Override
		public Licence getLicence(String featureID, String version) {
			return 	provider.getLicence(featureID, version);
		}

	}



}
