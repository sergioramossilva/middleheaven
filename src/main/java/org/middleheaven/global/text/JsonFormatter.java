package org.middleheaven.global.text;


public class JsonFormatter<T> implements Formatter<T>{

	
	public static JsonFormatter<Object> formatOnly(){
		return new JsonFormatter<Object>(Object.class);
	}

	public static <U> JsonFormatter<U> formatOrParse(Class<U> objectType){
		return new JsonFormatter<U>(objectType);
	}
	
	private Class<T> objectType;

	private JsonFormatter (Class<T> objectType){
		this.objectType = objectType;
	}
	
	@Override
	public String format(T object) {
		return "[]";
	}

	@Override
	public T parse(String stringValue) {
		// TODO implement Formatter.parse
		return null;
	}

}
