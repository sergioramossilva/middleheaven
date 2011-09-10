package org.middleheaven.application;

import java.io.Serializable;

import org.middleheaven.util.Version;

public final class ApplicationID implements Serializable{

	
	private String identifier;
	private Version version;
	
	public ApplicationID(String identifier, Version version) {
		super();
		this.identifier = identifier;
		this.version = version;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Version getVersion() {
		return version;
	}
	
	public boolean equals(Object other) {
		return other instanceof ApplicationID && equals((ApplicationID) other);
	}

	public boolean equals(ApplicationID other) {
		return this.identifier.equals(other.identifier) && this.version.equals(other.version);
	}

	public int hashCode() {
		return this.identifier.hashCode() ^ version.hashCode();
	}
}
