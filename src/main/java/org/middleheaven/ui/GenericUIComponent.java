package org.middleheaven.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.core.reflection.DefaultMethodDelegator;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UIInput;
import org.middleheaven.ui.components.UILayout;

public class GenericUIComponent<T extends UIComponent> implements UIContainer,UILayout{

	static private int nextID=0;
	
	private List<UIComponent> children = new CopyOnWriteArrayList<UIComponent>();
	private UILayout layout = null;
	private String id;
	private String familly; 
	private Class<T> renderType;
	private UIModel model;
	private UIComponent parent;
	private boolean visible = true;
	private boolean enable = true;

	private int x;
	private int y;

	private int height;
	private int width;
	
	public static <T extends UIComponent>  T getInstance(Class<T> renderType, String familly){
		return ReflectionUtils.proxy(new GenericUIComponent(renderType, familly), renderType);
	}
	
	public GenericUIComponent(Class<T> renderType, String familly){
		this.renderType = renderType;
		this.familly = familly;
		this.id = Integer.toString(nextID++);
		if (renderType.equals(UIContainer.class)){
			layout = new GenericUIComponent(UILayout.class,"border");
		}

	}
	
	// Special Methods for genericUIComponent 
	
	public  GenericUIComponent addComponent(Class<T> renderType, String familly){
		return new GenericUIComponent( renderType,  familly);
	}
	
	public boolean equals(Object other){
		return other instanceof UIComponent && this.id.equals(((UIComponent)other).getGID());
	}
	
	
	public int hashCode(){
		return this.id.hashCode();
	}
	
	public String toString(){
		return this.getClass().getSimpleName() + "." + this.renderType + "." +  id;
	}
	
	@Override
	public void addChildComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		if (layout!=null){
			layout.addChildComponent(component,layoutConstrain);
		} else {
			children.add(component);
		}
	}
	
	@Override
	public void addComponent(UIComponent component) {
		children.add(component);
	}
	
	@Override
	public void addComponent(UIComponent component,UILayoutConstraint layoutConstrain) {
		children.add(component);
	}

	@Override
	public void removeComponent(UIComponent component) {
		children.remove(component);
	}

	@Override
	public UILayout getUIContainerLayout() {
		return layout;
	}

	@Override
	public void setUIContainerLayout(UILayout component) {
		this.layout = component;
	}



	@Override
	public Set<UIComponent> findComponents(UIQuery query) {
		return query.execute(this);
	}

	@Override
	public void gainFocus() {
		//no-op
	}

	@Override
	public boolean hasFocus() {
		return false;
	}
	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.unmodifiableList(this.children);
	}

	@Override
	public int getChildrenCount() {
		return children.size();
	}

	@Override
	public String getFamily() {
		return familly;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public Class<T> getType() {
		return renderType;
	}

	@Override
	public UIModel getUIModel() {
		return model;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isEnabled() {
		return this.enable;
	}

	@Override
	public boolean isRendered() {
		return true;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	public void setEnabled(boolean enabled) {
		this.enable = enabled;
	}

	@Override
	public void setFamily(String familly) {
		this.familly = familly;
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}

	@Override
	public void setUIModel(UIModel model) {
		this.model = model;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}




	@Override
	public UIDimension getDimension() {
		return new UIDimension(this.width,this.height);
	}

	@Override
	public UIPosition getPosition() {
		return new UIPosition(x,y);
	}

	@Override
	public void setSize(UIDimension size) {
		this.width = size.getWidth();
		this.height = size.getHeight();
	}


}
