package org.middleheaven.util.conversion;

class InvertedTypeConverter<O,R> implements TypeConverter<O,R>{

	TypeConverter<R,O> original;
	
	public InvertedTypeConverter(TypeConverter<R,O> original){
		this.original = original;
	}
	
	@Override
	public TypeConverter<R, O> inverse() {
		return original;
	}

	@Override
	public <T extends R> T convertFoward(O value, Class<T> type) {
		return original.convertReverse(value, type);
	}

	@Override
	public <T extends O> T convertReverse(R value, Class<T> type) {
		return original.convertFoward(value, type);
	}

}
