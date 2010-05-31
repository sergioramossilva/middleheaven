package org.middleheaven.util.criteria.entity;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.storage.IdentityManager;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.criteria.Criteria;

public class JuntionFilter<T> implements BooleanClassifier<T> {

	QualifiedName fieldName;
	CriteriaFilter<T> subCriteria;
	
	public JuntionFilter(QualifiedName fieldName,StorableEntityModel target , EntityCriteria<T> criteria,IdentityManager identityManager) {
		super();
		subCriteria = new CriteriaFilter<T>(criteria, target,identityManager);
		
		this.fieldName = fieldName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean classify(T obj) {
		Object realValue = null;

		
		PropertyAccessor acessor =  Introspector.of(((Storable)obj).getPersistableClass())
		.inspect().properties().named(fieldName.getName()).retrive();
			
		realValue = acessor.getValue(obj);

		return realValue!=null && subCriteria.classify((T)realValue);
	
	}

}
