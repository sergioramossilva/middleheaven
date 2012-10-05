package org.middleheaven.ui.rendering;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.middleheaven.ui.UIComponent;

/**
 * Defines the renderization of {@link UIComponent}s.
 * The rendering processing is the transformation of generic {@link UIComponent}s in {@link RenderKit} specific {@link UIComponent}.
 * The visualization is then handled by the {@link RenderKit} specific {@link UIComponent}.
 */
public abstract class UIRender implements Serializable{


	private static final long serialVersionUID = -6018457073198859268L;
	
	EnumMap<DecorationPhase, Set<UIDecorator>> decorators = new EnumMap<DecorationPhase, Set<UIDecorator>>(DecorationPhase.class);

	/**
	 * Is this renderer responsible for rendering children components
	 * @return
	 */
	public boolean isChildrenRenderer(RenderingContext context, UIComponent parent,UIComponent component) {
		return false;
	}
	
	/**
	 * Adds a {@link UIDecorator} to be used at a specific {@link DecorationPhase}.
	 * @param decorator the decorator to use.
	 * @param phase the phase to use the decorator
	 */
	public void addDecorator(UIDecorator decorator, DecorationPhase phase){

		Set<UIDecorator> phaseList = decorators.get(phase);
		if (phaseList==null){
			phaseList = new LinkedHashSet<UIDecorator>();
			decorators.put(phase, phaseList);
		}
		phaseList.add(decorator);
	}

	/**
	 * Render de component, i.e. obtains a {@link RenderKit} specific {@link UIComponent}.
	 * 
	 * @param context the rendering context.
	 * @param parent the component parent. This component is a rendered component.
	 * @param component the component to render. This is a generic component.
	 * @return the rendered component.
	 */
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

	/**
	 * Atually build a {@link RenderKit} specific component from the given component.
	 * @param context the current rendering context.
	 * @param parent the component's parent. This is a already rendered component.
	 * @param component the component to render.
	 * 
	 * @return the rendered component.  
	 */
	protected abstract UIComponent build(RenderingContext context , UIComponent parent, UIComponent component);



}
