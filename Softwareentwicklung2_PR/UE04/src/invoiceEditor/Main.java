package invoiceEditor;

import invoiceEditor.controller.ListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/list.fxml"));
		final Parent root = loader.load();
		ListController controller = loader.getController();
		controller.setStage(primaryStage);

		primaryStage.setTitle("Invoice");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
