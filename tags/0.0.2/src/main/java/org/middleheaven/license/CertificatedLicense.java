package org.middleheaven.license;


public class CertificatedLicense extends AbstractLicense {


	public CertificatedLicense(String feature){
		super(feature);
	}
	
	@Override
	public void checkIn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isValid() {
		return true;
	}
	
}
