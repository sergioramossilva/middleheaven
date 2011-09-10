package org.middleheaven.validation;

public class ValidatorBuilder<T> {

	
	public static <B> ValidatorBuilder<B> newValidator(){
		return new ValidatorBuilder<B>();
	}
	
	private BeanValidator<T> validator = new BeanValidator<T>();
	
	public ValidatorBuilder(){
		
	}
	
	public ValidatorBuilder<T> instanceWith(Validator<T> validator){
		this.validator.add(validator);
		return this;
	}
	
	public PropertyValidatorBuilder<T> property(String propertyName){
		
		return new PropertyValidatorBuilder<T>(this,propertyName);
	}
	
	public Validator<T> validator(){
		return validator;
	}
}
