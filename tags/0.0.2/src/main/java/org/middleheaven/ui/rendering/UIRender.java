package org.middleheaven.ui.rendering;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.middleheaven.ui.UIComponent;

public abstract class UIRender implements Serializable{


	EnumMap<DecorationPhase, Set<UIDecorator>> decorators = new EnumMap<DecorationPhase, Set<UIDecorator>>(DecorationPhase.class);

	/**
	 * Is this renderer responsible for rendering children components
	 * @return
	 */
	public boolean isChildrenRenderer(RenderingContext context, UIComponent parent,UIComponent component) {
		return false;
	}
	
	public void addDecorator(UIDecorator decorator, DecorationPhase phase){

		Set<UIDecorator> phaseList = decorators.get(phase);
		if (phaseList==null){
			phaseList = new LinkedHashSet<UIDecorator>();
			decorators.put(phase, phaseList);
		}
		phaseList.add(decorator);
	}

	public final UIComponent render(RenderingContext context , UIComponent parent,UIComponent component){
		UIComponent c = decorate(context,component,DecorationPhase.PRE_RENDER);
		UIComponent b = build (context,parent, c);

		return decorate(context,b,DecorationPhase.POS_RENDER);

	}

	private UIComponent decorate(RenderingContext context , UIComponent component, DecorationPhase phase){

		Set<UIDecorator> decoratorsSet = this.decorators.get(phase);

		if (decoratorsSet==null){
			return component;
		}
		
		
		UIComponent decoratedComponent = component;

		for (UIDecorator decorator : decoratorsSet ){
			decoratedComponent = decorator.decorate(context, decoratedComponent);
		}
		return decoratedComponent;

	}

	protected abstract UIComponent build(RenderingContext context , UIComponent parent, UIComponent component);



}
