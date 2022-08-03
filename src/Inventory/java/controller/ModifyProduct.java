package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import model.*;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class ModifyProduct implements Initializable {
    public TableView<Part> modProdPartsTable;
    public TableColumn<Part, Integer> modProdIDCol;
    public TableColumn<Part, String> modProdNameCol;
    public TableColumn<Part, Integer> modProdInStockCol;
    public TableColumn<Part, Double> modProdPriceCol;

    public TableView<Part> modProdAssocPartsTable;
    public TableColumn<Part, Integer> modProdAssocPartsIDCol;
    public TableColumn<Part, String> modProdAssocPartsNameCol;
    public TableColumn<Part, Integer> modProdAssocPartsInStockCol;
    public TableColumn<Part, Double> modProdAssocPartsPriceCol;
    

    public TextField modProdId;
    public TextField modProdName;
    public TextField modProdInStock;
    public TextField modProdPrice;
    public TextField modProdMin;
    public TextField modProdMax;
    public TextField modProdSearch;
    
    public Button modProdAddButton;
    public Button modProdRemoveButton;
    public Button modProdSaveButton;
    public Button modProdCancelButton;
    Stage stage;
    Scene scene;

    private Product currProd;
    int prodIndex = Main.getProductToModify();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProdPartsTable.setItems(Inventory.getAllParts());
        modProdIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Returns the user to the main screen
     * @param event - The user is returned to the main menu
     */
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the 2nd table to display the associated parts from the product that is passed into the method
     * @param item - The item that is being passed from the Main controller
     */
    public void passSelectedProduct(Product item) {
        this.currProd = item;
        modProdAssocPartsTable.setItems(currProd.getAllAssociatedParts());
        modProdAssocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdAssocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdAssocPartsInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdAssocPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modProdId.setText(String.valueOf(currProd.getId()));
        modProdName.setText(currProd.getName());
        modProdInStock.setText(String.valueOf(currProd.getStock()));
        modProdPrice.setText(String.valueOf(currProd.getPrice()));
        modProdMax.setText(String.valueOf(currProd.getMax()));
        modProdMin.setText(String.valueOf(currProd.getMin()));
    }

    /**
     * Values for the modified product are entered by the user, checking for type mismatches and ensuring
     * that min is less than max.
     * In-House and Outsourced are then checked to ensure that the part is correctly attributed to either field.
     * @param event - Clicking on the Save button
     * @throws IOException
     */
    public void handleModProdSave(ActionEvent event) throws IOException {
        try {
            String name = modProdName.getText();
            int stock = Integer.parseInt(modProdInStock.getText());
            double price = Double.parseDouble(modProdPrice.getText());
            int max = Integer.parseInt(modProdMax.getText());
            int min = Integer.parseInt(modProdMin.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Issue");
                alert.setContentText("Maximum MUST be GREATER than minimum!");
                alert.showAndWait();
            }
            else{
                currProd.setName(name);
                currProd.setPrice(price);
                currProd.setStock(stock);
                currProd.setMax(max);
                currProd.setMin(min);
                Inventory.updateProduct(prodIndex, currProd);
                returnToMainMenu(event);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Inputs");
            alert.setContentText("Be sure that your values are in the appropriate format!");
        }
    }
    /**
     * First, the method deselects any items. Then, part to search for is taken from the user's
     * input and a list is created of the search results. If the results list is empty,
     * then an idSearch is executed. If that search returns empty, an error message is shown.
     * Otherwise, the resulting list is set to the TableView.
     * @param event - The user searches for a part
     */
    public void handleModProdSearch(ActionEvent event) {
        String search = modProdSearch.getText();
        ObservableList<Part> results = Inventory.lookUpPart(search);
        try {
            if (results.size() == 0) {
                int idSearch = Integer.parseInt(search);
                Part idResult = Inventory.lookUpPart(idSearch);
                if (idResult.equals(null)) {
                    throw new Exception();
                } else {
                    modProdPartsTable.setItems(Inventory.getAllParts());
                    modProdPartsTable.getSelectionModel().select(idResult);
                }
            } else {
                modProdPartsTable.setItems(results);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Part Not Found");
            alert.setContentText("Press OK to return to the Add Product page!");
            alert.showAndWait();
            modProdSearch.setText("");
        }
    }
    /**
     * Takes the selected item and adds it to the current product's list
     * @param event - Add is selected by the user
     */
    public void handleModProdAdd(ActionEvent event) {
        Part item = modProdAssocPartsTable.getSelectionModel().getSelectedItem();
        currProd.addAssociatedPart(item);
    }
    /**
     * Takes the item that the user requests to remove, asks them if they are sure
     * they have selected the right item, and then removes the item.
     * @param event - Remove is clicked
     */
    public void handleModProdRemove(ActionEvent event) {
        Part item = modProdAssocPartsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING");
        alert.setHeaderText("Part Removal Confirmation");
        alert.setContentText("Are you sure you want to remove " + item.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            currProd.deleteAssociatedPart(item);
        }
    }
    /**
     * The user is returned to the main menu when cancel is clicked
     * @param event - Cancel is clicked
     * @throws IOException
     */
    public void handleModProdCancel(ActionEvent event) throws IOException { returnToMainMenu(event); }

}

