package org.middleheaven.util.criteria.entity.projection;

import org.middleheaven.storage.QualifiedName;

public class FieldGroupCriterion implements GroupCriterion {

	private QualifiedName fieldname;

	public FieldGroupCriterion(QualifiedName fieldname) {
		this.fieldname = fieldname;
	}

	@Override
	public QualifiedName getFieldName() {
		return fieldname;
	}

}
