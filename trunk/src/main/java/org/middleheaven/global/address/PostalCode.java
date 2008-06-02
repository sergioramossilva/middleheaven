package org.middleheaven.global.address;



public class PostalCode {

	String code;

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
		return other instanceof PostalCode && equals((PostalCode)other);
	}
	
	public boolean equals(PostalCode other){
		return this.code.equals(other.code);
	}
	
	public int hashCode(){
		return this.code.hashCode();
	}
	

}
