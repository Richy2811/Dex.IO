module at.fhtw.dexio {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens at.fhtw.dexio to javafx.fxml;
    opens at.fhtw.dexio.pokedex to com.fasterxml.jackson.databind; // Allow Jackson to access Pokedex classes

    exports at.fhtw.dexio;
    exports at.fhtw.dexio.networking;
    opens at.fhtw.dexio.networking to javafx.fxml;
    opens at.fhtw.dexio.pokemonstats to com.fasterxml.jackson.databind;
}