package at.fhtw.dexio.services;

import at.fhtw.dexio.pokemontypes.TypeDTO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TypeUIService extends Service<List<List<Pane>>> {
    private final List<List<Pane>> typeContainers = new ArrayList<>(12);
    private List<TypeDTO> pokemonTypeList;
    private List<List<Float>> damageRelations;
    private TypeDTO primaryTypeSelect;
    private TypeDTO secondaryTypeSelect;

    public void setParams(List<TypeDTO> pokemonTypeList, List<List<Float>> damageRelations, TypeDTO primaryTypeSelect, TypeDTO secondaryTypeSelect) {
        this.pokemonTypeList = pokemonTypeList;
        this.damageRelations = damageRelations;
        this.primaryTypeSelect = primaryTypeSelect;
        this.secondaryTypeSelect = secondaryTypeSelect;
    }

    private Pane buildTypeGuiContainer(Integer typeIndex, Float multiplier){
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

    private void damageTakenHandler(Task<List<List<Pane>>> task, int attackTypeIndex, float multiplier) {
        Pane typeContainer = buildTypeGuiContainer(attackTypeIndex, multiplier);

        if(multiplier > 1){
            if(task.isCancelled()){
                return;
            }
            typeContainers.getFirst().add(typeContainer);
        }
        else if(multiplier < 1 && multiplier > 0){
            if(task.isCancelled()){
                return;
            }
            typeContainers.get(1).add(typeContainer);
        }
        else if(multiplier == 0){
            if(task.isCancelled()){
                return;
            }
            typeContainers.get(2).add(typeContainer);
        }
        else {
            if(task.isCancelled()){
                return;
            }
            typeContainers.get(3).add(typeContainer);
        }
    }

    private void damageGivenHandler(Task<List<List<Pane>>> task){
        Pane typeContainer;
        Float multiplier;

        if(primaryTypeSelect != null){
            //fetch sublist containing attack multipliers for the currently chosen primary type
            List<Float> primAttackMultipliers = damageRelations.get(primaryTypeSelect.getId() - 1);
            for(int i = 0; i < primAttackMultipliers.size(); i++){
                multiplier = primAttackMultipliers.get(i);

                if(multiplier > 1){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(4).add(typeContainer);
                }
                else if(multiplier < 1 && multiplier > 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(5).add(typeContainer);
                }
                else if(multiplier == 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(6).add(typeContainer);
                }
                else {
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(7).add(typeContainer);
                }
            }
        }

        if(secondaryTypeSelect != null){
            //fetch sublist containing attack multipliers for the currently chosen secondary type
            List<Float> secAttackMultipliers = damageRelations.get(secondaryTypeSelect.getId() - 1);
            for(int i = 0; i < secAttackMultipliers.size(); i++){
                multiplier = secAttackMultipliers.get(i);

                if(multiplier > 1){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(8).add(typeContainer);
                }
                else if(multiplier < 1 && multiplier > 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(9).add(typeContainer);
                }
                else if(multiplier == 0){
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(10).add(typeContainer);
                }
                else {
                    typeContainer = buildTypeGuiContainer(i, multiplier);
                    if(task.isCancelled()){
                        return;
                    }
                    typeContainers.get(11).add(typeContainer);
                }
            }
        }
    }

    @Override
    protected Task<List<List<Pane>>> createTask() {
        return new Task<>() {
            protected List<List<Pane>> call() {
                //fill all spaces to access and add UI elements to inner lists
                while(typeContainers.size() < 12){
                    typeContainers.add(new ArrayList<>());
                }

                //clear the inner lists each service restart
                for(List<Pane> typeContainer : typeContainers) {
                    typeContainer.clear();
                }

                //start handler which updates damage effectiveness in GUI for primary and secondary type
                damageGivenHandler(this);

                if(primaryTypeSelect != null && secondaryTypeSelect != null){
                    //both primary and secondary type are selected
                    //iterate through damage table and add type weaknesses, resistances, and immunities to the corresponding part in the GUI
                    for(int i = 0; i < damageRelations.size(); i++){
                        float multiplier = damageRelations.get(i).get(primaryTypeSelect.getId() - 1) * damageRelations.get(i).get(secondaryTypeSelect.getId() - 1);
                        if(isCancelled()){
                            return null;
                        }
                        damageTakenHandler(this, i, multiplier);
                    }
                }
                else if(primaryTypeSelect != null){
                    //only primary type is currently selected
                    //iterate through damage table and add type weaknesses, resistances, and immunities to the corresponding part in the GUI
                    for(int i = 0; i < damageRelations.size(); i++){
                        float multiplier = damageRelations.get(i).get(primaryTypeSelect.getId() - 1);
                        if(isCancelled()){
                            return null;
                        }
                        damageTakenHandler(this, i, multiplier);
                    }
                }
                else if(secondaryTypeSelect != null){
                    //only secondary type is currently selected
                    //iterate through damage table and add type weaknesses, resistances, and immunities to the corresponding part in the GUI
                    for(int i = 0; i < damageRelations.size(); i++){
                        float multiplier = damageRelations.get(i).get(secondaryTypeSelect.getId() - 1);
                        if(isCancelled()){
                            return null;
                        }
                        damageTakenHandler(this, i, multiplier);
                    }
                }
                //if no condition applies, no type is selected
                return typeContainers;
            }
        };
    }
}
