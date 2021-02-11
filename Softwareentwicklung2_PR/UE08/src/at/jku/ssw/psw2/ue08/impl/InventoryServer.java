package at.jku.ssw.psw2.ue08.impl;

import at.jku.ssw.psw2.ue08.model.InventoryModel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InventoryServer {
    public InventoryServer () {
        try {
            InventoryModel<InventoryItemImpl> inventory = new InventoryModelImpl();
            Registry registry = LocateRegistry.createRegistry(Constants.PORT);
            registry.bind(Constants.REG_NAME, inventory);
            System.out.println("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
