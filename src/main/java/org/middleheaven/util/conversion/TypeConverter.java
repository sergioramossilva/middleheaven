package org.middleheaven.util.conversion;

/**
 * Converts a value of a certain type <code>O</code> into a value of type <code>R</code>
 * 
 * @param <O> original type
 * @param <R> result type
 */
public interface TypeConverter<O,R> {

	 public <T extends R> T convertFoward(O value, Class<T> type);
	 public <T extends O> T convertReverse(R value , Class<T> type);
	 public TypeConverter<R,O> inverse();
	 
}
