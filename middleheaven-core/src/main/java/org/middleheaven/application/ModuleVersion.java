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

	public ModuleVersion(String moduleName, Version moduleVersion) {
		this.name = moduleName;
		this.version = moduleVersion;
	}
	
	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
	}
	
	public boolean equals(Object other) {
		return other instanceof ModuleVersion && equalsOther((ModuleVersion) other);
	}

	public boolean equalsOther(ModuleVersion other) {
		return this.name.equals(other.name) && this.version.equals(other.version);
	}

	public int hashCode() {
		return this.name.hashCode() ^ version.hashCode();
	}
	
	public String toString(){
		return name + "=" + version;
	}
}
