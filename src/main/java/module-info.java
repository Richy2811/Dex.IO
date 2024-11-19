module at.fhtw.dexio {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.fhtw.dexio to javafx.fxml;
    exports at.fhtw.dexio;
}