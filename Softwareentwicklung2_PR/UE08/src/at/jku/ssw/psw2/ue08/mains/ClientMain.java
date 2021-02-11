package at.jku.ssw.psw2.ue08.mains;

import at.jku.ssw.psw2.ue08.gui.InventoryGUI;
import at.jku.ssw.psw2.ue08.impl.Constants;
import at.jku.ssw.psw2.ue08.impl.InventoryItemImpl;
import at.jku.ssw.psw2.ue08.model.InventoryModel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {
    public static void main(String[] args) {
        //Start GUI 1 for testing
        startAGUI();
        //Start GUI 2 for testing
        startAGUI();
    }

    private static void startAGUI() {
        try {
            Registry registry = LocateRegistry.getRegistry(Constants.SERVER_ADR, Constants.PORT);
            final InventoryModel<InventoryItemImpl> model = (InventoryModel<InventoryItemImpl>) registry.lookup(Constants.REG_NAME);
            InventoryGUI.startGui(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
