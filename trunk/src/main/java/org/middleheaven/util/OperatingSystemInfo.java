package org.middleheaven.util;

public final class OperatingSystemInfo {

	private String operatingSystem;
	private String operatingSystemVersion;
	private String operatingSystemVersionBase;
	
	public static OperatingSystemInfo unkown() {
		return new OperatingSystemInfo("","","");
	}
	
	public static OperatingSystemInfo system(String operatingSystem, String operatingSystemVersion, String operatingSystemVersionBase) {
		return new OperatingSystemInfo(operatingSystem,operatingSystemVersion,operatingSystemVersionBase);
	}
	
	
	private OperatingSystemInfo(String operatingSystem, String operatingSystemVersion, String operatingSystemVersionBase) {
		this.operatingSystem = operatingSystem;
		this.operatingSystemVersion = operatingSystemVersion;
		this.operatingSystemVersionBase = operatingSystemVersionBase;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public String getOperatingSystemVersion() {
		return operatingSystemVersion;
	}

	public String getOperatingSystemVersionBase() {
		return operatingSystemVersionBase;
	}


}
