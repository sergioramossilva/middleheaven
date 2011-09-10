package org.middleheaven.process.web;

import org.middleheaven.util.OperatingSystemInfo;


/**
 * Represents an HTTP user agent.
 * 
 * Commonly an user agent is a Browser, however it can be an application. In this case it's called a (ro)bot   
 *
 */
public final class HttpUserAgent implements UserAgent{

	private BrowserInfo browserInfo;
	private OperatingSystemInfo osInfo;


	public HttpUserAgent(BrowserInfo browserInfo, OperatingSystemInfo osInfo){
		this.browserInfo = browserInfo;
		this.osInfo = osInfo;
	}

	public BrowserInfo getBrowserInfo(){
		return this.browserInfo;
	}

	@Override
	public OperatingSystemInfo getOperatingSystemInfo() {
		return this.osInfo;
	}
}


