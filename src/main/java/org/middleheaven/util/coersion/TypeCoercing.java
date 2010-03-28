package org.middleheaven.util.coersion;

import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.NumberIdentityCoersor;
import org.middleheaven.util.identity.StringIdentityCoersor;

public class TypeCoercing {

	private final static Map<Key, TypeCoersor> converters = new HashMap<Key, TypeCoersor>();

	static {
		// text
		addCoersor(CharSequence.class, Number.class , new CharSequenceNumberConverter());
		addCoersor(CharSequence.class, Boolean.class , new CharSequenceBooleanCoersor());
		addCoersor(String.class, Identity.class , new StringIdentityCoersor());
		// numbers
		addCoersor(Integer.class, Identity.class ,  NumberIdentityCoersor.newInstance(Integer.class));
		addCoersor(Long.class, Identity.class ,  NumberIdentityCoersor.newInstance(Long.class));
		// dates
		addCoersor(Timestamp.class, java.util.Date.class ,  DateTypeCoersor.newInstance(Timestamp.class));
		addCoersor(java.sql.Date.class, java.util.Date.class ,  DateTypeCoersor.newInstance(java.sql.Date.class));
		addCoersor(Time.class, java.util.Date.class ,  DateTypeCoersor.newInstance(Time.class));
		
		addCoersor(CalendarDate.class ,java.util.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.util.Date.class));
		addCoersor(CalendarDate.class ,java.sql.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Date.class));
		addCoersor(CalendarDate.class ,java.sql.Timestamp.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Timestamp.class));
		addCoersor(CalendarDate.class ,java.sql.Time.class,  CalendarDateTypeCoersor.getInstance(CalendarDate.class,java.sql.Time.class));
		
		addCoersor(CalendarDateTime.class ,java.util.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.util.Date.class));
		addCoersor(CalendarDateTime.class ,java.sql.Date.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.sql.Date.class));
		addCoersor(CalendarDateTime.class ,java.sql.Timestamp.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.sql.Timestamp.class));
		addCoersor(CalendarDateTime.class ,java.sql.Time.class,  CalendarDateTypeCoersor.getInstance(CalendarDateTime.class,java.sql.Time.class));
		
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
	
	@SuppressWarnings("unchecked")
	public static <O,T> T coerce (O value , Class<T> type ){
		if (value==null){
			return null;
		} else if (type.isAssignableFrom(java.util.Date.class)){
			// dates are handled differently
			if(value.getClass().equals(type)){
				return type.cast(value);
			}
			@SuppressWarnings("unchecked") Class<O> valueClass = (Class<O>) value.getClass();
			return getCoersor(valueClass, type).coerceForward(value, type);
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
		return getCoersor(valueClass, type).coerceForward(value, type);
	}

	private static void checkOverflow(BigInteger value, BigInteger max){
		if (value.compareTo(max) > 0){
			throw new CoersionException("Overflow convertion." + value + " is greater than " + max);
		}
	}
	public static <O,R> void addCoersor(Class<O> from , Class<R> to, TypeCoersor<O,R> converter){
		converters.put(new Key(from,to), converter);
		converters.put(new Key(to,from), converter.inverse());
	}

	public static <O,R> void removeCoersor(Class<O> from, Class<R> to) {
		converters.remove(new Key(from,to));
		converters.remove(new Key(to,from));
	}  
	
	
	
	@SuppressWarnings("unchecked")
	public static <O,R> TypeCoersor<O,R> getCoersor(Class<O> from , Class<R> to){
		
		if (to.isPrimitive()){
			String wrapper = to.getSimpleName().substring(0,1).toUpperCase() + to.getSimpleName().substring(1);
			if (to.getSimpleName().equals("int")){
				wrapper = "Integer";
			}
			 to = (Class<R>)Introspector.of(Object.class).load("java.lang."+wrapper).getIntrospected();
			
		}
		Key key = new Key(from,to);
		TypeCoersor<O,R> converter = converters.get(key);

		if (converter==null){
			throw new CoersionException("TypeCoersor from " + from + " to " + to + " has not found");
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
