package org.middleheaven.license;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.ServiceEvent;
import org.middleheaven.core.services.ServiceListener;
import org.middleheaven.core.services.ServiceEvent.ServiceEventType;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.crypto.CryptoUtils;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.service.FileRepositoryService;
import org.middleheaven.logging.LogBook;
import org.middleheaven.logging.Logging;


public class LicenceServiceActivator extends ServiceActivator {

	private LicenseService implementation;
	private LicenceProvider provider = new VoidLicenceProvider();
	private FileRepositoryService frs;

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		implementation = null;
	}

	@Publish
	public LicenseService getLicenceService(){
		return implementation;
	}

	@Require
	public void setFileRepositoryService(FileRepositoryService fileService){
		this.frs = fileService;
	}

	@Override
	public void activate(ServiceAtivatorContext context) {

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

		context.addServiceListener(new ServiceLock());
	}

	private class ServiceLock implements ServiceListener{

		private int alreadySet = 0; 
		@Override
		public void onEvent(ServiceEvent event) {
			if (event.getServiceClass().getName().equals(LicenseService.class.getName())){
				if (event.getType().equals(ServiceEventType.ADDED)){
					if (alreadySet>0){
						throw new SecurityException("LicenceService is locked");
					}

					alreadySet++;
				} else {
					alreadySet--;
				}
			}

		}

	}



	private ComposedProvider load(ComposedProvider providers ,Collection<ManagedFile> licences , ManagedFile certificate ){
		AddocClassLoader cloader = new AddocClassLoader();

		LogBook logBook = Logging.getBook(this.getClass());

		for (ManagedFile f : licences){
			// open file and decipher it with key

			String className = null;
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
				className = cd.read(in, classDefStream);

				cloader.addClassData(className, classDefStream.toByteArray());

				Object obj = cloader.loadClass(className).newInstance();

				SerializableLicenceProvider p = ReflectionUtils.proxy(SerializableLicenceProvider.class, LicenceProviderHandler.wrapp(obj));

				String data = buffer.substring(buffer.indexOf("<data>")+ "<data>".length(), buffer.indexOf("</data>"));

				p.setAttributes(data);

				providers.addProvider(p);


			} catch (Throwable e) {
				logBook.error("Licence " + f.getURL() + " could not be loaded", e);
			} 


			// read provider class

			// read properties data

			// add reader provider to providers

		}
		return providers;
	}

	private static class LicenceProviderHandler implements ProxyHandler{

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
		public Object invoke(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable {
			if (delegator.getName().equals(getLicenceMethod.getName())){
				try{
					Object objx=  getLicenceMethod.invoke(provider, args);

					return objx;
				} catch (Throwable t){
					return null;
				}
			} else if (delegator.getName().equals(setAttributesMethod.getName())){
				return setAttributesMethod.invoke(provider, args);
			} else {
				return null;
			}
		}

	}


	private class LicenceServiceImpl implements LicenseService{


		public LicenceServiceImpl(){


		}

		@Override
		public License getLicence(String featureID, String version) {
			return 	provider.getLicence(featureID, version);
		}

	}



}
