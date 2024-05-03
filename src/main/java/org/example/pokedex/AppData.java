package org.example.pokedex;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppData implements Initializable {

    @FXML
    private GridPane gridpane;

    @FXML
    private ScrollPane scrollpane;
    @FXML
    private Button details;
    private List<PokemonModel> pokemons = new ArrayList<>();

    public List<PokemonModel> getPokemon() {
        List<PokemonModel> pokemons = new ArrayList<>();
        PokemonModel pokemon;
        Connection connection = DbController.getInstance();
        try {
            String sql = "SELECT * FROM POKEMON";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int pokID = resultSet.getInt("ID");
                String pokName = resultSet.getString("Name");
                String type1 = resultSet.getString("Type 1");
                String type2 = resultSet.getString("Type 2");
                int total = resultSet.getInt("Total");
                int hp = resultSet.getInt("HP");
                int attack = resultSet.getInt("Attack");
                int defense = resultSet.getInt("Defense");
                int spAttack = resultSet.getInt("Sp. Atk");
                int spDefense = resultSet.getInt("Sp. Def");
                int speed = resultSet.getInt("Speed");
                int generation = resultSet.getInt("Generation");
                String legend = resultSet.getString("Legendary");
                boolean legendary;
                legendary = !legend.equals("False");
                pokemon = new PokemonModel(pokID, pokName, type1, type2, total, hp, attack, defense, spAttack, spDefense, speed, generation, legendary);
                pokemons.add(pokemon);
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("App data: " + e);
        }

        return pokemons;
    }

    public void print(PokemonModel pokemon) {
        System.out.println(pokemon.pokID);
        System.out.println(pokemon.pokName);
        System.out.println(pokemon.attack);
        System.out.println();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pokemons.addAll(getPokemon());
//        print(pokemons.get(5));
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < pokemons.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("pokemonCard.fxml"));
                AnchorPane anchorpane = fxmlLoader.load();
                pkController pokcontroller = fxmlLoader.getController();
                pokcontroller.setData(pokemons.get(i));

                if (column == 3) {
                    column = 0;
                    row++;
                }
                gridpane.add(anchorpane, column++, row);
                GridPane.setMargin(anchorpane, new Insets(10));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
