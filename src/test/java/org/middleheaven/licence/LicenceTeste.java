package org.middleheaven.licence;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.licence.LicenceService;
import org.middleheaven.licence.LicenceServiceActivator;


public class LicenceTeste {

	@BeforeClass
	public static void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
		bootstrap.start();
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine();
		engine.addActivator(new LicenceServiceActivator());
		new ServiceContextConfigurator().addEngine(engine);
	}


	@Test(expected=SecurityException.class)
	public void testSecureService(){

		LicenceService ls = ServiceRegistry.getService(LicenceService.class);
		ServiceRegistry.register(LicenceService.class, ls);
		assertTrue(false);

	}

	
	@Test
	public void teste(){
		LicenceService ls = ServiceRegistry.getService(LicenceService.class);
		Licence licence = ls.getLicence("featureA", "1.0.0");

		assertTrue(licence instanceof CertificatedLicence);

		assertTrue(licence.isValid());
	}
	
}
