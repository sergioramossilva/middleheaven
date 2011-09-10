/*
 * Created on 13/12/2005
 */
package org.middleheaven.util;


/**
 * Represents a version number. A version number as 4 parts The major version number, the minor version number, the revision number and the build version number
 */
public final class Version implements Comparable<Version>{


    private int major;
    private int minor;
    private int revision;
    private int build;
    private String tag;
    
    private boolean unkown = false;
    
	public static Version valueOf(int major, int minor, int revision) {
		return valueOf(major, minor, revision, 0, "");
	}
	
	public static Version valueOf(int major, int minor) {
		return valueOf(major, minor, 0, 0, "");
	}
	
	public static Version valueOf(int major, int minor, int revision, int build, CharSequence tag) {
		return new Version(major, minor, revision, build, tag.toString().trim());
	}


	public static Version unknown() {
		return new Version(true);
	}
	
	Version(boolean unkown){
		this.unkown = unkown;
	}
	
    Version(int major, int minor, int revision, int build, String tag) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.build = build;
        this.tag = tag.isEmpty() ? null : tag;
    }

    public boolean isUnknown(){
    	return this.unkown;
    }
    
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
    	if (this.isUnknown()){
    		return "UNKNOWN";
    	}
        return major + "." + minor + "." + revision + (build>0 ? "." + build : "") + (tag != null ? "-" + tag : "");
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
		this.build == other.build &&
		(this.tag == null ? other.tag == null : this.tag.equals(other.tag));
	}


}
