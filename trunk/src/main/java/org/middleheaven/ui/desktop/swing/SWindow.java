package org.middleheaven.ui.desktop.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.events.UIFocusEvent;
import org.middleheaven.ui.events.UIPrespectiveEvent;
import org.middleheaven.ui.events.UIWindowEvent;
import org.middleheaven.ui.models.UIWindowModel;
import org.middleheaven.util.collections.DelegatingList;

public class SWindow extends JFrame implements UIWindow{

	private UIComponent parent;
	private String id;
	private String family;
	private UIWindowModel model;
	
	public SWindow(){

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addFocusListener(new FocusListener (){

			@Override
			public void focusGained(FocusEvent e) {
				model.onFocusGained(new UIFocusEvent(SWindow.this));
			}

			@Override
			public void focusLost(FocusEvent e) {
				model.onFocusLost(new UIFocusEvent(SWindow.this));
			}
			
		});
		
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent event) {
				//Invoked the first time a window is made visible.
				model.onOpened(new UIPrespectiveEvent(SWindow.this));
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// Invoked when a window has been closed as the result of calling dispose on the window.
				model.onClosed(new UIPrespectiveEvent(SWindow.this));
			}

			@Override
			public void windowClosing(WindowEvent event) {
				// Invoked when the user attempts to close the window from the window's system menu. 
				// If the program does not explicitly hide or dispose the 
				// window while processing this event, the window close operation will be cancelled. 
				model.onClosing(new UIPrespectiveEvent(SWindow.this));
				if (!SWindow.this.isVisible()){
					SWindow.this.dispose();
				}
			}

			@Override
			public void windowDeiconified(WindowEvent event) {
				// Invoked when a window is changed from a minimized to a normal state. 
				model.onDeiconified(new UIPrespectiveEvent(SWindow.this));
			}

			@Override
			public void windowIconified(WindowEvent event) {
				//Invoked when a window is changed from a normal to a minimized state. 
				model.onIconified(new UIPrespectiveEvent(SWindow.this));
			}

			
			@Override
			public void windowActivated(WindowEvent event) {
				// Invoked when the Window is set to be the active Window. 
				// Only a Frame or a Dialog can be the active Window. 
				// The native windowing system may denote the active Window or its children with special decorations,
				// such as a highlighted title bar. 
				// The active Window is always either the focused Window, or the first Frame or Dialog that is an owner 
				// of the focused Window. 
				model.onAtivated(new UIWindowEvent(SWindow.this));
			}
			
			@Override
			public void windowDeactivated(WindowEvent event) {
				// Invoked when a Window is no longer the active Window. 
				// Only a Frame or a Dialog can be the active Window. 
				// The native windowing system may denote the active Window or its children with special decorations, 
				// such as a highlighted title bar. 
				// The active Window is always either the focused Window, or the first Frame or Dialog that is an 
				// owner of the focused Window.
				
				model.onDeativated(new UIWindowEvent(SWindow.this));
			}


		
			
		});
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
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return (UIComponent)getContentPane().getComponent(index);
			}

			@Override
			public int size() {
				return getComponentCount();
			}
			
		};
	}

	@Override
	public int getChildrenCount() {
		return getComponentCount();
	}
	
	@Override
	public void gainFocus() {
		this.requestFocus();
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
		return (Class<T>) UIWindow.class;
	}

	@Override
	public UIWindowModel getUIModel() {
		return model;
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
	public void setUIModel(UIModel model) {
		this.model = (UIWindowModel) model;
		this.setTitle(this.model.getTitle());
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UIPosition getPosition() {
		return new UIPosition(this.getX(),this.getY());
	}

	@Override
	public void setSize(UIDimension size) {
		this.setSize(size.getWidth(), size.getHeight());
	}

	@Override
	public UIDimension getDimension() {
		return new UIDimension(this.getWidth(), this.getHeight());
	}

}
