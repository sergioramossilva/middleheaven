package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.middleheaven.logging.Log;
import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataQuery;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataRowStream;
import org.middleheaven.persistance.DataSet;
import org.middleheaven.persistance.DataSetNotFoundException;
import org.middleheaven.persistance.DataStore;
import org.middleheaven.persistance.DataStoreName;
import org.middleheaven.persistance.DataStoreNotFoundException;
import org.middleheaven.persistance.DataStoreProvider;
import org.middleheaven.persistance.DataStoreSchema;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.HashDataRow;
import org.middleheaven.persistance.ModelNotEditableException;
import org.middleheaven.persistance.RelatedDataSet;
import org.middleheaven.persistance.SearchPlan;
import org.middleheaven.persistance.SequenceNotFoundException;
import org.middleheaven.persistance.criteria.DataSetConstraint;
import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.building.ColumnNameValueLocator;
import org.middleheaven.persistance.criteria.building.ColumnValueConstraint;
import org.middleheaven.persistance.criteria.building.ExplicitValueLocator;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.persistance.db.metamodel.DataBaseModel;
import org.middleheaven.persistance.db.metamodel.DataBaseObjectModel;
import org.middleheaven.persistance.db.metamodel.DataBaseObjectType;
import org.middleheaven.persistance.db.metamodel.EditableDataBaseModel;
import org.middleheaven.persistance.db.metamodel.SequenceModel;
import org.middleheaven.persistance.db.metamodel.TableAlreadyExistsException;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.EnhancedMap;
import org.middleheaven.util.collections.Walker;
import org.middleheaven.util.criteria.CriterionOperator;

/**
 * A {@link DataStore} that reads data from a Relational Data Base Management System (RDBMS)
 */
public class RDBMSDataStoreProvider implements DataStoreProvider  {


	private final DataSource datasource;
	private final RDBMSDialect dialect;
	private final DataBaseMapper mapper;

	/**
	 * 
	 * Constructor.
	 * @param datasource the source of data.
	 * @param mapper the database mapper.
	 */
	public RDBMSDataStoreProvider (DataSource datasource , DataBaseMapper mapper){
		this.datasource = datasource;
		this.mapper = mapper;
		this.dialect = RDBMSDialectFactory.getDialect(this.datasource);
	}


	protected SearchPlan plan(DataSetCriteria criteria) {

		SearchPlan plan = new SearchPlan();
		plan.setCountOnly(false); // TODO determine
		plan.setDistinct(criteria.isDistinct());
		plan.setFowardOnly(true);
		plan.setReadOnly(true);
		plan.setMaxCount(criteria.getMaxCount());
		plan.setOffSet(criteria.getOffset());
		plan.setReadOnly(true);
		plan.setResultColumns(criteria.getResultColumns());
		plan.setConstraints(criteria.getDataSetRestrictions());
		plan.setOrdering(criteria.getOrderConstraints());
		plan.setGrouping(criteria.getGroupConstraint());

		for (RelatedDataSet rd : criteria.getRelatedDataSets()){

			TableRelation relation = new TableRelation(new LogicConstraint(rd.getRelationConstraint().getOperator()) , rd.getRelationOperator());
			relation.setSourceTableModel(mapper.getTableForDataSet(rd.getSourceDataSetModel().getName()));
			plan.add(relation);
			
			if (!rd.getRelationConstraint().getConstraints().isEmpty()){
				relation = new TableRelation(new LogicConstraint(rd.getRelationConstraint().getOperator()) , rd.getRelationOperator());
				
				
				for (DataSetConstraint c : rd.getRelationConstraint().getConstraints()){

					if (c instanceof ColumnValueConstraint) {
						QualifiedName q = ( (ColumnValueConstraint) c).getLeftValuelocator().getName();

						if (q != null){

							relation.setSourceTableModel(mapper.getColumnModel(q).getTableModel());
						}

						q = ( (ColumnValueConstraint) c).getRightValueLocator().getName();

						if (q != null){

							relation.setTargetTableModel(mapper.getColumnModel(q).getTableModel());
						}
					} else {
						throw new IllegalStateException("Should not happen");
					}

				}
				
				plan.add(relation);
			}

			
		}


		return plan;
	}

	protected long countSearchPlan(SearchPlan plan) {
		// TODO 
		return 0;
	}

