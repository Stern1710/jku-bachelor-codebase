package invoiceEditor.controller;

import invoiceEditor.model.InvoiceItem;
import invoiceEditor.model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import javax.activity.InvalidActivityException;
import java.io.IOException;
import java.text.DecimalFormat;

public class ListController {

    private final Model model;
    private Stage stage;

    @FXML
    private TableView<InvoiceItem> itemView;

    @FXML
    private TableColumn<InvoiceItem, String> idColumn;
    @FXML
    private TableColumn<InvoiceItem, String> nameColumn;
    @FXML
    private TableColumn<InvoiceItem, Double> priceUnitColumn;
    @FXML
    private TableColumn<InvoiceItem, Integer> unitColumn;
    @FXML
    private TableColumn<InvoiceItem, Double> discountColumn;
    @FXML
    private TableColumn<InvoiceItem, Double> totalPriceColumn;

    @FXML
    private Button addBtn;
    @FXML
    private Button removeBtn;

    public ListController () {
        this.model = new Model();
    }

    @FXML
    private void initialize() {
        itemView.itemsProperty().setValue(model.getItems());
        itemView.setEditable(true);
        itemView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("units"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));

        totalPriceColumn.setCellValueFactory(data -> {
            InvoiceItem item = data.getValue();
            return item.pricePerUnitProperty()
                    .multiply(item.unitsProperty())
                    .multiply(item.discountProperty().negate().add(1))
                    .asObject();
        });

        totalPriceColumn.setCellFactory(col -> new TableCell<InvoiceItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText("");
                } else {
                    DecimalFormat f = new DecimalFormat("#0.00");
                    setText(f.format(item));
                }
            }
        });

        unitColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Integer fromString(String string) {
                int selIndex = itemView.getSelectionModel().getSelectedIndex();
                Integer oldVal = selIndex >= 0 ? unitColumn.getCellData(model.getItems().get(selIndex)) : null;
                if (string == null) {
                    return oldVal != null ? oldVal : 1;
                }
                for (char c : string.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return oldVal != null ? oldVal : 1;
                    }
                }
                try {
                    int val = Integer.parseInt(string);
                    return val >= 1 ? val : oldVal;
                } catch (Exception e) {
                    return oldVal != null ? oldVal : 1;
                }
            }
        }));

        discountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                DecimalFormat f = new DecimalFormat("#0.00");
                return object != null ? f.format(object) : "";
            }

            @Override
            public Double fromString(String string) {
                int selIndex = itemView.getSelectionModel().getSelectedIndex();
                Double oldVal = selIndex >= 0 ? discountColumn.getCellData(model.getItems().get(selIndex)) : null;
                try {
                    if (string == null) {
                        return oldVal != null ? oldVal : 0d;
                    }
                    for (char c : string.toCharArray()) {
                        if (!Character.isDigit(c) && c != '.') {
                            return oldVal != null ? oldVal : 0d;
                        }
                    }
                    double value = Double.parseDouble(string);
                    return (value < 0d || value > 1d) ? oldVal : value;
                } catch (Exception e) {
                    return oldVal != null ? oldVal : 0d;
                }
            }
        }));


        addBtn.setOnAction(e -> {
            try {
                launchAddDialog();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        removeBtn.setOnAction(e -> {
            int index = itemView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                model.getItems().remove(index);
            }
        });
    }

    private void launchAddDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/addDialog.fxml"));
        Parent root = loader.load();
        AddController controller = loader.getController();
        Stage addStage = new Stage();

        controller.setPrimaryStage(addStage);
        controller.setModel(model);

        addStage.setTitle("Add new Invoice");
        addStage.setScene(new Scene(root));
        addStage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest((WindowEvent ev1) -> {
            try {
                model.getConnector().closeConnection();
            } catch (InvalidActivityException e) {
                e.printStackTrace();
            }
        });
    }


}
