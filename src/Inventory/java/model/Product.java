package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     *
     * @param id is the value used to set this.id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     *
     * @return id
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @param name is the value used to set this.name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     *
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @param price is the value used to set this.price
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     *
     * @return price
     */
    public double getPrice(){
        return price;
    }

    /**
     *
     * @param stock is the value used to set this.stock
     */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     *
     * @return stock
     */
    public int getStock(){
        return stock;
    }

    /**
     *
     * @param min is the value used to set this.min
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     *
     * @return min
     */
    public int getMin(){
        return min;
    }

    /**
     *
     * @param max is the value used to set this.max
     */
    public void setMax(int max){
        this.max = max;
    }

    /**
     *
     * @return max
     */
    public int getMax(){
        return max;
    }

    /**
     *
     * @param part is used to specify which part to add to the Observable list
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     *
     * @param currPart is used to specify which part to delete from the associatedParts list
     * @return true if deletion was successful, false if unsuccessful
     */
    public boolean deleteAssociatedPart(Part currPart){
        if(associatedParts.contains(currPart)){
            associatedParts.remove(currPart);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @return observable list of associatedParts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

}
