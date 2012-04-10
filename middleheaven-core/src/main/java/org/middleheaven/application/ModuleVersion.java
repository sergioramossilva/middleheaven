package org.middleheaven.application;

import java.io.Serializable;

import org.middleheaven.util.Version;

/**
 * Represents an Application Module.
 * 
 * A module is represented by a name and a version number.
 */
public class ModuleVersion implements Serializable {

	private static final long serialVersionUID = 1475241472644564012L;
	
	private String name;
	private Version version;
	private ApplicationVersion applicationVersion;
	
	public ModuleVersion(String applicationName, Version appicationVersion, String moduleName, Version moduleVersion) {
		this(new ApplicationVersion(applicationName, appicationVersion), moduleName, moduleVersion);
	}
	
	public ModuleVersion(ApplicationVersion parentApplication, String name, Version version) {
		super();
		this.applicationVersion = parentApplication;
		this.name = name;
		this.version = version;
	}

	public ApplicationVersion getApplicationVersion(){
		return applicationVersion;
	}
	
	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
	}
	
	public boolean equals(Object other) {
		return other instanceof ModuleVersion && equals((ModuleVersion) other);
	}

	public boolean equals(ModuleVersion other) {
		return this.name.equals(other.name) && this.version.equals(other.version);
	}

	public int hashCode() {
		return this.name.hashCode() ^ version.hashCode();
	}
	
	public String toString(){
		return this.applicationVersion.toString() + "#" + name + "=" + version;
	}
}
