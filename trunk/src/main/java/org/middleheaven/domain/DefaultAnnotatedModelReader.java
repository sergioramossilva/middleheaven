package org.middleheaven.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.domain.annotations.Key;
import org.middleheaven.domain.annotations.ManyToMany;
import org.middleheaven.domain.annotations.ManyToOne;
import org.middleheaven.domain.annotations.OneToMany;
import org.middleheaven.domain.annotations.OneToOne;
import org.middleheaven.domain.annotations.Temporal;
import org.middleheaven.domain.annotations.Transient;
import org.middleheaven.domain.annotations.Unique;
import org.middleheaven.domain.annotations.Version;
import org.middleheaven.logging.Logging;
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

	private <E> FieldModelBuilder processField (PropertyAccessor pa ,EntityModelBuilder<E> em){

		FieldModelBuilder fm = em.getField(pa.getName())
		.setTransient(pa.isAnnotatedWith(Transient.class))
		.setVersion( pa.isAnnotatedWith(Version.class))
		.setUnique( pa.isAnnotatedWith(Unique.class))
		.setValueType(pa.getValueType());

		if(pa.isAnnotatedWith(Key.class)){
			Key key = pa.getAnnotation(Key.class);

			fm.setIdentity(true);
			
			em.setIdentityType(key.type());

		}

		if (pa.isAnnotatedWith(ManyToOne.class)){
			fm.setDataType(DataType.MANY_TO_ONE);
			ManyToOne ref = pa.getAnnotation(ManyToOne.class);
			String fieldName = ref.targetIdentityField();
			if (fieldName.isEmpty()){
				fieldName = fm.getName();
			}
			fm.putParam("targetField", fieldName);

		} else if (pa.isAnnotatedWith(OneToOne.class)){
			fm.setDataType(DataType.ONE_TO_ONE);
			OneToOne ref = pa.getAnnotation(OneToOne.class);
			String fieldName = ref.targetIdentityField();
			if (fieldName.isEmpty()){
				fieldName = fm.getName();
			}
			fm.putParam("targetField", fieldName);
		} else if (pa.isAnnotatedWith(OneToMany.class)){
			fm.setDataType( DataType.ONE_TO_MANY);
			OneToMany ref = pa.getAnnotation(OneToMany.class);
			Class target = ref.target();

		} else if (pa.isAnnotatedWith(ManyToMany.class)){
			fm.setDataType(DataType.MANY_TO_MANY);
			ManyToMany ref = pa.getAnnotation(ManyToMany.class);
			Class target = ref.target();

		}

		Class<?> valueType = pa.getValueType();

		if ( fm.getDataType() == null){
			if (matchTypes(valueType, String.class) ){
				fm.setDataType(DataType.TEXT);
			} else if (matchTypes(valueType,Integer.class ,int.class, Byte.class, byte.class, Short.class, short.class)  ){
				fm.setDataType(DataType.INTEGER);
			} else if (matchTypes(valueType,Identity.class)  ){
				fm.setDataType(DataType.IDENTITY);
			} else if (matchTypes(valueType,CalendarDate.class)){
				fm.setDataType(DataType.DATE);
			} else if (matchTypes(valueType,CalendarDateTime.class)){
				fm.setDataType(DataType.DATETIME);
				if (pa.isAnnotatedWith(Temporal.class)){
					fm.setDataType(pa.getAnnotation(Temporal.class).value());
				} 
			} else if (matchTypes(valueType, Date.class,Calendar.class)){
				if (pa.isAnnotatedWith(Temporal.class)){
					fm.setDataType(pa.getAnnotation(Temporal.class).value());
				} else {
					Logging.warn(valueType.getName() + 
							" is to a too generic timestamp type. Consider annotated " + 
							fm.getName() + " with @Temporal or use an instance of " + 
							TimePoint.class.getName()
					);
					fm.setDataType(DataType.DATETIME);
				}
			} else if (matchTypes(valueType,Boolean.class,boolean.class)){
				fm.setDataType(DataType.LOGIC);
			} else if ( matchTypes(valueType,Double.class , double.class , Float.class , float.class, BigDecimal.class)){
				fm.setDataType(DataType.DECIMAL);
			}
		}
		
		return fm;
	}


	@Override
	public void read(Class<?> type, ModelBuilder builder) {
		EntityModelBuilder em = builder.getEntity(type);

		Iterable<PropertyAccessor> propertyAccessors = ReflectionUtils.getPropertyAccessors(type);

		if (!propertyAccessors.iterator().hasNext()){
			throw new ModelingException("No public fields found for entity " + type.getName() + ".");
		} else {
		
			for (PropertyAccessor pa : propertyAccessors){

				processField(pa,em);
				
			}


		}
	}



}
