package org.middleheaven.util.sequence;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.reflection.ReflectionUtils;

public class Sequences {

	private Sequences(){}
	
	private static final Map<String, String> sequences = new TreeMap<String, String>();

	static {
		setSequence(Long.class, LongSequence.class);
	}
	
	public static <T> Sequence<T> newSequenceFor (Class<T> elementType){
		String sequenceType = sequences.get(elementType);
		if (sequenceType==null){
			throw new RuntimeException("Sequence not found for type " + elementType);
		}
		return (Sequence<T>) ReflectionUtils.newInstance(sequenceType);
	}
	
	public static <T, S extends Sequence<T>> void setSequence(Class<T> elementType,Class<S> sequenceType){
		sequences.put(elementType.getName(), sequenceType.getName());
	}
	
	public static <T> Sequence<T> over (T ... elements){
		return IterableBasedSequence.sequenceFor(elements);
	}
	
	public static <T> Sequence<T> over (List<T> elements ){
		return IterableBasedSequence.sequenceFor(elements);
	}
}
