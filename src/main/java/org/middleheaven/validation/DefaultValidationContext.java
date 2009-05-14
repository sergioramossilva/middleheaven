package org.middleheaven.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.util.collections.IteratorsIterator;

public class DefaultValidationContext implements ValidationContext{


	private Map<String, List<InvalidationReason>> reasons = new HashMap<String, List<InvalidationReason>>();
	private boolean hasErrors = false;
	private boolean hasWarnings = false;
	
	
	public <T> ValidationContext apply(Validator<T> validator, T object){
		 validator.validate(this, object);
		 return this;
	}
	
	public void clear(){
		hasErrors = false;
		hasWarnings = false;
		reasons.clear();
	}
	
	@Override
	public void add(InvalidationReason reason) {
		if (!hasErrors && InvalidationSeverity.ERROR.equals(reason.getSeverity())){
			hasErrors = true;
		}
		if (!hasWarnings && InvalidationSeverity.WARNING.equals(reason.getSeverity())){
			hasWarnings = true;
		}
		if (reason instanceof TaggedInvalidationReason){
			add(((TaggedInvalidationReason)reason).getTag(), reason);
		} else {
			add(null, reason);
		}
	}
	
	private void add(String tag, InvalidationReason reason){
		List<InvalidationReason> list = reasons.get(tag);
		if (list==null){
			list = new LinkedList<InvalidationReason>();
			reasons.put(tag,list);
		}
		list.add(reason);
	}

	@Override
	public boolean hasErrors() {
		return hasErrors;
	}

	@Override
	public boolean hasWarnings() {
		return hasWarnings;
	}

	@Override
	public boolean isValid() {
		return !hasErrors;
	}

	@Override
	public Iterator<InvalidationReason> iterator() {
		// TODO implement ValidationContext.iterator
		// reason.values returns a list of list of reasons.
		return new IteratorsIterator<InvalidationReason>(reasons.values());
	}


	@Override
	public Iterator<InvalidationReason> iterator(InvalidationSeverity severity) {
		// TODO implement ValidationContext.iterator
		return null;
	}
	
	@Override
	public boolean isStrictlyValid() {
		return reasons.isEmpty();
	}

	@Override
	public void merge(ValidationContext other) {
	
		for ( InvalidationReason reason : other){
			this.add(reason);
		}

	}


}
