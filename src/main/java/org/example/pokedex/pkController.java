package org.example.pokedex;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class pkController {
    @FXML
    private Button details;
    @FXML
    private Label idLabel;
    @FXML
    private ImageView img;
    @FXML
    private Label nameLabel;
    @FXML
//    private RadioButton favRadio;

    private PokemonModel pokemon;

    public void setData(PokemonModel pokemon) {
        this.pokemon = pokemon;
        idLabel.setText(Integer.toString(pokemon.pokID));
        nameLabel.setText(pokemon.pokName);
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pokemon.getImgSrc())));
            img.setImage(image);
        } catch (Exception e) {
            // Image not found, load default image (bulbasaur.png)
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("question.png")));
            img.setImage(defaultImage);
        }
        details.setOnAction((event) -> {
            try {
                onButtonClick(pokemon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onButtonClick(PokemonModel pokemon) throws IOException {
        System.out.println(pokemon.pokName);

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pokemonScene.fxml"));
        Parent root = fxmlLoader.load();

        newSceneController newscctrl = fxmlLoader.getController();
        newscctrl.setPokID("Pokemon ID: " + pokemon.pokID);
        newscctrl.setNameLabel(pokemon.pokName);
        newscctrl.setWrapNameLabel(true);
        newscctrl.setType1_label(pokemon.type1);
        newscctrl.setType2_label(pokemon.type2);
        newscctrl.setTotalBar((double) pokemon.total / 800);
        newscctrl.setHpBar((double) pokemon.hp / 300);
        newscctrl.setAtkBar((double) pokemon.attack / 200);
        newscctrl.setDefBar((double) pokemon.defense / 250);
        newscctrl.setSpatkBar((double) pokemon.spAttack / 200);
        newscctrl.setSpdefBar((double) pokemon.spDefense / 250);
        newscctrl.setSpdBar((double) pokemon.speed / 200);
        newscctrl.setGenLabel("Generation: " + pokemon.generation);
        newscctrl.setLegendBar("Legendary: " + pokemon.legendary);
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(pokemon.getImgSrc())));
            newscctrl.setImg(image);
        } catch (Exception e) {
            // Image not found, load default image (bulbasaur.png)
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("question.png")));
            newscctrl.setImg(defaultImage);
        }

        String cssFile = Objects.requireNonNull(HelloApplication.class.getResource("styles.css")).toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(cssFile);

        Stage stage = new Stage();
        stage.setTitle("Pokedex");
        stage.setScene(scene);
        stage.show();
    }

//    public void favourite(PokemonModel pokemon) {
//
//    }
}
