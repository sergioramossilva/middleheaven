package org.middleheaven.ui;

import java.io.File;

import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.middleheaven.io.xml.XMLUtils;
import org.middleheaven.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUIComponentBuilder extends XMLObjectContructor<UIEnvironment> implements UIComponentBuilder {


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

				UIComponent uiclient = (build(child,null));

				env.addClient((UIClient)uiclient);

			}
		}

		this.setConstructedObject(env);
	}

	private <T extends UIComponent > Class<T> resolveClass(String type){
		try {
			return (Class<T>) ReflectionUtils.loadClass("org.middleheaven.ui.UI" + StringUtils.capaitalize(type));
		} catch (NoSuchClassReflectionException e) {
			return (Class<T>) ReflectionUtils.loadClass("org.middleheaven.ui.components.UI" + StringUtils.capaitalize(type));
		}
	}

	private UIComponent build (Node node , UIComponent parent){

		String type = XMLUtils.getStringAttribute("type", node);
		String familly = XMLUtils.getStringAttribute("familly", node, "default");
		String name = XMLUtils.getStringAttribute("name", node, "");

		if ("default".equals(familly)){
			familly = null;
		}

		UIComponent uiComponent =  GenericUIComponent.getInstance(resolveClass(type), familly);
		if (!name.isEmpty()){
			uiComponent.setGID(name);
		}

		uiComponent.setUIParent(parent);
		if (parent!=null){
			parent.addComponent(uiComponent);
		}
		
		// model TODO read model from xml 
		
		
		
		// children

		NodeList children = node.getChildNodes();
		for (int i=0; i < children.getLength(); i++){
			Node child = children.item(i);
			if (child.getNodeName().equals("uic")){
				build(child,uiComponent);

			}
		}

		return uiComponent;
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
