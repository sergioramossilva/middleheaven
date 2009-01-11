package org.middleheaven.ui.desktop.swing;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.models.UISelectionModel;

public class SList extends SBaseInput implements UISelectOne{


	private static final long serialVersionUID = 4655745102374363086L;
	JList list = new JList();

	public SList(){
		this.add(new JScrollPane(list));
	}

	@Override
	public UISelectionModel getUIModel(){
		return (UISelectionModel)super.getUIModel();
	}

	public void setUIModel(UIModel model){
		super.setUIModel(model);
		
		ListSelectionModelAdpater adp = new ListSelectionModelAdpater(this.getUIModel(),ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		list.setModel(adp);
		list.setSelectionModel(adp);
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UISelectOne.class;
	}
}
