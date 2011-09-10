package org.middleheaven.ui.desktop.swing;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.middleheaven.core.reflection.bean.BeanBinding;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.events.UIFocusEvent;
import org.middleheaven.ui.events.UIPrespectiveEvent;
import org.middleheaven.ui.models.UIViewModel;
import org.middleheaven.util.collections.DelegatingList;

public class SInternalFrameView extends JInternalFrame implements UIView{

	private String family;
	private String id;
	private UIViewModel model;
	private UIComponent parent;

	
	public SInternalFrameView(){
		
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		
		this.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				model.onFocusGained(new UIFocusEvent(SInternalFrameView.this));
			}

			@Override
			public void focusLost(FocusEvent e) {
				model.onFocusLost(new UIFocusEvent(SInternalFrameView.this));
			}
			
		});
		
		this.addInternalFrameListener(new InternalFrameListener(){

			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				model.onFocusGained(new UIFocusEvent(SInternalFrameView.this));
			}
			
			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				model.onFocusLost(new UIFocusEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				model.onOpened(new UIPrespectiveEvent(SInternalFrameView.this));
			}
			
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				model.onClosed(new UIPrespectiveEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				model.onClosing(new UIPrespectiveEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameDeiconified(InternalFrameEvent e) {
				model.onDeiconified(new UIPrespectiveEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameIconified(InternalFrameEvent e) {
				model.onIconified(new UIPrespectiveEvent(SInternalFrameView.this));
			}


		});
	}
	@Override
	public void setUIModel(UIModel model) {
		this.model = (UIViewModel)model;
		this.setTitle(this.model.getTitle());
		
		BeanBinding.bind(this.model, this);
	}
	
	@Override
	public UIViewModel getUIModel() {
		return model;
	}

	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		
		if (component instanceof JMenuBar){
			this.setJMenuBar((JMenuBar)component);
		} else {
			this.getContentPane().add((JComponent)component);
		}
	}
	
	
	@Override
	public void removeComponent(UIComponent component) {
		this.getContentPane().remove((JComponent)component);
	}

	@Override
	public void gainFocus() {
		try {
			this.setSelected(true);
		} catch (PropertyVetoException e) {
			// can-t select at this time
		}
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return (UIComponent)getContentPane().getComponent(index);
			}

			@Override
			public int size() {
				return getContentPane().getComponentCount();
			}
			
		};
	}

	@Override
	public int getChildrenCount() {
		return this.getContentPane().getComponentCount();
	}

	@Override
	public String getFamily() {
		return family;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UIView.class;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isRendered() {
		return true;
	}


	@Override
	public void setFamily(String family) {
		this.family = family;
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}



	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UIDimension getDimension() {
		return new UIDimension(this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return new UIPosition(this.getX(), this.getY());
	}


	@Override
	public void setSize(UIDimension size) {
		this.setSize(new Dimension(size.getWidth(), size.getHeight()));
	}

}
