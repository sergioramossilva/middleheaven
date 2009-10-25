package org.middleheaven.util.conversion;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.NumberIdentityConverter;
import org.middleheaven.util.identity.StringIdentityConverter;

public class TypeConvertions {

	private final static Map<Key, TypeConverter> converters = new HashMap<Key, TypeConverter>();

	static {
		// text
		addConverter(CharSequence.class, Number.class , new CharSequenceNumberConverter());
		addConverter(CharSequence.class, Boolean.class , new CharSequenceBooleanConverter());
		addConverter(String.class, Identity.class , new StringIdentityConverter());
		// numbers
		addConverter(Integer.class, Identity.class ,  NumberIdentityConverter.newInstance(Integer.class));
		addConverter(Long.class, Identity.class ,  NumberIdentityConverter.newInstance(Long.class));
		// dates
		addConverter(Timestamp.class, java.util.Date.class ,  DateTypeConverter.newInstance(Timestamp.class));
		addConverter(java.sql.Date.class, java.util.Date.class ,  DateTypeConverter.newInstance(java.sql.Date.class));
		addConverter(Time.class, java.util.Date.class ,  DateTypeConverter.newInstance(Time.class));
		addConverter(CalendarDate.class ,java.util.Date.class, new CalendarDateTypeConverter());
		
	}
	
	
	/**
	 * Converts a value represented in one type to the same value represented by another type.
	 * 
	 * If {@code type} is a primitive type, the conversion 
	 * @param <O> value original type
	 * @param <T> target conversion type. 
	 * @param value the value to be converted
	 * @param type the target type. 
	 * @return The same value represented in the {@code type} type.
	 */
	
	public static <O,T> T convert (O value , Class<T> type ){
		if (value==null){
			return null;
		} else if (type.isAssignableFrom(java.util.Date.class)){
			// dates are handled differently
			if(value.getClass().equals(type)){
				return type.cast(value);
			}
			@SuppressWarnings("unchecked") Class<O> valueClass = (Class<O>) value.getClass();
			return getConverter(valueClass, type).convertFoward(value, type);
		} else if (type.isInstance(value)){
			return type.cast(value);
		} else if (type.isPrimitive()){
			// cannot type.cast the result because type is a primitive
			
			if (type.isInstance(value)){
				return (T)value;
			} else {
				BigInteger big = new BigInteger(value.toString());
				if (type.equals(Byte.TYPE)){
					checkOverflow(big, new BigInteger(new Byte(Byte.MAX_VALUE).toString()));
					return (T)Byte.valueOf(big.byteValue());
				} else if (type.equals(Short.TYPE)){
					checkOverflow(big, new BigInteger(new Short(Short.MAX_VALUE).toString()));
					return (T)Short.valueOf(big.shortValue());
				} else if (type.equals(Integer.TYPE)){
					checkOverflow(big, new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
					return (T)Integer.valueOf(big.intValue());
				} else if (type.equals(Character.TYPE)){
					checkOverflow(big, new BigInteger(new Character(Character.MAX_VALUE).toString()));
					return (T) new Character((char)big.intValue());
				} else if (type.equals(Long.TYPE)){
					checkOverflow(big, new BigInteger(new Long(Long.MAX_VALUE).toString()));
					return (T)Long.valueOf(big.longValue());
				}
			}
		} 
		
		
		@SuppressWarnings("unchecked") Class<O> valueClass = (Class<O>) value.getClass();
		return getConverter(valueClass, type).convertFoward(value, type);
	}

	private static void checkOverflow(BigInteger value, BigInteger max){
		if (value.compareTo(max) > 0){
			throw new ConvertionException("Overflow convertion." + value + " is greater than " + max);
		}
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
			to = (Class<R>)Introspector.of(Object.class).load("java.lang."+wrapper).getIntrospected();
			
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
