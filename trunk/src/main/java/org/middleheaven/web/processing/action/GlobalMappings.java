package org.middleheaven.web.processing.action;

class GlobalMappings {

	private GlobalMappings(){}
	
	private static String viewBase = "/WEB-INF/view";

	public static String getViewBase() {
		return viewBase;
	}

	public static void setViewBase(String viewBase) {
		GlobalMappings.viewBase = viewBase;
	}
}
