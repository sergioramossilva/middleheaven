package org.middleheaven.web.processing;

import org.middleheaven.global.Culture;
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
	private Culture culture;
	
	public static BrowserInfo unkownBrowser(Culture culture){
		return new BrowserInfo(culture,"","",Version.from(0, 0, 0));
	}
	
	public static BrowserInfo browser(Culture culture, String name, String baseEngine, Version version){
		return new BrowserInfo(culture,name,baseEngine,version);
	}
	
	private BrowserInfo(Culture culture , String name, String baseEngine, Version version) {
		super();
		this.name = name;
		this.baseEngine = baseEngine;
		this.version = version;
		this.culture = culture;
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
	
	public Culture getCulture(){
		return this.culture;
	}

	public boolean isUnkown(){
		return this.name.isEmpty();	
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
