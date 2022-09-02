package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Product Parameter*/

public class Product {

    private ObservableList<model.Part> associatedParts = FXCollections.observableArrayList();
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
    public void setID(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    public void setMin(int min){
        this.min = min;
    }
    public void setMax(int max){
        this.max = max;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }
    public int getMin(){
        return min;
    }
    public int getMax(){
        return max;
    }

    public void addAssociatedPart(model.Part newPart) {
        associatedParts.add(newPart);
    }

    public model.Part lookupAssociatedPart(int partID) {
        if(!associatedParts.isEmpty()) {
            for (int i = 0; i < associatedParts.size(); i++) {
                if (associatedParts.get(i).getId() == partID) return associatedParts.get(i);
            }
        }
        return null;
    }

    public model.Part lookupAssociatedPart(String partName) {
        if(!associatedParts.isEmpty()) {
            for (int i = 0; i < associatedParts.size(); i++) {
                if (associatedParts.get(i).getName() == partName) return associatedParts.get(i);
            }
        }
        return null;
    }

    public void updateAssociatedPart(int index, model.Part selectedPart) {
        associatedParts.add(index, selectedPart);
        associatedParts.remove(++index);
    }

    public boolean deleteAssociatedPart(model.Part selectedPart) {
        return associatedParts.remove(selectedPart);
    }

    public ObservableList<model.Part> getAllAssociatedParts() {
        return associatedParts;
    }

    public void addAssociatedPart(ObservableList <model.Part> newParts) {
        associatedParts.addAll(newParts);
    }
}
