package org.middleheaven.storage.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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
import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.AbstractSequencialIdentityStorage;
import org.middleheaven.storage.AbstractDataStorage;
import org.middleheaven.storage.ExecutableQuery;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.QueryExecuter;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.SimpleExecutableQuery;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaFilter;
import org.middleheaven.util.identity.Identity;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.nq.NativeQuery;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLStorage extends AbstractSequencialIdentityStorage {

	private static class XMLStoreContructor extends XMLObjectContructor<Document>{

		@Override
		protected void constructFrom(Document document) throws ManagedIOException, XMLException {
			this.setConstructedObject(document);
		}

		public void constructFrom(ManagedFile file){
			super.constructFrom(file);
		}
	}


	public static XMLStorage manage(ManagedFile source,StorableModelReader reader){
		return new XMLStorage(source, reader);
	}

	Document doc;
	public XMLStorage(ManagedFile source , StorableModelReader reader){
		super(reader);
		if (source.getType().isFile()){
			XMLStoreContructor c = new XMLStoreContructor();
			c.constructFrom(source);
			doc = c.getConstructedObject();
		}

	}
	XMLCriteriaInterpreter interpreter = new XMLCriteriaInterpreter();

	
	private QueryExecuter xmlQueryExecuter = new QueryExecuter() {

		@Override
		public <T> Collection<T> execute(final ExecutableQuery<T> query) {
			StorableEntityModel model = query.getModel();
			String xpathStr = interpreter.Interpreter(model,query.getCriteria());

			try {
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();
				XPathExpression expr = xpath.compile(xpathStr);

				NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

				List<T> list = new ArrayList<T>(nodes.getLength());
				
				for (int i = 0; i < nodes.getLength(); i++) {
					T t = (T)merge(model.newInstance());
					NodeStorable s = new NodeStorable(nodes.item(i),model.identityFieldModel());
					copy(s, (Storable)t, model);
					
					list.add(t);
				}

				return list;

			} catch (XPathExpressionException e) {
				throw new StorageException("Illegal expression " + xpathStr);
			}
		}
		
	};

	
	


	
	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria,StorableEntityModel model, ReadStrategy strategy) {
		return new SimpleExecutableQuery<T>(criteria,model, this.xmlQueryExecuter);

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
