package org.middleheaven.ui.desktop.swing;

import javax.swing.JProgressBar;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIProgress;
import org.middleheaven.ui.models.UIProgressModel;

public class SProgress extends SBasePanel implements UIProgress{


	private static final long serialVersionUID = 7564245586904494636L;

	public SProgress(){
		JProgressBar progressBar = new JProgressBar();
		this.add(progressBar);
		
		progressBar.setIndeterminate(true);
	}
	
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIProgress.class;
	}

	@Override
	public UIReadState getReadState() {
		return UIReadState.OUTPUT_ONLY;
	}

	
	public UIProgressModel getUIModel(){
		return (UIProgressModel)super.getUIModel();
	}
}
