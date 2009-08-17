package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;


import org.middleheaven.domain.DataType;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.logging.Logging;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.AbstractSequencialIdentityStorage;
import org.middleheaven.storage.StorableState;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.WrappStorableReader;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.db.datasource.DataSourceProvider;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IdentitySequence;
import org.middleheaven.util.identity.IntegerIdentitySequence;

/**
 * Keeps data stored in a relational database.
 * The access to the database is gained through a {@code javax.sql.DataSource}.
 * The data model can be tweeked using a StorableModelReader
 */
public final class DataBaseStorage extends AbstractSequencialIdentityStorage {

	private DataBaseDialect dialect;
	private DataSource datasource;

	public DataBaseStorage(DataSourceProvider provider){
		this(provider, new WrappStorableReader());
	}

	public DataBaseStorage(DataSourceProvider provider, StorableModelReader reader){
		super(reader);
		this.datasource = provider.getDataSource();
		this.dialect = DatabaseDialectFactory.getDialect(this.datasource);
	}

	public DataBaseStorage setDialect(DataBaseDialect dialect){
		this.dialect = dialect;
		return this;
	}

	public DataBaseStorage setDataSource(DataSourceProvider provider){
		this.datasource = provider.getDataSource();
		this.dialect = DatabaseDialectFactory.getDialect(this.datasource);
		return this;
	}


	Map<String, IdentitySequence> sequences = new TreeMap<String,IdentitySequence>();

	@Override
	public <I extends Identity> Sequence<I> getSequence(String name) {

		IdentitySequence iseq = sequences.get(name);
		if (iseq==null){
			Sequence<Long> seq = dialect.getSequence(datasource, name);
			iseq = new IntegerIdentitySequence();
			sequences.put(name,iseq);
		}
		return iseq;

	}

	class DBStorageQuery<T> implements Query<T>{
		Criteria<T> criteria;
		StorableEntityModel model;
		private ReadStrategy hints;

		public DBStorageQuery(Criteria<T> criteria, StorableEntityModel model, ReadStrategy hints) {
			super();
			this.criteria = criteria;
			this.model = model;
			this.hints = hints;
		}

		@Override
		public long count() {
			return countByCriteria(criteria.duplicate(), model);
		}

		@Override
		public T find() {
			Collection<T> list = findByCriteria(criteria.duplicate(),model,hints);
			return list.isEmpty() ? null : list.iterator().next();
		}

		@Override
		public Collection<T> findAll() {
			return findByCriteria(criteria.duplicate(),model,hints);
		}

		@Override
		public boolean isEmpty() {
			return this.count() == 0;
		}

		@Override
		public Query<T> setRange(int startAt, int maxCount) {
			Criteria<T> rangeCriteria = this.criteria.duplicate();
			rangeCriteria.setRange(startAt, maxCount);

			return new DBStorageQuery<T>( rangeCriteria, this.model, hints);
		}

	} 

