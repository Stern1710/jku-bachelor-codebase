package ControlSystem;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ControlSystem.model.Item;
import ControlSystem.ui.Street;
import common.Participant.Roads;
import common.TrafficControlDetect.StreetMode;
import common.TrafficControlDetect.TrafficLight;
import common.TrafficControlDetect.Warnsign;

public class DBManager {
	// DB QUERY STRINGS
	private static final String DB_URL = "jdbc:derby:demoDB;create=true";
	private static final String TABLE_NAME = "Streets";
	private static final String STREET_ID = "ID";
	private static final String STREET_NAME = "Name";
	private static final String STREET_MODE = "Mode";
	private static final String STREET_LIGHT = "Light";
	private static final String STREET_WARNSIGN = "Warnsign";
	private static final String STREET_ROAD = "Roadtype";

	private static final String IMPORT_ITEMS = "SELECT * from " + TABLE_NAME;

	private static final String INSERT_ITEM = "INSERT INTO " + TABLE_NAME + " (" + STREET_NAME + ", " + STREET_MODE
			+ ", " + STREET_LIGHT + "," + STREET_WARNSIGN + "," + STREET_ROAD + ") VALUES(?,?,?,?,?)";
	private static final int INSERT_STREET_PARAM_NAME = 1;
	private static final int INSERT_STREET_PARAM_MODE = 2;
	private static final int INSERT_STREET_PARAM_LIGHT = 3;
	private static final int INSERT_STREET_PARAM_WARNSIGN = 4;
	private static final int INSERT_STREET_PARAM_ROAD = 5;

	private static final String UPDATE_STREET_PROPERTY = "UPDATE " + TABLE_NAME + " SET %s=? WHERE " + STREET_ID + "=?";
	private static final int UPDATE_PARAM_VALUE = 1;
	private static final int UPDATE_PARAM_ID = 2;

	private static final String DELETE_ITEM = "DELETE from " + TABLE_NAME + " WHERE " + STREET_NAME + "=?";
	private static final int DELETE_PARAM_ID = 1;
	private static final String DELETE_ITEM_UI = "DELETE from " + TABLE_NAME + " WHERE " + STREET_ID + "=?";
	
	// DBManager fields
	private Connection dbConnection;

	public DBManager() {
		dbConnection = null;
	}
	/**
	 * Open a new connection to the DB.
	 */
	public void openConnection(boolean newDb) {
		if (dbConnection != null)
			return;
		try {
			dbConnection = DriverManager.getConnection(DB_URL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (newDb) {
			deleteTables();
			createTables();
		}
	}

	/**
	 * Close DB connection.
	 */
	void closeConnection() {
		if (dbConnection == null) {
			throw new IllegalStateException("Connection was already closed");
		}

		try {
			dbConnection.close();
		} catch (SQLException e) {
			throw new RuntimeException("Could not close database", e);
		}
	}

	/**
	 * Delete table.
	 */
	private void deleteTables() {
		try (Statement statement = dbConnection.createStatement()) {
			statement.execute(String.format("DROP TABLE %s", TABLE_NAME));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create table for database items.
	 */
	private void createTables() {
		try (Statement statement = dbConnection.createStatement()) {
			statement.execute(String.format(
					"CREATE TABLE %s ("
							+ "%s INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
							+ "%s VARCHAR(20), " + "%s INT, " + "%s INT, " + "%s INT, " + "%s INT)",
					TABLE_NAME, STREET_ID, STREET_NAME, STREET_MODE, STREET_LIGHT, STREET_WARNSIGN, STREET_ROAD));
			System.out.println("Table successfully created!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes an item using the database ID.
	 */
	public void deleteItem(int id) {
		try (final PreparedStatement deleteStatement = dbConnection.prepareStatement(String.format(DELETE_ITEM_UI))) {
			deleteStatement.setInt(DELETE_PARAM_ID, id);
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Deletes an item using the name.
	 */
	public void deleteItem(String name) {
		try (final PreparedStatement deleteStatement = dbConnection.prepareStatement(String.format(DELETE_ITEM))) {
			deleteStatement.setString(DELETE_PARAM_ID, name);
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an item to the table.
	 */
	public int addItem(String name, int mode, int lights, int warnsignal, int roadtype) {
		try (PreparedStatement insertStatement = dbConnection.prepareStatement(INSERT_ITEM,
				Statement.RETURN_GENERATED_KEYS)) {

			insertStatement.setString(INSERT_STREET_PARAM_NAME, name);
			insertStatement.setInt(INSERT_STREET_PARAM_MODE, mode);
			insertStatement.setInt(INSERT_STREET_PARAM_WARNSIGN, lights);
			insertStatement.setInt(INSERT_STREET_PARAM_LIGHT, warnsignal);
			insertStatement.setInt(INSERT_STREET_PARAM_ROAD, roadtype);

			final int affectedRows = insertStatement.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to add new person to database");
			}

			try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
				generatedKeys.next();
				return generatedKeys.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Updates a double property.
	 */
	public void updateDoubleProperty(String colName, double val, int id) {
		try (final PreparedStatement updateStatement = dbConnection
				.prepareStatement(String.format(UPDATE_STREET_PROPERTY, colName))) {
			updateStatement.setDouble(UPDATE_PARAM_VALUE, val);
			updateStatement.setInt(UPDATE_PARAM_ID, id);
			updateStatement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Updates an integer property.
	 */
	public void updateIntegerProperty(String colName, int val, int id) {
		try (final PreparedStatement updateStatement = dbConnection
				.prepareStatement(String.format(UPDATE_STREET_PROPERTY, colName))) {
			updateStatement.setInt(UPDATE_PARAM_VALUE, val);
			updateStatement.setInt(UPDATE_PARAM_ID, id);
			updateStatement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int countItems() {
		int cnt = 0;
		try {
			try (PreparedStatement itemQuery = dbConnection.prepareStatement(IMPORT_ITEMS)) {
				try (final ResultSet items = itemQuery.executeQuery()) {
					while (items.next()) {
						cnt++;
				}
				}
			}

		}catch (Exception e) {}
		return cnt;
	}
	
	/**
	 * Selects all data of the table and returns is as List of Items.
	 */
	public List<Item> importItems() {
		final List<Item> imported = new ArrayList<>();
		try {
			try (PreparedStatement itemQuery = dbConnection.prepareStatement(IMPORT_ITEMS)) {
				try (final ResultSet items = itemQuery.executeQuery()) {
					while (items.next()) {
						final Item item = new Item(items.getInt(STREET_ID),
								new Street(items.getString(STREET_NAME), StreetMode.valueOf(items.getInt(STREET_MODE)),
										TrafficLight.valueOf(items.getInt(STREET_LIGHT)),
										Warnsign.valueOf(items.getInt(STREET_WARNSIGN)),
										Roads.valueOf(items.getInt(STREET_ROAD))));
						imported.add(item);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} 

		return imported;
	}

	/**
	 * Performs a backup to c:/mybackups/[todaysdate].
	 */
	public void backUpDatabase() throws SQLException {
		// Get today's date as a string:
		java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String backupdirectory = "c:/mybackups/" + todaysDate.format((java.util.Calendar.getInstance()).getTime());

		CallableStatement cs = dbConnection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
		cs.setString(1, backupdirectory);
		cs.execute();
		cs.close();
		System.out.println("Backed up database to " + backupdirectory);
	}
}
