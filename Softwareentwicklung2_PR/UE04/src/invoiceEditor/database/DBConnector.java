package invoiceEditor.database;

import invoiceEditor.model.InvoiceItem;

import javax.activity.InvalidActivityException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
    /*
        static strings for db operations
    */
    private static final String DB_URL = "jdbc:derby:InvoiceDB;create=true";

    private static final String TABLE_NAME = "Invoices";
    private static final String INVOICE_TABLE_ID = "tabID";
    private static final String INVOICE_ID = "id";
    private static final String INVOICE_NAME = "name";
    private static final String INVOICE_UNIT_PRICE = "pricePerUnit";
    private static final String INVOICE_UNITS = "units";
    private static final String INVOICE_DISCOUNT = "discount";

    private static final String INSERT_INVOICE_ITEM = "INSERT INTO " + TABLE_NAME + "(" + INVOICE_ID + ", " + INVOICE_NAME
            + ", " + INVOICE_UNIT_PRICE + ", " + INVOICE_UNITS + ", " + INVOICE_DISCOUNT + ") VALUES(?,?,?,?,?)";
    private static final int INSERT_INVOICE_ID = 1;
    private static final int INSERT_INVOICE_NAME = 2;
    private static final int INSERT_INVOICE_UNIT_PRICE = 3;
    private static final int INSERT_INVOICE_UNITS = 4;
    private static final int INSERT_INVOICE_DISCOUNT = 5;

    private static final String DELETE_INVOICE_ITEM = "DELETE FROM " + TABLE_NAME + " WHERE " + INVOICE_TABLE_ID + "= ?";
    private static final int DELETE_INVOICE_ID = 1;

    private static final String UPDATE_INVOICE_ITEM = "UPDATE " + TABLE_NAME + " SET %s=? WHERE "
            + INVOICE_TABLE_ID + "=?";
    private static final int UPDATE_FIELD_VALUE = 1;
    private static final int UPDATE_INVOICE_TABLE_ID= 2;

    private static final String SELECT = "SELECT * FROM " + TABLE_NAME;

    /* Other fields */
    private Connection dbConnection;

    public DBConnector() {
        dbConnection = null;
    }

    /* DB connection methods */
    public void openConnection(boolean newDB) throws InvalidActivityException {
        if (dbConnection != null) {
            throw new InvalidActivityException("Connection is already opened, cannot open new one!");
        }

        try {
            dbConnection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (newDB) {
            dropTables();
            createTables();
        }
    }

    public void closeConnection() throws InvalidActivityException {
        if (dbConnection == null) {
            throw new InvalidActivityException("Cannot close non-opened connection");
        }
        try {
            dbConnection.close();
        } catch (SQLException e) {
            throw new InvalidActivityException("Error while closing the dbConnection");
        }
    }

    /* Helper methods */
    private void createTables() throws InvalidActivityException {
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute(String.format("CREATE TABLE %s ("
                            + "%s INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                            "%s VARCHAR(30), " + //Id of item
                            "%s VARCHAR(30), " + //Name
                            "%s DOUBLE," + //Invoice price per unit
                            "%s INT," + //Unit counter
                            "%s DOUBLE)", //Discount amount
                    TABLE_NAME, INVOICE_TABLE_ID, INVOICE_ID, INVOICE_NAME, INVOICE_UNIT_PRICE, INVOICE_UNITS, INVOICE_DISCOUNT));
        } catch (SQLException e) {
            throw new InvalidActivityException("Could not create new tables in the database");
        }
    }

    private void dropTables() throws InvalidActivityException {
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute(String.format("DROP TABLE %s", TABLE_NAME));
        } catch (SQLException e) {
            throw new InvalidActivityException("Could not drop tables in the database");
        }
    }

    /* Data getting */
    public List<InvoiceItem> getItems() {
        final List<InvoiceItem> newItems = new ArrayList<>();

        try (PreparedStatement select = dbConnection.prepareStatement(SELECT)) {
            try (final ResultSet items = select.executeQuery()) {
                while (items.next()) {
                    InvoiceItem item = new InvoiceItem(items.getString(INVOICE_ID), items.getString(INVOICE_NAME),
                            items.getDouble(INVOICE_UNIT_PRICE), items.getInt(INVOICE_UNITS),
                            items.getDouble(INVOICE_DISCOUNT));
                    item.setDatabaseID(items.getInt(INVOICE_TABLE_ID));
                    item.setCon(this);

                    newItems.add(item);
                }
            }

            return newItems;
        } catch (SQLException e) {
            throw new RuntimeException("There was a general error when trying to delete a invoice from the database");
        }
    }

    /* Item manipulation */
    public int addInvoiceItem(InvoiceItem item) {
        try (PreparedStatement insert = dbConnection.prepareStatement(INSERT_INVOICE_ITEM, Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(INSERT_INVOICE_ID, item.idProperty().get());
            insert.setString(INSERT_INVOICE_NAME, item.nameProperty().get());
            insert.setDouble(INSERT_INVOICE_UNIT_PRICE, item.pricePerUnitProperty().get());
            insert.setInt(INSERT_INVOICE_UNITS, item.unitsProperty().get());
            insert.setDouble(INSERT_INVOICE_DISCOUNT, item.discountProperty().get());

            final int affectedRows = insert.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Failed to add a new item to the database");
            }
            //Access key of inserted column
            try (ResultSet genKeys = insert.getGeneratedKeys()) {
                genKeys.next();
                return genKeys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("There was a general error when trying to insert a new invoice to the database");
        }
    }

    public void updateItem(String field, Number value, int dbID) {
        try {
            PreparedStatement update = dbConnection.prepareStatement(String.format(UPDATE_INVOICE_ITEM, field));
            update.setInt(UPDATE_INVOICE_TABLE_ID, dbID);

            if (INVOICE_UNITS.equals(field)) {
                update.setInt(UPDATE_FIELD_VALUE, (int)value);
            } else {
                update.setDouble(UPDATE_FIELD_VALUE, (double)value);
            }

            update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteInvoiceItem (InvoiceItem item) {
        try (PreparedStatement delete = dbConnection.prepareStatement(DELETE_INVOICE_ITEM)) {
            delete.setInt(DELETE_INVOICE_ID, item.getDatabaseID());

            final int affectedRows = delete.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Failed to add a new item to the database");
            }

        } catch (SQLException e) {
            throw new RuntimeException("There was a general error when trying to delete a invoice from the database");
        }
    }

}
