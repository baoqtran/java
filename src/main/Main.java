/** @author Bao Quoc Tran - C482 Ver 2.0
 * JavaFX-based project for WGU C482
 * A JavaFX program that simulates an inventory management system with the ability to add,
 * modify and delete both parts and products,
 * while also incorporating parts into products.
 * The future enhance will be located in Utility where it stores
 * methods, variables for future expansion of the project
 * Required JAVA FX SDK*/

package main;

import model.InHouse;
import model.Outsourced;
import model.Product;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static model.Inventory.*;
import static controller.Utility.*;

/** Main method for application */

public class Main extends Application {

    /** Open Main Screen with general navigation */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(mainScreenFxmlUrl));
        Scene scene = new Scene(root, 1200, 500);
        scene.getStylesheets().add(cssUrl);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            if (!alertBox(alertType.confirmation, exit, confirmation)) {
                e.consume();
            }
        });
    }

    /** Add sample parts and products in Main method for testing */
    public static void main(String[] args) {
        Product prodTest1 = new Product(incProductID(), "PC", 1399.99, 30, 1, 20);
        Product prodTest2 = new Product(incProductID(), "Playstation 5", 599.99, 30, 1, 20);
        Product prodTest3 = new Product(incProductID(), "Xbox Series X", 579.99, 30, 1, 20);
        InHouse inTest1 = new InHouse(incPartID(),"RAM", 50, 25, 1, 4, 80);
        InHouse inTest2 = new InHouse(incPartID(),"Power Supply", 35.00, 25, 1, 4, 200);
        Outsourced outTest1 = new Outsourced(incPartID(), "GPU", 300.00, 25, 1, 20, "NVIDIA");
        Outsourced outTest2 = new Outsourced(incPartID(), "CPU", 200.0, 25, 1, 20, "INTEL");
        prodTest1.addAssociatedPart(inTest1);
        prodTest2.addAssociatedPart(inTest1);
        prodTest3.addAssociatedPart(inTest1);
        prodTest1.addAssociatedPart(outTest1);
        prodTest2.addAssociatedPart(outTest1);
        prodTest3.addAssociatedPart(outTest1);
        prodTest1.addAssociatedPart(inTest1);
        prodTest2.addAssociatedPart(inTest2);
        addPart(inTest1);
        addPart(inTest2);
        addPart(outTest1);
        addPart(outTest2);
        addProduct(prodTest1);
        addProduct(prodTest2);
        addProduct(prodTest3);

        launch(args);

    }
}
