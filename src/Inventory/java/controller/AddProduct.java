package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import model.*;

/**
 * Class that houses the different methods to handle the actions of the users once the Add button
 * is clicked on the Product TableView
 */
public class AddProduct implements Initializable {

    public TextField addProdName;
    public TextField addProdMax;
    public TextField addProdMin;
    public TextField addProdInStock;
    public TextField addProdPrice;
    public TextField addProdSearch;
    public Button addProdRemoveButton;
    public Button addProdSaveButton;
    public Button addProdCancelButton;
    @FXML
    private TableView<Part> addProdPartsTable;
    @FXML
    private TableColumn<Part, Integer> addProdPartsIDCol;
    @FXML
    private TableColumn<Part, String> addProdPartsNameCol;
    @FXML
    private TableColumn<Part, Integer> addProdPartsInStockCol;
    @FXML
    private TableColumn<Part, Double> addProdPartsPriceCol;
    @FXML
    private TableView<Part> addProdAssocPartsTable;
    @FXML
    private TableColumn<Part, Integer> addProdAssocPartsIDCol;
    @FXML
    private TableColumn<Part, String> addProdAssocPartsNameCol;
    @FXML
    private TableColumn<Part, Integer> addProdAssocPartsInStockCol;
    @FXML
    private TableColumn<Part, Double> addProdAssocPartsPriceCol;
    public Button addButton;
    public Button removeButton;
    public Button saveButton;

    Stage stage;
    Scene scene;
    ObservableList<Part> currAssocParts = FXCollections.observableArrayList();

    /**
     * Initializes the tables for both allParts and currParts.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addProdPartsTable.setItems(Inventory.getAllParts());
        addProdPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdPartsInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProdPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProdAssocPartsTable.setItems(currAssocParts);
        addProdAssocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdAssocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdAssocPartsInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProdAssocPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Returns the user to the main screen
     * @param event - The user is returned to the main menu
     * @throws IOException
     */
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Assigns variables and checks for type mismatches and to make sure that
     * the entered min is less than max.
     * <p>
     *     A list of parts for the current product is created and called getallAssociatedParts.
     *     The parts are added to the associated product list and then the user is returned to the main menu.
     * </p>
     * @param event - The save button is clicked
     * @throws IOException
     * <p>
     *     LOGIC ERROR: Experienced logic errors with the inventory, min and max values. Implemented if and else if
     *     statements to properly check those inputs to prevent any errors.
     * </p>
     */
    public void handleAddProdSave(ActionEvent event) throws IOException {
        try {
            String name = addProdName.getText();
            int stock = Integer.parseInt(addProdInStock.getText());
            double price = Double.parseDouble(addProdPrice.getText());
            int max = Integer.parseInt(addProdMax.getText());
            int min = Integer.parseInt(addProdMin.getText());
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
                alert.setContentText("Product inventory is higher than the maximum allowed. Please check your inputs.");
                alert.showAndWait();
            }
            else if (min > stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Issue");
                alert.setContentText("Product inventory is lower than the minimum allowed. Please check your inputs.");
                alert.showAndWait();
            }
            else {
                int id = Inventory.getAllProducts().size() + Inventory.productIdGenerator.addAndGet(3) + 1000;
                Product newProduct = new Product(id, name, price, stock, min, max);
                Inventory.addProduct(newProduct);

                for (Part part : currAssocParts) {
                    newProduct.addAssociatedPart(part);
                }
                returnToMainMenu(event);
            }
        }
        catch (Exception e) {
            addProdName.setText("");
            addProdInStock.setText("");
            addProdPrice.setText("");
            addProdMax.setText("");
            addProdMin.setText("");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Incorrect Inputs");
            alert.setContentText("Be sure that your values are in the appropriate format!");
            alert.showAndWait();
        }
    }

    /**
     * Takes the selected item and adds it to the current product's list
     * @param event - Add is selected by the user
     */
    public void handleAddProdAdd(ActionEvent event) {
        Part selection = addProdPartsTable.getSelectionModel().getSelectedItem();
        if(selection != null)
            currAssocParts.add(selection);
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Empty Selection");
            alert.setContentText("You have not selected a part to add!");
            alert.showAndWait();
        }
    }

    /**
     * Takes the item that the user requests to remove, asks them if they are sure
     * they have selected the right item, and then removes the item.
     * @param event - Remove is clicked
     *<p>
     *             RUNTIME ERROR:
     * Encountered a NullPointerException error when I allowed the program to accept an item as null if the user
     * failed to select an item. The program attempted to retrieve the name of the item and that is where the error
     * occurred.
     * Implemented a try-catch block to circumvent this and check the user's selection, presenting them
     * with an error message if they had not selected an item.
     *              </p>
     */
    public void handleAddProdRemove(ActionEvent event){
        Part item = addProdAssocPartsTable.getSelectionModel().getSelectedItem();
        try {
            if (item == null)
                throw new Exception();
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("WARNING");
                alert.setHeaderText("Part Removal Confirmation");
                alert.setContentText("Are you sure you want to remove " + item.getName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    currAssocParts.remove(item);
                }
            }
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Empty Selection");
            alert.setContentText("You have not selected a part to remove!");
            alert.showAndWait();
        }
    }

    /**
     * <p>First, the method deselects any items. Then, part to search for is taken from the user's
     * input and a list is created of the search results. If the results list is empty,
     * then an idSearch is executed. If that search returns empty, an error message is shown.
     * Otherwise, the resulting list is set to the TableView.</p>
     * @param event - The user searches for a part
     *
     */
    public void handleAddProdSearch(ActionEvent event){
        addProdPartsTable.getSelectionModel().clearSelection();
        String search = addProdSearch.getText();
        ObservableList<Part> results = Inventory.lookUpPart(search);
        try{
            if(results.size() == 0){
                int idSearch = Integer.parseInt(search);
                Part idResult = Inventory.lookUpPart(idSearch);
                if(idResult.equals(null)){
                    throw new Exception();
                }
                else{
                    addProdPartsTable.setItems(Inventory.getAllParts());
                    addProdPartsTable.getSelectionModel().select(idResult);
                }
            }
            else{
                addProdPartsTable.setItems(results);
            }
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Part Not Found");
            alert.setContentText("Part does not exist. Press OK to return to add product page");
            alert.showAndWait();
            addProdSearch.setText("");
        }
    }

    /**
     * The user is returned to the main menu when cancel is clicked
     * @param event - Cancel is clicked
     * @throws IOException
     */
    public void handleAddProdCancel(ActionEvent event) throws IOException{ returnToMainMenu(event); }

}

