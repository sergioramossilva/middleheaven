package org.middleheaven.io.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.middleheaven.io.ManagedIOException;

public class NetUtils {

	
	public static InetAddress getPrimaryAddress(){
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw ManagedIOException.manage(e);
		}
	}
}
