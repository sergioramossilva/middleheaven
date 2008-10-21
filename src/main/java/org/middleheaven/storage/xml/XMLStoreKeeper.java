package org.middleheaven.storage.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.middleheaven.storage.AbstractStoreKeeper;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.sequence.Sequence;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLStoreKeeper extends AbstractStoreKeeper {

	private static class XMLStoreContructor extends XMLObjectContructor<Document>{

		@Override
		protected void constructFrom(Document document) throws ManagedIOException, XMLException {
			this.setConstructedObject(document);
		}

		public void constructFrom(ManagedFile file){
			super.constructFrom(file);
		}
	}


	public static XMLStoreKeeper manage(ManagedFile source){
		return new XMLStoreKeeper(source);
	}

	Document doc;
	public XMLStoreKeeper(ManagedFile source){
		if (source.getType().isFile()){
			XMLStoreContructor c = new XMLStoreContructor();
			c.constructFrom(source);
			doc = c.getConstructedObject();
		}

	}

	XMLCriteriaInterpreter interpreter = new XMLCriteriaInterpreter();

	class XPathStorageQuery<T> implements Query<T>{
		Criteria<T> criteria;
		StorableEntityModel model;

		public XPathStorageQuery(Criteria<T> criteria, StorableEntityModel model) {
			super();
			this.criteria = criteria;
			this.model = model;
		}

		@Override
		public long count() {
			return list().size();
		}

		@Override
		public T find() {
			List<T> list = findByCriteria(criteria,model);
			return list.isEmpty() ? null : list.get(0);
		}

		@Override
		public Collection<T> list() {
			return findByCriteria(criteria,model);
		}

		@Override
		public boolean isEmpty() {
			return list().isEmpty();
		}

	} 

	<T> List<T> findByCriteria(Criteria<T> criteria,StorableEntityModel model){

		String xpathStr = interpreter.Interpreter(criteria);

		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(xpathStr);

			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			List<T> list = new ArrayList<T>(nodes.getLength());
			
			for (int i = 0; i < nodes.getLength(); i++) {
				T t = merge((T)model.newInstance());
				NodeStorable s = new NodeStorable(nodes.item(i),model.identityFieldModel());
				this.copy(s, (Storable)t, model);
				
				list.add(t);
			}

			return list;

		} catch (XPathExpressionException e) {
			throw new StorageException("Illegal expression " + xpathStr);
		}

	}
	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria,StorableEntityModel model, ReadStrategy strategy) {
		return new XPathStorageQuery<T>(criteria,model);

	}

	@Override
	public void insert(Collection<Storable> obj, StorableEntityModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Collection<Storable> obj, StorableEntityModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Criteria<?> criteria, StorableEntityModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Collection<Storable> obj, StorableEntityModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public <I extends Identity> Sequence<I> getSequence(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
