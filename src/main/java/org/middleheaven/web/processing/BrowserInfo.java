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
	
	public static BrowserInfo unkownBrowser(){
		return new BrowserInfo("","",Version.from(0, 0, 0));
	}
	
	public static BrowserInfo browser(String name, String baseEngine, Version version){
		return new BrowserInfo(name,baseEngine,version);
	}
	
	private BrowserInfo(String name, String baseEngine, Version version) {
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
