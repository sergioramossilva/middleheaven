/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.persistance.db.mapping.ModelParsingException;
import org.middleheaven.persistance.model.ColumnValueType;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 */
public class XmlDrivenDatasetRepositoryModelReader implements
		DatasetRepositoryModelReader {

	


	public static XmlDrivenDatasetRepositoryModelReader newInstance(ManagedFile file){
		return new XmlDrivenDatasetRepositoryModelReader(file);
	}

	private ManagedFile file;
	
	/**
	 * Constructor.
	 * @param file
	 */
	private XmlDrivenDatasetRepositoryModelReader(ManagedFile file) {
		this.file = file;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void read(EditableDatasetRepositoryModel model) {
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			
			InputSource input = new InputSource(file.getContent().getInputStream());

			parser.parse(input, new XmlHandler(model));

			
		} catch (ParserConfigurationException e) {
			throw new ModelParsingException(e);
		} catch (SAXException e) {
			throw new ModelParsingException(e);
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}  
	}

	private static class UserType {
		
		String name;
		String type;
		int size;
		int scale;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public int getScale() {
			return scale;
		}
		public void setScale(int scale) {
			this.scale = scale;
		}
		
		
	}
	
	private class XmlHandler extends DefaultHandler {
		
		
		private  Map<String , UserType> userTypes = new HashMap<String , UserType>();
		private  Map<String , ColumnValueType> typesMapping = new HashMap<String , ColumnValueType>();

		private EditableDatasetRepositoryModel model;
	
		private EditableDatasetModel atual;

		private ClassIntrospector<EditableDatasetModel> inspectorDataSet;
		private ClassIntrospector<EditableDatasetColumnModel> inspectorColumn;
		private ClassIntrospector<UserType> inspectorUserType;
		
		public XmlHandler (EditableDatasetRepositoryModel model){
			this.model = model;
			
			inspectorDataSet = Introspector.of(EditableDatasetModel.class);
			inspectorColumn = Introspector.of(EditableDatasetColumnModel.class);
			inspectorUserType = Introspector.of(UserType.class);
			
			typesMapping.put("blob", ColumnValueType.BLOB);
			typesMapping.put("clob", ColumnValueType.CLOB);
			typesMapping.put("date", ColumnValueType.DATE);
			typesMapping.put("datetime", ColumnValueType.DATETIME);
			typesMapping.put("timestamp", ColumnValueType.DATETIME);
			typesMapping.put("decimal", ColumnValueType.DECIMAL);
			typesMapping.put("int", ColumnValueType.INTEGER);
			typesMapping.put("integer", ColumnValueType.INTEGER);
			typesMapping.put("logic", ColumnValueType.LOGIC);
			typesMapping.put("short", ColumnValueType.SMALL_INTEGER);
			typesMapping.put("text", ColumnValueType.TEXT);
			typesMapping.put("memo", ColumnValueType.MEMO);
			typesMapping.put("time", ColumnValueType.TIME);
		}
		
		 public void startElement(  
			        String uri,  
			        String localName,  
			        String tag,  
			        Attributes attributes)  
			        throws SAXException  {  
			      
			  
			    	if (tag.equals("dataset")) {
			    		if (atual == null) {
			    			
			    			String name = attributes.getValue(attributes.getIndex("name"));
			    			// case sentistive
			    			atual = model.getDataSetModel(name);
			    			
			    			if (atual == null){
			    				atual = new HashDatasetModel();
			    				atual.setName(name);
			    				model.addDatasetModel(atual);
			    			}
			    			
			    			for (int i=0; i < attributes.getLength() ; i++) {
			    				PropertyAccessor pa = inspectorDataSet.inspect().properties().named(attributes.getLocalName(i)).retrive();
			    				
			    				if (pa != null){
				    				pa.setValue(atual, attributes.getValue(i) /*.toLowerCase()*/);
				    			}
			    			}
			    			
			    		
			    		}
			    	} else if (tag.equals("column")) {
			    		String name = attributes.getValue(attributes.getIndex("name"));
		    			
			    		EditableDatasetColumnModel cm = atual.getColumn(name);
			    		
			    		if (cm == null) {
			    			cm = new HashDatasetColumnModel();
			    			cm.setName(name);
			    			atual.addDatasetColumnModel(cm);
			    		}
			    		
			    		for (int i=0; i < attributes.getLength() ; i++) {
			    			final String attributeName = attributes.getLocalName(i);
			    			
			    			if ("type".equals(attributeName)){
			    				PropertyAccessor pa = inspectorColumn.inspect().properties().named("valueType").retrive();
				    			
			    				if (pa != null){
			    					pa.setValue(cm, this.typesMapping.get(attributes.getValue(i)));
			    				}
			    			} else {
			    				PropertyAccessor pa = inspectorColumn.inspect().properties().named(attributeName).retrive();
				    			
				    			if (pa != null){
			    					pa.setValue(cm, attributes.getValue(i) /*.toLowerCase()*/);
				    			}
			    			}
							
			    			
		    			}

			    	} else if (tag.equals("type")){
			    		
			    		UserType ut = new UserType();
			    		
			    		for (int i=0; i < attributes.getLength() ; i++) {
			    			PropertyAccessor pa = inspectorUserType.inspect().properties().named(attributes.getLocalName(i)).retrive();
			    			
			    			if (pa != null){
			    				pa.setValue(ut, attributes.getValue(i).toLowerCase());
			    			}
			    			
		    			}
			    		
			    		userTypes.put(ut.getName(), ut);
			    	}
			      
			    } 
	
			    public void endElement(String uri, String localName, String tag)  
			        throws SAXException  {  
			     
			    	// no-op
			    }  
			    
			    
	}
	
}
