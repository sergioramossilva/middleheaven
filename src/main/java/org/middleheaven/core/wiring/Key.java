package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.middleheaven.util.collections.CollectionUtils;

class Key {

	private Class targetClass;
	private Set specifications = new HashSet ();
	

	public String toString(){
		return targetClass.getName() + "+" + specifications.toString();
	}
	
	public static  Key keyFor(Class<?> targetClass, Set<Annotation> specificationsSet) {
		return new Key(targetClass,specificationsSet);
	}
	
	private Key(Class<?> targetClass, Set<Annotation> specificationsSet) {
		this.targetClass = targetClass;
		for (Iterator<Annotation> it = specificationsSet.iterator();it.hasNext();){
			Object a  = it.next();
			if (a==null){
				continue;
			}
			if (a instanceof Annotation){
				this.specifications.add(((Annotation)a).annotationType());
			} else {
				this.specifications.add(a);
			}
		
		}
		
		
	}
	
	public int hashCode(){
		return targetClass==null ? 0 :  targetClass.getName().hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof Key && equals((Key)other);
	}
	
	public boolean equals(Key other){
		return other.targetClass == this.targetClass || ( other.targetClass.getName().equals(this.targetClass.getName()) &&
		 	CollectionUtils.equalContents(this.specifications, other.specifications));

	}
}
