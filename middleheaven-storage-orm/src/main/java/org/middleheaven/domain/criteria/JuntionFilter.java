//package org.middleheaven.domain.criteria;
//
//import org.middleheaven.core.reflection.PropertyAccessor;
//import org.middleheaven.domain.model.EntityModel;
//import org.middleheaven.domain.store.EntityInstance;
//import org.middleheaven.domain.store.IdentityManager;
//import org.middleheaven.util.classification.BooleanClassifier;
//import org.middleheaven.util.criteria.QualifiedName;
//
//public class JuntionFilter<T> implements BooleanClassifier<T> {
//
//	QualifiedName fieldName;
//	CriteriaFilter<T> subCriteria;
//	
//	public JuntionFilter(QualifiedName fieldName, EntityModel target , EntityCriteria<T> criteria,IdentityManager identityManager) {
//		super();
//		subCriteria = new CriteriaFilter<T>(criteria, target,identityManager);
//		
//		this.fieldName = fieldName;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public Boolean classify(T obj) {
//		Object realValue = null;
//
//		
//		PropertyAccessor acessor = ((EntityInstance)obj).getEntityModel().getEntityClass().getPropertyAcessor(fieldName.getName());
//				
//		realValue = acessor.getValue(obj);
//
//		return realValue!=null && subCriteria.classify((T)realValue);
//	
//	}
//
//}
