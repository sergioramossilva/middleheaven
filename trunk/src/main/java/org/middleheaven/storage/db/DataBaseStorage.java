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

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.domain.DataType;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.domain.TextDataTypeModel;
import org.middleheaven.logging.Log;
import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.storage.AbstractSequencialIdentityStorage;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.ReferenceStorableDataTypeModel;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorableModelReader;
import org.middleheaven.storage.StorableState;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.db.datasource.DataSourceProvider;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.criteria.Criteria;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.criteria.entity.EntityCriteriaBuilder;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IdentitySequence;
import org.middleheaven.util.identity.IntegerIdentity;

/**
 * Keeps data stored in a relational database. The access to the database is
 * gained through a {@code javax.sql.DataSource}. The data model can be tweeked
 * using a StorableModelReader
 */
public final class DataBaseStorage extends AbstractSequencialIdentityStorage {

	private DataBaseDialect dialect;
	private DataSource datasource;
	private final Map<String, IdentitySequence> sequences = new TreeMap<String, IdentitySequence>();

	public DataBaseStorage(DataSource datasource, StorableModelReader reader) {
		super(reader);
		this.datasource = datasource;
		this.dialect = DatabaseDialectFactory.getDialect(this.datasource);
	}

	public DataBaseStorage setDialect(DataBaseDialect dialect) {
		this.dialect = dialect;
		return this;
	}

	public DataBaseStorage setDataSource(DataSourceProvider provider) {
		this.datasource = provider.getDataSource();
		this.dialect = DatabaseDialectFactory.getDialect(this.datasource);
		return this;
	}

	@Override
	public final <I extends Identity> Sequence<I> getSequence(
			Class<?> entityType) {

		String name = this.reader().read(entityType).getEntityLogicName();

		StoreQuerySession session = StoreQuerySession.getInstance(this);

		IdentitySequence iseq = sequences.get(name);
		if (iseq == null) {
			Sequence<Long> seq = dialect.getSequence(datasource, name);
			iseq = new DBIdentitySequenceAdapter(seq);
			sequences.put(name, iseq);
		}
		return iseq;

	}

	class DBIdentitySequenceAdapter implements
			IdentitySequence<IntegerIdentity> {

		private Sequence<Long> nativeSequence;

		public DBIdentitySequenceAdapter(Sequence<Long> nativeSequence) {
			this.nativeSequence = nativeSequence;
		}

		@Override
		public SequenceToken<IntegerIdentity> next() {
			return new DefaultToken(IntegerIdentity.valueOf(nativeSequence
					.next().value().intValue()));
		}

	}

	class DBStorageQuery<T> implements Query<T> {
		EntityCriteria<T> criteria;
		private ReadStrategy hints;

		public DBStorageQuery(EntityCriteria<T> criteria, ReadStrategy hints) {
			super();
			this.criteria = criteria;
			this.hints = hints;
		}

		@Override
		public long count() {
			return countByCriteria(criteria.duplicate());
		}

		@Override
		public T fetchFirst() {
			Collection<T> list = findByCriteria(criteria.duplicate().setRange(
					1, 1), hints);
			return list.isEmpty() ? null : list.iterator().next();
		}

		@Override
		public Collection<T> fetchAll() {
			return findByCriteria(criteria.duplicate(), hints);
		}

		@Override
		public boolean isEmpty() {
			return this.count() == 0;
		}

		@Override
		public Query<T> limit(int startAt, int maxCount) {
			EntityCriteria<T> rangeCriteria = this.criteria.duplicate();
			rangeCriteria.setRange(startAt, maxCount);

			return new DBStorageQuery<T>(rangeCriteria, hints);
		}

	}

	<T> long countByCriteria(EntityCriteria<T> criteria) {

		Connection con = null;
		try {
			con = this.datasource.getConnection();

			criteria.setCountOnly(true);
			RetriveDataBaseCommand command = dialect.createSelectCommand(
					criteria, reader());
			Log.onBook("SQL").trace(command.toString());
			command.execute(this, con);
			ResultSet rs = command.getResult();

			try {
				if (rs.next()) {
					return rs.getInt(1);
				}
				throw new StorageException(
						"Count operation returned without result");
			} finally {
				rs.close();
			}

		} catch (SQLException e) {
			throw dialect.handleSQLException(e);
		} finally {
			close(con);
		}
	}

