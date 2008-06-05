package org.middleheaven.ui;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.ui.rendering.RenderType;

public class GenericUIComponent implements UIContainer,UILayout{

	static private int nextID=0;
	
	private Set<UIComponent> children = new HashSet<UIComponent>();
	private UILayout layout = null;
	private String id;
	private String familly; 
	private RenderType renderType;
	private UIModel model;
	private UIComponent parent;
	private boolean visible = true;
	private boolean enable = true;
	
	
	public GenericUIComponent(RenderType renderType, String familly){
		this.renderType = renderType;
		this.familly = familly;
		this.id = Integer.toString(nextID++);
		if (!renderType.equals(RenderType.LAYOUT)){
			layout = new GenericUIComponent(RenderType.LAYOUT,"border");
		}

	}
	
	// Special Methods for genericUIComponent 
	
	public GenericUIComponent addComponent(RenderType renderType, String familly){
		return new GenericUIComponent( renderType,  familly);
	}
	
	public boolean equals(UIComponent other){
		return this.id.equals(other.getID());
	}
	
	public int hashCode(){
		return this.id.hashCode();
	}
	
	public String toString(){
		return this.getClass().getSimpleName() + "." + this.renderType + "." +  id;
	}
	
	@Override
	public void addChildComponent(UIComponent component, Object layoutConstrain) {
		if (layout!=null){
			layout.addChildComponent(component,layoutConstrain);
		} else {
			children.add(component);
		}
	}
	
	@Override
	public void addChildComponent(UIComponent component) {
		children.add(component);
	}
	
	@Override
	public void removeChildComponent(UIComponent component) {
		children.remove(component);
	}

	@Override
	public UILayout getLayout() {
		return layout;
	}

	@Override
	public void setLayout(UILayout component) {
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
	public Set<UIComponent> getChildrenComponents() {
		return Collections.unmodifiableSet(this.children);
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
	public String getID() {
		return id;
	}

	@Override
	public RenderType getType() {
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

	@Override
	public void setEnabled(boolean enabled) {
		this.enable = enabled;
	}

	@Override
	public void setFamily(String familly) {
		this.familly = familly;
	}

	@Override
	public void setID(String id) {
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
		return 100;
	}

	@Override
	public int getWidth() {
		return 100;
	}


}
