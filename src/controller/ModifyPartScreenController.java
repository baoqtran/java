package controller;

import model.InHouse;
import model.Outsourced;
import model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

import static model.Inventory.updatePart;
import static controller.Utility.*;

/** Modify value of ModifyPartScreen */

public class ModifyPartScreenController {

    String[] unique;

    @FXML
    private AnchorPane pane;

    @FXML
    private RadioButton partInHouseRadio;

    @FXML
    private ToggleGroup partRadioGroup;

    @FXML
    private RadioButton partOutsourcedRadio;

    @FXML
    private Label partUniqueLabel;

    @FXML
    private TextField partMaxField;

    @FXML
    private Label partMinLabel;

    @FXML
    private TextField partMinField;

    @FXML
    private Label partIDLabel;

    @FXML
    private Label partNameLabel;

    @FXML
    private Label PartInvLabel;

    @FXML
    private Label partPriceLabel;

    @FXML
    private Label partMaxLabel;

    @FXML
    private Label partCompanyLabel;

    @FXML
    private TextField partIdField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField partInvField;

    @FXML
    private TextField partPriceField;

    @FXML
    private TextField partUniqueField;

    @FXML
    private Button partSaveButton;

    @FXML
    private Button partCancelButton;

    /** Sends user back to the main screen, but confirms they want to first */
    @FXML
    void partCancelHandler(MouseEvent event) throws IOException {
        cancelButton(event);
    }

    /** Validates all text fields, will warn if error is found, otherwise will create a part and return user to main screen */
    @FXML
    void partSaveHandler(MouseEvent event) throws IOException {

        /** Adds all text fields to an array */
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

        /** Validation that styles text fields but does not throw error
         * Returns text field to queue for future reference */
        validateInput(partFields, partInHouseRadio);

        /** Input validation that will throw error if unsuccessful
         * If no errors, will create new Part based on the Part type selected
         * Will send back to main screen after part is created */
        int id = Integer.parseInt(partIdField.getText());
        try {
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
                updatePart(id, new InHouse(id, name, price, stock, min, max, machineID));
            } else {
                String companyName = partFields[5].getText().trim();
                checkEmpty(companyName);
                updatePart(id, new Outsourced(id, name, price, stock, min, max, companyName));
            }
            viewScreen(event, mainScreenFxmlUrl);
        }

        /** Catches first error encountered and displays alert box with all items from error queue */
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

    /** Function to bring Part information from Main screen to Modify Part Screen */
    public void sendPart(Part part) {

        /** Sets text fields based on part selected from prior screen */
        setFields(part, partIdField, partNameField, partInvField, partPriceField, partMaxField, partMinField);

        /** Checks to see if the part is InHouse or not, and sets the Label and Field accordingly
         * Also creates a variable to remember the field in case it needs to be recalled */
        if(part instanceof InHouse) {
            partInHouseRadio.setSelected(true);
            partUniqueLabel.setText("Machine ID");
            partUniqueField.setText(String.valueOf(((InHouse) part).getMachineId()));
            unique = new String[]{"InHouse", partUniqueField.getText()};
        }
        else {
            partOutsourcedRadio.setSelected(true);
            partUniqueLabel.setText("Company Name");
            partUniqueField.setText(((Outsourced) part).getCompanyName());
            unique = new String[]{"Outsourced", partUniqueField.getText()};
        }
    }

    /** Radio Button Handlers for InHouse Part with unique machineID*/
    @FXML
    void partInHouseRadioHandler(ActionEvent event) {
        partUniqueLabel.setText("Machine ID");
        partUniqueField.clear();
        if(unique[0].equals("InHouse")) partUniqueField.setText(unique[1]);
        else partUniqueField.setPromptText("Machine ID");
    }

    /** Radio Button Handlers for Outsourced Part with unique company name*/
    @FXML
    void partOutsourcedRadioHandler(ActionEvent event) {
        partUniqueLabel.setText("Company Name");
        partUniqueField.clear();
        if(unique[0].equals("Outsourced")) partUniqueField.setText(unique[1]);
        else partUniqueField.setPromptText("Company Name");
    }

}
