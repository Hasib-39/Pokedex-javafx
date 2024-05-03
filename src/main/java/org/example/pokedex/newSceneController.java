package org.example.pokedex;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class newSceneController extends AnchorPane {

    @FXML
    private AnchorPane anchor;
    @FXML
    private ImageView img;
    @FXML
    private ProgressBar atkBar;

    @FXML
    private ProgressBar defBar;

    @FXML
    private Label genLabel;

    @FXML
    private ProgressBar hpBar;

    @FXML
    private Label legendBar;

    @FXML
    private Label nameLabel;

    @FXML
    private Label pokID;

    @FXML
    private ProgressBar spatkBar;

    @FXML
    private ProgressBar spdBar;

    @FXML
    private ProgressBar spdefBar;

    @FXML
    private ProgressBar totalBar;

    @FXML
    private Label type1_label;

    @FXML
    private Label type2_label;

    public void setAnchor(AnchorPane anchor) {
        this.anchor.getChildren().add(anchor);
    }

    public void setAtkBar(double value) {
        atkBar.setProgress(value);
    }

    public void setDefBar(double value) {
        defBar.setProgress(value);
    }

    public void setGenLabel(String text) {
        genLabel.setText(text);
    }

    public void setHpBar(double value) {
        hpBar.setProgress(value);
    }

    public void setLegendBar(String text) {
        legendBar.setText(text);
    }

    public void setNameLabel(String text) {
        nameLabel.setText(text);
    }

    public void setPokID(String text) {
        pokID.setText(text);
    }

    public void setSpatkBar(double value) {
        spatkBar.setProgress(value);
    }

    public void setSpdBar(double value) {
        spdBar.setProgress(value);
    }

    public void setSpdefBar(double value) {
        spdefBar.setProgress(value);
    }

    public void setTotalBar(double value) {
        totalBar.setProgress(value);
    }

    public void setType1_label(String text) {
        type1_label.setText(text);
    }

    public void setType2_label(String text) {
        type2_label.setText(text);
    }

    public void setImg(Image image) {
        img.setImage(image);
    }

    public void setWrapNameLabel(boolean t) {
        nameLabel.setWrapText(t);
    }
}
