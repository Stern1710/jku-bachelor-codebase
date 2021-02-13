package ControlSystem.ui;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

import ControlSystem.model.Item;
import ControlSystem.model.Model;
import common.Participant.Roads;
import common.TrafficControlDetect.StreetMode;
import common.TrafficControlDetect.TrafficLight;
import common.TrafficControlDetect.Warnsign;

public class Controller {

	private final Model model;

	public Controller() {
		model = new Model();
	}

	@FXML
	private TableView<Item> streetView;
	
	@FXML
	private TableColumn<Item, String> idColumn;
	
	@FXML
	private TableColumn<Item, String> nameColumn;
	
	@FXML
	private TableColumn<Item, String> modeColumn;
	
	@FXML
	private TableColumn<Item, String> lightsColumn;
	
	@FXML
	private TableColumn<Item, String> warnsignColumn;
	
	@FXML
	private TableColumn<Item, String> roadColumn;

	@FXML
	private Button addBtn;
	@FXML
	private Button removeBtn;

	@FXML
	private void initialize() {
		streetView.itemsProperty().setValue(model.invoiceItems);
		streetView.setEditable(true);
		streetView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); 
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		idColumn.setCellValueFactory(new PropertyValueFactory<>("dbID"));

		warnsignColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Warnsign.valueOf(cellData.getValue().warnsignProperty().intValue()).toString()));
		lightsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(TrafficLight.valueOf(cellData.getValue().lightsProperty().intValue()).toString()));
		roadColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Roads.valueOf(cellData.getValue().roadProperty().intValue()).toString()));
		modeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(StreetMode.valueOf(cellData.getValue().modeProperty().intValue()).toString()));


		addBtn.setOnAction(e -> {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("add.fxml"));
			Parent root;
			try {
				root = loader.load();
				ControllerAdd controllerAdd = loader.getController();
				controllerAdd.setModel(model);
				Stage stage = new Stage();
				stage.setTitle("Add item");
				stage.setScene(new Scene(root));
				stage.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		removeBtn.setOnAction(e -> {
			if (streetView.getSelectionModel().getSelectedIndex() >= 0) {
				model.getDBManager().deleteItem(streetView.getSelectionModel()
						.getSelectedItem().getDbID());
				model.invoiceItems.remove(streetView.getSelectionModel()
						.getSelectedIndex());
			}
		});
	}
}