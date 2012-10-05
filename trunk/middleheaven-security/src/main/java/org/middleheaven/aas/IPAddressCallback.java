package org.middleheaven.aas;

import java.net.InetAddress;

public class IPAddressCallback implements Callback {


	private static final long serialVersionUID = 4050559024059022255L;
	
	private InetAddress address;

	@Override
	public boolean isBlank() {
		return address==null;
	}
	
	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}
}
