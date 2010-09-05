package org.middleheaven.domain;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.domain.annotations.EmailAddress;
import org.middleheaven.domain.annotations.Id;
import org.middleheaven.domain.annotations.Length;
import org.middleheaven.domain.annotations.ManyToMany;
import org.middleheaven.domain.annotations.ManyToOne;
import org.middleheaven.domain.annotations.NotEmpty;
import org.middleheaven.domain.annotations.NotNull;
import org.middleheaven.domain.annotations.OneToMany;
import org.middleheaven.domain.annotations.OneToOne;
import org.middleheaven.domain.annotations.Temporal;
import org.middleheaven.domain.annotations.Transient;
import org.middleheaven.domain.annotations.Unique;
import org.middleheaven.domain.annotations.Url;
import org.middleheaven.domain.annotations.Version;
import org.middleheaven.logging.Log;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.util.identity.Identity;

public class DefaultAnnotatedModelReader implements ModelReader {

	private boolean matchTypes(Class<?> candidate , Class<?> ... types){
		for (Class<?> t : types){
			if (candidate.isAssignableFrom(t)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void read(Class<?> type, ModelBuilder builder) {
		
		if (!isEntityType(type)){
			return;
		}
		
		EntityModelBuilder<?> em = builder.getEntity(type);

		Collection<PropertyAccessor> propertyAccessors = Introspector.of(type).inspect().properties().retriveAll();

		if (propertyAccessors.isEmpty()){
			throw new ModelingException("No public properties found for entity " + type.getName() + ".");
		} else {

			for (PropertyAccessor pa : propertyAccessors){

				processField(pa,em, builder);

			}

			if (em.getIdentityField() == null){
				throw new ModelingException("No identity properties found for entity " + type.getName() + ".");
			}
		}

	}


	private boolean isEntityType(Class<?> type) {
		return !type.isEnum();
	}

	private <T> Class<T> resolveValidIdentityType(Class<T> type){
		if (type.isInterface() || Modifier.isAbstract(type.getModifiers())){
			throw new ModelingException("Cannot use interfaces or abstract types as identity type. Please use a concrete type.");
		}

		return type;
	}

	private FieldModelBuilder processField (PropertyAccessor pa ,EntityModelBuilder<?> em, ModelBuilder builder){

		FieldModelBuilder fm = em.getField(pa.getName())
		.setTransient(pa.isAnnotadedWith(Transient.class))
		.setVersion( pa.isAnnotadedWith(Version.class))
		.setUnique( pa.isAnnotadedWith(Unique.class))
		.setNullable(!pa.isAnnotadedWith(NotNull.class))
		;

		Class<?> valueType = pa.getValueType();

		fm.setValueType(valueType);

		if(pa.isAnnotadedWith(Id.class)){
			Id key = pa.getAnnotation(Id.class);

			fm.setIdentity(true);
			fm.setUnique(true);
			fm.setNullable(false);

			Class<?> identityType = mapAsIdentityField(fm,  key.type() , valueType, builder);
			
			
			em.setIdentityType(resolveValidIdentityType(identityType));
			
		} else if (pa.isAnnotadedWith(ManyToOne.class)){

			ManyToOne ref = pa.getAnnotation(ManyToOne.class);
			String fieldName = ref.targetIdentityField();

			mapAsManyToOne(fm,fieldName,valueType,builder);

		} else if (pa.isAnnotadedWith(OneToOne.class)){
			fm.setDataType(DataType.ONE_TO_ONE);
			OneToOne ref = pa.getAnnotation(OneToOne.class);
			String fieldName = ref.targetIdentityField();

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ONE_TO_ONE);

			model.setTargetType(valueType);

			EntityModelBuilder<?> targetModel = builder.getEntity(valueType);

			if (fieldName.isEmpty()){
				fieldName = targetModel.getIdentityField().getName();
			} else {
				// TODO validate field exists
			}

			model.setTargetFieldType(this.resolveValidIdentityType(targetModel.getIdentityType()));

			fm.setDataTypeModel(model);


		} else if (pa.isAnnotadedWith(OneToMany.class)){
			fm.setDataType( DataType.ONE_TO_MANY);
			OneToMany ref = pa.getAnnotation(OneToMany.class);

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ONE_TO_MANY);

			model.setTargetType(ref.target());
			model.setAggregationType(valueType);

			fm.setDataTypeModel(model);



		} else if (pa.isAnnotadedWith(ManyToMany.class)){
			fm.setDataType(DataType.MANY_TO_MANY);
			ManyToMany ref = pa.getAnnotation(ManyToMany.class);

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.MANY_TO_MANY);

			model.setTargetType(ref.target());
			model.setAggregationType(valueType);

			fm.setDataTypeModel(model);

		} else if( pa.getValueType().isEnum()) {
			fm.setDataType(DataType.ENUM);
		}

		if ( fm.getDataType() == null){
			if (matchTypes(valueType, String.class) ){
				fm.setDataType(DataType.TEXT);

				TextDataTypeModel model= new TextDataTypeModel();

				fm.setDataTypeModel(model);

				if(pa.isAnnotadedWith(EmailAddress.class)){
					model.setEmailAddress(true);
					model.setMaxLength(255);
					model.setMinLength(0);

				} else if(pa.isAnnotadedWith(Length.class)){
					Length ref = pa.getAnnotation(Length.class);
					model.setMaxLength(ref.max());
					model.setMinLength(ref.min());
				}

				if(pa.isAnnotadedWith(NotEmpty.class)){
					model.setEmptyable(false);
				}



				if(pa.isAnnotadedWith(Url.class)){
					model.setUrl(true);
				}

			} else if (matchTypes(valueType,Integer.class ,int.class, Byte.class, byte.class, Short.class, short.class)  ){
				fm.setDataType(DataType.INTEGER);
			}  else if (matchTypes(valueType,CalendarDateTime.class)){
				fm.setDataType(DataType.DATETIME);
				if (pa.isAnnotadedWith(Temporal.class)){
					fm.setDataType(pa.getAnnotation(Temporal.class).value());
				} 
			} else if (matchTypes(valueType,CalendarDate.class)){
				fm.setDataType(DataType.DATE);
			} else if (matchTypes(valueType, Date.class,Calendar.class)){
				if (pa.isAnnotadedWith(Temporal.class)){
					fm.setDataType(pa.getAnnotation(Temporal.class).value());
				} else {
					Log.onBookFor(this.getClass()).warn(
							" {0} is to a too generic timestamp type. Consider annotate property {1} with @Temporal or use an instance of {2}", 
							valueType.getName(),
							fm.getName(), 
							TimePoint.class.getName()
					);
					fm.setDataType(DataType.DATETIME);
				}
			} else if (matchTypes(valueType,Boolean.class,boolean.class)){
				fm.setDataType(DataType.LOGIC);
			} else if ( matchTypes(valueType,Double.class , double.class , Float.class , float.class, BigDecimal.class)){
				fm.setDataType(DataType.DECIMAL);
			} else if (matchTypes(valueType, Collection.class, Map.class)){
				fm.setDataType( DataType.ONE_TO_MANY);
			} else {

				Log.onBookFor(this.getClass()).warn("Implicitly composing many-to-one relation for property " + pa.toString());

				mapAsManyToOne(fm, "",valueType,builder);
			}
		}

		return fm;
	}


