package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class that houses the different methods to handle the actions of the users once the Modify button
 * is clicked on the Part TableView
 */

public class ModifyPart implements Initializable {
    public RadioButton modPartInHouseButton;
    public RadioButton modPartOutsourcedButton;
    public TextField modPartToggle;
    public Label modPartToggleText;
    public TextField modPartId;
    public TextField modPartName;
    public TextField modPartInStock;
    public TextField modPartPrice;
    public TextField modPartMax;
    public TextField modPartMin;
    public Button modPartCancel;
    public Button modPartSaveButton;
    int index = Main.getPartToModify();
    Stage stage;
    Scene scene;

    private Part currentPart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){}

    /**
     * Fills the part with the passed part values from the main controller.
     * Checks for the part being In-House or Outsourced.
     * @param part - Used to set the current part
     */
    public void passSelectedPart(Part part) {
        this.currentPart = part;
        modPartId.setText(String.valueOf(currentPart.getId()));
        modPartName.setText(String.valueOf(currentPart.getName()));
        modPartInStock.setText(String.valueOf(currentPart.getStock()));
        modPartPrice.setText(String.valueOf(currentPart.getPrice()));
        modPartMax.setText(String.valueOf(currentPart.getMax()));
        modPartMin.setText(String.valueOf(currentPart.getMin()));
        if (currentPart instanceof InHouse) {
            modPartToggle.setText(String.valueOf(((InHouse) currentPart).getMachineId()));
            modPartInHouseButton.setSelected(true);
            modPartToggleText.setText("Machine ID");
        }
        if (currentPart instanceof Outsourced) {
            modPartToggle.setText(String.valueOf(((Outsourced) currentPart).getCompanyName()));
            modPartOutsourcedButton.setSelected(true);
            modPartToggleText.setText("Company Name");
        }
    }

    /**
     * Toggles the label if In-House is selected.
     * @param event - Clicking on the In-House radio button
     */
    public void handleModPartInHouse(ActionEvent event){
        modPartToggleText.setText("Machine ID");
    }

    /**
     * Toggles the label if Outsourced is selected.
     * @param event - Clicking on the Outsourced radio button
     */
    public void handleModPartOutsourced(ActionEvent event){
        modPartToggleText.setText("Company Name");
    }

    /**
     * Values for the modified part are entered by the user, checking for type mismatches and ensuring
     * that min is less than max.
     * In-House and Outsourced are then checked to ensure that the part is correctly attributed to either field.
     * @param event - Clicking on the Save button
     * @throws IOException
     * <p>
     * LOGIC ERROR: Experienced logic errors with the inventory, min and max values. Implemented if and else if
     * statements to properly check those inputs to prevent any errors.
     * </p>
     */
    public void handleModPartSave(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modPartId.getText());
            String name = modPartName.getText();
            int stock = Integer.parseInt(modPartInStock.getText());
            double price = Double.parseDouble(modPartPrice.getText());
            int max = Integer.parseInt(modPartMax.getText());
            int min = Integer.parseInt(modPartMin.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Issue");
                alert.setContentText("Maximum MUST be GREATER than minimum!");
                alert.showAndWait();
            }
            else if (max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Issue");
                alert.setContentText("Part inventory is higher than the maximum allowed. Please check your inputs.");
                alert.showAndWait();
            }
            else if (min > stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Issue");
                alert.setContentText("Part inventory is lower than the minimum allowed. Please check your inputs.");
                alert.showAndWait();
            }
            else {
                if (modPartOutsourcedButton.isSelected() && currentPart instanceof InHouse) {
                    String company = modPartToggle.getText();
                    currentPart = new Outsourced(id, name, price, stock, min, max, company);
                } else if (modPartInHouseButton.isSelected() && currentPart instanceof Outsourced) {
                    int machineId = Integer.parseInt(modPartToggle.getText());
                    currentPart = new InHouse(id, name, price, stock, min, max, machineId);
                } else {
                    currentPart.setName(name);
                    currentPart.setPrice(price);
                    currentPart.setStock(stock);
                    currentPart.setMax(max);
                    currentPart.setMin(min);
                    if (currentPart instanceof InHouse) {
                        int machineId = Integer.parseInt(modPartToggle.getText());
                        ((InHouse) currentPart).setMachineId(machineId);
                    } else {
                        String company = modPartToggle.getText();
                        ((Outsourced) currentPart).setCompanyName(company);
                    }
                }
                Inventory.updatePart(index, currentPart);
                returnToMainMenu(event);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Inputs");
            alert.setContentText("Be sure that your values are in the appropriate format and" +
                    " either In House or Outsourced is selected!");
            alert.showAndWait();
        }
    }

    /**
     * Returns the user to the main menu
     * @param event - Clicking on the Cancel button
     * @throws IOException
     */
    public void handleModPartCancel(ActionEvent event) throws IOException{
        returnToMainMenu(event);
    }
    
    /**
     * Returns the user to the main screen
     * @param event - The user is returned to the main menu
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
