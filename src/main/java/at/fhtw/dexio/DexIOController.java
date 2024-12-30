package at.fhtw.dexio;

import at.fhtw.dexio.pokedex.*;
import at.fhtw.dexio.pokemontypes.TypeDTO;
import at.fhtw.dexio.pokemontypes.TypeEntryDTO;
import at.fhtw.dexio.services.*;
import at.fhtw.dexio.fileio.FileIO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class DexIOController {
    //Pokédex fields
    private ObservableList<PokedexEntryDTO> pokedexEntries;

    @FXML
    private ListView<PokedexEntryDTO> dexListView;

    @FXML
    private ImageView pokemonImg;

    @FXML
    private Label pokemonName;

    @FXML
    private Label type1;

    @FXML
    private Label type2;

    @FXML
    private Text pokedexFlavorText;


    //Shiny Counter fields
    @FXML
    private ImageView shinyTargetImage;

    @FXML
    private TextField shinyCountText;

    @FXML
    private Text shinyProbabilityText;

    @FXML
    private Text shinyTimeText;

    @FXML
    private Button shinyOptBtn;

    @FXML
    private AnchorPane shinyOptPane;

    @FXML
    private ComboBox<PokedexEntryDTO> shinyTargetSelector;

    @FXML
    private DatePicker shinyStartDate;

    @FXML
    private CheckBox shinyCharmCheckbox;

    @FXML
    private ChoiceBox<String> shinyGenerationChoice;

    @FXML
    private Button shinyResetBtn;

    private FilteredList<PokedexEntryDTO> shinyTargetFilteredList;

    private double shinyOdds = (double) 1 / 8192;

    private int shinyRolls = 1;


    //Pokémon Type fields
    @FXML
    private ComboBox<TypeDTO> primaryType;

    @FXML
    private ComboBox<TypeDTO> secondaryType;

    @FXML
    private VBox dmgTakenWeakVBox;

    @FXML
    private VBox dmgTakenResistVBox;

    @FXML
    private VBox dmgTakenImmuneVBox;

    @FXML
    private VBox dmgTakenNeutralVBox;

    @FXML
    private VBox dmgDealtPrimSuperEffectiveVBox;

    @FXML
    private VBox dmgDealtPrimNotEffectiveVBox;

    @FXML
    private VBox dmgDealtPrimNoEffectVBox;

    @FXML
    private VBox dmgDealtPrimNeutralVBox;

    @FXML
    private VBox dmgDealtSecSuperEffectiveVBox;

    @FXML
    private VBox dmgDealtSecNotEffectiveVBox;

    @FXML
    private VBox dmgDealtSecNoEffectVBox;

    @FXML
    private VBox dmgDealtSecNeutralVBox;

    //map object containing all Pokémon types as a key-value pair of type name and TypeDTO (used for getting type by name)
    private Map<String, TypeDTO> pokemonTypeMap;
    //list object containing all types as TypeDTO (used for getting types by index)
    private List<TypeDTO> pokemonTypeList;
    //table containing all damage relations for [attack type] -> [defending Pokémon type] (e.g. the electric type with index 12 will have a damage multiplier of 2 against water type with index 10, therefore damageRelations[12][10] = 2)
    private final List<List<Float>> damageRelations = new ArrayList<>();


    //Services
    private final PokedexService pokedexEntryService = new PokedexService();
    private final PokemonInfoService pokemonInfoService = new PokemonInfoService();
    private final PokemonSpeciesService pokemonSpeciesService = new PokemonSpeciesService();
    private final ShinyTrackerService shinyTrackerService = new ShinyTrackerService();
    private final TypeService typeService = new TypeService();


    //Placeholder sprite for loading images
    private final Image placeHolderSprite = new Image(Objects.requireNonNull(DexIOController.class.getResource("images/Loading_Sprite.png")).toString(), true);


    @FXML
    public void initialize() {
        //-------------------------------------------------------------------------------------------------
        //---------------------------------------------Pokédex---------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //get Pokédex entries from the PokeAPI
        pokedexEntryService.setPokedexURL("https://pokeapi.co/api/v2/pokemon/");
        pokedexEntryService.restart();

        //add listener for when the Pokédex object has been loaded
        pokedexEntryService.valueProperty().addListener((observable, oldEntries, newEntries) -> {
            //assign list of entries to field in controller class for later use
            pokedexEntries = newEntries;
            dexListView.setItems(pokedexEntries);
            shinyTargetFilteredList = new FilteredList<>(pokedexEntries, p -> true);
            shinyTargetSelector.setItems(shinyTargetFilteredList);
        });

        //add listener for selecting a Pokémon in the Pokédex tab
        dexListView.getSelectionModel().selectedItemProperty().addListener((observable, oldPokemonEntry, newPokemonEntry) -> {
            //stop every service which may potentially run at the time
            pokemonInfoService.cancel();
            pokemonSpeciesService.cancel();

            //clear any currently displayed information while the new one is loaded
            pokemonName.setText("...");
            type1.setText("...");
            type2.setText("");
            pokemonImg.setImage(placeHolderSprite);
            pokedexFlavorText.setText("...");


            //get information from attached url of PokedexDTO object
            pokemonInfoService.setPokemonInfoURL(newPokemonEntry.getURL());
            pokemonInfoService.restart();
        });

        //add listener for changes of the Pokémon info in the Pokédex tab (responsible for Pokémon name, types, and sprite)
        pokemonInfoService.valueProperty().addListener((observable, oldPokemonInfo, newPokemonInfo) -> {
            if(newPokemonInfo == null){
                return;
            }

            //collect Pokémon species information while editing UI
            pokemonSpeciesService.setPokemonSpeciesURL(newPokemonInfo.getSpecies().getUrl());
            pokemonSpeciesService.restart();

            //update Pokémon data in information section
            pokemonName.setText(newPokemonInfo.getName());

            type1.setText(newPokemonInfo.getTypes().getFirst().getType().getName());
            //show secondary type in case of dual typing
            if(newPokemonInfo.getTypes().size() == 2){
                type2.setText(newPokemonInfo.getTypes().getLast().getType().getName());
            }

            //show Pokémon sprite in information section
            Image spriteImage = new Image(newPokemonInfo.getSprites().getFront_default());
            pokemonImg.setImage(spriteImage);
        });

        //add listener for changes of the Pokémon species information in the Pokédex tab (responsible for Pokédex description)
        pokemonSpeciesService.valueProperty().addListener((observable, oldPokemonSpecies, newPokemonSpecies) -> {
            if(newPokemonSpecies == null){
                return;
            }

            //make sure the text is in english
            for(PokedexFlavorTextDTO entry : newPokemonSpecies.getFlavor_text_entries()){
                if(entry.getLanguage().getName().equals("en")){
                    pokedexFlavorText.setText(entry.getFlavor_text().replaceAll("\\u000c", " ").replaceAll("\\n", " "));
                }
            }
        });


        //-------------------------------------------------------------------------------------------------
        //------------------------------------------Shiny Counter------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //hide shiny counter options by default
        shinyOptPane.setVisible(false);
        shinyOptPane.setPrefWidth(0);
        shinyOptBtn.setText(">");

        //set date language to english
        Locale.setDefault(Locale.ENGLISH);

        //set current date
        shinyStartDate.setValue(LocalDate.now());

        //prevent future dates to be selected
        shinyStartDate.setDayCellFactory(param -> new DateCell(){
            //override method responsible for updating the dates inside the date picker
            @Override public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                //set node to disable all dates which are after the current date
                setDisable(date.isAfter(LocalDate.now()));
            }
        });

        //set options for generation choice
        String[] choices = {"Generation 2-4", "Generation 5", "Generation 6-8"};
        shinyGenerationChoice.setItems(FXCollections.observableArrayList(choices));
        shinyGenerationChoice.getSelectionModel().selectFirst();

        //since the first option does not include the shiny charm, it is disabled until a different choice was made
        shinyCharmCheckbox.setSelected(false);
        shinyCharmCheckbox.setDisable(true);

        //add listener for selection of a shiny Pokémon to show the sprite of
        shinyTrackerService.valueProperty().addListener((observable, oldPokemon, newPokemon) -> {
            if(newPokemon == null){
                return;
            }

            Image shinySpriteImage = new Image(newPokemon.getSprites().getFront_shiny());
            shinyTargetImage.setImage(shinySpriteImage);
        });

        //add listener for changes in the encounter textbox
        shinyCountText.textProperty().addListener((observable, oldEncounters, newEncounters) -> {
            if(newEncounters == null || newEncounters.isEmpty()){
                return;
            }
            if(!newEncounters.matches("^\\d+$")){
                //if the encounter text is not a pure numerical value, remove any non-numeric values
                newEncounters = newEncounters.replaceAll("\\D", "");

                //check if resulting string is a valid positive number
                if(newEncounters.isEmpty() || Integer.parseInt(newEncounters) < 0){
                    shinyCountText.setText("0");
                    return;
                }

                shinyCountText.setText(newEncounters);
            }
            updateShinyInfo();
        });

        //add listener for shiny Pokémon combobox text search filter
        shinyTargetSelector.getEditor().textProperty().addListener((observable, oldPokemonName, newPokemonName) -> {
            final String editorText = shinyTargetSelector.getEditor().getText();
            PokedexEntryDTO selectedPokemon;
            if(!(shinyTargetSelector.getSelectionModel().getSelectedItem() instanceof PokedexEntryDTO)){
                selectedPokemon = null;
            }
            else {
                selectedPokemon = shinyTargetSelector.getSelectionModel().getSelectedItem();
            }

            //do not adjust filter if no selection was made or if the current selection already matches the name
            if(selectedPokemon == null || !selectedPokemon.getName().equals(editorText)){
                //take focus away from selection field or else deleting characters immediately afterwards causes an IllegalArgumentException
                shinyOptPane.requestFocus();
                shinyTargetSelector.requestFocus();

                //change available options based on input
                shinyTargetFilteredList.setPredicate(entry -> entry.getName().toLowerCase().contains(newPokemonName.toLowerCase()));

                //show to refresh row count (which gets hidden after items changed)
                shinyTargetSelector.show();
            }
            else {
                //set placeholder image and clear text
                shinyTargetImage.setImage(placeHolderSprite);
                //start service for getting shiny sprite
                shinyTrackerService.setShinyPokemonInfoURL(selectedPokemon.getURL());
                shinyTrackerService.restart();
            }
        });

        //add listener for starting date
        shinyStartDate.valueProperty().addListener((observable, oldStartingDate, newStartingDate) -> {
            updateShinyInfo();
        });

        //add listener for shiny charm checkbox
        shinyCharmCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            //if shiny charm has been acquired, every Pokémon encounter gets three random rolls which determines if it is shiny
            shinyRolls = newValue ? 3 : 1;
            updateShinyInfo();
        });

        //add listener for selection of generation to determine base odds
        shinyGenerationChoice.getSelectionModel().selectedItemProperty().addListener(observable -> {
            //enable checkbox usage by default
            shinyCharmCheckbox.setDisable(false);

            //get current selection index and set shiny odds accordingly
            int selectionIndex = shinyGenerationChoice.getSelectionModel().getSelectedIndex();
            switch (selectionIndex){
                case 0:
                    //this generation does not include the shiny charm
                    shinyCharmCheckbox.setSelected(false);
                    shinyCharmCheckbox.setDisable(true);

                    shinyOdds = (double) 1 / 8192;
                    break;

                case 1:
                    shinyOdds = (double) 1 / 8192;
                    break;

                case 2:
                    shinyOdds = (double) 1 / 4096;
                    break;
            }

            updateShinyInfo();
        });

        //set initial info
        updateShinyInfo();


        //-------------------------------------------------------------------------------------------------
        //------------------------------------------Pokémon Types------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //add listener for loading basic types into the combobox
        typeService.valueProperty().addListener((observableTypes, oldTypeEntries, newTypeEntries) -> {
            //fill type map and list (first type is a "None" type mapped to null)
            pokemonTypeMap = newTypeEntries;
            pokemonTypeList = new ArrayList<>(newTypeEntries.values());

            //fill type selection ComboBoxes
            primaryType.setItems(FXCollections.observableList(pokemonTypeList));
            secondaryType.setItems(FXCollections.observableList(pokemonTypeList));

            //set default selection (no type)
            primaryType.getSelectionModel().select(0);
            secondaryType.getSelectionModel().select(0);

            int id;
            //damage relation list contains damage multiplier of defending type against attacking type
            for(int i = 1; i < pokemonTypeList.size(); i++){
                //fill list with 1 as initial multipliers (since the first type in pokemonTypeList is a null type, it is not used for damage calculations)
                List<Float> damageTo = new ArrayList<>(Collections.nCopies(pokemonTypeList.size() - 1, (float)1));

                //set double damage to all types the current type is strong against
                for(TypeEntryDTO defendingType: pokemonTypeList.get(i).getDamage_relations().getDouble_damage_to()){
                    id = pokemonTypeMap.get(defendingType.getName()).getId() - 1;
                    damageTo.set(id, damageTo.get(id) * 2);
                }

                //set half damage to all types the current type is not very effective against
                for(TypeEntryDTO defendingType: pokemonTypeList.get(i).getDamage_relations().getHalf_damage_to()){
                    id = pokemonTypeMap.get(defendingType.getName()).getId() - 1;
                    damageTo.set(id, damageTo.get(id) * (float)0.5);
                }

                //set no damage to all types the current type is not effective against
                for(TypeEntryDTO defendingType: pokemonTypeList.get(i).getDamage_relations().getNo_damage_to()){
                    id = pokemonTypeMap.get(defendingType.getName()).getId() - 1;
                    damageTo.set(id, damageTo.get(id) * 0);
                }
                damageRelations.add(damageTo);
            }

            //once the content of the ComboBoxes are set, add listeners for type selection
            primaryType.getSelectionModel().selectedItemProperty().addListener((observableTypeEntry, oldPrimaryTypeSelect, newPrimaryTypeSelect) -> {
                typeSelectionHandler(newPrimaryTypeSelect, secondaryType.getSelectionModel().getSelectedItem());
            });

            secondaryType.getSelectionModel().selectedItemProperty().addListener((observableTypeEntry, oldSecondaryTypeSelect, newSecondaryTypeSelect) -> {
                typeSelectionHandler(primaryType.getSelectionModel().getSelectedItem(), newSecondaryTypeSelect);
            });
        });

        //start service for loading types
        typeService.setTypeURL("https://pokeapi.co/api/v2/type/?limit=18");
        typeService.restart();
    }

    @FXML
    protected void onShinyOptBtnClick(){
        //toggle visibility of shiny counter options
        shinyOptPane.setVisible(!shinyOptPane.isVisible());
        //toggle option pane width to show/hide it
        shinyOptPane.setPrefWidth(shinyOptPane.getPrefWidth() == Region.USE_COMPUTED_SIZE ? 0 : Region.USE_COMPUTED_SIZE);
        //toggle button text
        shinyOptBtn.setText(shinyOptBtn.getText().equals(">") ? "x" : ">");
    }

    @FXML
    private void onShinyResetBtnPress(){
        //clear all current selections and set them to their default values
        shinyTargetSelector.getEditor().setText("");
        //refocus on button since focus shifts to selection box if editor text is changed
        shinyResetBtn.requestFocus();
        shinyStartDate.setValue(LocalDate.now());
        shinyCharmCheckbox.setSelected(false);
        shinyGenerationChoice.getSelectionModel().selectFirst();
        shinyTargetImage.setImage(null);
        shinyCountText.setText("0");
    }

    @FXML
    public void onShinyCountDownBtnPress() {
        //check if counter textbox is empty
        if(shinyCountText.getText().isEmpty()){
            shinyCountText.setText("0");
            return;
        }

        //reduce current encounters by one
        int encounters = Integer.parseInt(shinyCountText.getText());
        encounters--;
        shinyCountText.setText(Integer.toString(encounters));
    }

    @FXML
    protected void onShinyCountUpBtnPress(){
        if(shinyCountText.getText().isEmpty()){
            shinyCountText.setText("1");
            return;
        }

        //increase current encounters by one
        int encounters = Integer.parseInt(shinyCountText.getText());
        encounters++;
        shinyCountText.setText(Integer.toString(encounters));
    }

    //refresh shiny information text
    private void updateShinyInfo(){
        //calculate probability of having encountered at least one shiny Pokémon in the amount of encounters (1 - P(no shiny found in n encounters))
        double pOnce = 1 - Math.pow(1 - shinyOdds * shinyRolls, Double.parseDouble(shinyCountText.getText()));
        //truncate after two decimal places
        pOnce = Math.floor(pOnce * 10000) / 10000;
        //if the result of ponce was one, assign 99.99% to ponce (floating point error adjustment since a probability of 100% would be an untrue statement)
        pOnce = pOnce == 1 ? 0.9999 : pOnce;

        shinyProbabilityText.setText(String.format("%.2f%%", pOnce * 100));
        shinyTimeText.setText(ChronoUnit.DAYS.between(shinyStartDate.getValue(), LocalDate.now()) + " days");
    }

    @FXML
    private void handleExport() {
        // Get the selected Pokémon
        PokedexEntryDTO selectedPokemon = dexListView.getSelectionModel().getSelectedItem();

        if (selectedPokemon == null) {
            System.err.println("No Pokémon selected for export.");
            return;
        }

        // Use the Pokémon's name as the file name
        String fileName = selectedPokemon.getName() + ".json";

        // Export the selected Pokémon to the desktop
        FileIO.exportToDesktop(fileName, selectedPokemon);
    }

    private Region buildTypeGuiContainer(Integer typeIndex, Float multiplier){
        //since the type list contains a null type at the beginning, the type index must be offset by 1
        typeIndex++;
        Image typeImage = new Image(pokemonTypeList.get(typeIndex).getSprites().getGeneration_ix_type_sprites().getScarlet_violet_type_sprite().getName_icon());
        ImageView typeImageView = new ImageView(typeImage);

        HBox typeContainer = new HBox(typeImageView);
        typeImageView.setPreserveRatio(true);
        typeContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(typeImageView, new Insets(5));

        Text multiplierText = new Text((multiplier % 1.0 == 0) ? String.format("x%.0f", multiplier) : String.format("x%s", multiplier));
        multiplierText.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 15));
        typeContainer.getChildren().add(multiplierText);

        return typeContainer;
    }

    private void damageTakenHandler(int attackTypeIndex, float multiplier) {
        Region typeContainer = buildTypeGuiContainer(attackTypeIndex, multiplier);

        if(multiplier > 1){
            dmgTakenWeakVBox.getChildren().add(typeContainer);
        }
        else if(multiplier < 1 && multiplier > 0){
            dmgTakenResistVBox.getChildren().add(typeContainer);
        }
        else if(multiplier == 0){
            dmgTakenImmuneVBox.getChildren().add(typeContainer);
        }
        else {
            dmgTakenNeutralVBox.getChildren().add(typeContainer);
        }
    }

    private void damageGivenHandler(){
        Region typeContainer;
        Float multiplier;

        if(primaryType.getSelectionModel().getSelectedItem() != null){
            //fetch sublist containing attack multipliers for the currently chosen primary type
            List<Float> primAttackMultipliers = damageRelations.get(primaryType.getSelectionModel().getSelectedItem().getId() - 1);
            for(int i = 0; i < primAttackMultipliers.size(); i++){
                multiplier = primAttackMultipliers.get(i);

                if(multiplier > 1){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtPrimSuperEffectiveVBox.getChildren().add(typeContainer);
                }
                else if(multiplier < 1 && multiplier > 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtPrimNotEffectiveVBox.getChildren().add(typeContainer);
                }
                else if(multiplier == 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtPrimNoEffectVBox.getChildren().add(typeContainer);
                }
                else {
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtPrimNeutralVBox.getChildren().add(typeContainer);
                }
            }
        }

        if(secondaryType.getSelectionModel().getSelectedItem() != null){
            //fetch sublist containing attack multipliers for the currently chosen secondary type
            List<Float> secAttackMultipliers = damageRelations.get(secondaryType.getSelectionModel().getSelectedItem().getId() - 1);
            for(int i = 0; i < secAttackMultipliers.size(); i++){
                multiplier = secAttackMultipliers.get(i);

                if(multiplier > 1){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtSecSuperEffectiveVBox.getChildren().add(typeContainer);
                }
                else if(multiplier < 1 && multiplier > 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtSecNotEffectiveVBox.getChildren().add(typeContainer);
                }
                else if(multiplier == 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtSecNoEffectVBox.getChildren().add(typeContainer);
                }
                else {
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    dmgDealtSecNeutralVBox.getChildren().add(typeContainer);
                }
            }
        }
    }

    private void typeSelectionHandler(TypeDTO primaryTypeSelect, TypeDTO secondaryTypeSelect) {
        //deselect current type if it matches the type in the primary ComboBox (unless no primary or secondary type is selected)
        if((primaryTypeSelect != null || secondaryTypeSelect != null) && primaryTypeSelect == secondaryTypeSelect){
            primaryType.getSelectionModel().clearSelection();
            return;
        }

        //disable the option to choose the same type in the secondary ComboBox as in the primary selection
        secondaryType.setCellFactory(param -> new ComboBoxListCell<>() {
            @Override
            public void updateItem(TypeDTO item, boolean empty) {
                super.updateItem(item, empty);
                //disable node in secondary selection if equal to current selection to prevent selecting the same type twice
                if(!empty && (item == primaryTypeSelect)){
                    setDisable(true);
                    setBackground(Background.fill(Color.GREY));
                }
            }
        });

        //disable the option to choose the same type in the primary ComboBox as in the secondary selection
        primaryType.setCellFactory(param -> new ComboBoxListCell<>() {
            @Override
            public void updateItem(TypeDTO item, boolean empty) {
                super.updateItem(item, empty);
                //disable node in primary selection if equal to current selection to prevent selecting the same type twice
                if(!empty && (item == secondaryTypeSelect)){
                    setDisable(true);
                    setBackground(Background.fill(Color.GREY));
                }
            }
        });

        //clear current damage relations before rebuilding them
        dmgTakenWeakVBox.getChildren().clear();
        dmgTakenResistVBox.getChildren().clear();
        dmgTakenImmuneVBox.getChildren().clear();
        dmgTakenNeutralVBox.getChildren().clear();

        dmgDealtPrimSuperEffectiveVBox.getChildren().clear();
        dmgDealtPrimNotEffectiveVBox.getChildren().clear();
        dmgDealtPrimNoEffectVBox.getChildren().clear();
        dmgDealtPrimNeutralVBox.getChildren().clear();

        dmgDealtSecSuperEffectiveVBox.getChildren().clear();
        dmgDealtSecNotEffectiveVBox.getChildren().clear();
        dmgDealtSecNoEffectVBox.getChildren().clear();
        dmgDealtSecNeutralVBox.getChildren().clear();

        //start handler which updates damage effectiveness in GUI for primary and secondary type
        damageGivenHandler();

        if(primaryTypeSelect != null && secondaryTypeSelect != null){
            //both primary and secondary type are selected
            //iterate through damage table and add type weaknesses, resistances, and immunities to the corresponding part in the GUI
            for(int i = 0; i < damageRelations.size(); i++){
                float multiplier = damageRelations.get(i).get(primaryTypeSelect.getId() - 1) * damageRelations.get(i).get(secondaryTypeSelect.getId() - 1);
                damageTakenHandler(i, multiplier);
            }
        }
        else if(primaryTypeSelect != null){
            //only primary type is currently selected
            //iterate through damage table and add type weaknesses, resistances, and immunities to the corresponding part in the GUI
            for(int i = 0; i < damageRelations.size(); i++){
                float multiplier = damageRelations.get(i).get(primaryTypeSelect.getId() - 1);
                damageTakenHandler(i, multiplier);
            }
        }
        else if(secondaryTypeSelect != null){
            //only secondary type is currently selected
            //iterate through damage table and add type weaknesses, resistances, and immunities to the corresponding part in the GUI
            for(int i = 0; i < damageRelations.size(); i++){
                float multiplier = damageRelations.get(i).get(secondaryTypeSelect.getId() - 1);
                damageTakenHandler(i, multiplier);
            }
        }
        //if no condition applies, no type is selected
    }
}



