package org.middleheaven.ui;

import java.io.File;
import java.util.Collection;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyBagProxyHandler;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.BindingNotFoundException;
import org.middleheaven.core.wiring.ObjectPool;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.middleheaven.io.xml.XMLUtils;
import org.middleheaven.ui.desktop.swing.SwingRenderKit;
import org.middleheaven.ui.models.AbstractUIFieldInputModel;
import org.middleheaven.ui.models.DefaultUIWindowModel;
import org.middleheaven.ui.models.DesktopClientModel;
import org.middleheaven.ui.models.UIClientModel;
import org.middleheaven.ui.models.UIFieldInputModel;
import org.middleheaven.ui.models.UIWindowModel;
import org.middleheaven.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUIComponentBuilder extends XMLObjectContructor<UIEnvironment> implements UIComponentBuilder {

	ObjectPool wiringContext;
	
	public XMLUIComponentBuilder(ObjectPool wiringContext){
		this.wiringContext = wiringContext;
	}

	@Override
	public UIEnvironment build() {
		return this.getConstructedObject();
	}

	public UIEnvironment buildFrom(ManagedFile file){
		constructFrom(file);
		return build();
	}

	public UIEnvironment buildFrom(File file){
		constructFrom(file);
		return build();
	}

	@Override
	protected void constructFrom(Document document) 
	throws ManagedIOException,XMLException {

		// create environment and client
		Element element = document.getDocumentElement();

		UIEnvironmentType type = UIEnvironmentType.valueOf(XMLUtils.getStringAttribute("type", element).toUpperCase());
		String name= XMLUtils.getStringAttribute("name", element);

		BuildedUIEnvironment env = new BuildedUIEnvironment(type);
		env.setName(name);

		// set clients

		NodeList clients = element.getChildNodes();
		for (int i=0; i < clients.getLength(); i++){
			Node child = clients.item(i);
			if (child.getNodeName().equals("uic")){

				UIComponent uiclient = (build(type,child,null));

				env.addClient((UIClient)uiclient);

			}
		}

		this.setConstructedObject(env);
	}

	private Class<UIComponent> resolveClass(String type){
		if (type.equals("commandset")){
			type = "CommandSet";
		}
		try {
			return Introspector.of(UIComponent.class).load("org.middleheaven.ui.UI" + StringUtils.capitalizeFirst(type)).getIntrospected();
		} catch (NoSuchClassReflectionException e) {
			return Introspector.of(UIComponent.class).load("org.middleheaven.ui.components.UI" + StringUtils.capitalizeFirst(type)).getIntrospected();
		}
	}

	private UIComponent build (UIEnvironmentType envType, Node node , UIComponent parent){

		String type = XMLUtils.getStringAttribute("type", node);
		String familly = XMLUtils.getStringAttribute("familly", node, "default");
		String name = XMLUtils.getStringAttribute("name", node, "");

		if ("default".equals(familly)){
			familly = null;
		}

		Class<UIComponent> uiClass = resolveClass(type);

		UIComponent uiComponent =  GenericUIComponent.getInstance(uiClass, familly);
		
		if (!uiClass.isInstance(uiComponent)){
			throw new Error();
		}
		
		if (!name.isEmpty()){
			uiComponent.setGID(name);
		}

		uiComponent.setUIParent(parent);
		if (parent!=null){
			parent.addComponent(uiComponent);
		}

		// model read model from xml 

		Node modelNode = XMLUtils.getChildNode("model", node);
		UIModel uiModel = null;
		Class<? extends UIModel> uiModelClass = null;

		// is model node is defined
		if (modelNode!=null){
			// if class attribute is defined
			String modelClass = XMLUtils.getStringAttribute("class", modelNode, "");
			if (!modelClass.isEmpty()){
				ClassIntrospector<UIModel> modelIntrospector = Introspector.of(UIModel.class).load(modelClass);
				uiModelClass = modelIntrospector.getIntrospected();
				try{
					uiModel = wiringContext.getInstance(uiModelClass);
				} catch (BindingNotFoundException e){
					uiModel = modelIntrospector.newInstance();
					wiringContext.addConfiguration(new ModelBinderConfiguration(uiModelClass, uiModel));
				}
				
				
			}	
		}

		// is not model is yet defined
		if (uiModel==null){
			try {
				// infer class from contract
				uiModelClass = (Class<? extends UIModel>) uiClass.getMethod("getUIModel", new Class[0]).getReturnType();
			} catch (SecurityException e) {
				throw new ReflectionException(e);
			} catch (NoSuchMethodException e) {
				throw new ReflectionException(e);
			}


			try {
			
					// load
					if (UIClientModel.class.isAssignableFrom(uiModelClass)){
						if (envType.equals(UIEnvironmentType.DESKTOP)){
							uiModel = new DesktopClientModel(){

								@Override
								public UIComponent defineMainWindow(UIClient client,AttributeContext context) {
									return client.getChildrenComponents().get(client.getChildrenCount()-1);
								}

								@Override
								public UIComponent defineSplashWindow(UIClient client,AttributeContext context) {
									return client.getChildrenCount() == 1 ? null : client.getChildrenComponents().get(0);
								}

							};
						} else if (envType.equals(UIEnvironmentType.BROWSER)){
							uiModel = null; // TODO
						}  
					} else if (UIWindowModel.class.isAssignableFrom(uiModelClass)) { 
						uiModel = new DefaultUIWindowModel();
					} else if (UIFieldInputModel.class.isAssignableFrom(uiModelClass)){
						uiModel = new AbstractUIFieldInputModel(){};
					} else {
						uiModel = Introspector.of(uiModelClass).newProxyInstance(new PropertyBagProxyHandler());
					}
					
			
			} catch (SecurityException e) {
				throw new ReflectionException(e);
			} 

		}
		uiComponent.setUIModel(uiModel);

		// inject attributes

		Collection<PropertyAccessor> properties = Introspector.of(uiModelClass).inspect().properties().retriveAll();

		for (PropertyAccessor p : properties){
			Node pnode = XMLUtils.getChildNode(p.getName().toString(), modelNode);
			if (pnode!=null){
				Object obj = pnode.getFirstChild().getNodeValue();
				if (p.getName().equals("renderkit")){
					if (obj.toString().equalsIgnoreCase("SwingRenderKit")){
						obj = new SwingRenderKit();
					}
				}
				p.setValue(uiModel, obj);
			}
		}

		// children

		NodeList children = node.getChildNodes();
		for (int i=0; i < children.getLength(); i++){
			Node child = children.item(i);
			if (child.getNodeName().equals("uic")){
				build(envType,child,uiComponent);

			}
		}

		return uiComponent;
	}

	
	private static class ModelBinderConfiguration <T> implements BindConfiguration{
		Class<T> uiModelClass;
		T uiModel;
		
		public  ModelBinderConfiguration(Class<T> uiModelClass , T uiModel){
			this.uiModel = uiModel;
			this.uiModelClass = uiModelClass;
		}
		
		@Override
		public void configure(Binder binder) {
			binder.bind(uiModelClass).toInstance(uiModel);
		}
		
	}
	public static class BuildedUIEnvironment extends UIEnvironment{


		public BuildedUIEnvironment(UIEnvironmentType type) {
			super(type);
		}

		@Override
		protected boolean accept(Class<? extends UIClient> type) {
			return true; // TODO verify agains type
		}

	}

}
