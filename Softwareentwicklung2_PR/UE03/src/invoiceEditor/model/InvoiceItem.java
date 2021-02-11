package invoiceEditor.model;

import javafx.beans.property.*;

public class InvoiceItem {
    private final StringProperty id;
    private final StringProperty name;
    private final DoubleProperty pricePerUnit;
    private final IntegerProperty units;
    private final DoubleProperty discount;

    public InvoiceItem(String id, String name, double pricePerUnit, int units, double discount) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.pricePerUnit = new SimpleDoubleProperty(pricePerUnit);
        this.units = new SimpleIntegerProperty(units);
        this.discount = new SimpleDoubleProperty(discount);
    }

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
}
