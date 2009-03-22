package org.middleheaven.web.processing;

import org.middleheaven.util.Version;

public class BrowserInfo {

	public static enum CommonBrowsers {
		UNKOWN,
		MSIE,
		FIREFOX,
		CROME,
		OPERA,
		NAVIGATOR,
		SAFARI
		;
	}
	
	String name;
	String baseEngine;
	Version version;
	

	public BrowserInfo(String name, String baseEngine, Version version) {
		super();
		this.name = name;
		this.baseEngine = baseEngine;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getBaseEngine() {
		return baseEngine;
	}

	public Version getVersion() {
		return version;
	}

	public boolean is(CommonBrowsers browser){
		return this.name.equalsIgnoreCase(browser.name());
	}
	
	public boolean isMSIE(){
		return is(CommonBrowsers.MSIE);
	}
	
	public boolean isFirefox(){
		return is(CommonBrowsers.FIREFOX);
	}
}