	protected DataRowStream executeSearchPlan(SearchPlan plan) {
		ResultSet rs = null;
		Connection con = null;
		try {

			con = this.datasource.getConnection();

			RetriveDataBaseCommand command = dialect.createSelectCommand(plan, mapper);
			Log.onBook("SQL").trace(command.toString());

			command.execute( mapper, con, null);
			rs = command.getResult();

			// if dialect does not support offset
			if (!dialect.supportsOffSet()) {

				try {
					rs.absolute(plan.getOffSet() + 1);

				} catch (SQLFeatureNotSupportedException e){
					// iterate manually  until we arrive to the offset
					int off = 1;
					while (off < plan.getOffSet() && rs.next()) {
						//iterate
						off++;
					}
				}

			}

		} catch (SQLException e) {
			close(con);
			throw dialect.handleSQLException(e);
		}


		if (plan.isFowardOnly() && plan.isReadOnly()) {
			// fastlane
			try {
				return  ResultSetDataRowStream.newInstance(rs, mapper, dialect);
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			} 

		} else {

			try {
				// load all objects onto a list

				DataRow sourceRow = ResultSetDataRow.newInstance(rs, dialect);

				if (dialect.supportsCountLimit()) {
					ListDataRowStream stream = new ListDataRowStream();

					while (rs.next()) {
						stream.addRow(new HashDataRow(sourceRow));
					}
					return stream;
				} else {

					ListDataRowStream stream = new ListDataRowStream(plan.getMaxCount());

					int count = 0;
					while (rs.next() && count < plan.getMaxCount()) {

						stream.addRow(new HashDataRow(sourceRow));

						count++;

					}
					return stream;
				}
			} catch (SQLException e) {
				throw dialect.handleSQLException(e);
			} finally {
				try {
					rs.close();
					this.close(con);
				} catch (SQLException e) {
					throw dialect.handleSQLException(e);
				}
			}

		}

	}

