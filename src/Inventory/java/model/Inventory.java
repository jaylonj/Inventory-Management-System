package model;

import java.util.concurrent.atomic.AtomicInteger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    public static AtomicInteger partIdGenerator = new AtomicInteger();
    public static AtomicInteger productIdGenerator = new AtomicInteger();
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart is added to allParts
     */
    public static void addPart(Part newPart){ allParts.add(newPart); }

    /**
     * @param newProduct is added to allProducts
     */
    public static void addProduct(Product newProduct){ allProducts.add(newProduct); }

    /**
     *
     * @param partId used to search through allParts for a matching part ID
     * @return Part object with the matching part ID
     */
    public static Part lookUpPart(int partId){
        for(Part part: allParts){
            if(part.getId() == partId){
                return part;
            }
        }
        return null;
    }

    /**
     *
     * @param partName used to search through allParts for a matching part name
     * @return Part object with the matching part name or an empty list if no item is found
     */
    public static ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> partResults = FXCollections.observableArrayList();
        for(Part part : allParts){
            // Checks if the search term is contained in the part name,
            // changing each letter to lowercase to prevent errors caused by incorrect casing
            if(part.getName().toLowerCase().contains(partName.toLowerCase())){
                partResults.add(part);
            }
        }
        return partResults;
    }

    /**
     * Sample data used to test the inventory system. Adds parts and product to the allParts and allProducts lists.
     */
    public static void addTestData() {
        InHouse chip = new InHouse(1, "Processor Chip", 249.99, 10, 5, 25, 100);
        InHouse leftArm = new InHouse(2, "Left Robot Arm", 49.99, 20, 4, 50, 105);
        Outsourced jetPack = new Outsourced(3, "Jet Pack", 499.99, 10, 2, 12, "Boston Dynamic");
        Outsourced tesseract = new Outsourced(4, "Tesseract", 2499.99, 5, 1, 8, "Stark Industries");
        allParts.add(chip);
        allParts.add(leftArm);
        allParts.add(jetPack);
        allParts.add(tesseract);

        Product zb1 = new Product(1, "Zenith Bot Gen 1", 25000.00, 10, 1, 10);
        Product mzb1 = new Product(2, "Mini Zenith Gen 1", 12000.00, 4, 1, 10);
        Product zoomba = new Product(3, "Zoomba", 2000.00, 12, 1, 25);
        allProducts.add(zb1);
        allProducts.add(mzb1);
        allProducts.add(zoomba);
    }

    /**
     *
     * @param index is used to declare the desired position of the Part in allParts
     * @param p specifies which part is being updated
     */
    public static void updatePart(int index, Part p){
        allParts.set(index, p);
    }

    /**
     *
     * @param p specifies which part is to be deleted
     * @return true when the part is successfully deleted, false when the part is not located in allParts
     */
    public static boolean deletePart(Part p) {
        for (Part part : allParts) {
            if (part.getId() == p.getId()) {
                allParts.remove(p);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return the allParts list
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     *
     * @param productId is the Id being searched through the allProducts list to find the product
     * @return the product once found or null if it cannot be found
     */
    public static Product lookUpProduct(int productId){
        for(Product product: allProducts){
            if(product.getId() == productId){
                return product;
            }
        }
        return null;
    }

    /**
     *
     * @param productName specifies the name to search for in the allProducts list
     * @return the list of products that match the searched name. The list will be empty if no products were found.
     */
    public static ObservableList<Product> lookUpProduct(String productName){
        ObservableList<Product> prodResults = FXCollections.observableArrayList();
        for(Product product : allProducts){
            // Checks if the search term is contained in the product name,
            // changing each letter to lowercase to prevent errors caused by incorrect casing
            if(product.getName().toLowerCase().contains(productName.toLowerCase())) {
                prodResults.add(product);
            }
        }
        return prodResults;
    }

    /**
     *
     * @param index specifies the index at which to set the product to
     * @param newProduct is the product being updated
     */
    public static void updateProduct(int index, Product newProduct){ allProducts.set(index, newProduct); }

    /**
     *
     * @param newProduct specifies the product that is being set for deletion
     * @return true if the product was successfully deleted, false if the product was not found
     */
    public static boolean deleteProduct(Product newProduct) {
        for (Product prod : allProducts) {
            if (prod.getId() == newProduct.getId()) {
                allProducts.remove(newProduct);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return the allProducts list when prompted
     */
    public static ObservableList<Product> getAllProducts(){ return allProducts; }


}
