module at.fhtw.dexio {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens at.fhtw.dexio to javafx.fxml;
    exports at.fhtw.dexio;
    exports at.fhtw.dexio.networking;
    opens at.fhtw.dexio.networking to javafx.fxml;
}