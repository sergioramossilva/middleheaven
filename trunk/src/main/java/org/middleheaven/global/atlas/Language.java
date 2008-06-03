package org.middleheaven.global.atlas;

public class Language implements Comparable<Language>{

	
	private String code;
	
	public Language(String code){
		this.code = code;
	}
	
	public boolean equals(Object other) {
		return other instanceof Language && equals((Language) other);
	}

	public boolean equals(Language other) {
		return this.code.equals(other.code);
	}

	public int hashCode() {
		return code.hashCode();
	}
	
	public String toString() {
		return code;
	}
	
	/**
	 * Assert if the language represented by this is the same 
	 * as the represented by the ISO 639 language code
	 * @param isoCode 
	 * @return
	 */
	public boolean equals (String isoCode){
		return code.equalsIgnoreCase(isoCode);
	}

	@Override
	public int compareTo(Language other) {
		return this.code.compareTo(other.code);
	}
}
