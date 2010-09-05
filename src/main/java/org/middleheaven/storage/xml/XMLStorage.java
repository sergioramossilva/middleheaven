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
import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.AbstractSequencialIdentityStorage;
import org.middleheaven.storage.ExecutableQuery;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.QueryExecuter;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.SimpleExecutableQuery;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.db.StoreQuerySession;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.identity.Identity;
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
			
			StoreQuerySession session = StoreQuerySession.getInstance(XMLStorage.this);
			
			StorableEntityModel model = query.getModel();
			String xpathStr = interpreter.Interpreter(model,query.getCriteria());

			try {
				session.open();
				
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();
				XPathExpression expr = xpath.compile(xpathStr);

				NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

				List<T> list = new ArrayList<T>(nodes.getLength());
				
				for (int i = 0; i < nodes.getLength(); i++) {
					T t = (T)getStorableStateManager().merge(newInstance(model.getEntityClass()));
					NodeStorable s = new NodeStorable(nodes.item(i),model);
					copy(s, (Storable)t, model, session);
					
					list.add(t);
				}

				return list;

			} catch (XPathExpressionException e) {
				throw new StorageException("Illegal expression " + xpathStr);
			} finally {
				session.close();
			}
		}
		
	};

	
	



	@Override
	public void insert(Collection<Storable> obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Collection<Storable> obj) {
		// TODO Auto-generated method stub

	}


	@Override
	public void update(Collection<Storable> obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public <I extends Identity> Sequence<I> getSequence(Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria,ReadStrategy strategy) {
		final StorableEntityModel model = reader().read(criteria.getTargetClass());
		return new SimpleExecutableQuery<T>(criteria,model, this.xmlQueryExecuter);

	}

	@Override
	public void remove(EntityCriteria<?> criteria) {
		// TODO implement DataStorage.remove
		
	}


}
