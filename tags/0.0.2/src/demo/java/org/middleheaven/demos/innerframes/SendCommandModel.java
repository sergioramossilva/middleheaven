package org.middleheaven.demos.innerframes;

import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.components.UIInput;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.models.AbstractUICommandModel;

public class SendCommandModel extends AbstractUICommandModel {

	@Override
	public void onCommand(UIActionEvent event) {
		String[] uic = new String[]{"text","secret","number","date","color","drop","list"};
		
		for (String name : uic){
		UIInput input = UITreeCriteria.search("./../" + name)
							.execute(event.getSource())
							.first(UIInput.class);
		
		Object value = input.getUIModel().getValue();
		
		System.out.println(value);
		}
		
		
	}

}
