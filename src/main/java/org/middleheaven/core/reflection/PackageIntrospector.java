package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;

import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.TransformedCollection;

public class PackageIntrospector extends Introspector {

	private Package typePackage;

	PackageIntrospector(Package typePackage) {
		this.typePackage = typePackage;
	}

	public String getName(){
		return typePackage.getName();
	}


	public EnhancedCollection<ClassIntrospector> getClassesIntrospectors(){
		return CollectionUtils.enhance(
				new TransformedCollection<Class<?>, ClassIntrospector>(
						ReflectionUtils.getPackageClasses(typePackage) , 
						new Classifier< ClassIntrospector,Class<?>>(){

							@Override
							public ClassIntrospector classify(Class<?> obj) {
								return Introspector.of(obj);
							}
						}
				)
		);
	}

	public EnhancedCollection<Class<?>> getClasses(){
		return CollectionUtils.enhance(ReflectionUtils.getPackageClasses(typePackage));
	}
	
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return this.typePackage.getAnnotation(annotationClass);
	}

	@Override
	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return this.typePackage.isAnnotationPresent(annotationClass);
	}
}
