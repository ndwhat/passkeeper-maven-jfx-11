package com.stendyx.passkeeper.passkeeper.properties;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum Images {

    BACKGROUND("/img/background.png"),
    SUCC("/img/succ.png"),
    NSUCC("/img/nsucc.png"),
    SEARCH("/img/search.png");


    private String path;

    Images(String path) {
        this.path = path;
    }


    public ImageView getImageView() {

        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);

        return imageView;
    }

    public ImageView getImageView(int width, int height) {

        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);


        return imageView;
    }

}