	private void executeCommand(DataBaseCommand command) {

		Connection con = null;
		try {
			con = this.datasource.getConnection();

			command.execute(mapper, con, null);

		} catch (SQLException e) {
			Log.onBookFor(this.getClass()).trace("SQL : {0}", command.getSQL());
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStore getDataStore(DataStoreName name) throws DataStoreNotFoundException {
		if (isProviderDataStore(name)){
			return new RDBMSDataStore(DataStoreName.name(name.getName()));
		}

		throw new DataStoreNotFoundException();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProviderDataStore(DataStoreName name) {

		return dialect.existsDatabase(name.getName(), this.datasource );

	}


	private class RDBMSDataStore implements DataStore {


		private DataStoreName name;

		public RDBMSDataStore (DataStoreName name){
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataStoreName getName() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataStoreSchema getDataStoreSchema(final DataStoreSchemaName name) {


			return new DataStoreSchema(){

				/**
				 * {@inheritDoc}
				 */
				@Override
				public DataQuery query(DataSetCriteria criteria) {
					return new RDBMSDataQuery(this, plan(criteria));
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public DataSet getDataSet(String name) throws DataSetNotFoundException {
					return new RDBMSDataSet(this, name);
				}

				/**
				 * {@inheritDoc}
				 */
				@Override
				public void updateModel() throws ModelNotEditableException {
					updatePhysicalModel(name);

				}

				@Override
				public Sequence<Long> getSequence(
						String sequenceName) throws SequenceNotFoundException {

					return dialect.getSequence(datasource, sequenceName);


				}

			};
		}


	}


	private void updatePhysicalModel (DataStoreSchemaName name) throws ModelNotEditableException {

		EditableDataBaseModel dbModel = new EditableDataBaseModel();

		for (DBTableModel tm  : mapper.getTableModels()){
			if (tm.isEmpty()){
				throw new IllegalModelStateException("Table " + tm.getName() + " has no columns");
			}
			dbModel.addDataBaseObjectModel(EditableDBTableModel.valueOf(tm));
		}


		dialect.extendsDatabaseModel(dbModel);

		DataBaseModel existingDBModel = dialect.readDataBaseModel(name.getDatabaseName(), datasource);


		for (DataBaseObjectModel dbObject : dbModel) {

			DataBaseObjectModel existingModel = existingDBModel.getDataBaseObjectModel(
					dbObject.getName(), 
					dbObject.getType()
					);

			if (existingModel != null) {
				// alter objets
				if (dbObject.getType().equals(DataBaseObjectType.TABLE)) {

					EditableDBTableModel newModel = (EditableDBTableModel) dbObject;
					EditableDBTableModel oldModel = (EditableDBTableModel) existingModel;

					EditableDBTableModel diff = newModel.differenceTo(oldModel);

					// alter only creates, does not remove
					if (!diff.isEmpty()) {
						DataBaseCommand command = dialect.createAlterTableCommand(diff);
						executeCommand(command);
					}

				}
			} else {
				// create object
				try {
					if (dbObject.getType().equals(DataBaseObjectType.TABLE)) {
						EditableDBTableModel tm = (EditableDBTableModel) dbObject;

						DataBaseCommand command = dialect.createCreateTableCommand(tm);

						executeCommand(command);

						// create Indexes	
						// Group columns into the indexes 

						EnhancedMap<String,EnhancedCollection<DBColumnModel>> map = tm.columns().groupBy(new Classifier<String, DBColumnModel>(){

							@Override
							public String classify(DBColumnModel obj) {
								return obj.getUniqueGroupName();
							}

						});

						final List<DataBaseCommand> indexComands = new ArrayList<DataBaseCommand>(map.size());

						map.each( new Walker<Map.Entry<String,EnhancedCollection<DBColumnModel>>>(){

							@Override
							public void doWith(
									Entry<String, EnhancedCollection<DBColumnModel>> entry) {

								indexComands.add(dialect.createCreateIndexCommand(entry.getValue() , true));

							}

						});

						for (DataBaseCommand idxCommand :  indexComands){
							executeCommand(idxCommand);
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

	private class RDBMSDataQuery implements DataQuery {


		private DataStoreSchema dataStoreSchema;
		private SearchPlan searchPlan;

		public RDBMSDataQuery (DataStoreSchema dataStoreSchema, SearchPlan searchPlan){
			this.dataStoreSchema = dataStoreSchema;
			this.searchPlan = searchPlan;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataRowStream getRowStream() {
			return executeSearchPlan(searchPlan);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long rowCount() {
			return countSearchPlan(searchPlan);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			return rowCount() == 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DataQuery limit(int startAt, int maxCount) {
			return new RDBMSDataQuery (this.dataStoreSchema, searchPlan.limit(startAt , maxCount));
		}

	}

	private class RDBMSDataSet implements DataSet {


		private String name;
		private DataStoreSchema schema;

		public RDBMSDataSet (DataStoreSchema dataStoreSchema, String name){
			this.schema = dataStoreSchema;
			this.name = name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void insert(Collection<DataRow> dataRows) {

			if (dataRows.isEmpty()){
				return;
			}

			DBTableModel model = mapper.getTableForDataSet(name);

			if (model != null) {
				executeCommand(dialect.createInsertCommand(dataRows, model));
			}
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void delete(Collection<DataRow> dataRows) {

			if (dataRows.isEmpty()){
				return;
			}

			DBTableModel model = mapper.getTableForDataSet(name);

			if (model != null) {

				// transform the rows to a criteria of keys
				DataSetCriteria deleteCriteria = new DataSetCriteria();

				if (dataRows.size() == 1){
					LogicConstraint and = new LogicConstraint(LogicOperator.and());

					DataRow row = dataRows.iterator().next();

					for (DataColumn col : row){
						final DataColumnModel dcm = col.getModel();
						if (dcm.isInPrimaryKeyGroup()){
							and.addConstraint(new ColumnValueConstraint(
									new ColumnNameValueLocator(dcm.getName()),
									CriterionOperator.EQUAL,
									new ExplicitValueLocator(row.getColumn(dcm.getName()))
									));
						}
					}

				} else {
					LogicConstraint or = new LogicConstraint(LogicOperator.or());
					for (DataRow row : dataRows) { 
						LogicConstraint and = new LogicConstraint(LogicOperator.and());

						for (DataColumn col : row){
							final DataColumnModel dcm = col.getModel();
							if (dcm.isInPrimaryKeyGroup()){
								and.addConstraint(new ColumnValueConstraint(
										new ColumnNameValueLocator(dcm.getName()),
										CriterionOperator.EQUAL,
										new ExplicitValueLocator(row.getColumn(dcm.getName()))
										));
							}
						}

						or.addConstraint(and);
					}
				}



				executeCommand(dialect.createDeleteCommand(deleteCriteria, model));
			}


		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void delete(DataSetCriteria criteria) {
			DBTableModel model = mapper.getTableForDataSet(name);

			if (model != null){
				executeCommand(dialect.createDeleteCommand(criteria, model));
			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update(Collection<DataRow> dataRows) {
			if (dataRows.isEmpty()){
				return;
			}

			DBTableModel model = mapper.getTableForDataSet(name);

			if (model != null) {
				executeCommand(dialect.createUpdateCommand(dataRows, model));
			}
		}

	}




}
