/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Enumeration;
import java.util.Iterator;

import org.middleheaven.process.Attribute;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ContextScopeStrategy;
import org.middleheaven.process.ObjectAttribute;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.TransformedIterator;

/**
 * 
 */
abstract class AbstractEnumerationBasedContextScopeStrategy implements ContextScopeStrategy{

	
	private ContextScope scope;

	public AbstractEnumerationBasedContextScopeStrategy (ContextScope scope){
		this.scope = scope;
	}
	
	protected abstract Enumeration<String> getEnumeration();
	
	protected abstract Object getValue(String name);
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final int size() {
		int count = 0;
		Enumeration enumeration = this.getEnumeration();
		while (enumeration.hasMoreElements()){
			count++;
		}
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isEmpty() {
		return this.getEnumeration().hasMoreElements();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Attribute> iterator() {
		Iterator<String> it = CollectionUtils.enumationIterator(this.getEnumeration());
		
		return TransformedIterator.transform(it, new Classifier<Attribute, String>(){

			@Override
			public Attribute classify(String next) {
				return new ObjectAttribute(next, getValue(next));
			}
			
		});
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ContextScope getScope() {
		return scope;
	}
	
	

}
