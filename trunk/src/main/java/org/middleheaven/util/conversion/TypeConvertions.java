package org.middleheaven.util.conversion;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentityConverter;
import org.middleheaven.util.identity.StringIdentityConverter;

public class TypeConvertions {

	private final static Map<Key, TypeConverter> converters = new HashMap<Key, TypeConverter>();

	static {
		addConverter(CharSequence.class, Number.class , new CharSequenceNumberConverter());
		addConverter(CharSequence.class, Boolean.class , new CharSequenceBooleanConverter());
		addConverter(String.class, Identity.class , new StringIdentityConverter());
		addConverter(Integer.class, Identity.class , new IntegerIdentityConverter());
		
	}
	
	
	public static <O,T> T convert (O value , Class<T> type ){
		if (value==null){
			return null;
		}
		
		if (type.isPrimitive()){
			return (T)value;
		}
		
		if (type.isInstance(value)){
			return type.cast(value);
		}
		Class<O> valueClass = (Class<O>) value.getClass();
		return getConverter(valueClass, type).convertFoward(value, type);
	}

	public static <O,R> void addConverter(Class<O> from , Class<R> to, TypeConverter<O,R> converter){
		converters.put(new Key(from,to), converter);
		converters.put(new Key(to,from), converter.inverse());
	}

	public static <O,R> void removeConverter(Class<O> from, Class<R> to) {
		converters.remove(new Key(from,to));
		converters.remove(new Key(to,from));
	}  
	
	
	public static <O,R> TypeConverter<O,R> getConverter(Class<O> from , Class<R> to){
		
		if (to.isPrimitive()){
			String wrapper = to.getSimpleName().substring(0,1).toUpperCase() + to.getSimpleName().substring(1);
			if (to.getSimpleName().equals("int")){
				wrapper = "Integer";
			}
			to = (Class<R>) ReflectionUtils.loadClass("java.lang."+wrapper);
			
		}
		Key key = new Key(from,to);
		TypeConverter<O,R> converter = converters.get(key);

		if (converter==null){
			throw new RuntimeException("Converter from " + from + " to " + to + " has not found");
		}
		return converter;
	}

	private static class Key{
		Class<?> from;
		Class<?> to;


		public Key(Class<?> from, Class<?> to) {
			super();
			this.from = from;
			this.to = to;
		}

		public Class<?> getFrom() {
			return from;
		}
		public Class<?> getTo() {
			return to;
		}

		@Override
		public int hashCode() {
			return 0 ;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof Key && isCompatible(this, (Key)other);
		}
		
		private static boolean isCompatible(Key a , Key b ){
			return a.from.isAssignableFrom(b.from) && a.to.isAssignableFrom(b.to) ||
			b.from.isAssignableFrom(a.from) && b.to.isAssignableFrom(a.to);
		}
	}

	
}
