package org.middleheaven.ui;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.UILayoutModel;
import org.middleheaven.ui.rendering.RenderKit;

/**
 * Generic, {@link RenderKit} agnostic representation of a {@link UIComponent}.
 * 
 * @param <T> the {@link UIComponent} represented by the object.
 */
public class GenericUIComponent<T extends UIComponent> implements UIContainer {

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

	private UIDimension x = UIDimension.pixels(0);
	private UIDimension y = UIDimension.pixels(0);

	private UIDimension height;
	private UIDimension width;
	
	/**
	 * 
	 * @param uiClass
	 * @param familly
	 * @return
	 */
	public static <T extends UIComponent>  T getInstance(Class<T> uiClass, String familly){
		
		final GenericUIComponent object = new GenericUIComponent( uiClass, familly);
		final GenericUIComponentProxyHandler proxyHandler = new GenericUIComponentProxyHandler(object,uiClass);
		GenericUIComponent uic= (GenericUIComponent)Introspector.of(object).newProxyInstance(proxyHandler,uiClass);
		
		return uiClass.cast(uic);
	}
	
	/**
	 * 
	 * Constructor.
	 */
	public GenericUIComponent(){
		this.id = Integer.toString(nextID++);
	}
	
	/**
	 * 
	 * Constructor.
	 * @param renderType
	 * @param familly
	 */
	protected GenericUIComponent(Class<T> renderType, String familly){
		this();
		this.renderType = renderType;
		this.familly = familly;
		
		if (renderType.equals(UIContainer.class)){
			layout = getInstance(UILayout.class,"border");
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
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
	public void addComponent(UIComponent component, UILayoutConstraint constraint) {
		children.add(component);
		
		if ( this.model instanceof UILayoutModel){
			((UILayoutModel) model).componentAdded(new ComponentAggregationEvent(component, constraint));
		} 
	}
	
	@Override
	public void addComponent(UIComponent component) {
		children.add(component);
		
		if ( this.model instanceof UILayoutModel){
			((UILayoutModel) model).componentAdded(new ComponentAggregationEvent(component, null));
		} 
	}

	@Override
	public void removeComponent(UIComponent component) {
		children.remove(component);
		
		if ( this.model instanceof UILayoutModel){
			((UILayoutModel) model).componentRemoved(new ComponentAggregationEvent(component, null));
		} 
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
	public Class<T> getComponentType() {
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
		return false;
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
	public UISize getDisplayableSize() {
		return UISize.valueOf(this.width,this.height);
	}

	@Override
	public UIPosition getPosition() {
		return UIPosition.valueOf(x, y);
	}

	@Override
	public void setDisplayableSize(UISize size) {
		this.width = size.getWidth();
		this.height = size.getHeight();
	}


}
