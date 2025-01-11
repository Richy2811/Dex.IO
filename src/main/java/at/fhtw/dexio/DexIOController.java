package at.fhtw.dexio;

import at.fhtw.dexio.networking.TcpConnectionHandler;
import at.fhtw.dexio.pokedex.*;
import at.fhtw.dexio.pokemonmoves.MoveDTO;
import at.fhtw.dexio.pokemonmoves.MoveEntryDTO;
import at.fhtw.dexio.pokemonnatures.NatureDTO;
import at.fhtw.dexio.pokemontypes.TypeDTO;
import at.fhtw.dexio.pokemontypes.TypeEntryDTO;
import at.fhtw.dexio.services.*;
import at.fhtw.dexio.fileio.FileIO;
import at.fhtw.dexio.sorting.SortingController;
import at.fhtw.dexio.sorting.SortingEntryDTO;
import at.fhtw.dexio.sorting.SortingTypeDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


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

    private FilteredList<PokedexEntryDTO> shinyPokemonSelectFiltered;

    private double shinyOdds = (double) 1 / 8192;

    private int shinyRolls = 1;

    //Team builder fields

    /**
     * Button for adding or removing a Pokemon from the current Team
     */
    @FXML
    public Button addOrRemoveTeamMemberButton;

    /**
     * Image views for displaying the image of the Pokemon in the Team 1-5 for each Pokemon an extra variable
     */
    @FXML
    private ImageView teamImage0;

    @FXML
    private ImageView teamImage1;

    @FXML
    private ImageView teamImage2;

    @FXML
    private ImageView teamImage3;

    @FXML
    private ImageView teamImage4;

    @FXML
    private ImageView teamImage5;

    /**
     * Labels for displaying the names of each Pokemon 1-5
     */
    @FXML
    private Label teamText0;

    @FXML
    private Label teamText1;

    @FXML
    private Label teamText2;

    @FXML
    private Label teamText3;

    @FXML
    private Label teamText4;

    @FXML
    private Label teamText5;

    /**
     * Array holding the image views for team Pokemon
     */
    private ImageView[] teamImages;
    /**
     * Array holding each Pokemon name
     */
    private Label[] teamTexts;
    /**
     * Current team size
     */
    private int currentTeamSize = 0;


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


    //Pokémon damage calculator fields
    @FXML
    private AnchorPane dmgCalcAnchorPane;

    @FXML
    private Text dmgCalcResultText;

    @FXML
    private Text dmgCalcResultCommentText;

    @FXML
    private ComboBox<PokedexEntryDTO> dmgCalcPokemon1Selector;

    private FilteredList<PokedexEntryDTO> dmgCalcPokemon1SelectFiltered;

    @FXML
    private ComboBox<PokedexEntryDTO> dmgCalcPokemon2Selector;

    private FilteredList<PokedexEntryDTO> dmgCalcPokemon2SelectFiltered;

    @FXML
    private ComboBox<MoveEntryDTO> dmgCalcMoveSelector;

    private List<MoveEntryDTO> dmgCalcMoveList;

    private MoveDTO dmgCalcCurrentMove;

    @FXML
    private Spinner<Integer> dmgCalcHpSpin;

    private SpinnerValueFactory.IntegerSpinnerValueFactory dmgCalcHpSpinValueFactory;

    private Boolean newPokemon2Chosen;

    @FXML
    private ProgressBar dmgCalcHpBar;

    @FXML
    private ImageView dmgCalcPokemon1Img;

    @FXML
    private ImageView dmgCalcPokemon1Type1;

    @FXML
    private ImageView dmgCalcPokemon1Type2;

    @FXML
    private ImageView dmgCalcPokemon2Img;

    @FXML
    private ImageView dmgCalcPokemon2Type1;

    @FXML
    private ImageView dmgCalcPokemon2Type2;

    @FXML
    private Text dmgCalcMoveTypeText;

    @FXML
    private Text dmgCalcMoveCatText;

    @FXML
    private Text dmgCalcMovePwrText;

    @FXML
    private Text hpBasePokemon1Text;

    @FXML
    private Text atkBasePokemon1Text;

    @FXML
    private Text defBasePokemon1Text;

    @FXML
    private Text spAtkBasePokemon1Text;

    @FXML
    private Text spDefBasePokemon1Text;

    @FXML
    private Text speedBasePokemon1Text;

    @FXML
    private Spinner<Integer> hpIvPokemon1Spin;

    @FXML
    private Spinner<Integer> atkIvPokemon1Spin;

    @FXML
    private Spinner<Integer> defIvPokemon1Spin;

    @FXML
    private Spinner<Integer> spAtkIvPokemon1Spin;

    @FXML
    private Spinner<Integer> spDefIvPokemon1Spin;

    @FXML
    private Spinner<Integer> speedIvPokemon1Spin;

    @FXML
    private Spinner<Integer> hpEvPokemon1Spin;

    @FXML
    private Spinner<Integer> atkEvPokemon1Spin;

    @FXML
    private Spinner<Integer> defEvPokemon1Spin;

    @FXML
    private Spinner<Integer> spAtkEvPokemon1Spin;

    @FXML
    private Spinner<Integer> spDefEvPokemon1Spin;

    @FXML
    private Spinner<Integer> speedEvPokemon1Spin;

    @FXML
    private Text hpResultPokemon1Text;

    @FXML
    private Text atkResultPokemon1Text;

    @FXML
    private Text defResultPokemon1Text;

    @FXML
    private Text spAtkResultPokemon1Text;

    @FXML
    private Text spDefResultPokemon1Text;

    @FXML
    private Text speedResultPokemon1Text;

    @FXML
    private Text hpBasePokemon2Text;

    @FXML
    private Text atkBasePokemon2Text;

    @FXML
    private Text defBasePokemon2Text;

    @FXML
    private Text spAtkBasePokemon2Text;

    @FXML
    private Text spDefBasePokemon2Text;

    @FXML
    private Text speedBasePokemon2Text;

    @FXML
    private Spinner<Integer> hpIvPokemon2Spin;

    @FXML
    private Spinner<Integer> atkIvPokemon2Spin;

    @FXML
    private Spinner<Integer> defIvPokemon2Spin;

    @FXML
    private Spinner<Integer> spAtkIvPokemon2Spin;

    @FXML
    private Spinner<Integer> spDefIvPokemon2Spin;

    @FXML
    private Spinner<Integer> speedIvPokemon2Spin;

    @FXML
    private Spinner<Integer> hpEvPokemon2Spin;

    @FXML
    private Spinner<Integer> atkEvPokemon2Spin;

    @FXML
    private Spinner<Integer> defEvPokemon2Spin;

    @FXML
    private Spinner<Integer> spAtkEvPokemon2Spin;

    @FXML
    private Spinner<Integer> spDefEvPokemon2Spin;

    @FXML
    private Spinner<Integer> speedEvPokemon2Spin;

    @FXML
    private Text hpResultPokemon2Text;

    @FXML
    private Text atkResultPokemon2Text;

    @FXML
    private Text defResultPokemon2Text;

    @FXML
    private Text spAtkResultPokemon2Text;

    @FXML
    private Text spDefResultPokemon2Text;

    @FXML
    private Text speedResultPokemon2Text;

    @FXML
    private ChoiceBox<String> dmgCalcWeatherChoice;

    @FXML
    private Spinner<Integer> dmgCalcPokemon1LevelSpin;

    @FXML
    private ComboBox<NatureDTO> dmgCalcPokemon1Nature;

    @FXML
    private Text dmgCalcPokemon1NatureUpText;

    @FXML
    private Text dmgCalcPokemon1NatureDownText;

    @FXML
    private ComboBox<String> dmgCalcAtkBoostChoice;

    @FXML
    private ComboBox<String> dmgCalcSpAtkBoostChoice;

    @FXML
    private CheckBox dmgCalcBurnedCheckBox;

    @FXML
    private Spinner<Integer> dmgCalcPokemon2LevelSpin;

    @FXML
    private ComboBox<NatureDTO> dmgCalcPokemon2Nature;

    @FXML
    private Text dmgCalcPokemon2NatureUpText;

    @FXML
    private Text dmgCalcPokemon2NatureDownText;

    @FXML
    private ComboBox<String> dmgCalcDefBoostChoice;

    @FXML
    private ComboBox<String> dmgCalcSpDefBoostChoice;

    private PokemonDTO dmgCalcCurrentPokemon1;

    private PokemonDTO dmgCalcCurrentPokemon2;

    //map containing all Pokémon natures for quick access of natures by name
    private Map<String, NatureDTO> pokemonNatureMap;

    //map for stat boost multiplier
    private final Map<Integer, Double> statStageMap = Map.ofEntries(
            Map.entry(+6, 8.0 / 2.0),
            Map.entry(+5, 7.0 / 2.0),
            Map.entry(+4, 6.0 / 2.0),
            Map.entry(+3, 5.0 / 2.0),
            Map.entry(+2, 4.0 / 2.0),
            Map.entry(+1, 3.0 / 2.0),
            Map.entry(+0, 1.0),
            Map.entry(-1, 2.0 / 3.0),
            Map.entry(-2, 2.0 / 4.0),
            Map.entry(-3, 2.0 / 5.0),
            Map.entry(-4, 2.0 / 6.0),
            Map.entry(-5, 2.0 / 7.0),
            Map.entry(-6, 2.0 / 8.0)
    );


    //Services
    private final PokedexService pokedexEntryService = new PokedexService();
    private final PokemonInfoService dexPokemonInfoService = new PokemonInfoService();
    private final PokemonSpeciesService pokemonSpeciesService = new PokemonSpeciesService();
    private final PokemonInfoService shinyTrackerService = new PokemonInfoService();
    private final TypeAPIService typeApiService = new TypeAPIService();
    private final TypeUIService typeUiService = new TypeUIService();
    private final PokemonInfoService dmgCalcPokemon1Service = new PokemonInfoService();
    private final PokemonInfoService dmgCalcPokemon2Service = new PokemonInfoService();
    private final MovedexService moveEntryService = new MovedexService();
    private final MoveInfoService moveInfoService = new MoveInfoService();
    private final NatureService natureService =  new NatureService();
    private final PokemonInfoService pokemonInfoService = new PokemonInfoService();


    //Placeholder sprite for loading images
    private final Image placeHolderSprite = new Image(Objects.requireNonNull(DexIOController.class.getResource("images/Loading_Sprite.png")).toString(), true);


    @FXML
    public void initialize() {
        //-------------------------------------------------------------------------------------------------
        //---------------------------------------------Pokédex---------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //get Pokédex entries from the PokeAPI
        pokedexEntryService.setPokedexURL("https://pokeapi.co/api/v2/pokemon?limit=200");
        pokedexEntryService.restart();

        //add listener for when the Pokédex object has been loaded
        pokedexEntryService.valueProperty().addListener((observable, oldEntries, newEntries) -> {
            //assign list of entries to field in controller class for later use
            pokedexEntries = newEntries;
            dexListView.setItems(pokedexEntries);

            //make different filtered lists so the filter only applies to their respective ComboBoxes
            shinyPokemonSelectFiltered = new FilteredList<>(pokedexEntries, p -> true);
            shinyTargetSelector.setItems(shinyPokemonSelectFiltered);

            dmgCalcPokemon1SelectFiltered = new FilteredList<>(pokedexEntries, p -> true);
            dmgCalcPokemon1Selector.setItems(dmgCalcPokemon1SelectFiltered);

            dmgCalcPokemon2SelectFiltered = new FilteredList<>(pokedexEntries, p -> true);
            dmgCalcPokemon2Selector.setItems(dmgCalcPokemon2SelectFiltered);
        });

        //add listener for selecting a Pokémon in the Pokédex tab
        dexListView.getSelectionModel().selectedItemProperty().addListener((observable, oldPokemonEntry, newPokemonEntry) -> {
            //stop every service which may potentially run at the time
            dexPokemonInfoService.cancel();
            pokemonSpeciesService.cancel();

            //clear any currently displayed information while the new one is loaded
            pokemonName.setText("...");
            type1.setText("...");
            type2.setText("");
            pokemonImg.setImage(placeHolderSprite);
            pokedexFlavorText.setText("...");


            //get information from attached url of PokedexDTO object
            dexPokemonInfoService.setPokemonInfoURL(newPokemonEntry.getURL());
            dexPokemonInfoService.restart();
        });

        //add listener for changes of the Pokémon info in the Pokédex tab (responsible for Pokémon name, types, and sprite)
        dexPokemonInfoService.valueProperty().addListener((observable, oldPokemonInfo, newPokemonInfo) -> {
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

            //check if Pokémon is already in team and change button text according to that
            if (currentTeamSize >= 6) {
                addOrRemoveTeamMemberButton.setText("Team is full");
                addOrRemoveTeamMemberButton.setDisable(true);
            } else if (IsPokemonAlreadyInTeam()) {
                addOrRemoveTeamMemberButton.setText("Remove from team");
                addOrRemoveTeamMemberButton.setDisable(false);
            } else {
                addOrRemoveTeamMemberButton.setText("Add to team");
                addOrRemoveTeamMemberButton.setDisable(false);
            }
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
        String[] genChoices = {"Generation 2-4", "Generation 5", "Generation 6-8"};
        shinyGenerationChoice.setItems(FXCollections.observableArrayList(genChoices));
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
            PokedexEntryDTO selectedPokemon;
            if(!(shinyTargetSelector.getSelectionModel().getSelectedItem() instanceof PokedexEntryDTO)){
                selectedPokemon = null;
            }
            else {
                selectedPokemon = shinyTargetSelector.getSelectionModel().getSelectedItem();
            }

            //do not adjust filter if no selection was made or if the current selection already matches the name
            if(selectedPokemon == null || !selectedPokemon.getName().equals(newPokemonName)){
                //take focus away from selection field or else deleting characters immediately afterwards causes an IllegalArgumentException
                shinyOptPane.requestFocus();
                shinyTargetSelector.requestFocus();

                //change available options based on input
                shinyPokemonSelectFiltered.setPredicate(entry -> entry.getName().toLowerCase().contains(newPokemonName.toLowerCase()));

                //show to refresh row count (which gets hidden after items changed)
                shinyTargetSelector.show();
            }
            else {
                //set placeholder image and clear text
                shinyTargetImage.setImage(placeHolderSprite);
                //start service for getting shiny sprite
                shinyTrackerService.setPokemonInfoURL(selectedPokemon.getURL());
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
        //------------------------------------------Team builder------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //Array for saving all the images of the Pokemon
        teamImages = new ImageView[]{teamImage0, teamImage1, teamImage2, teamImage3, teamImage4, teamImage5};
        //Array for saving every label of each Pokemon
        teamTexts = new Label[]{teamText0, teamText1, teamText2, teamText3, teamText4, teamText5};

        //Initialize all available team slots with the pokemon placholder sprite
        for (int i = 0; i < teamImages.length; i++)
        {
            teamImages[i].setImage(placeHolderSprite);
        }



        //-------------------------------------------------------------------------------------------------
        //------------------------------------------Pokémon Types------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //add listener for loading basic types into the combobox
        typeApiService.valueProperty().addListener((observableTypes, oldTypeEntries, newTypeEntries) -> {
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

        //add listener for generating Pokémon type UI elements
        typeUiService.valueProperty().addListener((observableTypes, oldTypeCollection, newTypeCollection) -> {
            if(newTypeCollection == null){
                return;
            }

            //each entry contains type containers for each of the UI fields (type weaknesses, resistances, type effectiveness, etc.)
            dmgTakenWeakVBox.getChildren().addAll(newTypeCollection.get(0));
            dmgTakenResistVBox.getChildren().addAll(newTypeCollection.get(1));
            dmgTakenImmuneVBox.getChildren().addAll(newTypeCollection.get(2));
            dmgTakenNeutralVBox.getChildren().addAll(newTypeCollection.get(3));

            if(primaryType.getSelectionModel().getSelectedItem() != null){
                dmgDealtPrimSuperEffectiveVBox.getChildren().addAll(newTypeCollection.get(4));
                dmgDealtPrimNotEffectiveVBox.getChildren().addAll(newTypeCollection.get(5));
                dmgDealtPrimNoEffectVBox.getChildren().addAll(newTypeCollection.get(6));
                dmgDealtPrimNeutralVBox.getChildren().addAll(newTypeCollection.get(7));
            }
            if(secondaryType.getSelectionModel().getSelectedItem() != null){
                dmgDealtSecSuperEffectiveVBox.getChildren().addAll(newTypeCollection.get(8));
                dmgDealtSecNotEffectiveVBox.getChildren().addAll(newTypeCollection.get(9));
                dmgDealtSecNoEffectVBox.getChildren().addAll(newTypeCollection.get(10));
                dmgDealtSecNeutralVBox.getChildren().addAll(newTypeCollection.get(11));
            }
        });

        //start service for loading types
        typeApiService.setTypeURL("https://pokeapi.co/api/v2/type/?limit=18");
        typeApiService.start();


        //-------------------------------------------------------------------------------------------------
        //----------------------------------------Damage Calculator----------------------------------------
        //-------------------------------------------------------------------------------------------------
        //set ChoiceBox values for weather and attack/defense boosts for attacking/defending Pokémon
        String[] weatherChoices = {"Neutral", "Harsh Sunlight", "Rain"};
        dmgCalcWeatherChoice.setItems(FXCollections.observableArrayList(weatherChoices));
        dmgCalcWeatherChoice.getSelectionModel().select(0);

        //add listener for weather selection
        dmgCalcWeatherChoice.valueProperty().addListener((observable, oldValue, newValue) -> choiceHandler());

        //fill stat boost ChoiceBoxes with values ranging from +6 to -6
        String[] statBoostChoices = {"+6", "+5", "+4", "+3", "+2", "+1", "+0", "-1", "-2", "-3", "-4", "-5", "-6"};
        dmgCalcAtkBoostChoice.setItems(FXCollections.observableArrayList(statBoostChoices));
        dmgCalcAtkBoostChoice.getSelectionModel().select("+0");

        dmgCalcDefBoostChoice.setItems(FXCollections.observableArrayList(statBoostChoices));
        dmgCalcDefBoostChoice.getSelectionModel().select("+0");

        dmgCalcSpAtkBoostChoice.setItems(FXCollections.observableArrayList(statBoostChoices));
        dmgCalcSpAtkBoostChoice.getSelectionModel().select("+0");

        dmgCalcSpDefBoostChoice.setItems(FXCollections.observableArrayList(statBoostChoices));
        dmgCalcSpDefBoostChoice.getSelectionModel().select("+0");

        //add listeners for selection of damage or defense boost
        dmgCalcAtkBoostChoice.valueProperty().addListener((observable, oldValue, newValue) -> choiceHandler());
        dmgCalcDefBoostChoice.valueProperty().addListener((observable, oldValue, newValue) -> choiceHandler());
        dmgCalcSpAtkBoostChoice.valueProperty().addListener((observable, oldValue, newValue) -> choiceHandler());
        dmgCalcSpDefBoostChoice.valueProperty().addListener((observable, oldValue, newValue) -> choiceHandler());

        //set value factories and input handlers for spinners which are used to enter Individual Values (IVs) and Effort Values (EVs) of the selected Pokémon
        setDmgCalcSpinnerValueFactories();

        //add listener for attacking Pokémon selection service
        dmgCalcPokemon1Service.valueProperty().addListener((observable, oldPokemon, newPokemon) -> {
            if(newPokemon == null){
                return;
            }

            //set current Pokémon for later calculation use
            dmgCalcCurrentPokemon1 = newPokemon;

            //set Pokémon image
            Image pokemon1SpriteImage = new Image(newPokemon.getSprites().getFront_default());
            dmgCalcPokemon1Img.setImage(pokemon1SpriteImage);

            //set type images for selected Pokémon
            Image typeImage = new Image(pokemonTypeMap.get(newPokemon.getTypes().getFirst().getType().getName()).getSprites().getGeneration_ix_type_sprites().getScarlet_violet_type_sprite().getName_icon());
            dmgCalcPokemon1Type1.setImage(typeImage);
            if(newPokemon.getTypes().size() == 2){
                typeImage = new Image(pokemonTypeMap.get(newPokemon.getTypes().get(1).getType().getName()).getSprites().getGeneration_ix_type_sprites().getScarlet_violet_type_sprite().getName_icon());
                dmgCalcPokemon1Type2.setImage(typeImage);
            }

            //clear current move
            dmgCalcCurrentMove = null;

            //set move filter to only show moves the selected Pokémon can learn
            List<String> pokemonMoveStringList = newPokemon.getMoves().stream().map(moveEntry -> moveEntry.getMove().getName()).toList();
            dmgCalcMoveSelector.setItems(FXCollections.observableList(dmgCalcMoveList.stream().filter(moveEntryDTO -> pokemonMoveStringList.contains(moveEntryDTO.getName())).collect(Collectors.toList())));

            //set Pokémon stats
            setPokemon1BaseStats(
                    newPokemon.getStats().getFirst().getBase_stat(),
                    newPokemon.getStats().get(1).getBase_stat(),
                    newPokemon.getStats().get(2).getBase_stat(),
                    newPokemon.getStats().get(3).getBase_stat(),
                    newPokemon.getStats().get(4).getBase_stat(),
                    newPokemon.getStats().get(5).getBase_stat()
            );

            calculateStats();
            calculateDamage();
        });

        //add listener for defending Pokémon selection service
        dmgCalcPokemon2Service.valueProperty().addListener((observable, oldPokemon, newPokemon) -> {
            if(newPokemon == null){
                return;
            }

            //set current Pokémon for later calculation use
            dmgCalcCurrentPokemon2 = newPokemon;

            //set Pokémon image
            Image pokemon2SpriteImage = new Image(newPokemon.getSprites().getFront_default());
            dmgCalcPokemon2Img.setImage(pokemon2SpriteImage);

            //set type images for selected Pokémon
            Image typeImage = new Image(pokemonTypeMap.get(newPokemon.getTypes().getFirst().getType().getName()).getSprites().getGeneration_ix_type_sprites().getScarlet_violet_type_sprite().getName_icon());
            dmgCalcPokemon2Type1.setImage(typeImage);
            if(newPokemon.getTypes().size() == 2){
                typeImage = new Image(pokemonTypeMap.get(newPokemon.getTypes().get(1).getType().getName()).getSprites().getGeneration_ix_type_sprites().getScarlet_violet_type_sprite().getName_icon());
                dmgCalcPokemon2Type2.setImage(typeImage);
            }

            //set Pokémon stats
            setPokemon2BaseStats(
                    newPokemon.getStats().getFirst().getBase_stat(),
                    newPokemon.getStats().get(1).getBase_stat(),
                    newPokemon.getStats().get(2).getBase_stat(),
                    newPokemon.getStats().get(3).getBase_stat(),
                    newPokemon.getStats().get(4).getBase_stat(),
                    newPokemon.getStats().get(5).getBase_stat()
            );

            newPokemon2Chosen = true;
            calculateStats();
            calculateDamage();
        });

        //add listener for attacking Pokémon selection ComboBox
        dmgCalcPokemon1Selector.getEditor().textProperty().addListener((observable, oldPokemonName, newPokemonName) -> {
            PokedexEntryDTO selectedPokemon;
            if(!(dmgCalcPokemon1Selector.getSelectionModel().getSelectedItem() instanceof PokedexEntryDTO)){
                selectedPokemon = null;
            }
            else {
                selectedPokemon = dmgCalcPokemon1Selector.getSelectionModel().getSelectedItem();
            }

            //do not adjust filter if no selection was made or if the current selection already matches the name
            if(selectedPokemon == null || !selectedPokemon.getName().equals(newPokemonName)){
                //take focus away from selection field or else deleting characters immediately afterwards causes an IllegalArgumentException
                dmgCalcAnchorPane.requestFocus();
                dmgCalcPokemon1Selector.requestFocus();

                //change available options based on input
                dmgCalcPokemon1SelectFiltered.setPredicate(entry -> entry.getName().toLowerCase().contains(newPokemonName.toLowerCase()));

                //show to refresh row count (which gets hidden after items changed)
                dmgCalcPokemon1Selector.show();
            }
            else {
                //clear final damage result text
                dmgCalcResultText.setText("");
                dmgCalcResultCommentText.setText("");

                //set placeholder image
                dmgCalcPokemon1Img.setImage(placeHolderSprite);

                //clear type images
                dmgCalcPokemon1Type1.setImage(null);
                dmgCalcPokemon1Type2.setImage(null);

                //set stats to zero
                setPokemon1BaseStats(0, 0, 0, 0, 0, 0);
                setPokemon1ResultStats(0, 0, 0, 0, 0, 0);

                //start service for getting Pokémon sprite
                dmgCalcPokemon1Service.setPokemonInfoURL(selectedPokemon.getURL());
                dmgCalcPokemon1Service.restart();
            }
        });

        //add listener for defending Pokémon selection ComboBox
        dmgCalcPokemon2Selector.getEditor().textProperty().addListener((observable, oldPokemonName, newPokemonName) -> {
            PokedexEntryDTO selectedPokemon;
            if(!(dmgCalcPokemon2Selector.getSelectionModel().getSelectedItem() instanceof PokedexEntryDTO)){
                selectedPokemon = null;
            }
            else {
                selectedPokemon = dmgCalcPokemon2Selector.getSelectionModel().getSelectedItem();
            }

            //do not adjust filter if no selection was made or if the current selection already matches the name
            if(selectedPokemon == null || !selectedPokemon.getName().equals(newPokemonName)){
                //take focus away from selection field or else deleting characters immediately afterwards causes an IllegalArgumentException
                dmgCalcAnchorPane.requestFocus();
                dmgCalcPokemon2Selector.requestFocus();

                //change available options based on input
                dmgCalcPokemon2SelectFiltered.setPredicate(entry -> entry.getName().toLowerCase().contains(newPokemonName.toLowerCase()));

                //show to refresh row count (which gets hidden after items changed)
                dmgCalcPokemon2Selector.show();
            }
            else {
                //clear final damage result text
                dmgCalcResultText.setText("");
                dmgCalcResultCommentText.setText("");

                //set placeholder image
                dmgCalcPokemon2Img.setImage(placeHolderSprite);

                //clear type images
                dmgCalcPokemon2Type1.setImage(null);
                dmgCalcPokemon2Type2.setImage(null);

                //disable field to one until the new Pokémon has loaded and its stats are calculated
                dmgCalcHpSpin.setDisable(true);

                //set stats to zero
                setPokemon2BaseStats(0, 0, 0, 0, 0, 0);
                setPokemon2ResultStats(0, 0, 0, 0, 0, 0);

                //start service for getting Pokémon sprite
                dmgCalcPokemon2Service.setPokemonInfoURL(selectedPokemon.getURL());
                dmgCalcPokemon2Service.restart();
            }
        });

        //add listener for when the move list finished loading
        moveEntryService.valueProperty().addListener((observable, oldMoveList, newMoveList) -> {
            dmgCalcMoveList = newMoveList;
        });

        //start service to load move list
        moveEntryService.setMoveURL("https://pokeapi.co/api/v2/move?limit=1000");
        moveEntryService.restart();

        //add listener for when the selected move finished loading
        moveInfoService.valueProperty().addListener((observable, oldMove, newMove) -> {
            if(newMove == null){
                return;
            }

            dmgCalcCurrentMove = newMove;
            dmgCalcMoveTypeText.setText(dmgCalcCurrentMove.getType() != null ? dmgCalcCurrentMove.getType().getName() : "-");
            dmgCalcMoveCatText.setText(dmgCalcCurrentMove.getDamage_class() != null ? dmgCalcCurrentMove.getDamage_class().getName() : "-");
            dmgCalcMovePwrText.setText(dmgCalcCurrentMove.getPower() != null ? Integer.toString(dmgCalcCurrentMove.getPower()) : "0");

            calculateDamage();
        });

        //add listener for move selector
        dmgCalcMoveSelector.valueProperty().addListener((observable, oldMoveEntry, newMoveEntry) -> {
            //cancel any currently running move info service
            moveInfoService.cancel();

            //clear final damage result text
            dmgCalcResultText.setText("");
            dmgCalcResultCommentText.setText("");

            //set move information to point out it is loading
            dmgCalcMoveTypeText.setText("...");
            dmgCalcMoveCatText.setText("...");
            dmgCalcMovePwrText.setText("...");

            if(newMoveEntry == null){
                return;
            }

            moveInfoService.setMoveURL(newMoveEntry.getUrl());
            moveInfoService.restart();
        });

        //add listener for the burned-status checkbox
        dmgCalcBurnedCheckBox.selectedProperty().addListener((observable, oldBurned, newBurned) -> {
            //recalculate damage when changed
            calculateDamage();
        });

        //add listener for loading Pokémon natures
        natureService.valueProperty().addListener((observable, oldNatureMap, newNatureMap) -> {
            if(newNatureMap == null){
                return;
            }

            pokemonNatureMap = newNatureMap;

            //set nature selection for both attacking and defending Pokémon
            dmgCalcPokemon1Nature.setItems(FXCollections.observableArrayList(pokemonNatureMap.values()));
            dmgCalcPokemon1Nature.getSelectionModel().selectFirst();

            //show which stats are affected by the current nature of the attacking Pokémon
            dmgCalcPokemon1NatureUpText.setText(dmgCalcPokemon1Nature.getValue().getIncreased_stat() != null ? dmgCalcPokemon1Nature.getValue().getIncreased_stat().getName() : "---");
            dmgCalcPokemon1NatureDownText.setText(dmgCalcPokemon1Nature.getValue().getDecreased_stat() != null ? dmgCalcPokemon1Nature.getValue().getDecreased_stat().getName() : "---");


            dmgCalcPokemon2Nature.setItems(FXCollections.observableArrayList(pokemonNatureMap.values()));
            dmgCalcPokemon2Nature.getSelectionModel().selectFirst();

            //show which stats are affected by the current nature of the defending Pokémon
            dmgCalcPokemon2NatureUpText.setText(dmgCalcPokemon2Nature.getValue().getIncreased_stat() != null ? dmgCalcPokemon2Nature.getValue().getIncreased_stat().getName() : "---");
            dmgCalcPokemon2NatureDownText.setText(dmgCalcPokemon2Nature.getValue().getDecreased_stat() != null ? dmgCalcPokemon2Nature.getValue().getDecreased_stat().getName() : "---");


            //add listeners after natures have finished loading
            //add listener for nature selection of attacking Pokémon
            dmgCalcPokemon1Nature.valueProperty().addListener((observableNaturePokemon1, oldPokemonNature, newPokemonNature) -> {
                if(newPokemonNature == null){
                    return;
                }

                //show which stats are affected by the nature in the UI
                dmgCalcPokemon1NatureUpText.setText(newPokemonNature.getIncreased_stat() != null ? newPokemonNature.getIncreased_stat().getName() : "---");
                dmgCalcPokemon1NatureDownText.setText(newPokemonNature.getDecreased_stat() != null ? newPokemonNature.getDecreased_stat().getName() : "---");

                calculateStats();
                calculateDamage();
            });

            //add listener for nature selection of defending Pokémon
            dmgCalcPokemon2Nature.valueProperty().addListener((observableNaturePokemon2, oldPokemonNature, newPokemonNature) -> {
                if(newPokemonNature == null){
                    return;
                }

                //show which stats are affected by the nature in the UI
                dmgCalcPokemon2NatureUpText.setText(newPokemonNature.getIncreased_stat() != null ? newPokemonNature.getIncreased_stat().getName() : "---");
                dmgCalcPokemon2NatureDownText.setText(newPokemonNature.getDecreased_stat() != null ? newPokemonNature.getDecreased_stat().getName() : "---");

                calculateStats();
                calculateDamage();
            });
        });

        //start service for getting Pokémon natures
        natureService.setNatureDexURL("https://pokeapi.co/api/v2/nature?limit=100");
        natureService.restart();
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

    /**
     * Handle adding or removing Pokemons from the team
     */
    @FXML
    protected void onTeamMemberAddOrRemoveBtnPress() {
        //check if Pokemon is already in team and update button function if it is or is not
        if (IsPokemonAlreadyInTeam())
        {
            //Pokemon is already in team -> remove it
            for (int i = 0; i < teamTexts.length; i++)
            {
                if (teamTexts[i].getText().equals(pokemonName.getText()))
                {
                    // remove pokemon in the according slot
                    UpdateTeamSlots(i);
                    break;
                }
            }
            //Updated text so the Pokemon can be added again
            addOrRemoveTeamMemberButton.setText("Add to team");
        }
        else
        {
            //Add Pokeom the the team
            if (currentTeamSize <= 5) {
                teamImages[currentTeamSize].setImage(pokemonImg.getImage());
                teamTexts[currentTeamSize].setText(pokemonName.getText());
                currentTeamSize++;
                addOrRemoveTeamMemberButton.setText("Remove from team");
            }
        }
    }

    /**
     * Each functtions for removing team members for each slot there is a dedicated slot number
     */
    @FXML
    protected void removeTeamMember0() {
        UpdateTeamSlots(0);
        if (currentTeamSize > 0)
            currentTeamSize--;
    }

    @FXML
    protected void removeTeamMember1() {
        UpdateTeamSlots(1);
        if (currentTeamSize > 0)
            currentTeamSize--;
    }

    @FXML
    protected void removeTeamMember2() {
        UpdateTeamSlots(2);
        if (currentTeamSize > 0)
            currentTeamSize--;
    }

    @FXML
    protected void removeTeamMember3() {
        UpdateTeamSlots(3);
        if (currentTeamSize > 0)
            currentTeamSize--;
    }

    @FXML
    protected void removeTeamMember4() {
        UpdateTeamSlots(4);
        if (currentTeamSize > 0)
            currentTeamSize--;
    }

    @FXML
    protected void removeTeamMember5() {
        UpdateTeamSlots(5);
        if (currentTeamSize > 0)
            currentTeamSize--;
    }

    private void UpdateTeamSlots(int startIndex) {
        //check if removed Pokemon is selected in the Pokedex
        if (teamTexts[startIndex].getText().equals(pokemonName.getText()))
            addOrRemoveTeamMemberButton.setText("Add to team");
        //Move Pokemon to the left
        int i = startIndex;
        for (; i < currentTeamSize - 1; i++) {
            teamImages[i].setImage(teamImages[i + 1].getImage());
            teamTexts[i].setText(teamTexts[i + 1].getText());
        }
        // Clear the last slot
        teamImages[currentTeamSize - 1].setImage(placeHolderSprite);
        teamTexts[currentTeamSize - 1].setText("");
    }

    /**
     * Helper methode to check if the pokemon is already in the team simple if, return false when Pokemon is not in the team
     */
    private boolean IsPokemonAlreadyInTeam() {
        for (int i = 0; i < teamTexts.length; i++) {
            if (teamTexts[i].getText().equals(pokemonName.getText()))
                return true;
        }
        return false;
    }

    @FXML
    private void handleExportTeam() {
        List<PokemonExportDTO> teamMembers = new ArrayList<>();

        //Array which contains the data of the Pokemons
        for (int i = 0; i < teamTexts.length; i++) {
            if (!teamTexts[i].getText().isEmpty()) {
                String pokemonName = teamTexts[i].getText();

                // Construct the Pokemon API URL
                String pokemonUrl = "https://pokeapi.co/api/v2/pokemon/" + pokemonName.toLowerCase();

                // Fetch detailed data for the Pokemon
                PokemonDTO detailedData = fetchPokemonDetails(pokemonUrl);

                //Extract relevant data
                if (detailedData != null) {
                    String imageUrl = detailedData.getSprites().getFront_default(); // Fetch image
                    String primaryType = detailedData.getTypes().get(0).getType().getName(); // Fetch type
                    String secondaryType = detailedData.getTypes().size() > 1
                            ? detailedData.getTypes().get(1).getType().getName()
                            : null;

                    // Add all Pokemon data to the export list
                    teamMembers.add(new PokemonExportDTO(
                            detailedData.getName(), imageUrl, primaryType, secondaryType, 0));
                }
            }
        }
        //Nothing to export
        if (teamMembers.isEmpty()) {
            System.err.println("No Pokemon in the team to export.");
            return;
        }

        // Export to HTML
        FileIO.exportTeamToHTML("PokemonTeam.html", teamMembers);
    }

    private PokemonDTO fetchPokemonDetails(String pokemonUrl) {
        // Use TcpConnectionHandler to fetch raw JSON data from the URL
        String rawJson = TcpConnectionHandler.getFromUrl(pokemonUrl);

        //Failed to fetch data
        if (rawJson == null || rawJson.isEmpty()) {
            System.err.println("Failed to fetch Pokemon details from URL: " + pokemonUrl);
            return null;
        }

        // Map the JSON data to a PokemonDTO object
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return jsonMapper.readValue(rawJson, PokemonDTO.class);
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing Pokémon details: " + e.getMessage());
            return null;
        }
    }

    @FXML
    private void handleExport() {
        // Get the selected Pokemon
        PokedexEntryDTO selectedPokemon = dexListView.getSelectionModel().getSelectedItem();

        if (selectedPokemon == null) {
            System.err.println("No Pokemon selected for export.");
            return;
        }

        // Use the Pokemons name as the file name
        String fileName = selectedPokemon.getName() + ".json";

        // Export the selected Pokemon to the desktop
        FileIO.exportToDesktop(fileName, selectedPokemon);
    }

    @FXML
    public void OpenSortingMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/fhtw/dexio/sorting/popupSorting.fxml"));
            Parent root = loader.load();

            SortingController sortingController = loader.getController();

            List<SortingEntryDTO> sortingEntries = pokedexEntries.stream()
                    .map(entry -> new SortingEntryDTO(entry.getName(), List.of())) // Ignore types if not needed
                    .toList();


            sortingController.setSortingEntries(sortingEntries);

            Stage popupStage = new Stage();
            popupStage.setTitle("Sorting Menu");
            popupStage.setScene(new Scene(root));
            popupStage.initOwner(((Stage) dexListView.getScene().getWindow()));
            popupStage.show();

        } catch (Exception e) {
            System.err.println("Error opening the sorting menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void typeSelectionHandler(TypeDTO primaryTypeSelect, TypeDTO secondaryTypeSelect) {
        //cancel any currently running service
        typeUiService.cancel();

        //deselect current type if it matches the type in the primary ComboBox (unless no primary or secondary type is selected)
        if((primaryTypeSelect != null || secondaryTypeSelect != null) && primaryTypeSelect == secondaryTypeSelect){
            primaryType.getSelectionModel().clearSelection();
            return;
        }

        if(primaryTypeSelect != null){
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
        }

        if(secondaryTypeSelect != null){
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
        }

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

        typeUiService.setParams(pokemonTypeList, damageRelations, primaryTypeSelect, secondaryTypeSelect);
        typeUiService.restart();
    }

    private void choiceHandler(){
        //execute calculation of damage when choosing different weather, stat stage boost, etc.
        calculateDamage();
    }

    private void intSpinnerHandler(String oldValue, String newValue, Spinner<Integer> spinner) {
        //set spinner value to 0 if null
        if(newValue == null){
            spinner.getEditor().setText("0");
            return;
        }
        if(newValue.equals(oldValue)){
            //no change
            return;
        }
        //check if spinner only contains numeric values
        if(!newValue.matches("^\\d+$")){
            //remove any non-numeric values in spinner
            newValue = newValue.replaceAll("\\D", "");

            //check if filtered string is a valid positive number
            if(newValue.isEmpty() || Integer.parseInt(newValue) < 0){
                //set 0 as default value if number is not valid
                spinner.getEditor().setText("0");
                return;
            }

            //set filtered string to replace current values (since this will trigger the listener again, the method may return here)
            spinner.getEditor().setText(newValue);
            return;
        }

        //spinner does not update its value properly if the arrow buttons are not used to update its value, therefore the value is manually committed here
        spinner.commitValue();

        calculateStats();
        calculateDamage();
    }

    private void setPokemon1BaseStats(Integer hp, Integer atk, Integer def, Integer spAtk, Integer spDef, Integer speed){
        hpBasePokemon1Text.setText(hp.toString());
        atkBasePokemon1Text.setText(atk.toString());
        defBasePokemon1Text.setText(def.toString());
        spAtkBasePokemon1Text.setText(spAtk.toString());
        spDefBasePokemon1Text.setText(spDef.toString());
        speedBasePokemon1Text.setText(speed.toString());
    }

    private void setPokemon1ResultStats(Integer hp, Integer atk, Integer def, Integer spAtk, Integer spDef, Integer speed){
        hpResultPokemon1Text.setText(hp.toString());
        atkResultPokemon1Text.setText(atk.toString());
        defResultPokemon1Text.setText(def.toString());
        spAtkResultPokemon1Text.setText(spAtk.toString());
        spDefResultPokemon1Text.setText(spDef.toString());
        speedResultPokemon1Text.setText(speed.toString());
    }

    private void setPokemon2BaseStats(Integer hp, Integer atk, Integer def, Integer spAtk, Integer spDef, Integer speed){
        hpBasePokemon2Text.setText(hp.toString());
        atkBasePokemon2Text.setText(atk.toString());
        defBasePokemon2Text.setText(def.toString());
        spAtkBasePokemon2Text.setText(spAtk.toString());
        spDefBasePokemon2Text.setText(spDef.toString());
        speedBasePokemon2Text.setText(speed.toString());
    }

    private void setPokemon2ResultStats(Integer hp, Integer atk, Integer def, Integer spAtk, Integer spDef, Integer speed){
        hpResultPokemon2Text.setText(hp.toString());
        atkResultPokemon2Text.setText(atk.toString());
        defResultPokemon2Text.setText(def.toString());
        spAtkResultPokemon2Text.setText(spAtk.toString());
        spDefResultPokemon2Text.setText(spDef.toString());
        speedResultPokemon2Text.setText(speed.toString());
    }

    private void setDmgCalcSpinnerValueFactories(){
        //hp value of defending Pokémon
        dmgCalcHpSpinValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0);
        dmgCalcHpSpin.setValueFactory(dmgCalcHpSpinValueFactory);
        dmgCalcHpSpin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, dmgCalcHpSpin));

        //individual values of attacking Pokémon
        hpIvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        hpIvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, hpIvPokemon1Spin));

        atkIvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        atkIvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, atkIvPokemon1Spin));

        defIvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        defIvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, defIvPokemon1Spin));

        spAtkIvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        spAtkIvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spAtkIvPokemon1Spin));

        spDefIvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        spDefIvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spDefIvPokemon1Spin));

        speedIvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        speedIvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, speedIvPokemon1Spin));

        //individual values of defending Pokémon
        hpIvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        hpIvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, hpIvPokemon2Spin));

        atkIvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        atkIvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, atkIvPokemon2Spin));

        defIvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        defIvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, defIvPokemon2Spin));

        spAtkIvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        spAtkIvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spAtkIvPokemon2Spin));

        spDefIvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        spDefIvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spDefIvPokemon2Spin));

        speedIvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 31, 31));
        speedIvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, speedIvPokemon2Spin));

        //effort values of attacking Pokémon
        hpEvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        hpEvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, hpEvPokemon1Spin));

        atkEvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        atkEvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, atkEvPokemon1Spin));

        defEvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        defEvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, defEvPokemon1Spin));

        spAtkEvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        spAtkEvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spAtkEvPokemon1Spin));

        spDefEvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        spDefEvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spDefEvPokemon1Spin));

        speedEvPokemon1Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        speedEvPokemon1Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, speedEvPokemon1Spin));

        //effort values of defending Pokémon
        hpEvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        hpEvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, hpEvPokemon2Spin));

        atkEvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        atkEvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, atkEvPokemon2Spin));

        defEvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        defEvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, defEvPokemon2Spin));

        spAtkEvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        spAtkEvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spAtkEvPokemon2Spin));

        spDefEvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        spDefEvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, spDefEvPokemon2Spin));

        speedEvPokemon2Spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 252, 0));
        speedEvPokemon2Spin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> intSpinnerHandler(oldValue, newValue, speedEvPokemon2Spin));

        dmgCalcPokemon1LevelSpin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 100));
        dmgCalcPokemon1LevelSpin.getEditor().textProperty().addListener((observable, oldLevel, newLevel) -> intSpinnerHandler(oldLevel, newLevel, dmgCalcPokemon1LevelSpin));

        dmgCalcPokemon2LevelSpin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 100));
        dmgCalcPokemon2LevelSpin.getEditor().textProperty().addListener((observable, oldLevel, newLevel) -> intSpinnerHandler(oldLevel, newLevel, dmgCalcPokemon2LevelSpin));
    }

    private Integer hpCalculation(Integer base, Integer iv, Integer ev, Integer level){
        //health points (hp) have their own calculation formula
        return ((2 * base + iv + (ev / 4)) * level / 100) + level + 10;
    }

    private Integer mainStatCalculation(Integer base, Integer iv, Integer ev, Integer level, NatureDTO nature, String statName){
        //used for calculation of attack, defense, special attack, special defense, and speed of any given Pokémon
        //calculate unmodified stats
        float unmodifiedStat = (float) ((2 * base + iv + (ev / 4)) * level / 100) + 5;
        if(nature.getIncreased_stat() == null || nature.getDecreased_stat() == null){
            //some natures are neutral and do not affect any stat
            return (int) unmodifiedStat;
        }

        if(nature.getIncreased_stat().getName().equals(statName)){
            //nature gives stat benefit of 10%
            return (int) (unmodifiedStat * 1.1);
        }
        else if(nature.getDecreased_stat().getName().equals(statName)){
            //nature gives stat deficit of -10%
            return (int) (unmodifiedStat * 0.9);
        }

        //if the above conditions do not apply, then this nature does not affect the currently checked stat
        return (int) unmodifiedStat;
    }

    private Boolean isBetween(double low, double high, double num){
        //return true if number is between low and (exclusive) high
        return (low <= num) && (num < high);
    }

    private void calculateStats(){
        //the stats of any Pokémon are calculated differently depending on the game generation, this calculation uses the latest formula for calculation of stats

        //calculate stats of attacking Pokémon if one is currently active
        if(dmgCalcCurrentPokemon1 != null){
            Integer hpResult1 = hpCalculation(
                    Integer.parseInt(hpBasePokemon1Text.getText()),
                    hpIvPokemon1Spin.getValue(),
                    hpEvPokemon1Spin.getValue(),
                    dmgCalcPokemon1LevelSpin.getValue()
            );

            Integer atkResult1 = mainStatCalculation(
                    Integer.parseInt(atkBasePokemon1Text.getText()),
                    atkIvPokemon1Spin.getValue(),
                    atkEvPokemon1Spin.getValue(),
                    dmgCalcPokemon1LevelSpin.getValue(),
                    dmgCalcPokemon1Nature.getValue(),
                    "Attack"
            );

            Integer defResult1 = mainStatCalculation(
                    Integer.parseInt(defBasePokemon1Text.getText()),
                    defIvPokemon1Spin.getValue(),
                    defEvPokemon1Spin.getValue(),
                    dmgCalcPokemon1LevelSpin.getValue(),
                    dmgCalcPokemon1Nature.getValue(),
                    "Defense"
            );

            Integer spAtkResult1 = mainStatCalculation(
                    Integer.parseInt(spAtkBasePokemon1Text.getText()),
                    spAtkIvPokemon1Spin.getValue(),
                    spAtkEvPokemon1Spin.getValue(),
                    dmgCalcPokemon1LevelSpin.getValue(),
                    dmgCalcPokemon1Nature.getValue(),
                    "Special-attack"
            );

            Integer spDefResult1 = mainStatCalculation(
                    Integer.parseInt(spDefBasePokemon1Text.getText()),
                    spDefIvPokemon1Spin.getValue(),
                    spDefEvPokemon1Spin.getValue(),
                    dmgCalcPokemon1LevelSpin.getValue(),
                    dmgCalcPokemon1Nature.getValue(),
                    "Special-defense"
            );

            Integer speedResult1 = mainStatCalculation(
                    Integer.parseInt(speedBasePokemon1Text.getText()),
                    speedIvPokemon1Spin.getValue(),
                    speedEvPokemon1Spin.getValue(),
                    dmgCalcPokemon1LevelSpin.getValue(),
                    dmgCalcPokemon1Nature.getValue(),
                    "Speed"
            );

            setPokemon1ResultStats(hpResult1, atkResult1, defResult1, spAtkResult1, spDefResult1, speedResult1);
        }

        //calculate stats of defending Pokémon if one is currently active
        if(dmgCalcCurrentPokemon2 != null){
            Integer hpResult2 = hpCalculation(
                    Integer.parseInt(hpBasePokemon2Text.getText()),
                    hpIvPokemon2Spin.getValue(),
                    hpEvPokemon2Spin.getValue(),
                    dmgCalcPokemon2LevelSpin.getValue()
            );

            Integer atkResult2 = mainStatCalculation(
                    Integer.parseInt(atkBasePokemon2Text.getText()),
                    atkIvPokemon2Spin.getValue(),
                    atkEvPokemon2Spin.getValue(),
                    dmgCalcPokemon2LevelSpin.getValue(),
                    dmgCalcPokemon2Nature.getValue(),
                    "Attack"
            );

            Integer defResult2 = mainStatCalculation(
                    Integer.parseInt(defBasePokemon2Text.getText()),
                    defIvPokemon2Spin.getValue(),
                    defEvPokemon2Spin.getValue(),
                    dmgCalcPokemon2LevelSpin.getValue(),
                    dmgCalcPokemon2Nature.getValue(),
                    "Defense"
            );

            Integer spAtkResult2 = mainStatCalculation(
                    Integer.parseInt(spAtkBasePokemon2Text.getText()),
                    spAtkIvPokemon2Spin.getValue(),
                    spAtkEvPokemon2Spin.getValue(),
                    dmgCalcPokemon2LevelSpin.getValue(),
                    dmgCalcPokemon2Nature.getValue(),
                    "Special-attack"
            );

            Integer spDefResult2 = mainStatCalculation(
                    Integer.parseInt(spDefBasePokemon2Text.getText()),
                    spDefIvPokemon2Spin.getValue(),
                    spDefEvPokemon2Spin.getValue(),
                    dmgCalcPokemon2LevelSpin.getValue(),
                    dmgCalcPokemon2Nature.getValue(),
                    "Special-defense"
            );

            Integer speedResult2 = mainStatCalculation(
                    Integer.parseInt(speedBasePokemon2Text.getText()),
                    speedIvPokemon2Spin.getValue(),
                    speedEvPokemon2Spin.getValue(),
                    dmgCalcPokemon2LevelSpin.getValue(),
                    dmgCalcPokemon2Nature.getValue(),
                    "Speed"
            );

            setPokemon2ResultStats(hpResult2, atkResult2, defResult2, spAtkResult2, spDefResult2, speedResult2);

            //reset minimum and maximum hp for spinner upon recalculation of stats
            dmgCalcHpSpinValueFactory.setMin(1);
            dmgCalcHpSpinValueFactory.setMax(hpResult2);

            //set hp to maximum if a new defending Pokémon is chosen
            if(newPokemon2Chosen){
                dmgCalcHpSpinValueFactory.setValue(hpResult2);
                //re-enable spinner
                dmgCalcHpSpin.setDisable(false);
                //unset Pokémon chosen flag
                newPokemon2Chosen = false;
            }

            //update hp-bar
            double hpRatio = (double) dmgCalcHpSpin.getValue() / hpResult2;
            dmgCalcHpBar.setProgress(hpRatio);
            if(hpRatio >= 0.5){
                dmgCalcHpBar.styleProperty().setValue("-fx-accent: lime");
            }
            else if(isBetween(0.2, 0.5, hpRatio)){
                dmgCalcHpBar.styleProperty().setValue("-fx-accent: orange");
            }
            else if(isBetween(0, 0.2, hpRatio)){
                dmgCalcHpBar.styleProperty().setValue("-fx-accent: red");
            }
        }
    }

    private List<Double> applyMultipliers(Double initialDamage){
        if(dmgCalcWeatherChoice.getSelectionModel().getSelectedIndex() == 1){
            //harsh sunlight gives fire type moves a boost while weakening water type moves
            if(dmgCalcMoveTypeText.getText().equals("Fire")){
                initialDamage *= 1.5;
            }
            else if(dmgCalcMoveTypeText.getText().equals("Water")){
                initialDamage *= 0.5;
            }
        }
        else if(dmgCalcWeatherChoice.getSelectionModel().getSelectedIndex() == 2){
            //rain boosts water type moves while weakening fire type moves
            if(dmgCalcMoveTypeText.getText().equals("Fire")){
                initialDamage *= 0.5;
            }
            else if(dmgCalcMoveTypeText.getText().equals("Water")){
                initialDamage *= 1.5;
            }
        }

        //after each division or multiplication, the damage is rounded down
        initialDamage = Math.floor(initialDamage);

        //damage is multiplied with a random value between 0.85 and 1 at this point in the formula, only the minimum and maximum values are relevant for the user
        List<Double> damageRange = Arrays.asList(initialDamage * 0.85, initialDamage);

        //round down
        damageRange.set(0, Math.floor(damageRange.getFirst()));
        damageRange.set(1, Math.floor(damageRange.getLast()));

        //same-type-attack-bonus (STAB) is applied if move type equals one of the types of the attacking Pokémon
        //check if primary type is equal to move type
        if(dmgCalcMoveTypeText.getText().equals(dmgCalcCurrentPokemon1.getTypes().getFirst().getType().getName())){
            damageRange.set(0, damageRange.getFirst() * 1.5);
            damageRange.set(1, damageRange.getLast() * 1.5);
        }
        else if(dmgCalcCurrentPokemon1.getTypes().size() == 2){
            //check if secondary type is equal to move type
            if(dmgCalcMoveTypeText.getText().equals(dmgCalcCurrentPokemon1.getTypes().get(1).getType().getName())){
                damageRange.set(0, damageRange.getFirst() * 1.5);
                damageRange.set(1, damageRange.getLast() * 1.5);
            }
        }

        //round down
        damageRange.set(0, Math.floor(damageRange.getFirst()));
        damageRange.set(1, Math.floor(damageRange.getLast()));

        //get type multiplier (type id is read as [id - 1] because the index in damageRelations starts at 0)
        Float typeMultiplier1 = damageRelations.get(pokemonTypeMap.get(dmgCalcMoveTypeText.getText()).getId() - 1).get(pokemonTypeMap.get(dmgCalcCurrentPokemon2.getTypes().getFirst().getType().getName()).getId() - 1);
        damageRange.set(0, damageRange.getFirst() * typeMultiplier1);
        damageRange.set(1, damageRange.getLast() * typeMultiplier1);
        //round down
        damageRange.set(0, Math.floor(damageRange.getFirst()));
        damageRange.set(1, Math.floor(damageRange.getLast()));
        //if the defending Pokémon has a secondary type, the damage relation has to be checked for this type too (this way a Pokémon may take 4x or 0.25x the amount of damage depending on the type)
        if(dmgCalcCurrentPokemon2.getTypes().size() == 2){
            Float typeMultiplier2 = damageRelations.get(pokemonTypeMap.get(dmgCalcMoveTypeText.getText()).getId() - 1).get(pokemonTypeMap.get(dmgCalcCurrentPokemon2.getTypes().get(1).getType().getName()).getId() - 1);
            damageRange.set(0, damageRange.getFirst() * typeMultiplier2);
            damageRange.set(1, damageRange.getLast() * typeMultiplier2);
            //round down
            damageRange.set(0, Math.floor(damageRange.getFirst()));
            damageRange.set(1, Math.floor(damageRange.getLast()));
        }

        //if a physical move is used while having the "Burned" status condition, the damage is effectively halved
        if(dmgCalcMoveCatText.getText().equals("Physical") && dmgCalcBurnedCheckBox.isSelected()){
            damageRange.set(0, damageRange.getFirst() * 0.5);
            damageRange.set(1, damageRange.getLast() * 0.5);
        }

        //round down
        damageRange.set(0, Math.floor(damageRange.getFirst()));
        damageRange.set(1, Math.floor(damageRange.getLast()));

        return damageRange;
    }

    private void calculateDamage(){
        if(!validityCheck()){
            //not all required fields for calculation (e.g. move attributes) are valid yet or are still loading
            return;
        }

        //damage formulas differ between game generations, this calculation uses the latest formula for damage calculation
        /*simplifications are applied to this formula, which include:
        - only one Pokémon is targeted with the move (no split damage if the move hits multiple targets)
        - no field conditions like electric terrain, psychic terrain, etc.
        - only two weather conditions are implemented (harsh sunlight and rain)
        - secondary effects of moves are not considered
        - no critical hits
        - no item effects
        - no ability effects
        - it is assumed the move will hit (regardless of accuracy)
        - both Pokémon are assumed to not have their typing changed
        - moves without power such as status moves are assumed to deal no damage (this includes one-hit KO moves)
         */
        double adRatio;
        if(dmgCalcMoveCatText.getText().equals("Status") || Integer.parseInt(dmgCalcMovePwrText.getText()) == 0){
            dmgCalcResultText.setText("No damage");
            return;
        }
        else if(dmgCalcMoveCatText.getText().equals("Physical")){
            //use attack (* attack boost) and defense (* defense boost) to calculate damage output
            adRatio = (double) Integer.parseInt(atkResultPokemon1Text.getText()) * statStageMap.get(Integer.parseInt(dmgCalcAtkBoostChoice.getValue())) /
                    (Integer.parseInt(defResultPokemon2Text.getText()) * statStageMap.get(Integer.parseInt(dmgCalcDefBoostChoice.getValue())));
        }
        else if(dmgCalcMoveCatText.getText().equals("Special")){
            //use special attack (* special attack boost) and special defense (* special defense boost) to calculate damage output
            adRatio = (double) Integer.parseInt(spAtkResultPokemon1Text.getText()) * statStageMap.get(Integer.parseInt(dmgCalcSpAtkBoostChoice.getValue())) /
                    (Integer.parseInt(spDefResultPokemon2Text.getText()) * statStageMap.get(Integer.parseInt(dmgCalcSpDefBoostChoice.getValue())));
        }
        else{
            throw new RuntimeException("Damage category could not be identified");
        }

        double initialDamage = ((((2.0 * dmgCalcPokemon1LevelSpin.getValue()) / 5.0) + 2.0) * Double.parseDouble(dmgCalcMovePwrText.getText()) * adRatio / 50.0) + 2.0;
        //round damage
        initialDamage = Math.floor(initialDamage);

        List<Double> damageRange = applyMultipliers(initialDamage);
        int finalLowRollDamage = damageRange.getFirst().intValue();
        int finalHighRollDamage = damageRange.getLast().intValue();

        //print damage numbers on UI (truncate past first decimal)
        String lowRollDamagePercent = String.format("%.1f", Math.floor((float) finalLowRollDamage / dmgCalcHpSpin.getValue() * 1000.0) / 10.0);
        String highRollDamagePercent = String.format("%.1f", Math.floor((float) finalHighRollDamage / dmgCalcHpSpin.getValue() * 1000.0) / 10.0);
        dmgCalcResultText.setText(finalLowRollDamage + " - " + finalHighRollDamage + "HP (" + lowRollDamagePercent + " - " + highRollDamagePercent + "%)");

        //print additional information on UI
        //calculate the ratio of damage dealt (ranges from around 0 to any value > 0 with 1 and above meaning a move will knock out the opposing Pokémon in one hit)
        float lowRollDamageRatio = (float) finalLowRollDamage / dmgCalcHpSpin.getValue();
        float highRollDamageRatio = (float) finalHighRollDamage / dmgCalcHpSpin.getValue();

        //check how many hits it would take to knock out the opposing Pokémon under the same conditions
        if(highRollDamageRatio == 0){
            //attack does not deal any damage (e.g. in this calculation only through immunities)
            dmgCalcResultText.setText(" --> immune");
        }
        if(isBetween(0, 1.0/10.0, highRollDamageRatio)){
            //attack will not even KO after being used 10 times on the same Pokémon
            dmgCalcResultCommentText.setText(" --> very little damage");
        }
        else if(isBetween(1.0/10.0, 1.0/9.0, highRollDamageRatio)){
            //check if low roll damage falls under the same range
            if(isBetween(1.0/10.0, 1.0/9.0, lowRollDamageRatio)){
                //if both are in the same range, the attack is guaranteed to 10-hit KO
                dmgCalcResultCommentText.setText(" --> guaranteed 10HKO");
            }
            else{
                //otherwise it is merely possible with luck
                dmgCalcResultCommentText.setText(" --> possible 10HKO");
            }
        }
        else if(isBetween(1.0/9.0, 1.0/8.0, highRollDamageRatio)){
            //apply the same pattern for 9-hit KO, 8-hit KO, etc.
            if(isBetween(1.0/9.0, 1.0/8.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 9HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 9HKO");
            }
        }
        else if(isBetween(1.0/8.0, 1.0/7.0, highRollDamageRatio)){
            if(isBetween(1.0/8.0, 1.0/7.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 8HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 8HKO");
            }
        }
        else if(isBetween(1.0/7.0, 1.0/6.0, highRollDamageRatio)){
            if(isBetween(1.0/7.0, 1.0/6.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 7HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 7HKO");
            }
        }
        else if(isBetween(1.0/6.0, 1.0/5.0, highRollDamageRatio)){
            if(isBetween(1.0/6.0, 1.0/5.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 6HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 6HKO");
            }
        }
        else if(isBetween(1.0/5.0, 1.0/4.0, highRollDamageRatio)){
            if(isBetween(1.0/5.0, 1.0/4.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 5HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 5HKO");
            }
        }
        else if(isBetween(1.0/4.0, 1.0/3.0, highRollDamageRatio)){
            if(isBetween(1.0/4.0, 1.0/3.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 4HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 4HKO");
            }
        }
        else if(isBetween(1.0/3.0, 1.0/2.0, highRollDamageRatio)){
            if(isBetween(1.0/3.0, 1.0/2.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 3HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 3HKO");
            }
        }
        else if(isBetween(1.0/2.0, 1.0, highRollDamageRatio)){
            if(isBetween(1.0/2.0, 1.0, lowRollDamageRatio)){
                dmgCalcResultCommentText.setText(" --> guaranteed 2HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 2HKO");
            }
        }
        else if(highRollDamageRatio >= 1){
            if(lowRollDamageRatio >= 1){
                dmgCalcResultCommentText.setText(" --> guaranteed 1HKO");
            }
            else{
                dmgCalcResultCommentText.setText(" --> possible 1HKO");
            }
        }
    }

    private Boolean validityCheck(){
        //check for any invalid values used for calculation
        return !(dmgCalcCurrentPokemon1 == null) &&
                !(dmgCalcCurrentPokemon2 == null) &&
                !(dmgCalcHpSpin == null) &&
                !hpResultPokemon1Text.getText().isEmpty() &&
                !atkResultPokemon1Text.getText().isEmpty() &&
                !defResultPokemon1Text.getText().isEmpty() &&
                !spAtkResultPokemon1Text.getText().isEmpty() &&
                !spDefResultPokemon1Text.getText().isEmpty() &&
                !speedResultPokemon1Text.getText().isEmpty() &&
                !hpResultPokemon2Text.getText().isEmpty() &&
                !atkResultPokemon2Text.getText().isEmpty() &&
                !defResultPokemon2Text.getText().isEmpty() &&
                !spAtkResultPokemon2Text.getText().isEmpty() &&
                !spDefResultPokemon2Text.getText().isEmpty() &&
                !speedResultPokemon2Text.getText().isEmpty() &&
                !dmgCalcMoveTypeText.getText().isEmpty() &&
                !dmgCalcMoveTypeText.getText().equals("...") &&
                !dmgCalcMoveCatText.getText().isEmpty() &&
                !dmgCalcMoveCatText.getText().equals("...") &&
                !dmgCalcMovePwrText.getText().isEmpty() &&
                !dmgCalcMovePwrText.getText().equals("...") &&
                !dmgCalcWeatherChoice.getValue().isEmpty() &&
                !(dmgCalcPokemon1Nature.getValue() == null) &&
                !(dmgCalcPokemon2Nature.getValue() == null) &&
                !dmgCalcAtkBoostChoice.getValue().isEmpty() &&
                !dmgCalcDefBoostChoice.getValue().isEmpty() &&
                !dmgCalcSpAtkBoostChoice.getValue().isEmpty() &&
                !dmgCalcSpDefBoostChoice.getValue().isEmpty() &&
                !(dmgCalcPokemon1LevelSpin.getValue() == null) &&
                !(dmgCalcPokemon2LevelSpin.getValue() == null);
    }
}