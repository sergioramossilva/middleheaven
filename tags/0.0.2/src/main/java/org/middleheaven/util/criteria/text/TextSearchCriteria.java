package org.middleheaven.util.criteria.text;

import org.middleheaven.util.criteria.Criteria;




/**
 * 
 *
 * @param <D> the retrivable document class
 */
public interface TextSearchCriteria<D> extends Criteria<D>{

	
	public Class<D> getDocumentType();

}
