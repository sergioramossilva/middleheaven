package org.middleheaven.ui.desktop.swing;

import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.models.UISelectionModel;

public class SDropDownInput extends SBaseInput implements UISelectOne{

	private static final long serialVersionUID = 4655745102374363086L;
	JComboBox combo = new JComboBox();
	
	public SDropDownInput(){
		this.add(combo);
	}
	
	@Override
	public UISelectionModel getUIModel(){
		return (UISelectionModel)super.getUIModel();
	}
	
	public void setUIModel(UIModel model){
		super.setUIModel(model);
		
		combo.setModel(new ListSelectionModelAdpater(this.getUIModel(), ListSelectionModel.SINGLE_SELECTION));
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UISelectOne.class;
	}
}
