package org.middleheaven.web.processing;

import org.middleheaven.aas.UserAgent;
import org.middleheaven.util.OperatingSystemInfo;


/**
 * Represents a Browser based user agent. 
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


