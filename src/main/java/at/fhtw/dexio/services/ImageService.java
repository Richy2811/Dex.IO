package at.fhtw.dexio.services;

import at.fhtw.dexio.DexIOController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * A service which, when started with a valid URL set, will return an {@link Image}
 * when the task is finished.
 */
public class ImageService extends Service<Image> {
    private String imgURL;

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Override
    protected Task<Image> createTask() {
        return new Task<>() {
            protected Image call() {
                if(imgURL == null) {
                    //return image not found
                    return new Image(Objects.requireNonNull(DexIOController.class.getResource("images/Not_Found_Sprite.png")).toString(), true);
                }
                else{
                    return new Image(imgURL);
                }
            }
        };
    }
}
