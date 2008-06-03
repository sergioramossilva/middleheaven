/*
 * Created on 13/12/2005
 */
package org.middleheaven.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Represents a version number. A version number as 4 parts The major version number, the minor version number, the revision number and the build version number
 */
public class Version implements Comparable<Version>{


    private int major;
    private int minor;
    private int revision;
    private int build;

    /**
     * Reads the version from a string in the format *-M.m.r.b-* where * is any number of characters
     * M is the major number, m the minor , r the revision and b the build number
     * @return the correct Version
     */
    public static Version fromFileName(String versionStr){
        String[] parts =versionStr.split("-");
        if (parts.length >0){
            for (int i =0;i<parts.length;i++){
                if (Character.isDigit(parts[i].charAt(0)) && Character.isDigit(parts[i].charAt(2))){
                    return fromString(parts[i]);
                }
            }
        }
        return new Version();

    }

    public static Version fromJarFile(File file) throws IOException{
        JarFile jarFile = new JarFile(file);

        Enumeration<JarEntry> e = jarFile.entries();
        JarEntry entry =null;
        
        while (e.hasMoreElements()){
            entry = e.nextElement();
            if (entry.getName().endsWith("version.properties")){
                break;
            }
        }

        if (entry==null) return null;

        return Version.fromResource(jarFile.getInputStream(entry));
    }

    public static Version fromString(String versionStr){
        String[] parts =StringUtils.split(versionStr.trim(),".");
        Version version = new Version();
        try{
            if (parts.length>0){

                version.setMajor(Integer.parseInt(parts[0]));

            }
            if (parts.length>1){

                version.setMinor(Integer.parseInt(parts[1]));

            }
            if (parts.length>2){

                version.setRevision(Integer.parseInt(parts[2]));

            }
            if (parts.length>3){

                version.setBuild(Integer.parseInt(parts[3]));
            }
        } catch (NumberFormatException e){
            return version;
        }
        return version;
    }

    public static Version fromResource(String name) throws IOException{
        return fromResource(Version.class.getResourceAsStream(name));
    }

    public static Version fromResource(InputStream s) throws IOException{
        if (s==null){
            throw new FileNotFoundException();
        }
        Properties p = new Properties();
        p.load(s);
        return fromProperties(p);
    }

    public static Version fromProperties(Properties p) throws IOException{

        Version version = new Version();
        try{
            version.setMajor(Integer.parseInt(p.getProperty("version.major")));
        } catch (NumberFormatException e){}
        try{
            version.setMinor(Integer.parseInt(p.getProperty("version.minor")));
        } catch (NumberFormatException e){}
        try{
            version.setRevision(Integer.parseInt(p.getProperty("version.revision")));
        } catch (NumberFormatException e){}
        try{
            version.setBuild(Integer.parseInt(p.getProperty("version.build")));
        } catch (NumberFormatException e){}
        return version;
    }

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
        return major + "." + minor + "." + revision + "." + build;
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
