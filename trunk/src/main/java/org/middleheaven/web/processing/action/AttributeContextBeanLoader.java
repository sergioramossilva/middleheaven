package org.middleheaven.web.processing.action;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Enumeration;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.global.text.LocalizationService;
import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.ui.AttributeContext;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.CulturalAttributeContext;
import org.middleheaven.util.coersion.StringCalendarDateTimeCoersor;
import org.middleheaven.util.coersion.StringDateCoersor;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.web.annotations.In;

public class AttributeContextBeanLoader {

	public <T> T loadBean(CulturalAttributeContext context, Class<T> type ){
		return loadBean(context, type, new Annotation[0]);
	}
	
	public <T> T loadBean(CulturalAttributeContext context, Class<T> type ,Annotation[] annotations) {
		try{
			// set up timestamp formatters and converter
			LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

			TimepointFormatter formatter = i18nService.getTimestampFormatter(context.getCulture());
			
			formatter.setPattern(TimepointFormatter.Format.DATE_ONLY);
			
			TypeCoercing.addCoersor(String.class, Date.class, new StringDateCoersor(formatter));
			TypeCoercing.addCoersor(String.class, CalendarDateTime.class, new StringCalendarDateTimeCoersor(formatter));

			
			String name = "";
			ContextScope inScope =null;
			In in = getAnnotation(annotations,In.class);
			if (in!=null){
				name = in.value();
				inScope = in.scope();
				if(name.isEmpty()){
					throw new IllegalArgumentException("Cannot inject an unamed parameter");
				}
				return context.getAttribute(inScope, name, type);

			} else {
				if (isPrimitive(type)){
					throw new IllegalArgumentException("Use @In for instances of " + type);
				}

				T object = null;
				
				// search in the contexts in reverse order to find an object of class type
				ContextScope[] scopes = new ContextScope[]{
						ContextScope.APPLICATION,
						ContextScope.SESSION,
						ContextScope.REQUEST};


				for (ContextScope scope : scopes){
					Enumeration<String> names = context.getAttributeNames(scope);
					while (names.hasMoreElements()){
						Object candidate = context.getAttribute(scope, names.nextElement(), null);
						if (candidate!=null && type.isAssignableFrom(candidate.getClass())){
							object = type.cast(candidate);
							break;
						}
					}
				}

				if (object==null){

					// try to load from parameters
					object =  new ContextAssembler(
							ServiceRegistry.getService(WiringService.class).getObjectPool(), 
							context,
							ContextScope.PARAMETERS).assemble(type);
				}
				
				return object;
			}
		
		} finally {
			TypeCoercing.removeCoersor(String.class, Date.class);
			TypeCoercing.removeCoersor(String.class, CalendarDateTime.class);

		}



	}
	
	private <A extends Annotation> A getAnnotation(Annotation[] annotations ,Class<A> annotationClass){
		if (annotations !=null){
			for (Annotation a : annotations){
				if(annotationClass.isAssignableFrom(a.getClass())){
					return annotationClass.cast(a);
				}
			}
		}
		return null;
	}
	
	
	private boolean isPrimitive(Class<?> type){
		return type.isPrimitive() || 
		String.class.isAssignableFrom(type) || 
		Date.class.isAssignableFrom(type)|| 
		Number.class.isAssignableFrom(type);
	}
}
