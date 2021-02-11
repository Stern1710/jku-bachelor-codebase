package at.jku.ssw.psw2.ue09.client;

import at.jku.ssw.psw2.ue09.model.InventoryException;
import at.jku.ssw.psw2.ue09.model.InventoryModel;

public class InventoryClient {
    public static void main(String[] args) throws InventoryException {
        final InventoryModel model = new ClientModel();

        model.open();

        // insert some demo data
        if (model.getItems().size() == 0) {
            insertDemoData(model);
        }

        // start the gui
        InventoryGUI.startGui(model);
    }

    private static void insertDemoData(InventoryModel model) throws InventoryException {
        model.createItem("Apples");
        model.createItem("Oranges");
        model.createItem("Bananas");
        model.createItem("Cherries");
    }
}
