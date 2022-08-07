package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Inventory;


/**
 * <p>
 *     FUTURE ENHANCEMENTS:
 *
 * When adding associated parts to a product, prompting the user on how many of a particular part they would be adding,
 * and implementing a for-loop to add the specified number of parts. In the current version of the system, the user
 * would have to click the "Add" button 55 times if they wanted to add 55 of the same part to a product.
 *
 * A tracker variable for how many of one part has been added to a product would also be helpful.
 *
 * Another enhancement would be in removing associated parts. Prompting the user on if they would like to remove all
 * of that one part or just reduce the amount would be helpful. If the part is screws for a device, the user may have
 * added too many screws and needs to reduce the value from 24 to 12. A while-loop could be implemented that removes
 * the screws until the tracker has reached the new desired amount.
 *
 * A pricing enhancement could be implemented to help the user dictate the price for a product based on the price of the
 * parts that comprise the product. A value of the sum of the price of the parts could be displayed in the 'Add Product'
 * or 'Modify Product' window as the parts are individually added or removed.
 * The value could be displayed as a 'Suggested Price'.
 * </p>
 */

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

