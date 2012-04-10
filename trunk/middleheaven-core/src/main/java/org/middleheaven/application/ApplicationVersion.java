package org.middleheaven.application;

import java.io.Serializable;

import org.middleheaven.util.Version;

/**
 * Represents the application.
 * 
 * The application is represented by a name and a version number.
 */
public final class ApplicationVersion implements Serializable{

	
	private static final long serialVersionUID = -5042487406887297074L;
	
	private String name;
	private Version version;
	
	public ApplicationVersion(String name, Version version) {
		super();
		this.name = name;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public Version getVersion() {
		return version;
	}
	
	public boolean equals(Object other) {
		return other instanceof ApplicationVersion && equals((ApplicationVersion) other);
	}

	public boolean equals(ApplicationVersion other) {
		return this.name.equals(other.name) && this.version.equals(other.version);
	}

	public int hashCode() {
		return this.name.hashCode() ^ version.hashCode();
	}
	
	public String toString(){
		return name + "=" + version.toString();
	}
}

