package org.middleheaven.storage.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.storage.PersistableState;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.util.conversion.TypeConvertions;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeStorable implements Storable{

	Map<String, String> data = new TreeMap<String,String>();
	Identity key;
	public NodeStorable(Node node , StorableFieldModel keyModel) {
	
		NodeList fields = node.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++) {
			Node field = fields.item(i);
			data.put(field.getNodeName(), field.getTextContent());
		}
		String val = data.get(keyModel.getHardName().getColumnName());

		key = val ==null ? null : IntegerIdentity.valueOf(val);
	}

	@Override
	public Object getFieldValue(StorableFieldModel model) {
		if (model.isKey()){
			return key;
		}
		if (model.getDataType().isTemporal()){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = format.parse(data.get(model.getHardName().getColumnName()));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			
			return date;
		} else {
			return TypeConvertions.convert(data.get(model.getHardName().getColumnName()), model.getValueClass());
		}
	}

	@Override
	public Identity getIdentity() {
		return key;
	}

	@Override
	public Class<?> getPersistableClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersistableState getPersistableState() {
		return PersistableState.RETRIVED;
	}

	@Override
	public void setFieldValue(StorableFieldModel model, Object fieldValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIdentity(Identity id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPersistableState(PersistableState state) {
		// TODO Auto-generated method stub
		
	}

}
