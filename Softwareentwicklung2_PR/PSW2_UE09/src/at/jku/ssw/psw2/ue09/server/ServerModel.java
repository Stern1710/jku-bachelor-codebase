package at.jku.ssw.psw2.ue09.server;

import at.jku.ssw.psw2.ue09.model.InventoryException;
import at.jku.ssw.psw2.ue09.model.InventoryItem;
import at.jku.ssw.psw2.ue09.model.InventoryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ServerModel implements InventoryModel {

    private static final String DB_URL = "jdbc:derby:inventoryDB;create=true";
    private static final String TABLE_NAME = "inventory";
    private static final String TABLE_COLUMN_ID = "itemId";
    private static final String TABLE_COLUMN_NAME = "itemName";
    private static final String TABLE_COLUMN_DESCRIPTION = "itemDesc";
    private static final String TABLE_COLUMN_QUANTITY = "itemQty";

    static {
        final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        try {
            Class.forName(driver);
        } catch (java.lang.ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection dbConnection;

    public ServerModel() {
        this.dbConnection = null;
    }

    @Override
    public synchronized void open() throws InventoryException {
        if (dbConnection != null) {
            // we're already connected
            return;
        }

        try {
            dbConnection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }

        // check if the db is already set up
        try (ResultSet dbTables = dbConnection.getMetaData().getTables(null, null, null, null)) {
            while (dbTables.next()) {
                final String tableName = dbTables.getString("TABLE_NAME");
                if (TABLE_NAME.equalsIgnoreCase(tableName)) {
                    // the db is already initialized
                    return;
                }
            }
        } catch (SQLException e) {
            throw new InventoryException("Could not read available tables", e);
        }

        // set up the table containing our inventory items
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute(String.format(
                    "CREATE TABLE %s ("
                            + "%s INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                            + "%s VARCHAR(150), %s VARCHAR(1000), %s INT)",
                    TABLE_NAME, TABLE_COLUMN_ID, TABLE_COLUMN_NAME, TABLE_COLUMN_DESCRIPTION, TABLE_COLUMN_QUANTITY));
        } catch (SQLException e) {
            throw new InventoryException("Failed to create table \"" + TABLE_NAME + "\"", e);
        }
    }

    @Override
    public synchronized List<InventoryItem> getItems() throws InventoryException {
        if (dbConnection == null) {
            throw new InventoryException("Not connected to database");
        }

        try (final PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM " + TABLE_NAME)) {
            try (final ResultSet res = stmt.executeQuery()) {
                final List<InventoryItem> items = new ArrayList<>();
                while (res.next()) {
                    final int id = res.getInt(TABLE_COLUMN_ID);
                    final String name = res.getString(TABLE_COLUMN_NAME);
                    final String description = res.getString(TABLE_COLUMN_DESCRIPTION);
                    final int quantity = res.getInt(TABLE_COLUMN_QUANTITY);
                    final InventoryItem item = new InventoryItem(id, name, description, quantity);
                    items.add(item);
                }
                return items;
            }
        } catch (SQLException sqlException) {
            throw new InventoryException("Failed to get items", sqlException);
        }
    }

    @Override
    public int createItem(String name) throws InventoryException {
        if (name == null || name.isEmpty()) {
            throw new InventoryException("Name of new item must not be empty");
        }

        synchronized (this) {
            if (dbConnection == null) {
                throw new InventoryException("Not connected to database");
            }

            try (final PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" + TABLE_COLUMN_NAME + ", " + TABLE_COLUMN_DESCRIPTION + ", " + TABLE_COLUMN_QUANTITY + ") VALUES (?, '', 0)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                final int affectedRows = stmt.executeUpdate();
                if (affectedRows != 1) {
                    throw new RuntimeException("Failed to create new item \"" + name + "\"");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    return generatedKeys.getInt(1);
                }

            } catch (SQLException sqlException) {
                throw new InventoryException("Could not create new item \"" + name + "\"", sqlException);
            }
        }
    }

    @Override
    public InventoryItem getItem(int itemId) throws InventoryException {
        if (itemId <= 0) {
            throw new InventoryException.NoSuchItem(itemId);
        }

        synchronized (this) {
            if (dbConnection == null) {
                throw new InventoryException("Not connected to database");
            }

            try (final PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_ID + "=?")) {
                stmt.setInt(1, itemId);

                try (final ResultSet res = stmt.executeQuery()) {

                    if (!res.next()) {
                        throw new InventoryException.NoSuchItem(itemId);
                    }

                    final String name = res.getString(TABLE_COLUMN_NAME);
                    final String description = res.getString(TABLE_COLUMN_DESCRIPTION);
                    final int quantity = res.getInt(TABLE_COLUMN_QUANTITY);

                    if (!res.next()) {
                        return new InventoryItem(itemId, name, description, quantity);
                    } else {
                        throw new InventoryException(itemId, "Detected duplicate item id");
                    }
                }

            } catch (SQLException sqlException) {
                throw new InventoryException(itemId, "Failed to get item from database", sqlException);
            }
        }
    }

    @Override
    public void setDescription(int itemId, String description) throws InventoryException {
        if (itemId <= 0) {
            throw new InventoryException.NoSuchItem(itemId);
        }

        synchronized (this) {
            if (dbConnection == null) {
                throw new InventoryException("Not connected to database");
            }

            try (final PreparedStatement stmt = dbConnection.prepareStatement("UPDATE " + TABLE_NAME + " SET " + TABLE_COLUMN_DESCRIPTION + "=? WHERE " + TABLE_COLUMN_ID + "=?")) {
                stmt.setString(1, description);
                stmt.setInt(2, itemId);
                final int affectedRows = stmt.executeUpdate();
                if (affectedRows != 1) {
                    throw new InventoryException(itemId, "Update did not change description");
                }

            } catch (SQLException sqlException) {
                throw new InventoryException(itemId, "Failed to set description", sqlException);
            }
        }
    }

    @Override
    public void changeQuantity(int itemId, int difference) throws InventoryException {
        if (itemId <= 0) {
            throw new InventoryException.NoSuchItem(itemId);
        }

        synchronized (this) {
            if (dbConnection == null) {
                throw new InventoryException("Not connected to database");
            }

            final String sql = "UPDATE " + TABLE_NAME + " SET " + TABLE_COLUMN_QUANTITY + " = " + TABLE_COLUMN_QUANTITY + " + " + difference + " WHERE " + TABLE_COLUMN_ID + "=?";
            try (final PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
                stmt.setInt(1, itemId);
                final int affectedRows = stmt.executeUpdate();
                if (affectedRows != 1) {
                    throw new InventoryException(itemId, "Update did not change quantity");
                }

            } catch (SQLException sqlException) {
                throw new InventoryException(itemId, "Failed to change quantity", sqlException);
            }
        }
    }

    @Override
    public void deleteItem(int itemId) throws InventoryException {
        if (itemId <= 0) {
            throw new InventoryException.NoSuchItem(itemId);
        }

        synchronized (this) {
            if (dbConnection == null) {
                throw new InventoryException("Not connected to database");
            }

            try (final PreparedStatement stmt = dbConnection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_ID + "=?")) {
                stmt.setInt(1, itemId);
                final int affectedRows = stmt.executeUpdate();
                if (affectedRows != 1) {
                    throw new InventoryException(itemId, "Deletion did not remove item");
                }
            } catch (SQLException sqlException) {
                throw new InventoryException("Failed to delete item", sqlException);
            }
        }
    }

    @Override
    public synchronized void close() throws InventoryException {
        try {
            dbConnection.close();
        } catch (SQLException sqlException) {
            throw new InventoryException("Error while closing database");
        } finally {
            dbConnection = null;
        }
    }
}
