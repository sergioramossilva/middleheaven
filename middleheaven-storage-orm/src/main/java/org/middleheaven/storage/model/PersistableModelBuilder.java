package org.middleheaven.storage.model;

import org.middleheaven.domain.model.ModelSetItemBuilder;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;

public interface PersistableModelBuilder extends ModelSetItemBuilder<EditableDBTableModel>{

	
	public EditableDBTableModel getEditableModelOf(Class<?> type);
}
