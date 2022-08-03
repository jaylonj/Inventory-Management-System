package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Inventory;


public class Main extends Application {

    /**
     * Start the GUI by loading the main FXML and setting the GUI viewport
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage.setTitle("Home");
        stage.setScene(new Scene(root, 900, 350));
        stage.show();
    }

    /**
     * Launch the GUI
     *
     * @param args
     */
    public static void main(String[] args) {
        Inventory.addTestData();
        launch(args);
    }
}

