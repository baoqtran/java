package model;

import controller.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.LinkedList;
import java.util.Queue;

import static controller.Utility.*;

/** Inventory Parameter to modify Part and Product*/

public class Inventory {

    /** Observable lists of part and product: */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** Added in to help search for Parts / Products */
    private static ObservableList<Product> allSearchedProducts = FXCollections.observableArrayList();
    private static StringBuilder searchPartBuilder = new StringBuilder();
    private static StringBuilder onlyAssociatedPartBuilder = new StringBuilder();
    private static Queue<Product> searchProductQueue = new LinkedList<>();
    private static Queue<Product> onlyAssociatedPartQueue = new LinkedList<>();


    /** Variables that increment when a new Part / Product is created. Used to create unique IDs. */
    private static int partID = 0;
    private static int productID = 0;

    /** Required Getters, Setters, and other Methods per UML Diagram */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partId) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partId) return allParts.get(i);
            }
        }
        return null;
    }

    public static Product lookupProduct(int productID) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == productID) return allProducts.get(i);
            }
        }
        return null;
    }

    public static Part lookupPart(String partName) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getName() == partName) return allParts.get(i);
            }
        }
        return null;
    }

    public static Product lookupProduct(String productName) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allParts.get(i).getName() == productName) return allProducts.get(i);
            }
        }
        return null;
    }

    public static void updatePart(int index, Part selectedPart){
        int i = -1;
        for (Part p : getAllParts()) {
            i++;
            if(p.getId() == index) {
                getAllParts().set(i, selectedPart);
                return;
            }
        }
    }

    public static void updateProduct(int index, Product newProduct){
        int i = -1;
        for (Product p : getAllProducts()) {
            i++;
            if(p.getId() == index) {
                getAllProducts().set(i, newProduct);
                return;
            }
        }
    }

    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProduct, Queue<Product> searchProductQueue) {
        return allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /** Overloaded method that deletes part from All Parts list and from all products in the queue */
    public static boolean deletePart(Part selectedPart, Queue<Product> productQueue) {
        for (Product product : productQueue) product.deleteAssociatedPart(selectedPart);
        return allParts.remove(selectedPart);
    }

    /** Overloaded method that deletes part from a specified list */
    public static boolean deletePart(Part selectedPart, ObservableList<Part> partObservableList) {
        return partObservableList.remove(selectedPart);
    }

    /** Additional Getters, Setters, and other methods */
    public static int getPartID() {
        return partID;
    }

    public static int incPartID() {
        return ++partID;
    }

    public static int getProductID() {
        return productID;
    }

    public static int incProductID() {
        return ++productID;
    }

    public static ObservableList<Product> getAllSearchedProducts() {
        return allSearchedProducts;
    }

    public static Queue<Product> getOnlyAssociatedPartQueue() {
        return onlyAssociatedPartQueue;
    }

    public static Queue<Product> getSearchProductQueue() {
        return searchProductQueue;
    }

    public static StringBuilder getSearchPartBuilder() {
        return searchPartBuilder;
    }

    public static StringBuilder getOnlyAssociatedPartBuilder() {
        return onlyAssociatedPartBuilder;
    }

    /** Searches a given list for a Part ID and returns all matches */
    public static ObservableList<Part> searchPartResult(int search, ObservableList<Part> partObservableList) {
        ObservableList<Part> tempPartsList = FXCollections.observableArrayList();
        for (Part part : partObservableList) {
            if (part.getId() == search) {
                tempPartsList.add(part);
            }
        }
        return tempPartsList;
    }

    /** Searches the All Parts list for a Part ID or Part Name and returns all matches
     * Will show error message if no parts are found and return all parts instead */
    public static ObservableList<Part> searchPartResult(String search) {
        ObservableList<Part> tempPartsList = FXCollections.observableArrayList();
        search = search.trim().toLowerCase();
        for (Part part : getAllParts()) {
            if (part.getName().toLowerCase().contains(search) || Integer.toString(part.getId()).contains(search)) {
                tempPartsList.add(part);
            }
        }
        if (tempPartsList.isEmpty()) {
            alertBox(Utility.alertType.error, partNotFound, notFound);
            return getAllParts();
        } else {
            return tempPartsList;
        }
    }

    /** Searches all Products list for a Product ID or Product Name and returns all matches */
    public static ObservableList<Product> searchProductResult(String search) {
        ObservableList<Product> tempProductsList = FXCollections.observableArrayList();
        search = search.trim().toLowerCase();
        for (Product product : getAllProducts()) {
            if (product.getName().toLowerCase().contains(search) || Integer.toString(product.getId()).contains(search)) {
                tempProductsList.add(product);
            }
        }
        if (tempProductsList.isEmpty()) {
            alertBox(alertType.error, productNotFound, notFound);
            return getAllProducts();
        } else {
            return tempProductsList;
        }
    }

    /** Searches to find if a given part is also an associated part to any other product
     * Returns false if a part is the only associated part to a given product otherwise returns true
     * This is so the delete function knows if it should go through with deleting the part or not */
    public static boolean searchAssociatedParts(Part selectedPart) {
        searchPartBuilder.setLength(0);
        onlyAssociatedPartBuilder.setLength(0);
        onlyAssociatedPartQueue.clear();
        searchProductQueue.clear();
        for (Product product : getAllProducts()) {
            boolean partFound = false;
            boolean onlyAssociatedPart = true;
            for (Part part : searchPartResult(selectedPart.getId(), product.getAllAssociatedParts())) {
                partFound = true;
                searchProductQueue.add(product);
            }
            for (Part associatedPart : product.getAllAssociatedParts()) {
                if (!(associatedPart.getId() == selectedPart.getId())) {
                    onlyAssociatedPart = false;
                }
            }
            if (onlyAssociatedPart) {
                onlyAssociatedPartQueue.add(product);
                onlyAssociatedPartBuilder.append("Product ID " + product.getId() + "  -  " + product.getName() + "\n");
            }
            if (partFound) {
                searchPartBuilder.append("Product ID " + product.getId() + "  -  " + product.getName() + "\n");
            }
        }
        if(!onlyAssociatedPartQueue.isEmpty()) return false;
        return true;
    }

}