	private void close(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			throw dialect.handleSQLException(e);
		}
	}
	


	<T> Collection<T> findByCriteria(EntityCriteria<T> criteria,
			ReadStrategy hints) {

		StoreQuerySession session = StoreQuerySession.getInstance(this);

		ResultSet rs = null;
		Connection con = null;
		try {
			session.open();
			con = this.datasource.getConnection();

			criteria.setCountOnly(false);
			RetriveDataBaseCommand command = dialect.createSelectCommand(
					criteria, reader());
			Log.onBook("SQL").trace(command.toString());

			command.execute(this, con);
			rs = command.getResult();

			// if dialect does not support offset
			if (!dialect.supportsOffSet()) {
				// iterate all until we arrive to the offset
				int off = 1;
				while (off < criteria.getCount() && rs.next()) {
					// no-op. just iterate
				}

			}

		} catch (SQLException e) {
			close(con);
			throw dialect.handleSQLException(e);
		}

		StorableEntityModel model = reader().read(criteria.getTargetClass());
		if (hints != null && hints.isFowardOnly() && hints.isReadOnly()) {
			// fastlane
			long count;
			if (criteria.getCount() >= 0) {
				count = criteria.getCount();
			} else {
				count = countByCriteria(criteria);
			}
			return new FastlaneCollection<T>(count, rs, con, model, this, this
					.getStorableStateManager());
		} else {

			try {
				// load all objects onto a list

				ResultSetStorable s = new ResultSetStorable(rs, model);
				if (dialect.supportsCountLimit()) {
					LinkedList<T> list = new LinkedList<T>();
					while (rs.next()) {

						@SuppressWarnings("unchecked")
						T t = (T) this.getStorableStateManager().merge(
							newInstance(model.getEntityClass())
						);
						
						Storable st = this
								.copy(s, (Storable) t, model, session);
						st.setStorableState(StorableState.RETRIVED);
						list.addLast((T) st);

					}
					return list;
				} else {
					int count = 0;
					List<T> list = new ArrayList<T>(criteria.getCount());
					while (rs.next() && count < criteria.getCount()) {

						@SuppressWarnings("unchecked")
						T t = (T) this.getStorableStateManager().merge(
							newInstance(model.getEntityClass())
						);
						
						Storable st = this
								.copy(s, (Storable) t, model, session);
						st.setStorableState(StorableState.RETRIVED);
						list.add((T) st);

						count++;

					}
					return list;
				}
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			} finally {
				try {
					rs.close();
					session.close();
				} catch (SQLException e) {
					throw dialect.handleSQLException(e);
				}
			}

		}
	}

	protected Storable copyStorable(Storable from, Storable to,
			StorableEntityModel model) {
		StoreQuerySession session = StoreQuerySession.getInstance(this);
		try {
			session.open();
			return super.copy(from, to, model, session);
		} finally {
			session.close();
		}

	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria,
			ReadStrategy strategy) {

		StorableEntityModel model = this.reader().read(
				criteria.getTargetClass());
		return new DBStorageQuery<T>(dialect.mergeCriteria(criteria, model),
				strategy);

	}

	@Override
	public void insert(Collection<Storable> collection) {

		StorableEntityModel model = this.resolveModel(collection);
		if (model != null) {
			executeCommand(dialect.createInsertCommand(collection, model));
		}

	}

	@Override
	public void remove(Collection<Storable> collection) {

		StorableEntityModel model = this.resolveModel(collection);
		if (model != null) {

			List<Identity> keys = new ArrayList<Identity>(collection.size());
			Storable s = null;
			for (Iterator<Storable> it = collection.iterator(); it.hasNext();) {
				s = it.next();
				keys.add(s.getIdentity());
			}

			EntityCriteria<?> c = EntityCriteriaBuilder.search(
					s.getPersistableClass()).and(
					model.identityFieldModel().getHardName().getName())
					.in(keys).all();

			executeCommand(dialect.createDeleteCommand(c, reader()));
		}
	}

	@Override
	public void remove(EntityCriteria<?> criteria) {
		StorableEntityModel model = reader().read(criteria.getTargetClass());
		if (model != null) {
			executeCommand(dialect.createDeleteCommand(criteria, reader()));
		}

	}

	@Override
	public void update(Collection<Storable> collection) {

		StorableEntityModel model = this.resolveModel(collection);
		if (model != null) {
			executeCommand(dialect.createUpdateCommand(collection, model));
		}
	}

	private void executeCommand(DataBaseCommand command) {

		Connection con = null;
		try {
			con = this.datasource.getConnection();

			command.execute(this, con);

		} catch (SQLException e) {
			Log.onBookFor(this.getClass()).debug("SQL : {0}", command.getSQL());
			throw dialect.handleSQLException(e);
		} finally {
			close(con);
		}
	}

	/**
	 * Used the DomainModel to create Tables and Indexes in the DataBase.
	 */
	public void updateMetadata(DomainModel model, String catalog) {

		Enumerable<EntityModel> allEntities = model.entitiesModels();
		DataBaseModel dbModel = new DataBaseModel();

		for (EntityModel em : allEntities) {
			dbModel.addDataBaseObjectModel(tableModelFor(reader().read(
					em.getEntityClass())));
		}

		dialect.updateDatabaseModel(dbModel);

		updateDataBaseModel(catalog, dbModel);

	}

	private TableModel tableModelFor(StorableEntityModel em) {
		TableModel model = new TableModel(em.getEntityHardName());

		ColumnModel column = columnModelFor(em.identityFieldModel());
		if (column != null) {
			model.addColumn(column);
		}

		for (StorableFieldModel fm : em.fields()) {
			column = columnModelFor(fm);
			if (column != null) {
				model.addColumn(column);
			}
		}
		return model;
	}

	private ColumnModel columnModelFor(StorableFieldModel fm) {
		if (fm.isTransient() || fm.getDataType().isToManyReference()) {
			return null; // not persistable in a relacional table
		}

		ColumnModel column = new ColumnModel(fm.getHardName().getName(), fm
				.getDataType());
		column.setKey(fm.isIdentity());

		if (column.isKey()) {
			column.setNullable(false);
			column.setUnique(true);

		} else if (column.getType().equals(DataType.MANY_TO_ONE)) {

			ReferenceStorableDataTypeModel model = (ReferenceStorableDataTypeModel) fm
					.getDataTypeModel();

			column.setType(DataType.fromClass(model.getTargetFieldType()));

			String name = model.getTargetFieldHardName();
			if (name != null) {
				column.setTargetName(name);
			}
		}
		if (column.getSize() == 0) {
			if (column.getType().equals(DataType.TEXT)) {
				TextDataTypeModel model = (TextDataTypeModel) fm
						.getDataTypeModel();

				Integer maxLength = model.getMaxLength();

				if (maxLength != null) {
					column.setSize(maxLength);
				} else {
					column.setSize(0);
					column.setType(DataType.MEMO);
				}
			}
		}
		return column;
	}

	private void updateDataBaseModel(String catalog, DataBaseModel dbModel) {

		DataBaseModel existingDBModel = dialect.readDataBaseModel(catalog,
				this.datasource);
		for (DataBaseObjectModel dbObject : dbModel) {
			DataBaseObjectModel existingModel = existingDBModel
					.getDataBaseObjectModel(dbObject.getName(), dbObject
							.getType());
			if (existingModel != null) {
				// alter
				if (dbObject.getType().equals(DataBaseObjectType.TABLE)) {

					TableModel newModel = (TableModel) dbObject;
					TableModel oldModel = (TableModel) existingModel;

					TableModel diff = newModel.differenceTo(oldModel);
					// alter only creates, does not remove
					if (!diff.columns.isEmpty()) {
						DataBaseCommand command = dialect
								.createAlterTableCommand(diff);
						executeCommand(command);
					}

				}
			} else {
				// create
				try {
					if (dbObject.getType().equals(DataBaseObjectType.TABLE)) {
						TableModel tm = (TableModel) dbObject;
						DataBaseCommand command = dialect
								.createCreateTableCommand(tm);
						executeCommand(command);

						for (ColumnModel cm : tm) {
							if (cm.isIndexed()) {
								DataBaseCommand idxCommand = dialect
										.createCreateIndexCommand(cm);
								executeCommand(idxCommand);

							}
						}
					} else if (dbObject.getType().equals(
							DataBaseObjectType.SEQUENCE)) {
						DataBaseCommand command = dialect
								.createCreateSequenceCommand((SequenceModel) dbObject);
						executeCommand(command);
					}

				} catch (TableAlreadyExistsException e) {
					Log.onBookFor(this.getClass()).info(
							"Table {0} already exists.", dbObject.getName());
				}
			}
		}
	}

	protected DataBaseDialect getDialect() {
		return this.dialect;
	}

	protected DataSource getDataSource() {
		return this.datasource;
	}

	Object readFieldValue(Storable s, StorableFieldModel fm) {
		if (fm.getDataType().isToOneReference()) {
			Object referenced = s.getFieldValue(fm);

			Storable ref = this.getStorableStateManager().merge(referenced);

			ReferenceStorableDataTypeModel mo = (ReferenceStorableDataTypeModel) fm
					.getDataTypeModel();

			EntityFieldModel efm = ref.getEntityModel().fieldModel(
					QualifiedName.qualify(ref.getPersistableClass()
							.getSimpleName().toLowerCase(), mo
							.getTargetFieldName()));

			return ref.getFieldValue(efm);

		} else {
			return s.getFieldValue(fm);
		}
	}

}
