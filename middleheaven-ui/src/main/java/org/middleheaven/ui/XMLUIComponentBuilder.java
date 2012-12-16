package org.middleheaven.ui;

import java.io.File;
import java.util.Collection;

import org.middleheaven.core.annotations.Shared;
import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyBagProxyHandler;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.BindingNotFoundException;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.middleheaven.io.xml.XMLUtils;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.AbstractUIFieldInputModel;
import org.middleheaven.ui.models.UIClientModel;
import org.middleheaven.ui.models.UIFieldInputModel;
import org.middleheaven.ui.models.UIFlowLayoutModel;
import org.middleheaven.ui.models.UILayoutModel;
import org.middleheaven.ui.models.UIWindowModel;
import org.middleheaven.ui.models.impl.DefaultUIWindowModel;
import org.middleheaven.ui.models.impl.SimpleUIClientModel;
import org.middleheaven.ui.models.impl.UIBorderLayoutModel;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.Enumerable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUIComponentBuilder extends XMLObjectContructor<UIEnvironment> implements UIComponentBuilder {

	WiringService wiringContext;
	private UIEnvironmentType targetUIEnvironmentType;
	
	public XMLUIComponentBuilder(WiringService wiringContext , UIEnvironmentType targetUIEnvironmentType){
		this.wiringContext = wiringContext;
		this.targetUIEnvironmentType = targetUIEnvironmentType;
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

		//UIEnvironmentType type = UIEnvironmentType.valueOf(XMLUtils.getStringAttribute("type", element).toUpperCase());
		//String name= XMLUtils.getStringAttribute("name", element);

		BuildedUIEnvironment env = new BuildedUIEnvironment(targetUIEnvironmentType);
		//env.setName(name);

		// set clients

		NodeList clients = element.getChildNodes();
		for (int i=0; i < clients.getLength(); i++){
			Node child = clients.item(i);
			if (child.getNodeName().equals("uic")){

				
				UIComponent uiclient = (build(targetUIEnvironmentType, child,null));

				env.setClient((UIClient)uiclient);

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
		String layoutConstraint = XMLUtils.getStringAttribute("layoutConstraint", node, "");

		if ("default".equals(familly)){
			familly = null;
		}

		Class<UIComponent> uiClass = resolveClass(type);

		UIComponent uiComponent =  GenericUIComponent.getInstance(uiClass, familly);
		
		if (!uiClass.isInstance(uiComponent)){
			throw new IllegalStateException("Incompatible ui types");
		}
		
		if (!name.isEmpty()){
			uiComponent.setGID(name);
		}

		uiComponent.setUIParent(parent);
		if (parent!=null){
			if (layoutConstraint.length() > 0 && parent instanceof UILayout){
				((UILayout)parent).addComponent(uiComponent, XMLLayoutConstraintsParser.parseConstraint(layoutConstraint, (UILayoutModel) parent.getUIModel()));
			} else {
				parent.addComponent(uiComponent);
			}
			
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
				
				if (uiComponent instanceof UILayout){
					
					if ("border".equals(familly)){
						uiModel = new UIBorderLayoutModel();
					} else if ("flow".equals(familly)){
						uiModel = new UIFlowLayoutModel();
					} 
					
				}
				
		
			} catch (SecurityException e) {
				throw new ReflectionException(e);
			} catch (NoSuchMethodException e) {
				throw new ReflectionException(e);
			}


			if (uiModel == null){
				try {
					
					// load
					if (UIClientModel.class.isAssignableFrom(uiModelClass)){
						
						uiModel = new SimpleUIClientModel();
						
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
			

		}
		uiComponent.setUIModel(uiModel);

		// inject attributes

		Enumerable<PropertyAccessor> properties = Introspector.of(uiModelClass).inspect().properties().retriveAll();
				
		for (PropertyAccessor p : properties){
			Node pnode = XMLUtils.getChildNode(p.getName().toString(), modelNode);
			if (pnode!=null){
				Object obj = pnode.getFirstChild().getNodeValue();
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
			binder.bind(uiModelClass).in(Shared.class).toInstance(uiModel);
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
