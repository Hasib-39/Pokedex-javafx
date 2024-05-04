package org.example.pokedex;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class pkController {
    @FXML
    private Button details;
    @FXML
    private Label idLabel;
    @FXML
    private ImageView img;
    @FXML
    private Label name;
    @FXML
    private Button UnfavBtn;
    @FXML
    private Button favBtn;
    private PokemonModel pokemon;
    private final Map<String, String> color_map = new HashMap<>();

    public void initColor() {
        color_map.put("Normal", "#A8A77A");
        color_map.put("Fire", "#EE8130");
        color_map.put("Water", "#6390F0");
        color_map.put("Electric", "#F7D02C");
        color_map.put("Grass", "#7AC74C");
        color_map.put("Ice", "#96D9D6");
        color_map.put("Fighting", "#C22E28");
        color_map.put("Poison", "#A33EA1");
        color_map.put("Ground", "#E2BF65");
        color_map.put("Flying", "#A98FF3");
        color_map.put("Psychic", "#F95587");
        color_map.put("Bug", "#A6B91A");
        color_map.put("Rock", "#B6A136");
        color_map.put("Ghost", "#735797");
        color_map.put("Dragon", "#6F35FC");
        color_map.put("Dark", "#705746");
        color_map.put("Steel", "#B7B7CE");
        color_map.put("Fairy", "#D685AD");
    }

    public void setData(PokemonModel pokemon) {
        this.pokemon = pokemon;
        idLabel.setText(Integer.toString(pokemon.pokID));
        name.setText(pokemon.pokName);
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

        favBtn.setOnAction((event) -> {
            favourite(pokemon, true);
        });
        UnfavBtn.setOnAction((event) -> {
            favourite(pokemon, false);
        });
    }

    public void onButtonClick(PokemonModel pokemon) throws IOException {
        initColor();

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pokemonScene.fxml"));
        Parent root = fxmlLoader.load();
        root.setStyle("-fx-background-color: " + color_map.get(pokemon.type1) + ";");
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


    public void favourite(PokemonModel pokemon, boolean value) {
        Connection connection = DbController.getInstance();
        String update_query = "UPDATE POKEMON SET FAVOURITE = ? WHERE ID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(update_query);
            if (value) {
                statement.setString(1, "True");
                statement.setInt(2, pokemon.pokID);
            } else {
                statement.setString(1, "False");
                statement.setInt(2, pokemon.pokID);
            }

            int selection_update = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