	private void mapAsManyToOne(FieldModelBuilder fm,String fieldName,Class<?> valueType ,ModelBuilder builder ){

		
		if (valueType.isEnum()){
			fm.setDataType(DataType.ENUM);
			
			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ENUM);

			model.setTargetType(valueType);
			
			fm.setDataTypeModel(model);
		} else if (Identity.class.isAssignableFrom(valueType)){
			mapAsIdentityField(fm, null , valueType, builder);
		} else {
			fm.setDataType(DataType.MANY_TO_ONE);


			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.MANY_TO_ONE);

			model.setTargetType(valueType);
			

			EntityModelBuilder<?> targetModel = builder.getEntity(valueType);

			if (fieldName==null || fieldName.isEmpty()){
				fieldName = targetModel.getIdentityField().getName();
			}else {
				// TODO validate field exists
			}
			model.setTargetFieldName(fieldName);
			model.setTargetFieldType(this.resolveValidIdentityType(targetModel.getIdentityType()));

			fm.setDataTypeModel(model);
		}
	}

	
	private Class<?> mapAsIdentityField (FieldModelBuilder fm,Class<?> idType ,Class<?> valueType, ModelBuilder builder ){
		Class<?> identityType = valueType;
		if(identityType.isInterface() || Modifier.isAbstract(identityType.getModifiers())){
			identityType = idType;
			if(identityType==null || Void.class.equals(identityType)){
				throw new ModelingException("Illegal identity type for " 
						+ fm.toString() 
						+ ".When using interfaces or abstract types as identity type you must specify a non interface, non abstract, class for identity");
			}
		}
		fm.setDataType(DataType.fromClass(identityType));

		
		return identityType;
	}


}
