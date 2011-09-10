package org.middleheaven.web.processing;

import java.util.List;

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
	boolean bot;
	private boolean isUnkown;
	private List<Culture> acceptableCultures;
	
	public static BrowserInfo unkownBrowser(List<Culture> acceptableCultures){
		return new BrowserInfo(acceptableCultures,"","",Version.unknown(),false, true);
	}
	
	/**
	 * 
	 * @param acceptableCultures acceptable display cultures in preference order
	 * @param name
	 * @param baseEngine
	 * @param version
	 * @return
	 */
	public static BrowserInfo browser(List<Culture> acceptableCultures, String name, String baseEngine, Version version){
		return new BrowserInfo(acceptableCultures,name,baseEngine,version, false, false);
	}
	
	private BrowserInfo(List<Culture> acceptableCultures , String name, String baseEngine, Version version, boolean isBot, boolean isUnkown) {
		super();
		this.name = name;
		this.baseEngine = baseEngine;
		this.version = version;
		this.acceptableCultures = acceptableCultures;
		this.bot = isBot;
		this.isUnkown =isUnkown;
	}

	public boolean isUnkown(){
		return isUnkown;
	}
	
	public boolean isBot(){
		return bot;
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
	
	public List<Culture> getCultures(){
		return this.acceptableCultures;
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
