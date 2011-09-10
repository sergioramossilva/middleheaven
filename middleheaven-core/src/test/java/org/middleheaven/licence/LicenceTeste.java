package org.middleheaven.licence;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.license.CertificatedLicense;
import org.middleheaven.license.License;
import org.middleheaven.license.LicenseException;
import org.middleheaven.license.LicenseService;
import org.middleheaven.license.LicenseServiceActivator;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


public class LicenceTeste extends MiddleHeavenTestCase{

	@Override
	protected void configurateActivators(SetActivatorScanner scanner) {
		scanner.addActivator(LicenseServiceActivator.class);
	}
	
	@Test(expected=SecurityException.class)
	public void testSecureService(){

		// The License Service is not changeable once defined
		LicenseService ls = ServiceRegistry.getService(LicenseService.class);
		ServiceRegistry.register(LicenseService.class, ls);

		fail("SecurityException not throwned");

	}

	
	@Test
	public void testLicenceRead(){
		LicenseService ls = ServiceRegistry.getService(LicenseService.class);
		License licence = ls.getLicence("featureA", "1.0.0");

		assertTrue(licence instanceof CertificatedLicense);

		assertTrue(licence.isValid());
	}
	
	@Test
	public void testLicenceControl(){
		

		License licence = ServiceRegistry.getService(LicenseService.class)
				.getLicence("featureA", "1.0.0");
		
		try{
			licence.checkOut();
		
			// do protected code
		
		} catch (LicenseException e){
			// no valid license exists
			fail("LicenseException throwed");
		} finally {
			licence.checkIn();
		}
	}
}
