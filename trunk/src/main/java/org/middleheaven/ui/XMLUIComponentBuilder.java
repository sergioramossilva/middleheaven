package org.middleheaven.ui;

import java.io.File;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.middleheaven.io.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUIComponentBuilder extends XMLObjectContructor<UIComponent> implements UIComponentBuilder {


	@Override
	public UIComponent build() {
		return this.getConstructedObject();
	}

	public UIComponent buildFrom(ManagedFile file){
		constructFrom(file);
		return build();
	}
	
	public UIComponent buildFrom(File file){
		constructFrom(file);
		return build();
	}
	
	@Override
	protected void constructFrom(Document document) 
		throws ManagedIOException,XMLException {

		Element element = document.getDocumentElement();
		NodeList children = element.getChildNodes();
		for (int i=0; i < children.getLength(); i++){
			Node child = children.item(i);
			if (child.getNodeName().equals("uic")){
				this.setConstructedObject(build(child,null));
				return;
			}
		}
		
	}

	private GenericUIComponent build (Node node , GenericUIComponent parent){
		
		String type = XMLUtils.getStringAttribute("type", node);
		String familly = XMLUtils.getStringAttribute("familly", node, "default");
		String name = XMLUtils.getStringAttribute("name", node, "");

		if ("default".equals(familly)){
			familly = null;
		}
		GenericUIComponent uiComponent = new GenericUIComponent(ReflectionUtils.loadClass("org.middleheaven.ui" + type), familly);
		if (!name.isEmpty()){
			uiComponent.setGID(name);
		}
		uiComponent.setUIParent(parent);
		if (parent!=null){
			parent.addComponent(uiComponent);
		}
		NodeList children = node.getChildNodes();
		for (int i=0; i < children.getLength(); i++){
			Node child = children.item(i);
			if (child.getNodeName().equals("uic")){
				build(child,uiComponent);
				
			}
		}
		
		
		return uiComponent;
	}
}
