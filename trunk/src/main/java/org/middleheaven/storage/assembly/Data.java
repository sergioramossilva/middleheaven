package org.middleheaven.storage.assembly;

public class Data {

	private Object object;
	private String name;
	public Data(String name, Object value) {
		this.object = value;
		this.name = name;
	}

	public Object getValue(){
		return object;
	}
	
	public void setValue(Object value){
		this.object = value;
	}
	
	public Class<?> getValueType(){
		return object.getClass();
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(Object other){
		return other instanceof Data && ((Data)other).name.equals(this.name);
	}
	
	public int hashCode(){
		return name.hashCode();
	}
}
