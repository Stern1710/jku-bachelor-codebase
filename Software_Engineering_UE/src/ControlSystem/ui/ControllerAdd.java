package ControlSystem.ui;

import ControlSystem.model.Item;
import ControlSystem.model.Model;
import common.Participant.Roads;
import common.TrafficControlDetect.StreetMode;
import common.TrafficControlDetect.TrafficLight;
import common.TrafficControlDetect.Warnsign;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ControllerAdd {
	private Model model;

	public ControllerAdd() {
		model = null;
	}
	
	public void setModel(Model m) {
		model = m;
	}

	@FXML
	private TextField txtName;
	
	@FXML
	private ComboBox<StreetMode>  cmbMode;
	
	@FXML
	private ComboBox<TrafficLight>  cmbLights;
	
	@FXML
	private ComboBox<Roads>  cmbType;
	
	@FXML
	private ComboBox<Warnsign>  cmbWarnsign;
	
	
	
	@FXML
	private Button addBtn;
	
	@FXML
	private Label statusLabel;

	@FXML
	private void initialize() {
		
		cmbMode.setItems(FXCollections.observableArrayList(StreetMode.values()));
		cmbLights.setItems(FXCollections.observableArrayList(TrafficLight.values()));
		cmbType.setItems(FXCollections.observableArrayList(Roads.values()));
		cmbWarnsign.setItems(FXCollections.observableArrayList(Warnsign.values()));
		cmbMode.getSelectionModel().selectFirst();
		cmbLights.getSelectionModel().selectFirst();
		cmbType.getSelectionModel().selectFirst();
		cmbWarnsign.getSelectionModel().selectFirst();
		
		
		addBtn.setOnAction(e -> {
			try {
				int newItemID = model.getDBManager().addItem(
						txtName.getText(),
						cmbMode.getSelectionModel().getSelectedItem().getValue(), 
						cmbLights.getSelectionModel().getSelectedItem().getValue(), 
						cmbWarnsign.getSelectionModel().getSelectedItem().getValue(), 
						cmbType.getSelectionModel().getSelectedItem().getValue());
				
				System.out.println("new id" +newItemID + "type " + cmbType.getSelectionModel().getSelectedItem().getValue());

				
				Item item = new Item(newItemID,new Street(txtName.getText(),
						cmbMode.getSelectionModel().getSelectedItem(), 
						cmbLights.getSelectionModel().getSelectedItem(), 
						cmbWarnsign.getSelectionModel().getSelectedItem(), 
						cmbType.getSelectionModel().getSelectedItem()));
				model.invoiceItems.add(item);
				System.out.println(item.roadProperty());
				statusLabel.setText("Success");
			}catch (Exception ex) {
				statusLabel.setText("Error - Wrong user input");
				System.out.println(ex);
			}
		});
	}
}