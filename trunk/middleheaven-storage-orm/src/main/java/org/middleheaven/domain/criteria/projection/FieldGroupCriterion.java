package org.middleheaven.domain.criteria.projection;

import org.middleheaven.util.QualifiedName;


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
