package common.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Start the Project and runs the Traffic Detection and Control on all streets
 * @author Christopher Holzweber
 *
 */
public class SystemApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("../../ControlSystem/ui/mainUI.fxml"));
		final Parent root = loader.load();

		primaryStage.setTitle("Control system");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	/**
	 * Start System
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args); //ControlSystem UI
	}
}
