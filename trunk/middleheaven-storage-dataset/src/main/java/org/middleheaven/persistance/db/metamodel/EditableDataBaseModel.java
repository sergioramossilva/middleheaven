package org.middleheaven.persistance.db.metamodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Represents an editable {@link DataBaseModel}.
 */
public class EditableDataBaseModel implements  DataBaseModel{

	private final Map<String, DataBaseObjectModel> tableModels = new HashMap<String,DataBaseObjectModel>();

	/**
	 * 
	 * Constructor.
	 */
	public EditableDataBaseModel (){}
	
	/**
	 * Add database object model.
	 * 
	 * @param dbObjectModel the object model.
	 */
	public void addDataBaseObjectModel(DataBaseObjectModel dbObjectModel) {
		tableModels.put(dbObjectModel.getType().toString().toLowerCase() + ":" + dbObjectModel.getName().toLowerCase(), dbObjectModel);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataBaseObjectModel getDataBaseObjectModel(String name, DataBaseObjectType type) {
		return tableModels.get(type.toString().toLowerCase() + ":" +name.toLowerCase());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<DataBaseObjectModel> iterator() {
		return tableModels.values().iterator();
	}

	
	
}
