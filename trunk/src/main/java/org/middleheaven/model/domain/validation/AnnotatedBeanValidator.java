package org.middleheaven.model.domain.validation;

import org.middleheaven.model.domain.DataType;
import org.middleheaven.model.domain.EntityFieldModel;
import org.middleheaven.model.domain.EntityModel;
import org.middleheaven.model.domain.TextDataTypeModel;
import org.middleheaven.validation.BeanValidator;
import org.middleheaven.validation.CompositeValidator;
import org.middleheaven.validation.LengthValidator;
import org.middleheaven.validation.NotEmptyValidator;
import org.middleheaven.validation.NotNullValidator;
import org.middleheaven.validation.SimpleEmailAddressValidator;

public class AnnotatedBeanValidator<T> extends BeanValidator<T>{

	
	@SuppressWarnings("unchecked")
	public static <U> AnnotatedBeanValidator<U> getInstanceFor(EntityModel model){
		AnnotatedBeanValidator<U> validator = new AnnotatedBeanValidator<U>();
		
		for (EntityFieldModel efm : model.fields()){
			CompositeValidator<?> comp = new CompositeValidator();
			if (!efm.isNullable()){
				comp.add(new NotNullValidator());
			}
			
			if (efm.getDataType().equals(DataType.TEXT)){
				TextDataTypeModel dtModel = (TextDataTypeModel)efm.getDataTypeModel();
				
				if (!dtModel.isEmptyable()){
					comp.add(new NotEmptyValidator());
				} 
				
				if (dtModel.isEmailAddress()){
					comp.add(new SimpleEmailAddressValidator());
				} 
				
				
				comp.add(new LengthValidator(dtModel.getMinLength(), dtModel.getMaxLength()));
				
			} 
			
			validator.addPropertyValidator(efm.getLogicName().getName(), comp);
		}
		return validator;
	}
	
	private AnnotatedBeanValidator(){
		
	}
}
