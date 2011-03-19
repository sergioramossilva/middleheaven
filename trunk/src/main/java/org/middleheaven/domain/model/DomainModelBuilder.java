package org.middleheaven.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.middleheaven.core.dependency.DependencyResolver;
import org.middleheaven.core.dependency.InicializationNotPossibleException;
import org.middleheaven.core.dependency.InicializationNotResolvedException;
import org.middleheaven.core.dependency.Starter;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.logging.Log;
import org.middleheaven.model.AbstractModelBuilder;
import org.middleheaven.util.classification.Classifier;

/**
 * 
 */
public final class DomainModelBuilder extends AbstractModelBuilder<EntityModel, DomainModel, DomainModelReader> {


	public DomainModelBuilder(){
		addReader(new DefaultAnnotatedModelReader());
	}

	public DomainModel build(ClassSet classes){

		final SimpleModelBuilder builder = new SimpleModelBuilder();

		new DependencyResolver(Log.onBookFor(this.getClass())).resolve(classes.entities, new Starter<Class<?>>(){

			@Override
			public void inicialize(Class<?> type)
			throws InicializationNotResolvedException,
			InicializationNotPossibleException {

				reader().read(type, builder);
			}

			@Override
			public void inicializeWithProxy(Class<?> dependableProperties)
			throws InicializationNotResolvedException,
			InicializationNotPossibleException {

				throw new UnsupportedOperationException();
			}

			@Override
			public List<Class<?>> sort(final Collection<Class<?>> dependencies) {

				List<ClassDependency> deps = new ArrayList<ClassDependency>(dependencies.size());

				for (Class<?> c: dependencies){
					deps.add(new ClassDependency (c, dependencies));
				}

				Collections.sort(deps, new Comparator<ClassDependency>(){

					@Override
					public int compare(ClassDependency a,	ClassDependency b) {
						return a.getDepenciesCount() - b.getDepenciesCount();
					}

				});
				List<Class<?>> result = new ArrayList<Class<?>>(dependencies.size());
				for (ClassDependency cd : deps){
					result.add(cd.getType());
				}

				return result;
			}

			class ClassDependency {

				private Class<?> type;
				private Collection<Class<?>> depends;

				public ClassDependency(Class<?> type, final Collection<Class<?>> dependencies){
					this.type = type;

					this.depends = Introspector.of(type).inspect().properties().retriveAll().collect(new Classifier<Class<?>,PropertyAccessor>(){

						@Override
						public Class<?> classify(PropertyAccessor obj) {
							if( dependencies.contains(obj.getValueType())){
								return obj.getValueType();
							}
							return null;
						}

					});


				}

				public int getDepenciesCount() {
					return depends.size();
				}

				public Class getType(){
					return type;
				}

			}

		});

		return builder.getModel();
	}

	private static class SimpleModelBuilder implements EntityModelBuilder {

		EditableDomainModel model = new EditableDomainModel();
		
		@Override
		public EditableDomainEntityModel getEditableModelOf(Class<?> type) {
			EntityModel em = model.getModelFor(type);
			if (em == null){
				em =  new HashEditableEntityModel(type);
				model.addModel(type, em);
			}
			return (EditableDomainEntityModel) em;
		}

		public DomainModel getModel() {
			return model;
		}

	
	}
}
