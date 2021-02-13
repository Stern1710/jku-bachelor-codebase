package ControlSystem.model;

import ControlSystem.ui.Street;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
	private final StringProperty name;
	private final IntegerProperty mode;
	private final IntegerProperty lights;
	private final IntegerProperty warnsign;
	private final IntegerProperty road;
	
	private final int dbID;
	
	public Item(int dbId, Street street) {
		name = new SimpleStringProperty(street.getName());
		mode = new SimpleIntegerProperty(street.getMode().getValue());
		lights = new SimpleIntegerProperty(street.getLight().getValue());
		warnsign = new SimpleIntegerProperty(street.getWarnsign().getValue());
		road = new SimpleIntegerProperty(street.getRoad().getValue());
		this.dbID = dbId;
	}

	public StringProperty nameProperty() {
		return name;
	}
	
	public IntegerProperty modeProperty() {
		return mode;
	}
	
	public IntegerProperty lightsProperty() {
		return lights;
	}
	
	public IntegerProperty warnsignProperty() {
		return warnsign;
	}
	
	public IntegerProperty roadProperty() {
		return road;
	}
	
	public int getDbID(){
		return dbID;
	}
}