	<T> long countByCriteria(Criteria<T> criteria,StorableEntityModel model){

		Connection con =null;
		try {
			con = this.datasource.getConnection();
			criteria.setCountOnly(true);
			RetriveDataBaseCommand command = dialect.createSelectCommand(criteria, model);
			Logging.getBook("SQL").trace(command.toString());
			command.execute(this,con, model);
			ResultSet rs = command.getResult();

			try{
				if (rs.next()){
					return rs.getInt(1);
				} 
				throw new StorageException("Count operation returned without result");
			} finally {
				rs.close();
			}

		} catch (SQLException e){
			throw dialect.handleSQLException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			}
		}
	}

	<T> Collection<T> findByCriteria(Criteria<T> criteria,StorableEntityModel model, ReadStrategy hints){

		Connection con =null;
		ResultSet rs= null;
		try {
			con = this.datasource.getConnection();
			criteria.setCountOnly(false);
			RetriveDataBaseCommand command = dialect.createSelectCommand(criteria, model);
			Logging.getBook("SQL").trace(command.toString());
			command.execute(this,con, model);
			rs = command.getResult();

			// if dialect does not support offset
			if (!dialect.supportsOffSet()){
				// iterate all until we arrive to the offset
				int off = 1;
				while ( off < criteria.getCount() && rs.next()){
					//no-op. just iterate
				}

			}

		} catch (SQLException e){
			try {
				if (con!=null){
					con.close();
				}
			} catch (SQLException e2) {
				Logging.error("Error closing connection after exception",e2);
			}
			throw dialect.handleSQLException(e);
		} 

		if (hints!=null && hints.isFowardOnly() && hints.isReadOnly()){
			// fastlane
			long count;
			if (criteria.getCount()>=0){
				count = criteria.getCount();
			} else {
				count =  countByCriteria(criteria,model);
			}
			return new FastlaneCollection<T>(count, rs, con , model , this);
		} else {

			try{
				// load all objects onto a list
				
				ResultSetStorable s = new ResultSetStorable(rs,model);
				if (dialect.supportsCountLimit()){
					LinkedList<T> list = new LinkedList<T>();
					while (rs.next()){

						T t = (T)merge(criteria.getTargetClass().cast(model.newInstance()));
						this.copy(s, (Storable)t, model);
						((Storable)t).setStorableState(StorableState.RETRIVED);
						list.addLast(t);

					}
					return list;
				} else {
					int count =0;
					List<T> list = new ArrayList<T>(criteria.getCount());
					while (rs.next() && count < criteria.getCount()){

						T t = (T)merge(criteria.getTargetClass().cast(model.newInstance()));
						this.copy(s, (Storable)t, model);
						((Storable)t).setStorableState(StorableState.RETRIVED);

						list.add(t);
						count++;
						
					}
					return list;
				}
			} catch (SQLException e){
				throw dialect.handleSQLException(e);
			} finally {
				try {
					rs.close();
					if (con!=null){
						con.close();
					}
				} catch (SQLException e) {
					throw dialect.handleSQLException(e);
				}
			}

		}


	}

	protected void copyStorable(Storable from, Storable to,StorableEntityModel model) {
		super.copy(from, to, model);
	}

	
	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria,StorableEntityModel model, ReadStrategy strategy) {

		return new DBStorageQuery<T>(dialect.merge(criteria,model),model,strategy);

	}

	@Override
	public void insert(Collection<Storable> collection, StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}
		executeCommand(dialect.createInsertCommand(collection,model), model );

	}

	@Override
	public void remove(Collection<Storable> collection , StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}
		List<Identity> keys = new ArrayList<Identity>(collection.size());
		Storable s=null;
		for (Iterator<Storable> it = collection.iterator();it.hasNext();){
			s = it.next();
			keys.add(s.getIdentity());
		}

		Criteria<?> c = CriteriaBuilder.search(s.getPersistableClass())
		.and(model.identityFieldModel().getHardName().getName()).in(keys)
		.all();

		executeCommand(dialect.createDeleteCommand(c , model), model );

	}

	@Override
	public void remove(Criteria<?> criteria, StorableEntityModel model) {
		executeCommand(dialect.createDeleteCommand(criteria, model), model );

	}

	@Override
	public void update(Collection<Storable> collection, StorableEntityModel model) {
		if (collection.isEmpty()){
			return;
		}

		executeCommand(dialect.createUpdateCommand(collection , model), model );

	}

	private void executeCommand (DataBaseCommand command,StorableEntityModel model){
		Connection con =null;
		try {
			con = this.datasource.getConnection();

			command.execute(this,con, model);


		} catch (SQLException e){
			throw dialect.handleSQLException(e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			}
		}
	}

	/**
	 * Used the DomainModel to create Tables and Indexes in the DataBase.
	 */
	public void updateMetadata(DomainModel model , String catalog){


		Collection<EntityModel> allEntities = model.entitiesModels();
		DataBaseModel dbModel = new DataBaseModel();

		for (EntityModel em : allEntities ){
			dbModel.addDataBaseObjectModel(tableModelFor(this.storableModelOf(em)));
		}

		dialect.updateDatabaseModel(dbModel);

		updateDataBaseModel(catalog,dbModel);

	}

	private TableModel tableModelFor(StorableEntityModel em) {
		TableModel model = new TableModel(em.getEntityHardName());

		ColumnModel column = columnModelFor(em.identityFieldModel());
		if (column != null){
			model.addColumn( column);
		}
		
		for (StorableFieldModel fm : em.fields()){
			column = columnModelFor(fm);
			if (column != null){
				model.addColumn( column);
			}
		}
		return model;
	}

	private ColumnModel columnModelFor(StorableFieldModel fm) {
		if (fm.isTransient() || fm.getDataType().isToManyReference()){
			return null; // not persistable in a relacional table
		}

		ColumnModel column = new ColumnModel(fm.getHardName().getName(), fm.getDataType());

		if(column.getType().equals(DataType.IDENTITY)){
			column.setNullable(false);
			column.setType(DataType.INTEGER); // TODO resolve correct field type
		} else if (column.getType().equals(DataType.MANY_TO_ONE)){
			column.setType(DataType.INTEGER);
			String name = fm.getParam("targetFieldHardName");
			if(name !=null){
				column.setName(name);
			}
		}
		if (column.getSize()==0){
			if (column.getType().equals(DataType.TEXT)){
				String size = fm.getParam("size");
				if(size != null){
					column.setSize(Integer.parseInt(size));
				} else {
					column.setSize(50);
				}
			} 
		}
		return column;
	}

	private void updateDataBaseModel(String catalog, DataBaseModel dbModel)  {

		DataBaseModel existingDBModel = dialect.readDataBaseModel(catalog,this.datasource);
		for (DataBaseObjectModel dbObject : dbModel){
			DataBaseObjectModel existingModel = existingDBModel.getDataBaseObjectModel(dbObject.getName(), dbObject.getType());
			if ( existingModel != null){
				// alter 
				if (dbObject.getType().equals(DataBaseObjectType.TABLE)){

					TableModel newModel = (TableModel)dbObject;
					TableModel oldModel = (TableModel)existingModel;

					TableModel diff = newModel.differenceTo(oldModel);
					// alter only creates, does not remove
					if (!diff.columns.isEmpty()){
						DataBaseCommand command = dialect.createAlterTableCommand(diff);
						executeCommand(command, null);
					}

				}
			} else {
				// create 
				try {
					if (dbObject.getType().equals(DataBaseObjectType.TABLE)){
						TableModel tm = (TableModel)dbObject;
						DataBaseCommand command = dialect.createCreateTableCommand(tm);
						executeCommand(command, null);

						for (ColumnModel cm : tm){
							if (cm.isIndexed()){
								DataBaseCommand idxCommand = dialect.createCreateIndexCommand(cm);
								executeCommand(idxCommand, null);

							}
						}
					} else if (dbObject.getType().equals(DataBaseObjectType.SEQUENCE)) {
						DataBaseCommand command = dialect.createCreateSequenceCommand((SequenceModel)dbObject);
						executeCommand(command, null);
					}

				} catch (TableAlreadyExistsException e){
					Logging.getBook(this.getClass()).info("Table " + dbObject.getName() + " already exists");
				}
			}
		}
	}

	protected DataBaseDialect getDialect() {
		return this.dialect;
	}}
