package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPart implements Initializable {

    public Button addPartCancel;
    public Button addPartSaveButton;
    private Stage stage;
    private Scene scene;
    public RadioButton addPartInHouseButton;
    public RadioButton addPartOutsourcedButton;
    public TextField addPartName;
    public TextField addPartInStock;
    public TextField addPartPrice;
    public TextField addPartMax;
    public TextField addPartMin;
    public TextField addPartToggle;
    public Label addPartToggleLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Assigning values to id, name, stock, max, and min depending on the user's input.
     * The system checks for type mismatches and sends an error message if
     * a mismatch has occurred. The values of min and max are also checked to make sure that
     * min is less than max.
     *
     * If OutSourced is selected, a new Outsource object is created and added to allParts and companyName
     * is prompted and initialized.
     * If In-House is selected, a new InHouse object is created and added to allParts and machineId
     * is prompted, converted to an integer and then initialized.
     *
     *
     * @param event - User clicks the save button
     */

    public void handleSave(ActionEvent event) throws IOException {
        try {
            int id = Inventory.getAllParts().size() + Inventory.partIdGenerator.addAndGet(2) + 1000;
            String name = addPartName.getText();
            int stock = Integer.parseInt(addPartInStock.getText());
            double price = Double.parseDouble(addPartPrice.getText());
            int max = Integer.parseInt(addPartMax.getText());
            int min = Integer.parseInt(addPartMin.getText());

            // Checks for an inventory input error
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Issue");
                alert.setContentText("Maximum MUST be GREATER than minimum!");
                alert.showAndWait();
            }
            // Compares the values of the minimum inventory amount and the current inventory amount
            // to ensure there is a sufficient part inventory in stock
            else if (stock < min){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Issue");
                alert.setContentText("Part inventory is lower than the minimum allowed. Please check your inputs.");
                alert.showAndWait();
            }
            else {
                if (addPartOutsourcedButton.isSelected()) {
                    String company = addPartToggle.getText();
                    Part part = new Outsourced(id, name, price, stock, min, max, company);
                    Inventory.addPart(part);
                } else if (addPartInHouseButton.isSelected()) {
                    int machineId = Integer.parseInt(addPartToggle.getText());
                    Part part = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.addPart(part);
                }
                returnToMainMenu(event);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Incorrect Inputs");
            alert.setContentText("Be sure that your values are in the appropriate format and either " +
                    "In House or Outsourced is selected!");
            alert.showAndWait();
        }
    }

    /**
    * Toggles the label when inHouse is clicked to show "Machine ID"
     * @param event = InHouse is clicked by the user
    */
    public void handleAddPartInHouse(ActionEvent event) {
        addPartToggleLabel.setText("Machine ID");
    }

    /**
     * Toggles the label when Outsourced is clicked to show "Company Name"
     * @param event - OutSourced is clicked by the user
     */
    public void handleAddPartOutsourced(ActionEvent event){
        addPartToggleLabel.setText("Company Name");
    }

    /**
     * Returns the system to the main menu when Cancel is clicked
     * @param event - the cancel button is clicked
     * @throws IOException
     */
    public void handleCancel(ActionEvent event) throws IOException{ returnToMainMenu(event); }


    /**
     * Method created to give an easier visual of the program returning to the main menu
     * @param event - Returns the user to the main screen
     * @throws IOException
     */
    public void returnToMainMenu(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
