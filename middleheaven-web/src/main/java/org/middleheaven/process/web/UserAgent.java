package org.middleheaven.process.web;

import org.middleheaven.util.OperatingSystemInfo;

public interface UserAgent {

	
	public OperatingSystemInfo getOperatingSystemInfo();
	public BrowserInfo getBrowserInfo();
}
