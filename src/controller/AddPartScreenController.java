package controller;

import model.InHouse;
import model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import static model.Inventory.*;
import static controller.Utility.*;

/** Add functions and validations of AddPartScreen */
public class AddPartScreenController {

    @FXML
    private AnchorPane pane;

    @FXML
    private RadioButton partInHouseRadio;

    @FXML
    private ToggleGroup partRadioGroup;

    @FXML
    private RadioButton partOutsourcedRadio;

    @FXML
    private Label partIDLabel;

    @FXML
    private Label partNameLabel;

    @FXML
    private Label PartInvLabel;

    @FXML
    private Label partPriceLabel;

    @FXML
    private TextField partIDField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField partInvField;

    @FXML
    private TextField partPriceField;

    @FXML
    private Button partSaveButton;

    @FXML
    private Button partCancelButton;

    @FXML
    private Label partMaxLabel;

    @FXML
    private TextField partMaxField;

    @FXML
    private Label partMinLabel;

    @FXML
    private TextField partMinField;

    @FXML
    private Label partUniqueLabel;

    @FXML
    private TextField partUniqueField;

    /** Cancel Button Handler
     * Return to Main Screen with confirmation validation */
    @FXML
    void partCancelHandler(MouseEvent event) throws IOException {
        cancelButton(event);
    }

    /** Save Button Handler
     * Text field validation, display error if found, then create a part, and return to Main Screen */
    @FXML
    void partSaveHandler(MouseEvent event) throws IOException {

        /** Store text fields inside array */
        TextField[] partFields = new TextField[] {
                partNameField,    // 0
                partInvField,     // 1
                partPriceField,   // 2
                partMaxField,     // 3
                partMinField,     // 4
                partUniqueField   // 5
        };

        /** Clear previous validation errors */
        clearValidationErrors(partFields);

        /** Validate and returns text field */
        validateInput(partFields, partInHouseRadio);

        /** Input validation that will throw error if unsuccessful,
         * create new Part and return to Main Screen */
        try {
            int id = incPartID();
            String name = partNameField.getText().trim();
            checkEmpty(name);
            int stock = Integer.parseInt(partFields[1].getText().trim());
            checkPos(stock);
            double price = Double.parseDouble(partFields[2].getText().trim());
            checkPos(price);
            int min = Integer.parseInt(partFields[4].getText().trim());
            checkPos(min);
            int max = Integer.parseInt(partFields[3].getText().trim());
            checkPos(max);
            checkMaxMin(partFields[3], partFields[4], Integer.parseInt(partFields[1].getText().trim()));
            checkStock(partFields[1], min, max);

            if (partInHouseRadio.isSelected()) {
                int machineID = Integer.parseInt(partFields[5].getText().trim());
                checkPos(machineID);
                addPart(new InHouse(id, name, price, stock, min, max, machineID));
            } else {
                String companyName = partFields[5].getText().trim();
                checkEmpty(companyName);
                addPart(new Outsourced(id, name, price, stock, min, max, companyName));
            }
            viewScreen(event, mainScreenFxmlUrl);
        }

        /** Catches first error encountered and displays alert box with all items from queue */
        catch (NumberFormatException e) {
            if (partInHouseRadio.isSelected()) buildErrorMap("InHouse");
            else buildErrorMap("Outsourced");
            while(!errorQ.isEmpty()) {
                errorBuilder.append(errorMap.get(errorQ.poll().getId()) + "\n");
            }
            alertBox(alertType.error, errorFields.concat(errorBuilder.toString()), fieldInput);
        }

        /** Catches errors that have already generated an alert box */
        catch (IllegalArgumentException e) {
        }
    }

    /** Radio Button Handlers for InHouse */
    @FXML
    void partInHouseRadioHandler(ActionEvent event) {
        partUniqueLabel.setText("Machine ID");
        partUniqueField.setPromptText("Machine ID");
    }

    /** Radio Button Handlers for Outsourced */
    @FXML
    void partOutsourcedRadioHandler(ActionEvent event) {
        partUniqueLabel.setText("Company Name");
        partUniqueField.setPromptText("Company Name");
    }

}
