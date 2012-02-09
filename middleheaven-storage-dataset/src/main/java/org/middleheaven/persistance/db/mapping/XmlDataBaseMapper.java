package org.middleheaven.persistance.db.mapping;

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
import org.middleheaven.persistance.model.ColumnType;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 */
public class XmlDataBaseMapper extends AbstractDataBaseMapper {

	public static XmlDataBaseMapper newInstance(ManagedFile file) throws ManagedIOException , ModelParsingException{
		
		
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			
			InputSource input = new InputSource(file.getContent().getInputStream());
			
			XmlDataBaseMapper mapper = new XmlDataBaseMapper();
			
			parser.parse(input, mapper.getHandler());
			
			
			return mapper;
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
	private  Map<String , UserType> userTypes = new HashMap<String , UserType>();

	private static Map<String , ColumnType> typesMapping = new HashMap<String , ColumnType>();

	static {
		
		typesMapping.put("blob", ColumnType.BLOB);
		typesMapping.put("clob", ColumnType.CLOB);
		typesMapping.put("date", ColumnType.DATE);
		typesMapping.put("datetime", ColumnType.DATETIME);
		typesMapping.put("timestamp", ColumnType.DATETIME);
		typesMapping.put("decimal", ColumnType.DECIMAL);
		typesMapping.put("int", ColumnType.INTEGER);
		typesMapping.put("integer", ColumnType.INTEGER);
		typesMapping.put("logic", ColumnType.LOGIC);
		typesMapping.put("memo", ColumnType.MEMO);
		typesMapping.put("short", ColumnType.SMALL_INTEGER);
		typesMapping.put("text", ColumnType.TEXT);
		typesMapping.put("time", ColumnType.TIME);
		
	}
	
	protected ColumnType mapType(String type){
		ColumnType ct =  typesMapping.get(type.toLowerCase());
		
		if (ct == null){
			// try user defined types
			UserType ut = userTypes.get(type);
			
			if (ut == null){
				throw new IllegalModelStateException("User Type " + type + " is not defined");
			}
			
			return mapType(ut.type);
		}
		
		return ct;
	}
	
	
	
	private DefaultHandler getHandler(){
		return new XmlHandler(this);
	}
	
	private class XmlHandler extends DefaultHandler {
		
		
		private XmlDataBaseMapper mapper;

		private DataSetMapper atual;

		private ClassIntrospector<DataSetMapper> inspectorDataSet;
		private ClassIntrospector<ColumnMapper> inspectorColumn;
		private ClassIntrospector<UserType> inspectorUserType;
		
		public XmlHandler (XmlDataBaseMapper mapper){
			this.mapper = mapper;
			
			inspectorDataSet = Introspector.of(DataSetMapper.class);
			inspectorColumn = Introspector.of(ColumnMapper.class);
			inspectorUserType = Introspector.of(UserType.class);
		}
		
		 public void startElement(  
			        String uri,  
			        String localName,  
			        String tag,  
			        Attributes atributos)  
			        throws SAXException  {  
			      
			  
			    	if (tag.equals("dataset")) {
			    		if (atual == null) {
			    			atual = new DataSetMapper();
			    			
			    			
			    			for (int i=0; i < atributos.getLength() ; i++) {
			    				PropertyAccessor pa = inspectorDataSet.inspect().properties().named(atributos.getLocalName(i)).retrive();
			    				
			    				if (pa != null){
				    				pa.setValue(atual, atributos.getValue(i).toLowerCase());
				    			}
			    			}
			    			
			    		
			    		}
			    	} else if (tag.equals("column")) {
			    		
			    		ColumnMapper cm = new ColumnMapper();
			    		
			    		for (int i=0; i < atributos.getLength() ; i++) {
			    			PropertyAccessor pa = inspectorColumn.inspect().properties().named(atributos.getLocalName(i)).retrive();
			    			
			    			if (pa != null){
			    				pa.setValue(cm, atributos.getValue(i).toLowerCase());
			    			}
			    			
		    			}
			    		
			    		atual.addColumn(cm);
			    	} else if (tag.equals("type")){
			    		
			    		UserType ut = new UserType();
			    		
			    		for (int i=0; i < atributos.getLength() ; i++) {
			    			PropertyAccessor pa = inspectorUserType.inspect().properties().named(atributos.getLocalName(i)).retrive();
			    			
			    			if (pa != null){
			    				pa.setValue(ut, atributos.getValue(i).toLowerCase());
			    			}
			    			
		    			}
			    		
			    		userTypes.put(ut.getName(), ut);
			    	}
			      
			    } 
	
			    public void endElement(String uri, String localName, String tag)  
			        throws SAXException  {  
			     
			    	if (tag.equals("dataset")) {
			    		if (atual != null) {
			    			mapper.addDataSetMapping(atual);
			    			atual = null;
			    		}
			    	} 
			    }  
			    
			    
	}
	


}
