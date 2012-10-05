package org.middleheaven.global.address;

import java.io.Serializable;



public class PostalCode implements Serializable {

	private static final long serialVersionUID = -9189029361559385788L;
	
	private String code;

	public PostalCode (String code){
		this.code = code;
	}
	
	public String toString(){
		return code;
	}
	
	public char charAt(int index){
		return code.charAt(index);
	}
	
	public int length(){
		return code.length();
	}
	
	public boolean equals(Object other){
		return other instanceof PostalCode && equalsOther((PostalCode)other);
	}
	
	private boolean equalsOther(PostalCode other){
		return this.code.equals(other.code);
	}
	
	public int hashCode(){
		return this.code.hashCode();
	}
	

}
