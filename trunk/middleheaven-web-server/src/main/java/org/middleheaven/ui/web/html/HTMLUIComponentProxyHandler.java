/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class HTMLUIComponentProxyHandler implements ProxyHandler {

	private HtmlUIComponent original;
	private UIComponent parent;
	private final Map<String , Object> properties = new HashMap<String,Object>();

	/**
	 * Constructor.
	 * @param original
	 */
	public HTMLUIComponentProxyHandler(HtmlUIComponent original) {
		this.original = original;
	}


	public Object getWrappedObject(){
		return original;
	}

	@Override
	public final Object invoke(Object self, Object[] args, MethodDelegator delegator) throws Throwable {

		if (delegator.getName().equals("isType")){
			return original.isType((Class<? extends UIComponent>) args[0]);

		} else if (delegator.getName().equals("getType")){
			return original.getComponentType();

		} else if (delegator.getName().equals("getUIModel")){
			return original.getUIModel();

		} else if (delegator.getName().equals("getUIParent")){
			return parent;

		} else if (delegator.getName().equals("setUIParent")){
			this.parent = (UIComponent) args[0];
			return null;
		} else if (delegator.getName().equals("writeTo")){
			this.original.abstractHTMLRender.write((HtmlDocument)args[0], (RenderingContext) args[1], (UIComponent) self);
			return null;
		} else if (delegator.hasSuper()){
			try{
				return delegator.invokeSuper(self, args);  // execute the original method.
			} catch (NoSuchMethodError e){
				//no-op
			}
			return null;
		} else {
			String name= delegator.getName();
			if(name.startsWith("get")) {
				return properties.get(name.substring(3).toLowerCase());
			} else if(name.startsWith("is")) {
				return properties.get(name.substring(2).toLowerCase());
			} else if(name.startsWith("set") && args.length>0) {
				return properties.put(name.substring(3).toLowerCase(), args[0]);
			} else {
				return doOriginal(args, delegator);
			}	
		}
	} 



	protected final Object doOriginal(Object[] args, MethodDelegator delegator) throws Throwable{
		return delegator.invoke(original, args);  // execute the original method.
	}


}
