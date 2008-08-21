package org.middleheaven.util.conversion;

public abstract class AbstractTypeConverter<O,R> implements TypeConverter<O,R> {


	@Override
	public final TypeConverter<R,O> inverse() {
		return new InvertedTypeConverter<R,O>(this);
	}

}
