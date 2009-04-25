package org.middleheaven.ui.web.tags;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.global.text.LocalizationService;
import org.middleheaven.util.classification.BooleanClassifier;

public class MenuItem {
	
	private String title;
	private String url;
	private MenuItem parent;
	private final List<MenuItem> children = new LinkedList<MenuItem>();
	private boolean checked=false;
	private boolean open=false;
	private boolean visible=false;
	private Object userObject;
	private boolean isTitleLocalized;
	private String label;
	
	public MenuItem() { }
	
	public MenuItem(String title) {
		this(title,true);
	}
	
	public MenuItem(String title, boolean isTitleLocalized) {
		this (title, isTitleLocalized, null,null);
	}

	public MenuItem(String title, boolean isTitleLocalized, String url) {
		this( title ,  isTitleLocalized , url , null);
	}
	
	public MenuItem(String title, boolean isTitleLocalized, String url, Object menuObject) {
		this.title = title;
		this.isTitleLocalized = isTitleLocalized;
		this.url = url;
		this.userObject = menuObject;
	}	
	
	public MenuItem(String title, String url) {
		this(title,true,url);
	}
	
	public String getLabel() {
		return label;
	}

	void setLabel(String label) {
		this.label = label;
	}

	public String getTitle(){
		return title; 
	}
	
	protected boolean isTitleLocalized(){
		return this.isTitleLocalized;
	}
	
	public String getUrl(){
		return url;
	}
	
	public List<MenuItem> getChildren(){
		return Collections.unmodifiableList(children);
	}
	
	public int getChildrenCount(){
		return children.size();
	}
	
	public MenuItem getParent(){
		return parent;  
	}
	
	public int getLevel(){
		return resolveLevel(this);
	}
	
	public Object getUserObject(){
		return userObject;
	}
	
	private int resolveLevel(MenuItem item){
		if (item.parent==null){
			return 0;
		} else {
			return 1 + resolveLevel(item.parent);
		}
	}
	
	public boolean isChecked(){
		return checked;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public MenuItem add(MenuItem child){
		if (child.parent!=null){
			throw new IllegalArgumentException("Menu item is already assigned to a parent menu");
		}
		
		child.parent = this;
		this.children.add(child);
		
		if(!this.visible && child.isVisible()){
			this.visible = true;
			
		}
		
		return this;
	}
	
	public void removeChild(MenuItem child){
		if (this.equals(child.parent)){
			child.parent = null;
			this.children.remove(child);
		}
	}
	
	public void removeAllChildren(List<MenuItem> childs) {
		for (MenuItem item : childs){
			item.removeChild(item);
		}
	}
	
	public void setChecked(boolean checked){
		this.checked = checked;
	}
	
	public void setTreeOpen(boolean open){

		this.open = open;
		
		if(parent != null){
			parent.setTreeOpen(open);
		}
	}
	
	public void setUserObject(Object menuObject){
		this.userObject = menuObject;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		toString(builder,this,0);
		return builder.toString();
	}
	
	private static void toString(StringBuilder builder , MenuItem item, int tabs){
		for (int i=0; i < tabs; i++){
			builder.append("   ");
		}
		builder.append(item.getTitle())
			.append(item.isChecked() ? " [CHECKED]" : "")
			.append(item.isOpen() ? " [OPEN]" : "")
			.append(item.isVisible() ? " [VISIBLE]" : "")
			.append("[URL: "+item.getUrl()+"]")
			.append("\n");
		for (MenuItem m : item.getChildren()){
			toString(builder,m, tabs+1);
		}
	}

}
