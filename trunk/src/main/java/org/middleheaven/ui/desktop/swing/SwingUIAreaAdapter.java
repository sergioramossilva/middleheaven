package org.middleheaven.ui.desktop.swing;

import java.awt.Container;

import org.middleheaven.ui.UIDimension;

public class SwingUIAreaAdapter extends UIDimension{


	public SwingUIAreaAdapter() {
	
	}

	public Container getContainer() {
		return null;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}

}
