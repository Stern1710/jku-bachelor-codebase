package invoiceEditor.controller;

import invoiceEditor.model.InvoiceItem;
import invoiceEditor.model.Model;
import invoiceEditor.product.DummyProductList;
import invoiceEditor.product.Product;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddController {

    private Stage stage;
    private Model model;

    @FXML
    private ComboBox products;
    @FXML
    private TextField units;
    @FXML
    private TextField discount;
    @FXML
    private Button add;

    @FXML
    private void initialize() {
        products.setItems(new ReadOnlyListWrapper(FXCollections.observableArrayList(DummyProductList.getDummyProducts())));

        add.setOnAction(e -> {
            try {
                Product prod = getProductByString(products.getSelectionModel().getSelectedItem());
                if (prod != null) {
                    double disc = Double.parseDouble(discount.getText());

                    if (disc < 0.0) {
                        disc = 0;
                    } else if (disc > 1.0) {
                        disc = 1;
                    }

                    model.items.add(new InvoiceItem(prod.getID(), prod.getName(), prod.getPrice(), Integer.parseInt(units.getText()), disc));
                    stage.close();
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    /**
     * Sets the model where the new items should be added
     * @param model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Gets a product by its toString representation.
     * Please don't kill me for this method.
     * @param prod String which hopefully holds the product string
     * @return The according product or null
     */
    private Product getProductByString(Object prod) {
        for (Product p : DummyProductList.getDummyProducts()) {
            if (p.toString().equals(prod.toString())) {
                return p;
            }
        }
        return null;
    }
}
