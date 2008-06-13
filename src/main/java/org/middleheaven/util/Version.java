/*
 * Created on 13/12/2005
 */
package org.middleheaven.util;


/**
 * Represents a version number. A version number as 4 parts The major version number, the minor version number, the revision number and the build version number
 */
public class Version implements Comparable<Version>{


    private int major;
    private int minor;
    private int revision;
    private int build;

 
    /**
     * @param major
     * @param minor
     * @param revision
     * @param build
     */
    public Version(int major, int minor, int revision, int build) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.build = build;
    }

    public Version(int major, int minor, int revision) {
        this(major,minor,revision,-1);
    }
    
    public Version(int major, int minor) {
        this(major,minor,0,-1);
    }
    
    protected Version(){}

    public final int getBuild() {
        return build;
    }
    
    public final void setBuild(int build) {
        this.build = build;
    }
    
    public final int getMajor() {
        return major;
    }
  
    public final void setMajor(int major) {
        this.major = major;
    }
  
    public final int getMinor() {
        return minor;
    }
    
    public final void setMinor(int minor) {
        this.minor = minor;
    }

    public final int getRevision() {
        return revision;
    }

    public final void setRevision(int revision) {
        this.revision = revision;
    }

    public String toString(){
        return major + "." + minor + "." + revision + (build>=0 ? "." + build : "");
    }

    public boolean exists(){
        return major + minor + revision + build >0;
    }

	@Override
	public int compareTo(Version other) {
		int d = this.major - other.major;
		if (d==0){
			d = this.minor - other.minor;
			if (d==0){
				d = this.revision - other.revision;
				if (d==0){
					d = this.build - other.build;
				}
			}
		} 
		return d;
	}
	
	public boolean equals(Object other){
		return other instanceof Version && equals((Version)other);
	}
	
	public boolean equals(Version other){
		return this.major == other.major && 
		this.minor == other.minor && 
		this.revision == other.revision &&
		this.build == other.build;
	}

	public static Version from(int major, int minor, int revision) {
		return new Version(major, minor, revision, 0);
	}
}
