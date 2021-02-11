package invoiceEditor.model;

import invoiceEditor.database.DBConnector;
import javafx.beans.property.*;

public class InvoiceItem {
    private final StringProperty id;
    private final StringProperty name;
    private final DoubleProperty pricePerUnit;
    private final IntegerProperty units;
    private final DoubleProperty discount;
    private int databaseID;
    private DBConnector con;

    public InvoiceItem(String id, String name, double pricePerUnit, int units, double discount) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.pricePerUnit = new SimpleDoubleProperty(pricePerUnit);
        this.units = new SimpleIntegerProperty(units);
        this.discount = new SimpleDoubleProperty(discount);

        this.units.addListener((observable, oldValue, newValue) -> {
            con.updateItem("units", newValue, databaseID);
        });

        this.discount.addListener((observable, oldValue, newValue) -> {
           con.updateItem("discount", newValue, databaseID);
        });
    }

    /*
        Getter and Setter
     */
    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty pricePerUnitProperty() {
        return pricePerUnit;
    }

    public IntegerProperty unitsProperty() {
        return units;
    }

    public DoubleProperty discountProperty() {
        return discount;
    }

    public void setDatabaseID(int databaseID) {
        this.databaseID = databaseID;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public void setCon(DBConnector con) {
        this.con = con;
    }
}
