package controller;

import model.Part;
import model.Product;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import static model.Inventory.*;
import static model.Inventory.lookupProduct;
import static controller.Utility.*;

/** Modify value of ModifyProductScreen */

public class ModifyProductScreenController {

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField prodMaxField;

    @FXML
    private Label prodMinLabel;

    @FXML
    private TextField prodMinField;

    @FXML
    private Label prodIDLabel;

    @FXML
    private Label prodNameLabel;

    @FXML
    private Label prodInvLabel;

    @FXML
    private Label prodPriceLabel;

    @FXML
    private Label prodMaxLabel;

    @FXML
    private TextField prodIdField;

    @FXML
    private TextField prodNameField;

    @FXML
    private TextField prodInvField;

    @FXML
    private TextField prodPriceField;

    @FXML
    private Button prodSearchButton;

    @FXML
    private TextField partSearchField;

    @FXML
    private TableView<?> prodTable1;

    @FXML
    private Button prodAddButton;

    @FXML
    private TableView<?> prodTable;

    @FXML
    private Button prodDeleteButton;

    @FXML
    private Button prodSaveButton;

    @FXML
    private Button prodCancelButton;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> associatedPartTable;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdCol;

    @FXML
    private TableColumn<Part, String> associatedPartNameCol;

    @FXML
    private TableColumn<Part, Integer> associatedPartInvCol;

    @FXML
    private TableColumn<Part, Double> associatedPartPriceCol;

    /** Add associated part to the product */
    @FXML
    void prodAddHandler(MouseEvent event) {
        addAssociatedPartButton(partTable, associatedPartTable, partIdCol);
    }

    /** Sends user back to the main screen, but confirms they want to first */
    @FXML
    void prodCancelHandler(MouseEvent event) throws IOException {
        cancelButton(event);
    }

    /** Validates all text fields, will warn if error is found,
     * otherwise will create a part and return user to main screen */
    @FXML
    void prodSaveHandler(MouseEvent event) {

        /** Adds all text fields to an array */
        TextField[] prodFields = new TextField[] {
                prodNameField,    // 0
                prodInvField,     // 1
                prodPriceField,   // 2
                prodMaxField,     // 3
                prodMinField,     // 4
        };

        /** Clear previous validation errors */
        clearValidationErrors(prodFields);

        /** Validation that styles text fields but does not throw error
         * Returns text field to queue for future reference */
        validateInput(prodFields);

        /** Input validation that will throw error if unsuccessful
         * If no errors, will create new prod based on the prod type selected
         * Will send back to main screen after prod is created */
        try {
            int id = Integer.parseInt(prodIdField.getText());
            String name = prodFields[0].getText().trim();
            checkEmpty(name);
            int stock = Integer.parseInt(prodFields[1].getText().trim());
            checkPos(stock);
            double price = Double.parseDouble(prodFields[2].getText().trim());
            checkPos(price);
            int min = Integer.parseInt(prodFields[4].getText().trim());
            checkPos(min);
            int max = Integer.parseInt(prodFields[3].getText().trim());
            checkPos(max);
            checkProductPrice(price, associatedPartTable, prodPriceField);
            checkMaxMin(prodFields[3], prodFields[4], Integer.parseInt(prodFields[1].getText().trim()));
            checkStock(prodFields[1], min, max);
//            checkAssociatedParts(associatedPartTable);
            updateProduct(id, new Product(id, name, price, stock, min, max));
            for(Part associatedPart : associatedPartTable.getItems()) lookupProduct(id).addAssociatedPart(associatedPart);
            viewScreen(event, mainScreenFxmlUrl);
        }
        catch (NumberFormatException e) {
            buildErrorMap("Product");
            while(!errorQ.isEmpty()) {

                errorBuilder.append(errorMap.get(errorQ.poll().getId()) + "\n");
            }
            alertBox(alertType.error, errorFields.concat(errorBuilder.toString()), fieldInput);
        }

        /** Catches errors that have already generated an alert box */
        catch (Exception e) {
        }
    }

    /** Handle part Deletion with removing selected part */
    @FXML
    void partDeleteHandler(MouseEvent event) {
        removeSelectedPart(associatedPartTable);
    }

    /** Handle searching part field */
    @FXML
    void partSearchHandler(MouseEvent event) {
        searchPart(partSearchField, partTable);
    }

    /** Function to bring Product information from Main screen to Modify Part Screen */
    public void sendProduct(Product product) {

        /** Sets text fields and table based on prod selected from prior screen */
        setFields(product, prodIdField, prodNameField, prodInvField, prodPriceField, prodMaxField, prodMinField);

        setAllPartsTable(partTable, partIdCol, partNameCol, partInvCol, partPriceCol, SelectionMode.MULTIPLE);
        setAssociatedPartTable(associatedPartIdCol, associatedPartInvCol, associatedPartNameCol, associatedPartPriceCol, associatedPartTable, product);
    }

}
