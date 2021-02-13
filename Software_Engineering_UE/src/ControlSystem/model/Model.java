package ControlSystem.model;
import ControlSystem.DBManager;
import ControlSystem.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	private DBManager dbm = null;
	public ObservableList<Item> invoiceItems = null;
	public ControlSystem cs;
	
	@SuppressWarnings("static-access")
	public Model() {
		dbm = new DBManager();
		dbm.openConnection(true);
		cs = new ControlSystem(5000); 
		cs.setDBConnection(dbm);
		invoiceItems = FXCollections.observableArrayList(dbm.importItems());
		cs.startSubsystems(this);
	}
	
	public DBManager getDBManager(){
		return dbm;
	}
}