package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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

import model.Inventory;
import model.Part;
import model.Product;

/**
 * Class that launches the application
 */

public class Main implements Initializable {


    public TableView<Part> mainPartTable;
    public TableColumn<Part, Integer> mainPartIDCol;
    public TableColumn<Part, String> mainPartNameCol;
    public TableColumn<Part, Integer> mainPartInStockCol;
    public TableColumn<Part, Double> mainPartPriceCol;


    public TableView<Product> mainProductTable;
    public TableColumn<Product, Integer> mainProductIDCol;
    public TableColumn<Product, String> mainProductNameCol;
    public TableColumn<Product, Integer> mainProductInStockCol;
    public TableColumn<Product, Double> mainProductPriceCol;


    public TextField mainPartSearchField;
    public TextField mainProductSearchField;
    private static int modPartIndex;
    private static int modProdIndex;

    private Stage stage;
    private Scene scene;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPartTable.setItems(Inventory.getAllParts());
        mainPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainProductTable.setItems(Inventory.getAllProducts());
        mainProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public static int getPartToModify() {
        return modPartIndex;
    }

    public static int getProductToModify() {
        return modProdIndex;
    }


    /**
     * Loads the Add Part TableView
     * @param event - Clicking the Add button on the parts pane
     * @throws IOException
     */
    public void handleMainPartAdd(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the ModifyPart controller. Once it is loaded, passSelectedPart is called to the pass
     * the selected part to the ModifyPart controller.
     * <p>
     *     The ModifyPart view is shown on the screen.
     * </p>
     * @param event - Clicking the Modify button on the Parts pane
     * @throws IOException
     */
    public void handleMainPartMod(ActionEvent event) throws IOException {
        Part item = mainPartTable.getSelectionModel().getSelectedItem();
        try {
            modPartIndex = Inventory.getAllParts().indexOf(item);
            // Loading the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
            Parent root = loader.load();
            // Passing the part to modify
            ModifyPart modPart = loader.getController();
            modPart.passSelectedPart(item);
            // Setting the stage
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("WARNING");
            alert.setContentText("Please select a product and try again!");
            alert.show();
        }
    }

    /**
     * Loads the Add Product TableView
     * @param event - Clicking the Add button on the Products pane
     * @throws IOException
     */
    public void handleMainProdAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleMainProdMod(ActionEvent event) throws IOException {
        Product item = mainProductTable.getSelectionModel().getSelectedItem();
        try {
            modProdIndex = Inventory.getAllProducts().indexOf(item);
            // Loading the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
            Parent root = loader.load();
            // Passing the product to modify
            ModifyProduct modProd = loader.getController();
            modProd.passSelectedProduct(item);
            // Setting the stage
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("WARNING");
            alert.setContentText("Please select a product and try again!");
            alert.show();
        }
    }

    /**
     * Exits the system
     * @param event - Clicking on the Exit button
     */
    public void handleExit(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * This method clears the selection, gets the desired search item from user input,
     * then creates a list of the results from lookUpPart. If the part is found, the table
     * is set to display allParts with the correct item highlighted. If it isn't found, an error message
     * is shown to the user to notify them that the part does not exist.
     * Otherwise, lookUpPart returned a non-empty list and the table is then set to display those
     * values returned by the search.
     * @param event - Entering a search term into the search bar
     */
    public void handleMainPartSearch(ActionEvent event) {
        mainPartTable.getSelectionModel().clearSelection();
        String search = mainPartSearchField.getText();
        ObservableList<Part> results = Inventory.lookUpPart(search);
        try {
            if (results.size() == 0) {
                int idSearch = Integer.parseInt(search);
                Part idResults = Inventory.lookUpPart(idSearch);
                if (idResults.equals(null)) {
                    throw new Exception();
                }
                else {
                    mainPartTable.setItems(Inventory.getAllParts());
                    mainPartTable.getSelectionModel().select(Inventory.lookUpPart(idSearch));
                }
            } else {
                mainPartTable.setItems(results);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Part Not Found");
            alert.setContentText("Part does not exist. Press OK to return to the main menu!");
            alert.showAndWait();
            mainPartSearchField.setText("");

        }
    }

    /**
     * This method clears the selection, gets the desired search item from user input,
     * then creates a list of the results from lookUpProduct. If the part is found, the table
     * is set to display allProducts with the correct item highlighted. If it isn't found, an error message
     * is shown to the user to notify them that the part does not exist.
     * Otherwise, lookUpProduct returned a non-empty list and the table is then set to display those
     * values returned by the search.
     * @param event - Entering a search term into the search bar
     */
    public void handleMainProdSearch(ActionEvent event) {
        mainProductTable.getSelectionModel().clearSelection();
        String search = mainProductSearchField.getText();
        ObservableList<Product> results = Inventory.lookUpProduct(search);
        try {
            if (results.size() == 0) {
                int idSearch = Integer.parseInt(search);
                Product idResults = Inventory.lookUpProduct(idSearch);
                if (idResults.equals(null)) {
                    throw new Exception();
                } else {
                    mainProductTable.setItems(Inventory.getAllProducts());
                    mainProductTable.getSelectionModel().select(Inventory.lookUpProduct(idSearch));
                }
            } else {
                mainProductTable.setItems(results);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product Not Found");
            alert.setContentText("Product does not exist. Press OK to return to the main menu!");
            alert.showAndWait();
            mainProductSearchField.setText("");
        }
    }

    /**
     * First, the highlighted part is assigned to a variable, then allParts list is called.
     * If the item selected is null, there is nothing to delete.
     * Otherwise, the user is then prompted to confirm that this is the item they desire to delete.
     * If the deletion is confirmed, the part is deleted. If not, the user is returned to the main menu.
     * @param event - Clicking the Delete button on the Parts pane
     */
    public void handleMainPartDelete(ActionEvent event) {
        Part item = (Part) mainPartTable.getSelectionModel().getSelectedItem();
        ObservableList<Part> allParts = Inventory.getAllParts();
        if (item == null) {
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING");
            alert.setHeaderText("Deletion Confirmation");
            alert.setContentText("Are you sure you want to delete " + item.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(item);
            }
        }
        mainPartTable.setItems(allParts);
    }

    /**
     *  First, the highlighted product is assigned to a variable, then allProducts list is called.
     *  If the item selected is null, there is nothing to delete.
     *  Otherwise, the user is then prompted to confirm that this is the item they desire to delete.
     *  If the deletion is confirmed, the product is deleted. If not, the user is returned to the main menu.
     * @param event - Clicking the Delete button on the Products pane
     */
    public void handleMainProdDelete(ActionEvent event) {
        Product item = (Product) mainProductTable.getSelectionModel().getSelectedItem();
        ObservableList<Product> allProds = Inventory.getAllProducts();
        if (item == null) {
            return;
        } else {
            if (item.getAllAssociatedParts().size() == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("WARNING");
                alert.setHeaderText("Deletion Confirmation");
                alert.setContentText("Are you sure you want to delete " + item.getName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deleteProduct(item);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Can't Delete");
                alert.setContentText("Products with associated parts can't be deleted!");
                alert.show();
            }
        }
        mainProductTable.setItems(allProds);
    }
}

