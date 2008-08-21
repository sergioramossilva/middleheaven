package org.middleheaven.util.conversion;

import java.util.HashMap;
import java.util.Map;

public class TypeConvertions {

	private final static Map<Key, TypeConverter> converters = new HashMap<Key, TypeConverter>();

	static {

		addConverter(CharSequence.class, Number.class , new CharSequenceNumberConverter());

	}
	public static <O,T> T convert (O value , Class<T> type ){
		if (type.isAssignableFrom(value.getClass())){
			return type.cast(value);
		}
		Class<O> valueClass = (Class<O>) value.getClass();
		return getConverter(valueClass, type).convertFoward(value, type);
	}

	public static <O,R> void addConverter(Class<O> from , Class<R> to, TypeConverter<O,R> converter){
		converters.put(new Key(from,to), converter);
		converters.put(new Key(to,from), converter.inverse());
	}

	public static <O,R> TypeConverter<O,R> getConverter(Class<O> from , Class<R> to){
		Key key = new Key(from,to);
		TypeConverter<O,R> converter = converters.get(key);

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
			return 0;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof Key && isCompatible(this, (Key)other);
		}
		
		private static boolean isCompatible(Key a , Key b ){
			return a.from.isAssignableFrom(a.from) && a.to.isAssignableFrom(b.to) ||
			b.from.isAssignableFrom(a.from) && b.to.isAssignableFrom(a.to);
		}
	}  
}
