package org.middleheaven.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.domain.annotations.Key;
import org.middleheaven.domain.annotations.ManyToMany;
import org.middleheaven.domain.annotations.ManyToOne;
import org.middleheaven.domain.annotations.OneToMany;
import org.middleheaven.domain.annotations.OneToOne;
import org.middleheaven.domain.annotations.Temporal;
import org.middleheaven.domain.annotations.Transient;
import org.middleheaven.domain.annotations.Unique;
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
		EntityModelBuilder<?> em = builder.getEntity(type);

		Collection<PropertyAccessor> propertyAccessors = Introspector.of(type).inspect().properties().retriveAll();
		
		if (propertyAccessors.isEmpty()){
			throw new ModelingException("No public fields found for entity " + type.getName() + ".");
		} else {
		
			for (PropertyAccessor pa : propertyAccessors){

				processField(pa,em,builder);
				
			}

		}

	}
	
	private FieldModelBuilder processField (PropertyAccessor pa ,EntityModelBuilder<?> em,ModelBuilder builder){

		FieldModelBuilder fm = em.getField(pa.getName())
		.setTransient(pa.isAnnotadedWith(Transient.class))
		.setVersion( pa.isAnnotadedWith(Version.class))
		.setUnique( pa.isAnnotadedWith(Unique.class));
		
		Class<?> valueType = pa.getValueType();

		if(pa.isAnnotadedWith(Key.class)){
			Key key = pa.getAnnotation(Key.class);

			fm.setIdentity(true);
			fm.setUnique(true);
			em.setIdentityType(key.type());

		} else if ( fm.isIdentity() || valueType.isAssignableFrom(Identity.class) ){
			
			fm.setIdentity(true);
			fm.setUnique(true);
			
			em.setIdentityType(valueType.asSubclass(Identity.class));
			
		}

		if (pa.isAnnotadedWith(ManyToOne.class)){
			fm.setDataType(DataType.MANY_TO_ONE);
			ManyToOne ref = pa.getAnnotation(ManyToOne.class);
			String fieldName = ref.targetIdentityField();
			if (fieldName.isEmpty()){
				fieldName = fm.getName();
			}
			ReferenceDataTypeModel model = new ReferenceDataTypeModel(DataType.MANY_TO_ONE);
			model.setTargetFieldName(fieldName);
			model.setTargetType(valueType);
			fm.setDataTypeModel(model);

			
		} else if (pa.isAnnotadedWith(OneToOne.class)){
			fm.setDataType(DataType.ONE_TO_ONE);
			OneToOne ref = pa.getAnnotation(OneToOne.class);
			String fieldName = ref.targetIdentityField();
			if (fieldName.isEmpty()){
				fieldName = fm.getName();
			}
			ReferenceDataTypeModel model = new ReferenceDataTypeModel(DataType.ONE_TO_ONE);
			model.setTargetFieldName(fieldName);
			model.setTargetType(valueType);
			fm.setDataTypeModel(model);

		
		} else if (pa.isAnnotadedWith(OneToMany.class)){
			fm.setDataType( DataType.ONE_TO_MANY);
			OneToMany ref = pa.getAnnotation(OneToMany.class);

			ReferenceDataTypeModel model = new ReferenceDataTypeModel(DataType.ONE_TO_MANY);

			model.setTargetType(ref.target());
			model.setAggregationType(valueType);
			
			fm.setDataTypeModel(model);
			


		} else if (pa.isAnnotadedWith(ManyToMany.class)){
			fm.setDataType(DataType.MANY_TO_MANY);
			ManyToMany ref = pa.getAnnotation(ManyToMany.class);
			
			ReferenceDataTypeModel model = new ReferenceDataTypeModel(DataType.MANY_TO_MANY);

			model.setTargetType(ref.target());
			model.setAggregationType(valueType);
			
			fm.setDataTypeModel(model);
			
		}

		
		if ( fm.getDataType() == null){
			if (matchTypes(valueType, String.class) ){
				fm.setDataType(DataType.TEXT);
				
				TextDataTypeModel model = new TextDataTypeModel();
				fm.setDataTypeModel(model);
				
			} else if (matchTypes(valueType,Integer.class ,int.class, Byte.class, byte.class, Short.class, short.class)  ){
				fm.setDataType(DataType.INTEGER);
			} else if (matchTypes(valueType,Identity.class)  ){
				fm.setDataType(DataType.UNKWON);
			} else if (matchTypes(valueType,CalendarDate.class)){
				fm.setDataType(DataType.DATE);
			} else if (matchTypes(valueType,CalendarDateTime.class)){
				fm.setDataType(DataType.DATETIME);
				if (pa.isAnnotadedWith(Temporal.class)){
					fm.setDataType(pa.getAnnotation(Temporal.class).value());
				} 
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
			} else if (matchTypes(valueType, Identity.class)){
				fm.setDataType( DataType.UNKWON);
				fm.setIdentity(true);
				em.setIdentityType(valueType.asSubclass(Identity.class));
			} else {
				fm.setDataType(DataType.MANY_TO_ONE);
				
				ReferenceDataTypeModel model = new ReferenceDataTypeModel(DataType.MANY_TO_ONE);

				model.setTargetType(valueType);
				fm.setDataTypeModel(model);
				

			}
		}
		
		return fm;
	}






}
