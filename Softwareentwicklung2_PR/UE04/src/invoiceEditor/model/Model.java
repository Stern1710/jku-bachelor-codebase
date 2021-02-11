package invoiceEditor.model;

import invoiceEditor.database.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.activity.InvalidActivityException;

public class Model {
    private final ObservableList<InvoiceItem> items = FXCollections.observableArrayList();
    private final DBConnector con;

    public Model () {
        con = new DBConnector();
        try {
            con.openConnection(false);
            items.addAll(con.getItems());
            setTableListener();
        } catch (InvalidActivityException e) {
            e.printStackTrace();
        }

    }

    /*
        Getter and Setter
    */
    public DBConnector getConnector () {
        return con;
    }

    public ObservableList<InvoiceItem> getItems() {
        return items;
    }

    /*
        Sets a listener that works for adding and deleting items and calling the corresponding db operations
    */
    private void setTableListener() {
        items.addListener((ListChangeListener<InvoiceItem>) c -> {
            while(c.next()) {
                if (c.wasAdded()) {
                    for (InvoiceItem it : c.getAddedSubList()) {
                        int id = con.addInvoiceItem(it);
                        it.setDatabaseID(id);
                        it.setCon(con);
                    }
                } else if (c.wasRemoved()) {
                    for (InvoiceItem it : c.getRemoved()) {
                        con.deleteInvoiceItem(it);
                    }
                }
            }
        });
    }
}
